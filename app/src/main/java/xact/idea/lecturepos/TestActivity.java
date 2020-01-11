package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
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
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.PrintPic;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.UnicodeFormatter;

import static xact.idea.lecturepos.Utils.Utils.getValue;

public class TestActivity extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    String invoiceId;
    String customerName;
    String sub;
    String ww;
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
    TextView text_total_value;
    TextView text_return;
    TextView text_discount;
    TextView text_payment;
    TextView create_challan;
    TextView text_sub_total_value;
    RecyclerView rcl_approval_in_list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PrintSalesAdapter mAdapters;
    List<SalesDetailPrintModel> printModels;
    private Printing printing = null;
    PrintingCallback printingCallback=null;
    Customer customer;
    SalesMaster salesMaster;


    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_invoice_print);
        mScan = (Button) findViewById(R.id.Scan);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(TestActivity.this, "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(TestActivity.this,
                        DeviceListActivity.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }

        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));

        frag_to_TvInvoiceTitle=findViewById(R.id.frag_to_TvInvoiceTitle);
        text_payment=findViewById(R.id.text_payment);
        tv_temp_one_company_name=findViewById(R.id.tv_temp_one_company_name);
        tv_temp_one_address=findViewById(R.id.tv_temp_one_address);
        create_bill=findViewById(R.id.create_bill);
        create_challan=findViewById(R.id.create_challan);
        text_invoice_number=findViewById(R.id.text_invoice_number);
        text_invoice_date=findViewById(R.id.text_invoice_date);
        text_bill_shop_name=findViewById(R.id.text_bill_shop_name);
        text_biil_customer_name=findViewById(R.id.text_biil_customer_name);
        text_bill_address=findViewById(R.id.text_bill_address);
        text_bill_phone_number=findViewById(R.id.text_bill_phone_number);
        text_bill_retail_code=findViewById(R.id.text_bill_retail_code);
        text_ship_shop_name=findViewById(R.id.text_ship_shop_name);
        text_ship_shop_address=findViewById(R.id.text_ship_shop_address);
        text_ship_shop_number=findViewById(R.id.text_ship_shop_number);
        text_ship_retail_code=findViewById(R.id.text_ship_retail_code);
        text_store=findViewById(R.id.text_store);
        text_total_value=findViewById(R.id.text_total_value);
        text_return=findViewById(R.id.text_return);
        text_discount=findViewById(R.id.text_discount);
        text_sub_total_value=findViewById(R.id.text_sub_total_value);
        invoiceId = getIntent().getStringExtra("invoiceId");
        tv_temp_one_company_name.setText(SharedPreferenceUtil.getUserName(TestActivity.this));
        tv_temp_one_address.setText(SharedPreferenceUtil.getUserAddress(TestActivity.this));
        if (invoiceId!=null){

            customerName = getIntent().getStringExtra("customerName");

            salesMaster= Common.salesMasterRepository.getSalesMaster(invoiceId);
            if (salesMaster!=null){
                customer=Common.customerRepository.getCustomerss(customerName);
                if (customer!=null){
                    Constant.rate="rate1";
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

                    text_sub_total_value.setText(salesMaster.SubTotal);
                    if (salesMaster.TrnType.equals("S")){
                        frag_to_TvInvoiceTitle.setText("Invoice");
                    }
                    else if (salesMaster.TrnType.equals("R")){
                        frag_to_TvInvoiceTitle.setText("Return Invoice");

                    }

                    text_payment.setText("Payment Type: "+salesMaster.PayMode);

                    text_total_value.setText(String.valueOf(salesMaster.NetValue));
                    if (salesMaster.Return!=null){
                        text_return.setText(String.valueOf(salesMaster.Return));

                    }
                    else {
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
                            Log.e("Book","Book"+new Gson().toJson(departments));
                            printModels=departments;
                            mAdapters=new PrintSalesAdapter(TestActivity.this,departments);
                            rcl_approval_in_list.setAdapter(mAdapters);
                        }
                    }));


