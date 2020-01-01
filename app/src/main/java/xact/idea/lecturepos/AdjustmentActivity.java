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
import xact.idea.lecturepos.Adapter.ItemAdapter;
import xact.idea.lecturepos.Adapter.ItemAdjustmentAdapter;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;
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

public class AdjustmentActivity extends AppCompatActivity {


    Button btn_scan;
    Button btn_input;
    Button save;
    Button saves;

    EditText edit_note;


    static EditText edit_date;


    ItemAdjustmentAdapter mAdapters;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView rcl_this_customer_list;
    ImageView btn_header_back;

    private ArrayList<String> ArrayList = new ArrayList<>();
    private ArrayList<String> ArrayList1 = new ArrayList<>();
    private ArrayList<String> bookArrayList = new ArrayList<>();
    private ArrayList<String> bookArrayListAgain = new ArrayList<>();
    String Name;
    String sessionId;
    String dataId;
    SpinnerDialogCustomer spinnerDialog;
    SpinnerDialogFor spinnerDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));

        btn_input = findViewById(R.id.btn_input);

        edit_date = findViewById(R.id.edit_date);
        save = findViewById(R.id.save);
        saves = findViewById(R.id.saves);
        btn_scan = findViewById(R.id.btn_scan);
        edit_note = findViewById(R.id.edit_note);

    //    create = findViewById(R.id.create);
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);

        btn_header_back = findViewById(R.id.btn_header_back);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdjustmentActivity.this, BarcodeAdjustmentActivity.class));
                finish();
            }
        });
        compositeDisposable.add(Common.bookStockRepository.getBookStockModel().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<StockModel>>() {
            @Override
            public void accept(List<StockModel> books) throws Exception {
                for (StockModel book : books) {
                    String data = Utils.getValue("20" + book.F_BOOK_EDITION_NO);
                    bookArrayList.add(book.BookName + " \n " + book.BookNameBangla + " \n " + book.BOOK_SPECIMEN_CODE + "(20" + book.F_BOOK_EDITION_NO + ") " + "(" + data + ")" + book.BARCODE_NUMBER);
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


                spinnerDialogs = new SpinnerDialogFor(AdjustmentActivity.this, bookArrayList, "Select Book", "A");
                spinnerDialogs.showSpinerDialog();
                //showInfoDialog();
            }
        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdjustmentActivity.this, MainActivity.class));
                finish();
            }
        });
