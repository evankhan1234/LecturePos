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
    @ColumnInfo(name = "BOOK_SELLING_CODE")
    public String BOOK_SELLING_CODE;
    @ColumnInfo(name = "BARCODE_NUMBER")
    public String BARCODE_NUMBER;
    @ColumnInfo(name = "BOOK_NET_PRICE")
    public String BOOK_NET_PRICE;
    @ColumnInfo(name = "BOOK_FACE_VALUE")
    public String BOOK_FACE_VALUE;

    @ColumnInfo(name = "BookPrice")
    public String BookPrice;
    @ColumnInfo(name = "BookName")
    public String BookName;
    @ColumnInfo(name = "BookNo")
    public String BookNo;
    @ColumnInfo(name = "BookNameBangla")
    public String BookNameBangla;
    @ColumnInfo(name = "BOOK_SPECIMEN_CODE")
    public String BOOK_SPECIMEN_CODE;
    @ColumnInfo(name = "F_BOOK_EDITION_NO")
    public String F_BOOK_EDITION_NO;


}
