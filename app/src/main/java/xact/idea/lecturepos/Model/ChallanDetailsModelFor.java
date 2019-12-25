package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class ChallanDetailsModelFor {
    @ColumnInfo(name = "F_BOOK_NO")
    public String  F_BOOK_NO;
    @ColumnInfo(name = "BOOK_NET_PRICE")
    public String  BOOK_NET_PRICE;
    @ColumnInfo(name = "CHALLAN_BOOK_QTY")
    public String  CHALLAN_BOOK_QTY;
    @SerializedName("F_CHALLAN_NO")
    public String  F_CHALLAN_NO;
    @SerializedName("BookName")
    public String  BookName;
}
