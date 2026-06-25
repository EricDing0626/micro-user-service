# 跨模块联调测试报告

> 对应任务 1-4、19（测试部分）  
> 测试人员：Eric  
> 测试时间：2026-06-17

## 一、测试目标

验证 **认证 → 用户管理 → 操作日志 → 基础数据** 四个模块之间的数据交互是否正确：

1. 用户登录后可完成用户 CRUD 全流程  
2. 每次用户操作均由 AOP 自动生成操作日志  
3. 用户新增时可先加载基础数据（部门类型、用户状态）作为下拉选项  
4. 可通过接口回查最近操作日志，验证关联关系  

---

## 二、前置条件

1. MySQL 已启动，已执行 `sql/init.sql`（含 `dept_type` 种子数据）  
2. 应用已启动：`mvn spring-boot:run -DskipTests`  
3. 测试账号：`testadmin / 123456`  

**补充 dept_type 种子（若库已存在可单独执行）：**

```sql
INSERT IGNORE INTO sys_base_data (type_code, data_code, data_name, sort, status) VALUES
('dept_type', 'RD', '研发部', 1, 1),
('dept_type', 'HR', '人力资源部', 2, 1),
('dept_type', 'OPS', '运维部', 3, 1);
```

---

## 三、跨模块测试用例

### 用例 CM-01：全流程（登录 → 用户 CRUD → 查看日志）

| 步骤 | 操作 | 涉及模块 | 预期 |
|------|------|----------|------|
| 1 | POST `/api/auth/login` | 认证 | code=200，返回 token |
| 2 | GET `/api/base-data/type/dept_type` | 基础数据 | 返回部门下拉列表 |
| 3 | GET `/api/base-data/type/user_status` | 基础数据 | 返回状态字典（禁用/启用） |
| 4 | POST `/api/users`（status 取自基础数据） | 用户 | code=200，返回新 userId |
| 5 | PUT `/api/users` | 用户 | code=200，修改成功 |
| 6 | DELETE `/api/users/{id}` | 用户 | code=200，删除成功 |
| 7 | GET `/api/operate-logs/recent?operator=testadmin&requestPathPrefix=/api/users` | 操作日志 | 含 POST/PUT/DELETE 日志 |
| 8 | GET `/api/operate-logs/{id}` | 操作日志 | 单条日志字段完整 |

### 用例 CM-02：用户操作与日志关联

| 步骤 | 用户操作 | 预期日志字段 |
|------|----------|--------------|
| 1 | 新增用户 POST `/api/users` | operator=testadmin，requestMethod=POST，requestPath=/api/users |
| 2 | 修改用户 PUT `/api/users` | operator=testadmin，requestMethod=PUT |
| 3 | 删除用户 DELETE `/api/users/{id}` | operator=testadmin，requestMethod=DELETE |
| 4 | 登录 POST `/api/auth/login` | operator=testadmin（来自 LoginVO） |

### 用例 CM-03：基础数据在用户管理中的使用

| 场景 | 说明 | 预期 |
|------|------|------|
| 部门下拉 | 新增用户页调用 `GET /api/base-data/type/dept_type` | 返回研发部、人力资源部、运维部 |
| 状态下拉 | 新增用户页调用 `GET /api/base-data/type/user_status` | 返回禁用(0)、启用(1) |
| 创建用户 | POST `/api/users` 时 `status=1`（来自 user_status 字典） | 用户创建成功，status 与字典一致 |

> 说明：当前用户表未单独存 dept 字段，部门类型用于模拟前端下拉加载；用户 `status` 字段与 `user_status` 字典对应。

---

## 四、curl 快速验证

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"123456"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

curl -s http://localhost:8080/api/base-data/type/dept_type -H "Authorization: Bearer $TOKEN"
curl -s http://localhost:8080/api/base-data/type/user_status -H "Authorization: Bearer $TOKEN"

USER_ID=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"cross'$(date +%s)'","password":"123456","status":1,"roleIds":[2]}' \
  | grep -o '"data":[0-9]*' | cut -d: -f2)

curl -s -X PUT http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"id\":$USER_ID,\"email\":\"cross@test.com\"}"

curl -s -X DELETE http://localhost:8080/api/users/$USER_ID \
  -H "Authorization: Bearer $TOKEN"

curl -s "http://localhost:8080/api/operate-logs/recent?operator=testadmin&requestPathPrefix=/api/users&limit=20" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 五、Postman 测试

1. 导入 `docs/postman/user-api.postman_collection.json`  
2. 导入 `docs/postman/local.postman_environment.json`  
3. 运行文件夹 **06-跨模块联调验证**（建议 Collection Runner 顺序执行）  

---

## 六、数据库验证（可选）

```sql
SELECT id, operator, request_path, request_method, operate_time
FROM sys_operate_log
WHERE operator = 'testadmin'
  AND request_path LIKE '/api/users%'
ORDER BY id DESC
LIMIT 10;
```

---

## 七、问题记录与解决方法

| 编号 | 问题描述 | 解决方法 | 状态 |
|------|----------|----------|------|
| CM-B01 | 新增接口 404 | 代码变更后重启 Spring Boot | 已解决 |
| CM-B02 | curl 使用 `<token>` 占位符导致 401 | 替换为登录返回的真实 token | 已解决 |
| CM-B03 | 无法通过 API 批量查看操作日志 | 新增 GET `/api/operate-logs/recent` 接口 | 已解决 |
| CM-B04 | 缺少部门类型基础数据 | init.sql 增加 `dept_type` 种子 | 已解决 |

---

## 八、测试结论

| 项目 | 结果 |
|------|------|
| 登录 → 用户 CRUD 全流程 | 待测 / 通过 |
| 模块间数据交互正确性 | 待测 / 通过 |
| 用户操作与操作日志关联 | 待测 / 通过 |
| 基础数据在用户新增场景的使用 | 待测 / 通过 |

测试完成后将「待测」改为「通过」。
