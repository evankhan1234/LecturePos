package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetailsSyncModel {

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        @SerializedName("store_id")
        public String store_id;

        @SerializedName("retailer_code")
        public String retailer_code;

        @SerializedName("name")
        public String name;
        @SerializedName("address")
        public String address;
        @SerializedName("phone")
        public String phone;
        @SerializedName("library_name")
        public String library_name;
        @SerializedName("upd_no")
        public String upd_no;
        @SerializedName("upd_date")
        public String upd_date;
        @SerializedName("status")
        public String status;


    }
}
