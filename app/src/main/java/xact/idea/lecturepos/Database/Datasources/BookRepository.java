package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Customer;

public class BookRepository implements IBookDataSource {
    public IBookDataSource IBookDataSource;
    public BookRepository(IBookDataSource IBookDataSource){
        this.IBookDataSource=IBookDataSource;
    }
    private static  BookRepository instance;

    public static BookRepository getInstance(IBookDataSource iCartDataSource){
        if(instance==null)
            instance= new BookRepository(iCartDataSource);
        return instance;

    }

    @Override
    public Flowable<List<Book>> getBookItems() {
        return IBookDataSource.getBookItems();
    }

    @Override
    public Flowable<List<Book>> getBookItemById(int BookItemId) {
        return IBookDataSource.getBookItemById(BookItemId);
    }

    @Override
    public Book getBook(String BookItem) {
        return IBookDataSource.getBook(BookItem);
    }

    @Override
    public Book getBookItemFor(String BookItem, String Group) {
        return IBookDataSource.getBookItemFor(BookItem,Group);
    }

    @Override
    public Flowable<List<Book>> getBook(int favoriteid) {
        return IBookDataSource.getBook(favoriteid);
    }

    @Override
    public void emptyBook() {
        IBookDataSource.emptyBook();
    }

    @Override
    public int size() {
        return IBookDataSource.size();
    }

    @Override
    public Book getBookNo(String BookItem) {
        return IBookDataSource.getBookNo(BookItem);
    }

    @Override
    public void insertToBook(Book... Books) {
        IBookDataSource.insertToBook(Books);
    }

    @Override
    public void updateBook(Book... Books) {
        IBookDataSource.updateBook(Books);
    }

    @Override
    public void deleteBookItem(Book... Books) {
        IBookDataSource.deleteBookItem(Books);
    }
}