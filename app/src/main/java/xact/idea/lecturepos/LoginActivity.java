package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Datasources.BookRepository;
import xact.idea.lecturepos.Database.Datasources.BookStockRepository;
import xact.idea.lecturepos.Database.Datasources.ChallanDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.ChallanRepositoy;
import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Datasources.ItemRepository;
import xact.idea.lecturepos.Database.Datasources.LoginRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Datasources.SyncRepository;
import xact.idea.lecturepos.Database.Local.BookDataSources;
import xact.idea.lecturepos.Database.Local.BookStockDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDetailsDataSources;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.Local.ItemDataSources;
import xact.idea.lecturepos.Database.Local.LoginDataSource;
import xact.idea.lecturepos.Database.Local.SalesDetailsDataSources;
import xact.idea.lecturepos.Database.Local.SalesMasterDataSources;
import xact.idea.lecturepos.Database.Local.SyncDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Login;
import xact.idea.lecturepos.Model.BookResponseEntity;
import xact.idea.lecturepos.Model.LoginEntity;
import xact.idea.lecturepos.Model.LoginPostEntity;
import xact.idea.lecturepos.Retrofit.IRetrofitApi;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.dismissLoadingProgress;
import static xact.idea.lecturepos.Utils.Utils.showLoadingProgress;

public class LoginActivity extends AppCompatActivity {

