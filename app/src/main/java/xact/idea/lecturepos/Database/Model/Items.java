package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Items {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "Price")
    public double Price;
    @ColumnInfo(name = "Discount")
    public double Discount;
    @ColumnInfo(name = "Amount")
    public double Amount;
    @ColumnInfo(name = "Quantity")
    public int Quantity;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "BookId")
    public String BookId;

}
