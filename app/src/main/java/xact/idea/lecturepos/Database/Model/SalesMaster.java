package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sales_mst")
public class SalesMaster {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "InvoiceId")
    public String InvoiceId;
    @ColumnInfo(name = "InvoiceNumber")
    public String InvoiceNumber;
    @ColumnInfo(name = "StoreId")
    public String StoreId;
    @ColumnInfo(name = "InvoiceDate")
    public Date InvoiceDate;
    @ColumnInfo(name = "Discount")
    public double Discount;
    @ColumnInfo(name = "InvoiceAmount")
    public double InvoiceAmount;
    @ColumnInfo(name = "RetailCode")
    public String RetailCode;
    @ColumnInfo(name = "NetValue")
    public double NetValue;
    @ColumnInfo(name = "PayMode")
    public String PayMode;
    @ColumnInfo(name = "Device")
    public String Device;
    @ColumnInfo(name = "update_date")
    public Date update_date;
    @ColumnInfo(name = "CustomerName")
    public String CustomerName;
    @ColumnInfo(name = "Note")
    public String Note;
    @ColumnInfo(name = "InvoiceDates")
    public String InvoiceDates;
    @ColumnInfo(name = "DateSimple")
    public String DateSimple;
    @ColumnInfo(name = "PhoneNumber")
    public String PhoneNumber;
    @ColumnInfo(name = "SubTotal")
    public String SubTotal;
    @ColumnInfo(name = "Return")
    public String Return;
    @ColumnInfo(name = "Date")
    public Date  Date;
    @ColumnInfo(name = "TrnType")
    public String TrnType;
    @ColumnInfo(name = "UpdateNo")
    public int UpdateNo;


}
