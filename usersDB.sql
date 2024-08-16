CREATE TABLE users (
    username VARCHAR2(50) PRIMARY KEY,
    name VARCHAR2(100),
    password VARCHAR2(50),
    transaction_id NUMBER,
    transaction_type VARCHAR2(50),
    amount NUMBER,
    transaction_time TIMESTAMP,
    status VARCHAR2(50)
);

INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('johnDoe', 'John Doe', 'password123', 1001, 'deposit', 250.75, SYSTIMESTAMP, 'completed');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('janeSmith', 'Jane Smith', 'password456', 1002, 'withdrawal', 100.50, SYSTIMESTAMP, 'pending');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('aliceJones', 'Alice Jones', 'password789', 1003, 'deposit', 500.00, SYSTIMESTAMP, 'completed');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('bobBrown', 'Bob Brown', 'password012', 1004, 'transfer', 150.25, SYSTIMESTAMP, 'failed');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('carolWhite', 'Carol White', 'password345', 1005, 'deposit', 75.00, SYSTIMESTAMP, 'completed');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('keerthanaB', 'Keerthana B', 'password123', 1001, 'deposit', 250.75, SYSTIMESTAMP, 'completed');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('bhavanshG', 'Bhavansh G', 'password456', 1002, 'withdrawal', 100.50, SYSTIMESTAMP, 'pending');
INSERT INTO users (username, name, password, transaction_id, transaction_type, amount, transaction_time, status) VALUES ('alisonRose', 'Alison Rose', 'password789', 1003, 'deposit', 500.00, SYSTIMESTAMP, 'completed');

SELECT * FROM users;
COMMIT;
