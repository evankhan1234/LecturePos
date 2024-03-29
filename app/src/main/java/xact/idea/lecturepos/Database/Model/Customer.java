package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "retail_customers")
public class Customer {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "StoreId")
    public String StoreId;
    @ColumnInfo(name = "RetailerCode")
    public String RetailerCode;
    @ColumnInfo(name = "Name")
    public String Name;
    @ColumnInfo(name = "Address")
    public String Address;
    @ColumnInfo(name = "MobileNumber")
    public String MobileNumber;
    @ColumnInfo(name = "ShopName")
    public String ShopName;
    @ColumnInfo(name = "UpdateNo")
    public String UpdateNo;
    @ColumnInfo(name = "UpdateDate")
    public String UpdateDate;
    @ColumnInfo(name = "Status")
    public String Status;
    @Override
    public String toString() {
        return Name;
    }

}
