package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToMainPage();
            }
        }, 3000);
    }
    private void goToMainPage() {

        Intent i = new Intent(OnBoardingActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
