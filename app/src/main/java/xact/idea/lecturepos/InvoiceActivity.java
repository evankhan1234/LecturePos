package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.CustomerAdapter;
import xact.idea.lecturepos.Adapter.ItemAdapter;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Model.ItemModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class InvoiceActivity extends AppCompatActivity {

    Button create;
    Button save;
    EditText editShippingChargesValue;
    EditText edit_retail_code;
    EditText edit_contact_number;
    EditText edit_note;
    EditText edit_address;
    static EditText edit_date;
    TextView text_sub_total;
    TextView text_net_amounts;
    ItemAdapter mAdapters;
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView rcl_this_customer_list;
    ImageView btn_header_back;
    private List<Customer> customerArrayList = new ArrayList<>();
    ArrayAdapter<Customer> customerListEntityArrayAdapter;
    Spinner spinner_customer;
    String Name;
    RadioButton radioCash;
    RadioButton radioCredit;
    RadioGroup radioLogin;
    String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        radioLogin = findViewById(R.id.radioLogin);
        radioCash = findViewById(R.id.radioCash);
        radioCredit = findViewById(R.id.radioCredit);
        edit_date = findViewById(R.id.edit_date);
        save = findViewById(R.id.save);
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

        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvoiceActivity.this, MainActivity.class));
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
        if (sessionId == null) {
            //   showInfoDialog();
        } else if (sessionId.equals("value")) {
            showInfoDialog();
        } else {

        }
        spinner_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_units", "" + customerArrayList.get(position).Name);
                Name = customerArrayList.get(position).Name;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new DatePickerFromFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        edit_date.setText(formatter.format(date));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!edit_retail_code.getText().toString().equals("") && !edit_contact_number.getText().toString().equals("") && !edit_address.getText().toString().equals("") && !edit_note.getText().toString().equals("")) {
                    int selectedId = radioLogin.getCheckedRadioButtonId();
                    String s = text_net_amounts.getText().toString();
                    s = s.replace("BDT ", "");
                    // find the radiobutton by returned id
                    RadioButton radioSexButton = findViewById(selectedId);

                    String pay = String.valueOf(radioSexButton.getText());
                    double discount = 0;
                    double amount = Double.parseDouble(s);
                    try {
                        discount = Double.parseDouble(editShippingChargesValue.getText().toString());


                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    SalesMaster salesMaster = new SalesMaster();
                    int value = Common.salesMasterRepository.maxValue();
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("dd-MM-yyyy").parse(edit_date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    salesMaster.CustomerName = Name;
                    salesMaster.Discount = discount;
                    salesMaster.InvoiceId = SharedPreferenceUtil.getUserID(InvoiceActivity.this);
                    salesMaster.StoreId = SharedPreferenceUtil.getUserID(InvoiceActivity.this);
                    salesMaster.InvoiceNumber = value + "##" + SharedPreferenceUtil.getUserID(InvoiceActivity.this);
                    salesMaster.InvoiceDates = edit_date.getText().toString();
                    salesMaster.InvoiceDate = date1;
                    salesMaster.Discount = discount;
                    salesMaster.InvoiceAmount = amount;
                    salesMaster.NetValue = amount;
                    salesMaster.Device = "Mobile";
                    salesMaster.update_date = date1;
                    salesMaster.PayMode = pay;
                    salesMaster.Note = edit_note.getText().toString();
                    salesMaster.RetailCode = edit_retail_code.getText().toString();
                    Common.salesMasterRepository.insertToSalesMaster(salesMaster);

                    for (ItemModel itemModel : Constant.arrayList) {
                        int values = Common.salesMasterRepository.maxValue();
                        SalesDetails salesDetails = new SalesDetails();
                        salesDetails.BookId = itemModel.BookId;
                        salesDetails.Discount = itemModel.Discount;
                        salesDetails.MRP = itemModel.Price;
                        salesDetails.Quantity = itemModel.Quantity;
                        salesDetails.TotalAmount = itemModel.Amount;
                        salesDetails.InvoiceId = values;
                        salesDetails.StoreId = SharedPreferenceUtil.getUserID(InvoiceActivity.this);
                        Common.salesDetailsRepository.insertToSalesDetails(salesDetails);
                    }


                    startActivity(new Intent(InvoiceActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(InvoiceActivity.this, "Successfully Created a Invoice ", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(InvoiceActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        customerListData();
        editShippingChargesValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                 double prices;
                  double d = 0.0;
                if (!text_sub_total.getText().toString().equals("")){
                    String s1 = text_sub_total.getText().toString();
                    s1 = s1.replace("BDT ", "");
                    prices = Double.parseDouble(s1);
                    if (!s.toString().equals("")){
                        d = Double.parseDouble(s.toString());
                    }
                    else {
                        d=0.0;
                    }
                    double total = prices  -d;
                    text_net_amounts.setText("BDT "+String.valueOf(rounded(total,2)));
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

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomer();
        masterListData();
        detailsListData();


        double total = 0;
        double discount = 0;
        double amount = 0;
        for (ItemModel itemModel : Constant.arrayList) {
            total += itemModel.Amount;
        }
        text_sub_total.setText("BDT " + String.valueOf(total));
        if (!editShippingChargesValue.getText().toString().equals("")) {
            discount = Double.parseDouble(editShippingChargesValue.getText().toString());
            amount = total - discount;
            text_net_amounts.setText("BDT " + String.valueOf(amount));

        } else {
            discount = 0;
            amount = total - discount;
            text_net_amounts.setText("BDT " + String.valueOf(amount));
        }
    }

    private void loadCustomer() {

        mAdapters = new ItemAdapter(this, Constant.arrayList);

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
        customerArrayList = customers;
        customerListEntityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerArrayList);
        customerListEntityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_customer.setAdapter(customerListEntityArrayAdapter);
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
        //   final  Button btn_done = infoDialog.findViewById(R.id.btn_done);

        CorrectSizeUtil.getInstance(this).correctSize(main_root);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_book_code.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                startActivity(new Intent(InvoiceActivity.this, BarcodeActivity.class));
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
                    Intent intent = new Intent(InvoiceActivity.this, ItemActivity.class);
                    intent.putExtra("EXTRA_SESSION", edit_book_code.getText().toString());
                    startActivity(intent);
                    finish();
                    infoDialog.dismiss();
                } else {
                    Toast.makeText(InvoiceActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        infoDialog.show();
    }

}
