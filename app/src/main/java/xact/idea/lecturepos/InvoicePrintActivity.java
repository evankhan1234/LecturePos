package xact.idea.lecturepos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.PrintSalesAdapter;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.PrintPic;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.UnicodeFormatter;

public class InvoicePrintActivity extends AppCompatActivity implements Runnable{

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print);
        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
        //initViews();
        initListeners();
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


        tv_temp_one_company_name.setText(SharedPreferenceUtil.getUserName(InvoicePrintActivity.this));
        tv_temp_one_address.setText(SharedPreferenceUtil.getUserAddress(InvoicePrintActivity.this));
        if (invoiceId!=null){

            customerName = getIntent().getStringExtra("customerName");

             salesMaster= Common.salesMasterRepository.getSalesMaster(invoiceId);
            if (salesMaster!=null){
                 customer=Common.customerRepository.getCustomerss(customerName);
                if (customer!=null)
                {
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
                                mAdapters=new PrintSalesAdapter(InvoicePrintActivity.this,departments);
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
//                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                            if (mBluetoothAdapter == null) {
//                                Toast.makeText(InvoicePrintActivity.this, "Message1", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (!mBluetoothAdapter.isEnabled()) {
//                                    Intent enableBtIntent = new Intent(
//                                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                                    startActivityForResult(enableBtIntent,
//                                            REQUEST_ENABLE_BT);
//                                } else {
//                                    ListPairedDevices();
//                                    Intent connectIntent = new Intent(InvoicePrintActivity.this,
//                                            DeviceListActivity.class);
//                                    startActivityForResult(connectIntent,
//                                            REQUEST_CONNECT_DEVICE);
//                                }
//                            }

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

    }
//    private void ListPairedDevices() {
//        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
//                .getBondedDevices();
//        if (mPairedDevices.size() > 0) {
//            for (BluetoothDevice mDevice : mPairedDevices) {
//                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
//                        + mDevice.getAddress());
//            }
//        }
//    }

    private void initListeners() {
        if (printing!=null && printingCallback==null) {
            Log.d("xxx", "initListeners ");
            printingCallback = new PrintingCallback() {

                public void connectingWithPrinter() {
                    Toast.makeText(getApplicationContext(), "Connecting with printer", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "Connecting");
                }
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(getApplicationContext(), "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "printingOrderSentSuccessfully");

                    startActivity(new Intent(InvoicePrintActivity.this,MainActivity.class));
                    finish();
                }
                public void connectionFailed(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "connectionFailed :"+error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "connectionFailed : "+error);
                //    startActivity(new Intent(MainActivity.this,MainActivity.class));


                }
                public void onError(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "onError :"+error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "onError : "+error);

                }
                public void onMessage(@NonNull String message) {
                    Toast.makeText(getApplicationContext(), "onMessage :" +message, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onMessage : "+message);
                }
            };

            Printooth.INSTANCE.printer().setPrintingCallback(printingCallback);
        }
    }
    private void printSomePrintable() {
        Log.d("xxx", "printSomePrintable ");
        if (printing!=null)
            printing.print(getSomePrintables());
      //  getSomePrintables();
    }
    PrintWriter oStream;
    private ArrayList<Printable> getSomePrintables() {
        Thread t = new Thread() {
            public void run() {
                try {
//                    pdfTest = new PDFTest(MainActivity.this);
//                    byte[] s = pdfTest.getData();
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
                    c.restore();;
                    PrintPic printPic = PrintPic.getInstance();
                    printPic.init(b);
                    byte[] bitmapdata = printPic.printDraw();
                    os.write(bitmapdata);
                    // os.write(byteArray);
                    //This is printer specific code you can comment ==== > Start
                    // Setting height
                    int gs = 29;
//                    os.write(intToByteArray(gs));
//                    int h = 104;
//                    os.write(intToByteArray(h));
//                    int n = 162;
//                    os.write(intToByteArray(n));
//
//                    // Setting Width
//                    int gs_width = 29;
//                    os.write(intToByteArray(gs_width));
//                    int w = 119;
//                    os.write(intToByteArray(w));
//                    int n_width = 2;
//                    os.write(intToByteArray(n_width));
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



        ArrayList<Printable> al = new ArrayList<>();
        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode
        String BILL = "";

        BILL =    "                  "+ SharedPreferenceUtil.getUserName(InvoicePrintActivity.this) +" \n \n"+

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

        al.add( (new TextPrintable.Builder())
                .setText(BILL)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());


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
//

        al.add( (new TextPrintable.Builder())
                .setText(BILLS)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
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
    private void printSomePrintableChalan() {
        Log.d("xxx", "printSomePrintable ");
        if (printing!=null)
            printing.print(getSomePrintablesChalan());
        //  getSomePrintables();
    }

    private ArrayList<Printable> getSomePrintablesChalan() {


        ArrayList<Printable> al = new ArrayList<>();
        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode
        String BILL = "";

        BILL = "                  "+customer.ShopName+" \n "+
                "                   "+customer.Name+" \n "+
                "                   "+customer.MobileNumber+" \n "+
                "                   "+customer.Address+" \n "+
                "                   "+customer.RetailerCode+" \n "+
                "                   "+customer.StoreId+" \n "
                + "\n";
        BILL = BILL
                + "-----------------------------------------------\n";


        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "সংখ্যা");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";

        for (SalesDetailPrintModel salesDetailPrintModel: printModels){

            String quantity=getValue(String.valueOf(salesDetailPrintModel.Quantity));
            String rate=getValue(String.valueOf(salesDetailPrintModel.BookPrice));
            double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice);
            String totalPrice=getValue(String.valueOf(price));

            BILL = BILL  + String.format("%1$-10s %2$10s %3$11s %4$10s", salesDetailPrintModel.BookNameBangla, quantity);
        }


        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n ";


        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";
        BILL = BILL + "\n\n\n\n\n\n\n\n";
        al.add( (new TextPrintable.Builder())
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
    private String getValue(String value)
    {
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
        try {
           // trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent openIntent = new Intent(getApplicationContext(), MainActivity.class);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(openIntent);

    }

    @Override
    public void run() {
//        try {
//            mBluetoothSocket = mBluetoothDevice
//                    .createRfcommSocketToServiceRecord(applicationUUID);
//            mBluetoothAdapter.cancelDiscovery();
//            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
//        } catch (IOException eConnectException) {
//            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
//            closeSocket(mBluetoothSocket);
//            return;
//        }
    }

//    private void closeSocket(BluetoothSocket nOpenSocket) {
//        try {
//            nOpenSocket.close();
//            Log.d(TAG, "SocketClosed");
//        } catch (IOException ex) {
//            Log.d(TAG, "CouldNotCloseSocket");
//        }
//    }
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            mBluetoothConnectProgressDialog.dismiss();
//            Toast.makeText(InvoicePrintActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    public static byte intToByteArray(int value) {
//        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
//
//        for (int k = 0; k < b.length; k++) {
//            System.out.println("Selva  [" + k + "] = " + "0x"
//                    + UnicodeFormatter.byteToHex(b[k]));
//        }
//
//        return b[3];
//    }
//
//    public byte[] sel(int val) {
//        ByteBuffer buffer = ByteBuffer.allocate(2);
//        buffer.putInt(val);
//        buffer.flip();
//        return buffer.array();
//    }
//    public static void trimCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            if (dir != null && dir.isDirectory()) {
//                deleteDir(dir);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        // The directory is now empty so delete it
//        return dir.delete();
//    }
//public void onActivityResult(int mRequestCode, int mResultCode,
//                             Intent mDataIntent) {
//    super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
//
//    switch (mRequestCode) {
//        case REQUEST_CONNECT_DEVICE:
//            if (mResultCode == Activity.RESULT_OK) {
//                Bundle mExtra = mDataIntent.getExtras();
//                String mDeviceAddress = mExtra.getString("DeviceAddress");
//                Log.v(TAG, "Coming incoming address " + mDeviceAddress);
//                mBluetoothDevice = mBluetoothAdapter
//                        .getRemoteDevice(mDeviceAddress);
//                mBluetoothConnectProgressDialog = ProgressDialog.show(this,
//                        "Connecting...", mBluetoothDevice.getName() + " : "
//                                + mBluetoothDevice.getAddress(), true, false);
//                Thread mBlutoothConnectThread = new Thread(this);
//                mBlutoothConnectThread.start();
//                // pairToDevice(mBluetoothDevice); This method is replaced by
//                // progress dialog with thread
//            }
//            break;
//
//        case REQUEST_ENABLE_BT:
//            if (mResultCode == Activity.RESULT_OK) {
//                ListPairedDevices();
//                Intent connectIntent = new Intent(InvoicePrintActivity.this,
//                        DeviceListActivity.class);
//                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
//            } else {
//                Toast.makeText(InvoicePrintActivity.this, "Message", Toast.LENGTH_SHORT).show();
//            }
//            break;
//    }
//}
}

