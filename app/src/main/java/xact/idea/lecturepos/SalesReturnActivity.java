package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.ItemReturnAdapter;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.ItemReturn;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Interface.CustomerInterface;
import xact.idea.lecturepos.Model.StockModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.SpinnerDialogCustomer;
import xact.idea.lecturepos.Utils.SpinnerDialogFor;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class SalesReturnActivity extends AppCompatActivity {

    Button create;
    Button btn_scan;
    Button btn_input;
    Button save;
    Button saves;
    EditText editShippingChargesValue;
    static EditText edit_retail_code;
    static EditText edit_contact_number;
    EditText edit_note;
    static EditText edit_address;
    EditText editReturn;
    static EditText edit_date;
    TextView text_sub_total;
    TextView text_net_amounts;
    static TextView text_customer_spinner;
    ItemReturnAdapter mAdapters;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView rcl_this_customer_list;
    ImageView btn_header_back;
    private List<Customer> customerArrayList = new ArrayList<>();
    private ArrayList<String> ArrayList = new ArrayList<>();
    private ArrayList<String> ArrayList1 = new ArrayList<>();
    private ArrayList<String> bookArrayList = new ArrayList<>();
    private ArrayList<String> bookArrayListAgain = new ArrayList<>();
    ArrayAdapter<Customer> customerListEntityArrayAdapter;
    ArrayAdapter<String> bookListEntityArrayAdapter;
    Spinner spinner_customer;
    String Name;
    RadioButton radioCash;
    RadioButton radioCredit;
    RadioGroup radioLogin;
    String sessionId;
    String dataId;
    SpinnerDialogCustomer spinnerDialog;
    SpinnerDialogFor spinnerDialogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        editReturn = findViewById(R.id.editReturn);
        radioLogin = findViewById(R.id.radioLogin);
        text_customer_spinner = findViewById(R.id.text_customer_spinner);
        radioCash = findViewById(R.id.radioCash);
        btn_input = findViewById(R.id.btn_input);
        radioCredit = findViewById(R.id.radioCredit);
        edit_date = findViewById(R.id.edit_date);
        save = findViewById(R.id.save);
        saves = findViewById(R.id.saves);
        btn_scan = findViewById(R.id.btn_scan);
        edit_retail_code = findViewById(R.id.edit_retail_code);
        spinner_customer = findViewById(R.id.spinner_customer);
        edit_contact_number = findViewById(R.id.edit_contact_number);
        edit_note = findViewById(R.id.edit_note);
        edit_address = findViewById(R.id.edit_address);
        editShippingChargesValue = findViewById(R.id.editShippingChargesValue);
        text_sub_total = findViewById(R.id.text_sub_total);
        text_net_amounts = findViewById(R.id.text_net_amounts);
        create = findViewById(R.id.create);
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);

        btn_header_back = findViewById(R.id.btn_header_back);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        final CustomerInterface customerInterface = new CustomerInterface() {
            @Override
            public void onClick(String item, int position) {


                Customer customer = Common.customerRepository.getCustomePhoe(item.substring(item.length() - 11));
                if (customer != null) {
                    text_customer_spinner.setText(customer.ShopName);
                    Name = customer.ShopName;
                    Constant.name = customer.ShopName;
                    edit_address.setText(customer.Address);
                    edit_contact_number.setText(customer.MobileNumber);
                    edit_retail_code.setText(customer.RetailerCode);

                } else {

                }
                spinnerDialog.closeSpinerDialog();
            }
        };
        text_customer_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog = new SpinnerDialogCustomer(SalesReturnActivity.this, customerArrayList, ArrayList, "Select Customer", customerInterface);

                spinnerDialog.showSpinerDialog();


            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalesReturnActivity.this, BarcodeReturnActivity.class));
                finish();
            }
        });
        compositeDisposable.add(Common.bookStockRepository.getBookStockModelReturenAdjustment().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<StockModel>>() {
            @Override
            public void accept(List<StockModel> books) throws Exception {
                for (StockModel book : books) {
                    String data= Utils.getValue("12"+book.F_BOOK_EDITION_NO);
                    bookArrayList.add(  book.BookName + " \n "+ book.BookNameBangla + " \n "+book.BOOK_SPECIMEN_CODE + "(20"+book.F_BOOK_EDITION_NO+") "+"("+data+")"+book.BARCODE_NUMBER );
                    //  bookArrayList.add(book.BOOK_SPECIMEN_CODE+" "+book.BookName+" "+book.BARCODE_NUMBER);
                    //ArrayList.add(customer.ShopName);
                }

                //  dismissLoadingProgress();
            }
        }));
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//


                spinnerDialogs = new SpinnerDialogFor(SalesReturnActivity.this, bookArrayList, "Select Book","R","");
                spinnerDialogs.showSpinerDialog();
                //showInfoDialog();
            }
        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReturnActivity.this, MainActivity.class));
                finish();
            }
        });
