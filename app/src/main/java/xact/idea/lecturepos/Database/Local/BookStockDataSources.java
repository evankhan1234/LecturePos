package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IBookStockDataSources;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Model.GroupModel;
import xact.idea.lecturepos.Model.StockModel;

public class BookStockDataSources implements IBookStockDataSources {
    private BookStockDao BookStockDao;
    private static BookStockDataSources instance;

    public BookStockDataSources(BookStockDao BookStockDao){
        this.BookStockDao=BookStockDao;
    }
    public static BookStockDataSources getInstance(BookStockDao BookStockDao){
        if(instance==null)
            instance = new BookStockDataSources(BookStockDao);
        return instance;

    }
    @Override
    public Flowable<List<BookStock>> getBookStockItems() {
        return BookStockDao.getBookStockItems();
    }

    @Override
    public Flowable<List<BookStock>> getBookStockItemById(int BookStockItemId) {
        return BookStockDao.getBookStockItemById(BookStockItemId);
    }

    @Override
    public BookStock getBookStock(String BookStockItem) {
        return BookStockDao.getBookStock(BookStockItem);
    }

    @Override
    public void updateReciverQuantity(int value, double price, String BookStockItem) {
        BookStockDao.updateReciverQuantity(value, price, BookStockItem);
    }

    @Override
    public int maxValue(String BookStockItem) {
        return BookStockDao.maxValue(BookStockItem);
    }


    @Override
    public void emptyBookStock() {
        BookStockDao.emptyBookStock();
    }

    @Override
    public int size() {
        return BookStockDao.value();
    }

    @Override
    public Flowable<List<GroupModel>> getGroup(String group) {
        return BookStockDao.getGroup(group);
    }

    @Override
    public Flowable<List<StockModel>> getBookStockModel() {
        return BookStockDao.getBookStockModel();
    }

    @Override
    public void insertToBookStock(BookStock... BookStocks) {
        BookStockDao.insertToBookStock(BookStocks);
    }

    @Override
    public void updateReciver(int value,String book) {
        BookStockDao.updateReciver(value,book);
    }

    @Override
    public int TotalQuantity() {
        return BookStockDao.TotalQuantity();
    }

    @Override
    public double TotalPrice() {
        return BookStockDao.TotalPrice();
    }

    @Override
    public void updateBookStock(BookStock... BookStocks) {
        BookStockDao.updateBookStock(BookStocks);
    }

    @Override
    public void deleteBookStockItem(BookStock... BookStocks) {
        BookStockDao.deleteBookStockItem(BookStocks);
    }
}
