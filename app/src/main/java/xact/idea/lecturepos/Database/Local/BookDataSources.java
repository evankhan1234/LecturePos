package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IBookDataSource;
import xact.idea.lecturepos.Database.Model.Book;

public class BookDataSources implements IBookDataSource {

    private BookDao BookDao;
    private static BookDataSources instance;

    public BookDataSources(BookDao BookDao){
        this.BookDao=BookDao;
    }
    public static BookDataSources getInstance(BookDao BookDao){
        if(instance==null)
            instance = new BookDataSources(BookDao);
        return instance;

    }



    @Override
    public Flowable<List<Book>> getBookItems() {
        return BookDao.getBookItems();
    }

    @Override
    public Flowable<List<Book>> getBookItemById(int BookItemId) {
        return BookDao.getBookItemById(BookItemId);
    }

    @Override
    public Book getBook(String BookItem) {
        return BookDao.getBook(BookItem);
    }

    @Override
    public Flowable<List<Book>> getBook(int favoriteid) {
        return BookDao.getBook();
    }

    @Override
    public void emptyBook() {
        BookDao.emptyBook();
    }

    @Override
    public int size() {
        return BookDao.value();
    }

    @Override
    public Book getBookNo(String BookItem) {
        return BookDao.getBookNo(BookItem);
    }

    @Override
    public void insertToBook(Book... carts) {

        BookDao.insertToBook(carts);
    }

    @Override
    public void updateBook(Book... carts) {

        BookDao.updateBook(carts);
    }

    @Override
    public void deleteBookItem(Book... carts) {

        BookDao.deleteBookItem(carts);
    }
}
