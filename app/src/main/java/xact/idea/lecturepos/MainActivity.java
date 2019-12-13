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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.SalesDetailsAdapter;
import xact.idea.lecturepos.Database.Datasources.BookRepository;
import xact.idea.lecturepos.Database.Datasources.BookStockRepository;
import xact.idea.lecturepos.Database.Datasources.ChallanDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.ChallanRepositoy;
import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Datasources.LoginRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Datasources.SyncRepository;
import xact.idea.lecturepos.Database.Local.BookDataSources;
import xact.idea.lecturepos.Database.Local.BookStockDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDetailsDataSources;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.Local.LoginDataSource;
import xact.idea.lecturepos.Database.Local.SalesDetailsDataSources;
import xact.idea.lecturepos.Database.Local.SalesMasterDataSources;
import xact.idea.lecturepos.Database.Local.SyncDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.ChallanDetails;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.Database.Model.Sync;
import xact.idea.lecturepos.Model.BookModel;
import xact.idea.lecturepos.Model.BookResponseEntity;
import xact.idea.lecturepos.Model.ChallanDetailsModel;
import xact.idea.lecturepos.Model.ChallanPostEntity;
import xact.idea.lecturepos.Model.ChallanResponseEntity;
import xact.idea.lecturepos.Model.CustomerModel;
import xact.idea.lecturepos.Model.LoginEntity;
import xact.idea.lecturepos.Model.Response;
import xact.idea.lecturepos.Model.SalesModel;
import xact.idea.lecturepos.Model.SyncChallanModel;
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
    LinearLayout linear_sync;
    LinearLayout linear_stock;
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
        linear_sync=findViewById(R.id.linear_sync);
        linear_stock=findViewById(R.id.linear_stock);
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
        linear_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InventoryActivity.class));

            }
        });
        linear_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChallan();
                loadCustomer();

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        initDB();
//        if (Common.customerRepository.size() > 0) {
//
//            loadDepartmentItems();
//
//
//        }else{
//            CustomerModel customer = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00059","Md Abul Khair","Mohadebpur","01741242652");
//            CustomerModel customer1 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00060","Md Abdus Satter","Ghulshan","01641262652");
//            CustomerModel customer2 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00061","Md Saiful Islam","Merul badda","01941242632");
//            CustomerModel customer3 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00062","Md Hakim","Patuakhali","01541642652");
//            CustomerModel customer4 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00063","Md Monirul Islam","Dhaka","01781242689");
//            customerModelList.add(customer);
//            customerModelList.add(customer1);
//            customerModelList.add(customer2);
//            customerModelList.add(customer3);
//            customerModelList.add(customer4);
//            Log.e("list","data"+new Gson().toJson(customerModelList));
//
//            for (CustomerModel cus: customerModelList ){
//                Customer c = new Customer();
//                c.Address=cus.Address;
//                c.Name=cus.Name;
//                c.MobileNumber=cus.MobileNumber;
//                c.StoreId=cus.StoreId;
//                c.RetailerCode=cus.RetailerCode;
//                Common.customerRepository.insertToCustomer(c);
//
//            }
//        }
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
        if (Common.challanDetailsRepository.size() > 0){
           // loadChalanDetails();
        }else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadChalanDetails();
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
        Common.syncRepository = SyncRepository.getInstance(SyncDataSources.getInstance(Common.mainDatabase.syncDao()));
        Common.bookStockRepository = BookStockRepository.getInstance(BookStockDataSources.getInstance(Common.mainDatabase.bookStockDao()));
        Common.challanDetailsRepository = ChallanDetailsRepository.getInstance(ChallanDetailsDataSources.getInstance(Common.mainDatabase.challanDetailsDao()));

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

    private  void loadChalanDetails() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no=SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalanDetails(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanDetailsModel>() {
            @Override
            public void accept(ChallanDetailsModel challanDetailsModel) throws Exception {
                Log.e("challanDetailsModel", "challanDetailsModel" + new Gson().toJson(challanDetailsModel));

                for (ChallanDetailsModel.Data challan : challanDetailsModel.data) {

                    ChallanDetails challanDetails = new ChallanDetails();

                    challanDetails.BOOK_NET_PRICE=challan.BOOK_NET_PRICE;
                    challanDetails.F_BOOK_NO=challan.F_BOOK_NO;
                    challanDetails.CHALLAN_BOOK_QTY=challan.CHALLAN_BOOK_QTY;
                    challanDetails.F_CHALLAN_NO=challan.F_CHALLAN_NO;

                    Common.challanDetailsRepository.insertToChallanDetails(challanDetails);

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
                    challan.receive_date=books.RECEIVE_DAT;
                    challan.IS_RECEIVE=books.IS_RECEIVE;
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
                    challan.IS_BUSY=books.IS_BUSY;
                    challan.BUSY_USER_NO=books.BUSY_USER_NO;
                    challan.BUSY_TIME=books.BUSY_TIME;
                    challan.TOTAL_COMMISION=books.TOTAL_COMMISION;
                    challan.OVERALL_DISCOUNT=books.OVERALL_DISCOUNT;
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
    private  void loadChalanItemsSync() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no=SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalan(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanResponseEntity>() {
            @Override
            public void accept(ChallanResponseEntity challanResponseEntity) throws Exception {
                Log.e("size", "size" + new Gson().toJson(challanResponseEntity));

                for (ChallanResponseEntity.Data books : challanResponseEntity.data) {
                    Challan challan = new Challan();
                    Date datess = null;
                    //SimpleDateFormat formatterss = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    SimpleDateFormat formatterss = new SimpleDateFormat("dd-MMM-yy");
                    try {
                        datess= formatterss.parse(books.RECEIVE_DAT);
                        Log.e("currentTime","currentTime"+datess);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Challan challan1 =Common.challanRepositoy.getChallans(books.CHALLAN_NO);
                    if (challan1!=null){

                        Date date1 =null;
                       // SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            date1= formatter1.parse(challan1.receive_date);
                            Log.e("currentTime","currentTime"+date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (datess.after(date1)){
//                            Date date3 = new SimpleDateFormat("dd-MMM-yy").parse(books.CHALLAN_DATE);


                            challan.receive_date=books.RECEIVE_DAT;
                            challan.IS_RECEIVE=books.IS_RECEIVE;
                            challan.CHALLAN_NO=books.CHALLAN_NO;
                            challan.id=challan1.id;

                            Common.challanRepositoy.updateChallan(challan);

                        }
                        else {

                        }
                    }
                    else{
                        Date date1 = new SimpleDateFormat("dd-MMM-yy").parse(books.CHALLAN_DATE);
                        challan.CHALLAN_CODE=books.CHALLAN_CODE;
                        challan.Date=date1;
                        challan.receive_date=books.RECEIVE_DAT;
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
                        challan.IS_BUSY=books.IS_BUSY;
                        challan.BUSY_USER_NO=books.BUSY_USER_NO;
                        challan.BUSY_TIME=books.BUSY_TIME;
                        challan.TOTAL_COMMISION=books.TOTAL_COMMISION;
                        challan.OVERALL_DISCOUNT=books.OVERALL_DISCOUNT;
                        challan.IS_BOOKED=books.IS_BOOKED;
                        Common.challanRepositoy.insertToChallan(challan);
                    }


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


    private  void loadChallan() {

        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                List<SyncChallanModel.Sync> syncs = new ArrayList<>();
                for (Challan challan :customers){
                    SyncChallanModel.Sync syncChallanModel = new SyncChallanModel.Sync();
                    syncChallanModel.receive_date=challan.receive_date;
                    syncChallanModel.is_receive=challan.IS_RECEIVE;
                    syncChallanModel.challan_no=challan.CHALLAN_NO;
                    syncs.add(syncChallanModel);
                }
                SyncChallanModel s = new SyncChallanModel();
                s.data=syncs;
                compositeDisposable.add(mService.syncChalan(s).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response loginEntity) throws Exception {
                        Log.e("data","Sdfds"+loginEntity.status_code);
                        loadChalanItemsSync();
                    }
                }));

                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm a");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);

                Sync name=Common.syncRepository.valueFor("challan");
                if (name==null){
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE=SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("challan");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="challan";
                    Common.syncRepository.insertToSync(sync);
                }
                else{
                    Sync sync = new Sync();
                    sync.id=name.id;
                    sync.CUSTOMER_CODE=name.CUSTOMER_CODE;
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("challan");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="challan";
                    Common.syncRepository.updateSync(sync);
                }

                Log.e("currentTime","currentTime"+currentTime);
            }
        }));

    }

    private  void loadCustomer() {

        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> userActivities) throws Exception
            {


                 List<SalesModel.SalesMaster> syncs = new ArrayList<>();

                for (SalesMaster sales :userActivities ){
                 final  SalesModel.SalesMaster salesMaster = new SalesModel.SalesMaster();

                    salesMaster.StoreId=sales.StoreId;
                    salesMaster.Device=sales.Device;
                    salesMaster.Discount=String.valueOf(sales.Discount);
                    salesMaster.PayMode=sales.PayMode;
                    salesMaster.NetValue=String.valueOf(sales.NetValue);
                    salesMaster.RetailCode=sales.RetailCode;
                    salesMaster.InvoiceAmount=String.valueOf(sales.InvoiceAmount);
                    salesMaster.InvoiceDate=sales.InvoiceDates;
                    salesMaster.InvoiceId=sales.InvoiceId;
                    salesMaster.InvoiceNumber=sales.InvoiceNumber;
                    salesMaster.Note=sales.Note;
                    int value=Common.syncRepository.maxValue("sales_mst");
                    salesMaster.UpdNo= String.valueOf(value+1);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    String currentDate = formatter.format(date);
                    SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                    Date dates = new Date(System.currentTimeMillis());
                    String currentTime = formatters.format(dates);
                    salesMaster.UpdDate= currentDate+" "+currentTime;
                    salesMaster.Phone=sales.PhoneNumber;
                    List<SalesModel.SalesMaster.SalesDetails> getList= getList(sales.id);

                    salesMaster.salesDetails=getList;
                    syncs.add(salesMaster);




                }
                Date dates = new Date(System.currentTimeMillis());
                SalesModel salesModel = new SalesModel();
                salesModel.data=syncs;
                Sync name=Common.syncRepository.valueFor("sales_mst");
                if (name==null){
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE=SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("sales_mst");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="sales_mst";
                    Common.syncRepository.insertToSync(sync);
                }
                else{
                    Sync sync = new Sync();
                    sync.id=name.id;
                    sync.CUSTOMER_CODE=name.CUSTOMER_CODE;
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("sales_mst");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="sales_mst";
                    Common.syncRepository.updateSync(sync);
                }
                Sync names=Common.syncRepository.valueFor("sales_dtl");
                if (names==null){
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE=SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("sales_dtl");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="sales_dtl";
                    Common.syncRepository.insertToSync(sync);
                }
                else{
                    Sync sync = new Sync();
                    sync.id=names.id;
                    sync.CUSTOMER_CODE=names.CUSTOMER_CODE;
                    sync.DEVICE="Mobile";
                    sync.SYNC_DATETIME=dates;
                    int value=Common.syncRepository.maxValue("sales_dtl");
                    sync.SYNC_NUMBER=value+1;
                    sync.TABLE_NAME="sales_dtl";
                    Common.syncRepository.updateSync(sync);
                }
                compositeDisposable.add(mService.syncSales(salesModel).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response loginEntity) throws Exception {
                        Log.e("sales","sales"+loginEntity.status_code);
                      //  loadChalanItemsSync();
                    }
                }));

                Log.e("1","dsd"+new Gson().toJson(salesModel));
            }
        }));

    }

    private List<SalesModel.SalesMaster.SalesDetails> getList(int id) {
        List<SalesModel.SalesMaster.SalesDetails> salesDetails = new ArrayList<>();

        Flowable<List<SalesDetails>> units=  Common.salesDetailsRepository.getSalesDetailsItemById(id);
        for (SalesDetails salesDetails1 : units.blockingFirst()){
            SalesModel.SalesMaster.SalesDetails details = new SalesModel.SalesMaster.SalesDetails();
            details.BookId=salesDetails1.BookId;
            details.StoreId=salesDetails1.StoreId;
            details.InvoiceId=salesDetails1.InvoiceIdNew;
            details.MRP=String.valueOf(salesDetails1.MRP);
            details.Discount=String.valueOf(salesDetails1.Discount);
            details.DiscountPc="";
            int value=Common.syncRepository.maxValue("sales_dtl");
            details.UpdNo= String.valueOf(value+1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = formatter.format(date);
            SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
            Date dates = new Date(System.currentTimeMillis());
            String currentTime = formatters.format(dates);
            details.UpdDate= currentDate+" "+currentTime;
            details.TotalAmount=String.valueOf(salesDetails1.TotalAmount);
            details.Quantity=String.valueOf(salesDetails1.Quantity);
            salesDetails.add(details);
        }
        return salesDetails;

    }
//    List<SalesModel.SalesMaster.SalesDetails> salesDetails = new ArrayList<>();
//    private List<SalesModel.SalesMaster.SalesDetails> display(List<SalesDetails> units) {
//
//        return salesDetails;
//
//    }
}
