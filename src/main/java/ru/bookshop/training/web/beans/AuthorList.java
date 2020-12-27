package ru.bookshop.training.web.beans;

import ru.bookshop.training.web.db.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AuthorList {

    private List<Author> authorList = new ArrayList<>();

    private List<Author> getAuthors() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = Database.getConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM author ORDER BY fio ASC");
            while (rs.next()) {
                Author author = new Author();
                author.setName(rs.getString("fio"));
                authorList.add(author);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        return authorList
                .stream()
                .sorted(Comparator.comparing(Author::getName))
                .collect(Collectors.toList());
    }

    public List<Author> getAuthorList() {
        if (!authorList.isEmpty()) {
            return authorList;
        } else {
            return getAuthors();
        }
    }
}
