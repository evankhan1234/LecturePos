package xact.idea.lecturepos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.ItemReturn;
import xact.idea.lecturepos.Model.GroupModel;
import xact.idea.lecturepos.Model.ItemModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class ItemReturnActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView bookname;
    TextView price;
    TextView bookMRP;
    TextView booknameBangla;
    EditText quantity;
    EditText discount;
    EditText amount;
    TextView bookQuantity;
    Button save;
    Button btn_new;
    Button btn_new_scan;
    Button btn_update;
    ImageView btn_header_back;
    String sessionId;
    String position;
    BookStock bookStock;
    Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_return);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back = findViewById(R.id.btn_header_back);
        btn_new = findViewById(R.id.btn_new);
        btn_update = findViewById(R.id.btn_update);
        bookname = findViewById(R.id.bookname);
        price = findViewById(R.id.price);
        bookMRP = findViewById(R.id.bookMRP);
        booknameBangla = findViewById(R.id.booknameBangla);
        quantity = findViewById(R.id.quantity);
        discount = findViewById(R.id.discount);
        amount = findViewById(R.id.amount);
        save = findViewById(R.id.save);
        btn_new = findViewById(R.id.btn_new);
        btn_new_scan = findViewById(R.id.btn_new_scan);
        bookQuantity = findViewById(R.id.bookQuantity);
        quantity.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(quantity, InputMethodManager.SHOW_IMPLICIT);
        sessionId = getIntent().getStringExtra("EXTRA_SESSION");
        Log.e("seesion", "dsd" + sessionId);
        if (sessionId.equals("update"))
        {
            btn_update.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            btn_new.setVisibility(View.GONE);
            position = getIntent().getStringExtra("id");
            Log.e("position","position"+position);
            ItemReturn itemModel = Common.itemReturnRepository.getItems(position);
            if (itemModel != null) {
                bookname.setText(itemModel.BookName);
                quantity.setText(String.valueOf(itemModel.Quantity));
                price.setText(String.valueOf(itemModel.Price));
                bookMRP.setText(String.valueOf(itemModel.ValuePrice));
                discount.setText(String.valueOf(itemModel.Discount));
                amount.setText(String.valueOf(itemModel.Amount));
                booknameBangla.setText(String.valueOf(itemModel.BookNameBangla));
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
            Book books = Common.bookRepository.getBook(sessionId);
            book = Common.bookRepository.getBookItemFor(sessionId, books.BOOK_GROUP_ID);
            if (book==null){
                book=  Common.bookRepository.getBook(sessionId);
            }
            bookStock = Common.bookStockRepository.getBookStock(book.BookNo);
            if (bookStock!=null){
                bookQuantity.setText(String.valueOf(bookStock.QTY_NUMBER));
            }
            else {

            }


            if (book != null) {
                bookname.setText(book.BookName+" (20"+book.F_BOOK_EDITION_NO+")");

                String data= Utils.getValue("20"+book.F_BOOK_EDITION_NO);
                booknameBangla.setText(book.BookNameBangla+" ("+data+")");
                //  booknameBangla.setText(book.BookNameBangla);
                price.setText(String.valueOf(book.BOOK_NET_PRICE));
                bookMRP.setText(String.valueOf(book.BOOK_FACE_VALUE));
            } else {
                Toast.makeText(this, "No Books Found", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemReturnActivity.this, SalesReturnActivity.class));
                finish();
            }

        }

//        btn_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ItemReturnActivity.this, SalesReturnActivity.class);
//                intent.putExtra("value", "value");
//                startActivity(intent);
//                finish();
//            }
//        });
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ItemReturnActivity.this, SalesReturnActivity.class));
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
                double quantitys = 0;
                final double ss;
                if (!s.toString().equals("")) {
                    ss = Double.parseDouble(s.toString());
                } else {
                    ss = 0.0;
                }
