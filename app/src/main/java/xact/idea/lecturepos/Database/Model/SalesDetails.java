package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    @ColumnInfo(name = "BookId")
    public String BookId;
    @ColumnInfo(name = "Quantity")
    public int Quantity;
    @ColumnInfo(name = "MRP")
    public double MRP;
    @ColumnInfo(name = "Discount")
    public double Discount;
    @ColumnInfo(name = "TotalAmount")
    public double TotalAmount;
}
