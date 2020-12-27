package ru.bookshop.training.web.beans;

public class Author {

    private String name;
    private long id;
    
    public Author() {
    }

    public Author(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

}
