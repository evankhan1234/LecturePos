package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

public class SalesPostEntity {
    @SerializedName("customer_no")
    public String customer_no;
    @SerializedName("transaction_type")
    public String transaction_type;
}
