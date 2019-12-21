package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Book;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    Flowable<List<Book>> getBookItems();

    @Query("SELECT * FROM books WHERE id=:BookItemId")
    Flowable<List<Book>> getBookItemById(int BookItemId);
    @Query("SELECT * FROM books WHERE BARCODE_NUMBER=:BookItem")
    Book getBook(String BookItem);
    @Query("SELECT * FROM books WHERE BookNo=:BookItem")
    Book getBookNo(String BookItem);

    @Query("Select Count(id)  FROM books")
    int value();


    @Query("DELETE  FROM books")
    void emptyBook();

    @Insert
    void insertToBook(Book...Books);

    @Update
    void updateBook(Book...Books);

    @Delete
    void deleteBookItem(Book...Books);

    @Query("SELECT * from books")
        //@Query("SELECT * books Book as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Book>> getBook();
}
