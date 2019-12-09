package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;

@Dao
public interface SaleMastersDao {
    @Query("SELECT * FROM sales_mst")
    Flowable<List<SalesMaster>> getSalesDetailsItems();

    @Query("SELECT * FROM sales_mst WHERE id=:SalesDetailsItemId")
    Flowable<List<SalesMaster>> getSalesDetailsItemById(int SalesDetailsItemId);

    @Query("Select Count(id)  FROM sales_mst")
    int value();
    @Query("Select MAX(id)  FROM sales_mst")
    int maxValue();

    @Query("SELECT * FROM sales_mst WHERE InvoiceDate BETWEEN :from AND :to")
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to);
    @Query("DELETE  FROM sales_mst")
    void emptySalesDetails();

    @Insert
    void insertToSalesDetails(SalesMaster...sales_mst);

    @Update
    void updateSalesDetails(SalesMaster...sales_mst);

    @Delete
    void deleteSalesDetailsItem(SalesMaster...sales_mst);

    @Query("SELECT * from sales_mst")
        //@Query("SELECT * sales_mst SalesDetails as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<SalesMaster>> getSalesDetails();
}
