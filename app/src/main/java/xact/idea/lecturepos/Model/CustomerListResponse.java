package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerListResponse {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int status_code;
    @SerializedName("total_record")
    public int total_record;
    @SerializedName("message")
    public String message;

    public class Data {
        @SerializedName("STORE_ID")
        public String STORE_ID;
        @SerializedName("RETAILER_CODE")
        public String RETAILER_CODE;
        @SerializedName("NAME")
        public String NAME;
        @SerializedName("ADDRESS")
        public String ADDRESS;
        @SerializedName("PHONE")
        public String PHONE;
        @SerializedName("LIBRARY_NAME")
        public String LIBRARY_NAME;
        @SerializedName("UPD_NO")
        public String UPD_NO;
        @SerializedName("UPD_DATE")
        public String UPD_DATE;
        @SerializedName("STATUS")
        public String STATUS;
    }


}
