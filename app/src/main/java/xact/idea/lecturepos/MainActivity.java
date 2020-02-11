package xact.idea.lecturepos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import xact.idea.lecturepos.Database.Datasources.ItemAdjustmentRepository;
import xact.idea.lecturepos.Database.Datasources.ItemRepository;
import xact.idea.lecturepos.Database.Datasources.ItemReturnRepository;
import xact.idea.lecturepos.Database.Datasources.LoginRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Datasources.SyncRepository;
import xact.idea.lecturepos.Database.Local.BookDataSources;
import xact.idea.lecturepos.Database.Local.BookStockDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDataSources;
import xact.idea.lecturepos.Database.Local.ChallanDetailsDataSources;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.Local.ItemAdjustmentDataSources;
import xact.idea.lecturepos.Database.Local.ItemDataSources;
import xact.idea.lecturepos.Database.Local.ItemReturnDataSources;
import xact.idea.lecturepos.Database.Local.LoginDataSource;
import xact.idea.lecturepos.Database.Local.SalesDetailsDataSources;
import xact.idea.lecturepos.Database.Local.SalesMasterDataSources;
import xact.idea.lecturepos.Database.Local.SyncDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.BookStock;
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
import xact.idea.lecturepos.Model.CustomerListResponse;
import xact.idea.lecturepos.Model.CustomerModel;
import xact.idea.lecturepos.Model.LoginEntity;
import xact.idea.lecturepos.Model.Response;
import xact.idea.lecturepos.Model.RetailsSyncModel;
import xact.idea.lecturepos.Model.SaleesMasterForModel;
import xact.idea.lecturepos.Model.SalesDetailsForModel;
import xact.idea.lecturepos.Model.SalesDetailsPostEntity;
import xact.idea.lecturepos.Model.SalesModel;
import xact.idea.lecturepos.Model.SalesPostEntity;
import xact.idea.lecturepos.Model.StockResponse;
import xact.idea.lecturepos.Model.SyncChallanModel;
import xact.idea.lecturepos.Retrofit.IRetrofitApi;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.dismissLoadingProgress;
import static xact.idea.lecturepos.Utils.Utils.showLoadingProgress;

public class MainActivity extends AppCompatActivity {

    List<CustomerModel> customerModelList = new ArrayList<>();
    List<BookModel> bookModelList = new ArrayList<>();
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tv_store;
    TextView text_date_time;
    TextView text_all_cap;
    TextView text_publisher_chalan;
    LinearLayout linear_logout;
    LinearLayout linear_customers;
    LinearLayout linear_invoice;
    LinearLayout linear_notes;
    LinearLayout linear_challan;
    LinearLayout linear_sales_return;
    LinearLayout linear_sync;
    LinearLayout linear_stock;
    LinearLayout linear_order;
    LinearLayout linear_stock_adjustment;
    Activity mActivity;
    IRetrofitApi mService;
    RelativeLayout root_rlt_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = Common.getApiXact();
        text_date_time = findViewById(R.id.text_date_time);
        text_all_cap = findViewById(R.id.text_all_cap);
        text_publisher_chalan = findViewById(R.id.text_publisher_chalan);
        linear_sales_return = findViewById(R.id.linear_sales_return);
        linear_challan = findViewById(R.id.linear_challan);
        linear_logout = findViewById(R.id.linear_logout);
        linear_customers = findViewById(R.id.linear_customers);
        linear_invoice = findViewById(R.id.linear_invoice);
        linear_notes = findViewById(R.id.linear_notes);
        linear_sync = findViewById(R.id.linear_sync);
        linear_stock = findViewById(R.id.linear_stock);
        linear_order = findViewById(R.id.linear_order);
        linear_stock_adjustment = findViewById(R.id.linear_stock_adjustment);
        tv_store = findViewById(R.id.tv_store);
        root_rlt_dashboard = findViewById(R.id.root_rlt_dashboard);
        tv_store.setSelected(true);
        mActivity = this;
        String upperString = "(" + SharedPreferenceUtil.getUserID(MainActivity.this) + ") " + SharedPreferenceUtil.getUserName(MainActivity.this);
        text_all_cap.setAllCaps(true);
        text_all_cap.setText(upperString.toUpperCase());
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root_rlt_dashboard));

        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferenceUtil.getSync(MainActivity.this).equals("green")) {
                    showInfoDialogReminder(mActivity);

                } else {
                    Utils.showInfoDialog(mActivity);
                }

            }
        });

        linear_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CustomerActivity.class));

            }
        });
        linear_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linear_stock_adjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AdjustmentListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linear_sales_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SalesReturnListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linear_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InvoiceListActivity.class));

            }
        });
        linear_challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChallanActivity.class));

            }
        });
        linear_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InventoryActivity.class));

            }
        });
        linear_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, InvoicePrintActivity.class);
