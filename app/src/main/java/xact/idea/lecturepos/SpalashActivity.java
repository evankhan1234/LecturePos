package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class SpalashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
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
