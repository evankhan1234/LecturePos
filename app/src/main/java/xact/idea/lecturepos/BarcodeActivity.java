package xact.idea.lecturepos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;

public class BarcodeActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        text=findViewById(R.id.text);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(Portait.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scan Book");
        intentIntegrator.initiateScan();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult!=null){
            if (intentResult.getContents()==null){

                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();

            }
            else {
               // Constant.code=intentResult.getContents();

                Book book= Common.bookRepository.getBook( intentResult.getContents());
                if (book!=null){
                    Intent intent = new Intent(BarcodeActivity.this, ItemActivity.class);
                    intent.putExtra("EXTRA_SESSION", intentResult.getContents());
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Not Books Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BarcodeActivity.this, InvoiceActivity.class);
                    startActivity(intent);
                    finish();
                }





            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("DSad","sf");
        super.onBackPressed();
        Intent intent = new Intent(BarcodeActivity.this, InvoiceActivity.class);
        startActivity(intent);
        finish();
    }
}
