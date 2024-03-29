package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class LoginEntity {
    @SerializedName("data")
    public Data  data;

    @SerializedName("status_code")
    public int  status_code;
    @SerializedName("user_id")
    public String  user_id;
    @SerializedName("message")
    public String  message;
    public class Data {
        @SerializedName("USER_ID")
        public String  USER_ID;
        @SerializedName("PASSWORD")
        public String  PASSWORD;
        @SerializedName("ACTIVE")
        public String  ACTIVE;
        @SerializedName("DEVICE")
        public String  DEVICE;
        @SerializedName("CUSTOMER_NAME")
        public String  CUSTOMER_NAME;
        @SerializedName("CUSTOMER_ADDRESS")
        public String  CUSTOMER_ADDRESS;
        @SerializedName("CUSTOMER_NAME_B")
        public String CUSTOMER_NAME_B;
        @SerializedName("CUSTOMER_ADDRESS_B")
        public String CUSTOMER_ADDRESS_B;



    }
}
