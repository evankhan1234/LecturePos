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
    @Override
    public String toString() {
        return Name;
    }

}
