-------------------------------Oracle执行以下语句---------------------------------------------------------------------------------
DELETE FROM sys_common_user_role;
DELETE FROM sys_common_role_permission;
DELETE FROM SYS_COMMON_PERMISSION;
DELETE FROM sys_common_role;
DELETE FROM sys_common_user;

insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '应用管理', 1000, 'app', 1, null, 'app:module', 'MODULE', null, '/app');
insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P2', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '流程管理', 2000, 'process', 2, null, 'process:module', 'MODULE', null, '/process');
insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P21', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '定义管理', 2100, 'define', 3, 'P2', 'process:define', 'MODULE', null, '/process/define');
insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P22', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '部署管理', 2200, 'deploy', 3, 'P2', 'process:deploy', 'MODULE', null, '/process/deploy');
insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P23', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '实例管理', 2300, 'instance', 3, 'P2', 'process:instance', 'MODULE', null, '/process/instance');
insert into SYS_COMMON_PERMISSION (ID, CREATED_TIME, MODIFIED_TIME, CREATOR, ENABLED, MODIFIER, REMOVED_TIME, APPCODE, DESCRIPTION, DISPLAY_ORDER, ICON, MENU_LEVEL, PARENT_ID, PERMISSION_CODE, PERMISSION_TYPE, REMARK, URL)
values ('P24', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'wfengine', '任务管理', 2400, 'task', 3, 'P2', 'process:task', 'MODULE', null, '/process/task');

INSERT INTO sys_common_role VALUES ('R1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, '100', 0, 'ROLE_ADMIN', '管理员');
INSERT INTO sys_common_role VALUES ('R2', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, '200', 0, 'ROLE_USER', '普通用户');


INSERT INTO sys_common_role_permission VALUES ('RP1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P1', 'R1');
INSERT INTO sys_common_role_permission VALUES ('RP2', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P2', 'R1');
INSERT INTO sys_common_role_permission VALUES ('RP3', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P21', 'R1');
INSERT INTO sys_common_role_permission VALUES ('RP4', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P22', 'R1');
INSERT INTO sys_common_role_permission VALUES ('RP5', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P23', 'R1');
INSERT INTO sys_common_role_permission VALUES ('RP6', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'P24', 'R1');

INSERT INTO sys_common_user (id, created_time, modified_time, creator, enabled, modifier, removed_time, account_non_expired, account_non_locked, address, belong_company_code, belong_company_type_dict_value, belong_department_code, belong_org_code, compress_photo, credentials_non_expired, display_order, duty_date, duty_status, email, gender, id_card, nation, nickname, openid, password, photo,photo_time, preferred_mobile, reserve1, reserve2, reserve3, reserve4, reserve5, truename, unionid, username, user_type) VALUES ('U1', sysdate, sysdate, 'hadmin', 1, 'hadmin', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '超级管理员', NULL, '$2a$12$qE7F54Gm9lbjphbwOG8N2OsAH03N2R5lsaDaBMp4yY./oRf2w3K9i', NULL, NULL, '13888888888', NULL, NULL, NULL, NULL, NULL, '超级管理员', '330102199012302711', 'hadmin', NULL);

INSERT INTO sys_common_user_role VALUES ('UR1', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'R1', 'U1');
INSERT INTO sys_common_user_role VALUES ('UR2', sysdate, sysdate, 'hadmin', 1, 'hadmin', null, 'R2', 'U1');

commit;
-------------------------------Oracle执行以下语句---------------------------------------------------------------------------------