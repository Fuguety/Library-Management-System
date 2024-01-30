# Library Management System 📚

## Setup PostgreSQL Database

Before you proceed with the project, make sure to watch this video to set up PostgreSQL: [PostgreSQL Setup Video](https://www.youtube.com/watch?v=RdPYA-wDhTA)

Also make sure you have Docker installed

> [![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.youtube.com/watch?v=RdPYA-wDhTA)</br>
> [![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
](https://www.docker.com)

## Initial Setup

1. Run the `database.sql` script every time you want to execute the project.

2. If you prefer not to run the SQL file every time, ensure to delete the sample data part at the end of the SQL file. Adjust the `Main` class accordingly.

## Project Structure

The project includes a Library Management System with the following features:

### Books

- **Add a New Book:**
    - *Description:* Adds a new book to the database.
    - *SQL:* `INSERT INTO BOOKS (ISBN, TITLE, STATUS, LOCALIZER) VALUES (?, ?, 'AVAILABLE', NULL) RETURNING *`.

- **Find a Book by ISBN:**
    - *Description:* Searches for a book by its ISBN in the database.
    - *SQL:* `SELECT ISBN, TITLE, STATUS, LOCALIZER FROM BOOKS WHERE ISBN = ?`.

### Users

- **Add a New User:**
    - *Description:* Adds a new user to the database.
    - *SQL:* `INSERT INTO USERS (SEQ_CODE, NAME, ID) VALUES (NEXTVAL('SEQ_SOCIOS'), ?, ?) RETURNING *`.

- **Find a User by ID:**
    - *Description:* Searches for a user by their ID in the database.
    - *SQL:* `SELECT SEQ_CODE, NAME, ID FROM USERS WHERE ID = ?`.

- **Find a User by Code:**
    - *Description:* Searches for a user by their code in the database.
    - *SQL:* `SELECT SEQ_CODE, NAME, ID FROM USERS WHERE SEQ_CODE = ?`.

### Borrow

- **Borrow a Book:**
    - *Description:* Performs a book loan to a user, updating the `BORROW` and `BOOKS` tables.
    - *SQL:* `INSERT INTO BORROW (LOCATION_CODE, ISBN, MEMBER_CODE, LOAN_DATE, RETURN_DATE) VALUES (NEXTVAL('SEQ_PRESTAMOS'), ?, ?, ?, NULL); UPDATE BOOKS SET STATUS = 'OCCUPIED', LOCALIZER = CURRVAL('SEQ_PRESTAMOS') WHERE ISBN = ?`.

- **Get Books Not Borrowed:**
    - *Description:* Retrieves books that have never been borrowed.
    - *SQL:* `SELECT ISBN, TITLE, STATUS, LOCALIZER FROM BOOKS WHERE STATUS = 'AVAILABLE' AND LOCALIZER IS NULL`.

- **Return a Book:**
    - *Description:* Returns a book, updating the `BORROW` and `BOOKS` tables.
    - *SQL:* `UPDATE BORROW SET RETURN_DATE = ? WHERE ISBN = ? AND RETURN_DATE IS NULL; UPDATE BOOKS SET STATUS = 'AVAILABLE', LOCALIZER = NULL WHERE ISBN = ?`.

### History

#### Historical Book List

- **Retrieve Historical Book List:**
    - *Description:* Retrieves a list of loans for a given book ISBN. The list includes the user code, user name, loan date, and return date (or "En préstamo" if not returned).
    - *SQL:* `SELECT LOCATION_CODE, ISBN, MEMBER_CODE, LOAN_DATE, RETURN_DATE FROM BORROW WHERE ISBN = ? ORDER BY LOAN_DATE`.

#### Historical User List

- **Retrieve Historical User List:**
    - *Description:* Retrieves a list of all users and, for each user, a list of books borrowed. Displays user DNI, name, and a list of borrowed books with ISBN, title, loan date, and return date (or "En préstamo" if not returned).
    - *SQL:* `SELECT U.SEQ_CODE, U.NAME, U.ID, B.LOCATION_CODE, B.ISBN, B.LOAN_DATE, B.RETURN_DATE FROM USERS U LEFT JOIN BORROW B ON U.SEQ_CODE = B.MEMBER_CODE ORDER BY B.LOAN_DATE`.

### Note

- Comment out the Borrow 1.0 and History sections in the `Main` class if using Borrow 2.0.

Feel free to adapt the project to your needs.😊
</br></br>

#### Code made with

> ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=black) </br>
> ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) </br>
> ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)</br>
> ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

This project is licensed under the **[MIT License](https://opensource.org/license/mit/).**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
