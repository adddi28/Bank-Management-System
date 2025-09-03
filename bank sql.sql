CREATE DATABASE bankdb;

USE bankdb;

CREATE TABLE accounts (
    acc_no INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    balance DOUBLE
);
