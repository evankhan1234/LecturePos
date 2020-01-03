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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;
import xact.idea.lecturepos.Model.GroupModel;
import xact.idea.lecturepos.Model.ItemModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class ItemAdjustmentActivity extends AppCompatActivity {
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
    Button btn_update;
    ImageView btn_header_back;
    String sessionId;
    String position;
    BookStock bookStock;
    Book book;
    RadioButton radioCash;
    RadioButton radioCredit;
    RadioGroup radioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_adjustment);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        btn_header_back = findViewById(R.id.btn_header_back);
        btn_new = findViewById(R.id.btn_new);
        btn_update = findViewById(R.id.btn_update);
        bookname = findViewById(R.id.bookname);
        price = findViewById(R.id.price);
        radioCash = findViewById(R.id.radioCash);
        radioCredit = findViewById(R.id.radioCredit);
        bookMRP = findViewById(R.id.bookMRP);
        radioLogin = findViewById(R.id.radioLogin);
        booknameBangla = findViewById(R.id.booknameBangla);
        quantity = findViewById(R.id.quantity);
        discount = findViewById(R.id.discount);
        amount = findViewById(R.id.amount);
        save = findViewById(R.id.save);
        btn_new = findViewById(R.id.btn_new);
        bookQuantity = findViewById(R.id.bookQuantity);
        quantity.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(quantity, InputMethodManager.SHOW_IMPLICIT);
        sessionId = getIntent().getStringExtra("EXTRA_SESSION");
        Log.e("seesion", "dsd" + sessionId);
        if (sessionId.equals("update")) {
            btn_update.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            btn_new.setVisibility(View.GONE);
            position = getIntent().getStringExtra("id");
            Log.e("position", "position" + position);
            ItemAdjustment itemModel = Common.itemAdjustmentRepository.getItems(position);
            if (itemModel != null) {
                bookname.setText(itemModel.BookName);
                quantity.setText(String.valueOf(itemModel.Quantity));

                booknameBangla.setText(String.valueOf(itemModel.BookNameBangla));
                bookStock = Common.bookStockRepository.getBookStock(itemModel.BookId);
                if (bookStock != null) {
                    bookQuantity.setText(String.valueOf(bookStock.QTY_NUMBER));
                } else {

                }
            }

        } else {
            btn_update.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            btn_new.setVisibility(View.VISIBLE);
            Book books = Common.bookRepository.getBook(sessionId);
            book = Common.bookRepository.getBookItemFor(sessionId, books.BOOK_GROUP_ID);
            if (book == null) {
                book = Common.bookRepository.getBook(sessionId);
            }
            bookStock = Common.bookStockRepository.getBookStock(book.BookNo);
            if (bookStock != null) {
                bookQuantity.setText(String.valueOf(bookStock.QTY_NUMBER));
            } else {

            }


            if (book != null) {
                bookname.setText(book.BookName + " (20" + book.F_BOOK_EDITION_NO + ")");

                String data = Utils.getValue("20" + book.F_BOOK_EDITION_NO);
                booknameBangla.setText(book.BookNameBangla + " (" + data + ")");
                //  booknameBangla.setText(book.BookNameBangla);
                price.setText(String.valueOf(book.BOOK_NET_PRICE));
                bookMRP.setText(String.valueOf(book.BOOK_FACE_VALUE));
            } else {
                Toast.makeText(this, "No Books Found", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemAdjustmentActivity.this, AdjustmentActivity.class));
                finish();
            }

        }

        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ItemAdjustmentActivity.this, AdjustmentActivity.class));
                finish();
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookStock != null) {
                    if (bookStock.QTY_NUMBER > 0) {
                        if (!quantity.getText().toString().equals("")) {
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());
                            ItemAdjustment itemModel = new ItemAdjustment();
                            ItemAdjustment itemModels = Common.itemAdjustmentRepository.getItems(position);
                            itemModel.BookId = itemModels.BookId;
                            itemModel.id = itemModels.id;
                            itemModel.InOut = pay;
                            int quantityfor = Integer.parseInt(quantity.getText().toString());
                            itemModel.Quantity = quantityfor;
                            itemModel.BookName = bookname.getText().toString();
                            itemModel.BookNameBangla = booknameBangla.getText().toString();
                            Common.itemAdjustmentRepository.updateItem(itemModel);
                            startActivity(new Intent(ItemAdjustmentActivity.this, AdjustmentActivity.class));
                            finish();


                        } else {
                            Toast.makeText(ItemAdjustmentActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                            // Constant.arrayList.set(1,itemModel);
                        }
                    } else {
                        Toast.makeText(ItemAdjustmentActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(ItemAdjustmentActivity.this, "Not Enough Stocck", Toast.LENGTH_SHORT).show();
                }


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                if (!quantity.getText().toString().equals("")) {

                    String s1 = bookname.getText().toString().substring(0, bookname.getText().toString().length() - 7);

                    final ItemAdjustment ItemAdjustment1 = Common.itemAdjustmentRepository.getItems(s1);
                    final ItemAdjustment ItemAdjustment = new ItemAdjustment();

                    if (ItemAdjustment1 != null) {
                        if (book.BOOK_GROUP_ID.equals("0")) {
                            int quantityfor = Integer.parseInt(quantity.getText().toString());
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());

                            ItemAdjustment.Quantity = quantityfor;
                            ItemAdjustment.InOut = pay;
                            ItemAdjustment.BookId = book.BookNo;
                            ItemAdjustment.BookName = book.BookName;
                            ItemAdjustment.BookNameBangla = book.BookNameBangla;
                            ItemAdjustment it = Common.itemAdjustmentRepository.getItems(book.BookName);
                            ItemAdjustment.id = it.id;
                            Common.itemAdjustmentRepository.updateItem(ItemAdjustment);
                        } else {
                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                @Override
                                public void accept(List<GroupModel> userActivities) throws Exception {

                                    for (GroupModel groupModel : userActivities) {
                                        int quantityfor = Integer.parseInt(quantity.getText().toString());
                                        int selectedId = radioLogin.getCheckedRadioButtonId();
                                        RadioButton radioSexButton = findViewById(selectedId);

                                        String pay = String.valueOf(radioSexButton.getText());

                                        ItemAdjustment.Quantity = quantityfor;
                                        ItemAdjustment.InOut = pay;
                                        ItemAdjustment.BookId = groupModel.BookNo;
                                        ItemAdjustment.BookName = groupModel.BookName;
                                        ItemAdjustment.BookNameBangla = groupModel.BookNameBangla;
                                        ItemAdjustment it = Common.itemAdjustmentRepository.getItems(groupModel.BookName);
                                        ItemAdjustment.id = it.id;
                                        Common.itemAdjustmentRepository.updateItem(ItemAdjustment);
                                    }
                                }
                            }));

                        }

                    } else {

                        if (book.BOOK_GROUP_ID.equals("0")){
                            int quantityfor = Integer.parseInt(quantity.getText().toString());
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());
                            ItemAdjustment.Quantity = quantityfor;
                            ItemAdjustment.InOut = pay;
                            ItemAdjustment.BookId = book.BookNo;
                            ItemAdjustment.BookNameBangla = book.BookNameBangla;
                            ItemAdjustment.BookName = book.BookName;
                            Common.itemAdjustmentRepository.insertToItem(ItemAdjustment);

                        }
                        else {
                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                @Override
                                public void accept(List<GroupModel> userActivities) throws Exception {

                                    for (GroupModel groupModel : userActivities) {
                                        int quantityfor = Integer.parseInt(quantity.getText().toString());
                                        int selectedId = radioLogin.getCheckedRadioButtonId();
                                        RadioButton radioSexButton = findViewById(selectedId);

                                        String pay = String.valueOf(radioSexButton.getText());
                                        ItemAdjustment.Quantity = quantityfor;
                                        ItemAdjustment.InOut = pay;
                                        ItemAdjustment.BookId = groupModel.BookNo;
                                        ItemAdjustment.BookNameBangla = groupModel.BookNameBangla;
                                        ItemAdjustment.BookName = groupModel.BookName;
                                        Common.itemAdjustmentRepository.insertToItem(ItemAdjustment);
                                    }
                                }
                            }));
                        }



                    }
                    startActivity(new Intent(ItemAdjustmentActivity.this, AdjustmentActivity.class));
                    finish();

                } else {
                    Toast.makeText(ItemAdjustmentActivity.this, "Quantity Field is more than stock", Toast.LENGTH_SHORT).show();
                    // Constant.arrayList.set(1,itemModel);
                }


            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (!quantity.getText().toString().equals("")) {


                    String s1 = bookname.getText().toString().substring(0, bookname.getText().toString().length() - 7);

                    final ItemAdjustment ItemAdjustment1 = Common.itemAdjustmentRepository.getItems(s1);
                    final ItemAdjustment ItemAdjustment = new ItemAdjustment();

                    if (ItemAdjustment1 != null) {

                        if (book.BOOK_GROUP_ID.equals("0")){
                            int quantityfor = Integer.parseInt(quantity.getText().toString());
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());
                            ItemAdjustment.InOut = pay;
                            ItemAdjustment.Quantity = quantityfor;
                            ItemAdjustment.BookId = book.BookNo;
                            ItemAdjustment.BookName = book.BookName;
                            ItemAdjustment.BookNameBangla = book.BookNameBangla;
                            ItemAdjustment it = Common.itemAdjustmentRepository.getItems(book.BookName);
                            ItemAdjustment.id = it.id;
                            Common.itemAdjustmentRepository.updateItem(ItemAdjustment);
                        }
                        else {
                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                @Override
                                public void accept(List<GroupModel> userActivities) throws Exception {


                                    for (GroupModel groupModel : userActivities) {
                                        int quantityfor = Integer.parseInt(quantity.getText().toString());
                                        int selectedId = radioLogin.getCheckedRadioButtonId();
                                        RadioButton radioSexButton = findViewById(selectedId);

                                        String pay = String.valueOf(radioSexButton.getText());
                                        ItemAdjustment.InOut = pay;
                                        ItemAdjustment.Quantity = quantityfor;
                                        ItemAdjustment.BookId = groupModel.BookNo;
                                        ItemAdjustment.BookName = groupModel.BookName;
                                        ItemAdjustment.BookNameBangla = groupModel.BookNameBangla;
                                        ItemAdjustment it = Common.itemAdjustmentRepository.getItems(groupModel.BookName);
                                        ItemAdjustment.id = it.id;
                                        Common.itemAdjustmentRepository.updateItem(ItemAdjustment);
                                    }
                                }
                            }));
                        }


                    } else {

                        if (book.BOOK_GROUP_ID.equals("0")){
                            int quantityfor = Integer.parseInt(quantity.getText().toString());
                            int selectedId = radioLogin.getCheckedRadioButtonId();
                            RadioButton radioSexButton = findViewById(selectedId);

                            String pay = String.valueOf(radioSexButton.getText());
                            ItemAdjustment.Quantity = quantityfor;
                            ItemAdjustment.InOut = pay;
                            ItemAdjustment.BookId = book.BookNo;
                            ItemAdjustment.BookNameBangla = book.BookNameBangla;
                            ItemAdjustment.BookName = book.BookName;
                            Common.itemAdjustmentRepository.insertToItem(ItemAdjustment);
                        }
                        else {
                            compositeDisposable.add(Common.bookStockRepository.getGroup(book.BOOK_GROUP_ID).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<GroupModel>>() {
                                @Override
                                public void accept(List<GroupModel> userActivities) throws Exception {

                                    for (GroupModel groupModel : userActivities) {
                                        int quantityfor = Integer.parseInt(quantity.getText().toString());
                                        int selectedId = radioLogin.getCheckedRadioButtonId();
                                        RadioButton radioSexButton = findViewById(selectedId);

                                        String pay = String.valueOf(radioSexButton.getText());
                                        ItemAdjustment.Quantity = quantityfor;
                                        ItemAdjustment.InOut = pay;
                                        ItemAdjustment.BookId = groupModel.BookNo;
                                        ItemAdjustment.BookNameBangla = groupModel.BookNameBangla;
                                        ItemAdjustment.BookName = groupModel.BookName;
                                        Common.itemAdjustmentRepository.insertToItem(ItemAdjustment);
                                    }
                                }
                            }));
                        }



                    }
                    Intent intent = new Intent(ItemAdjustmentActivity.this, AdjustmentActivity.class);
                    intent.putExtra("value", "value");
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(ItemAdjustmentActivity.this, "Quantity Field is required", Toast.LENGTH_SHORT).show();
                    // Constant.arrayList.set(1,itemModel);
                }
            }
        });
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

}
