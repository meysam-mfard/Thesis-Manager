
CREATE DATABASE IF NOT EXISTS thesis_manager;

CREATE USER 'thesis_product_user'@'localhost' IDENTIFIED BY 'th-pass';

GRANT SELECT ON thesis_manager.* to 'thesis_product_user'@'localhost';
GRANT INSERT ON thesis_manager.* to 'thesis_product_user'@'localhost';
GRANT DELETE ON thesis_manager.* to 'thesis_product_user'@'localhost';
GRANT UPDATE ON thesis_manager.* to 'thesis_product_user'@'localhost';
