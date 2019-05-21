
CREATE DATABASE thesis_dev;

CREATE USER 'thesis_dev_user'@'localhost' IDENTIFIED BY 'th-pass';

GRANT SELECT ON thesis_dev.* to 'thesis_dev_user'@'localhost';
GRANT INSERT ON thesis_dev.* to 'thesis_dev_user'@'localhost';
GRANT DELETE ON thesis_dev.* to 'thesis_dev_user'@'localhost';
GRANT UPDATE ON thesis_dev.* to 'thesis_dev_user'@'localhost';
