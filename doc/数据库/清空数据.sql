DROP TABLE xc_api_supplier;
DROP TABLE xc_app;
DROP TABLE xc_applet;
DROP TABLE xc_authority;
DROP TABLE xc_authorize;
DROP TABLE xc_column;
DROP TABLE xc_data_type;
DROP TABLE xc_deleted_user;
DROP TABLE xc_dict;
DROP TABLE xc_group;
DROP TABLE xc_group__role;
DROP TABLE xc_info;
DROP TABLE xc_menu;
DROP TABLE xc_message_log;
DROP TABLE xc_message_template;
DROP TABLE xc_page;
DROP TABLE xc_page__column;
DROP TABLE xc_role;
DROP TABLE xc_role__authority;
DROP TABLE xc_statistics;
DROP TABLE xc_task;
DROP TABLE xc_task_log;
DROP TABLE xc_tree_dict;
DROP TABLE xc_user;
DROP TABLE xc_user__group;
DROP TABLE xc_user__role;
DROP TABLE xc_user_authorize;
DROP TABLE xc_user_authorize__authority;
DROP TABLE xc_user_info;
DROP TABLE xc_version;

-- 清除后需要运行基础数据才能使用哦，并将角色关联到admin账户上。
-- 切记修改管理员密码，初始密码为：123456
