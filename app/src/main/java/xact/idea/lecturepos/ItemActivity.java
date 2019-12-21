package xact.idea.lecturepos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.Items;
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
    EditText bookQuantity;
    Button save;
    Button btn_new;
    Button btn_update;
    ImageView btn_header_back;
    String sessionId;
    String position;
    BookStock bookStock;
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
        bookQuantity = findViewById(R.id.bookQuantity);

        sessionId = getIntent().getStringExtra("EXTRA_SESSION");
        Log.e("seesion", "dsd" + sessionId);
        if (sessionId.equals("update")) {
            btn_update.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            btn_new.setVisibility(View.GONE);
            position = getIntent().getStringExtra("id");
            Log.e("position","position"+position);
            Items   itemModel = Common.itemRepository.getItems(position);
            if (itemModel != null) {
                bookname.setText(itemModel.BookName);
                quantity.setText(String.valueOf(itemModel.Quantity));
                price.setText(String.valueOf(itemModel.Price));
                discount.setText(String.valueOf(itemModel.Discount));
                amount.setText(String.valueOf(itemModel.Amount));
                bookStock = Common.bookStockRepository.getBookStock(itemModel.BookId);
                if (bookStock!=null){
                    bookQuantity.setText(String.valueOf(bookStock.QTY_NUMBER));
                }
                else {

                }
            }

        } else {
            btn_update.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            btn_new.setVisibility(View.VISIBLE);
            Book book = Common.bookRepository.getBook(sessionId);
             bookStock = Common.bookStockRepository.getBookStock(book.BookNo);
            if (bookStock!=null){
                bookQuantity.setText(String.valueOf(bookStock.QTY_NUMBER));
            }
            else {

            }


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
                final double d;
                if (!price.getText().toString().equals("")) {
                    prices = Double.parseDouble(price.getText().toString());
                } else {
                    prices = 0.0;
                }
                if (!discount.getText().toString().equals("")) {
                    quantitys = Double.parseDouble(discount.getText().toString());
                } else {
                    quantitys = 0.0;
                }
                if (!s.toString().equals("")) {
                    d = Double.parseDouble(s.toString());
                } else {
                    d = 0.0;
                }

                Log.e("prices",""+prices);
                Log.e("quantitys",""+quantitys);
                Log.e("dis",""+d);
                double total = (prices * d);
                double totals = (prices * d) * quantitys/100;
                double t = total-totals;

                amount.setText(String.valueOf(rounded(t, 2)));


            }
        });
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (quantity.getText().toString().equals("")) {
                    Toast.makeText(ItemActivity.this, "Please Give your Quantity Value", Toast.LENGTH_SHORT).show();
                } else {
                    final double prices;
                    final double quantitys;
                    final double d;
                    if (!price.getText().toString().equals("")) {
                        prices = Double.parseDouble(price.getText().toString());
                    } else {
                        prices = 0.0;
                    }
                    if (!quantity.getText().toString().equals("")) {
                        quantitys = Double.parseDouble(quantity.getText().toString());
                    } else {
                        quantitys = 0.0;
                    }
                    if (!s.toString().equals("")) {
                        d = Double.parseDouble(s.toString());
                    } else {
                        d = 0.0;
                    }


                    double total = (prices * quantitys);
                    double totals = (prices * quantitys) * d/100;
                    double t = total-totals;
                    amount.setText(String.valueOf(rounded(t, 2)));
                   // Toast.makeText(ItemActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                    if (bookStock.QTY_NUMBER>0){
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
                            Items itemModel = new Items();
                            itemModel.Amount = totalfor;
                            itemModel.Price = pricesfor;
                            itemModel.Quantity = quantityfor;
                            itemModel.Discount = discountfor;
//                    Book book = Common.bookRepository.getBook(sessionId);

                            Items   itemModels = Common.itemRepository.getItems(position);
                            itemModel.BookId = itemModels.BookId;
                            itemModel.id = itemModels.id;
                            itemModel.BookName = bookname.getText().toString();
                            Common.itemRepository.updateItem(itemModel);
                            startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                    }
                    else {
                        Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }



            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                    if (bookStock.QTY_NUMBER>0){
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
//                    if (data()){
//                        Map<String, ItemModel> userMap = new HashMap<>();
//                        ItemModel itemModel = new ItemModel();
//                        itemModel.Amount = totalfor+BookPrice;
//                        itemModel.Price = pricesfor;
//                        itemModel.Quantity = quantityfor+BookQuantity;
//                        itemModel.Discount = discountfor+BookDiscount;
//                        userMap.put(bookname.getText().toString(), itemModel);
//
//                    }
                            // else {
                            //    else {
                            Items items1 =Common.itemRepository.getItems(bookname.getText().toString());
                            Items items = new Items();
                            if (items1!=null){
                                items.Amount = totalfor+items1.Amount;
                                items.Price = pricesfor;
                                items.Quantity = quantityfor+items1.Quantity;
                                items.Discount = discountfor+items1.Discount;
                                Book book = Common.bookRepository.getBook(sessionId);
                                items.BookId = book.BookNo;
                                items.BookName = bookname.getText().toString();
                                items.id=items1.id;
                                Common.itemRepository.updateItem(items);
                            }
                            else {
                                items.Amount = totalfor;
                                items.Price = pricesfor;
                                items.Quantity = quantityfor;
                                items.Discount = discountfor;
                                Book book = Common.bookRepository.getBook(sessionId);
                                items.BookId = book.BookNo;
                                items.BookName = bookname.getText().toString();
                                Common.itemRepository.insertToItem(items);
                            }

//                    List<ItemModel> itemsList = Constant.map.get(bookname.getText().toString());
//
//                    // if list does not exist create it
//                    if(itemsList == null) {
//                        itemsList = new ArrayList<>();
//                        itemsList.add(itemModel);
//                        Constant.map.put(bookname.getText().toString(), itemsList);
//                    } else {
//                        // add if item is not already in list
//                        if(!itemsList.contains(itemModel)) {
//                            // itemsList.add(itemModel);
//                            Constant.map.replace(bookname.getText().toString(), itemsList);
//                        }
//                    }
                            //     }
//
//                    ItemModel itemModel = new ItemModel();
//                    itemModel.Amount = totalfor;
//                    itemModel.Price = pricesfor;
//                    itemModel.Quantity = quantityfor;
//                    itemModel.Discount = discountfor;
//                    Book book = Common.bookRepository.getBook(sessionId);
//                    itemModel.BookId = book.BookNo;
//                    itemModel.BookName = bookname.getText().toString();
//                    Constant.arrayList.add(itemModel);
                            startActivity(new Intent(ItemActivity.this, InvoiceActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                    }
                    else
                    {
                        Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }



            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                    if (bookStock.QTY_NUMBER>0){
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
//                 //   if (data()){
//                        Map<String, ItemModel> userMap = new HashMap<>();
//                        ItemModel itemModel = new ItemModel();
//                        itemModel.Amount = totalfor;
//                        itemModel.Price = pricesfor+BookPrice;
//                        itemModel.Quantity = quantityfor+BookQuantity;
//                        itemModel.Discount = discountfor+BookDiscount;
//                        userMap.put(itemModel.BookName, itemModel);
//
//                    }
                            //    else {
                            Items items1 =Common.itemRepository.getItems(bookname.getText().toString());
                            Items items = new Items();
                            if (items1!=null){
                                items.Amount = totalfor;
                                items.Price = pricesfor+items1.Price;
                                items.Quantity = quantityfor+items1.Quantity;
                                items.Discount = discountfor+items1.Discount;
                                Book book = Common.bookRepository.getBook(sessionId);
                                items.BookId = book.BookNo;
                                items.BookName = bookname.getText().toString();
                                items.id=items1.id;
                                Common.itemRepository.updateItem(items);
                            }
                            else {
                                items.Amount = totalfor;
                                items.Price = pricesfor;
                                items.Quantity = quantityfor;
                                items.Discount = discountfor;
                                Book book = Common.bookRepository.getBook(sessionId);
                                items.BookId = book.BookNo;
                                items.BookName = bookname.getText().toString();
                                Common.itemRepository.insertToItem(items);
                            }

//                     List<ItemModel> itemsList = Constant.map.get(bookname.getText().toString());
//
//                    // if list does not exist create it
//                    if(itemsList == null) {
//                        itemsList = new ArrayList<>();
//                        itemsList.add(itemModel);
//                        Constant.map.put(bookname.getText().toString(), itemsList);
//                    } else {
//                        // add if item is not already in list
//                        if(!itemsList.contains(itemModel)) {
//                           // itemsList.add(itemModel);
//                            Constant.map.replace(bookname.getText().toString(), itemsList);
//                        }
//                    }
                            //   }


                            Intent intent = new Intent(ItemActivity.this, InvoiceActivity.class);
                            intent.putExtra("value", "value");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ItemActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                    }
                    else
                    {
                        Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(ItemActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private int BookQuantity;
    private double BookPrice;
    private double BookDiscount;

    private boolean data() {
        for (ItemModel itemModel : Constant.arrayList) {

            if (itemModel.BookName.equals(bookname.getText().toString())) {
                BookQuantity = itemModel.Quantity;
                BookPrice = itemModel.Amount;
                BookDiscount = itemModel.Discount;
                return true;
            } else {

            }
        }
        return false;
    }
}
