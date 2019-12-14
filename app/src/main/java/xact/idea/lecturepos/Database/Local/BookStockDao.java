package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Model.StockModel;

@Dao
public interface BookStockDao {
    @Query("SELECT * FROM book_stock")
    Flowable<List<BookStock>> getBookStockItems();

    @Query("SELECT * FROM book_stock WHERE id=:BookStockItemId")
    Flowable<List<BookStock>> getBookStockItemById(int BookStockItemId);
    @Query("SELECT * FROM book_stock WHERE BOOK_ID=:BookStockItem")
    BookStock getBookStock(String BookStockItem);

    @Query("Select Count(id)  FROM book_stock")
    int value();
    @Query("Select MAX(QTY_NUMBER)  FROM book_stock WHERE BOOK_ID=:BookStockItem")
    int maxValue(String BookStockItem);
    @Query("Select SUM(QTY_NUMBER)  FROM book_stock")
    int TotalQuantity();
    @Query("Select SUM(BOOK_NET_PRICES)  FROM book_stock")
    double TotalPrice();
    @Query("UPDATE  book_stock SET QTY_NUMBER=:value WHERE BOOK_ID=:BookStockItem")
    void updateReciver(int value,String BookStockItem);

    @Query("DELETE  FROM book_stock")
    void emptyBookStock();

    @Insert
    void insertToBookStock(BookStock...book_stock);

    @Update
    void updateBookStock(BookStock...book_stock);

    @Delete
    void deleteBookStockItem(BookStock...book_stock);

    @Query("SELECT * from book_stock as bookstock inner join books as book ON bookstock.BOOK_ID=book.BookNo")
        //@Query("SELECT * book_stock BookStock as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<StockModel>> getBookStockModel();
}
