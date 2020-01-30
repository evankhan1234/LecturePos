package xact.idea.lecturepos;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import java.text.SimpleDateFormat;
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
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.PDFTest;
import xact.idea.lecturepos.Utils.PrintPic;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.UnicodeFormatter;

import static xact.idea.lecturepos.Utils.Utils.getValue;
import static xact.idea.lecturepos.Utils.Utils.rounded;

public class TemporaryActivity extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button create_challan, c;
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
    TextView text_sub_total_value;
    RecyclerView rcl_approval_in_list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PrintSalesAdapter mAdapters;
    List<SalesDetailPrintModel> printModels;
    Customer customer;
    SalesMaster salesMaster;
    boolean test = false;

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_temporary);
        //   create_challan = (Button) findViewById(R.id.create_challan);


        fn_permission();
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));

        frag_to_TvInvoiceTitle = findViewById(R.id.frag_to_TvInvoiceTitle);
        text_payment = findViewById(R.id.text_payment);
        tv_temp_one_company_name = findViewById(R.id.tv_temp_one_company_name);
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


        tv_temp_one_company_name.setText(SharedPreferenceUtil.getUserName(TemporaryActivity.this));
        tv_temp_one_address.setText(SharedPreferenceUtil.getUserAddress(TemporaryActivity.this));
        if (invoiceId != null) {

            customerName = getIntent().getStringExtra("customerName");

            salesMaster = Common.salesMasterRepository.getSalesMaster(invoiceId);
            if (salesMaster != null) {
                customer = Common.customerRepository.getCustomerss(customerName);
                if (customer != null) {
                    Constant.rate = "rate1";
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
                    if (salesMaster.TrnType.equals("S")) {
                        frag_to_TvInvoiceTitle.setText("Invoice");
                    } else if (salesMaster.TrnType.equals("R")) {
                        frag_to_TvInvoiceTitle.setText("Return Invoice");

                    }

                    text_payment.setText("Payment Type: " + salesMaster.PayMode);

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
                            mAdapters = new PrintSalesAdapter(TemporaryActivity.this, departments);
                            rcl_approval_in_list.setAdapter(mAdapters);
                        }
                    }));
                } else {

                }
            } else {

            }
        } else {

        }
        create_challan = (Button) findViewById(R.id.create_challan);
        create_challan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (test) {
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

                                String datesss = getValue(formatter.format(salesMaster.InvoiceDate));
                                String inv = getValue(salesMaster.InvoiceNumber);
                                String mobile = getValue(customer.MobileNumber);

                                String strUserName = SharedPreferenceUtil.getUserNameBangla(TemporaryActivity.this);
                                String strUserAddress = SharedPreferenceUtil.getUserAddressBangla(TemporaryActivity.this);
                                String strUserNameEnglish;
                                if (strUserName == null || strUserName.equals("")) {

                                    strUserName = SharedPreferenceUtil.getUserName(TemporaryActivity.this);
                                    if (strUserName == null || strUserName.equals("")) {
                                        strUserName = "N/A";
                                    }

                                }
                                if (strUserAddress == null || strUserAddress.equals("")) {

                                    strUserAddress = SharedPreferenceUtil.getUserAddress(TemporaryActivity.this);
                                    if (strUserAddress == null || strUserAddress.equals("")) {
                                        strUserAddress = "N/A";
                                    }

                                }


                                BILL = "               " + strUserName + " \n" +
                                        "       " + strUserAddress + "  \n" +
                                        "    (লেকচার পাবলিকেশন লিমিটেড অনুমোদিত এজেন্ট)      \n \n" +
                                        "ইনভয়েস নং: " + inv + "\n" +
                                        "      তারিখ : " + datesss + "\n" +

                                        "    গ্রন্থাগার নাম: " + customer.ShopName + " \n" +
                                        "    গ্রাহক নাম: " + customer.Name + "(" + customer.RetailerCode + ")" + " \n" +
                                        "   মোবাইল নং: " + mobile + "\n" +
                                        "\n";
                                BILL = BILL
                                        + "---------------------------------------------------------------------------------\n";

                                BILL = BILL + getWidth("বইয়ের নাম", 1) + getWidth("সংখ্যা", 2) + getWidth("মূল্য", 3) + getWidth("মোট টাকা", 4) + "\n";

                                //  BILL = BILL + String.format("%-30s%-15s%-15s%-10s", "বইয়ের নাম", "সংখ্যা ", "মূল্য ", "মোট টাকা");
                                BILL = BILL + "\n";
                                BILL = BILL
                                        + "---------------------------------------------------------------------------------\n";

                                for (SalesDetailPrintModel salesDetailPrintModel : printModels) {

                                    Log.e("data", "datass" + salesDetailPrintModel.BookNameBangla);
                                    String value;

                                    String quantity = getValue(String.valueOf(salesDetailPrintModel.Quantity));
                                    String rate = getValue(String.valueOf(salesDetailPrintModel.BookPrice));
                                    double price = salesDetailPrintModel.Quantity * Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
                                    double ww = Double.parseDouble(salesDetailPrintModel.BookPrice) * (1 - Double.parseDouble(salesDetailPrintModel.Discount) / 100);
                                    String totalPrice = getValue(String.valueOf(rounded(price, 2)));
                                    if (totalPrice.equals("০.০")) {
                                        totalPrice = "  " + totalPrice;
                                    }

                                    String wws = getValue(String.valueOf(rounded(ww, 2)));
                                    if (wws.equals("০.০")) {
                                        // wws="   "+wws;

                                    } else {
                                        if (quantity.length() < 3) {
                                            quantity = "    " + quantity;
                                        } else if (quantity.length() < 2) {
                                            quantity = "     " + quantity;
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

                                        value = bookName.substring(0, 15);
                                        //float w = mPaint.measureText(value, 0, value.length());
                                        // Log.e("data1","datass1"+w);
                                        Log.e("data1", "datass1" + value.length());

                                    } else {
                                        value = salesDetailPrintModel.BookNameBangla;
                                    }
                                    BILL = BILL + getWidth(value, 1) + getWidth(quantity + "", 2) + getWidth(wws + "", 3) + getWidth(totalPrice + "", 4) + "\n";

                                    // BILL = BILL+ String.format("%-20s%-18s%-15s%-10s",getWidth(value,1), quantity, wws, totalPrice) + "\n";
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
                                BILL = BILL + "                  পরিশোধিত     :" + " " + salesMaster.PayMode + "" + "\n";

                                BILL = BILL
                                        + "---------------------------------------------------------------------------------\n";
                                BILL = BILL + "  আপনার সহযোগিতার জন্য ধন্যবাদ\n\n";
                                BILL = BILL + " Developed By                               Printed At\n";
                                BILL = BILL + "  www.xactidea.com                 " + currentDate + " " + currentTime + "\n";
                                BILL = BILL + "\n\n";
                                BILL = BILL + "\n\n";
                                BILL = BILL + "\n\n";
                                BILL = BILL + "\n\n";
                                BILL = BILL + "\n\n";

                                Drawable d = ContextCompat.getDrawable(TemporaryActivity.this, R.mipmap.ic_launcher);
//
                                TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                                        | Paint.LINEAR_TEXT_FLAG);
                                textPaint.setStyle(Paint.Style.FILL);
                                textPaint.setTextSize(25);

                                StaticLayout mTextLayout = new StaticLayout(BILL, textPaint,
                                        600, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

                                // Create bitmap and canvas to draw to
                                Bitmap b = Bitmap.createBitmap(600, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
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
                                PDFTest pdfTest = new PDFTest(TemporaryActivity.this, salesMaster, customer, printModels);
                                Bitmap v = pdfTest.getBitmap();
                                PrintPic printPic = PrintPic.getInstance();
                                printPic.init(v);
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
                                Log.e("TemporaryActivity", "Exe ", e);
                            }
                        }
                    };
                    t.start();
                } else {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(TemporaryActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {
                            ListPairedDevices();
                            Intent connectIntent = new Intent(TemporaryActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);
                        }
                    }

                }

            }
        });


    }// onCreate


    PrintWriter oStream;

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

        Intent openIntent = new Intent(getApplicationContext(), MainActivity.class);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(openIntent);
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
                    Intent connectIntent = new Intent(TemporaryActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(TemporaryActivity.this, "Message", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(TemporaryActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            test = true;
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

    int totalWidth = 0;
    int pos = 0;

    private String getWidth(String text, int type) {
        Paint textPaint = new Paint();
        String result = text;
        //  TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#ff00ff"));
        textPaint.setTextSize(20);
        Rect bounds = new Rect();
        int width = 0;
        int remain = 0;
        int remainder = 0;
        int toatlSpaces = 0;
        Log.e("totalWidth", pos + "  " + totalWidth);
        pos++;
        if (type == 1) {
            if (!result.startsWith(" ")) {
                result = spaces[4] + result;
            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            width = bounds.width() + 25;
            remain = 250 - width;
            remainder = remain / 5;
            totalWidth = width;
            if (remainder > 0) {
                result = result + spaces[remainder - 1];
                totalWidth = width + remainder * 5;
            }


        } else if (type == 2) {
            if ((250 - totalWidth) > 3) {
                result = spaces[3] + result;
                toatlSpaces = 4;
            } else if ((totalWidth - 250) > 3) {
                result = spaces[1] + result;
                toatlSpaces = 2;

            } else {

                result = spaces[2] + result;
                toatlSpaces = 3;


            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            //Log.e("width->2",""+width);
            width = bounds.width() + toatlSpaces * 5;
            totalWidth = totalWidth + width;
            remain = 350 - totalWidth;
            remainder = remain / 5;
            if (remainder > 0) {
                result = result + spaces[remainder - 1];
                totalWidth = totalWidth + remainder * 5;

            }


        } else if (type == 3) {

            if ((350 - totalWidth) > 3) {
                result = spaces[3] + result;
                toatlSpaces = 4;
            } else if ((totalWidth - 350) > 3) {
                result = spaces[1] + result;
                toatlSpaces = 2;

            } else {

                result = spaces[2] + result;
                toatlSpaces = 3;

            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);

            width = bounds.width() + toatlSpaces * 5;

            totalWidth = totalWidth + width;
            remain = 450 - totalWidth;
            remainder = remain / 5;
            if (remainder > 0) {
                result = result + spaces[remainder - 1];
                totalWidth = totalWidth + remainder * 5;

            }

        } else {
            if ((450 - totalWidth) > 3) {
                result = spaces[3] + result;
                toatlSpaces = 4;
            } else if ((totalWidth - 450) > 3) {
                result = spaces[1] + result;
                toatlSpaces = 2;

            } else {

                result = spaces[2] + result;
                toatlSpaces = 3;

            }
            bounds = new Rect();
            textPaint.getTextBounds(result, 0, result.length(), bounds);
            width = bounds.width() + toatlSpaces * 5;
            totalWidth = totalWidth + width;
            remain = 550 - totalWidth;

            remainder = remain / 5;
            if (remainder > 0) {
                result = result + spaces[remainder - 1];
                totalWidth = totalWidth + width;
            }


        }


        return result;
    }

    String spaces[] = {" ",
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
    boolean boolean_permission = false;
    public static int REQUEST_PERMISSIONS = 2;

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(TemporaryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(TemporaryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(TemporaryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(TemporaryActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(TemporaryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(TemporaryActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
}

