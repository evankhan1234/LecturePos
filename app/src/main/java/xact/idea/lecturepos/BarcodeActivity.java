package xact.idea.lecturepos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(Portait.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scan Book");
        intentIntegrator.initiateScan();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult!=null){
            if (intentResult.getContents()!=null){

                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
