package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.CustomerAdapter;
import xact.idea.lecturepos.Adapter.InvoiceAdapter;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InvoiceListActivity extends AppCompatActivity {
    RecyclerView rcl_this_customer_list;
    InvoiceAdapter mAdapters;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView btn_header_application;
    ImageView btn_header_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        rcl_this_customer_list = findViewById(R.id.rcl_this_customer_list);
        btn_header_application = findViewById(R.id.btn_header_application);
        btn_header_back = findViewById(R.id.btn_header_back);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);

        btn_header_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvoiceListActivity.this,InvoiceActivity.class));
                finish();
            }
        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvoiceListActivity.this,MainActivity.class));
                finish();
            }
        });
    }
    private void displayCustomerItems(List<SalesMaster> userActivities) {
        //  showLoadingProgress(mActivity);
        mAdapters = new InvoiceAdapter(this, userActivities);

        rcl_this_customer_list.setAdapter(mAdapters);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomer();
        load();
    }

    private  void loadCustomer() {

        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> customers) throws Exception {
                displayCustomerItems(customers);
                Log.e("dsd","vxcv"+new Gson().toJson(customers));
            }
        }));

    }

    private  void load() {

        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
            @Override
            public void accept(List<SalesDetails> customers) throws Exception {

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
}
