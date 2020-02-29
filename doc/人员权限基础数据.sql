-------------------------------MySql执行以下语句---------------------------------------------------------------------------------
-- ----------------------------
-- Records of sys_common_permission
-- ----------------------------
INSERT INTO 'sys_common_permission' VALUES ('P1', now(), now(), 'hadmin', '', 'hadmin', null, 'ilaw', '首页', '1000', null, '1', null, 'module:index', 'MODULE', null, null);

-- ----------------------------
-- Records of sys_common_role
-- ----------------------------
INSERT INTO 'sys_common_role' VALUES ('R1', now(), now(), 'hadmin', '', 'hadmin', null, '100', '', 'ROLE_ADMIN', '管理员');
INSERT INTO 'sys_common_role' VALUES ('R2', now(), now(), 'hadmin', '', 'hadmin', null, '200', '', 'ROLE_USER', '普通用户');

-- ----------------------------
-- Records of sys_common_role_permission
-- ----------------------------
INSERT INTO 'sys_common_role_permission' VALUES ('RP1', now(), now(), 'hadmin', '', 'hadmin', null, 'P1', 'R2');

-- ----------------------------
-- Records of sys_common_user
-- ----------------------------
INSERT INTO 'sys_common_user' ('id', 'created_time', 'modified_time', 'creator', 'enabled', 'modifier', 'removed_time', 'account_non_expired', 'account_non_locked', 'address', 'belong_company_code', 'belong_company_type_dict_value', 'belong_department_code', 'belong_org_code', 'compress_photo', 'credentials_non_expired', 'display_order', 'duty_date', 'duty_status', 'email', 'gender', 'id_card', 'nation', 'nickname', 'openid', 'password', 'photo', 'photo_time', 'preferred_mobile', 'reserve1', 'reserve2', 'reserve3', 'reserve4', 'reserve5', 'truename', 'unionid', 'username', 'user_type') VALUES ('U1', now(), now(), 'hadmin', '', 'hadmin', NULL, '', '', NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '超级管理员', NULL, '$2a$12$qE7F54Gm9lbjphbwOG8N2OsAH03N2R5lsaDaBMp4yY./oRf2w3K9i', NULL, NULL, '13888888888', NULL, NULL, NULL, NULL, NULL, '超级管理员', '330102199012302711', 'hadmin', NULL);

-- ----------------------------
-- Records of sys_common_user_role
-- ----------------------------
INSERT INTO 'sys_common_user_role' VALUES ('UR1', now(), now(), 'hadmin', '', 'hadmin', null, 'R1', 'U1');
INSERT INTO 'sys_common_user_role' VALUES ('UR2', now(), now(), 'hadmin', '', 'hadmin', null, 'R2', 'U1');

commit;
-------------------------------MySql执行以下语句---------------------------------------------------------------------------------



-------------------------------Oracle执行以下语句---------------------------------------------------------------------------------
INSERT INTO sys_common_permission VALUES ('P1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'ilaw', '首页', '1000', null, '1', null, 'module:index', 'MODULE', null, null);


INSERT INTO sys_common_role VALUES ('R1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, '100', 0, 'ROLE_ADMIN', '管理员');
INSERT INTO sys_common_role VALUES ('R2', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, '200', 0, 'ROLE_USER', '普通用户');


INSERT INTO sys_common_role_permission VALUES ('RP1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P1', 'R2');


INSERT INTO sys_common_user (id, created_time, modified_time, creator, enabled, modifier, removed_time, account_non_expired, account_non_locked, address, belong_company_code, belong_company_type_dict_value, belong_department_code, belong_org_code, compress_photo, credentials_non_expired, display_order, duty_date, duty_status, email, gender, id_card, nation, nickname, openid, password, photo,photo_time, preferred_mobile, reserve1, reserve2, reserve3, reserve4, reserve5, truename, unionid, username, user_type) VALUES ('U1', sysdate, sysdate, 'hadmin', 1, 'hadmin', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '超级管理员', NULL, '$2a$12$qE7F54Gm9lbjphbwOG8N2OsAH03N2R5lsaDaBMp4yY./oRf2w3K9i', NULL, NULL, '13888888888', NULL, NULL, NULL, NULL, NULL, '超级管理员', '330102199012302711', 'hadmin', NULL);

INSERT INTO sys_common_user_role VALUES ('UR1', sysdate, sysdate, 'hadmin', 0, 'hadmin', null, 'R1', 'U1');
INSERT INTO sys_common_user_role VALUES ('UR2', sysdate, sysdate, 'hadmin', 0, 'hadmin', null, 'R2', 'U1');

commit;
-------------------------------Oracle执行以下语句---------------------------------------------------------------------------------