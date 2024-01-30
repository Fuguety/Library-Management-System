package org.example;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface Dao
{

    // Books
    Book addBook(String isbn, String title) throws SQLException;
    Book findBookByIsbn(String isbn) throws SQLException;

    // Users
    User addUser(String id, String name) throws SQLException;
    User findUserById(String id) throws SQLException;
    User findUserByCode(String code) throws SQLException;

    // Borrow
    Borrow borrowBook(String id, String isbn, java.sql.Date loanDate) throws SQLException;
    List<Book> getBooksNotBorrowed() throws SQLException;
    void returnBook(String isbn, java.sql.Date returnDate) throws SQLException;

    List<Borrow> historyBook(String isbn) throws SQLException;

    List<User> historyUsers() throws SQLException;
}