///        Common.bookRepository.getBookItems();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog();
            }
        });
        sessionId = getIntent().getStringExtra("value");
        dataId = getIntent().getStringExtra("data");
        //customerListData();
        compositeDisposable.add(Common.customerRepository.getCustomerItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Customer>>() {
            @Override
            public void accept(List<Customer> units) throws Exception {
                customerArrayList = units;

                for (Customer customer : units) {
                    ArrayList.add(customer.ShopName + " " + customer.Name + " " + customer.MobileNumber);
                }

                //  dismissLoadingProgress();
            }
        }));
        if (sessionId == null) {
            //   showInfoDialog();
        } else if (sessionId.equals("value")) {
            compositeDisposable.add(Common.bookStockRepository.getBookStockModelReturenAdjustment().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<StockModel>>() {
                @Override
                public void accept(List<StockModel> books) throws Exception {
                    for (StockModel book : books) {
                        String data= Utils.getValue("12"+book.F_BOOK_EDITION_NO);
                        bookArrayListAgain.add(  book.BookName + " \n "+ book.BookNameBangla + " \n "+book.BOOK_SPECIMEN_CODE + "(20"+book.F_BOOK_EDITION_NO+") "+"("+data+")"+book.BARCODE_NUMBER );                        //  bookArrayList.add(book.BOOK_SPECIMEN_CODE+" "+book.BookName+" "+book.BARCODE_NUMBER);
                        //ArrayList.add(customer.ShopName);
                    }

                    //  dismissLoadingProgress();
                }
            }));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    spinnerDialogs = new SpinnerDialogFor(SalesReturnActivity.this, bookArrayListAgain, "Select Book","R","");
                    spinnerDialogs.showSpinerDialog();
                }
            }, 300);

        } else if (sessionId.equals("valueNew")){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SalesReturnActivity.this, BarcodeReturnActivity.class));
                    finish();
                }
            }, 300);
        }
        if (dataId == null) {
            //   showInfoDialog();
        } else {
            // showInfoDialog();
            text_customer_spinner.setText(dataId);
            Name = Constant.name;
            Customer customer = Common.customerRepository.getCustomerss(dataId);
            if (customer != null) {

                edit_address.setText(customer.Address);
                edit_contact_number.setText(customer.MobileNumber);
                edit_retail_code.setText(customer.RetailerCode);

            } else {

            }
        }

        if (Constant.name.equals("")) {

        } else {
            text_customer_spinner.setText(Constant.name);
            Name = Constant.name;
            Customer customer = Common.customerRepository.getCustomerss(Constant.name);
            if (customer != null) {

                edit_address.setText(customer.Address);
                edit_contact_number.setText(customer.MobileNumber);
                edit_retail_code.setText(customer.RetailerCode);

            } else {

            }
        }


        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new SalesReturnActivity.DatePickerFromFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final Date date = new Date(System.currentTimeMillis());
        edit_date.setText(formatter.format(date));

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String amounts = text_sub_total.getText().toString();
                amounts = amounts.replace(" Tk", "");
                //  Toast.makeText(SalesReturnActivity.this, amount, Toast.LENGTH_LONG).show();
                if (!text_customer_spinner.getText().toString().equals("Select")){
                    if (Common.itemReturnRepository.size()>0) {

                        if (stock>0){
                            Toast.makeText(SalesReturnActivity.this, "Out of stock ItemReturn", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            String s = text_net_amounts.getText().toString();
                            s = s.replace(" Tk", "");
                            // find the radiobutton by returned id
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());
                            double discount = 0;
                            double amount = 0;
                            amount = Double.parseDouble(s);
                            try {
                                discount = Double.parseDouble(editShippingChargesValue.getText().toString());


                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            SalesMaster salesMaster = new SalesMaster();
                            Date datess = new Date(System.currentTimeMillis());
                            final SimpleDateFormat formatterq = new SimpleDateFormat("dd-MM-yyyy");
                            final Date dateq = new Date(System.currentTimeMillis());
                            //     String currentDate =formatterq.format(dateq);

                            int value = Common.salesMasterRepository.maxValue(formatterq.format(dateq),"R");
                            String totalValue;
                            value = value + 1;
                            if (value < 9) {
                                totalValue = "00" + value;

                            } else if (value > 9) {
                                totalValue = "0" + value;
                            } else {
                                totalValue = String.valueOf(value);
                            }
                            String store = null;
                            if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<6){
                                store="0"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);
                            }
                            else if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<5){
                                store="00"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);

                            }
                            else if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<4){
                                store="000"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);

                            }
                            else if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<3){
                                store="0000"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);

                            }
                            else if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<2){
                                store="00000"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);

                            }
                            else if (SharedPreferenceUtil.getUserID(SalesReturnActivity.this).length()<1){
                                store="000000"+SharedPreferenceUtil.getUserID(SalesReturnActivity.this);

                            }
                            Date date1 = null;
                            Date date2 = null;
                            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
                            Date date = new Date(System.currentTimeMillis());
                            try {
                                date1 = new SimpleDateFormat("dd-MM-yyyy").parse(edit_date.getText().toString());
                                // date2= new SimpleDateFormat("yy-MM-dd").parse(edit_date.getText().toString());


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Customer customer =Common.customerRepository.getCustomerss(Name);
                            salesMaster.CustomerName = customer.ShopName;
                            salesMaster.Discount = discount;
                            salesMaster.TrnType = "R";
                            salesMaster.InvoiceId = "12" + store + formatter.format(date) + totalValue;
                            salesMaster.StoreId = SharedPreferenceUtil.getUserID(SalesReturnActivity.this);
                            salesMaster.InvoiceNumber = "12" + store + formatter.format(date) + totalValue;
                            SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss a");
                            Date dates1 = new Date(System.currentTimeMillis());
                            String currentTime = formatters.format(dates1);
                            salesMaster.InvoiceDates = edit_date.getText().toString() + " " + currentTime;
                            salesMaster.DateSimple = edit_date.getText().toString();
                            salesMaster.InvoiceDate = date1;
                            //salesMaster.Discount = discount;

                            salesMaster.NetValue = amount;
                            String str = android.os.Build.MODEL;
                            String str1 = Build.DEVICE;
                            salesMaster.Device = str1 + " " + str;
                            salesMaster.update_date = date1;
                            salesMaster.PayMode = pay;
                            String s1 = text_sub_total.getText().toString();
                            s1 = s1.replace(" Tk", "");
                            salesMaster.SubTotal = s1;
                            String s2 = text_net_amounts.getText().toString();
                            s2= s2.replace(" Tk", "");
                            salesMaster.UpdateNo = 0;
                            salesMaster.InvoiceAmount = Double.parseDouble(s1);
                            Date dates = new Date(System.currentTimeMillis());
                            salesMaster.Date = dates;
                            salesMaster.Note = edit_note.getText().toString();
                            salesMaster.RetailCode = edit_retail_code.getText().toString();
                            salesMaster.PhoneNumber = edit_contact_number.getText().toString();
                            salesMaster.Return = editReturn.getText().toString();
                            Common.salesMasterRepository.insertToSalesMaster(salesMaster);
                            Flowable<List<ItemReturn>> units = Common.itemReturnRepository.getItemItems();

                            for (ItemReturn itemModel : units.blockingFirst())
                            {
                                Date datess1 = new Date(System.currentTimeMillis());
                                int values = Common.salesMasterRepository.maxValue(formatterq.format(dateq),"R");
                                SalesDetails salesDetails = new SalesDetails();
                                salesDetails.BookId = itemModel.BookId;
                                salesDetails.BookName = itemModel.BookName;
                                salesDetails.Discount = itemModel.Discount;
                                salesDetails.MRP = itemModel.Price;
                                salesDetails.UpdateNo = 0;
                                salesDetails.InvoiceDate = date1;
                                salesDetails.Quantity = itemModel.Quantity;
                                salesDetails.TotalAmount = itemModel.Amount;
                                SalesMaster salesMasters = Common.salesMasterRepository.invoice(values);
                                salesDetails.InvoiceId = values;
                                salesDetails.InvoiceIdNew = "12" + store + formatter.format(date) + totalValue;
                                salesDetails.StoreId = SharedPreferenceUtil.getUserID(SalesReturnActivity.this);
                                Common.salesDetailsRepository.insertToSalesDetails(salesDetails);


                                BookStock bookStocks = Common.bookStockRepository.getBookStock(itemModel.BookId);
//                        BookStock bookStock = new BookStock();
//                        bookStock.id=bookStocks.id;
//                        bookStock.QTY_NUMBER=bookStocks.QTY_NUMBER-itemModel.Quantity;
//                        Common.bookStockRepository.updateBookStock(bookStock);
                                double t =bookStocks.BOOK_NET_MRP*itemModel.Quantity;
                                double pr=bookStocks.BOOK_NET_PRICES+t;
                                Common.bookStockRepository.updateReciverQuantity(bookStocks.QTY_NUMBER +itemModel.Quantity,pr, bookStocks.BOOK_ID);

                            }
                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                            Date date11 = new Date(System.currentTimeMillis());
                            String currentDate = formatter1.format(date11);
                            SimpleDateFormat formatters1 = new SimpleDateFormat("hh:mm:ss a");
                            Date dates11 = new Date(System.currentTimeMillis());
                            String currentTime1 = formatters1.format(dates11);
                            SharedPreferenceUtil.saveShared(SalesReturnActivity.this, SharedPreferenceUtil.USER_TEST ,"12" + store + formatter.format(date) + totalValue);

                            SharedPreferenceUtil.saveShared(SalesReturnActivity.this, SharedPreferenceUtil.USER_SYNC, "green");
                            SharedPreferenceUtil.saveShared(SalesReturnActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime1 + "");

                            Constant.arrayList.clear();
                            Constant.name="";
                            Constant.rate="rate12";
                            Common.itemReturnRepository.emptyItem();
                            Intent intent = new Intent(SalesReturnActivity.this, TemporaryActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.putExtra("customerName", Name);
                            intent.putExtra("invoiceId", "12" + store + formatter.format(date) + totalValue);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SalesReturnActivity.this, "Successfully Created a Return Invoice ", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(SalesReturnActivity.this, "Please Add ItemReturn", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SalesReturnActivity.this, "Please Select Customer", Toast.LENGTH_SHORT).show();
                }

            }
        });

        editReturn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double prices;
                double d = 0.0;
                String amount = text_sub_total.getText().toString();
                amount = amount.replace(" Tk", "");
                //  Toast.makeText(SalesReturnActivity.this, amount, Toast.LENGTH_LONG).show();
                if (amount.equals("0.0")) {
                    Toast.makeText(SalesReturnActivity.this, "Sub Total value is 0", Toast.LENGTH_SHORT).show();
                } else {
                    double t1;
                    String s1 = text_sub_total.getText().toString();
                    s1 = s1.replace(" Tk", "");
                    prices = Double.parseDouble(s1);
                    if (!editShippingChargesValue.getText().toString().equals("")) {
                        d = Double.parseDouble(editShippingChargesValue.getText().toString());
                    } else {
                        d = 0.0;
                    }
                    double total = (prices);
                    double totals = (prices) * d / 100;
                    double t = total - totals;

                    double ts = 0;
                    try {
                        ts = Double.parseDouble(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ts = 0;

                    }
                    if (t >= ts) {
                        t1 = t - ts;
                    } else {
                        t1 = 0;
                        Toast.makeText(SalesReturnActivity.this, "Return can not grater than Sub Total Value", Toast.LENGTH_SHORT).show();
                    }

                    //    double t1 = t-Double.parseDouble(s.toString());

                    text_net_amounts.setText(String.valueOf(rounded(t1, 2) + " Tk"));
                }

            }
        });
        editShippingChargesValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                double prices;
                double d = 0.0;
                String amount = text_sub_total.getText().toString();
                amount = amount.replace(" Tk", "");
                //  Toast.makeText(SalesReturnActivity.this, amount, Toast.LENGTH_LONG).show();
                if (amount.equals("0.0")) {
                    Toast.makeText(SalesReturnActivity.this, "Sub Total value is 0", Toast.LENGTH_SHORT).show();
                } else {

                    String s1 = text_sub_total.getText().toString();
                    s1 = s1.replace(" Tk", "");
                    prices = Double.parseDouble(s1);
                    if (!s.toString().equals("")) {
                        d = Double.parseDouble(s.toString());
                    } else {
                        d = 0.0;
                    }
                    double total = (prices);
                    double totals = (prices) * d / 100;
                    double t = total - totals;

                    double ts = 0;
                    try {
                        ts = Double.parseDouble(editReturn.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ts = 0;
                    }
                    double t1 = t - ts;
                    text_net_amounts.setText(String.valueOf(rounded(t1, 2) + " Tk"));
                }


                // amount.setText(String.valueOf(rounded(total,2)));
            }
        });
        // Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(input);
    }

    public static class DatePickerFromFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                calendar.setTime(sdf.parse(edit_date.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);

            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = formatter.format(chosenDate);
            EditText startTime2 = (EditText) getActivity().findViewById(R.id.edit_date);
            startTime2.setText(formattedDate);

        }
    }

    //    public static void  data(String item){
