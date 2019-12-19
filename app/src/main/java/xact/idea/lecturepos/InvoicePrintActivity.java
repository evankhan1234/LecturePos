package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InvoicePrintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
    }
}
