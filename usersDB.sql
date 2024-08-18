SET WRAP ON;
SET LINESIZE 32767;
DROP TABLE USERS;
CREATE TABLE users (
    username VARCHAR2(20) PRIMARY KEY,
    name VARCHAR2(20),
    password VARCHAR2(20),
    transaction_id NUMBER(15),
    transaction_type VARCHAR2(20),
    amount BINARY_DOUBLE,
    transaction_time TIMESTAMP(0),
    status VARCHAR2(20)
);


COLUMN transaction_time FORMAT A20;

SELECT * FROM users;
COMMIT;