//                double ss1=Double.parseDouble(s.toString());
                double stock= 0;
                try {
                    stock = Double.parseDouble(bookQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    stock=0;
                }
               // if (stock>=ss){
                    final double prices;

                    final double d;
                    if (!bookMRP.getText().toString().equals("")) {
                        prices = Double.parseDouble(bookMRP.getText().toString());
                    } else {
                        prices = 0.0;
                    }

                    if (!s.toString().equals("")) {
                        d = Double.parseDouble(s.toString());
                    } else {
                        d = 0.0;
                    }
                    if (!discount.getText().toString().equals("")) {
                        try {
                            quantitys = Double.parseDouble(discount.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    } else {
                        quantitys = 0.0;
                    }
                    Log.e("prices",""+prices);
                    Log.e("quantitys",""+quantitys);
                    Log.e("dis",""+d);
                    double total = (prices * d);
                    double totals = (prices * d) * quantitys/100;
                    double t = total-totals;

                    amount.setText(String.valueOf(rounded(t, 2)));
//                }
//                else {
//                    Toast.makeText(ItemReturnActivity.this, "Quantity can not grater than Stock", Toast.LENGTH_SHORT).show();
//                    amount.setText("");
//
//                }





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
                    Toast.makeText(ItemReturnActivity.this, "Please Give your Quantity Value", Toast.LENGTH_SHORT).show();
                } else {
                    final double prices;
                    final double quantitys;
                    final double d;
                    if (!bookMRP.getText().toString().equals("")) {
                        prices = Double.parseDouble(bookMRP.getText().toString());
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
                    // Toast.makeText(ItemReturnActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                   // if (bookStock.QTY_NUMBER>0)
                  //  {
                        if (!quantity.getText().toString().equals("")&& !amount.getText().toString().equals("")) {
                            double quan= Double.parseDouble(quantity.getText().toString());
                            if (quan>0){
                                double pricesfor = Double.parseDouble(bookMRP.getText().toString());
                                int quantityfor = Integer.parseInt(quantity.getText().toString());
                                double discountfor;
                                if (!discount.getText().toString().equals("")) {
                                    discountfor = Double.parseDouble(discount.getText().toString());
                                } else {
                                    discountfor = 0;
                                }

                                double totalfor = Double.parseDouble(amount.getText().toString());
                                ItemReturn itemModel = new ItemReturn();
                                itemModel.Amount = totalfor;
                                itemModel.Price = pricesfor;
                                itemModel.Quantity = quantityfor;
                                itemModel.Discount = discountfor;

                                ItemReturn   itemModels = Common.itemReturnRepository.getItems(position);
                                itemModel.BookId = itemModels.BookId;
                                BookStock  bookStocks = Common.bookStockRepository.getBookStock(itemModels.BookId);
                                if (bookStocks!=null){
                                    if (bookStock.QTY_NUMBER<=bookStocks.QTY_NUMBER){
                                        itemModel.Stock="Out";
                                    }
                                    else {
                                        itemModel.Stock="In";
                                    }
                                }
                                //itemModel.Stock = itemModels.Stock;
                                itemModel.id = itemModels.id;
                                itemModel.ValuePrice = itemModels.ValuePrice;
                                itemModel.BookName = bookname.getText().toString();
                                itemModel.BookNameBangla = booknameBangla.getText().toString();
                                Common.itemReturnRepository.updateItem(itemModel);
                                startActivity(new Intent(ItemReturnActivity.this, SalesReturnActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(ItemReturnActivity.this, "Quantity Can not be 0", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(ItemReturnActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                 //   }
                   // else {
                   //     Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                   // }
                }
                else {
                    Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }



            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                  //  if (bookStock.QTY_NUMBER>0)
                  //  {
                        if (!quantity.getText().toString().equals("") && !amount.getText().toString().equals("")) {

                           // double value=Double.parseDouble(amount.getText().toString());
                         //   if (value>0){
                                double quan= Double.parseDouble(quantity.getText().toString());
                                if (quan>0){
                                    String s=bookname.getText().toString().substring(0,bookname.getText().toString().length()-7);
                                    ItemReturn getItems =Common.itemReturnRepository.getItems(s);
                                    //  if (getItems!=null)
                                    //  {
                                    final double pricesfor = Double.parseDouble(bookMRP.getText().toString());
                                    final int quantityfor = Integer.parseInt(quantity.getText().toString());
                                    final double discountfor;
                                    if (!discount.getText().toString().equals("")) {
                                        discountfor = Double.parseDouble(discount.getText().toString());
                                    } else {
                                        discountfor = 0;
                                    }

                                    double totalfor = 0;
                                    try {
                                        totalfor = Double.parseDouble(amount.getText().toString());
                                    } catch (NumberFormatException e) {
                                        totalfor=0;
                                        e.printStackTrace();
                                    }
                                    String s1=bookname.getText().toString().substring(0,bookname.getText().toString().length()-7);

                                    final ItemReturn items1 =Common.itemReturnRepository.getItems(s1);
                                    final ItemReturn ItemReturn = new ItemReturn();
                                    if (items1!=null){
                                        final double finalTotalfor1 = totalfor;
                                        if (book.BOOK_GROUP_ID.equals("0")){
                                            double qty=Double.parseDouble(quantity.getText().toString());
                                            double dis=0;
                                            try {
                                                dis = Double.parseDouble(discount.getText().toString());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                                dis=0;
                                            }
                                            double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                            double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis/100;
                                            double t = total-totals;
                                            //ItemReturn.Amount = t +items1.Amount;
                                            ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
//                                                    ItemReturn.Amount = finalTotalfor1 +items1.Amount;
//                                                    ItemReturn.Price = pricesfor;
                                            ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                            ItemReturn.Quantity = quantityfor+items1.Quantity;
                                            ItemReturn.Discount = discountfor;
                                            ItemReturn.BookId = book.BookNo;
                                            ItemReturn.BookName = book.BookName;
                                            ItemReturn.BookNameBangla = book.BookNameBangla;
                                            ItemReturn it=Common.itemReturnRepository.getItems(book.BookName);
                                            ItemReturn.Amount = t + it.Amount;
                                            BookStock  bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);
                                            ItemReturn.Stock="Out";
                                            ItemReturn.id=it.id;
                                            Common.itemReturnRepository.updateItem(ItemReturn);
                                        }
                                        else {
                                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                                @Override
                                                public void accept(List<GroupModel> userActivities) throws Exception {


                                                    for (GroupModel groupModel:userActivities){
                                                        double qty=Double.parseDouble(quantity.getText().toString());
                                                        double dis=0;
                                                        try {
                                                            dis = Double.parseDouble(discount.getText().toString());
                                                        } catch (NumberFormatException e) {
                                                            e.printStackTrace();
                                                            dis=0;
                                                        }
                                                        double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                        double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis/100;
                                                        double t = total-totals;
                                                        ItemReturn.Amount = t +items1.Amount;
                                                        ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
//                                                    ItemReturn.Amount = finalTotalfor1 +items1.Amount;
//                                                    ItemReturn.Price = pricesfor;
                                                        ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                                        ItemReturn.Quantity = quantityfor+items1.Quantity;
                                                        ItemReturn.Discount = discountfor;
                                                        ItemReturn.BookId = groupModel.BookNo;
                                                        ItemReturn.BookName = groupModel.BookName;
                                                        ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                        ItemReturn it=Common.itemReturnRepository.getItems(groupModel.BookName);
                                                        ItemReturn.Amount = t +it.Amount;
                                                        BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                        ItemReturn.Stock="Out";
                                                        ItemReturn.id=it.id;
                                                        Common.itemReturnRepository.updateItem(ItemReturn);
                                                    }
                                                }
                                            }));
                                        }


                                    }
                                    else {
                                        final double finalTotalfor = totalfor;
                                        if (book.BOOK_GROUP_ID.equals("0")){
                                            double qty=Double.parseDouble(quantity.getText().toString());
                                            double dis=0;
                                            try {
                                                dis = Double.parseDouble(discount.getText().toString());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                                dis=0;
                                            }
                                            double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                            double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis/100;
                                            double t = total-totals;
                                            ItemReturn.Amount = t ;
                                            ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
                                            ItemReturn.Quantity = quantityfor;
                                            ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                            BookStock  bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);

                                                ItemReturn.Stock="Out";
                                            ItemReturn.Discount = discountfor;
                                            ItemReturn.BookId = book.BookNo;
                                            ItemReturn.BookNameBangla = book.BookNameBangla;
                                            ItemReturn.BookName = book.BookName;
                                            Common.itemReturnRepository.insertToItem(ItemReturn);
                                        }
                                        else {
                                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                                @Override
                                                public void accept(List<GroupModel> userActivities) throws Exception {

                                                    for (GroupModel groupModel : userActivities){
                                                        double qty=Double.parseDouble(quantity.getText().toString());
                                                        double dis=0;
                                                        try {
                                                            dis = Double.parseDouble(discount.getText().toString());
                                                        } catch (NumberFormatException e) {
                                                            e.printStackTrace();
                                                            dis=0;
                                                        }
                                                        double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                        double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis/100;
                                                        double t = total-totals;
                                                        ItemReturn.Amount = t ;
                                                        ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
                                                        ItemReturn.Quantity = quantityfor;
                                                        ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                                        BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                        ItemReturn.Stock="Out";
                                                        ItemReturn.Discount = discountfor;
                                                        ItemReturn.BookId = groupModel.BookNo;
                                                        ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                        ItemReturn.BookName = groupModel.BookName;
                                                        Common.itemReturnRepository.insertToItem(ItemReturn);
                                                    }
                                                }
                                            }));
                                        }



                                    }

                                    startActivity(new Intent(ItemReturnActivity.this, SalesReturnActivity.class));
                                    finish();
                                    //  }
//                                    else{
//                                        double pricesfor = Double.parseDouble(bookMRP.getText().toString());
//                                        final int quantityfor = Integer.parseInt(quantity.getText().toString());
//                                        final double discountfor;
//                                        if (!discount.getText().toString().equals("")) {
//                                            discountfor = Double.parseDouble(discount.getText().toString());
//                                        } else {
//                                            discountfor = 0;
//                                        }
//
//                                        final double totalfor = Double.parseDouble(amount.getText().toString());
//                                        final ItemReturn itemModel = new ItemReturn();
//
//                                        compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
//                                            @Override
//                                            public void accept(List<GroupModel> userActivities) throws Exception {
//
//                                                for (GroupModel groupModel : userActivities){
//                                                    ItemReturn get =Common.itemReturnRepository.getItems(groupModel.BookName);
//                                                    itemModel.Amount = get.Amount+ totalfor;
//                                                    itemModel.Price = get.Price;
//                                                    BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
//                                                    if (bookStocks!=null){
//                                                        if (bookStock.QTY_NUMBER<=bookStocks.QTY_NUMBER){
//                                                            itemModel.Stock="Out";
//                                                        }
//                                                        else {
//                                                            itemModel.Stock="In";
//                                                        }
//                                                    }
//                                                    itemModel.Quantity = get.Quantity+quantityfor;
//                                                    itemModel.Discount = discountfor;
//                                                 //   ItemReturn getq =Common.itemReturnRepository.getItems(bookname.getText().toString());
//                                                    itemModel.BookId = groupModel.BookNo;
//                                                    itemModel.id = get.id;
//                                                    itemModel.BookName = groupModel.BookName;
//                                                    itemModel.BookNameBangla = groupModel.BookNameBangla;
//                                                    Common.itemReturnRepository.updateItem(itemModel);
//                                                }
//                                            }
//                                        }));
//
//                                        startActivity(new Intent(ItemReturnActivity.this, SalesReturnActivity.class));
//                                        finish();
//                                    }

                                }
                                else {
                                    Toast.makeText(ItemReturnActivity.this, "Quantity Can not be 0", Toast.LENGTH_SHORT).show();

                                }
                       //     }else {
                           //     Toast.makeText(ItemReturnActivity.this, "Amount Can not be 0", Toast.LENGTH_SHORT).show();
//
                         //   }



                        }

                        else {
                            Toast.makeText(ItemReturnActivity.this, "Quantity Field is more than stock", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                    //}
                   // else
                   // {
                      //  Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                   // }
                }
                else {
                    Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }



            }
        });
        btn_new_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (bookStock != null) {
                        ///  if (bookStock.QTY_NUMBER>0){
                        if (!quantity.getText().toString().equals("") && !amount.getText().toString().equals("")) {
                            double value = Double.parseDouble(amount.getText().toString());

                            //  if (value>0){
                            double quan = Double.parseDouble(quantity.getText().toString());
                            if (quan > 0) {
                                ItemReturn getItems = Common.itemReturnRepository.getItems(bookname.getText().toString());
                                // if (getItems!=null)
                                // {
                                final double pricesfor = Double.parseDouble(bookMRP.getText().toString());
                                final int quantityfor = Integer.parseInt(quantity.getText().toString());
                                final double discountfor;
                                if (!discount.getText().toString().equals("")) {
                                    discountfor = Double.parseDouble(discount.getText().toString());
                                } else {
                                    discountfor = 0;
                                }

                                double totalfor = Double.parseDouble(amount.getText().toString());
                                String s1 = bookname.getText().toString().substring(0, bookname.getText().toString().length() - 7);

                                final ItemReturn items1 = Common.itemReturnRepository.getItems(s1);
                                final ItemReturn ItemReturn = new ItemReturn();
                                if (items1 != null) {
                                    final double finalTotalfor1 = totalfor;
                                    if (book.BOOK_GROUP_ID.equals("0")) {
                                        double qty = Double.parseDouble(quantity.getText().toString());
                                        double dis = 0;
                                        try {
                                            dis = Double.parseDouble(discount.getText().toString());
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            dis = 0;
                                        }
                                        double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                        double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis / 100;
                                        double t = total - totals;
                                        // ItemReturn.Amount = t +items1.Amount;
                                        ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
                                        ItemReturn.Quantity = quantityfor + items1.Quantity;
                                        ItemReturn.Discount = discountfor;
                                        ItemReturn.BookId = book.BookNo;
                                        ItemReturn.BookName = book.BookName;
                                        ItemReturn.BookNameBangla = book.BookNameBangla;
                                        ItemReturn it = Common.itemReturnRepository.getItems(book.BookName);
                                        ItemReturn.id = it.id;
                                        ItemReturn.Amount = t + it.Amount;

                                        BookStock bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);
                                        ItemReturn.Stock = "Out";
                                        Common.itemReturnRepository.updateItem(ItemReturn);
                                    } else {
                                        compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                            @Override
                                            public void accept(List<GroupModel> userActivities) throws Exception {


                                                for (GroupModel groupModel : userActivities) {
                                                    double qty = Double.parseDouble(quantity.getText().toString());
                                                    double dis = 0;
                                                    try {
                                                        dis = Double.parseDouble(discount.getText().toString());
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                        dis = 0;
                                                    }
                                                    double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                    double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis / 100;
                                                    double t = total - totals;
                                                    //ItemReturn.Amount = t +items1.Amount;
                                                    ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
                                                    ItemReturn.Quantity = quantityfor + items1.Quantity;
                                                    ItemReturn.Discount = discountfor;
                                                    ItemReturn.BookId = groupModel.BookNo;
                                                    ItemReturn.BookName = groupModel.BookName;
                                                    ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                    ItemReturn it = Common.itemReturnRepository.getItems(groupModel.BookName);
                                                    ItemReturn.id = it.id;
                                                    ItemReturn.Amount = t + it.Amount;
                                                    BookStock bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                    ItemReturn.Stock = "Out";
                                                    Common.itemReturnRepository.updateItem(ItemReturn);
                                                }
                                            }
                                        }));
                                    }


                                } else {
                                    final double finalTotalfor = totalfor;
                                    if (book.BOOK_GROUP_ID.equals("0")) {
                                        double qty = Double.parseDouble(quantity.getText().toString());
                                        double dis = 0;
                                        try {
                                            dis = Double.parseDouble(discount.getText().toString());
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            dis = 0;
                                        }
                                        double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                        double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis / 100;
                                        double t = total - totals;
                                        ItemReturn.Amount = t;
                                        ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
                                        ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                        BookStock bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);
                                        ItemReturn.Stock = "Out";
                                        ItemReturn.Quantity = quantityfor;
                                        ItemReturn.Discount = discountfor;
                                        ItemReturn.BookId = book.BookNo;
                                        ItemReturn.BookNameBangla = book.BookNameBangla;
                                        ItemReturn.BookName = book.BookName;
                                        Common.itemReturnRepository.insertToItem(ItemReturn);
                                    } else {
                                        compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                            @Override
                                            public void accept(List<GroupModel> userActivities) throws Exception {

                                                for (GroupModel groupModel : userActivities) {
                                                    double qty = Double.parseDouble(quantity.getText().toString());
                                                    double dis = 0;
                                                    try {
                                                        dis = Double.parseDouble(discount.getText().toString());
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                        dis = 0;
                                                    }
                                                    double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                    double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis / 100;
                                                    double t = total - totals;
                                                    ItemReturn.Amount = t;
                                                    ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
                                                    ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                                    BookStock bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                    ItemReturn.Stock = "Out";
                                                    ItemReturn.Quantity = quantityfor;
                                                    ItemReturn.Discount = discountfor;
                                                    ItemReturn.BookId = groupModel.BookNo;
                                                    ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                    ItemReturn.BookName = groupModel.BookName;
                                                    Common.itemReturnRepository.insertToItem(ItemReturn);
                                                }
                                            }
                                        }));
                                    }


                                }

                                Intent intent = new Intent(ItemReturnActivity.this, SalesReturnActivity.class);
                                intent.putExtra("value", "valueNew");
                                startActivity(intent);
                                finish();
                                //  }
