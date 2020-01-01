package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.InvoiceAdapter;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class SalesReturnListActivity extends AppCompatActivity {
    RecyclerView rcl_this_customer_list;
    InvoiceAdapter mAdapters;
    Activity mActivity;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView btn_header_application;
    ImageView btn_header_back;
    static EditText edit_start_date;
    static  EditText edit_end_date;
    Button btn_yes;
    ProgressBar progress_bar;
    EditText edit_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return_list);
        CorrectSizeUtil.getInstance(this).correctSize();
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        edit_start_date = findViewById(R.id.edit_start_date);
        edit_content = findViewById(R.id.edit_content);
        progress_bar = findViewById(R.id.progress_bar);
        edit_end_date = findViewById(R.id.edit_end_date);
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);
        btn_header_application = findViewById(R.id.btn_header_application);
        btn_header_back = findViewById(R.id.btn_header_back);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                mAdapters.getFilter().filter(edit_content.getText().toString());
            }
        });
        btn_header_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReturnListActivity.this,SalesReturnActivity.class));
                finish();
            }
        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReturnListActivity.this,MainActivity.class));
                finish();
            }
        });
        btn_yes =findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_start_date.getText().toString().matches("")) {
                    Toast.makeText(mActivity, "You did not enter a Start Date", Toast.LENGTH_SHORT).show();

                }
                else if (edit_end_date.getText().toString().matches("")){
                    Toast.makeText(mActivity, "You did not enter a End Date", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadDataByDate();
                }


            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        edit_start_date.setText(formatter.format(date));
        edit_end_date.setText(formatter.format(date));
        edit_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new SalesReturnListActivity.DatePickerFromFragment();

                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        edit_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new SalesReturnListActivity.DatePickerFromFragment.DatePickerFromFragments();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
    }
    private void displayCustomerItems(List<SalesMaster> userActivities) {
        //  showLoadingProgress(mActivity);
        mAdapters = new InvoiceAdapter(this, userActivities);

        rcl_this_customer_list.setAdapter(mAdapters);


    }
    private void loadDataByDate() {
        progress_bar.setVisibility(View.VISIBLE);
        String startDate=edit_start_date.getText().toString();
        String endDate=edit_end_date.getText().toString();
        Date date1 = null;
        Date date2 = null;
        try {
            date1=new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
            date2=new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        compositeDisposable.add(Common.salesMasterRepository.getInvoiceActivityItemByDate(date1,date2,"R").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> userActivities) throws Exception {
                displayCustomerItems(userActivities);
                progress_bar.setVisibility(View.GONE);

            }
        }));

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadCustomer();
        load();
    }

    private  void loadCustomer() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        progress_bar.setVisibility(View.VISIBLE);
        Date date1 = null;
        Date date2 = null;
        try {
            date1=new SimpleDateFormat("dd-MM-yyyy").parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        compositeDisposable.add(Common.salesMasterRepository.getInvoiceActivityItemByDate(date1,date1,"R").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> userActivities) throws Exception {
                displayCustomerItems(userActivities);
                Log.e("fsd","dfsdf"+new Gson().toJson(userActivities));
                // Log.e("fsd","dfsdf"+date);
                progress_bar.setVisibility(View.GONE);

            }
        }));

    }

    private  void load() {

        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> customers) throws Exception {
                Log.e("fsd","dfsdf"+new Gson().toJson(customers));
                // displayCustomerItems(customers);
            }
        }));

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
    public static class DatePickerFromFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                calendar.setTime(sdf.parse(edit_start_date.getText().toString()));
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
            EditText startTime2 = (EditText) getActivity().findViewById(R.id.edit_start_date);
            startTime2.setText(formattedDate);


        }
        public static class DatePickerFromFragments extends DialogFragment implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                try {
                    calendar.setTime(sdf.parse(edit_end_date.getText().toString()));
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
                EditText startTime2 = (EditText) getActivity().findViewById(R.id.edit_end_date);
                startTime2.setText(formattedDate);


            }

        }
    }
}
