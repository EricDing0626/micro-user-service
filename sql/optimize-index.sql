-- 性能优化：高频查询字段索引（已有库执行，重复执行可能报 Duplicate key name，可忽略）
USE micro_user;

-- sys_user.username 已通过 uk_username 唯一索引覆盖，无需重复创建

-- sys_operate_log：按操作人 + 操作时间查询最近日志（对应 /api/operate-logs/recent）
ALTER TABLE sys_operate_log ADD INDEX idx_operator_operate_time (operator, operate_time);

-- 说明：当前操作日志表使用 operator(用户名) 字段，未单独存 user_id；
-- 联调与慢查询分析时以 operator、operate_time 作为高频筛选条件。
