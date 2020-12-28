package ru.ilyastuit.training.web.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import ru.ilyastuit.training.web.enums.SearchType;

@ManagedBean
@RequestScoped
public class SearchTypeController {

    private static Map<String, SearchType> searchList = new HashMap<String, SearchType>();

    public SearchTypeController() {

        ResourceBundle bundle = ResourceBundle.getBundle("ru.ilyastuit.training.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.clear();
        searchList.put(bundle.getString("author_name"), SearchType.AUTHOR);
        searchList.put(bundle.getString("book_name"), SearchType.TITLE);
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }
}
