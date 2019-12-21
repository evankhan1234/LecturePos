package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;

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


    @Query("DELETE  FROM sales_dtl")
    void emptySalesDetails();

    @Insert
    void insertToSalesDetails(SalesDetails...sales_dtl);

    @Update
    void updateSalesDetails(SalesDetails...sales_dtl);

    @Delete
    void deleteSalesDetailsItem(SalesDetails...sales_dtl);

    @Query("SELECT * from sales_dtl")
        //@Query("SELECT * sales_dtl SalesDetails as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<SalesDetails>> getSalesDetails();
}
