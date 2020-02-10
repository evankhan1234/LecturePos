package xact.idea.lecturepos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.PrintSalesAdapter;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.PDFTest;
import xact.idea.lecturepos.Utils.PrintPic;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.UnicodeFormatter;

import static java.awt.font.NumericShaper.BENGALI;
import static xact.idea.lecturepos.Utils.Utils.getValue;
import static xact.idea.lecturepos.Utils.Utils.rounded;

public class InvoicePrintAgainActivity  extends AppCompatActivity implements Runnable {
    public static int REQUEST_PERMISSIONS = 1;
    private Document mDocument;
    String invoiceId;
    String customerName;
    String sub;
    String ww;
    String valueFor;
    Button create_bill;
    TextView tv_temp_one_company_name;
    TextView tv_temp_one_address;
    TextView text_bill_shop_name;
    TextView text_biil_customer_name;
    TextView text_bill_address;
    TextView text_bill_phone_number;
    TextView text_bill_retail_code;
    TextView text_ship_shop_name;
    TextView text_ship_shop_address;
    TextView text_ship_shop_number;
    TextView text_ship_retail_code;
    TextView text_invoice_number;
    TextView text_invoice_date;
    TextView text_store;
    TextView frag_to_TvInvoiceTitle;
    TextView text_payment;
    TextView text_total_value;
    TextView text_return;
    TextView text_discount;
    TextView create_challan;
    TextView text_sub_total_value;
    RecyclerView rcl_approval_in_list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PrintSalesAdapter mAdapters;
    List<SalesDetailPrintModel> printModels;
    private Printing printing = null;
    PrintingCallback printingCallback = null;
    Customer customer;
    SalesMaster salesMaster;
    ImageButton btn_header_back;
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    boolean test=false;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print_again);
//        if (Printooth.INSTANCE.hasPairedPrinter())
//            printing = Printooth.INSTANCE.printer();
//        //initViews();
//        initListeners();
//        fn_permission();
//        Rectangle one = new Rectangle(270,270);
//        mDocument = new Document(one, 36, 36, 36, 36);
//        try {
//            PdfWriter writer = PdfWriter.getInstance(mDocument,
//                    new FileOutputStream(getFilePath()));
//
//            Rectangle rect = new Rectangle(30, 30, 450, 800);
//            writer.setBoxSize("art", rect);
//            //   HeaderFooterPageEvent event = new HeaderFooterPageEvent();
//            //   writer.setPageEvent(event);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        mDocument.open();
//
//        try {
//            mDocument.add(new Chunk(""));
////            final FontSet set = new FontSet();
////            set.addFont("fonts/NotoSansTamil-Regular.ttf");
////            mDocument.set(new FontProvider(set));
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }


        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        tv_temp_one_company_name = findViewById(R.id.tv_temp_one_company_name);
        text_payment = findViewById(R.id.text_payment);
        frag_to_TvInvoiceTitle = findViewById(R.id.frag_to_TvInvoiceTitle);
        tv_temp_one_address = findViewById(R.id.tv_temp_one_address);
        create_bill = findViewById(R.id.create_bill);
        create_challan = findViewById(R.id.create_challan);
        text_invoice_number = findViewById(R.id.text_invoice_number);
        text_invoice_date = findViewById(R.id.text_invoice_date);
        text_bill_shop_name = findViewById(R.id.text_bill_shop_name);
        text_biil_customer_name = findViewById(R.id.text_biil_customer_name);
        text_bill_address = findViewById(R.id.text_bill_address);
        text_bill_phone_number = findViewById(R.id.text_bill_phone_number);
        text_bill_retail_code = findViewById(R.id.text_bill_retail_code);
        text_ship_shop_name = findViewById(R.id.text_ship_shop_name);
        text_ship_shop_address = findViewById(R.id.text_ship_shop_address);
        text_ship_shop_number = findViewById(R.id.text_ship_shop_number);
        text_ship_retail_code = findViewById(R.id.text_ship_retail_code);
        text_store = findViewById(R.id.text_store);
        text_total_value = findViewById(R.id.text_total_value);
        text_return = findViewById(R.id.text_return);
        text_discount = findViewById(R.id.text_discount);
        text_sub_total_value = findViewById(R.id.text_sub_total_value);
        invoiceId = getIntent().getStringExtra("invoiceId");
        sub = getIntent().getStringExtra("sub");
        ww = getIntent().getStringExtra("ww");
        valueFor = getIntent().getStringExtra("value");

        tv_temp_one_company_name.setText(SharedPreferenceUtil.getUserName(InvoicePrintAgainActivity.this));
        tv_temp_one_address.setText(SharedPreferenceUtil.getUserAddress(InvoicePrintAgainActivity.this));
        if (invoiceId != null) {

            customerName = getIntent().getStringExtra("customerName");

            salesMaster = Common.salesMasterRepository.getSalesMaster(invoiceId);
            if (salesMaster != null) {
                customer = Common.customerRepository.getCustomerss(customerName);
                if (customer != null) {

                    text_bill_shop_name.setText(customer.ShopName);
                    text_biil_customer_name.setText(customer.Name);
                    text_bill_address.setText(customer.Address);
                    text_bill_phone_number.setText(customer.MobileNumber);
                    text_bill_retail_code.setText(customer.RetailerCode);
//                    text_ship_shop_name.setText(customer.ShopName);
//                    text_ship_shop_address.setText(customer.Address);
//                    text_ship_shop_number.setText(customer.MobileNumber);
//                    text_ship_retail_code.setText(customer.RetailerCode);
                    text_invoice_number.setText(salesMaster.InvoiceId);
                    text_invoice_date.setText(salesMaster.InvoiceDates);
                    text_store.setText("");
                    if (salesMaster.TrnType.equals("S")){
                        frag_to_TvInvoiceTitle.setText("Invoice");
                    }
                    else if (salesMaster.TrnType.equals("R")){
                        frag_to_TvInvoiceTitle.setText("Return Invoice");

                    }
                    text_sub_total_value.setText(String.valueOf(salesMaster.SubTotal));
                    text_payment.setText("Payment Type: "+salesMaster.PayMode);
                    text_total_value.setText(String.valueOf(salesMaster.NetValue));
                    if (salesMaster.Return != null) {
                        text_return.setText(String.valueOf(salesMaster.Return));

                    } else {
                        text_return.setText("0");

                    }
                    text_discount.setText(String.valueOf(salesMaster.Discount));

                    rcl_approval_in_list = findViewById(R.id.rcl_approval_in_list);

                    LinearLayoutManager lm = new LinearLayoutManager(this);
                    lm.setOrientation(LinearLayoutManager.VERTICAL);
                    rcl_approval_in_list.setLayoutManager(lm);


                    compositeDisposable.add(Common.salesDetailsRepository.getBookStockModel(invoiceId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetailPrintModel>>() {
                        @Override
                        public void accept(List<SalesDetailPrintModel> departments) throws Exception {
                            Log.e("Book", "Book" + new Gson().toJson(departments));
                            printModels = departments;
                            mAdapters = new PrintSalesAdapter(InvoicePrintAgainActivity.this, departments);
                            rcl_approval_in_list.setAdapter(mAdapters);
                        }
                    }));


//                    create_bill.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Printooth.INSTANCE.hasPairedPrinter())
//                                startActivityForResult(new Intent(InvoicePrintAgainActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
//                            else
//                                printSomePrintable();
//
//                        }
//                    });
                    create_challan.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onClick(View mView) {
                            if (test)
                            {
                                Thread t = new Thread() {
                                    public void run() {
                                        try {

                                            final OutputStream os = mBluetoothSocket
                                                    .getOutputStream();
                                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                            Date date = new Date(System.currentTimeMillis());
                                            String currentDate = formatter.format(date);
                                            SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                                            Date dates = new Date(System.currentTimeMillis());
                                            String currentTime = formatters.format(dates);
                                            String BILL = "";

                                            String datesss=getValue(formatter.format(salesMaster.InvoiceDate));
                                            String inv=getValue(salesMaster.InvoiceNumber);
                                            String mobile=getValue(customer.MobileNumber);

                                            String strUserName=SharedPreferenceUtil.getUserNameBangla(InvoicePrintAgainActivity.this);
                                            String strUserAddress=SharedPreferenceUtil.getUserAddressBangla(InvoicePrintAgainActivity.this);
                                            String strUserNameEnglish;
                                            if (strUserName==null ||strUserName.equals("")){

                                                strUserName=SharedPreferenceUtil.getUserName(InvoicePrintAgainActivity.this);
                                                if (strUserName==null ||strUserName.equals("")){
                                                    strUserName="N/A";
                                                }

                                            }
                                            if (strUserAddress==null ||strUserAddress.equals("")){

                                                strUserAddress=SharedPreferenceUtil.getUserAddress(InvoicePrintAgainActivity.this);
                                                if (strUserAddress==null ||strUserAddress.equals("")){
                                                    strUserAddress="N/A";
                                                }

                                            }


                                            BILL = "               " + strUserName + " \n" +
                                                    "       " + strUserAddress  + "  \n" +
                                                    "    (লেকচার পাবলিকেশন লিমিটেড অনুমোদিত এজেন্ট)      \n \n" +
                                                    "ইনভয়েস নং: " + inv + "\n" +
                                                    "      তারিখ : " + datesss + "\n" +

                                                    "    গ্রন্থাগার নাম: " + customer.ShopName + " \n" +
                                                    "    গ্রাহক নাম: " + customer.Name+"("+customer.RetailerCode+")" + " \n" +
                                                    "   মোবাইল নং: " + mobile + "\n" +
                                                    "\n";
                                            BILL = BILL
                                                    + "---------------------------------------------------------------------------------\n";

                                         //   BILL = BILL + getWidth("বইয়ের নাম",1)+ getWidth("সংখ্যা",2)+ getWidth("মূল্য",3)+ getWidth("মোট টাকা",4)+"\n";

                                         //   BILL = BILL + String.format("%-30s%-15s%-15s%-10s", "বইয়ের নাম", "সংখ্যা ", "মূল্য ", "মোট টাকা");
                                            BILL = BILL + "\n";
                                            BILL = BILL
                                                    + "---------------------------------------------------------------------------------\n";

                                            for (SalesDetailPrintModel salesDetailPrintModel : printModels) {

                                                Log.e("data","datass"+salesDetailPrintModel.BookNameBangla);
                                                String value="";

                                                String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
                                                String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
                                                double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
                                                double ww = Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
                                                String totalPrice = getValue(String.valueOf(rounded(price,2)));
                                                if (totalPrice.equals("০.০")){
                                                    totalPrice="  "+totalPrice;
                                                }

                                                String wws = getValue(String.valueOf(rounded(ww,2)));
                                                if (wws.equals("০.০")){
                                                    // wws="   "+wws;

                                                }
                                                else{
                                                    if (quantity.length()<3){
                                                        quantity="    "+quantity;
                                                    }
                                                    else if (quantity.length()<2){
                                                        quantity="     "+quantity;
                                                    }
                                                }
                                                String bookName = salesDetailPrintModel.BookNameBangla;
                                                if (bookName.length() > 12) {
//                                        Paint   mPaint = new Paint();
//                                        mPaint.setAntiAlias(true);
//                                        mPaint.setStrokeWidth(5);
//                                        mPaint.setStrokeCap(Paint.Cap.ROUND);
//                                        mPaint.setTextSize(64);
//                                        mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//// ...

                                                   // value = bookName.substring(0, 15);
                                                    //float w = mPaint.measureText(value, 0, value.length());
                                                    // Log.e("data1","datass1"+w);
                                                    Log.e("data1","datass1"+value.length());

                                                } else {
                                                    value = salesDetailPrintModel.BookNameBangla;
                                                }
                                             //   BILL = BILL + getWidth(value,1)+ getWidth(quantity+"",2)+ getWidth(wws+"",3)+ getWidth(totalPrice+"",4)+"\n";

                                             //   BILL = BILL+ String.format("%-20s%-18s%-15s%-10s",getWidth(value,1), quantity, wws, totalPrice) + "\n";
                                            }


                                            String cc;
                                            if (salesMaster.Return != null) {
                                                cc = salesMaster.Return;

                                            } else {
                                                cc = "";
                                            }
                                            BILL = BILL
                                                    + "---------------------------------------------------------------------------------";
                                            BILL = BILL + "\n";
                                            String subTotal = getValue(salesMaster.SubTotal);
                                            String Total = getValue(String.valueOf(salesMaster.NetValue));
                                            String Return = getValue(String.valueOf(salesMaster.Return));
                                            String Discount = getValue(String.valueOf(salesMaster.Discount));
                                            BILL = BILL + "                       মোট টাকা    :" + "  " + subTotal + " (ট )" + "\n";
                                            BILL = BILL + "                       ছাড়        :" + "  " + Discount + " %" + "\n";
                                            BILL = BILL + "                       ফেরৎ        :" + "  " + Return + " (ট )" + "\n";
                                            BILL = BILL + "                       নেট টাকা     :" + " " + Total + " (ট )" + "\n";
                                            BILL = BILL + "                  পরিশোধিত     :" + " " + salesMaster.PayMode + ""+"\n";

                                            BILL = BILL
                                                    + "---------------------------------------------------------------------------------\n";
                                            BILL=  BILL  + "  আপনার সহযোগিতার জন্য ধন্যবাদ\n\n";
                                            BILL=  BILL  + " Developed By                               Printed At\n";
                                            BILL=  BILL  + "  www.xactidea.com                 "+currentDate+" "+currentTime+"\n";
                                            BILL = BILL + "\n\n";
                                            BILL = BILL + "\n\n";
                                            BILL = BILL + "\n\n";
                                            BILL = BILL + "\n\n";
                                            BILL = BILL + "\n\n";

                                            Drawable d = ContextCompat.getDrawable(InvoicePrintAgainActivity.this, R.mipmap.ic_launcher);
//
                                            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                                                    | Paint.LINEAR_TEXT_FLAG);
                                            textPaint.setStyle(Paint.Style.FILL);
                                            textPaint.setTextSize(25);

                                            StaticLayout mTextLayout = new StaticLayout(BILL, textPaint,
                                                    570, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

                                            // Create bitmap and canvas to draw to
                                            Bitmap b = Bitmap.createBitmap(770, mTextLayout.getHeight(), Bitmap.Config.RGB_565);

                                            PDFTest pdfTest= new PDFTest(InvoicePrintAgainActivity.this,salesMaster,customer,printModels);
                                            Bitmap v=   pdfTest.getBitmap();
                                       //     Bitmap cs= getResizedBitmap();
                                            Canvas c = new Canvas(b);

                                            // Draw background
                                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                                                    | Paint.LINEAR_TEXT_FLAG);
                                            paint.setStyle(Paint.Style.FILL);
                                            paint.setColor(Color.WHITE);
                                            c.drawPaint(paint);

                                            // Draw text
                                            c.save();
                                            c.translate(0, 0);
                                            mTextLayout.draw(c);
                                            c.restore();
//
                                            PrintPic printPic = PrintPic.getInstance();

                                            printPic.init(v);

                                          //  v.recycle();
                                            byte[] bitmapdata = printPic.printDraw();
                                            os.write(bitmapdata);

                                            int gs = 29;
                                            os.write(intToByteArray(gs));
                                            int h = 104;
                                            os.write(intToByteArray(h));
                                            int n = 162;
                                            os.write(intToByteArray(n));

                                            // Setting Width
                                            int gs_width = 29;
                                            os.write(intToByteArray(gs_width));
                                            int w = 119;
                                            os.write(intToByteArray(w));
                                            int n_width = 2;
                                            os.write(intToByteArray(n_width));
                                            oStream = new PrintWriter(mBluetoothSocket.getOutputStream());

                                            oStream.write(0x1D);
                                            oStream.write(86);
                                            oStream.write(48);
                                            oStream.write(0);

                                        } catch (Exception e) {
                                            Log.e("InvoicePrintAgainAivity", "Exe ", e);
                                        }
                                    }
                                };
                                t.start();
                            }
                            else
                              {
//                                    PDFTest pdfTest= new PDFTest(InvoicePrintAgainActivity.this,salesMaster,customer,printModels);
//                                    Bitmap v=   pdfTest.getBitmap();
//                                    String s="";
                                  //Bitmap v1=   pdfTest.getBitmapTwo();
//                                //    Bitmap dstBmp=getResizedBitmap(v1);
//
//                                    //Bitmap qwww=crop(v1);
//
//                                   // Bitmap vs=combineImages(v,v1);
//                                    String s="";

                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                if (mBluetoothAdapter == null) {
                                    Toast.makeText(InvoicePrintAgainActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!mBluetoothAdapter.isEnabled()) {
                                        Intent enableBtIntent = new Intent(
                                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                        startActivityForResult(enableBtIntent,
                                                REQUEST_ENABLE_BT);
                                    } else {
                                        ListPairedDevices();
                                        Intent connectIntent = new Intent(InvoicePrintAgainActivity.this,
                                                DeviceListActivity.class);
                                        startActivityForResult(connectIntent,
                                                REQUEST_CONNECT_DEVICE);
                                    }
                                }

//

                            }

                        }
                    });
                } else {

                }
            } else {

            }
        } else {

        }

    }
    public Bitmap getResizedBitmap(Bitmap originalImage) {
        Bitmap background = Bitmap.createBitmap((int)770, (int)270, Bitmap.Config.ARGB_8888);

        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = 270 / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (270 - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);

        return background;
    }
    public Bitmap crop (Bitmap bitmap){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty,0);

        int top = 0;
        int left = 0;
        int botton = height;
        int right = width;

        for (int y = 0; y < height; y++) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }

        for (int y = height - 1; y > top; y--) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                botton = y;
                break;
            }
        }


        int bufferSize = botton -top +1;
        empty = new int[bufferSize];
        buffer = new int[bufferSize];
        Arrays.fill(empty,0);

        for (int x = 0; x < width; x++) {
            bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }

        Arrays.fill(empty, 0);
        for (int x = width - 1; x > left; x--) {
            bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }

        Bitmap cropedBitmap = Bitmap.createBitmap(bitmap, left, top, right-left, botton-top);
        return cropedBitmap;
    }

    public static Bitmap TrimBitmap(Bitmap bmp) {
        int imgHeight = bmp.getHeight();
        int imgWidth  = bmp.getWidth();


        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for(int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM WIDTH - RIGHT
        int endWidth  = 0;
        for(int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }



        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for(int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }



        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for(int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0 ) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }


        return Bitmap.createBitmap(
                bmp,
                startWidth,
                startHeight,
                endWidth - startWidth,
                endHeight - startHeight
        );

    }
    private Bitmap CropBitmapTransparency(Bitmap sourceBitmap)
    {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++)
        {
            for(int x = 0; x < sourceBitmap.getWidth(); x++)
            {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if(alpha > 0)   // pixel is not 100% transparent
                {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }
        if((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }
    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight()+ s.getHeight();
        } else {
            width = s.getWidth() ;
            height = c.getHeight()+ s.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f,c.getHeight(), null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

        return cs;
    }
    private void printSomePrintable() {
        getSomePrintables();
    }

    private void getSomePrintables() {


        ArrayList<Printable> al = new ArrayList<>();
        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode
        String BILL = "";

        BILL = "                  " + SharedPreferenceUtil.getUserName(InvoicePrintAgainActivity.this) + " \n \n" +

                "                   Customer Copy        \n \n" +
                "Invoice Number : " + salesMaster.InvoiceNumber + "\n" +
                "Date : " + salesMaster.InvoiceDates + "\n" +

                "Customer Name: " + customer.Name + " \n" +
                "Phone Number: " + customer.MobileNumber + "\n" +
                "\n";
        BILL = BILL
                + "-----------------------------------------------\n";


        BILL = BILL + String.format("%-22s%-10s%-10s%-10s", "Book Name", "Qty", "Price", "Amount");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";

        for (SalesDetailPrintModel salesDetailPrintModel : printModels) {

            String value;

            String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
            String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
            double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
            double ww = Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
            String totalPrice = getValue(String.valueOf(price));
            String bookName = salesDetailPrintModel.BookName;
            if (bookName.length() > 15) {
                value = bookName.substring(0, 18);
            } else {
                value = salesDetailPrintModel.BookName;
            }

            BILL = BILL + "\n" + String.format("%-22s%-9s%-9s%-10s", value, salesDetailPrintModel.Quantity, ww, String.valueOf(price));
        }

        String cc;
        if (salesMaster.Return != null) {
            cc = salesMaster.Return;

        } else {
            cc = "";
        }
        BILL = BILL
                + "-----------------------------------------------";
        BILL = BILL + "\n";
        String subTotal = getValue(sub);
        String Total = getValue(String.valueOf(salesMaster.NetValue));
        String Return = getValue(String.valueOf(salesMaster.Return));
        String Discount = getValue(String.valueOf(salesMaster.Discount));
        BILL = BILL + "                   Sub Total    :" + "  " + salesMaster.SubTotal + " Tk" + "\n";
        BILL = BILL + "                   Discount     :" + "  " + salesMaster.Discount + " %" + "\n";
        BILL = BILL + "                   Return       :" + "  " + cc + " Tk" + "\n";
        BILL = BILL + "                   Total Amount :" + " " + salesMaster.NetValue + " Tk" + "\n";
        BILL = BILL + "                   Payment Via  :" + " " + salesMaster.PayMode + ""+"\n";

        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n";

        al.add((new TextPrintable.Builder())
                .setText(BILL)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());


        String BILLS = "";

        BILLS = "                    Challan Copy        \n" +
                "                                         \n" +
                "Invoice Number : " + salesMaster.InvoiceNumber + "\n" +
                "Date : " + salesMaster.InvoiceDates + "\n" +
                "Name:  " + customer.Name + "\n" +
                "Number: " + customer.MobileNumber + "\n"

        ;
        BILLS = BILLS
                + "\n-----------------------------------------------\n";


        BILLS = BILLS + String.format("%1$-10s %2$10s", "Book Name", "                  Quantity");
        BILLS = BILLS + "\n";
        BILLS = BILLS
                + "-----------------------------------------------";

        for (SalesDetailPrintModel salesDetailPrintModel : printModels) {

            String value;
            String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
            String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
            double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice);
            String totalPrice = getValue(String.valueOf(price));
            String bookName = salesDetailPrintModel.BookName;
            String spacer = null;
            if (bookName.length() > 25) {
                value = bookName.substring(0, 25);
            } else {
                if (bookName.length() - 25 > 0) {

                } else {


                    for (int i = 0; i < 25 - bookName.length(); i++) {
                        spacer = spacer + " ";
                    }
                }
                value = salesDetailPrintModel.BookName + spacer;
            }
            BILLS = BILLS + "\n" + String.format("%1$-10s %2$10s", value, "" + salesDetailPrintModel.Quantity);
        }


        BILLS = BILLS
                + "\n-----------------------------------------------";
        BILLS = BILLS + "\n\n ";


        BILLS = BILLS + "\n\n ";
        BILLS = BILLS + "\n\n\n\n\n\n\n\n";
//

      
//                .setText("Hello World : été è à €")
//                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("Hello World")
//                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("Hello World")
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_RIGHT())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("اختبار العربية")
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setFontSize(DefaultPrinter.Companion.getFONT_SIZE_NORMAL())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_ARABIC_FARISI())
//                .setNewLinesAfter(1)
//                .setCustomConverter(new ArabicConverter()) // change only the converter for this one
//                .build());

    }
    PrintWriter oStream;
    private void printSomePrintableChalan() {
        Log.d("xxx", "printSomePrintable ");
        if (printing != null)
            printing.print(getSomePrintablesChalan());
        //  getSomePrintables();
    }

    private ArrayList<Printable> getSomePrintablesChalan() {


        ArrayList<Printable> al = new ArrayList<>();
        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode
        String BILL = "";

        BILL = "                  " + customer.ShopName + " \n " +
                "                   " + customer.Name + " \n " +
                "                   " + customer.MobileNumber + " \n " +
                "                   " + customer.Address + " \n " +
                "                   " + customer.RetailerCode + " \n " +
                "                   " + customer.StoreId + " \n "
                + "\n";
        BILL = BILL
                + "-----------------------------------------------\n";


        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "সংখ্যা");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";

        for (SalesDetailPrintModel salesDetailPrintModel : printModels) {

            String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
            String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
            double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice);
            String totalPrice = getValue(String.valueOf(price));

            BILL = BILL + String.format("%1$-10s %2$10s %3$11s %4$10s", salesDetailPrintModel.BookNameBangla, quantity);
        }


        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n ";


        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";
        BILL = BILL + "\n\n\n\n\n\n\n\n";
        al.add((new TextPrintable.Builder())
                .setText(BILL)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
//


//                .setText("Hello World : été è à €")
//                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("Hello World")
//                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("Hello World")
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_RIGHT())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setNewLinesAfter(1)
//                .build());
//
//        al.add( (new TextPrintable.Builder())
//                .setText("اختبار العربية")
//                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
//                .setFontSize(DefaultPrinter.Companion.getFONT_SIZE_NORMAL())
//                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_ARABIC_FARISI())
//                .setNewLinesAfter(1)
//                .setCustomConverter(new ArabicConverter()) // change only the converter for this one
//                .build());

        return al;
    }

    private String getValue(String value) {
        char[] chBookTotalPrice = new char[(String.valueOf(value).length())];
//
        // Copy character by character into array
        for (int i = 0; i < (String.valueOf(value).length()); i++) {
            chBookTotalPrice[i] = (String.valueOf(value).charAt(i));
        }

        StringBuilder stringBuilderBookTotalPrice = new StringBuilder();
        // Printing content of array
        for (char c2 : chBookTotalPrice) {
            if (c2 == '1') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('1', '১');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '2') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('2', '২');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '3') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('3', '৩');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '4') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('4', '৪');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '5') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('5', '৫');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '6') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('6', '৬');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '7') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('7', '৭');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '8') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('8', '৮');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '9') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('9', '৯');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '0') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('0', '০');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '.') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('.', '.');
                stringBuilderBookTotalPrice.append(replaceString);
            }


        }
        String prices = stringBuilderBookTotalPrice.toString();
        return prices;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intent);
