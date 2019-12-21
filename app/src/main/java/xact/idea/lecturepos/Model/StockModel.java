package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class StockModel {
    public int id;
    @SerializedName("BOOK_NET_PRICES")
    public String  BOOK_NET_PRICES;
    @SerializedName("BookName")
    public String  BookName;
    @SerializedName("QTY_NUMBER")
    public int  QTY_NUMBER;
    @SerializedName("BOOK_NET_MRP")
    public String  BOOK_NET_MRP;

    @SerializedName("BOOK_SPECIMEN_CODE")
    public String  BOOK_SPECIMEN_CODE;
    @SerializedName("BARCODE_NUMBER")
    public String  BARCODE_NUMBER;


}
