package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Book;

public interface IBookDataSource {
    Flowable<List<Book>> getBookItems();


    Flowable<List<Book>> getBookItemById(int BookItemId);

    Book getBook(String BookItem);
    Flowable<List<Book>> getBook(int favoriteid);

    void emptyBook();
    int size();

    Book getBookNo(String BookItem);
    void insertToBook(Book... Books);


    void updateBook(Book... Books);


    void deleteBookItem(Book... Books);
}
