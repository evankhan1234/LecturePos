package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sales_dtl")
public class SalesDetails {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "StoreId")
    public String StoreId;
    @ColumnInfo(name = "InvoiceId")
    public int InvoiceId;
    @ColumnInfo(name = "InvoiceIdNew")
    public String InvoiceIdNew;
    @ColumnInfo(name = "BookId")
    public String BookId;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "Quantity")
    public int Quantity;
    @ColumnInfo(name = "MRP")
    public double MRP;
    @ColumnInfo(name = "Discount")
    public double Discount;
    @ColumnInfo(name = "TotalAmount")
    public double TotalAmount;
    @ColumnInfo(name = "UpdateNo")
    public int UpdateNo;
    @ColumnInfo(name = "QTY")
    public int QTY;
    @ColumnInfo(name = "InvoiceDate")
    public Date InvoiceDate;
}
