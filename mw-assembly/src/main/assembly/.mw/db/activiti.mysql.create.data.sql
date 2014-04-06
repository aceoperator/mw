-- Change the password for security reasons
INSERT INTO  ACT_ID_USER (ID_, REV_, FIRST_, EMAIL_, PWD_) VALUES ('admin', 1, 'Admin', 'operations@quik-j.net', 'a1b2c3d4');
INSERT INTO ACT_ID_GROUP (ID_, REV_, NAME_, TYPE_) VALUES ('admin', 1, 'Admin', 'security-role');
INSERT INTO  ACT_ID_MEMBERSHIP(USER_ID_, GROUP_ID_) VALUES ('admin', 'admin');