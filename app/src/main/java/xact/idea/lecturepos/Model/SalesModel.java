package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SalesModel {

    @SerializedName("data")
    public List<SalesMaster> data;

    public static  class SalesMaster {
        @SerializedName("InvoiceId")
        public String InvoiceId;
        @SerializedName("InvoiceNumber")
        public String InvoiceNumber;
        @SerializedName("StoreId")
        public String StoreId;
        @SerializedName("Discount")
        public double Discount;
        @SerializedName("InvoiceAmount")
        public double InvoiceAmount;
        @SerializedName("RetailCode")
        public String RetailCode;
        @SerializedName("NetValue")
        public double NetValue;
        @SerializedName("PayMode")
        public String PayMode;
        @SerializedName("Device")
        public String Device;
        @SerializedName("CustomerName")
        public String CustomerName;
        @SerializedName("Note")
        public String Note;
        @SerializedName("InvoiceDate")
        public String InvoiceDate;

        public List<SalesDetails> salesDetails;

        public static  class SalesDetails {
            @SerializedName("StoreId")
            public String StoreId;
            @SerializedName("InvoiceId")
            public int InvoiceId;
            @SerializedName("BookId")
            public String BookId;
            @SerializedName("BookName")
            public String BookName;
            @SerializedName("Quantity")
            public int Quantity;
            @SerializedName("MRP")
            public double MRP;
            @SerializedName("Discount")
            public double Discount;
            @SerializedName("TotalAmount")
            public double TotalAmount;
        }
    }


}
