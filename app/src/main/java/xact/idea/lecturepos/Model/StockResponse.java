package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockResponse {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int status_code;
    @SerializedName("total_record")
    public int total_record;
    @SerializedName("message")
    public String message;

    public class Data {
//        @SerializedName("STORE_ID")
//        public String STORE_ID;

        @SerializedName("BOOK_NO")
        public String BOOK_NO;
        @SerializedName("BAL_QTY")
        public String BAL_QTY;
//        @SerializedName("LAST_UPDATE_DATE")
//        public String LAST_UPDATE_DATE;
    }
}
