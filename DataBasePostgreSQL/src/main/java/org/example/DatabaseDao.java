package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.Driver;
import java.util.UUID;

public class DatabaseDao implements Dao
{
    private Connection connection;

    public DatabaseDao(String username, String password) throws SQLException {
        DriverManager.registerDriver(new Driver());
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", username, password);
    }


    // Add Books
    @Override
    public Book addBook(String isbn, String title) throws SQLException
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO books (ISBN, TITLE, STATUS, LOCALIZER) VALUES (?, ?, 'AVAILABLE', NULL) RETURNING *")) {
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, title);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Book.builder()
                        .withISBN(resultSet.getString("ISBN"))
                        .withTITLE(resultSet.getString("TITLE"))
                        .withSTATE(resultSet.getString("STATUS"))
                        .withLOCALIZER(resultSet.getString("LOCALIZER"))
                        .build();
            }
        }
        return null;
    }

    @Override
    public Book findBookByIsbn(String isbn) throws SQLException
    {
        final String SQL = "SELECT ISBN, TITLE, STATUS, LOCALIZER FROM books WHERE ISBN = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, isbn);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Book.builder()
                        .withISBN(resultSet.getString("ISBN"))
                        .withTITLE(resultSet.getString("TITLE"))
                        .withSTATE(resultSet.getString("STATUS"))
                        .withLOCALIZER(resultSet.getString("LOCALIZER"))
                        .build();
            }
        }
        return null;
    }



    // Add Users
    @Override
    public User addUser(String id, String name) throws SQLException
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users (SEQ_CODE, NAME, ID) VALUES (?, ?, ?) RETURNING SEQ_CODE")) {

            String seqCode = generateUniqueSeqCode();

            preparedStatement.setString(1, seqCode);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return User.builder()
                        .withCode(resultSet.getString("SEQ_CODE"))
                        .withName(name)
                        .withId(id)
                        .build();
            }
        }
        return null;
    }


    @Override
    public User findUserById(String id) throws SQLException
    {
        final String SQL = "SELECT SEQ_CODE, NAME, ID FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return User.builder()
                        .withCode(resultSet.getString("SEQ_CODE"))
                        .withName(resultSet.getString("NAME"))
                        .withId(resultSet.getString("ID"))
                        .build();
            }
        }
        return null;
    }

    @Override
    public User findUserByCode(String code) throws SQLException
    {
        final String SQL = "SELECT SEQ_CODE, NAME, ID FROM users WHERE SEQ_CODE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return User.builder()
                        .withCode(resultSet.getString("SEQ_CODE"))
                        .withName(resultSet.getString("NAME"))
                        .withId(resultSet.getString("ID"))
                        .build();
            }
        }

        // Return null if no user with the given code is found
        return null;
    }


    @Override
    public Borrow borrowBook(String id, String isbn, Date loanDate) throws SQLException
    {
        Book book = findBookByIsbn(isbn);

        if (book == null || !"AVAILABLE".equals(book.getSTATE())) {
            System.out.println("Book not found or unavailable");
            return null;
        }

        User user = findUserById(id);

        if (user == null) {
            System.out.println("User not found");
            return null;
        }

        // Proceed with the borrowing
        try (PreparedStatement insertBorrow = connection.prepareStatement(
                "INSERT INTO BORROW (LOCATION_CODE, ISBN, MEMBER_CODE, LOAN_DATE, RETURN_DATE) VALUES (?, ?, ?, ?, NULL) RETURNING *");
             PreparedStatement updateBook = connection.prepareStatement(
                     "UPDATE BOOKS SET STATUS = 'OCCUPIED', LOCALIZER = ? WHERE ISBN = ?")) {

            // Generate a unique location code (You need to implement this method)
            String locationCode = generateUniqueSeqCode();

            insertBorrow.setString(1, locationCode);
            insertBorrow.setString(2, isbn);
            insertBorrow.setString(3, user.getCode());
            insertBorrow.setDate(4, loanDate);

            ResultSet borrowResult = insertBorrow.executeQuery();

            if (borrowResult.next()) {
                updateBook.setString(1, locationCode);
                updateBook.setString(2, isbn);
                updateBook.executeUpdate();

                return Borrow.builder()
                        .withLocationCode(locationCode)
                        .withIsbn(isbn)
                        .withMemberCode(user.getCode())
                        .withLoanDate(loanDate)
                        .withReturnDate(null)  // Return date is initially null
                        .build();
            }
        }

        return null;
    }

    @Override
    public List<Book> getBooksNotBorrowed() throws SQLException
    {
        final String SQL = "SELECT ISBN, TITLE, STATUS, LOCALIZER FROM BOOKS WHERE STATUS = 'AVAILABLE'";
        List<Book> availableBooks = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("ISBN"),
                        resultSet.getString("TITLE"),
                        resultSet.getString("STATUS"),
                        resultSet.getString("LOCALIZER")
                );

                availableBooks.add(book);
            }
        }

        return availableBooks;
    }

    @Override
    public void returnBook(String isbn, Date returnDate) throws SQLException
    {
        // Check if the book exists and is currently borrowed
        Book book = findBookByIsbn(isbn);

        if (book == null || !"OCCUPIED".equals(book.getSTATE()) || book.getLOCALIZER() == null) {
            // Handle the case when the book is not currently borrowed
            return;
        }

        try (PreparedStatement updateBorrow = connection.prepareStatement(
                "UPDATE BORROW SET RETURN_DATE = ? WHERE LOCATION_CODE = ?");
             PreparedStatement updateBook = connection.prepareStatement(
                     "UPDATE BOOKS SET STATUS = 'AVAILABLE', LOCALIZER = NULL WHERE ISBN = ?")) {

            updateBorrow.setDate(1, returnDate);
            updateBorrow.setString(2, book.getLOCALIZER());
            updateBorrow.executeUpdate();

            updateBook.setString(1, isbn);
            updateBook.executeUpdate();
        }
    }


    @Override
    public List<Borrow> historyBook(String isbn) throws SQLException {
        final String SQL = "SELECT * FROM BORROW WHERE ISBN = ? ORDER BY LOAN_DATE";
        List<Borrow> borrows = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, isbn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Borrow borrow = Borrow.builder()
                            .withLocationCode(resultSet.getString("LOCATION_CODE"))
                            .withIsbn(resultSet.getString("ISBN"))
                            .withMemberCode(resultSet.getString("MEMBER_CODE"))
                            .withLoanDate(resultSet.getDate("LOAN_DATE"))
                            .withReturnDate(resultSet.getDate("RETURN_DATE"))
                            .build();
                    borrows.add(borrow);
                }
            }
        }

        return borrows;
    }

    @Override
    public List<User> historyUsers() throws SQLException {
        final String SQL = "SELECT DISTINCT MEMBER_CODE FROM BORROW";
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {

            while (resultSet.next()) {
                String memberCode = resultSet.getString("MEMBER_CODE");
                User user = findUserByCode(memberCode);
                if (user != null) {
                    users.add(user);
                }
            }
        }

        return users;
    }


    private String generateUniqueSeqCode()
    {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 6);
    }

    public void close() throws SQLException
    {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
