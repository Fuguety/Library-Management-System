package org.example;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            // MAKE SURE TO WTCH THIS VIDEO TO SETUP THE POSTGRES https://www.youtube.com/watch?v=RdPYA-wDhTA


            // BEFORE ANYTHING ELSE ... MAKE SURE TO RUN THE database.sql RUN IT EVERYTIME U WANT TO EXECUTE THE PROJECT

            // IF U DON'T WANT TO RUN THE SQL FILE EVERYTIME, MAKE SURE TO DELETE THE SAMPLE PART AT THE END OF THE SQL FILE
            // ALSO ADAPT THE MAIN ACCORDINGLY :)))))))

            DatabaseDao dao = new DatabaseDao("postgres", "password");

            // Books
            Book newBook = dao.addBook("123456789012", "Sample Book");
            Book foundBook = dao.findBookByIsbn("123456789012");

            // User
            User newUser = dao.addUser("123456789", "John Doe");
            User foundUserById = dao.findUserById("123456789");
            User foundUserByCode = dao.findUserByCode(newUser.getCode());


            // Borrow 1.0
            String isbnToBorrow = "123456789012";
            String userId = "123456789";
            java.sql.Date loanDate = new java.sql.Date(new Date().getTime());
            Borrow borrowedBook = dao.borrowBook(userId, isbnToBorrow, loanDate);

            // History
            String isbnToCheck = "123456789012";
            List<Borrow> historicalBookList = dao.historyBook(isbnToCheck);
            List<User> historicalUserList = dao.historyUsers();


            // Borrow 2.0 ... MAKE SURE TO COMMENT THE BORROW 1.0 AND HISTORY PART
//            dao.borrowBook(foundUserById.getId(), foundBook.getISBN(), new java.sql.Date(System.currentTimeMillis()));
//            List<Book> booksNotBorrowed = dao.getBooksNotBorrowed();
//            dao.returnBook(foundBook.getISBN(), new java.sql.Date(System.currentTimeMillis()));
//            booksNotBorrowed = dao.getBooksNotBorrowed();


            dao.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
