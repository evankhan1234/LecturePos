package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

public class LoginPostEntity {
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("user_pass")
    public String user_pass;

}
