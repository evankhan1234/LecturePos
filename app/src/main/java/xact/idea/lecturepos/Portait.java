package xact.idea.lecturepos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;

public class Portait extends CaptureActivity {

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_header);
//
//    }

    @Override
    public void onBackPressed() {
        Log.e("DSad","sf");
        super.onBackPressed();
        Intent intent = new Intent(Portait.this, InvoiceActivity.class);
        startActivity(intent);
        finish();
    }
}