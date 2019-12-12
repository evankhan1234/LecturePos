package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import xact.idea.lecturepos.Adapter.InventoryAdapter;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InventoryActivity extends AppCompatActivity {

    InventoryAdapter mAdapters;
    RecyclerView rcl_approval_in_list;
    Activity mActivity;
    ImageView btn_header_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back=findViewById(R.id.btn_header_back);
        rcl_approval_in_list=findViewById(R.id.rcl_approval_in_list);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_approval_in_list.setLayoutManager(lm);
        display();
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this,MainActivity.class));
                finish();
            }
        });
    }
    private  void display() {

        mAdapters = new InventoryAdapter(mActivity);
        try {
            rcl_approval_in_list.setAdapter(mAdapters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //EmployeeStaus();

    }
}
