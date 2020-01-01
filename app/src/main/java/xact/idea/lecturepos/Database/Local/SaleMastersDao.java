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
    @Query("SELECT * FROM sales_mst WHERE InvoiceId=:SalesDetailsItemId")
    SalesMaster getSalesMaster(String SalesDetailsItemId);
    @Query("SELECT * FROM sales_mst WHERE CustomerName=:SalesDetailsItemId")
    Flowable<List<SalesMaster>> getSalesMasterList(String SalesDetailsItemId);
    @Query("Select Count(id)  FROM sales_mst")
    int value();
    @Query("Select MAX(id)  FROM sales_mst  WHERE DateSimple=:date AND TrnType=:trnType")
    int maxValue(String date,String trnType);
    @Query("Select *  FROM sales_mst WHERE id=:id")
    SalesMaster invoice(int id);

    @Query("SELECT * FROM sales_mst WHERE TrnType=:Trn AND InvoiceDate BETWEEN :from AND :to order By InvoiceDate Desc")
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to,String Trn);
    @Query("SELECT * FROM sales_mst WHERE  TrnType=:Trn AND InvoiceDate BETWEEN :from AND :to AND CustomerName=:Name order By InvoiceDate Desc")
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDateByName(Date from, Date to,String Name,String Trn);
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
