package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "itemReturn")
public class ItemReturn {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "Price")
    public double Price;
    @ColumnInfo(name = "ValuePrice")
    public double ValuePrice;
    @ColumnInfo(name = "Discount")
    public double Discount;
    @ColumnInfo(name = "Amount")
    public double Amount;
    @ColumnInfo(name = "Quantity")
    public int Quantity;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "BookNameBangla")
    public String BookNameBangla;
    @ColumnInfo(name = "BookId")
    public String BookId;
    @ColumnInfo(name = "Stock")
    public String Stock;
}
