# 用户管理模块 Bug 清单

> 测试人员：Eric  
> 测试时间：2026-06-08  
> 测试工具：Postman（集合见 `docs/postman/user-api.postman_collection.json`）

## 一、测试范围

| 模块 | 接口 | 是否覆盖 |
|------|------|----------|
| 认证 | POST /api/auth/login | ✅ |
| 用户 | POST /api/users | ✅ |
| 用户 | GET /api/users | ✅ |
| 用户 | GET /api/users/{id} | ✅ |
| 用户 | PUT /api/users | ✅ |
| 用户 | DELETE /api/users/{id} | ✅ |
| 用户 | DELETE /api/users/batch | ✅ |
| AOP | 用户接口触发自动写日志 | 见 `docs/aop-test.md` |
| 操作日志 | GET /api/operate-logs/{id} | 见 `docs/aop-test.md` |
| 基础数据 | POST/PUT/DELETE /api/base-data | 见 `docs/cache-test.md` |
| 基础数据 | GET /api/base-data/type/{typeCode} | 见 `docs/cache-test.md` |
| 缓存 | @Cacheable / @CacheEvict | 见 `docs/cache-test.md` |
| 跨模块 | 登录→用户CRUD→操作日志 | 见 `docs/cross-module-test.md` |

## 二、边界测试用例

| 编号 | 场景 | 预期结果 | 实际结果 |
|------|------|----------|----------|
| B01 | 登录密码错误 | code=401 | 通过 |
| B02 | 登录用户名为空 | code=400 | 通过 |
| B03 | 未携带 token 访问接口 | HTTP 401 | 通过 |
| B04 | 无效 token 访问接口 | HTTP 401 | 通过 |
| B05 | 新增用户名重复 | code=400 | 通过 |
| B06 | 新增密码长度不足 | code=400 | 通过 |
| B07 | 查询不存在用户 | code=404 | 通过 |
| B08 | 修改用户名为已存在用户名 | code=400 | 通过 |
| B09 | 删除不存在用户 | code=404 | 通过 |
| B10 | 批量删除含不存在 ID | 部分成功，返回 failedIds | 通过 |
| B11 | 批量删除空列表 | code=400 | 通过 |
| B12 | 基础数据同类型重复 dataCode | code=400 | 通过 |
| B13 | 删除不存在基础数据 | code=404 | 通过 |
| B14 | 未登录访问基础数据接口 | HTTP 401 | 通过 |

## 三、Bug 清单（按优先级）

### P0 — 阻塞级

| ID | 描述 | 复现步骤 | 状态 |
|----|------|----------|------|
| — | 暂无 | — | — |

### P1 — 高优先级

| ID | 描述 | 复现步骤 | 建议修复 | 状态 |
|----|------|----------|----------|------|
| BUG-001 | 数据库无默认测试用户，首次无法登录测试 | 1. 执行 init.sql<br>2. 直接 Postman 登录 testadmin | 在 init.sql 增加测试用户初始化数据 | 已修复 |
| BUG-002 | 批量删除全部失败时，响应 code 仍为 200 | DELETE /api/users/batch body: `{"ids":[99998,99999]}` | 全部失败时返回 404 业务码 | 已修复 |
| BUG-007 | 逻辑删除后同名用户新增返回 500 | 删除用户后再新增同名用户 | 唯一性校验含已删除用户 | 已修复 |

### P2 — 中优先级

| ID | 描述 | 复现步骤 | 建议修复 | 状态 |
|----|------|----------|----------|------|
| BUG-003 | Token 存储在内存，服务重启后全部失效 | 1. 登录获取 token<br>2. 重启服务<br>3. 带原 token 访问 | 文档说明；后续可接入 Redis | 已说明（见接口调试文档） |
| BUG-004 | 业务错误 HTTP 状态码均为 200，仅靠 body.code 区分 | 调用任意返回 404/400 的接口 | 文档明确约定 | 已说明（见接口调试文档） |
| BUG-005 | 批量删除 ids 含 null 时，failedIds 会出现 null 元素 | DELETE /api/users/batch body: `{"ids":[null,1]}` | 返回 400 用户ID不能为空 | 已修复 |

### P3 — 低优先级

| ID | 描述 | 复现步骤 | 建议修复 | 状态 |
|----|------|----------|----------|------|
| BUG-006 | 分页列表不返回用户角色信息 | GET /api/users 对比 GET /api/users/{id} | 列表接口补充 roles 字段 | 已修复 |

## 四、Postman 使用说明

1. 执行 `sql/init.sql` 初始化数据库（已含测试用户 testadmin / 123456）
2. 导入 `docs/postman/user-api.postman_collection.json`
3. 导入 `docs/postman/local.postman_environment.json`
4. 运行 **01-认证 → 登录-成功**，自动保存 token
5. 使用 Collection Runner 运行全量用例

## 五、相关文档

- 接口调试文档：`docs/接口调试文档.md`（含操作日志、基础数据全量 API）
- AOP 测试文档：`docs/aop-test.md`
- 缓存测试文档：`docs/cache-test.md`
- 跨模块联调报告：`docs/cross-module-test.md`
