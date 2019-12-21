package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponseEntity {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int  status_code;
    @SerializedName("total_record")
    public int  total_record;
    @SerializedName("message")
    public String  message;
    public class Data {
        @SerializedName("BOOK_CODE")
        public String BOOK_CODE;
        @SerializedName("BOOK_SELLING_CODE")
        public String BOOK_SELLING_CODE;
        @SerializedName("BARCODE_NUMBER")
        public String BARCODE_NUMBER;
        @SerializedName("BOOK_NET_PRICE")
        public String BOOK_NET_PRICE;
        @SerializedName("BOOK_FACE_VALUE")
        public String BOOK_FACE_VALUE;
        @SerializedName("BOOK_NAME")
        public String BOOK_NAME;
        @SerializedName("BOOK_NO")
        public String BOOK_NO;
        @SerializedName("BOOK_SPECIMEN_CODE")
        public String BOOK_SPECIMEN_CODE;




    }
}
