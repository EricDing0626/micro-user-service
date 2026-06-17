# 基础数据与缓存联调测试

> 对应任务 17-19：基础数据 CRUD、按类型查询、Spring Cache 缓存与失效验证  
> 测试人员：Eric  
> 测试时间：2026-06-17

## 一、前置条件

1. MySQL 已启动，`micro_user` 库中 `sys_base_data` 已有种子数据
2. 应用已启动（**修改代码后需重启**）：`mvn spring-boot:run -DskipTests`
3. `application.yml` 中 `spring.cache.type: simple` 已配置
4. 测试账号：`testadmin / 123456`

## 二、基础数据 CRUD 测试

### 步骤 1：登录获取 Token

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"123456"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
```

### 步骤 2：按类型查询

```bash
curl -s http://localhost:8080/api/base-data/type/user_status \
  -H "Authorization: Bearer $TOKEN"
```

**预期：** `code=200`，返回 `user_status` 字典列表（至少含「禁用」「启用」），按 `sort` 升序。

### 步骤 3：新增基础数据

```bash
curl -s -X POST http://localhost:8080/api/base-data \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"typeCode":"user_status","dataCode":"99","dataName":"缓存测试","sort":99}'
```

**预期：** `code=200`，`data` 为新记录 ID。

### 步骤 4：修改基础数据

将 `{id}` 替换为步骤 3 返回的 ID：

```bash
curl -s -X PUT http://localhost:8080/api/base-data \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"id":{id},"dataName":"缓存测试-已修改","sort":100}'
```

**预期：** `code=200`，`message` 含「修改成功」。

### 步骤 5：再次按类型查询

```bash
curl -s http://localhost:8080/api/base-data/type/user_status \
  -H "Authorization: Bearer $TOKEN"
```

**预期：** 列表中含 `dataCode=99`，`dataName=缓存测试-已修改`。

### 步骤 6：删除基础数据

```bash
curl -s -X DELETE http://localhost:8080/api/base-data/{id} \
  -H "Authorization: Bearer $TOKEN"
```

**预期：** `code=200`；再次查询时该条不再出现（逻辑删除）。

## 三、缓存验证

观察 **应用启动终端** 中的 MyBatis SQL 日志（`StdOutImpl` 已开启）。

| 步骤 | 操作 | 服务端 SQL 日志 |
|------|------|-----------------|
| C01 | 第 1 次 GET `/api/base-data/type/user_status` | **应出现** `SELECT ... FROM sys_base_data` |
| C02 | 第 2 次 GET 同一路径 | **不应出现** 上述 SQL（`@Cacheable` 命中） |
| C03 | POST 新增同 typeCode 的数据 | 写库 SQL 出现；`@CacheEvict` 清除该类型缓存 |
| C04 | 再次 GET 同 typeCode | **应再次出现** SELECT（缓存已失效） |
| C05 | PUT 修改任意基础数据 | `@CacheEvict(allEntries=true)` 清空全部类型缓存 |
| C06 | DELETE 删除基础数据 | 同上，下次查询重新查库 |

> 说明：终端粘贴 curl 时请勿复制以 `#` 开头的注释行，否则 zsh 可能报 `command not found: #`。

## 四、边界用例

| 编号 | 场景 | 操作 | 预期 |
|------|------|------|------|
| D01 | 未登录访问 | GET `/api/base-data/type/user_status` 不带 token | HTTP 401 |
| D02 | typeCode 为空 | GET `/api/base-data/type/` | 404 或路由不匹配 |
| D03 | 同类型重复 dataCode | POST 相同 `typeCode` + `dataCode` | code=400 |
| D04 | 修改不存在记录 | PUT `id=99999` | code=404 |
| D05 | 删除不存在记录 | DELETE `/api/base-data/99999` | code=404 |
| D06 | 新增缺少必填字段 | POST body 缺 `dataName` | code=400 |

## 五、Postman 测试

1. 导入 `docs/postman/user-api.postman_collection.json`
2. 导入 `docs/postman/local.postman_environment.json`
3. 打开文件夹 **05-基础数据与缓存验证**
4. 按顺序执行（或 Collection Runner 运行该文件夹）
5. 执行 **03-按类型查询-第2次(观察缓存)** 时，对照服务端日志确认无 SQL

## 六、结论

| 项目 | 结果 |
|------|------|
| 基础数据 CRUD | 待测 / 通过 |
| 按 typeCode 查询与排序 | 待测 / 通过 |
| @Cacheable 二次查询不查库 | 待测 / 通过 |
| 新增/修改/删除后缓存失效 | 待测 / 通过 |
| AOP 对 base-data 接口写操作日志 | 待测 / 通过 |

测试完成后将上表「待测」改为「通过」。
