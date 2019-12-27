package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.InventoryAdapter;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Model.StockModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.Utils;

public class InventoryActivity extends AppCompatActivity {

    InventoryAdapter mAdapters;
    RecyclerView rcl_approval_in_list;
    Activity mActivity;
    ImageView btn_header_back;
    TextView text_quantity;
    TextView text_net_price;
    EditText edit_content;
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    int quantity;
    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        edit_content=findViewById(R.id.edit_content);
        text_quantity=findViewById(R.id.text_quantity);
        text_net_price=findViewById(R.id.text_net_price);
        btn_header_back=findViewById(R.id.btn_header_back);
        rcl_approval_in_list=findViewById(R.id.rcl_approval_in_list);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_approval_in_list.setLayoutManager(lm);

        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this,MainActivity.class));
                finish();
            }
        });
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

    }
    private  void loadStockItems() {
//        Log.e("size","size"+Common.bookStockRepository.size());
//        compositeDisposable.add(Common.bookStockRepository.getBookStockItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<BookStock>>() {
//            @Override
//            public void accept(List<BookStock> stockModels) throws Exception {
//                Log.e("size","size"+new Gson().toJson(stockModels));
//                display(stockModels);
//            }
//        }));
        compositeDisposable.add(Common.bookStockRepository.getBookStockModel().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<StockModel>>() {
            @Override
            public void accept(List<StockModel> stockModels) throws Exception {
                Log.e("StockModel","StockModel"+new Gson().toJson(stockModels));
               display(stockModels);
            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quantity=Common.bookStockRepository.TotalQuantity();
                text_quantity.setText(String.valueOf(quantity)+" Pcs");
                price=Common.bookStockRepository.TotalPrice();
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

                symbols.setGroupingSeparator(',');
                formatter.setDecimalFormatSymbols(symbols);
              //  System.out.println(formatter.format(price));
                text_net_price.setText(Utils.getCommaSeperatorValue(price));
                loadStockItems();
            }
        }, 300);

    }

    private  void display(List<StockModel> stockModels) {


        mAdapters = new InventoryAdapter(mActivity,stockModels);
        rcl_approval_in_list.setAdapter(mAdapters);

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
