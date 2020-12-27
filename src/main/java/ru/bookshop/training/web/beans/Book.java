package ru.bookshop.training.web.beans;

import java.util.Date;

public class Book {
    private String name;
    private int pageCount;
    private String isbn;
    private Date publishYear;

    public Book() {

    }

    public Book(String name, int pageCount, String isbn, Date publishYear) {
        this.name = name;
        this.pageCount = pageCount;
        this.isbn = isbn;
        this.publishYear = publishYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Date publishYear) {
        this.publishYear = publishYear;
    }
}
