package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "book_stock")
public class BookStock {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "STORE_ID")
    public String  STORE_ID;
    @ColumnInfo(name = "BOOK_ID")
    public String  BOOK_ID;
    @ColumnInfo(name = "QTY_NUMBER")
    public int  QTY_NUMBER;
    @ColumnInfo(name = "LAST_UPDATE_DATE")
    public String  LAST_UPDATE_DATE;
    @ColumnInfo(name = "BOOK_NET_PRICE")
    public double  BOOK_NET_PRICE;
    @ColumnInfo(name = "LAST_UPDATE_DATE_APP")
    public Date LAST_UPDATE_DATE_APP;
}
