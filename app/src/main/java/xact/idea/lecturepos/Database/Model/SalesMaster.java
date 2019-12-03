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
}
