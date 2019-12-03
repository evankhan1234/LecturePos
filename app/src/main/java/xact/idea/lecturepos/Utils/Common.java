package xact.idea.lecturepos.Utils;


import xact.idea.lecturepos.Database.Datasources.BookRepository;
import xact.idea.lecturepos.Database.Datasources.CustomerRepository;
import xact.idea.lecturepos.Database.Datasources.SalesDetailsRepository;
import xact.idea.lecturepos.Database.Datasources.SalesMasterRepository;
import xact.idea.lecturepos.Database.Local.CustomerDataSources;
import xact.idea.lecturepos.Database.MainDatabase;
import xact.idea.lecturepos.Retrofit.IRetrofitApi;
import xact.idea.lecturepos.Retrofit.RetrofitClient;

public abstract class Common {
    public static MainDatabase mainDatabase;
    public static CustomerRepository customerRepository;
    public static BookRepository bookRepository;
    public static SalesDetailsRepository salesDetailsRepository;
    public static SalesMasterRepository salesMasterRepository;
//    public static UserListRepository userListRepository;
//    public static UserActivityRepository userActivityRepository;
//    public static UnitRepository unitRepository;
//    public static SetUpDataRepository setUpDataRepository;
//    public static LeaveSummaryRepository leaveSummaryRepository;
//    public static EntityLeaveRepository entityLeaveRepository;
//    public static RemainingLeaveRepository remainingLeaveRepository;
    public static final String BASE_URL="https://api.myjson.com/bins/";
    public static final String BASE_URL_XACT="http://emp.xactidea.com/mobile-app/api/";

    public static IRetrofitApi getApi(){
        return RetrofitClient.getClient(BASE_URL).create(IRetrofitApi.class);
    }
    public static IRetrofitApi getApiXact(){
        return RetrofitClient.getClient(BASE_URL_XACT).create(IRetrofitApi.class);
    }
}
