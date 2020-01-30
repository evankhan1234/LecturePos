package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

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
import xact.idea.lecturepos.Model.BookResponseEntity;
import xact.idea.lecturepos.Retrofit.IRetrofitApi;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.Utils;

import static xact.idea.lecturepos.Utils.Utils.dismissLoadingProgress;
import static xact.idea.lecturepos.Utils.Utils.showLoadingProgress;

public class OnBoardingActivity extends AppCompatActivity {
  static CompositeDisposable compositeDisposable = new CompositeDisposable();
  IRetrofitApi mService;
  RelativeLayout rlt_root;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_on_boarding);
    CorrectSizeUtil.getInstance(this).correctSize();
    CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
    fn_permission();
//    rlt_root=findViewById(R.id.rlt_root);
//    mService = Common.getApiXact();
//    initDB();
//    if (Common.bookRepository.size() > 0) {
//
//    } else {
//      if (Utils.broadcastIntent(OnBoardingActivity.this, rlt_root)) {
//        loadBookItems();
//      } else {
//        Snackbar snackbar = Snackbar
//                .make(rlt_root, "No Internet", Snackbar.LENGTH_LONG);
//        snackbar.show();
//      }
//
//    }
    new Handler().postDelayed(() -> goToMainPage(), 3000);
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
  public static int REQUEST_PERMISSIONS = 2;
  private void fn_permission() {
    if ((ContextCompat.checkSelfPermission(OnBoardingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(OnBoardingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(OnBoardingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
      } else {
        ActivityCompat.requestPermissions(OnBoardingActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);

      }

      if ((ActivityCompat.shouldShowRequestPermissionRationale(OnBoardingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
      } else {
        ActivityCompat.requestPermissions(OnBoardingActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);

      }
    } else {
     // boolean_permission = true;


    }
  }
  private void loadBookItems() {
    showLoadingProgress(OnBoardingActivity.this);
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
  private void goToMainPage() {

    Intent i = new Intent(OnBoardingActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }
}