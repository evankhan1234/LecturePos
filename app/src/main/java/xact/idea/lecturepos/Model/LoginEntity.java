package xact.idea.lecturepos.Model;

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




    }
}