///        Common.bookRepository.getBookItemAdjustment();

        sessionId = getIntent().getStringExtra("value");
        dataId = getIntent().getStringExtra("data");
        //customerListData();

        if (sessionId == null) {
            //   showInfoDialog();
        } else if (sessionId.equals("value")) {
            compositeDisposable.add(Common.bookStockRepository.getBookStockModel().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<StockModel>>() {
                @Override
                public void accept(List<StockModel> books) throws Exception {
                    for (StockModel book : books) {
                        String data = Utils.getValue("20" + book.F_BOOK_EDITION_NO);
                        bookArrayListAgain.add(book.BookName + " \n " + book.BookNameBangla + " \n " + book.BOOK_SPECIMEN_CODE + "(20" + book.F_BOOK_EDITION_NO + ") " + "(" + data + ")" + book.BARCODE_NUMBER);                        //  bookArrayList.add(book.BOOK_SPECIMEN_CODE+" "+book.BookName+" "+book.BARCODE_NUMBER);
                        //ArrayList.add(customer.ShopName);
                    }

                    //  dismissLoadingProgress();
                }
            }));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    spinnerDialogs = new SpinnerDialogFor(AdjustmentActivity.this, bookArrayListAgain, "Select Book", "A");
                    spinnerDialogs.showSpinerDialog();
                }
            }, 300);

        } else {

        }


        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new AdjustmentActivity.DatePickerFromFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final Date date = new Date(System.currentTimeMillis());
        edit_date.setText(formatter.format(date));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SalesMaster salesMaster = new SalesMaster();
                Date datess = new Date(System.currentTimeMillis());
                final SimpleDateFormat formatterq = new SimpleDateFormat("dd-MM-yyyy");
                final Date dateq = new Date(System.currentTimeMillis());

                int value = Common.salesMasterRepository.maxValue(formatterq.format(dateq), "A");
                String totalValue;
                value = value + 1;
                if (value < 9) {
                    totalValue = "00" + value;

                } else if (value > 9) {
                    totalValue = "0" + value;
                } else {
                    totalValue = String.valueOf(value);
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

             //   Customer customer = Common.customerRepository.getCustomerss(Name);
                salesMaster.CustomerName = "";
                salesMaster.Discount = 0;
                salesMaster.TrnType = "A";
                salesMaster.InvoiceId = "130" + SharedPreferenceUtil.getUserID(AdjustmentActivity.this) + formatter.format(date) + totalValue;
                salesMaster.StoreId = SharedPreferenceUtil.getUserID(AdjustmentActivity.this);
                salesMaster.InvoiceNumber = "130" + SharedPreferenceUtil.getUserID(AdjustmentActivity.this) + formatter.format(date) + totalValue;
                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss  a");
                Date dates1 = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates1);
                salesMaster.InvoiceDates = edit_date.getText().toString() + " " + currentTime;
                salesMaster.DateSimple = edit_date.getText().toString();
                salesMaster.InvoiceDate = date1;
                //salesMaster.Discount = discount;

                salesMaster.NetValue = 0;
                String str = android.os.Build.MODEL;
                String str1 = Build.DEVICE;
                salesMaster.Device = str1 + " " + str;
                salesMaster.update_date = date1;
                salesMaster.PayMode = " ";
                salesMaster.SubTotal = " ";
                salesMaster.InvoiceAmount = 0;
                Date dates = new Date(System.currentTimeMillis());
                salesMaster.Date = dates;
                salesMaster.Note = edit_note.getText().toString();
                salesMaster.RetailCode = " ";
                salesMaster.PhoneNumber = " ";
                salesMaster.Return = " ";
                Common.salesMasterRepository.insertToSalesMaster(salesMaster);
                Flowable<List<ItemAdjustment>> units = Common.itemAdjustmentRepository.getItemItems();

                for (ItemAdjustment itemModel : units.blockingFirst()) {

                    BookStock bookStocks = Common.bookStockRepository.getBookStock(itemModel.BookId);

                    if (itemModel.InOut.equals("In")) {
                        Common.bookStockRepository.updateReciver(bookStocks.QTY_NUMBER + itemModel.Quantity, bookStocks.BOOK_ID);

                    } else {
                        Common.bookStockRepository.updateReciver(bookStocks.QTY_NUMBER - itemModel.Quantity, bookStocks.BOOK_ID);

                    }

                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date11 = new Date(System.currentTimeMillis());
                String currentDate = formatter1.format(date11);
                SimpleDateFormat formatters1 = new SimpleDateFormat("hh:mm:ss");
                Date dates11 = new Date(System.currentTimeMillis());
                String currentTime1 = formatters1.format(dates11);
                SharedPreferenceUtil.saveShared(AdjustmentActivity.this, SharedPreferenceUtil.USER_SYNC, "green");
                SharedPreferenceUtil.saveShared(AdjustmentActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime1 + "");

                Constant.arrayList.clear();
                Constant.name = "";
                Constant.rate = "ratejk";
                Common.itemAdjustmentRepository.emptyItem();
                Intent intent = new Intent(AdjustmentActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(AdjustmentActivity.this, "Successfully Created a Adjustment ", Toast.LENGTH_SHORT).show();
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


    double stock = 0;

    @Override
    protected void onResume() {
        super.onResume();
        loadBookItemAdjustmentFor();


    }

    private void loadBookItemAdjustmentFor() {

        compositeDisposable.add(Common.itemAdjustmentRepository.getItemItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<ItemAdjustment>>() {
            @Override
            public void accept(List<ItemAdjustment> ItemAdjustment) throws Exception {
                Log.e("Book", "Book" + new Gson().toJson(ItemAdjustment));
                loadCustomer(ItemAdjustment);
            }
        }));

    }

    private void loadCustomer(List<ItemAdjustment> ItemAdjustment) {

        // List<ItemModel> arrayList= new ArrayList(Constant.map.values());

        mAdapters = new ItemAdjustmentAdapter(this, ItemAdjustment);

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

        spinnerDialogs = new SpinnerDialogFor(AdjustmentActivity.this, bookArrayList, "Select Book", "A");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                text_customer_spinner.setText(item);
                Log.e("hgsc", "vcc" + item.substring(item.length() - 16));
                Book book = Common.bookRepository.getBook(item.substring(item.length() - 16));


                if (book != null) {
                    Intent intent = new Intent(AdjustmentActivity.this, ItemAdjustmentActivity.class);
                    intent.putExtra("EXTRA_SESSION", book.BARCODE_NUMBER);
                    startActivity(intent);
                    finish();
                    infoDialog.dismiss();
                } else {
                    Toast.makeText(AdjustmentActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(AdjustmentActivity.this, BarcodeAdjustmentActivity.class));
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
                    Intent intent = new Intent(AdjustmentActivity.this, ItemAdjustmentActivity.class);
                    intent.putExtra("EXTRA_SESSION", edit_book_code.getText().toString());
                    startActivity(intent);
                    finish();
                    infoDialog.dismiss();
                } else {
                    Toast.makeText(AdjustmentActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        infoDialog.show();
    }

}
