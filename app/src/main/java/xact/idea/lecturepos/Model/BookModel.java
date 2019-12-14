package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

public class BookModel {

    public String BookCode;

    public String BookName;

    public String BookNo;

    public double BookPrice;

    public BookModel(String bookCode, String bookName, String bookNo, double bookPrice) {
        BookCode = bookCode;
        BookName = bookName;
        BookNo = bookNo;
        BookPrice = bookPrice;
    }
}