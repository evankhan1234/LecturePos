package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SyncChallanModel {

    @SerializedName("data")
    public List<Sync> data;

    public static class Sync {
        @SerializedName("challan_no")
        public String challan_no;
//        @SerializedName("CHALLAN_CODE")
//        public String CHALLAN_CODE;
//        @SerializedName("NO_OF_PACKATE")
//        public String NO_OF_PACKATE;
//        @SerializedName("TOTAL_BOOK_COST")
//        public String TOTAL_BOOK_COST;
//        @SerializedName("PER_HANDLING_COST")
//        public String PER_HANDLING_COST;
//        @SerializedName("TOTAL_HANDLING_COST")
//        public String TOTAL_HANDLING_COST;
//        @SerializedName("PER_PACKAGING_COST")
//        public String PER_PACKAGING_COST;
//        @SerializedName("TOTAL_PACKAGING_COST")
//        public String TOTAL_PACKAGING_COST;
//        @SerializedName("IS_PACKAGING_DUE")
//        public String IS_PACKAGING_DUE;
//        @SerializedName("IS_HANDLING_DUE")
//        public String IS_HANDLING_DUE;
//        @SerializedName("TOTAL_VALUE")
//        public String TOTAL_VALUE;
//        @SerializedName("CHALLAN_DATE")
//        public String CHALLAN_DATE;
//        @SerializedName("CHALLAN_SL_NO")
//        public String CHALLAN_SL_NO;
//        @SerializedName("COMMENTS")
//        public String COMMENTS;
//        @SerializedName("F_BOOK_ORDER_NO")
//        public String F_BOOK_ORDER_NO;
//        @SerializedName("F_ORDER_INVOICE_NO")
//        public String F_ORDER_INVOICE_NO;
//        @SerializedName("F_TRANSPORT_BR_NO")
//        public String F_TRANSPORT_BR_NO;
//        @SerializedName("F_COMPANY_NO")
//        public String F_COMPANY_NO;
//        @SerializedName("SL_NUM")
//        public String SL_NUM;
//        @SerializedName("LAST_ACTION")
//        public String LAST_ACTION;
//        @SerializedName("LAST_ACTION_TIME")
//        public String LAST_ACTION_TIME;
//        @SerializedName("LAST_ACTION_LOGON_NO")
//        public String LAST_ACTION_LOGON_NO;
//        @SerializedName("INSERT_TIME")
//        public String INSERT_TIME;
//        @SerializedName("INSERT_USER_NO")
//        public String INSERT_USER_NO;
//        @SerializedName("INSERT_LOGON_NO")
//        public String INSERT_LOGON_NO;
//        @SerializedName("F_FISCAL_YEAR_NO")
//        public String F_FISCAL_YEAR_NO;
//        @SerializedName("F_CUSTOMER_NO")
//        public String F_CUSTOMER_NO;
//        @SerializedName("F_GODOWN_NO")
//        public String F_GODOWN_NO;
//        @SerializedName("F_EMPLOYEE_NO_CHALLAN_BY")
//        public String F_EMPLOYEE_NO_CHALLAN_BY;
//        @SerializedName("IS_BOOKED")
//        public String IS_BOOKED;
//        @SerializedName("CHALLAN_QTY")
//        public String CHALLAN_QTY;
//        @SerializedName("COMPLETED_BOOKING_PKT_QTY")
//        public String COMPLETED_BOOKING_PKT_QTY;
        @SerializedName("is_receive")
        public String is_receive;
      //  @SerializedName("IS_BUSY")
//        public String IS_BUSY;
//        @SerializedName("BUSY_USER_NO")
//        public String BUSY_USER_NO;
//        @SerializedName("BUSY_TIME")
//        public String BUSY_TIME;
//        @SerializedName("TOTAL_COMMISION")
//        public String TOTAL_COMMISION;
//        @SerializedName("OVERALL_DISCOUNT")
//        public String OVERALL_DISCOUNT;
        @SerializedName("receive_date")
        public String receive_date;
//        @SerializedName("Date")
//        public Date Date;
    }
}
