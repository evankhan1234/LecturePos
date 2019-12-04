package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class ItemActivity extends AppCompatActivity {

    EditText bookname;
    EditText price;
    EditText quantity;
    EditText discount;
    EditText amount;
    Button save;
    ImageView btn_header_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back=findViewById(R.id.btn_header_back);
        bookname=findViewById(R.id.bookname);
        price=findViewById(R.id.price);
        quantity=findViewById(R.id.quantity);
        discount=findViewById(R.id.discount);
        amount=findViewById(R.id.amount);
        save=findViewById(R.id.save);
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Book book= Common.bookRepository.getBook(sessionId);

        bookname.setText(book.BookName);
        price.setText(String.valueOf(book.BookPrice));
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ItemActivity.this,InvoiceActivity.class));
                finish();
            }
        });


        quantity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.e("ADsaf","sdfs"+s);
                final double prices= Double.parseDouble(price.getText().toString());
                final  double quantitys= Double.parseDouble(s.toString());
                double total=(prices*quantitys)-0;
                amount.setText(String.valueOf(total));
                Toast.makeText(ItemActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        discount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.e("ADsaf","sdfs"+s);
                final double prices= Double.parseDouble(price.getText().toString());
                final  double quantitys= Double.parseDouble(quantity.getText().toString());
                double d= Double.parseDouble(s.toString());
                double total=(prices*quantitys)-d;
               amount.setText(String.valueOf(total));
                Toast.makeText(ItemActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}
