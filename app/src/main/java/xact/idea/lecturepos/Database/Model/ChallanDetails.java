package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "challan_details")
public class ChallanDetails {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "F_BOOK_NO")
    public String  F_BOOK_NO;
    @ColumnInfo(name = "BOOK_NET_PRICE")
    public String  BOOK_NET_PRICE;
    @ColumnInfo(name = "CHALLAN_BOOK_QTY")
    public String  CHALLAN_BOOK_QTY;
    @ColumnInfo(name = "F_CHALLAN_NO")
    public String  F_CHALLAN_NO;
}