//                intent.putExtra("customerName","bh");
//                intent.putExtra("sub","150.00");
//                intent.putExtra("invoiceId","1001381191222003");
//                startActivity(intent);
//                finish();

            }
        });
        initDB();

        if (Common.challanDetailsRepository.size() > 0) {
            // loadChalanDetails();
        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadChalanDetails();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }

        if (Common.customerRepository.size() > 0) {

        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
//                Customer customers = new Customer();
//                customers.Address = "Dhaka";
//                customers.ShopName = "Retail Library";
//                customers.RetailerCode = "1001";
//                customers.MobileNumber = "00000000000";
//                customers.UpdateDate = "25-DEC-19";
//                customers.UpdateNo = "0";
//                customers.Name = "Retail Customer";
//                customers.StoreId = SharedPreferenceUtil.getUserID(MainActivity.this);
//                customers.Status = "I";
//
//                Common.customerRepository.insertToCustomer(customers);
                loadCustomers();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
        linear_sync.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                    data = true;
                    loadChallan();

                    loadCustomer();
                    loadCustomerSync();
                    downBookStockDetails();
                    //    loadBookItems();
                    SharedPreferenceUtil.saveShared(MainActivity.this, SharedPreferenceUtil.USER_SYNC, "gray");
                    linear_sync.setBackgroundTintList(getResources().getColorStateList(R.color.back));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    String currentDate = formatter.format(date);
                    SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                    Date dates = new Date(System.currentTimeMillis());
                    String currentTime = formatters.format(dates);
                    text_date_time.setText(currentDate + " " + currentTime);

                    SharedPreferenceUtil.saveShared(MainActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime + "");
                } else {
                    Snackbar snackbar = Snackbar
                            .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });
    }

    public void showInfoDialogReminder(final Context mContext) {

        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_pop_up_reminder, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        Button btn_yes = infoDialog.findViewById(R.id.btn_ok);
        Button btn_no = infoDialog.findViewById(R.id.btn_cancel);
        Button btn_now = infoDialog.findViewById(R.id.btn_now);

        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                SharedPreferenceUtil.removeShared(mContext, SharedPreferenceUtil.TYPE_USER_ID);
                infoDialog.dismiss();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity) mContext).finishAffinity();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        btn_now.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                loadChallan();

                loadCustomer();
                loadCustomerSync();
                downBookStockDetails();
                SharedPreferenceUtil.saveShared(MainActivity.this, SharedPreferenceUtil.USER_SYNC, "gray");
                linear_sync.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = formatter.format(date);
                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);
                text_date_time.setText(currentDate + " " + currentTime);

                SharedPreferenceUtil.saveShared(MainActivity.this, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate + " " + currentTime + "");

                infoDialog.dismiss();
            }
        });
        infoDialog.show();
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferenceUtil.getSync(MainActivity.this).equals("gray")) {
            linear_sync.setBackgroundTintList(getResources().getColorStateList(R.color.back));
        } else {
            linear_sync.setBackgroundTintList(getResources().getColorStateList(R.color.back_green));


        }
        text_date_time.setText(SharedPreferenceUtil.getSyncDateTime(MainActivity.this));
        initDB();
        //downBookStockDetails();
        compositeDisposable.add(Common.challanRepositoy.getList("N").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                Log.e("SDfd", "Dgd" + new Gson().toJson(customers));
                Constant.sizes = customers.size();
                text_publisher_chalan.setText("Publishers Chalan (" + String.valueOf(customers.size()) + ")");
                if (customers.size() > 0) {
                    linear_challan.setBackgroundTintList(getResources().getColorStateList(R.color.back_green));
                }

            }
        }));
        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                Log.e("SDfd", "Dgd" + new Gson().toJson(customers));
                Constant.size = customers.size();

            }
        }));
        if (Common.bookRepository.size() > 0) {
            loadBookItemsFor();
        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadBookItems();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
        if (Common.bookStockRepository.size() > 0) {
            // loadChalanDetails();
        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                downBookStockDetails();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
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
        if (Common.challanRepositoy.size() > 0) {
            loadChalanItemsFor();
        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadChalanItems();
            } else {
                Snackbar snackbar = Snackbar
                        .make(root_rlt_dashboard, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }


        if (Common.salesMasterRepository.size() > 0) {
            // loadChalanDetails();
        } else {
            if (Utils.broadcastIntent(MainActivity.this, root_rlt_dashboard)) {
                loadSalesMaster();

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
        Common.itemRepository = ItemRepository.getInstance(ItemDataSources.getInstance(Common.mainDatabase.itemDao()));
        Common.itemReturnRepository = ItemReturnRepository.getInstance(ItemReturnDataSources.getInstance(Common.mainDatabase.itemReturnDao()));
        Common.itemAdjustmentRepository = ItemAdjustmentRepository.getInstance(ItemAdjustmentDataSources.getInstance(Common.mainDatabase.itemAdjustmentDao()));
    }

    private void loadCustomerSync() {

        compositeDisposable.add(Common.customerRepository.getCustomerItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Customer>>() {
            @Override
            public void accept(List<Customer> departments) throws Exception {
                List<RetailsSyncModel.Data> syncs = new ArrayList<>();
                for (Customer customer : departments) {

                    RetailsSyncModel.Data retailsSyncModel = new RetailsSyncModel.Data();
                    retailsSyncModel.upd_no = customer.UpdateNo;
                    retailsSyncModel.upd_date = customer.UpdateDate;
                    if (customer.Address != null) {
                        retailsSyncModel.address = customer.Address;
                    } else {
                        retailsSyncModel.address = " ";
                    }

                    retailsSyncModel.name = customer.Name;
                    retailsSyncModel.store_id = customer.StoreId;
                    retailsSyncModel.retailer_code = customer.RetailerCode;
                    retailsSyncModel.phone = customer.MobileNumber;
                    retailsSyncModel.library_name = customer.ShopName;
                    retailsSyncModel.status = customer.Status;
                    syncs.add(retailsSyncModel);

                }

                RetailsSyncModel s = new RetailsSyncModel();
                s.data = syncs;

                Log.e("vv", "dfvfd" + new Gson().toJson(s));
                compositeDisposable.add(mService.syncCustomer(s).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response loginEntity) throws Exception {
                        Log.e("ada", "a" + loginEntity.status_code);
                        loadCustomers();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("adasd", "fx" + throwable.getMessage());
                        dismissLoadingProgress();
                    }
                }));

                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm a");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);

                Sync name = Common.syncRepository.valueFor("customer");
                if (name == null) {
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE = SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("customer");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "customer";
                    Common.syncRepository.insertToSync(sync);
                } else {
                    Sync sync = new Sync();
                    sync.id = name.id;
                    sync.CUSTOMER_CODE = name.CUSTOMER_CODE;
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("customer");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "customer";
                    Common.syncRepository.updateSync(sync);
                }

                Log.e("size", "size" + new Gson().toJson(s));
            }
        }));

    }

    private void loadCustomers() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.downCustomer(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<CustomerListResponse>() {
            @Override
            public void accept(CustomerListResponse customerListResponse) throws Exception {
                Log.e("CustomerListResponse", "CustomerListResponse" + new Gson().toJson(customerListResponse));


                for (CustomerListResponse.Data customer : customerListResponse.data) {
                    Customer customerq = Common.customerRepository.getCustomerss(customer.LIBRARY_NAME);
                    if (customerq != null) {

                    } else {
                        int value = customer.PHONE.length();
                        if (customer.PHONE.length() == 11) {
                            Customer customers = new Customer();
                            customers.Address = customer.ADDRESS;
                            customers.ShopName = customer.LIBRARY_NAME;
                            customers.RetailerCode = customer.RETAILER_CODE;
                            customers.MobileNumber = customer.PHONE;
                            customers.UpdateDate = customer.UPD_DATE;
                            customers.UpdateNo = customer.UPD_NO;
                            customers.Name = customer.NAME;
                            customers.StoreId = customer.STORE_ID;
                            customers.Status = customer.STATUS;

                            Common.customerRepository.insertToCustomer(customers);
                        }

                    }


                }

                dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("xdvxc", "fx" + throwable.getMessage());
                dismissLoadingProgress();
            }
        }));
    }

    private void loadBookItemsFor() {

        compositeDisposable.add(Common.bookRepository.getBookItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Book>>() {
            @Override
            public void accept(List<Book> departments) throws Exception {
                Log.e("Book", "Book" + new Gson().toJson(departments));
            }
        }));

    }

    private void loadChalanItemsFor() {

        compositeDisposable.add(Common.challanRepositoy.getChallanItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> departments) throws Exception {
                Log.e("Challan", "Challan" + new Gson().toJson(departments));
            }
        }));

    }

    private void loadBookItems() {
        showLoadingProgress(MainActivity.this);
        compositeDisposable.add(mService.getBook().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<BookResponseEntity>() {
            @Override
            public void accept(BookResponseEntity bookResponseEntity) throws Exception {
                Log.e("number5", "number5" + new Gson().toJson(bookResponseEntity));

                for (BookResponseEntity.Data books : bookResponseEntity.data) {

                    Book books1 = Common.bookRepository.getBookNo(books.BOOK_NO);
                    if (books1 != null) {
                        Book book = new Book();
                        book.BookPrice = books.BOOK_NET_PRICE;
                        book.id = books1.id;
                        book.BookName = books.BOOK_NAME;
                        book.BookCode = books.BOOK_CODE;
                        book.BookNo = books.BOOK_NO;
                        book.BOOK_GROUP_ID = books.BOOK_GROUP_ID;
                        book.BOOK_SPECIMEN_CODE = books.BOOK_SPECIMEN_CODE;
                        book.BOOK_NET_PRICE = books.BOOK_NET_PRICE;
                        book.BARCODE_NUMBER = books.BARCODE_NUMBER;
                        book.BOOK_SELLING_CODE = books.BOOK_SELLING_CODE;
                        book.BookNameBangla = books.BOOK_NAME_B;
                        book.BOOK_FACE_VALUE = books.BOOK_FACE_VALUE;
                        book.F_BOOK_EDITION_NO = books.F_BOOK_EDITION_NO;
                        Common.bookRepository.updateBook(book);
                    } else {
                        Book book = new Book();
                        book.BookPrice = books.BOOK_NET_PRICE;
                        book.BookName = books.BOOK_NAME;
                        book.BookCode = books.BOOK_CODE;
                        book.BookNo = books.BOOK_NO;
                        book.BOOK_GROUP_ID = books.BOOK_GROUP_ID;
                        book.BOOK_SPECIMEN_CODE = books.BOOK_SPECIMEN_CODE;
                        book.BOOK_NET_PRICE = books.BOOK_NET_PRICE;
                        book.BARCODE_NUMBER = books.BARCODE_NUMBER;
                        book.BOOK_SELLING_CODE = books.BOOK_SELLING_CODE;
                        book.BookNameBangla = books.BOOK_NAME_B;
                        book.BOOK_FACE_VALUE = books.BOOK_FACE_VALUE;
                        book.F_BOOK_EDITION_NO = books.F_BOOK_EDITION_NO;
                        Common.bookRepository.insertToBook(book);
                    }


                }

                downBookStockDetails();
                loadSalesMaster();
                dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                dismissLoadingProgress();
            }
        }));

    }

    private void loadChalanDetails() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalanDetails(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanDetailsModel>() {
            @Override
            public void accept(ChallanDetailsModel challanDetailsModel) throws Exception {
                Log.e("challanDetailsModel", "challanDetailsModel" + new Gson().toJson(challanDetailsModel));
                Log.e("challanDetailsModel", "size" + challanDetailsModel.data.size());
                for (ChallanDetailsModel.Data challan : challanDetailsModel.data) {

                    ChallanDetails challanDetails = new ChallanDetails();
                    ChallanDetails challan1 = Common.challanDetailsRepository.getChallanDetails(challan.F_CHALLAN_NO);
                    if (challan1 != null) {
                        challanDetails.id = challan1.id;
                        challanDetails.BOOK_NET_PRICE = challan.BOOK_NET_PRICE;
                        challanDetails.F_BOOK_NO = challan.F_BOOK_NO;
                        challanDetails.CHALLAN_BOOK_QTY = challan.CHALLAN_BOOK_QTY;
                        challanDetails.F_CHALLAN_NO = challan.F_CHALLAN_NO;
                        Common.challanDetailsRepository.updateChallanDetails(challanDetails);
                        Log.e("dfdf", "Dfd");
                    } else {
                        challanDetails.BOOK_NET_PRICE = challan.BOOK_NET_PRICE;
                        challanDetails.F_BOOK_NO = challan.F_BOOK_NO;
                        challanDetails.CHALLAN_BOOK_QTY = challan.CHALLAN_BOOK_QTY;
                        challanDetails.F_CHALLAN_NO = challan.F_CHALLAN_NO;
                        Common.challanDetailsRepository.insertToChallanDetails(challanDetails);
                        Log.e("qwertyyy", "Dfd");
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

    private void downBookStockDetails() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.downBookStock(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<StockResponse>() {
            @Override
            public void accept(StockResponse challanDetailsModel) throws Exception {
                Log.e("number4", "number4" + new Gson().toJson(challanDetailsModel));


                Common.bookStockRepository.emptyBookStock();
                for (StockResponse.Data stock : challanDetailsModel.data) {


                    BookStock bookStocks = Common.bookStockRepository.getBookStock(stock.BOOK_NO);
                    // int qty =Common.bookStockRepository.maxValue(stock.BOOK_ID);
                    if (bookStocks != null) {
//                        BookStock bookStock = new BookStock();
//                        bookStock.BOOK_ID = stock.BOOK_ID;
//                      //  bookStock.LAST_UPDATE_DATE_APP = bookStocks.LAST_UPDATE_DATE_APP;
//                     //   bookStock.LAST_UPDATE_DATE = bookStocks.LAST_UPDATE_DATE;
//                        bookStock.STORE_ID = SharedPreferenceUtil.getUserID(MainActivity.this);
//                        bookStock.id = bookStocks.id;
//                        bookStock.BOOK_NET_MRP = bookStocks.BOOK_NET_PRICES;
//                        bookStock.QTY_NUMBER = Integer.parseInt(stock.QTY);
//
//                        int total = Integer.parseInt(stock.QTY);
//                        bookStock.BOOK_NET_PRICES = bookStocks.BOOK_NET_PRICES * total;
//
//                        Common.bookStockRepository.updateBookStock(bookStock);
                    } else {
                        Book book = Common.bookRepository.getBookNo(stock.BOOK_NO);
                        if (book != null) {
                            BookStock bookStock = new BookStock();
                            bookStock.BOOK_ID = stock.BOOK_NO;
                            bookStock.BOOK_NET_MRP = Double.parseDouble(book.BOOK_NET_PRICE);
                            bookStock.STORE_ID = SharedPreferenceUtil.getUserID(MainActivity.this);
                            bookStock.QTY_NUMBER = Integer.parseInt(stock.BAL_QTY);
                            bookStock.BOOK_NET_PRICES = Double.parseDouble(book.BOOK_NET_PRICE) * Integer.parseInt(stock.BAL_QTY);
                            // bookStock.LAST_UPDATE_DATE_APP = "";
                            // bookStock.LAST_UPDATE_DATE = bookStocks.LAST_UPDATE_DATE;
                            Common.bookStockRepository.insertToBookStock(bookStock);
                        }

                    }

                }

                dismissLoadingProgress();
            }
        }, throwable -> dismissLoadingProgress()));

    }

    private void loadChalanItems() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalan(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanResponseEntity>() {
            @Override
            public void accept(ChallanResponseEntity challanResponseEntity) throws Exception {
                Log.e("size", "size" + new Gson().toJson(challanResponseEntity));
                Log.e("Challan size", "size" + challanResponseEntity.data.size());

                for (ChallanResponseEntity.Data books : challanResponseEntity.data) {
                    Challan challan = new Challan();

                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(books.CHALLAN_DATE);
                    challan.CHALLAN_CODE = books.CHALLAN_CODE;
                    challan.Date = date1;
                    challan.receive_date = books.RECEIVE_DAT;
                    challan.IS_RECEIVE = books.IS_RECEIVE;
                    challan.CHALLAN_DATE = books.CHALLAN_DATE;
                    challan.CHALLAN_NO = books.CHALLAN_NO;
                    challan.CHALLAN_QTY = books.CHALLAN_QTY;
                    challan.CHALLAN_SL_NO = books.CHALLAN_SL_NO;
                    challan.F_EMPLOYEE_NO_CHALLAN_BY = books.F_EMPLOYEE_NO_CHALLAN_BY;
                    challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                    challan.TOTAL_BOOK_COST = books.TOTAL_BOOK_COST;
                    challan.PER_HANDLING_COST = books.PER_HANDLING_COST;
                    challan.PER_PACKAGING_COST = books.PER_PACKAGING_COST;
                    challan.IS_PACKAGING_DUE = books.IS_PACKAGING_DUE;
                    challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                    challan.TOTAL_PACKAGING_COST = books.TOTAL_PACKAGING_COST;
                    challan.COMPLETED_BOOKING_PKT_QTY = books.COMPLETED_BOOKING_PKT_QTY;
                    challan.F_COMPANY_NO = books.F_COMPANY_NO;
                    challan.F_TRANSPORT_BR_NO = books.F_TRANSPORT_BR_NO;
                    challan.TOTAL_HANDLING_COST = books.TOTAL_HANDLING_COST;
                    challan.IS_HANDLING_DUE = books.TOTAL_HANDLING_COST;
                    challan.TOTAL_VALUE = books.TOTAL_VALUE;
                    challan.COMMENTS = books.COMMENTS;
                    challan.F_BOOK_ORDER_NO = books.F_BOOK_ORDER_NO;
                    challan.F_ORDER_INVOICE_NO = books.F_ORDER_INVOICE_NO;
                    challan.SL_NUM = books.SL_NUM;
                    challan.LAST_ACTION = books.LAST_ACTION;
                    challan.LAST_ACTION_TIME = books.LAST_ACTION_TIME;
                    challan.LAST_ACTION_LOGON_NO = books.LAST_ACTION_LOGON_NO;
                    challan.INSERT_TIME = books.INSERT_TIME;
                    challan.INSERT_USER_NO = books.INSERT_USER_NO;
                    challan.INSERT_LOGON_NO = books.INSERT_LOGON_NO;
                    challan.F_FISCAL_YEAR_NO = books.F_FISCAL_YEAR_NO;
                    challan.F_CUSTOMER_NO = books.F_CUSTOMER_NO;
                    challan.F_GODOWN_NO = books.F_GODOWN_NO;
                    challan.IS_BOOKED = books.IS_BOOKED;
                    challan.IS_BUSY = books.IS_BUSY;
                    challan.BUSY_USER_NO = books.BUSY_USER_NO;
                    challan.BUSY_TIME = books.BUSY_TIME;
                    challan.TOTAL_COMMISION = books.TOTAL_COMMISION;
                    challan.OVERALL_DISCOUNT = books.OVERALL_DISCOUNT;
                    challan.IS_BOOKED = books.IS_BOOKED;

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

    private void loadChalanItemsSync() {
        showLoadingProgress(MainActivity.this);
        ChallanPostEntity challanPostEntity = new ChallanPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.getChalan(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ChallanResponseEntity>() {
            @Override
            public void accept(ChallanResponseEntity challanResponseEntity) throws Exception {
                Log.e("sgfjsdgfs", "fsdfsd" + new Gson().toJson(challanResponseEntity));
                Log.e("uksfhkldhlks", "size" + challanResponseEntity.data.size());


                for (ChallanResponseEntity.Data books : challanResponseEntity.data) {

                    String test = books.CHALLAN_NO;
                    Challan challan = new Challan();
                    Date datess = null;
                    //SimpleDateFormat formatterss = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    SimpleDateFormat formatterss = new SimpleDateFormat("dd/MM/yyyy");
//                    try {
//                        datess = formatterss.parse(books.RECEIVE_DAT);
//                        Log.e("currentTime", "currentTime" + datess);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    Challan challan1 = Common.challanRepositoy.getChallans(books.CHALLAN_NO);
                    if (challan1 != null) {
                        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(books.CHALLAN_DATE);
                        challan.Date =date1;
                        challan.CHALLAN_DATE = books.CHALLAN_DATE;

                        challan.CHALLAN_NO = books.CHALLAN_NO;
                        challan.CHALLAN_QTY = books.CHALLAN_QTY;
                        challan.CHALLAN_SL_NO = books.CHALLAN_SL_NO;
                        challan.F_EMPLOYEE_NO_CHALLAN_BY = books.F_EMPLOYEE_NO_CHALLAN_BY;
                        challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                        challan.TOTAL_BOOK_COST = books.TOTAL_BOOK_COST;
                        challan.PER_HANDLING_COST = books.PER_HANDLING_COST;
                        challan.PER_PACKAGING_COST = books.PER_PACKAGING_COST;
                        challan.IS_PACKAGING_DUE = books.IS_PACKAGING_DUE;
                        challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                        challan.TOTAL_PACKAGING_COST = books.TOTAL_PACKAGING_COST;
                        challan.COMPLETED_BOOKING_PKT_QTY = books.COMPLETED_BOOKING_PKT_QTY;
                        challan.F_COMPANY_NO = books.F_COMPANY_NO;
                        challan.F_TRANSPORT_BR_NO = books.F_TRANSPORT_BR_NO;
                        challan.TOTAL_HANDLING_COST = books.TOTAL_HANDLING_COST;
                        challan.IS_HANDLING_DUE = books.TOTAL_HANDLING_COST;
                        challan.TOTAL_VALUE = books.TOTAL_VALUE;
                        challan.COMMENTS = books.COMMENTS;
                        challan.F_BOOK_ORDER_NO = books.F_BOOK_ORDER_NO;
                        challan.F_ORDER_INVOICE_NO = books.F_ORDER_INVOICE_NO;
                        challan.SL_NUM = books.SL_NUM;
                        challan.LAST_ACTION = books.LAST_ACTION;
                        challan.LAST_ACTION_TIME = books.LAST_ACTION_TIME;
                        challan.LAST_ACTION_LOGON_NO = books.LAST_ACTION_LOGON_NO;
                        challan.INSERT_TIME = books.INSERT_TIME;
                        challan.INSERT_USER_NO = books.INSERT_USER_NO;
                        challan.INSERT_LOGON_NO = books.INSERT_LOGON_NO;
                        challan.F_FISCAL_YEAR_NO = books.F_FISCAL_YEAR_NO;
                        challan.F_CUSTOMER_NO = books.F_CUSTOMER_NO;
                        challan.F_GODOWN_NO = books.F_GODOWN_NO;
                        challan.IS_BOOKED = books.IS_BOOKED;
                        challan.IS_BUSY = books.IS_BUSY;
                        challan.BUSY_USER_NO = books.BUSY_USER_NO;
                        challan.BUSY_TIME = books.BUSY_TIME;
                        challan.TOTAL_COMMISION = books.TOTAL_COMMISION;
                        challan.OVERALL_DISCOUNT = books.OVERALL_DISCOUNT;
                        challan.IS_BOOKED = books.IS_BOOKED;
                        challan.receive_date = books.RECEIVE_DAT;
                        challan.IS_RECEIVE = books.IS_RECEIVE;
                        challan.CHALLAN_NO = books.CHALLAN_NO;
                        challan.CHALLAN_CODE = books.CHALLAN_CODE;
                        challan.id = challan1.id;
                        Common.challanRepositoy.updateChallan(challan);

                    } else {
                        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(books.CHALLAN_DATE);
                        challan.CHALLAN_CODE = books.CHALLAN_CODE;
                        challan.Date = date1;
                        challan.receive_date = books.RECEIVE_DAT;
                        challan.IS_RECEIVE = "N";

                        challan.CHALLAN_DATE = books.CHALLAN_DATE;
                        challan.CHALLAN_NO = books.CHALLAN_NO;
                        challan.CHALLAN_QTY = books.CHALLAN_QTY;
                        challan.CHALLAN_SL_NO = books.CHALLAN_SL_NO;
                        challan.F_EMPLOYEE_NO_CHALLAN_BY = books.F_EMPLOYEE_NO_CHALLAN_BY;
                        challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                        challan.TOTAL_BOOK_COST = books.TOTAL_BOOK_COST;
                        challan.PER_HANDLING_COST = books.PER_HANDLING_COST;
                        challan.PER_PACKAGING_COST = books.PER_PACKAGING_COST;
                        challan.IS_PACKAGING_DUE = books.IS_PACKAGING_DUE;
                        challan.NO_OF_PACKATE = books.NO_OF_PACKATE;
                        challan.TOTAL_PACKAGING_COST = books.TOTAL_PACKAGING_COST;
                        challan.COMPLETED_BOOKING_PKT_QTY = books.COMPLETED_BOOKING_PKT_QTY;
                        challan.F_COMPANY_NO = books.F_COMPANY_NO;
                        challan.F_TRANSPORT_BR_NO = books.F_TRANSPORT_BR_NO;
                        challan.TOTAL_HANDLING_COST = books.TOTAL_HANDLING_COST;
                        challan.IS_HANDLING_DUE = books.TOTAL_HANDLING_COST;
                        challan.TOTAL_VALUE = books.TOTAL_VALUE;
                        challan.COMMENTS = books.COMMENTS;
                        challan.F_BOOK_ORDER_NO = books.F_BOOK_ORDER_NO;
                        challan.F_ORDER_INVOICE_NO = books.F_ORDER_INVOICE_NO;
                        challan.SL_NUM = books.SL_NUM;
                        challan.LAST_ACTION = books.LAST_ACTION;
                        challan.LAST_ACTION_TIME = books.LAST_ACTION_TIME;
                        challan.LAST_ACTION_LOGON_NO = books.LAST_ACTION_LOGON_NO;
                        challan.INSERT_TIME = books.INSERT_TIME;
                        challan.INSERT_USER_NO = books.INSERT_USER_NO;
                        challan.INSERT_LOGON_NO = books.INSERT_LOGON_NO;
                        challan.F_FISCAL_YEAR_NO = books.F_FISCAL_YEAR_NO;
                        challan.F_CUSTOMER_NO = books.F_CUSTOMER_NO;
                        challan.F_GODOWN_NO = books.F_GODOWN_NO;
                        challan.IS_BOOKED = books.IS_BOOKED;
                        challan.IS_BUSY = books.IS_BUSY;
                        challan.BUSY_USER_NO = books.BUSY_USER_NO;
                        challan.BUSY_TIME = books.BUSY_TIME;
                        challan.TOTAL_COMMISION = books.TOTAL_COMMISION;
                        challan.OVERALL_DISCOUNT = books.OVERALL_DISCOUNT;
                        challan.IS_BOOKED = books.IS_BOOKED;
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

    boolean data = true;

    private void loadChallan() {

        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                List<SyncChallanModel.Sync> syncs = new ArrayList<>();
                for (Challan challan : customers) {
                    SyncChallanModel.Sync syncChallanModel = new SyncChallanModel.Sync();
                    syncChallanModel.receive_date = challan.receive_date;
                    syncChallanModel.is_receive = challan.IS_RECEIVE;
                    syncChallanModel.challan_no = challan.CHALLAN_NO;
                    syncs.add(syncChallanModel);
                }
                SyncChallanModel s = new SyncChallanModel();
                s.data = syncs;
                if (data) {
                    data = false;
                    compositeDisposable.add(mService.syncChalan(s).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response>() {
                        @Override
                        public void accept(Response loginEntity) throws Exception {
                            if (loginEntity.status_code == 203) {
                                loadChalanItemsSync();
                                loadChalanDetails();
                            } else {
                                loadChalanItemsSync();
                                loadChalanDetails();
                            }
                            Log.e("number1", "number1" + loginEntity.status_code);


                        }
                    }));
                }


                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm a");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);

                Sync name = Common.syncRepository.valueFor("challan");
                if (name == null) {
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE = SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("challan");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "challan";
                    Common.syncRepository.insertToSync(sync);
                } else {
                    Sync sync = new Sync();
                    sync.id = name.id;
                    sync.CUSTOMER_CODE = name.CUSTOMER_CODE;
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("challan");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "challan";
                    Common.syncRepository.updateSync(sync);
                }

                Log.e("sfsfsf", "ss" + new Gson().toJson(s));
            }
        }));

    }

    private void loadCustomer() {

        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> userActivities) throws Exception {


                List<SalesModel.SalesMaster> syncs = new ArrayList<>();

                for (SalesMaster sales : userActivities) {
                    final SalesModel.SalesMaster salesMaster = new SalesModel.SalesMaster();

                    salesMaster.StoreId = sales.StoreId;
                    salesMaster.Device = sales.Device;
                    salesMaster.Discount = String.valueOf(sales.Discount);
                    salesMaster.PayMode = sales.PayMode;
                    salesMaster.NetValue = String.valueOf(sales.NetValue);
                    salesMaster.RetailCode = sales.RetailCode;
                    salesMaster.InvoiceAmount = String.valueOf(sales.InvoiceAmount);
                    salesMaster.InvoiceDate = sales.InvoiceDates;
                    salesMaster.TrnType = sales.TrnType;
                    salesMaster.Status = "I";
                    salesMaster.InvoiceId = sales.InvoiceId;
                    salesMaster.InvoiceNumber = sales.InvoiceNumber;
                    salesMaster.Note = sales.Note;
                    salesMaster.ReturnAmt = sales.Return;
                    int value = Common.syncRepository.maxValue("sales_mst");
                    salesMaster.UpdNo = String.valueOf(sales.UpdateNo);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    String currentDate = formatter.format(date);
                    SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss a");
                    Date dates = new Date(System.currentTimeMillis());
                    String currentTime = formatters.format(dates);
                    salesMaster.UpdDate = currentDate + " " + currentTime;
                    salesMaster.Phone = sales.PhoneNumber;
                    List<SalesModel.SalesMaster.SalesDetails> getList = getList(sales.InvoiceId, sales.TrnType);

                    salesMaster.salesDetails = getList;
                    syncs.add(salesMaster);

                }
                Date dates = new Date(System.currentTimeMillis());
                SalesModel salesModel = new SalesModel();
                salesModel.data = syncs;
                Log.e("SalesMasterForResponseU", "SalesMasterForResponseU" + new Gson().toJson(salesModel));

                Sync name = Common.syncRepository.valueFor("sales_mst");
                if (name == null) {
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE = SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("sales_mst");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "sales_mst";
                    Common.syncRepository.insertToSync(sync);
                } else {
                    Sync sync = new Sync();
                    sync.id = name.id;
                    sync.CUSTOMER_CODE = name.CUSTOMER_CODE;
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("sales_mst");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "sales_mst";
                    Common.syncRepository.updateSync(sync);
                }
                Sync names = Common.syncRepository.valueFor("sales_dtl");
                if (names == null) {
                    Sync sync = new Sync();
                    sync.CUSTOMER_CODE = SharedPreferenceUtil.getUserID(MainActivity.this);
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("sales_dtl");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "sales_dtl";
                    Common.syncRepository.insertToSync(sync);
                } else {
                    Sync sync = new Sync();
                    sync.id = names.id;
                    sync.CUSTOMER_CODE = names.CUSTOMER_CODE;
                    sync.DEVICE = "Mobile";
                    sync.SYNC_DATETIME = dates;
                    int value = Common.syncRepository.maxValue("sales_dtl");
                    sync.SYNC_NUMBER = value + 1;
                    sync.TABLE_NAME = "sales_dtl";
                    Common.syncRepository.updateSync(sync);
                }
                compositeDisposable.add(mService.syncSales(salesModel).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response loginEntity) throws Exception {
                        Log.e("SalesMasterForResponse2", "SalesMasterForResponse2" + loginEntity.status_code);


                        if (loginEntity.status_code == 200) {
                            loadSalesMaster();
                        } else if (loginEntity.status_code == 203) {
                            loadSalesMaster();
                        }

//                        loadSalesDetails("1001381191219001","01381");
//                        loadSalesDetails("1001381191212001","01381");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("xdvxc", "fx" + throwable.getMessage());
                        loadSalesMaster();
                        dismissLoadingProgress();
                    }
                }));

                Log.e("1", "dsd" + new Gson().toJson(salesModel));
            }
        }));

    }

    private void loadSalesMaster() {
        showLoadingProgress(MainActivity.this);
        SalesPostEntity challanPostEntity = new SalesPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);
        compositeDisposable.add(mService.downSalesMaster(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<SaleesMasterForModel>() {
            @Override
            public void accept(SaleesMasterForModel customerListResponse) throws Exception {
                Log.e("SalesMasterForResponse", "SalesMasterForResponse" + new Gson().toJson(customerListResponse));


                for (SaleesMasterForModel.Data customer : customerListResponse.data) {
                    SalesMaster salesMaster = Common.salesMasterRepository.getSalesMaster(String.valueOf(customer.INVOICE_ID));
                    if (salesMaster != null) {

                    } else {
                        SalesMaster salesMaster1 = new SalesMaster();
                        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(customer.INV_DATE);
                        final SimpleDateFormat formatterq = new SimpleDateFormat("dd-MM-yyyy");
                        // final Date dateq = new Date(System.currentTimeMillis());
                        //   String currentDate =formatterq.format(dateq);
                        salesMaster1.InvoiceId = customer.INVOICE_ID;
                        salesMaster1.DateSimple = formatterq.format(date1);
                        if (customer.TRN_TYPE != null) {
                            salesMaster1.TrnType = customer.TRN_TYPE;
                        } else {
                            salesMaster1.TrnType = " ";
                        }

                        salesMaster1.InvoiceDate = date1;
                        salesMaster1.InvoiceAmount = Double.parseDouble(customer.INVOICE_AMT);
                        salesMaster1.RetailCode = customer.RETAILER_CODE;
                        if (customer.PHONE != null) {
                            salesMaster1.PhoneNumber = customer.PHONE;
                        } else {
                            salesMaster1.PhoneNumber = "011111111";
                        }

                        Customer customer1 = Common.customerRepository.getCustomeRetailCode(customer.RETAILER_CODE);
                        if (customer1 != null) {
                            salesMaster1.CustomerName = customer1.ShopName;

                        } else {
                            salesMaster1.CustomerName = "N/A";

                        }
                        salesMaster1.StoreId = customer.STORE_ID;

                        if (customer.DISCOUNT != null) {
                            salesMaster1.Discount = Double.parseDouble(customer.DISCOUNT);
                        } else {
                            salesMaster1.Discount = 0;
                        }


                        salesMaster1.StoreId = customer.STORE_ID;
                        salesMaster1.Return = customer.RETURN_AMT;
                        salesMaster1.UpdateNo = Integer.parseInt(customer.UPD_NO);
                        salesMaster1.InvoiceNumber = customer.INVOICE_NO;
                        salesMaster1.InvoiceDates = customer.INV_DATE;
                        salesMaster1.Note = customer.NOTE;
                        salesMaster1.PayMode = customer.PAY_MODE;
                        salesMaster1.Device = customer.DEVICE;
                        salesMaster1.SubTotal = customer.INVOICE_AMT;

                        salesMaster1.NetValue = Double.parseDouble(customer.NET_VALUE);


                        Common.salesMasterRepository.insertToSalesMaster(salesMaster1);

                        loadSalesDetails(customer.INVOICE_ID, customer.STORE_ID, date1);
                    }


                }

                dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("xdvxc", "fx" + throwable.getMessage());
                dismissLoadingProgress();
            }
        }));
    }

    private void loadSalesDetails(final String invoiceId, final String storeId, final Date date) {


        showLoadingProgress(MainActivity.this);
        SalesDetailsPostEntity challanPostEntity = new SalesDetailsPostEntity();
        challanPostEntity.customer_no = SharedPreferenceUtil.getUserID(MainActivity.this);

        challanPostEntity.invoice_id = invoiceId;
        compositeDisposable.add(mService.downSalesDetails(challanPostEntity).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<SalesDetailsForModel>() {
            @Override
            public void accept(SalesDetailsForModel customerListResponse) throws Exception {
                Log.e("number3", "number3" + new Gson().toJson(customerListResponse));


                for (SalesDetailsForModel.Data customer : customerListResponse.data) {

                    SalesDetails salesDetails1 = new SalesDetails();
                    final SimpleDateFormat formatterq = new SimpleDateFormat("dd-MM-yyyy");
                    final Date dateq = new Date(System.currentTimeMillis());
                    int values = Common.salesMasterRepository.maxValue(formatterq.format(dateq), " ");
                    salesDetails1.InvoiceId = values;
                    salesDetails1.InvoiceIdNew = invoiceId;
                    int size = Common.bookRepository.size();
                    Book book = Common.bookRepository.getBookNo(customer.ITEM_ID);
                    if (book != null) {
                        salesDetails1.BookName = book.BookName;
                        salesDetails1.BookId = book.BookNo;
                    } else {
                        salesDetails1.BookName = "N/A";
                        salesDetails1.BookId = customer.ITEM_ID;
                    }

                    Log.e("dataaa", "" + invoiceId);
                    salesDetails1.MRP = Double.parseDouble(customer.MRP);
                    salesDetails1.TotalAmount = Double.parseDouble(customer.TOTAL_VALUE);
                    salesDetails1.StoreId = storeId;
                    salesDetails1.InvoiceDate = date;
                    salesDetails1.UpdateNo = Integer.parseInt(customer.UPD_NO);
                    salesDetails1.Discount = Double.parseDouble(customer.DISCOUNT_PC);
                    salesDetails1.Quantity = Integer.parseInt(customer.QTY);


                    Common.salesDetailsRepository.insertToSalesDetails(salesDetails1);


                }

                dismissLoadingProgress();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("xdvxc", "fx" + throwable.getMessage());
                dismissLoadingProgress();
            }
        }));
    }

    private List<SalesModel.SalesMaster.SalesDetails> getList(String id, String type) {
        List<SalesModel.SalesMaster.SalesDetails> salesDetails = new ArrayList<>();

        Flowable<List<SalesDetails>> units = Common.salesDetailsRepository.getSalesDetailsItemById(id);
        for (SalesDetails salesDetails1 : units.blockingFirst()) {
            SalesModel.SalesMaster.SalesDetails details = new SalesModel.SalesMaster.SalesDetails();
            details.BookId = salesDetails1.BookId;
            details.StoreId = salesDetails1.StoreId;
            details.InvoiceId = salesDetails1.InvoiceIdNew;
            details.MRP = String.valueOf(salesDetails1.MRP);


            if (salesDetails1.TotalAmount == 0) {
                details.DiscountPc = "0";
            } else {
                details.DiscountPc = String.valueOf(salesDetails1.Discount);
            }

            details.ReturnAmt = "0.0";
            int value = Common.syncRepository.maxValue("sales_dtl");
            details.UpdNo = String.valueOf(salesDetails1.UpdateNo);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = formatter.format(date);
            SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss a");
            Date dates = new Date(System.currentTimeMillis());
            String currentTime = formatters.format(dates);
            details.UpdDate = currentDate + " " + currentTime;
            details.TotalAmount = String.valueOf(salesDetails1.TotalAmount);
            if (type.equals("A")) {
                details.Quantity = String.valueOf(salesDetails1.QTY);
                double val =  salesDetails1.QTY * salesDetails1.MRP*salesDetails1.Discount / 100;
                details.Discount = String.valueOf(val);

            } else {
                details.Quantity = String.valueOf(salesDetails1.Quantity);
                double val =  salesDetails1.Quantity * salesDetails1.MRP*salesDetails1.Discount / 100;
                details.Discount = String.valueOf(val);

            }
            salesDetails.add(details);
        }
        return salesDetails;

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
    //    List<SalesModel.SalesMaster.SalesDetails> salesDetails = new ArrayList<>();
//    private List<SalesModel.SalesMaster.SalesDetails> display(List<SalesDetails> units) {
//
//        return salesDetails;
//
//    }
}
