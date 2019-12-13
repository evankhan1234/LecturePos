package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.BookStock;

public class BookStockRepository implements IBookStockDataSources {
    public IBookStockDataSources IBookStockDataSources;
    public BookStockRepository(IBookStockDataSources IBookStockDataSources){
        this.IBookStockDataSources=IBookStockDataSources;
    }
    private static  BookStockRepository instance;

    public static BookStockRepository getInstance(IBookStockDataSources iCartDataSource){
        if(instance==null)
            instance= new BookStockRepository(iCartDataSource);
        return instance;

    }

    @Override
    public Flowable<List<BookStock>> getBookStockItems() {
        return IBookStockDataSources.getBookStockItems();
    }

    @Override
    public Flowable<List<BookStock>> getBookStockItemById(int BookStockItemId) {
        return IBookStockDataSources.getBookStockItemById(BookStockItemId);
    }

    @Override
    public BookStock getBookStock(String BookStockItem) {
        return IBookStockDataSources.getBookStock(BookStockItem);
    }

    @Override
    public int maxValue(String BookStockItem) {
        return IBookStockDataSources.maxValue(BookStockItem);
    }


    @Override
    public void emptyBookStock() {
        IBookStockDataSources.emptyBookStock();
    }

    @Override
    public int size() {
        return IBookStockDataSources.size();
    }

    @Override
    public void insertToBookStock(BookStock... BookStocks) {
        IBookStockDataSources.insertToBookStock(BookStocks);
    }

    @Override
    public void updateBookStock(BookStock... BookStocks) {
        IBookStockDataSources.updateBookStock(BookStocks);
    }

    @Override
    public void deleteBookStockItem(BookStock... BookStocks) {
        IBookStockDataSources.deleteBookStockItem(BookStocks);
    }
}
