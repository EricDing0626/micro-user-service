# AOP 操作日志联调测试

> 对应任务 12：调用用户管理接口，验证操作日志是否自动生成  
> 测试人员：Eric  
> 测试时间：2026-06-15

## 一、前置条件

1. MySQL 已启动，`micro_user` 库表齐全
2. 应用已启动：`mvn spring-boot:run`
3. 测试账号：`testadmin / 123456`

## 二、测试步骤

### 步骤 1：登录获取 Token

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"123456"}'
```

记录返回的 `data.token`。

### 步骤 2：调用用户接口（触发 AOP）

```bash
TOKEN="你的token"

curl -s -X GET "http://localhost:8080/api/users/1" \
  -H "Authorization: Bearer $TOKEN"
```

也可调用 `POST/PUT/DELETE /api/users` 等任意非 `/api/operate-logs` 接口。

### 步骤 3：数据库验证日志已自动生成

```bash
mysql -uroot micro_user -e "
SELECT id, operator, request_path, request_method, operate_time
FROM sys_operate_log
ORDER BY id DESC
LIMIT 3;
"
```

**预期：**

| 字段 | 预期值 |
|------|--------|
| operator | `testadmin`（登录接口为请求体中的 username） |
| request_path | `/api/users/1`（或你调用的路径） |
| request_method | `GET`（或 POST/PUT/DELETE） |
| request_params | 含路径参数或请求体 JSON |
| response_result | 含 `code:200` 的 Result JSON |

### 步骤 4：通过接口回查日志详情

将步骤 3 查到的 `id` 代入：

```bash
LOG_ID=1

curl -s -X GET "http://localhost:8080/api/operate-logs/$LOG_ID" \
  -H "Authorization: Bearer $TOKEN"
```

**预期：** `code=200`，`data` 中字段与数据库一致。

## 三、验证用例

| 编号 | 场景 | 操作 | 预期 |
|------|------|------|------|
| A01 | 登录接口 | POST `/api/auth/login` | 生成日志，operator=登录用户名 |
| A02 | 查询用户 | GET `/api/users/{id}` | 生成日志，operator=testadmin |
| A03 | 新增用户 | POST `/api/users` | 生成日志，request_params 含请求体 |
| A04 | 手动写日志 | POST `/api/operate-logs` | **不**经 AOP 重复记录（已排除 OperateLogController） |
| A05 | 接口异常 | GET `/api/users/99999` | 仍生成日志，response_result 含 error 信息 |

## 四、Postman 测试

导入 `docs/postman/user-api.postman_collection.json`，执行文件夹 **04-AOP操作日志验证**：

1. **登录-成功** → 自动保存 token
2. **查询用户-触发AOP** → 调用 GET `/api/users/{{userId}}`
3. 终端执行步骤 3 的 SQL，将最新 `id` 填入环境变量 `operateLogId`
4. **查询操作日志-验证AOP** → GET `/api/operate-logs/{{operateLogId}}`

## 五、结论

| 项目 | 结果 |
|------|------|
| AOP 切点拦截 `/api/*` | 待测 / 通过 |
| 操作人、时间、路径、方法采集 | 待测 / 通过 |
| 请求参数、响应结果入库 | 待测 / 通过 |
| 日志写入失败不影响业务 | 待测 / 通过 |

测试完成后将上表「待测」改为「通过」。
