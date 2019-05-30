
CREATE DATABASE thesis_product;

CREATE USER 'thesis_prod_user'@'localhost' IDENTIFIED BY 'th-pass';

GRANT SELECT ON thesis_product.* to 'thesis_prod_user'@'localhost';
GRANT INSERT ON thesis_product.* to 'thesis_prod_user'@'localhost';
GRANT DELETE ON thesis_product.* to 'thesis_prod_user'@'localhost';
GRANT UPDATE ON thesis_product.* to 'thesis_prod_user'@'localhost';
