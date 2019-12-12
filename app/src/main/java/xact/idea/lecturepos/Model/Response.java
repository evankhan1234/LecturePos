package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("status_code")
    public int  status_code;

    @SerializedName("message")
    public String  message;
}
