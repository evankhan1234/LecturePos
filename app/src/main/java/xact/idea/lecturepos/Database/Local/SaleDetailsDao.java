package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;
import xact.idea.lecturepos.Model.StockModel;

@Dao
public interface SaleDetailsDao {
    @Query("SELECT * FROM sales_dtl")
    Flowable<List<SalesDetails>> getSalesDetailsItems();

    @Query("SELECT * FROM sales_dtl WHERE InvoiceIdNew=:SalesDetailsItemId")
    Flowable<List<SalesDetails>> getSalesDetailsItemById(String SalesDetailsItemId);
    @Query("SELECT * FROM sales_dtl WHERE InvoiceIdNew=:SalesDetailsItemId")
    SalesDetails getSalesMaster(String SalesDetailsItemId);
    @Query("Select Count(id)  FROM sales_dtl")
    int value();
    @Query("SELECT * FROM sales_dtl WHERE InvoiceIdNew=:SalesDetailsItemId AND InvoiceDate BETWEEN :from AND :to order By InvoiceDate Desc")
    Flowable<List<SalesDetails>> getSalesDetailsItemByDate(String SalesDetailsItemId, Date from, Date to);

    @Query("DELETE  FROM sales_dtl")
    void emptySalesDetails();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertToSalesDetails(SalesDetails...sales_dtl);

    @Update
    void updateSalesDetails(SalesDetails...sales_dtl);

    @Delete
    void deleteSalesDetailsItem(SalesDetails...sales_dtl);

    @Query("SELECT * from sales_dtl")
        //@Query("SELECT * sales_dtl SalesDetails as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<SalesDetails>> getSalesDetails();

    @Query("SELECT * from sales_dtl as sales inner join books as book ON sales.BookId=book.BookNo WHERE InvoiceIdNew=:SalesDetailsItemId")
        //@Query("SELECT * book_stock BookStock as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<SalesDetailPrintModel>> getBookStockModel(String SalesDetailsItemId);
}
