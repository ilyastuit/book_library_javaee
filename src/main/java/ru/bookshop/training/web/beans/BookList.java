package ru.bookshop.training.web.beans;

import ru.bookshop.training.web.db.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookList {

    private final ArrayList<Book> bookList = new ArrayList<>();

    private ArrayList<Book> getBooks() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        conn = Database.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from book");
            while (rs.next()) {
                Book book = new Book();
                book.setName(rs.getString("name"));
                book.setPageCount(rs.getInt("page_count"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublishYear(rs.getDate("publish_year"));
                bookList.add(book);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bookList;
    }

    public ArrayList<Book> getBookList() {
        if (!bookList.isEmpty()) {
            return bookList;
        } else {
            return getBooks();
        }
    }
}
