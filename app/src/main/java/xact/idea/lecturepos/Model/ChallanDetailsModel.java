package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChallanDetailsModel {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int  status_code;
    @SerializedName("total_record")
    public int  total_record;
    @SerializedName("message")
    public String  message;

    public class Data{
        @SerializedName("F_BOOK_NO")
        public String  F_BOOK_NO;
        @SerializedName("F_CHALLAN_NO")
        public String  F_CHALLAN_NO;
        @SerializedName("BOOK_NET_PRICE")
        public String  BOOK_NET_PRICE;
        @SerializedName("CHALLAN_BOOK_QTY")
        public String  CHALLAN_BOOK_QTY;
    }
}
