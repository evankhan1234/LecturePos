package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

public class SalesDetailPrintModel {
    @SerializedName("BookPrice")
    public String  BookPrice;
    @SerializedName("BookName")
    public String  BookName;
    @SerializedName("Quantity")
    public int  Quantity;
    @SerializedName("BOOK_NET_MRP")
    public String  BOOK_NET_MRP;

    @SerializedName("BOOK_SPECIMEN_CODE")
    public String  BOOK_SPECIMEN_CODE;
    @SerializedName("BARCODE_NUMBER")
    public String  BARCODE_NUMBER;
    @SerializedName("BookNameBangla")
    public String  BookNameBangla;


}
