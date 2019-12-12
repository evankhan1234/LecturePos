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
import xact.idea.lecturepos.Model.ItemModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

import static java.lang.Math.round;
import static xact.idea.lecturepos.Utils.Utils.rounded;

public class ItemActivity extends AppCompatActivity {

    EditText bookname;
    EditText price;
    EditText quantity;
    EditText discount;
    EditText amount;
    Button save;
    Button btn_new;
    Button btn_update;
    ImageView btn_header_back;
    String sessionId;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back = findViewById(R.id.btn_header_back);
        btn_new = findViewById(R.id.btn_new);
        btn_update = findViewById(R.id.btn_update);
        bookname = findViewById(R.id.bookname);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        discount = findViewById(R.id.discount);
        amount = findViewById(R.id.amount);
        save = findViewById(R.id.save);
        btn_new = findViewById(R.id.btn_new);

        sessionId = getIntent().getStringExtra("EXTRA_SESSION");
        Log.e("seesion", "dsd" + sessionId);
        if (sessionId.equals("update")) {
            btn_update.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            btn_new.setVisibility(View.GONE);
            position = getIntent().getIntExtra("id", 0);
            ItemModel itemModel = Constant.arrayList.get(position);
            if (itemModel != null) {
                bookname.setText(itemModel.BookName);
                quantity.setText(String.valueOf(itemModel.Quantity));
                price.setText(String.valueOf(itemModel.Price));
                discount.setText(String.valueOf(itemModel.Discount));
                amount.setText(String.valueOf(itemModel.Amount));
            }

        } else {
            btn_update.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            btn_new.setVisibility(View.VISIBLE);
            Book book = Common.bookRepository.getBook(sessionId);


            if (book != null) {
                bookname.setText(book.BookName);
                price.setText(String.valueOf(book.BookPrice));
            } else {
                Toast.makeText(this, "No Books Found", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                finish();
            }

        }

//        btn_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ItemActivity.this, InvoiceActivity.class);
//                intent.putExtra("value", "value");
//                startActivity(intent);
//                finish();
//            }
//        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                finish();
            }
        });


        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final double prices;
                final double quantitys;
                final  double d ;
                if (!price.getText().toString().equals("")){
                    prices = Double.parseDouble(price.getText().toString());
                }
                else {
                    prices=0.0;
                }
                if (!quantity.getText().toString().equals("")){
                    quantitys = Double.parseDouble(quantity.getText().toString());
                }
                else {
                    quantitys=0.0;
                }
                if (!s.toString().equals("")){
                    d = Double.parseDouble(s.toString());
                }
                else {
                    d=0.0;
                }

                double total = (prices * quantitys) - d;

                amount.setText(String.valueOf(rounded(total,2)));
            }
        });
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (quantity.getText().toString().equals("")){
                    Toast.makeText(ItemActivity.this, "Please Give your Quantity Value", Toast.LENGTH_SHORT).show();
                }
                else {
                    final double prices;
                    final double quantitys;
                    final  double d ;
                    if (!price.getText().toString().equals("")){
                        prices = Double.parseDouble(price.getText().toString());
                    }
                    else {
                        prices=0.0;
                    }
                    if (!quantity.getText().toString().equals("")){
                        quantitys = Double.parseDouble(quantity.getText().toString());
                    }
                    else {
                        quantitys=0.0;
                    }
                    if (!s.toString().equals("")){
                        d = Double.parseDouble(s.toString());
                    }
                    else {
                        d=0.0;
                    }


                    double total = (prices * quantitys) - d;
                    amount.setText(String.valueOf(rounded(total,2)));
                    Toast.makeText(ItemActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!quantity.getText().toString().equals("")) {
                    double pricesfor = Double.parseDouble(price.getText().toString());
                    int quantityfor = Integer.parseInt(quantity.getText().toString());
                    double discountfor;
                    if (!discount.getText().toString().equals("")) {
                        discountfor = Double.parseDouble(discount.getText().toString());
                    } else {
                        discountfor = 0;
                    }

                    double totalfor = Double.parseDouble(amount.getText().toString());
                    ItemModel itemModel = new ItemModel();
                    itemModel.Amount = totalfor;
                    itemModel.Price = pricesfor;
                    itemModel.Quantity = quantityfor;
                    itemModel.Discount = discountfor;
                    Book book = Common.bookRepository.getBook(sessionId);

                    Log.e("xc","Vdx"+book.BookNo);
                    itemModel.BookId = book.BookNo;
                    itemModel.BookName = bookname.getText().toString();
                    Constant.arrayList.set(position, itemModel);
                    startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                    finish();
                } else {
                    Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                    // Constant.arrayList.set(1,itemModel);
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!quantity.getText().toString().equals("")) {
                    double pricesfor = Double.parseDouble(price.getText().toString());
                    int quantityfor = Integer.parseInt(quantity.getText().toString());
                    double discountfor;
                    if (!discount.getText().toString().equals("")) {
                        discountfor = Double.parseDouble(discount.getText().toString());
                    } else {
                        discountfor = 0;
                    }

                    double totalfor = Double.parseDouble(amount.getText().toString());
                    ItemModel itemModel = new ItemModel();
                    itemModel.Amount = totalfor;
                    itemModel.Price = pricesfor;
                    itemModel.Quantity = quantityfor;
                    itemModel.Discount = discountfor;
                    Book book = Common.bookRepository.getBook(sessionId);
                    itemModel.BookId = book.BookNo;
                    itemModel.BookName = bookname.getText().toString();
                    Constant.arrayList.add(itemModel);
                    startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                    finish();
                } else {
                    Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                    // Constant.arrayList.set(1,itemModel);
                }

            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!quantity.getText().toString().equals("")) {
                    double pricesfor = Double.parseDouble(price.getText().toString());
                    int quantityfor = Integer.parseInt(quantity.getText().toString());
                    double discountfor;
                    if (!discount.getText().toString().equals("")) {
                        discountfor = Double.parseDouble(discount.getText().toString());
                    } else {
                        discountfor = 0;
                    }

                    double totalfor = Double.parseDouble(amount.getText().toString());
                    ItemModel itemModel = new ItemModel();
                    itemModel.Amount = totalfor;
                    itemModel.Price = pricesfor;
                    itemModel.Quantity = quantityfor;
                    itemModel.Discount = discountfor;
                    itemModel.BookId = sessionId;
                    itemModel.BookName = bookname.getText().toString();
                    Constant.arrayList.add(itemModel);

                    Intent intent = new Intent(ItemActivity.this, InvoiceActivity.class);
                    intent.putExtra("value", "value");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                    // Constant.arrayList.set(1,itemModel);
                }

            }
        });
    }
}
