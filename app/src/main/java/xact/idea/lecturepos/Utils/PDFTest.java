package xact.idea.lecturepos.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.InvoicePrintAgainActivity;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;
import xact.idea.lecturepos.R;

import static com.itextpdf.text.html.HtmlTags.FONT;
import static java.awt.font.NumericShaper.BENGALI;
import static java.security.AccessController.getContext;
import static java.util.Locale.CHINESE;
import static xact.idea.lecturepos.Utils.Utils.getValue;
import static xact.idea.lecturepos.Utils.Utils.rounded;

public class PDFTest {
    private Document mDocument;
    boolean boolean_permission;
    private Context mContext;
    Font f;
    public static int REQUEST_PERMISSIONS = 1;
    SalesMaster salesMaster;
    Customer customer;
    List<SalesDetailPrintModel> printModels;
    public PDFTest(Context context, SalesMaster salesMasters,Customer customers,List<SalesDetailPrintModel> printModel) {
        mContext = context;
        salesMaster=salesMasters;
         customer=customers;
        fn_permission();
        printModels=printModel;
        Rectangle one = new Rectangle(270,370);
        mDocument = new Document(one, 36, 36, 6, 0);
        try {
            PdfWriter writer = PdfWriter.getInstance(mDocument,
                    new FileOutputStream(getFilePath()));

            Rectangle rect = new Rectangle(30, 30, 450, 800);
            writer.setBoxSize("art", rect);
            //   HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            //   writer.setPageEvent(event);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mDocument.open();
        BaseFont bf = null;


        try {
            mDocument.add(new Chunk(""));
//            final FontSet set = new FontSet();
//            set.addFont("fonts/NotoSansTamil-Regular.ttf");
//            mDocument.set(new FontProvider(set));

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {


            generatePDF();

        } catch (NullPointerException | DocumentException e) {
            Log.e("EX", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDocument.close();
    }

    public byte[]  getData(){
        fn_permission();
        File targetFile = new File(getFilePath());

        byte[] bytesArray = new byte[(int) targetFile.length()];
        return bytesArray;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Intent getIntentPDF() {
        fn_permission();
        File targetFile = new File(getFilePath());
        byte[] bytesArray = new byte[(int) targetFile.length()];
        Log.e("bytes","bytes"+bytesArray);

        Bitmap bitmap=getBitmap();
        Uri targetUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileProvider", targetFile);

        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(targetUri, "application/pdf");

        return intent;
    }

    public String getSharedPrefDateFrom() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("DateFrom", null);
        return userName;
    }

    public String getSharedPrefDateTo() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("DateTo", null);
        return userName;
    }

    private String Name() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("Name", null);
        return userName;
    }

    private void generatePDF() throws DocumentException, IOException {
        final String[] sources = {"english.xml", "arabic.xml", "hindi.xml", "tamil.xml"};
        Font font = null;
//        try {
//            font = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        }
        PdfPTable headerTable = getEmptyTable(new float[]{1});
        headerTable.getDefaultCell().setPadding(2);
        headerTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
        Date dates = new Date(System.currentTimeMillis());
        String currentTime = formatters.format(dates);
        String BILLq = "";

        String datesss=getValue(formatter.format(salesMaster.InvoiceDate));
        String inv=getValue(salesMaster.InvoiceNumber);
        String mobile=getValue(customer.MobileNumber);

        String strUserName=SharedPreferenceUtil.getUserNameBangla(mContext);
        String strUserAddress=SharedPreferenceUtil.getUserAddressBangla(mContext);
        String strUserNameEnglish;
        if (strUserName==null ||strUserName.equals("")){

            strUserName=SharedPreferenceUtil.getUserName(mContext);
            if (strUserName==null ||strUserName.equals("")){
                strUserName="N/A";
            }

        }
        if (strUserAddress==null ||strUserAddress.equals("")){

            strUserAddress=SharedPreferenceUtil.getUserAddress(mContext);
            if (strUserAddress==null ||strUserAddress.equals("")){
                strUserAddress="N/A";
            }

        }


        //   headerTable.addCell(getPara(BILL, 14));
        //  Paragraph p = new Paragraph("poriman", f);
        //  mDocument.add(new Paragraph(String.format("CP1250: %s", BENGALI), 14));
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getPara(" "));
    //    headerTable.addCell(getParaS("eva Centre (8th Floor), 72, Mohakhali C/A, Bir Uttam AK Khandakar Road ",12));
//        headerTable.addCell(getParaS("Dhaka 1212, Bangladesh ",12));
//
//        headerTable.addCell(getParaS("Phone: 9856358, Fax: 88-02-9855949", 12));
//        headerTable.addCell(getParaS("Email: saifop@bdmail.net", 12));
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getPara(" Division Name:- "+getSharedPrefDataDivisionName(), 12));
//
//
//
//        headerTable.addCell(getPara(" "));
//     //   headerTable.addCell(getPara(" Designation Name:- "+pdfData.employeeTimeKeeping.get(0).Designation, 12));
//
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getParaColor("From :-  "+getSharedPrefDateFrom()+" To :- "+getSharedPrefDateTo(), 12));
//
//        Paragraph reportHeading = getFormatReportHeading("Employee-wise Time Keeping Report");
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(mContext.getResources().getColor(R.color.black));
        textPaint.setTextSize(35);
        TextPaint textPaint2 = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint2.setStyle(Paint.Style.FILL);
        textPaint2.setColor(mContext.getResources().getColor(R.color.black));
        textPaint2.setTextSize(25);
        TextPaint textPaint1 = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint1.setStyle(Paint.Style.FILL);
        textPaint1.setColor(mContext.getResources().getColor(R.color.black));
        textPaint1.setTextSize(12);
        textPaint1.setTextAlign(Paint.Align.CENTER);
        StaticLayout mTextLayoutFor1 = new StaticLayout(strUserName, textPaint1, 600, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor1 = Bitmap.createBitmap(570, mTextLayoutFor1.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor1 = new Canvas(imageFor1);

        // Draw background
        Paint paintFor1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor1.setStyle(Paint.Style.FILL);
        paintFor1.setColor(mContext.getResources().getColor(R.color.white));
        cFor1.drawPaint(paintFor1);

        // Draw text
        cFor1.save();
        cFor1.translate(0, 0);
        mTextLayoutFor1.draw(cFor1);
        cFor1.restore();
        Bitmap image21For1=imageFor1;
        ByteArrayOutputStream streamFor1 = new ByteArrayOutputStream();
        Bitmap bitmapFor1 = image21For1;
        bitmapFor1.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor1);
        Image myImgFor1  = Image.getInstance(streamFor1.toByteArray());
        myImgFor1.setAlignment(Element.ALIGN_CENTER);
//////////////////////////////////////
        StaticLayout mTextLayoutFor2 = new StaticLayout(strUserAddress , textPaint1, 600, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor2 = Bitmap.createBitmap(570, mTextLayoutFor2.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor2 = new Canvas(imageFor2);

        // Draw background
        Paint paintFor2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor2.setStyle(Paint.Style.FILL);
        paintFor2.setColor(mContext.getResources().getColor(R.color.white));
        cFor2.drawPaint(paintFor2);

        // Draw text
        cFor2.save();
        cFor2.translate(0, 0);
        mTextLayoutFor2.draw(cFor2);
        cFor2.restore();
        Bitmap image21For2=imageFor2;
        ByteArrayOutputStream streamFor2 = new ByteArrayOutputStream();
        Bitmap bitmapFor2 = image21For2;
        bitmapFor2.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor2);
        Image myImgFor2 = Image.getInstance(streamFor2.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor3 = new StaticLayout("(লেকচার পাবলিকেশন লিমিটেড অনুমোদিত এজেন্ট)", textPaint1, 800, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor3 = Bitmap.createBitmap(570, mTextLayoutFor3.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor3 = new Canvas(imageFor3);

        // Draw background
        Paint paintFor3 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor3.setStyle(Paint.Style.FILL);
        paintFor3.setColor(mContext.getResources().getColor(R.color.white));
        cFor3.drawPaint(paintFor3);

        // Draw text
        cFor3.save();
        cFor3.translate(0, 0);
        mTextLayoutFor3.draw(cFor3);
        cFor3.restore();
        Bitmap image21For3=imageFor3;
        ByteArrayOutputStream streamFor3 = new ByteArrayOutputStream();
        Bitmap bitmapFor3 = image21For3;
        bitmapFor3.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor3);
        Image myImgFor3  = Image.getInstance(streamFor3.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor4 = new StaticLayout("ইনভয়েস নং: " + inv , textPaint1, 700, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor4 = Bitmap.createBitmap(570, mTextLayoutFor4.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor4 = new Canvas(imageFor4);

        // Draw background
        Paint paintFor4 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor4.setStyle(Paint.Style.FILL);
        paintFor4.setColor(mContext.getResources().getColor(R.color.white));
        cFor4.drawPaint(paintFor4);

        // Draw text
        cFor4.save();
        cFor4.translate(0, 0);
        mTextLayoutFor4.draw(cFor4);
        cFor4.restore();
        Bitmap image21For4=imageFor4;
        ByteArrayOutputStream streamFor4 = new ByteArrayOutputStream();
        Bitmap bitmapFor4 = image21For4;
        bitmapFor4.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor4);
        Image myImgFor4  = Image.getInstance(streamFor4.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor5 = new StaticLayout("তারিখ : " + datesss , textPaint1, 600, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor5 = Bitmap.createBitmap(570, mTextLayoutFor5.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor5 = new Canvas(imageFor5);

        // Draw background
        Paint paintFor5 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor5.setStyle(Paint.Style.FILL);
        paintFor5.setColor(mContext.getResources().getColor(R.color.white));
        cFor5.drawPaint(paintFor5);

        // Draw text
        cFor5.save();
        cFor5.translate(0, 0);
        mTextLayoutFor5.draw(cFor5);
        cFor5.restore();
        Bitmap image21For5=imageFor5;
        ByteArrayOutputStream streamFor5 = new ByteArrayOutputStream();
        Bitmap bitmapFor5 = image21For5;
        bitmapFor5.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor5);
        Image myImgFor5  = Image.getInstance(streamFor5.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor6 = new StaticLayout("গ্রন্থাগার নাম : " + customer.ShopName, textPaint1, 700, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor6 = Bitmap.createBitmap(570, mTextLayoutFor6.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor6 = new Canvas(imageFor6);

        // Draw background
        Paint paintFor6 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor6.setStyle(Paint.Style.FILL);
        paintFor6.setColor(mContext.getResources().getColor(R.color.white));
        cFor6.drawPaint(paintFor6);

        // Draw text
        cFor6.save();
        cFor6.translate(0, 0);
        mTextLayoutFor6.draw(cFor6);
        cFor6.restore();
        Bitmap image21For6=imageFor6;
        ByteArrayOutputStream streamFor6 = new ByteArrayOutputStream();
        Bitmap bitmapFor6 = image21For6;
        bitmapFor6.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor6);
        Image myImgFor6  = Image.getInstance(streamFor6.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor7 = new StaticLayout("গ্রাহক নাম: " + customer.Name+"("+customer.RetailerCode +")", textPaint1, 700, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor7 = Bitmap.createBitmap(570, mTextLayoutFor7.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor7 = new Canvas(imageFor7);

        // Draw background
        Paint paintFor7 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor7.setStyle(Paint.Style.FILL);
        paintFor7.setColor(mContext.getResources().getColor(R.color.white));
        cFor7.drawPaint(paintFor7);

        // Draw text
        cFor7.save();
        cFor7.translate(0, 0);
        mTextLayoutFor7.draw(cFor7);
        cFor7.restore();
        Bitmap image21For7=imageFor7;
        ByteArrayOutputStream streamFor7= new ByteArrayOutputStream();
        Bitmap bitmapFor7 = image21For7;
        bitmapFor7.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor7);
        Image myImgFor7  = Image.getInstance(streamFor7.toByteArray());
//////////////////////////////////////
        StaticLayout mTextLayoutFor8 = new StaticLayout("মোবাইল নং:" + mobile , textPaint1, 700, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor8 = Bitmap.createBitmap(570, mTextLayoutFor8.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor8= new Canvas(imageFor8);

        // Draw background
        Paint paintFor8 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor8.setStyle(Paint.Style.FILL);
        paintFor8.setColor(mContext.getResources().getColor(R.color.white));
        cFor8.drawPaint(paintFor8);

        // Draw text
        cFor8.save();
        cFor8.translate(0, 0);
        mTextLayoutFor8.draw(cFor8);
        cFor8.restore();
        Bitmap image21For8=imageFor8;
        ByteArrayOutputStream streamFor8 = new ByteArrayOutputStream();
        Bitmap bitmapFor8= image21For8;
        bitmapFor8.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor8);
        Image myImgFor8  = Image.getInstance(streamFor8.toByteArray());
//////////////////////////////////////
          headerTable.addCell(myImgFor1);
          headerTable.addCell(myImgFor2);
          headerTable.addCell(myImgFor3);
        headerTable.addCell(myImgFor4);
        headerTable.addCell(myImgFor5);
        headerTable.addCell(myImgFor6);
        headerTable.addCell(myImgFor7);
        headerTable.addCell(myImgFor8);








        /////////////////////////////////////////////////

        StaticLayout mTextLayout = new StaticLayout("বইয়ের নাম", textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap image = Bitmap.createBitmap(570, mTextLayout.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(image);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mContext.getResources().getColor(R.color.white));
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();
        Bitmap image21=image;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = image21;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());

        /////

        StaticLayout mTextLayout2 = new StaticLayout("সংখ্যা", textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap image2 = Bitmap.createBitmap(570, mTextLayout2.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c2 = new Canvas(image2);

        // Draw background
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(mContext.getResources().getColor(R.color.white));
        c2.drawPaint(paint2);

        // Draw text
        c2.save();
        c2.translate(0, 0);
        mTextLayout2.draw(c2);
        c2.restore();
        Bitmap image22=image2;
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        Bitmap bitmap22 = image22;
        bitmap22.compress(Bitmap.CompressFormat.JPEG, 100 , stream2);
        Image myImg2 = Image.getInstance(stream2.toByteArray());
        ///

        StaticLayout mTextLayout3 = new StaticLayout("মূল্য", textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap image3 = Bitmap.createBitmap(570, mTextLayout3.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c3 = new Canvas(image3);

        // Draw background
        Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint3.setStyle(Paint.Style.FILL);
        paint3.setColor(mContext.getResources().getColor(R.color.white));
        c3.drawPaint(paint3);

        // Draw text
        c3.save();
        c3.translate(0, 0);
        mTextLayout3.draw(c3);
        c3.restore();
        Bitmap image23=image3;
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        Bitmap bitmap3 = image23;
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100 , stream3);
        Image myImg3 = Image.getInstance(stream3.toByteArray());
        ///
        StaticLayout mTextLayout4 = new StaticLayout("মোট টাকা", textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap image4 = Bitmap.createBitmap(570, mTextLayout4.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c4 = new Canvas(image4);

        // Draw background
        Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint4.setStyle(Paint.Style.FILL);
        paint4.setColor(mContext.getResources().getColor(R.color.white));
        c4.drawPaint(paint4);

        // Draw text
        c4.save();
        c4.translate(0, 0);
        mTextLayout4.draw(c4);
        c4.restore();
        Bitmap image24=image4;
        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
        Bitmap bitmap4 = image24;
        bitmap4.compress(Bitmap.CompressFormat.JPEG, 100 , stream4);
        Image myImg4 = Image.getInstance(stream4.toByteArray());
//
//
//
        PdfPTable table = new PdfPTable(4);

        table.setHeaderRows(1);
        PdfPCell cell1 = new PdfPCell(getParaS("বইয়ের In Date", 12));
        cell1.setPaddingBottom(5f);


        PdfPCell cell2 = new PdfPCell(getParaS("Log In Time", 12));
        cell2.setPaddingBottom(5f);
        PdfPCell cell3 = new PdfPCell(getParaS("Log Out Time", 12));
        cell3.setPaddingBottom(5f);
        PdfPCell cell4 = new PdfPCell(getParaS("Actual-Hrs", 12));
        cell4.setPaddingBottom(5f);
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);



        PdfPTable logoTable = getEmptyTable(new float[]{1,1,1,1});
        logoTable.addCell(myImg);
        logoTable.addCell(myImg2);
        logoTable.addCell(myImg3);
        logoTable.addCell(myImg4);
        String[] colors = { "দা.৯.পৌরনীতি-(নোট)", "দা.৯.তথ্য ও যোগাযোগ প্রযুক্তি-(নোট)", "দা.৯.শারীরিক শিক্ষা-(নোট)" };
        String[] colors1 = { colors[0], "50", "150" ,"544"};
        ArrayList<String> linearPDFData = new ArrayList<String>();
        ArrayList<String> linearPDFDataFor = new ArrayList<String>();
        ArrayList<String> linearPDFDataForOne = new ArrayList<String>();
        for (SalesDetailPrintModel salesDetailPrintModel : printModels)
        {

            Log.e("data","datass"+salesDetailPrintModel.BookNameBangla);
            String value;

            String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
            String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
            double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
            double ww = Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
            String totalPrice = getValue(String.valueOf(rounded(price,2)));
            String wws = getValue(String.valueOf(rounded(ww,2)));

            linearPDFData.add(salesDetailPrintModel.BookNameBangla);
            linearPDFData.add(rate);
            linearPDFData.add(wws);
            linearPDFData.add(totalPrice);



            // BILL = BILL+ String.format("%-20s%-18s%-15s%-10s",getWidth(value,1), quantity, wws, totalPrice) + "\n";
        }
        String subTotal = getValue(salesMaster.SubTotal);
        String Total = getValue(String.valueOf(salesMaster.NetValue));
        String Return = getValue(String.valueOf(salesMaster.Return));
        String Discount = getValue(String.valueOf(salesMaster.Discount));
        linearPDFDataFor.add("মোট টাকা");

        linearPDFDataFor.add(subTotal);
        linearPDFDataFor.add("ছাড়");
        linearPDFDataFor.add(Discount);
        linearPDFDataFor.add("ফেরৎ");
        linearPDFDataFor.add(Return);
        linearPDFDataFor.add("নেট টাকা");
        linearPDFDataFor.add(Total);
        linearPDFDataFor.add("পরিশোধিত");
        linearPDFDataFor.add(salesMaster.PayMode);



            for (String data : linearPDFData)
            {


                StaticLayout mText = new StaticLayout(data, textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // Create bitmap and canvas to draw to
                Bitmap ip = Bitmap.createBitmap(570, mText.getHeight(), Bitmap.Config.ARGB_4444);
                Canvas c1 = new Canvas(ip);

                // Draw background
                Paint pp = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
                pp.setStyle(Paint.Style.FILL);
                pp.setColor(mContext.getResources().getColor(R.color.white));
                c1.drawPaint(pp);

                // Draw text
                c1.save();
                c1.translate(0, 0);
                mText.draw(c1);
                c1.restore();
                Bitmap ie=ip;
                ByteArrayOutputStream st = new ByteArrayOutputStream();
                Bitmap b3 = ie;
                b3.compress(Bitmap.CompressFormat.JPEG, 100 , st);
                Image qwe = Image.getInstance(st.toByteArray());
                logoTable.addCell(qwe);
            }
        PdfPTable logoTableFor = getEmptyTable(new float[]{1,1});
        for (String data : linearPDFDataFor)
        {


            StaticLayout mText = new StaticLayout(data, textPaint2, 570, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, false);

            // Create bitmap and canvas to draw to
            Bitmap ip = Bitmap.createBitmap(570, mText.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas c1 = new Canvas(ip);

            // Draw background
            Paint pp = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
            pp.setStyle(Paint.Style.FILL);
            pp.setColor(mContext.getResources().getColor(R.color.white));
            c1.drawPaint(pp);

            // Draw text
            c1.save();
            c1.translate(0, 0);
            mText.draw(c1);
            c1.restore();
            Bitmap ie=ip;
            ByteArrayOutputStream st = new ByteArrayOutputStream();
            Bitmap b3 = ie;
            b3.compress(Bitmap.CompressFormat.JPEG, 100 , st);
            Image qwe = Image.getInstance(st.toByteArray());
            logoTableFor.addCell(qwe);
        }
        StaticLayout mText12= new StaticLayout("আপনার সহযোগিতার জন্য ধন্যবাদ", textPaint2, 600, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap imageFor12 = Bitmap.createBitmap(570, mText12.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cFor12= new Canvas(imageFor12);

        // Draw background
        Paint paintFor12 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paintFor12.setStyle(Paint.Style.FILL);
        paintFor12.setColor(mContext.getResources().getColor(R.color.white));
        cFor12.drawPaint(paintFor12);

        // Draw text
        cFor12.save();
        cFor12.translate(0, 0);
        mText12.draw(cFor12);
        cFor12.restore();
        Bitmap image21For12=imageFor12;
        ByteArrayOutputStream streamFor12 = new ByteArrayOutputStream();
        Bitmap bitmapFor12= image21For12;
        bitmapFor12.compress(Bitmap.CompressFormat.JPEG, 100 , streamFor12);
        Image myImgFor12  = Image.getInstance(streamFor12.toByteArray());

        linearPDFDataForOne.add("Developed By");
        linearPDFDataForOne.add("Printed At");
        linearPDFDataForOne.add("www.xactidea.com");
        linearPDFDataForOne.add(currentDate+" "+currentTime);
        PdfPTable logoTableForOne = getEmptyTableFor(new float[]{1,1});
        for (String data : linearPDFDataForOne)
        {


            StaticLayout mText = new StaticLayout(data, textPaint, 570, Layout.Alignment.ALIGN_CENTER ,1.0f, 0.0f, false);

            // Create bitmap and canvas to draw to
            Bitmap ip = Bitmap.createBitmap(570, mText.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas c1 = new Canvas(ip);

            // Draw background
            Paint pp = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
            pp.setStyle(Paint.Style.FILL);
            pp.setColor(mContext.getResources().getColor(R.color.white));
            c1.drawPaint(pp);

            // Draw text
            c1.save();
            c1.translate(0, 0);
            mText.draw(c1);
            c1.restore();
            Bitmap ie=ip;
            ByteArrayOutputStream st = new ByteArrayOutputStream();
            Bitmap b3 = ie;
            b3.compress(Bitmap.CompressFormat.JPEG, 100 , st);
            Image qwe = Image.getInstance(st.toByteArray());
            logoTableForOne.addCell(qwe);
        }
//        logoTable.getDefaultCell().setPadding(0);
//
//        logoTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//        logoTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
        //   logoTable.addCell(myImg);
//        logoTable.addCell(getPara(" "));
//        logoTable.addCell(getPara(" "));
//        logoTable.addCell(getPara(" "));
//
//        logoTable.addCell(getPara(" "));

//
//        mDocument.add(getPara(" "));

        //   mDocument.add(p);

      //  mDocument.add(myImgFor1);
        PdfPTable headerTable1 = getEmptyTable(new float[]{1});
        headerTable1.getDefaultCell().setPadding(2);
        headerTable1.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        headerTable1.getDefaultCell().setBorderColor(BaseColor.WHITE);
        headerTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        headerTable1.addCell(myImgFor12);
        mDocument.add(headerTable);
        mDocument.add(Chunk.NEWLINE);
        mDocument.add(logoTable);
        mDocument.add(Chunk.NEWLINE);
        mDocument.add(getParaS("   ",12));
        mDocument.add(logoTableFor);
        mDocument.add(getParaS("   ",12));
        mDocument.add(headerTable1);
        mDocument.add(logoTableForOne);
        //  mDocument.add(Chunk.NEWLINE);
        //  mDocument.add(reportHeading);
        //  mDocument.add(Chunk.NEWLINE);

        //   mDocument.add(table);

        //  mDocument.add(getPara(" "));

        // mDocument.add(getReport(" Report Summary", 14));
        //  mDocument.add(getPara(" "));
        //  mDocument.add(getPara(" "));
        //   mDocument.add(table1);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getBitmap(){

        int pageNum=0;


        PdfiumCore pdfiumCore = new PdfiumCore(mContext);
        try {
            byte[] bFile = Files.readAllBytes(Paths.get(getFilePath()));
            PdfDocument pdfDocument = pdfiumCore.newDocument(bFile);
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getBitmapTwo(){

        int pageNum=1;


        PdfiumCore pdfiumCore = new PdfiumCore(mContext);
        try {
            byte[] bFile = Files.readAllBytes(Paths.get(getFilePath()));
            PdfDocument pdfDocument = pdfiumCore.newDocument(bFile);
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return null;
    }
    private String getSharedPrefDataDivisionName() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String DivisionName = sharedPref.getString("DivisionName", null);
        return DivisionName;
    }
    private Paragraph getFormatReportHeading(String content) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private Paragraph getPara(String content) {
        Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
        Paragraph p = new Paragraph(content, font);

        return p;
    }


    private Paragraph getReport(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private Paragraph getPara(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaColor(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaColorName(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.BLUE);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaS(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaReportFormat(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private PdfPTable getEmptyTable(float[] col) {
        PdfPTable table = new PdfPTable(col);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(3);
        table.setWidthPercentage(130);

        return table;
    }
    private PdfPTable getEmptyTableFor(float[] col) {
        PdfPTable table = new PdfPTable(col);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setPadding(3);
        table.setWidthPercentage(130);


        return table;
    }
    private Paragraph getRedColorText(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        return p;
    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }

    public String getFilePath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Lecture";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "Pos.pdf";
    }
}