//                    create_bill.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Printooth.INSTANCE.hasPairedPrinter())
//                                startActivityForResult(new Intent(InvoicePrintActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
//                            else
//                                printSomePrintable();
//
//                        }
//                    });
                    create_challan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Thread t = new Thread() {
                                public void run() {
                                    try {

                                        final OutputStream os = mBluetoothSocket
                                                .getOutputStream();
                                        String BILL = "";

                                        BILL = "                                                              XXXX MART    \n"
                                                + "                                                     XX.AA.BB.CC.     \n " +
                                                "                                       NO 25 ABC ABCDE    \n" +
                                                "                                       XXXXX YYYYYY      \n" +
                                                "                                          MMM 590019091      \n";
                                        BILL = BILL
                                                + "-----------------------------------------------\n";

                                        BILL = BILL + String.format("%-22s%-17s%-17s%-17s", "বইয়ের নাম", "বইয়ের নাম", "বইয়ের নাম", "বইয়ের নাম");
                                        // BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "বইয়ের নাম", "বইয়ের নাম", "বইয়ের নাম");
                                        BILL = BILL + "\n";
                                        BILL = BILL
                                                + "---------------------------------------------------------------------------";
                                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");
                                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
                                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
                                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

                                        BILL = BILL
                                                + "\n-----------------------------------------------";
                                        BILL = BILL + "\n\n ";

                                        BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
                                        BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";

                                        BILL = BILL
                                                + "-----------------------------------------------\n";
                                        BILL = BILL + "\n\n ";
                                        byte[] s1 = BILL.getBytes();
//                            AssetManager am = getAssets();
//                            InputStream inputStream = am.open("ee.pdf");
//                            byte[] buffer = new byte[8192];
//                            int bytesRead;
//                            ByteArrayOutputStream output = new ByteArrayOutputStream();
//                            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                                output.write(buffer, 0, bytesRead);
//                            }
//                        //    byte file[] = s1;
//
//
//                            byte[] encodeByte = Base64.decode(buffer, Base64.DEFAULT);
//                            YuvImage yuvimage = new YuvImage(encodeByte, ImageFormat.JPEG, 500, 500, null);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            yuvimage.compressToJpeg(new Rect(0, 0, 500, 500), 100, baos);
//                            byte[] jdata = baos.toByteArray();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, buffer.length);
//                            // Bitmap bitmap = Bitmap.createBitmap(page.Width, page.Height, Bitmap.Config.Argb8888);
//                            //Bitmap bitmap = BitmapFactory.decodeByteArray(file , 0, file .length);
//                            //  printables.add(new ImagePrintable.Builder(bitmap).build());
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                            final byte[] byteArray = stream.toByteArray();
                                        Drawable d = ContextCompat.getDrawable(TestActivity.this, R.mipmap.ic_launcher);
                                        Bitmap bitmap1 = ((BitmapDrawable) d).getBitmap();

                                        // ByteArrayOutputStream stream = new ByteArrayOutputStream();

//                            Bitmap bmpMonochrome = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
//                            Canvas canvas = new Canvas(bmpMonochrome);
//                            ColorMatrix ma = new ColorMatrix();
//                            ma.setSaturation(0);
//                            Paint paint = new Paint();
//                            paint.setColorFilter(new ColorMatrixColorFilter(ma));
//                            canvas.drawBitmap(bitmap, 0, 0, paint);
//                            PrintPic printPic = PrintPic.getInstance();
//                            printPic.init(bitmap1);
                                        //  byte[] bitmapdata = printPic.printDraw();
                                        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                                                | Paint.LINEAR_TEXT_FLAG);
                                        textPaint.setStyle(Paint.Style.FILL);
                                        textPaint.setTextSize(25);

                                        StaticLayout mTextLayout = new StaticLayout(BILL, textPaint,
                                                770, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

                                        // Create bitmap and canvas to draw to
                                        Bitmap b = Bitmap.createBitmap(770, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
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
//                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
//                            b.compress(Bitmap.CompressFormat.PNG, 100, stream1);
//                            byte[] byteArray1 = stream1.toByteArray();
                                        PrintPic printPic = PrintPic.getInstance();
                                        printPic.init(b);
                                        byte[] bitmapdata = printPic.printDraw();
                                        os.write(bitmapdata);
                                        // os.write(byteArray);
                                        //This is printer specific code you can comment ==== > Start
                                        // Setting height
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
                                        Log.e("MainActivity", "Exe ", e);
                                    }
                                }
                            };
                            t.start();
                           // getSomePrintables();
//                            if (!Printooth.INSTANCE.hasPairedPrinter())
//                                startActivityForResult(new Intent(InvoicePrintActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
//                            else
//                                printSomePrintable();

                        }
                    });
                }
                else {

                }
            }
            else {

            }
        }
        else {

        }

    }// onCreate
    PrintWriter oStream;
    private void getSomePrintables() {
        Thread t = new Thread() {
            public void run() {
                try {
//                    pdfTest = new PDFTest(MainActivity.this);
//                    byte[] s = pdfTest.getData();
                    final OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String BILL = "";

                    BILL =    "                  "+ SharedPreferenceUtil.getUserName(TestActivity.this) +" \n \n"+

                            "                   Customer Copy        \n \n"+
                            "Invoice Number : "+salesMaster.InvoiceNumber+"\n"+
                            "Date : "+salesMaster.InvoiceDates+"\n"+

                            "Customer Name: "+customer.Name+" \n"+
                            "Phone Number: "+customer.MobileNumber+"\n"+
                            "\n";
                    BILL = BILL
                            + "-----------------------------------------------\n";


                    BILL = BILL + String.format("%-22s%-10s%-10s%-10s", "Book Name", "Qty", "Price", "Amount");
                    BILL = BILL + "\n";
                    BILL = BILL
                            + "-----------------------------------------------";

                    for (SalesDetailPrintModel salesDetailPrintModel: printModels){

                        String value;

                        String quantity=getValue(String.valueOf(salesDetailPrintModel.Quantity));
                        String rate=getValue(String.valueOf(salesDetailPrintModel.BookPrice));
                        double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice)* (1-Double.parseDouble(salesDetailPrintModel.Discount)/100);
                        double ww =  Double.parseDouble(salesDetailPrintModel.BookPrice)* (1-Double.parseDouble(salesDetailPrintModel.Discount)/100);
                        String totalPrice=getValue(String.valueOf(price));
                        String bookName=salesDetailPrintModel.BookName;
                        if (bookName.length()>15){
                            value=bookName.substring(0, 18);
                        }
                        else {
                            value=salesDetailPrintModel.BookName;
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
                    String subTotal=getValue(sub);
                    String Total=getValue(String.valueOf(salesMaster.NetValue));
                    String Return=getValue(String.valueOf(salesMaster.Return));
                    String Discount=getValue(String.valueOf(salesMaster.Discount));
                    BILL = BILL + "                   Sub Total    :" + "  " + salesMaster.SubTotal + " Tk"+"\n";
                    BILL = BILL + "                   Discount     :" + "  " + salesMaster.Discount +" %"+ "\n";
                    BILL = BILL + "                   Return       :" + "  " + cc + " Tk"+"\n";
                    BILL = BILL + "                   Total Amount :" + " " + salesMaster.NetValue + " Tk"+"\n";
                    BILL = BILL + "                   Payment Via  :" + " " + salesMaster.PayMode + ""+"\n";

                    BILL = BILL
                            + "-----------------------------------------------\n";
                    BILL = BILL + "\n\n";




                    String BILLS = "";

                    BILLS =  "                    Challan Copy        \n"+
                            "                                         \n"+
                            "Invoice Number : "+salesMaster.InvoiceNumber+"\n"+
                            "Date : "+salesMaster.InvoiceDates+"\n"+
                            "Name:  "+customer.Name+"\n"+
                            "Number: "+customer.MobileNumber+"\n"

                    ;
                    BILLS = BILLS
                            + "\n-----------------------------------------------\n";


                    BILLS = BILLS + String.format("%1$-10s %2$10s", "Book Name","                  Quantity");
                    BILLS = BILLS + "\n";
                    BILLS = BILLS
                            + "-----------------------------------------------";

                    for (SalesDetailPrintModel salesDetailPrintModel: printModels){

                        String value;
                        String quantity=getValue(String.valueOf(salesDetailPrintModel.Quantity));
                        String rate=getValue(String.valueOf(salesDetailPrintModel.BookPrice));
                        double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice);
                        String totalPrice=getValue(String.valueOf(price));
                        String bookName=salesDetailPrintModel.BookName;
                        String spacer = null;
                        if (bookName.length()>25){
                            value=bookName.substring(0, 25);
                        }
                        else {
                            if (bookName.length()-25>0){

                            }
                            else {


                                for (int i=0;i<25-bookName.length();i++){
                                    spacer=spacer+" ";
                                }
                            }
                            value=salesDetailPrintModel.BookName+spacer;
                        }
                        BILLS = BILLS + "\n" + String.format("%1$-10s %2$10s", value,""+ salesDetailPrintModel.Quantity);
                    }


                    BILLS = BILLS
                            + "\n-----------------------------------------------";
                    BILLS = BILLS + "\n\n ";



                    BILLS = BILLS + "\n\n ";
                    BILLS = BILLS + "\n\n\n\n\n\n\n\n";
//                            AssetManager am = getAssets();
//                            InputStream inputStream = am.open("ee.pdf");
//                            byte[] buffer = new byte[8192];
//                            int bytesRead;
//                            ByteArrayOutputStream output = new ByteArrayOutputStream();
//                            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                                output.write(buffer, 0, bytesRead);
//                            }
//                        //    byte file[] = s1;
//
//
//                            byte[] encodeByte = Base64.decode(buffer, Base64.DEFAULT);
//                            YuvImage yuvimage = new YuvImage(encodeByte, ImageFormat.JPEG, 500, 500, null);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            yuvimage.compressToJpeg(new Rect(0, 0, 500, 500), 100, baos);
//                            byte[] jdata = baos.toByteArray();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, buffer.length);
//                            // Bitmap bitmap = Bitmap.createBitmap(page.Width, page.Height, Bitmap.Config.Argb8888);
//                            //Bitmap bitmap = BitmapFactory.decodeByteArray(file , 0, file .length);
//                            //  printables.add(new ImagePrintable.Builder(bitmap).build());
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                            final byte[] byteArray = stream.toByteArray();


                    // ByteArrayOutputStream stream = new ByteArrayOutputStream();

//                            Bitmap bmpMonochrome = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
//                            Canvas canvas = new Canvas(bmpMonochrome);
//                            ColorMatrix ma = new ColorMatrix();
//                            ma.setSaturation(0);
//                            Paint paint = new Paint();
//                            paint.setColorFilter(new ColorMatrixColorFilter(ma));
//                            canvas.drawBitmap(bitmap, 0, 0, paint);
//                            PrintPic printPic = PrintPic.getInstance();
//                            printPic.init(bitmap1);
                    //  byte[] bitmapdata = printPic.printDraw();
                    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                            | Paint.LINEAR_TEXT_FLAG);
                    textPaint.setStyle(Paint.Style.FILL);
                    textPaint.setTextSize(25);

                    StaticLayout mTextLayout = new StaticLayout(BILL, textPaint,
                            770, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

                    // Create bitmap and canvas to draw to
                    Bitmap b = Bitmap.createBitmap(770, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
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
//                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
//                            b.compress(Bitmap.CompressFormat.PNG, 100, stream1);
//                            byte[] byteArray1 = stream1.toByteArray();
                    PrintPic printPic = PrintPic.getInstance();
                    printPic.init(b);
                    byte[] bitmapdata = printPic.printDraw();
                    os.write(bitmapdata);
                    // os.write(byteArray);
                    //This is printer specific code you can comment ==== > Start
                    // Setting height
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
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();



//        ArrayList<Printable> al = new ArrayList<>();
//        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode
//
////
//
//        al.add( (new TextPrintable.Builder())
//                .setText(BILLS)
//                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
//                .setNewLinesAfter(1)
//                .build());
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
    public void drawText(String text, int textSize) {


        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                370, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(370, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
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

        //printBitmap(b);
    }
    private File createFileFromInputStream(InputStream inputStream) {

        try {
            File f = new File("aa.pdf");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
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
                    Intent connectIntent = new Intent(TestActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(TestActivity.this, "Message", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(TestActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
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

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

}
