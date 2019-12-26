package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

public class LoginPostEntity {
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("user_pass")
    public String user_pass;
    @SerializedName("device_id")
    public String device_id;
    @SerializedName("device_info")
    public String device_info;
    @SerializedName("login_time")
    public String login_time;
    @SerializedName("device_ime")
    public String device_ime;

}
