package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Datasources.BookRepository;
import xact.idea.lecturepos.Database.Datasources.ChallanRepositoy;
import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Datasources.LoginRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Local.BookDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDataSources;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.Local.LoginDataSource;
import xact.idea.lecturepos.Database.Local.SalesDetailsDataSources;
import xact.idea.lecturepos.Database.Local.SalesMasterDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Model.BookModel;
import xact.idea.lecturepos.Model.BookResponseEntity;
import xact.idea.lecturepos.Model.ChallanPostEntity;
import xact.idea.lecturepos.Model.ChallanResponseEntity;
import xact.idea.lecturepos.Model.CustomerModel;
import xact.idea.lecturepos.Retrofit.IRetrofitApi;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.dismissLoadingProgress;
import static xact.idea.lecturepos.Utils.Utils.showLoadingProgress;

public class MainActivity extends AppCompatActivity {

    List<CustomerModel> customerModelList= new ArrayList<>();
    List<BookModel> bookModelList= new ArrayList<>();
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tv_store;
    LinearLayout linear_logout;
    LinearLayout linear_customers;
    LinearLayout linear_invoice;
    LinearLayout linear_notes;
    LinearLayout linear_challan;
    Activity mActivity;
    IRetrofitApi mService;
    RelativeLayout root_rlt_dashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService=Common.getApiXact();
        linear_challan=findViewById(R.id.linear_challan);
        linear_logout=findViewById(R.id.linear_logout);
        linear_customers=findViewById(R.id.linear_customers);
        linear_invoice=findViewById(R.id.linear_invoice);
        linear_notes=findViewById(R.id.linear_notes);
        tv_store=findViewById(R.id.tv_store);
        root_rlt_dashboard=findViewById(R.id.root_rlt_dashboard);
        tv_store.setSelected(true);
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root_rlt_dashboard));
        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showInfoDialog(mActivity);
            }
        });
        linear_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CustomerActivity.class));

            }
        });
        linear_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InvoiceActivity.class));

            }
        });
        linear_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InvoiceListActivity.class));

            }
        });
        linear_challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChallanActivity.class));

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        initDB();
        if (Common.customerRepository.size() > 0) {

            loadDepartmentItems();


        }else{
            CustomerModel customer = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00059","Md Abul Khair","Mohadebpur","01741242652");
            CustomerModel customer1 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00060","Md Abdus Satter","Ghulshan","01641262652");
            CustomerModel customer2 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00061","Md Saiful Islam","Merul badda","01941242632");
            CustomerModel customer3 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00062","Md Hakim","Patuakhali","01541642652");
            CustomerModel customer4 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00063","Md Monirul Islam","Dhaka","01781242689");
            customerModelList.add(customer);
            customerModelList.add(customer1);
            customerModelList.add(customer2);
            customerModelList.add(customer3);
            customerModelList.add(customer4);
            Log.e("list","data"+new Gson().toJson(customerModelList));

            for (CustomerModel cus: customerModelList ){
                Customer c = new Customer();
                c.Address=cus.Address;
                c.Name=cus.Name;
                c.MobileNumber=cus.MobileNumber;
                c.StoreId=cus.StoreId;
                c.RetailerCode=cus.RetailerCode;
                Common.customerRepository.insertToCustomer(c);

            }
        }
        if (Common.bookRepository.size() > 0){
            loadBookItemsFor();
        }else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadBookItems();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
        if (Common.challanRepositoy.size() > 0){
            loadChalanItemsFor();
        }else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
              loadChalanItems();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }
    private void initDB() {
        Common.mainDatabase = MainDatabase.getInstance(this);
        Common.customerRepository = CustomerRepository.getInstance(CustomerDataSources.getInstance(Common.mainDatabase.customerDao()));
        Common.bookRepository = BookRepository.getInstance(BookDataSources.getInstance(Common.mainDatabase.bookDao()));
        Common.salesDetailsRepository = SalesDetailsRepository.getInstance(SalesDetailsDataSources.getInstance(Common.mainDatabase.saleDetailsDao()));
        Common.salesMasterRepository = SalesMasterRepository.getInstance(SalesMasterDataSources.getInstance(Common.mainDatabase.saleMastersDao()));
        Common.challanRepositoy = ChallanRepositoy.getInstance(ChallanDataSources.getInstance(Common.mainDatabase.challanDao()));
        Common.loginRepository = LoginRepository.getInstance(LoginDataSource.getInstance(Common.mainDatabase.loginDao()));

    }
    private  void loadDepartmentItems() {

        compositeDisposable.add(Common.customerRepository.getCustomerItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Customer>>() {
            @Override
            public void accept(List<Customer> departments) throws Exception {
                Log.e("size","size"+new Gson().toJson(departments));
            }
        }));

    }
    private  void loadBookItemsFor() {

        compositeDisposable.add(Common.bookRepository.getBookItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Book>>() {
            @Override
            public void accept(List<Book> departments) throws Exception {
                Log.e("Book","Book"+new Gson().toJson(departments));
            }
        }));

    }
    private  void loadChalanItemsFor() {

        compositeDisposable.add(Common.challanRepositoy.getChallanItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> departments) throws Exception {
                Log.e("Challan","Challan"+new Gson().toJson(departments));
            }
        }));

    }
    private  void loadBookItems() {
        showLoadingProgress(MainActivity.this);
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
                    book.BOOK_NET_PRICE = books.BOOK_NET_PRICE;
                    book.BARCODE_NUMBER = books.BARCODE_NUMBER;
                    book.BOOK_SELLING_CODE = books.BOOK_SELLING_CODE;
                    Common.bookRepository.insertToBook(book);

                }

               dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                dismissLoadingProgress();
            }
        }));

    }

    private  void loadChalanItems() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no=SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalan(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanResponseEntity>() {
            @Override
            public void accept(ChallanResponseEntity challanResponseEntity) throws Exception {
                Log.e("size", "size" + new Gson().toJson(challanResponseEntity));

                for (ChallanResponseEntity.Data books : challanResponseEntity.data) {
                    Challan challan = new Challan();
                    Date date1 = new SimpleDateFormat("dd-MMM-yy").parse(books.CHALLAN_DATE);
                    challan.CHALLAN_CODE=books.CHALLAN_CODE;
                    challan.Date=date1;
                    challan.receive_date=date1;
                    challan.IS_RECEIVE="N";

                    challan.CHALLAN_DATE=books.CHALLAN_DATE;
                    challan.CHALLAN_NO=books.CHALLAN_NO;
                    challan.CHALLAN_QTY=books.CHALLAN_QTY;
                    challan.CHALLAN_SL_NO=books.CHALLAN_SL_NO;
                    challan.F_EMPLOYEE_NO_CHALLAN_BY=books.F_EMPLOYEE_NO_CHALLAN_BY;
                    challan.NO_OF_PACKATE=books.NO_OF_PACKATE;
                    challan.TOTAL_BOOK_COST=books.TOTAL_BOOK_COST;
                    challan.PER_HANDLING_COST=books.PER_HANDLING_COST;
                    challan.PER_PACKAGING_COST=books.PER_PACKAGING_COST;
                    challan.IS_PACKAGING_DUE=books.IS_PACKAGING_DUE;
                    challan.NO_OF_PACKATE=books.NO_OF_PACKATE;
                    challan.TOTAL_PACKAGING_COST=books.TOTAL_PACKAGING_COST;
                    challan.COMPLETED_BOOKING_PKT_QTY=books.COMPLETED_BOOKING_PKT_QTY;
                    challan.F_COMPANY_NO=books.F_COMPANY_NO;
                    challan.F_TRANSPORT_BR_NO=books.F_TRANSPORT_BR_NO;
                    challan.TOTAL_HANDLING_COST=books.TOTAL_HANDLING_COST;
                    challan.IS_HANDLING_DUE=books.TOTAL_HANDLING_COST;
                    challan.TOTAL_VALUE=books.TOTAL_VALUE;
                    challan.COMMENTS=books.COMMENTS;
                    challan.F_BOOK_ORDER_NO=books.F_BOOK_ORDER_NO;
                    challan.F_ORDER_INVOICE_NO=books.F_ORDER_INVOICE_NO;
                    challan.SL_NUM=books.SL_NUM;
                    challan.LAST_ACTION=books.LAST_ACTION;
                    challan.LAST_ACTION_TIME=books.LAST_ACTION_TIME;
                    challan.LAST_ACTION_LOGON_NO=books.LAST_ACTION_LOGON_NO;
                    challan.INSERT_TIME=books.INSERT_TIME;
                    challan.INSERT_USER_NO=books.INSERT_USER_NO;
                    challan.INSERT_LOGON_NO=books.INSERT_LOGON_NO;
                    challan.F_FISCAL_YEAR_NO=books.F_FISCAL_YEAR_NO;
                    challan.F_CUSTOMER_NO=books.F_CUSTOMER_NO;
                    challan.F_GODOWN_NO=books.F_GODOWN_NO;
                    challan.IS_BOOKED=books.IS_BOOKED;
                    Common.challanRepositoy.insertToChallan(challan);
                }
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
