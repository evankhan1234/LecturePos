package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sync")
public class Sync {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "CUSTOMER_CODE")
    public String CUSTOMER_CODE;
    @ColumnInfo(name = "TABLE_NAME")
    public String TABLE_NAME;
    @ColumnInfo(name = "SYNC_DATETIME")
    public Date SYNC_DATETIME;
    @ColumnInfo(name = "SYNC_NUMBER")
    public int SYNC_NUMBER;
    @ColumnInfo(name = "DEVICE")
    public String DEVICE;
}
