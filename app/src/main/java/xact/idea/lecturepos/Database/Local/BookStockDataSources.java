package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IBookStockDataSources;
import xact.idea.lecturepos.Database.Model.BookStock;

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
    public void insertToBookStock(BookStock... BookStocks) {
        BookStockDao.insertToBookStock(BookStocks);
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