//        compositeDisposable.clear();
//    }
    //    @Override
//    public void onBackPressed() {
////        Intent intent = new Intent(this, MainActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////        startActivity(intent);
////        InvoicePrintAgainActivity.this.finish();
//    }
//public boolean onKeyDown(int keyCode, KeyEvent event) {
//    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//        // you don't need to call finish(); because
//        // return super.onKeyDown(keyCode, event); does that for you
//
//        // clear your SharedPreferences
//       // getSharedPreferences("preferenceName",0).edit().clear().commit();
//    }
//    return super.onKeyDown(keyCode, event);
//}

    @Override
    public void onBackPressed() {


//        compositeDisposable.clear();
        if (valueFor.equals("Adjustment")){
            Intent intent = new Intent(InvoicePrintAgainActivity.this, AdjustmentListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            try {
                if (mBluetoothSocket != null)
                    mBluetoothSocket.close();
            } catch (Exception e) {
                Log.e("Tag", "Exe ", e);
            }
            setResult(RESULT_CANCELED);
            finish();
        }
        else  if (valueFor.equals("Customer")){
            Intent intent = new Intent(InvoicePrintAgainActivity.this, CustomerDetailsActivity.class);
            intent.putExtra("ShopName",salesMaster.CustomerName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            try {
                if (mBluetoothSocket != null)
                    mBluetoothSocket.close();
            } catch (Exception e) {
                Log.e("Tag", "Exe ", e);
            }
            setResult(RESULT_CANCELED);
            finish();
        }
        else  if (valueFor.equals("Invoice")){
            Intent intent = new Intent(InvoicePrintAgainActivity.this, InvoiceListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            try {
                if (mBluetoothSocket != null)
                    mBluetoothSocket.close();
            } catch (Exception e) {
                Log.e("Tag", "Exe ", e);
            }
            setResult(RESULT_CANCELED);
            finish();
        }
        else  if (valueFor.equals("Return")){
            Intent intent = new Intent(InvoicePrintAgainActivity.this, SalesReturnListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            try {
                if (mBluetoothSocket != null)
                    mBluetoothSocket.close();
            } catch (Exception e) {
                Log.e("Tag", "Exe ", e);
            }
            setResult(RESULT_CANCELED);
            finish();
        }



     //   super.onBackPressed();
    }
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(InvoicePrintAgainActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(InvoicePrintAgainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(InvoicePrintAgainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            test=true;
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }
    int totalWidth=0;
    int pos=0;
    private  String getWidth(String text,int type)
    {
        Paint textPaint = new Paint();
        String result=text;
        //  TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#ff00ff"));
        textPaint.setTextSize(20);
        Rect bounds = new Rect();
        int width =0;
        int remain=0;
        int remainder=0;
        int toatlSpaces=0;
        Log.e("totalWidth",pos+"  "+totalWidth);
        pos++;
        if(type==1)
        {
            if(!result.startsWith(" "))
            {
                result= spaces[4] +result;
            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            width = bounds.width()+25;
            remain=250-width;
            remainder=remain/5;
            totalWidth=width;
            if(remainder>0)
            {
                result= result+spaces[remainder-1] ;
                totalWidth=width+remainder*5;
            }


        }
        else if(type==2)
        {
            if((250-totalWidth)>3)
            {
                result= spaces[3] +result;
                toatlSpaces=4;
            }
            else if((totalWidth-250)>3)
            {
                result= spaces[1] +result;
                toatlSpaces=2;

            }
            else
            {

                result= spaces[2] +result;
                toatlSpaces=3;


            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            //Log.e("width->2",""+width);
            width = bounds.width()+toatlSpaces*5;
            totalWidth=totalWidth+width;
            remain=350-totalWidth;
            remainder=remain/5;
            if(remainder>0)
            {
                result= result+spaces[remainder-1] ;
                totalWidth=totalWidth+remainder*5;

            }


        }
        else if(type==3)
        {

            if((350-totalWidth)>3)
            {
                result= spaces[3] +result;
                toatlSpaces=4;
            }
            else if((totalWidth-350)>3)
            {
                result= spaces[1] +result;
                toatlSpaces=2;

            }
            else
            {

                result= spaces[2] +result;
                toatlSpaces=3;

            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);

            width = bounds.width()+toatlSpaces*5;

            totalWidth=totalWidth+width;
            remain=450-totalWidth;
            remainder=remain/5;
            if(remainder>0)
            {
                result= result+spaces[remainder-1] ;
                totalWidth=totalWidth+remainder*5;

            }

        }
        else
        {
            if((450-totalWidth)>3)
            {
                result= spaces[3] +result;
                toatlSpaces=4;
            }
            else if((totalWidth-450)>3)
            {
                result= spaces[1] +result;
                toatlSpaces=2;

            }
            else
            {

                result= spaces[2] +result;
                toatlSpaces=3;

            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            width = bounds.width()+toatlSpaces*5;
            totalWidth=totalWidth+width;
            remain=550-totalWidth;

            remainder=remain/5;
            if(remainder>0)
            {
                result= result+spaces[remainder-1] ;
                totalWidth=totalWidth+width;
            }



        }


        return  result;
    }
    String spaces[]={" ",
            "  ",
            "   ",
            "    ",
            "     ",
            "      ",
            "       ",
            "        ",
            "         ",
            "          ",
            "           ",
            "            ",
            "             ",
            "              ",
            "               ",
            "                ",
            "                 ",
            "                  ",
            "                   ",
            "                    ",
            "                     ",
            "                      ",
            "                       ",
            "                        ",
            "                         ",
            "                          ",
            "                           ",
            "                            ",
            "                             ",
            "                              ",
            "                               ",
            "                                ",

    };
    private void generatePDF() throws DocumentException, IOException, BadElementException {

        Font font = null;
//        try {
//            font = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        }
        PdfPTable headerTable = getEmptyTable(new float[]{1});
        headerTable.getDefaultCell().setPadding(2);
        headerTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        String BILL = "";

        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "পরিমাণ", "হার", "মোট",BENGALI);
        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "dfd", "g", "g", "মোট",BENGALI);

        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

        BILL = BILL
                + "\n-----------------------------------------------";
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

        String strUserName=SharedPreferenceUtil.getUserNameBangla(InvoicePrintAgainActivity.this);
        String strUserAddress=SharedPreferenceUtil.getUserAddressBangla(InvoicePrintAgainActivity.this);
        String strUserNameEnglish;
        if (strUserName==null ||strUserName.equals("")){

            strUserName=SharedPreferenceUtil.getUserName(InvoicePrintAgainActivity.this);
            if (strUserName==null ||strUserName.equals("")){
                strUserName="N/A";
            }

        }
        if (strUserAddress==null ||strUserAddress.equals("")){

            strUserAddress=SharedPreferenceUtil.getUserAddress(InvoicePrintAgainActivity.this);
            if (strUserAddress==null ||strUserAddress.equals("")){
                strUserAddress="N/A";
            }

        }


        BILLq = "               " + strUserName + " \n" +
                "       " + strUserAddress  + "  \n" +
                "    (লেকচার পাবলিকেশন লিমিটেড অনুমোদিত এজেন্ট)      \n \n" +
                "ইনভয়েস নং: " + inv + "\n" +
                "      তারিখ : " + datesss + "\n" +

                "    গ্রন্থাগার নাম: " + customer.ShopName + " \n" +
                "    গ্রাহক নাম: " + customer.Name+"("+customer.RetailerCode+")" + " \n" +
                "   মোবাইল নং: " + mobile + "\n" +
                "\n";
        headerTable.addCell(getParaS(strUserName,12));
        headerTable.addCell(getParaS(strUserAddress,12));
        headerTable.addCell(getParaS("(লেকচার পাবলিকেশন লিমিটেড অনুমোদিত এজেন্ট)",12));
        headerTable.addCell(getParaS("ইনভয়েস নং: " + inv  ,12));
        headerTable.addCell(getParaS("তারিখ : " + datesss  ,12));
        headerTable.addCell(getParaS("গ্রন্থাগার নাম : " + customer.ShopName  ,12));
        headerTable.addCell(getParaS("গ্রাহক নাম: " + customer.Name+"("+customer.RetailerCode +")"   ,12));
        headerTable.addCell(getParaS("মোবাইল নং:" + mobile  ,12));
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setTextSize(50);



        StaticLayout mTextLayout = new StaticLayout("বইয়ের নাম", textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap image = Bitmap.createBitmap(570, mTextLayout.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(image);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white));
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
        paint2.setColor(getResources().getColor(R.color.white));
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
        paint3.setColor(getResources().getColor(R.color.white));
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
        paint4.setColor(getResources().getColor(R.color.white));
        c4.drawPaint(paint4);

        // Draw text
        c4.save();
        c4.translate(0, 0);
        mTextLayout4.draw(c4);
        c4.restore();
        Bitmap image24=image;
        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
        Bitmap bitmap4 = image24;
        bitmap4.compress(Bitmap.CompressFormat.JPEG, 100 , stream4);
        Image myImg4 = Image.getInstance(stream4.toByteArray());
//
//
//
        PdfPTable table = new PdfPTable(4);

        table.setHeaderRows(1);



        PdfPTable logoTable = getEmptyTable(new float[]{1,1,1,1});
        logoTable.addCell(myImg);
        logoTable.addCell(myImg2);
        logoTable.addCell(myImg3);
        logoTable.addCell(myImg4);
        String[] colors = { "দা.৯.পৌরনীতি-(নোট)", "দা.৯.তথ্য ও যোগাযোগ প্রযুক্তি-(নোট)", "দা.৯.শারীরিক শিক্ষা-(নোট)" };
        String[] colors1 = { colors[0], "50", "150" ,"544"};

        for (int i=0;i<5;i++){
            for (String data : colors1)
            {
                StaticLayout mText = new StaticLayout(data, textPaint, 570, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // Create bitmap and canvas to draw to
                Bitmap ip = Bitmap.createBitmap(570, mText.getHeight(), Bitmap.Config.ARGB_4444);
                Canvas c1 = new Canvas(ip);

                // Draw background
                Paint pp = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
                pp.setStyle(Paint.Style.FILL);
                pp.setColor(getResources().getColor(R.color.white));
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
        }

        mDocument.add(logoTable);
        mDocument.add(headerTable);
        mDocument.add(Chunk.NEWLINE);

        mDocument.add(table);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getBitmap(){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
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
    private Paragraph getRedColorText(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        return p;
    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale((this) , android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((this) , new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale((this) , android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((this) , new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        }
    }

    public String getFilePath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Emu";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "sa1cd.pdf";
    }

}