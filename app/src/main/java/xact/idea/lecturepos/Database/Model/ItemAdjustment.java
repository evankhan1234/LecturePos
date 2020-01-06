package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "itemAdjustment")
public class ItemAdjustment {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "Quantity")
    public int Quantity;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "BookNameBangla")
    public String BookNameBangla;
    @ColumnInfo(name = "BookId")
    public String BookId;
    @ColumnInfo(name = "InOut")
    public String InOut;
    @ColumnInfo(name = "stock")
    public int stock;
}