//        text_customer_spinner.setText(item);
//        Name=item;
//        Constant.name=item;
//
//        Customer customer=Common.customerRepository.getCustomerss(item);
//        if (customer!=null){
//
//            edit_address.setText(customer.Address);
//            edit_contact_number.setText(customer.MobileNumber);
//            edit_retail_code.setText(customer.RetailerCode);
//
//        }
//        else {
//
//        }
//    }
    double stock = 0;;
    @Override
    protected void onResume() {
        super.onResume();
        loadBookItemsFor();
        masterListData();
        detailsListData();


        double total = 0;

        double discount = 0;
        double amount = 0;
//        for (ItemModel itemModel : Constant.arrayList) {
//            total += itemModel.Amount;
//        }
//        compositeDisposable.add(Common.itemReturnRepository.getItemItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<ItemReturn>>() {
//            @Override
//            public void accept(List<ItemReturn> departments) throws Exception {
//                for (ItemReturn itemModel : departments) {
//                    total += itemModel.Amount;
//                }
//            }
//        }));
        total = Common.itemReturnRepository.valueSum();
        stock = Common.itemReturnRepository.wrongItem("In");
        text_sub_total.setText(String.valueOf(total) + " Tk");
        if (!editShippingChargesValue.getText().toString().equals("")) {
            discount = Double.parseDouble(editShippingChargesValue.getText().toString());
            amount = total - discount;
            text_net_amounts.setText(String.valueOf(amount) + " Tk");

        } else {
            discount = 0;
            amount = total - discount;
            text_net_amounts.setText(String.valueOf(amount) + " Tk");
        }
    }

    public void fixed() {
        double total = 0;
        double discount = 0;
        double amount = 0;
        total = Common.itemReturnRepository.valueSum();
        text_sub_total.setText(String.valueOf(total) + " Tk");
        if (!editShippingChargesValue.getText().toString().equals("")) {
            discount = Double.parseDouble(editShippingChargesValue.getText().toString());
            amount = total - discount;
            text_net_amounts.setText(String.valueOf(amount) + " Tk");

        } else {
            discount = 0;
            amount = total - discount;
            text_net_amounts.setText(String.valueOf(amount) + " Tk");
        }
    }

    private void loadBookItemsFor() {

        compositeDisposable.add(Common.itemReturnRepository.getItemItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<ItemReturn>>() {
            @Override
            public void accept(List<ItemReturn> ItemReturn) throws Exception {
                Log.e("Book", "Book" + new Gson().toJson(ItemReturn));
                loadCustomer(ItemReturn);
            }
        }));

    }

    private void loadCustomer(List<ItemReturn> ItemReturn) {

        // List<ItemModel> arrayList= new ArrayList(Constant.map.values());

        mAdapters = new ItemReturnAdapter(this, ItemReturn);

        rcl_this_customer_list.setAdapter(mAdapters);

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


    private void customerListData() {
        //  showLoadingProgress(mActivity);
        compositeDisposable.add(Common.customerRepository.getCustomerItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Customer>>() {
            @Override
            public void accept(List<Customer> units) throws Exception {
                displayUnitItems(units);
                //  dismissLoadingProgress();
            }
        }));

    }


    private void masterListData() {
        //  showLoadingProgress(mActivity);
        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> units) throws Exception {
                Log.e("data", "data" + new Gson().toJson(units));
                //  Log.e("data","data"+new Gson().toJson(Common.salesDetailsRepository.getSalesDetailsItems()));
                //  dismissLoadingProgress();
            }
        }));

    }

    private void detailsListData() {
        //  showLoadingProgress(mActivity);
        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
            @Override
            public void accept(List<SalesDetails> units) throws Exception {
                Log.e("data", "data" + new Gson().toJson(units));
                // Log.e("data","data"+new Gson().toJson(Common.salesDetailsRepository.getSalesDetailsItems()));
                //  dismissLoadingProgress();
            }
        }));

    }

    private void displayUnitItems(List<Customer> customers) {
        for (Customer customer : customers) {
            ArrayList.add(customer.ShopName + " " + customer.Name + " " + customer.MobileNumber);
            //ArrayList.add(customer.ShopName);
            ArrayList1.add(customer.ShopName + "(" + customer.Name + ")");
        }
        customerArrayList = customers;
        customerListEntityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerArrayList);
        customerListEntityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //      spinner_customer.setAdapter(customerListEntityArrayAdapter);
    }


    public void showInfoDialog() {

        final CustomDialog infoDialog = new CustomDialog(this, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_pop_up_scan, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        Button btn_scan = infoDialog.findViewById(R.id.btn_scan);
        final EditText edit_book_code = infoDialog.findViewById(R.id.edit_book_code);
        final Button btn_input = infoDialog.findViewById(R.id.btn_input);
        final Button btn_done = infoDialog.findViewById(R.id.btn_done);
        final TextView text_customer_spinner = infoDialog.findViewById(R.id.text_customer_spinner);
        //   final  Button btn_done = infoDialog.findViewById(R.id.btn_done);

        CorrectSizeUtil.getInstance(this).correctSize(main_root);
        final SpinnerDialogFor spinnerDialogs;

        spinnerDialogs = new SpinnerDialogFor(SalesReturnActivity.this, bookArrayList, "Select Book","R","");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                text_customer_spinner.setText(item);
                Log.e("hgsc", "vcc" + item.substring(item.length() - 16));
                Book book = Common.bookRepository.getBook(item.substring(item.length() - 16));


                if (book != null) {
                    Intent intent = new Intent(SalesReturnActivity.this, ItemReturnActivity.class);
                    intent.putExtra("EXTRA_SESSION", book.BARCODE_NUMBER);
                    startActivity(intent);
                    finish();
                    infoDialog.dismiss();
                } else {
                    Toast.makeText(SalesReturnActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        text_customer_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs.showSpinerDialog();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_book_code.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                startActivity(new Intent(SalesReturnActivity.this, BarcodeReturnActivity.class));
                finish();
                infoDialog.dismiss();

            }
        });
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_book_code.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.VISIBLE);

                // infoDialog.dismiss();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = Common.bookRepository.getBook(edit_book_code.getText().toString());


                if (book != null) {
                    Intent intent = new Intent(SalesReturnActivity.this, ItemActivity.class);
                    intent.putExtra("EXTRA_SESSION", edit_book_code.getText().toString());
                    startActivity(intent);
                    finish();
                    infoDialog.dismiss();
                } else {
                    Toast.makeText(SalesReturnActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        infoDialog.show();
    }

}

