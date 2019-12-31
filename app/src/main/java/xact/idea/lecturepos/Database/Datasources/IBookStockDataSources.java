package xact.idea.lecturepos.Database.Datasources;

import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Model.GroupModel;
import xact.idea.lecturepos.Model.StockModel;

public interface IBookStockDataSources {
    Flowable<List<BookStock>> getBookStockItems();


    Flowable<List<BookStock>> getBookStockItemById(int BookStockItemId);

    BookStock getBookStock(String BookStockItem);

    int maxValue(String BookStockItem);
    void emptyBookStock();
    int size();
    Flowable<List<GroupModel>> getGroup(String group);
    Flowable<List<StockModel>> getBookStockModel();
    void insertToBookStock(BookStock... BookStocks);

    void updateReciver(int value,String book);
    int TotalQuantity();

    double TotalPrice();

    void updateBookStock(BookStock... BookStocks);


    void deleteBookStockItem(BookStock... BookStocks);
}
