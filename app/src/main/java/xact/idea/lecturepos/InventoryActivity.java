package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

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

public class InventoryActivity extends AppCompatActivity {

    InventoryAdapter mAdapters;
    RecyclerView rcl_approval_in_list;
    Activity mActivity;
    ImageView btn_header_back;
    TextView text_quantity;
    TextView text_net_price;
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
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
        int quantity=Common.bookStockRepository.TotalQuantity();
        text_quantity.setText(String.valueOf(quantity)+" Pcs");
        double price=Common.bookStockRepository.TotalPrice();
        text_net_price.setText(String.valueOf(price)+" Tk");
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
        loadStockItems();
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
