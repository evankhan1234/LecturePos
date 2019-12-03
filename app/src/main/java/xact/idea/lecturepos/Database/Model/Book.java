package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "BookCode")
    public String BookCode;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "BookNo")
    public String BookNo;
    @ColumnInfo(name = "BookPrice")
    public double BookPrice;

}