    Button sign_in;
    EditText edit_text_password;
    EditText edit_text_email;
    ImageView show_pass;
    boolean test = true;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IRetrofitApi mService;
    RelativeLayout rlt_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mService = Common.getApiXact();
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        edit_text_password = findViewById(R.id.edit_text_password);
        edit_text_email = findViewById(R.id.edit_text_email);
        sign_in = findViewById(R.id.sign_in);
        show_pass = findViewById(R.id.show_pass);
        rlt_root = findViewById(R.id.rlt_root);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.broadcastIntent(LoginActivity.this, rlt_root)) {
                    showLoadingProgress(LoginActivity.this);

                    if (!edit_text_email.getText().toString().equals("") && !edit_text_password.getText().toString().equals("")) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = new Date(System.currentTimeMillis());
                        String currentDate = formatter.format(date);
                        SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                        Date dates = new Date(System.currentTimeMillis());
                        String currentTime = formatters.format(dates);
                        LoginPostEntity loginPostEntity = new LoginPostEntity();
                        loginPostEntity.user_id = edit_text_email.getText().toString();
                        loginPostEntity.user_pass = edit_text_password.getText().toString();
                        String androidId = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        String str = android.os.Build.MODEL;
                        String str1 = Build.DEVICE;
                        loginPostEntity.device_id = androidId;
                        loginPostEntity.device_info = str + "" + str1;
                        loginPostEntity.login_time = currentDate + " " + currentTime;
                        loginPostEntity.device_ime = " ";
                        Log.e("ff", "dgg" + Common.loginRepository.size());
                        if (Common.loginRepository.size() > 0) {

                            Login login = Common.loginRepository.getLoginUser(loginPostEntity.user_id, loginPostEntity.user_pass);
                            if (login != null) {
                                SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_ID, login.USER_ID + "");
                                SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_NAME, login.CUSTOMER_NAME + "");
                                SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_NAME, login.CUSTOMER_ADDRESS + "");
                                if (SharedPreferenceUtil.getSync(LoginActivity.this).equals("green")) {
                                    SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_SYNC, "green");
                                } else {
                                    SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_SYNC, "gray");
                                }


                                SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime + "");

                                startActivity(new Intent(LoginActivity.this, OnBoardingActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Username and Password Incorrect", Toast.LENGTH_SHORT).show();
                                dismissLoadingProgress();
                            }
                        } else {
                            Log.e("ff", "dgg" + new Gson().toJson(loginPostEntity));
                            compositeDisposable.add(mService.Login(loginPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<LoginEntity>() {
                                @Override
                                public void accept(LoginEntity loginEntity) throws Exception {
                                    Log.e("ff", "dgg" + new Gson().toJson(loginEntity));
                                    if (loginEntity.data.USER_ID == null) {
                                        Toast.makeText(LoginActivity.this, "Username and Password Incorrect", Toast.LENGTH_SHORT).show();
                                        dismissLoadingProgress();
                                    } else {
//                                        if (Common.bookRepository.size() > 0) {
//
//                                        } else {
//                                            if (Utils.broadcastIntent(LoginActivity.this, rlt_root)) {
//                                                loadBookItems();
//                                            } else {
//                                                Snackbar snackbar = Snackbar
//                                                        .make(rlt_root, "No Internet", Snackbar.LENGTH_LONG);
//                                                snackbar.show();
//                                            }
//
//                                        }
                                        SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_ID, loginEntity.user_id + "");
                                        SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_NAME, loginEntity.data.CUSTOMER_NAME + "");
                                        SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_ADDRESS, loginEntity.data.CUSTOMER_ADDRESS + "");
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                        Date date = new Date(System.currentTimeMillis());
                                        String currentDate = formatter.format(date);
                                        SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                                        Date dates = new Date(System.currentTimeMillis());
                                        String currentTime = formatters.format(dates);
                                        SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_SYNC, "gray");
                                        SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime + "");

                                        Login login = new Login();
                                        login.ACTIVE = loginEntity.data.ACTIVE;
                                        login.USER_ID = loginEntity.data.USER_ID;
                                        login.PASSWORD = loginEntity.data.PASSWORD;
                                        login.DEVICE = loginEntity.data.DEVICE;
                                        login.CUSTOMER_NAME = loginEntity.data.CUSTOMER_NAME;
                                        login.CUSTOMER_ADDRESS = loginEntity.data.CUSTOMER_ADDRESS;
                                        Common.loginRepository.insertToLogin(login);
                                        startActivity(new Intent(LoginActivity.this, OnBoardingActivity.class));
                                        finish();
                                        dismissLoadingProgress();
                                    }


                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    dismissLoadingProgress();
                                    Log.e("ff", "dgg" + throwable.getMessage());

                                }
                            }));
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "Please Fill All Data", Toast.LENGTH_SHORT).show();
                        dismissLoadingProgress();
                    }

                } else {
                    Snackbar snackbar = Snackbar
                            .make(rlt_root, "No Internet", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
        edit_text_password.addTextChangedListener(new

                                                          TextWatcher() {
                                                              @Override
                                                              public void afterTextChanged(Editable mEdit) {
                                                                  show_pass.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                                                                  //  edit_text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                                              }

                                                              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                              }

                                                              public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                              }
                                                          });
        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPosition = edit_text_password.getSelectionStart();

                if (test) {
                    Log.e("show", "show");

                    test = false;
                    edit_text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_pass.setImageDrawable(getResources().getDrawable(R.drawable.hidden_password));

                } else {
                    Log.e("hidden", "hidden");
                    show_pass.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                    test = true;
                    edit_text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                edit_text_password.setSelection(cursorPosition);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDB();
    }

    private void initDB() {
        Common.mainDatabase = MainDatabase.getInstance(this);
        Common.customerRepository = CustomerRepository.getInstance(CustomerDataSources.getInstance(Common.mainDatabase.customerDao()));
        Common.bookRepository = BookRepository.getInstance(BookDataSources.getInstance(Common.mainDatabase.bookDao()));
        Common.salesDetailsRepository = SalesDetailsRepository.getInstance(SalesDetailsDataSources.getInstance(Common.mainDatabase.saleDetailsDao()));
        Common.salesMasterRepository = SalesMasterRepository.getInstance(SalesMasterDataSources.getInstance(Common.mainDatabase.saleMastersDao()));
        Common.challanRepositoy = ChallanRepositoy.getInstance(ChallanDataSources.getInstance(Common.mainDatabase.challanDao()));
        Common.loginRepository = LoginRepository.getInstance(LoginDataSource.getInstance(Common.mainDatabase.loginDao()));
        Common.syncRepository = SyncRepository.getInstance(SyncDataSources.getInstance(Common.mainDatabase.syncDao()));
        Common.bookStockRepository = BookStockRepository.getInstance(BookStockDataSources.getInstance(Common.mainDatabase.bookStockDao()));
        Common.challanDetailsRepository = ChallanDetailsRepository.getInstance(ChallanDetailsDataSources.getInstance(Common.mainDatabase.challanDetailsDao()));
        Common.itemRepository = ItemRepository.getInstance(ItemDataSources.getInstance(Common.mainDatabase.itemDao()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadBookItems() {
        showLoadingProgress(LoginActivity.this);
        compositeDisposable.add(mService.getBook().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<BookResponseEntity>() {
            @Override
            public void accept(BookResponseEntity bookResponseEntity) throws Exception {
                Log.e("size", "size" + new Gson().toJson(bookResponseEntity));

                for (BookResponseEntity.Data books : bookResponseEntity.data) {
                    Book book = new Book();
                    book.BookPrice = books.BOOK_NET_PRICE;
                    book.BookName = books.BOOK_NAME;
                    book.BookCode = books.BOOK_CODE;
                    book.BookNo = books.BOOK_NO;
                    book.BOOK_SPECIMEN_CODE = books.BOOK_SPECIMEN_CODE;
                    book.BOOK_NET_PRICE = books.BOOK_NET_PRICE;
                    book.BARCODE_NUMBER = books.BARCODE_NUMBER;
                    book.BOOK_SELLING_CODE = books.BOOK_SELLING_CODE;
                    book.BookNameBangla = books.BOOK_NAME_B;
                    book.BOOK_FACE_VALUE = books.BOOK_FACE_VALUE;
                    book.F_BOOK_EDITION_NO = books.F_BOOK_EDITION_NO;
                    Common.bookRepository.insertToBook(book);

                }

                //  downBookStockDetails();
                dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                dismissLoadingProgress();
            }
        }));

    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}
