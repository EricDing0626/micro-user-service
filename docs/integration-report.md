# 跨模块联调与交付报告

> 对应任务 19  
> 作者：Eric  
> 日期：2026-06-17

## 一、报告概述

本报告汇总 **micro-user-service** 跨模块联调、性能优化与接口自动化测试的完整交付情况，涵盖：

- 认证、用户管理、操作日志、基础数据四模块联动
- 性能优化（索引、分页、AOP 异步）
- Postman 全量自动化测试
- Swagger 分组在线文档

---

## 二、测试用例汇总

### 2.1 跨模块全流程（CM-01）

| 步骤 | 操作 | 预期 |
|------|------|------|
| 1 | 登录 | 返回 token |
| 2 | 查 dept_type / user_status | 基础数据下拉可用 |
| 3 | 新增 → 修改 → 删除用户 | CRUD 成功 |
| 4 | 查 recent 操作日志 | 含 POST/PUT/DELETE，operator=testadmin |

详见：`docs/cross-module-test.md`

### 2.2 AOP 日志关联（CM-02）

每个用户接口调用自动写入 `sys_operate_log`，字段含 operator、requestPath、requestMethod、requestParams、responseResult。

详见：`docs/aop-test.md`

### 2.3 基础数据与缓存（CM-03）

- 按 typeCode 查询字典，第二次命中 `@Cacheable`
- 增删改触发 `@CacheEvict`

详见：`docs/cache-test.md`

### 2.4 接口自动化（CM-04）

46 个 Postman 请求覆盖 15 个 REST 接口，Collection Runner 一键回归。

详见：`docs/postman-test-report.md`

---

## 三、问题记录与解决方法

| 编号 | 问题 | 解决方法 | 状态 |
|------|------|----------|------|
| IR-01 | 新接口 404 | 重启 Spring Boot | 已解决 |
| IR-02 | token 占位符导致 401 | 使用登录返回的真实 token | 已解决 |
| IR-03 | 无法批量查操作日志 | 新增 GET /api/operate-logs/recent | 已解决 |
| IR-04 | 分页查询含 password 字段 | select 指定列表字段 | 已解决 |
| IR-05 | AOP 同步写日志影响响应 | OperateLogAsyncWriter 异步写入 | 已解决 |
| IR-06 | 缺少 dept_type 种子 | init.sql 补充部门类型数据 | 已解决 |

---

## 四、优化效果

| 优化项 | 说明 | 文档 |
|--------|------|------|
| 慢查询分析 | MySQL slow_query_log 配置与分析命令 | performance-optimization.md §一 |
| 索引 | idx_operator_operate_time；username 已有唯一索引 | performance-optimization.md §二 |
| 分页 | 列表不查 password | performance-optimization.md §三 |
| AOP 异步 | operateLogExecutor 线程池 | performance-optimization.md §四 |
| 响应时间 | 优化前后对比表（本地实测填数） | performance-optimization.md §五 |

---

## 五、文档与工具交付

| 交付物 | 路径 |
|--------|------|
| 接口调试文档 | docs/接口调试文档.md |
| 跨模块联调 | docs/cross-module-test.md |
| 性能优化 | docs/performance-optimization.md |
| Postman 集合 | docs/postman/user-api.postman_collection.json |
| 自动化测试报告 | docs/postman-test-report.md |
| Bug 清单 | docs/bug-list.md |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

Swagger 分组：**认证管理 / 用户管理 / 操作日志管理 / 基础数据管理**

---

## 六、Git 提交记录（本周）

| Commit | 说明 |
|--------|------|
| 1 | test: 跨模块联调与用户操作日志关联验证 |
| 2 | perf: 优化查询与AOP异步日志，并统一代码规范 |
| 3 | docs: 完善Swagger与Postman自动化测试及联调报告 |

---

## 七、总体结论

| 项目 | 结果 |
|------|------|
| 跨模块数据交互 | 待测 / 通过 |
| 用户操作与日志关联 | 待测 / 通过 |
| 基础数据在用户场景的使用 | 待测 / 通过 |
| 性能优化落地 | 待测 / 通过 |
| 代码无编译错误 | 通过 |
| Swagger + Postman 全量覆盖 | 待测 / 通过 |

Runner 执行并将各专项文档结论更新为「通过」后，本项目 Week3 交付完成。
