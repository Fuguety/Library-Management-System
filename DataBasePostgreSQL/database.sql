CREATE SCHEMA IF NOT EXISTS public;


DROP TABLE IF EXISTS BORROW;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS BOOKS;

CREATE TABLE BOOKS (
                       ISBN VARCHAR(12) PRIMARY KEY,
                       TITLE VARCHAR(100),
                       STATUS VARCHAR(9) CHECK (STATUS IN ('AVAILABLE', 'OCCUPIED')),
                       LOCALIZER VARCHAR(8)
);

CREATE TABLE USERS (
                       SEQ_CODE VARCHAR(6) PRIMARY KEY,
                       NAME VARCHAR(100),
                       ID VARCHAR(12)
);

CREATE TABLE BORROW (
                        LOCATION_CODE VARCHAR(8) PRIMARY KEY,
                        ISBN VARCHAR(12),
                        MEMBER_CODE VARCHAR(6),
                        LOAN_DATE DATE,
                        RETURN_DATE DATE,
                        FOREIGN KEY (ISBN) REFERENCES BOOKS(ISBN),
                        FOREIGN KEY (MEMBER_CODE) REFERENCES USERS(SEQ_CODE)
);

INSERT INTO BOOKS (isbn, title, status, localizer)
VALUES ('111111111111', 'Harry Potter', 'AVAILABLE', NULL),
       ('333333333333', 'Star Wars', 'AVAILABLE', NULL);


INSERT INTO USERS (SEQ_CODE, NAME, ID)
VALUES ('001', 'John Doe', '1234567890'),
       ('002', 'Jane Smith', '0987654321');

INSERT INTO BORROW (LOCATION_CODE, ISBN, MEMBER_CODE, LOAN_DATE, RETURN_DATE)
VALUES ('L001', '111111111111', '001', '2024-01-20', '2024-02-10'),
       ('L002', '333333333333', '002', '2024-01-22', NULL);


-- Adding a constraint to the USERS table
ALTER TABLE USERS
    ADD CONSTRAINT unique_id UNIQUE (ID);
