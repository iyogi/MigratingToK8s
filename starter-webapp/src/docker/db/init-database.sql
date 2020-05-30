create database profiles;
CREATE USER empuser@'localhost' IDENTIFIED BY 'password';
CREATE USER empuser@'%' IDENTIFIED BY 'password';
ALTER USER 'empuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
ALTER USER 'empuser'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
GRANT ALL PRIVILEGES ON profiles.* to empuser@'localhost';
GRANT ALL PRIVILEGES ON profiles.* to empuser@'%';