//                                    else
//                                        {
//                                        double pricesfor = Double.parseDouble(bookMRP.getText().toString());
//                                        final int quantityfor = Integer.parseInt(quantity.getText().toString());
//                                        final double discountfor;
//                                        if (!discount.getText().toString().equals("")) {
//                                            discountfor = Double.parseDouble(discount.getText().toString());
//                                        } else {
//                                            discountfor = 0;
//                                        }
//
//                                        final double totalfor = Double.parseDouble(amount.getText().toString());
//                                        final ItemReturn itemModel = new ItemReturn();
//                                        compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
//                                            @Override
//                                            public void accept(List<GroupModel> userActivities) throws Exception
//                                            {
//
//                                                for (GroupModel groupModel : userActivities){
//                                                    ItemReturn get=Common.itemReturnRepository.getItems(groupModel.BookName);
//                                                    itemModel.Amount = get.Amount+ totalfor;
//                                                    itemModel.Price = get.Price;
//                                                    itemModel.Quantity = get.Quantity+quantityfor;
//                                                    itemModel.Discount = discountfor;
//                                                    BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
//                                                    if (bookStocks!=null){
//                                                        if (bookStock.QTY_NUMBER<=bookStocks.QTY_NUMBER){
//                                                            itemModel.Stock="Out";
//                                                        }
//                                                        else {
//                                                            itemModel.Stock="In";
//                                                        }
//                                                    }
//                                                    itemModel.BookId = groupModel.BookNo;
//                                                    itemModel.id = get.id;
//                                                    itemModel.BookName = groupModel.BookName;
//                                                    itemModel.BookNameBangla = groupModel.BookNameBangla;
//                                                    Common.itemReturnRepository.updateItem(itemModel);
//                                                }
//                                            }
//                                        }));
//
//                                        Intent intent = new Intent(ItemReturnActivity.this, SalesReturnActivity.class);
//                                        intent.putExtra("value", "value");
//                                        startActivity(intent);
//                                        finish();
//                                    }

                            } else {
                                Toast.makeText(ItemReturnActivity.this, "Quantity Can not be 0", Toast.LENGTH_SHORT).show();

                            }

                            // }
                            // else {
                            //       Toast.makeText(ItemReturnActivity.this, "Amount Can not be 0", Toast.LENGTH_SHORT).show();

                            //   }

                        } else {
                            Toast.makeText(ItemReturnActivity.this, "Quantity Field is more than stock", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                        //   }
                        //   else
                        //  {
                        //      Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                        //  }
                    } else {
                        Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                    }


                }

        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (bookStock!=null){
                  ///  if (bookStock.QTY_NUMBER>0){
                        if (!quantity.getText().toString().equals("")&& !amount.getText().toString().equals("")) {
                            double value=Double.parseDouble(amount.getText().toString());

                          //  if (value>0){
                                double quan= Double.parseDouble(quantity.getText().toString());
                                if (quan>0){
                                    ItemReturn getItems =Common.itemReturnRepository.getItems(bookname.getText().toString());
                                    // if (getItems!=null)
                                    // {
                                    final double pricesfor = Double.parseDouble(bookMRP.getText().toString());
                                    final int quantityfor = Integer.parseInt(quantity.getText().toString());
                                    final double discountfor;
                                    if (!discount.getText().toString().equals("")) {
                                        discountfor = Double.parseDouble(discount.getText().toString());
                                    } else {
                                        discountfor = 0;
                                    }

                                    double totalfor = Double.parseDouble(amount.getText().toString());
                                    String s1=bookname.getText().toString().substring(0,bookname.getText().toString().length()-7);

                                    final ItemReturn items1 =Common.itemReturnRepository.getItems(s1);
                                    final ItemReturn ItemReturn = new ItemReturn();
                                    if (items1!=null){
                                        final double finalTotalfor1 = totalfor;
                                        if (book.BOOK_GROUP_ID.equals("0")){
                                            double qty=Double.parseDouble(quantity.getText().toString());
                                            double dis=0;
                                            try {
                                                dis = Double.parseDouble(discount.getText().toString());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                                dis=0;
                                            }
                                            double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                            double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis/100;
                                            double t = total-totals;
                                           // ItemReturn.Amount = t +items1.Amount;
                                            ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
                                            ItemReturn.Quantity = quantityfor+items1.Quantity;
                                            ItemReturn.Discount = discountfor;
                                            ItemReturn.BookId = book.BookNo;
                                            ItemReturn.BookName = book.BookName;
                                            ItemReturn.BookNameBangla = book.BookNameBangla;
                                            ItemReturn it =Common.itemReturnRepository.getItems(book.BookName);
                                            ItemReturn.id=it.id;
                                            ItemReturn.Amount = t +it.Amount;

                                            BookStock  bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);
                                            ItemReturn.Stock="Out";
                                            Common.itemReturnRepository.updateItem(ItemReturn);
                                        }
                                        else {
                                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                                @Override
                                                public void accept(List<GroupModel> userActivities) throws Exception {


                                                    for (GroupModel groupModel:userActivities){
                                                        double qty=Double.parseDouble(quantity.getText().toString());
                                                        double dis=0;
                                                        try {
                                                            dis = Double.parseDouble(discount.getText().toString());
                                                        } catch (NumberFormatException e) {
                                                            e.printStackTrace();
                                                            dis=0;
                                                        }
                                                        double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                        double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis/100;
                                                        double t = total-totals;
                                                        //ItemReturn.Amount = t +items1.Amount;
                                                        ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
                                                        ItemReturn.Quantity = quantityfor+items1.Quantity;
                                                        ItemReturn.Discount = discountfor;
                                                        ItemReturn.BookId = groupModel.BookNo;
                                                        ItemReturn.BookName = groupModel.BookName;
                                                        ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                        ItemReturn it =Common.itemReturnRepository.getItems(groupModel.BookName);
                                                        ItemReturn.id=it.id;
                                                        ItemReturn.Amount = t +it.Amount;
                                                        BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                        ItemReturn.Stock="Out";
                                                        Common.itemReturnRepository.updateItem(ItemReturn);
                                                    }
                                                }
                                            }));
                                        }


                                    }
                                    else {
                                        final double finalTotalfor = totalfor;
                                        if (book.BOOK_GROUP_ID.equals("0")){
                                            double qty=Double.parseDouble(quantity.getText().toString());
                                            double dis=0;
                                            try {
                                                dis = Double.parseDouble(discount.getText().toString());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                                dis=0;
                                            }
                                            double total = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty);
                                            double totals = (Double.parseDouble(book.BOOK_FACE_VALUE) * qty) * dis/100;
                                            double t = total-totals;
                                            ItemReturn.Amount = t ;
                                            ItemReturn.Price = Double.parseDouble(book.BOOK_FACE_VALUE);
                                            ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                            BookStock  bookStocks = Common.bookStockRepository.getBookStock(book.BookNo);
                                            ItemReturn.Stock="Out";
                                            ItemReturn.Quantity = quantityfor;
                                            ItemReturn.Discount = discountfor;
                                            ItemReturn.BookId = book.BookNo;
                                            ItemReturn.BookNameBangla = book.BookNameBangla;
                                            ItemReturn.BookName = book.BookName;
                                            Common.itemReturnRepository.insertToItem(ItemReturn);
                                        }
                                        else {
                                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                                @Override
                                                public void accept(List<GroupModel> userActivities) throws Exception {

                                                    for (GroupModel groupModel : userActivities){
                                                        double qty=Double.parseDouble(quantity.getText().toString());
                                                        double dis=0;
                                                        try {
                                                            dis = Double.parseDouble(discount.getText().toString());
                                                        } catch (NumberFormatException e) {
                                                            e.printStackTrace();
                                                            dis=0;
                                                        }
                                                        double total = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty);
                                                        double totals = (Double.parseDouble(groupModel.BOOK_FACE_VALUE) * qty) * dis/100;
                                                        double t = total-totals;
                                                        ItemReturn.Amount = t ;
                                                        ItemReturn.Price = Double.parseDouble(groupModel.BOOK_FACE_VALUE);
                                                        ItemReturn.ValuePrice = Double.parseDouble(bookMRP.getText().toString());
                                                        BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
                                                        ItemReturn.Stock="Out";
                                                        ItemReturn.Quantity = quantityfor;
                                                        ItemReturn.Discount = discountfor;
                                                        ItemReturn.BookId = groupModel.BookNo;
                                                        ItemReturn.BookNameBangla = groupModel.BookNameBangla;
                                                        ItemReturn.BookName = groupModel.BookName;
                                                        Common.itemReturnRepository.insertToItem(ItemReturn);
                                                    }
                                                }
                                            }));
                                        }



                                    }

                                    Intent intent = new Intent(ItemReturnActivity.this, SalesReturnActivity.class);
                                    intent.putExtra("value", "value");
                                    startActivity(intent);
                                    finish();
                                    //  }
