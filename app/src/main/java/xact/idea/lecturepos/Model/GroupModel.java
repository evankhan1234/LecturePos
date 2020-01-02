package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class GroupModel {
    @SerializedName("BOOK_NET_PRICES")
    public String  BOOK_NET_PRICES;
    @SerializedName("BookName")
    public String  BookName;
    @SerializedName("BookNameBangla")
    public String  BookNameBangla;
    @SerializedName("QTY_NUMBER")
    public int  QTY_NUMBER;
    @SerializedName("BOOK_NET_MRP")
    public String  BOOK_NET_MRP;
    @SerializedName("BOOK_FACE_VALUE")
    public String  BOOK_FACE_VALUE;

    @SerializedName("BOOK_SPECIMEN_CODE")
    public String  BOOK_SPECIMEN_CODE;
    @SerializedName("BARCODE_NUMBER")
    public String  BARCODE_NUMBER;
    @SerializedName("F_BOOK_EDITION_NO")
    public String  F_BOOK_EDITION_NO;
    @SerializedName("BOOK_GROUP_ID")
    public String BOOK_GROUP_ID;
    @SerializedName("BookNo")
    public String BookNo;
}
