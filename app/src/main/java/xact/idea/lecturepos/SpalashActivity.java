package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class SpalashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
        Date datess = null;
        SimpleDateFormat formatterss = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            datess= formatterss.parse("09-12-2019 09:30:27 AM");
            Log.e("currentTime","currentTime"+datess);
        } catch (ParseException e) {
            e.printStackTrace();
        }



            Date date1 =null;
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            try {
                date1= formatter1.parse("09-12-2019 09:30:25 AM");
                Log.e("currentTime","currentTime"+date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (datess.compareTo(date1)>0){
                Log.e("evan","currentTime");
            }
            else {
                Log.e("khan","currentTime");
            }



        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToLoginPage();
            }
        }, 3000);
    }
    private void goToLoginPage() {
        if (SharedPreferenceUtil.getUserID(SpalashActivity.this)==null||SharedPreferenceUtil.getUserID(SpalashActivity.this).equals("")) {
            Intent i = new Intent(SpalashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(SpalashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }



}