//                                    else
//                                        {
//                                        double pricesfor = Double.parseDouble(bookMRP.getText().toString());
//                                        final int quantityfor = Integer.parseInt(quantity.getText().toString());
//                                        final double discountfor;
//                                        if (!discount.getText().toString().equals("")) {
//                                            discountfor = Double.parseDouble(discount.getText().toString());
//                                        } else {
//                                            discountfor = 0;
//                                        }
//
//                                        final double totalfor = Double.parseDouble(amount.getText().toString());
//                                        final ItemReturn itemModel = new ItemReturn();
//                                        compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
//                                            @Override
//                                            public void accept(List<GroupModel> userActivities) throws Exception
//                                            {
//
//                                                for (GroupModel groupModel : userActivities){
//                                                    ItemReturn get=Common.itemReturnRepository.getItems(groupModel.BookName);
//                                                    itemModel.Amount = get.Amount+ totalfor;
//                                                    itemModel.Price = get.Price;
//                                                    itemModel.Quantity = get.Quantity+quantityfor;
//                                                    itemModel.Discount = discountfor;
//                                                    BookStock  bookStocks = Common.bookStockRepository.getBookStock(groupModel.BookNo);
//                                                    if (bookStocks!=null){
//                                                        if (bookStock.QTY_NUMBER<=bookStocks.QTY_NUMBER){
//                                                            itemModel.Stock="Out";
//                                                        }
//                                                        else {
//                                                            itemModel.Stock="In";
//                                                        }
//                                                    }
//                                                    itemModel.BookId = groupModel.BookNo;
//                                                    itemModel.id = get.id;
//                                                    itemModel.BookName = groupModel.BookName;
//                                                    itemModel.BookNameBangla = groupModel.BookNameBangla;
//                                                    Common.itemReturnRepository.updateItem(itemModel);
//                                                }
//                                            }
//                                        }));
//
//                                        Intent intent = new Intent(ItemReturnActivity.this, SalesReturnActivity.class);
//                                        intent.putExtra("value", "value");
//                                        startActivity(intent);
//                                        finish();
//                                    }

                                }
                                else {
                                    Toast.makeText(ItemReturnActivity.this, "Quantity Can not be 0", Toast.LENGTH_SHORT).show();

                                }

                           // }
                           // else {
                         //       Toast.makeText(ItemReturnActivity.this, "Amount Can not be 0", Toast.LENGTH_SHORT).show();

                         //   }

                        } else {
                            Toast.makeText(ItemReturnActivity.this, "Quantity Field is more than stock", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                 //   }
                 //   else
                  //  {
                  //      Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                  //  }
                }
                else {
                    Toast.makeText(ItemReturnActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ItemReturnActivity.this,SalesReturnActivity.class));
        finish();
    }
}
