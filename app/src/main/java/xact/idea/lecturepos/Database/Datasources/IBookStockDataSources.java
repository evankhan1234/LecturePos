package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.BookStock;

public interface IBookStockDataSources {
    Flowable<List<BookStock>> getBookStockItems();


    Flowable<List<BookStock>> getBookStockItemById(int BookStockItemId);

    BookStock getBookStock(String BookStockItem);

    int maxValue(String BookStockItem);
    void emptyBookStock();
    int size();


    void insertToBookStock(BookStock... BookStocks);


    void updateBookStock(BookStock... BookStocks);


    void deleteBookStockItem(BookStock... BookStocks);
}
