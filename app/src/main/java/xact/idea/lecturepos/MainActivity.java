package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Datasources.BookRepository;
import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Local.BookDataSources;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.Local.SalesDetailsDataSources;
import xact.idea.lecturepos.Database.Local.SalesMasterDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Model.BookModel;
import xact.idea.lecturepos.Model.CustomerModel;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    List<CustomerModel> customerModelList= new ArrayList<>();
    List<BookModel> bookModelList= new ArrayList<>();
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tv_store;
    LinearLayout linear_logout;
    LinearLayout linear_customers;
    LinearLayout linear_invoice;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linear_logout=findViewById(R.id.linear_logout);
        linear_customers=findViewById(R.id.linear_customers);
        linear_invoice=findViewById(R.id.linear_invoice);
        tv_store=findViewById(R.id.tv_store);
        tv_store.setSelected(true);
        mActivity=this;
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        initDB();
        if (Common.customerRepository.size() > 0) {

            loadDepartmentItems();
            loaBookItems();
        }else{
            CustomerModel customer = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00059","Md Abul Khair","Mohadebpur","01741242652");
            CustomerModel customer1 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00060","Md Abdus Satter","Ghulshan","01641262652");
            CustomerModel customer2 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00061","Md Saiful Islam","Merul badda","01941242632");
            CustomerModel customer3 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00062","Md Hakim","Patuakhali","01541642652");
            CustomerModel customer4 = new CustomerModel(SharedPreferenceUtil.getUserID(MainActivity.this),"00063","Md Monirul Islam","Dhaka","01781242689");
            BookModel bookModel= new BookModel("1604000887","Economics-TC","29,209",885.35);
            BookModel bookModel1= new BookModel("1604000888","Accounting-TC","29,210",385.35);
            BookModel bookModel2= new BookModel("1604000889","Bus. Introduction-TC","29,211",1285.35);
            BookModel bookModel3= new BookModel("1604000890","Bus. Entre. & Comm. Geo.-TC","29,212",485.35);
            BookModel bookModel4= new BookModel("1604000891","Ananda Path","29,213",285.35);

            customerModelList.add(customer);
            customerModelList.add(customer1);
            customerModelList.add(customer2);
            customerModelList.add(customer3);
            customerModelList.add(customer4);
            bookModelList.add(bookModel);
            bookModelList.add(bookModel1);
            bookModelList.add(bookModel2);
            bookModelList.add(bookModel3);
            bookModelList.add(bookModel4);

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
            for (BookModel book:bookModelList){
                Book b = new Book();
                b.BookCode=book.BookCode;
                b.BookName=book.BookName;
                b.BookNo=book.BookNo;
                b.BookPrice=book.BookPrice;
                Common.bookRepository.insertToBook(b);

            }
        }
    }
    private void initDB() {
        Common.mainDatabase = MainDatabase.getInstance(this);
        Common.customerRepository = CustomerRepository.getInstance(CustomerDataSources.getInstance(Common.mainDatabase.customerDao()));
        Common.bookRepository = BookRepository.getInstance(BookDataSources.getInstance(Common.mainDatabase.bookDao()));
        Common.salesDetailsRepository = SalesDetailsRepository.getInstance(SalesDetailsDataSources.getInstance(Common.mainDatabase.saleDetailsDao()));
        Common.salesMasterRepository = SalesMasterRepository.getInstance(SalesMasterDataSources.getInstance(Common.mainDatabase.saleMastersDao()));

    }
    private static void loadDepartmentItems() {

        compositeDisposable.add(Common.customerRepository.getCustomerItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Customer>>() {
            @Override
            public void accept(List<Customer> departments) throws Exception {
                Log.e("size","size"+new Gson().toJson(departments));
            }
        }));

    }
    private static void loaBookItems() {

        compositeDisposable.add(Common.bookRepository.getBookItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Book>>() {
            @Override
            public void accept(List<Book> departments) throws Exception {
                Log.e("size","size"+new Gson().toJson(departments));
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
