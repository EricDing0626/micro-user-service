# 性能优化报告

> 对应任务 5-9  
> 作者：Eric  
> 日期：2026-06-17

## 一、慢查询分析

### 1.1 开启 MySQL 慢查询日志

```sql
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;
SET GLOBAL slow_query_log_file = '/tmp/mysql-slow.log';
```

或在 `my.cnf` 中配置：

```ini
[mysqld]
slow_query_log = 1
long_query_time = 1
slow_query_log_file = /var/log/mysql/slow.log
```

### 1.2 重点观察的 SQL

| 场景 | 典型 SQL | 优化方向 |
|------|----------|----------|
| 用户分页列表 | SELECT ... FROM sys_user WHERE deleted=0 | 只查列表所需字段，不查 password |
| 按类型查基础数据 | SELECT ... FROM sys_base_data WHERE type_code=? | type_code 索引 + @Cacheable |
| 最近操作日志 | SELECT ... FROM sys_operate_log WHERE operator=? ORDER BY id DESC | operator + operate_time 联合索引 |

### 1.3 分析命令

```bash
mysqldumpslow -s t -t 10 /tmp/mysql-slow.log
```

---

## 二、索引优化

| 表 | 字段 | 说明 |
|----|------|------|
| sys_user | username | 已有 `uk_username` 唯一索引 |
| sys_operate_log | operator, operate_time | 新增 `idx_operator_operate_time` 联合索引 |

已有库执行：

```bash
mysql -uroot micro_user < sql/optimize-index.sql
```

> 任务文档中的 `user_id` 字段在本项目中对应 `operator`（用户名），已在联调报告中说明。

---

## 三、分页查询优化

**优化前：** 分页使用 MyBatis-Plus 默认 `selectPage`，会查询 `sys_user` 全部字段（含 password）。

**优化后：** 列表查询通过 `LambdaQueryWrapper.select(...)` 只查询：

- id, username, email, phone, status, create_time, update_time

密码字段不再出现在分页 SQL 中，减少 IO 与内存占用。

---

## 四、AOP 日志异步写入

**优化前：** 环绕通知在 `finally` 中同步调用 `OperateLogService.createOperateLog()`，日志入库与接口返回在同一线程。

**优化后：**

- 新增 `OperateLogAsyncWriter`，使用 `@Async("operateLogExecutor")` 异步写库
- 配置独立线程池 `operateLogExecutor`（核心 2，最大 4，队列 200）
- 主接口先返回，日志写入在后台完成

---

## 五、优化效果对比（示例）

请在本地重启服务后，用同一 token 各执行 10 次取平均值填入：

| 接口 | 优化前 avg(ms) | 优化后 avg(ms) | 说明 |
|------|----------------|----------------|------|
| GET /api/users?pageNum=1&pageSize=10 | 待测 | 待测 | 分页字段精简 |
| GET /api/operate-logs/recent?operator=testadmin&limit=20 | 待测 | 待测 | 联合索引 |
| POST /api/users（含 AOP 写日志） | 待测 | 待测 | 异步写日志 |

**测试命令示例：**

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"123456"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

for i in $(seq 1 10); do
  curl -s -o /dev/null -w "%{time_total}\n" \
    "http://localhost:8080/api/users?pageNum=1&pageSize=10" \
    -H "Authorization: Bearer $TOKEN"
done
```

---

## 六、代码规范整理（任务 10-14）

| 项 | 内容 |
|----|------|
| 公共工具 | 提取 `PageHelper`、`TokenResolver` |
| 重复逻辑 | AuthInterceptor、OperateLogAspect 共用 Token 解析 |
| 无用导入 | 已清理（如 BaseDataUpdateVO 未使用的 NotBlank） |
| JavaDoc | Service、Config、Util 补充类与方法说明 |

---

## 七、结论

| 优化项 | 状态 |
|--------|------|
| 慢查询分析方案 | 已完成 |
| 高频字段索引 | 已完成 |
| 分页字段精简 | 已完成 |
| AOP 异步写日志 | 已完成 |
| 响应时间对比 | 待本地实测填表 |
