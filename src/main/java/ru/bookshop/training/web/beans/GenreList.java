package ru.bookshop.training.web.beans;

import ru.bookshop.training.web.db.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GenreList {

    private final ArrayList<Genre> genreList = new ArrayList<>();

    private ArrayList<Genre> getGenres() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        conn = Database.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from genre");
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setName(rs.getString("name"));
                genreList.add(genre);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return genreList;
    }

    public ArrayList<Genre> getBookList() {
        if (!genreList.isEmpty()) {
            return genreList;
        } else {
            return getGenres();
        }
    }

}
