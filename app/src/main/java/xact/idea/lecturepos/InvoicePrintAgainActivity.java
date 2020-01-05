package xact.idea.lecturepos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

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
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class InvoicePrintAgainActivity  extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print_again);
        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
        //initViews();
        initListeners();

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
                        @Override
                        public void onClick(View v) {

                            if (!Printooth.INSTANCE.hasPairedPrinter())
                                startActivityForResult(new Intent(InvoicePrintAgainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                            else
                                printSomePrintable();

                        }
                    });
                } else {

                }
            } else {

            }
        } else {

        }

    }

    private void initListeners() {
        if (printing != null && printingCallback == null) {
            Log.d("xxx", "initListeners ");
            printingCallback = new PrintingCallback() {

                public void connectingWithPrinter() {
                    Toast.makeText(getApplicationContext(), "Connecting with printer", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "Connecting");
                }

                public void printingOrderSentSuccessfully() {
                    Toast.makeText(getApplicationContext(), "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "printingOrderSentSuccessfully");

                    startActivity(new Intent(InvoicePrintAgainActivity.this, MainActivity.class));
                    finish();
                }

                public void connectionFailed(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "connectionFailed :" + error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "connectionFailed : " + error);
                    //    startActivity(new Intent(MainActivity.this,MainActivity.class));


                }

                public void onError(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "onError :" + error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "onError : " + error);

                }

                public void onMessage(@NonNull String message) {
                    Toast.makeText(getApplicationContext(), "onMessage :" + message, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onMessage : " + message);
                }
            };

            Printooth.INSTANCE.printer().setPrintingCallback(printingCallback);
        }
    }

    private void printSomePrintable() {
        Log.d("xxx", "printSomePrintable ");
        if (printing != null)
            printing.print(getSomePrintables());
        //  getSomePrintables();
    }

    private ArrayList<Printable> getSomePrintables() {


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

        al.add((new TextPrintable.Builder())
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
            finish();
        }



     //   super.onBackPressed();
    }
}