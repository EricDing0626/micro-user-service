# Postman 接口自动化测试报告

> 对应任务 16-18  
> 测试人员：Eric  
> 测试时间：2026-06-17

## 一、测试范围

| 文件夹 | 请求数 | 覆盖模块 |
|--------|--------|----------|
| 01-认证 | 4 | 登录、未授权 |
| 02-用户管理-正常流程 | 7 | 用户 CRUD、分页、批量删除 |
| 03-边界场景 | 9 | 参数校验、404、token |
| 04-AOP操作日志验证 | 4 | AOP 自动写日志 |
| 05-基础数据与缓存验证 | 9 | 基础数据 CRUD、缓存 |
| 06-跨模块联调验证 | 8 | 登录→基础数据→用户→日志 |
| 07-操作日志全量接口 | 5 | 操作日志 REST 全接口 |
| **合计** | **46** | 全模块 |

---

## 二、Collection Runner 执行步骤

1. 启动服务：`mvn spring-boot:run -DskipTests`
2. Postman 导入：
   - `docs/postman/user-api.postman_collection.json`
   - `docs/postman/local.postman_environment.json`
3. 环境选择 **micro-user-local**
4. 集合 **用户管理模块-全量测试** → **Run collection**
5. 勾选全部文件夹（或按需选择）
6. 点击 **Run**，等待执行完成
7. 查看 **Passed / Failed** 统计，可 **Export Results**

> 建议顺序执行：01 → 02 → 03 → 04 → 05 → 06 → 07。各文件夹内已含登录步骤或可复用 collection 级 token。

---

## 三、接口覆盖清单

| 模块 | 方法 | 路径 | Postman 覆盖 |
|------|------|------|--------------|
| 认证 | POST | /api/auth/login | 01 |
| 用户 | POST | /api/users | 02、06 |
| 用户 | GET | /api/users | 02 |
| 用户 | GET | /api/users/{id} | 02、04 |
| 用户 | PUT | /api/users | 02、06 |
| 用户 | DELETE | /api/users/{id} | 02、06 |
| 用户 | DELETE | /api/users/batch | 02 |
| 操作日志 | POST | /api/operate-logs | 07 |
| 操作日志 | GET | /api/operate-logs/recent | 06、07 |
| 操作日志 | GET | /api/operate-logs/{id} | 04、06、07 |
| 操作日志 | DELETE | /api/operate-logs/{id} | 07 |
| 基础数据 | POST | /api/base-data | 05 |
| 基础数据 | PUT | /api/base-data | 05 |
| 基础数据 | DELETE | /api/base-data/{id} | 05 |
| 基础数据 | GET | /api/base-data/type/{typeCode} | 05、06 |

**覆盖率：** 15/15 REST 接口（100%）

---

## 四、测试结果记录

| 文件夹 | 通过 | 失败 | 通过率 | 备注 |
|--------|------|------|--------|------|
| 01-认证 | 待填 | 待填 | 待填 | |
| 02-用户管理-正常流程 | 待填 | 待填 | 待填 | |
| 03-边界场景 | 待填 | 待填 | 待填 | |
| 04-AOP操作日志验证 | 待填 | 待填 | 待填 | 步骤 03 需 operateLogId |
| 05-基础数据与缓存验证 | 待填 | 待填 | 待填 | |
| 06-跨模块联调验证 | 待填 | 待填 | 待填 | |
| 07-操作日志全量接口 | 待填 | 待填 | 待填 | |
| **总计** | 待填 | 待填 | 待填 | |

Runner 执行后将「待填」改为实际数字。

---

## 五、失败用例记录（如有）

| 编号 | 请求名 | 失败原因 | 处理 |
|------|--------|----------|------|
| — | — | — | — |

---

## 六、结论

| 项目 | 结果 |
|------|------|
| 全接口 Postman 覆盖 | 待测 / 通过 |
| Collection Runner 全量执行 | 待测 / 通过 |
| 断言通过率 ≥ 95% | 待测 / 通过 |

测试完成后更新结论。
