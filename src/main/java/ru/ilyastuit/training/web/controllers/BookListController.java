package ru.ilyastuit.training.web.controllers;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.model.LazyDataModel;
import ru.ilyastuit.training.web.db.DataHelper;
import ru.ilyastuit.training.web.entity.ext.BookExt;
import ru.ilyastuit.training.web.models.BookListDataModel;
import ru.ilyastuit.training.web.beans.Pager;
import ru.ilyastuit.training.web.enums.SearchType;

@ManagedBean(eager = true)
@SessionScoped
public class BookListController implements Serializable {

    private ResourceBundle bundle = ResourceBundle.getBundle("ru.ilyastuit.training.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    private BookExt selectedBook;
    private BookExt newBook;
    private transient DataHelper dataHelper;
    private LazyDataModel<BookExt> bookListModel;
    private Long selectedAuthorId;
    private char selectedLetter;
    private SearchType selectedSearchType = SearchType.TITLE;
    private long selectedGenreId;
    private String currentSearchString;
    private Pager pager;
    //-------
    private boolean editMode;
    private boolean addMode;

    public BookListController() {
        pager = new Pager();
        dataHelper = new DataHelper(pager);
        bookListModel = new BookListDataModel(dataHelper, pager);
    }

    public DataHelper getDataHelper() {
        return dataHelper;
    }

    public Pager getPager() {
        return pager;
    }

    private void submitValues(Character selectedLetter, long selectedGenreId) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
    }

    //<editor-fold defaultstate="collapsed" desc="запросы в базу">
    private void fillBooksAll() {
        dataHelper.getAllBooks();
    }

    public void fillBooksByGenre() {

        imitateLoading();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenreId = Long.valueOf(params.get("genre_id"));
        submitValues(' ', selectedGenreId);
        dataHelper.getBooksByGenre(selectedGenreId);

    }

    public void fillBooksByRate() {

        imitateLoading();
        dataHelper.getBooksByRate();

    }

    public void fillBooksByLetter() {

        imitateLoading();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);
        submitValues(selectedLetter, -1);
        dataHelper.getBooksByLetter(selectedLetter);

    }

    public void fillBooksBySearch() {

        imitateLoading();

        submitValues(' ', -1);

        if (currentSearchString.trim().length() == 0) {
            fillBooksAll();

        }

        if (selectedSearchType == SearchType.AUTHOR) {
            dataHelper.getBooksByAuthor(currentSearchString);
        } else if (selectedSearchType == SearchType.TITLE) {
            dataHelper.getBooksByName(currentSearchString);
        }

    }

    public void deleteBook() {
        dataHelper.deleteBook(selectedBook);
        dataHelper.populateList();

//        RequestContext.getCurrentInstance().execute("PF('dlgDeleteBook').hide()");

        PrimeFaces.current().executeScript("PF('dlgDeleteBook').hide()");


        ResourceBundle bundle = ResourceBundle.getBundle("ru.ilyastuit.training.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("deleted")));


    }

    public void rate() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int bookIndex = Integer.parseInt(params.get("bookIndex"));

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String username = facesContext.getExternalContext().getUserPrincipal().getName();

        BookExt book = pager.getList().get(bookIndex);

        dataHelper.rateBook(book, username);

    }

    public void saveBook() {

        if (!validateFields()) {
            return;
        }

        if (editMode) {
            dataHelper.updateBook(selectedBook);
        } else if (addMode) {
            dataHelper.addBook(selectedBook.getBook());
        }

        cancelModes();
        dataHelper.populateList();

        ResourceBundle bundle = ResourceBundle.getBundle("ru.ilyastuit.training.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("updated")));

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="режим редактирования">
    public void switchEditMode() {
        editMode = true;
//        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').show()");

        PrimeFaces.current().executeScript("PF('dlgEditBook').show()");

    }

    public void switchAddMode() {
        addMode = true;
        selectedBook = new BookExt();
//        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').show()");

        PrimeFaces.current().executeScript("PF('dlgEditBook').show()");


    }

    public void cancelModes() {
        if (addMode) {
            addMode = false;
        }

        if (editMode) {
            editMode = false;
        }

        if (selectedBook != null) {
            selectedBook.setUploadedContent(null);
            selectedBook.setUploadedImage(null);
        }

//        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').hide()");

        PrimeFaces.current().executeScript("PF('dlgEditBook').hide()");
    }

    //</editor-fold>
    public Character[] getRussianLetters() {
        Character[] letters = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',};
        return letters;
    }

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }

   

    //<editor-fold defaultstate="collapsed" desc="гетеры сетеры">
    public boolean isEditMode() {
        return editMode;
    }

    public boolean isAddMode() {
        return addMode;
    }

    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public SearchType getSearchType() {
        return selectedSearchType;
    }

    public void setSearchType(SearchType searchType) {
        this.selectedSearchType = searchType;
    }

    public long getSelectedGenreId() {
        return selectedGenreId;
    }

    public void setSelectedGenreId(int selectedGenreId) {
        this.selectedGenreId = selectedGenreId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char selectedLetter) {
        this.selectedLetter = selectedLetter;
    }

    public Long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    public LazyDataModel<BookExt> getBookListModel() {
        return bookListModel;
    }

    public void setSelectedBook(BookExt selectedBook) {
        this.selectedBook = selectedBook;
    }

    public BookExt getSelectedBook() {
        return selectedBook;
    }

  
    public BookExt getNewBook() {
        if (newBook == null) {
            newBook = new BookExt();
        }
        return newBook;
    }

    public void setNewBook(BookExt newBook) {
        this.newBook = newBook;
    }

    //</editor-fold>
    private void imitateLoading() {
//        try {
//            Thread.sleep(1000);// имитация загрузки процесса
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private boolean validateFields() {

        if (isNullOrEmpty(selectedBook.getAuthor())
                || isNullOrEmpty(selectedBook.getDescr())
                || isNullOrEmpty(selectedBook.getGenre())
                || isNullOrEmpty(selectedBook.getIsbn())
                || isNullOrEmpty(selectedBook.getName())
                || isNullOrEmpty(selectedBook.getPageCount())
                || isNullOrEmpty(selectedBook.getPublishYear())
                || isNullOrEmpty(selectedBook.getPublisher())) {
            failValidation(bundle.getString("error_fill_all_fields"));
            return false;

        }
        
        
        if (dataHelper.isIsbnExist(selectedBook.getIsbn(), selectedBook.getId())){
            failValidation(bundle.getString("error_isbn_exist"));
            return false;            
        }

        if (addMode) {

            if (selectedBook.getUploadedContent() == null) {
                failValidation(bundle.getString("error_load_pdf"));
                return false;
            }

            if (selectedBook.getUploadedImage() == null) {
                failValidation(bundle.getString("error_load_image"));
                return false;
            }

        }


        return true;


    }

    private boolean isNullOrEmpty(Object obj) {
        if (obj == null || obj.toString().equals("")) {
            return true;
        }

        return false;
    }

    private void failValidation(String message) {
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, bundle.getString("error")));
    }
}
