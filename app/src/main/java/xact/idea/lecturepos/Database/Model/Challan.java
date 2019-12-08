package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "challan")
public class Challan {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "CHALLAN_NO")
    public String  CHALLAN_NO;
    @ColumnInfo(name = "CHALLAN_CODE")
    public String  CHALLAN_CODE;
    @ColumnInfo(name = "NO_OF_PACKATE")
    public String  NO_OF_PACKATE;
    @ColumnInfo(name = "TOTAL_BOOK_COST")
    public String  TOTAL_BOOK_COST;
    @ColumnInfo(name = "PER_HANDLING_COST")
    public String  PER_HANDLING_COST;
    @ColumnInfo(name = "TOTAL_HANDLING_COST")
    public String  TOTAL_HANDLING_COST;
    @ColumnInfo(name = "PER_PACKAGING_COST")
    public String  PER_PACKAGING_COST;
    @ColumnInfo(name = "TOTAL_PACKAGING_COST")
    public String  TOTAL_PACKAGING_COST;
    @ColumnInfo(name = "IS_PACKAGING_DUE")
    public String  IS_PACKAGING_DUE;
    @ColumnInfo(name = "IS_HANDLING_DUE")
    public String  IS_HANDLING_DUE;
    @ColumnInfo(name = "TOTAL_VALUE")
    public String  TOTAL_VALUE;
    @ColumnInfo(name = "CHALLAN_DATE")
    public String  CHALLAN_DATE;
    @ColumnInfo(name = "CHALLAN_SL_NO")
    public String  CHALLAN_SL_NO;
    @ColumnInfo(name = "COMMENTS")
    public String  COMMENTS;
    @ColumnInfo(name = "F_BOOK_ORDER_NO")
    public String  F_BOOK_ORDER_NO;
    @ColumnInfo(name = "F_ORDER_INVOICE_NO")
    public String  F_ORDER_INVOICE_NO;
    @ColumnInfo(name = "F_TRANSPORT_BR_NO")
    public String  F_TRANSPORT_BR_NO;
    @ColumnInfo(name = "F_COMPANY_NO")
    public String  F_COMPANY_NO;
    @ColumnInfo(name = "SL_NUM")
    public String  SL_NUM;
    @ColumnInfo(name = "LAST_ACTION")
    public String  LAST_ACTION;
    @ColumnInfo(name = "LAST_ACTION_TIME")
    public String  LAST_ACTION_TIME;
    @ColumnInfo(name = "LAST_ACTION_LOGON_NO")
    public String  LAST_ACTION_LOGON_NO;
    @ColumnInfo(name = "INSERT_TIME")
    public String  INSERT_TIME;
    @ColumnInfo(name = "INSERT_USER_NO")
    public String  INSERT_USER_NO;
    @ColumnInfo(name = "INSERT_LOGON_NO")
    public String  INSERT_LOGON_NO;
    @ColumnInfo(name = "F_FISCAL_YEAR_NO")
    public String  F_FISCAL_YEAR_NO;
    @ColumnInfo(name = "F_CUSTOMER_NO")
    public String  F_CUSTOMER_NO;
    @ColumnInfo(name = "F_GODOWN_NO")
    public String  F_GODOWN_NO;
    @ColumnInfo(name = "F_EMPLOYEE_NO_CHALLAN_BY")
    public String  F_EMPLOYEE_NO_CHALLAN_BY;
    @ColumnInfo(name = "IS_BOOKED")
    public String  IS_BOOKED;
    @ColumnInfo(name = "CHALLAN_QTY")
    public String  CHALLAN_QTY;
    @ColumnInfo(name = "COMPLETED_BOOKING_PKT_QTY")
    public String  COMPLETED_BOOKING_PKT_QTY;
    @ColumnInfo(name = "IS_RECEIVE")
    public String  IS_RECEIVE;




}
