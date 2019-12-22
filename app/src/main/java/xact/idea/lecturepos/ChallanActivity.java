package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.pager.Pager;

public class ChallanActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TabLayout tabLayout;
    ImageView btn_header_back;
    //This is our viewPager
    private ViewPager viewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int size;
    int sizes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back =  findViewById(R.id.btn_header_back);
        tabLayout =  findViewById(R.id.tabLayout);
//        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
//            @Override
//            public void accept(List<Challan> customers) throws Exception {
//                Log.e("xvd","cvcv"+new Gson().toJson(customers));
//                size=customers.size();
//
//            }
//        }));
        tabLayout.addTab(tabLayout.newTab().setText("New List"+" ("+ Constant.sizes +")"));
        tabLayout.addTab(tabLayout.newTab().setText("Receive List "+" ("+  Constant.size +")"));
        //Adding the tabs using addTab() method

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChallanActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    public  void fixed(){
        compositeDisposable.add(Common.challanRepositoy.getList("N").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                Log.e("SDfd","Dgd"+new Gson().toJson(customers));
                Constant.sizes =customers.size();


               // text_publisher_chalan.setText("Publishers Chalan (" +String.valueOf(customers.size())+")");

            }
        }));
        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                Log.e("SDfd","Dgd"+new Gson().toJson(customers));
                Constant.size =customers.size();


            }
        }));
    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
