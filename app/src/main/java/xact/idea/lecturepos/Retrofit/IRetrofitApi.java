package xact.idea.lecturepos.Retrofit;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import xact.idea.lecturepos.Model.BookResponseEntity;
import xact.idea.lecturepos.Model.ChallanDetailsModel;
import xact.idea.lecturepos.Model.ChallanPostEntity;
import xact.idea.lecturepos.Model.ChallanResponseEntity;
import xact.idea.lecturepos.Model.LoginEntity;
import xact.idea.lecturepos.Model.LoginPostEntity;
import xact.idea.lecturepos.Model.Response;
import xact.idea.lecturepos.Model.RetailsSyncModel;
import xact.idea.lecturepos.Model.SalesModel;
import xact.idea.lecturepos.Model.SyncChallanModel;


public interface IRetrofitApi {
//    @GET("setup/data.php")
//    io.reactivex.Observable<SetUpDataEntity> getSetUpData();
//    @GET("tvbo4")
//    io.reactivex.Observable<UserTotalLeaveEntity> getTotalLeave();
//    @GET("gl55w")
//    io.reactivex.Observable<ArrayList<LeaveSummaryEntity>> getLeaveSummary();
//    @GET("1fjadg")
//    io.reactivex.Observable<ArrayList<LeaveApprovalListEntity>> getLeaveApproval();
//
//    @GET("eu7j3")
//    io.reactivex.Observable<ArrayList<UserActivityEntity>> getUserActivity();
//
//    @POST("unit/unit_list.php")
//    io.reactivex.Observable<UnitListEntity> getUnitList();
//
//    @POST("department/department_list.php")
//    io.reactivex.Observable<DepartmentListEntity> getDepartmentList();
    @GET("books/get_data.php")
    io.reactivex.Observable<BookResponseEntity> getBook();
//
//
    @POST("auth/")
    io.reactivex.Observable<LoginEntity> Login(@Body LoginPostEntity loginPostEntity);

    @POST("challan/get_data.php")
    io.reactivex.Observable<ChallanResponseEntity> getChalan(@Body ChallanPostEntity challanPostEntity);
    @POST("challan-detail/get_data.php")
    io.reactivex.Observable<ChallanDetailsModel> getChalanDetails(@Body ChallanPostEntity challanPostEntity);

    @POST("sync_challans/action.php")
    io.reactivex.Observable<Response> syncChalan(@Body SyncChallanModel challanPostEntity);
    @POST("sync_sales/action.php")
    io.reactivex.Observable<Response> syncSales(@Body SalesModel salesModel);
    @POST("sync_retail_customers/action.php")
    io.reactivex.Observable<Response> syncCustomer(@Body RetailsSyncModel retailsSyncModel);
//    @POST("user/user_list.php")
//    io.reactivex.Observable<AllUserListEntity> getUserList();
//    @POST("user/user_activity.php")
  //  io.reactivex.Observable<UserActivityListEntity> getUserActivityList(@Body UserActivityPostEntity userActivityPostEntity);
  //   @GET("books/get_data.php")
//    io.reactivex.Observable<PunchInOutResponseEntity> postPunch(@Body PunchInOutPostEntity punchInOutPostEntity);

//    @FormUrlEncoded
//    @POST("server/category/add_category.php")
//    io.reactivex.Observable<String> addNewCategory(@Field("name") String name, @Field("imgPath") String imgPath);
//    @Multipart
//    @POST("server/category/upload_category_img.php")
//    Call<String> uploadCategoryFile(@Part MultipartBody.Part file);
//
//    @FormUrlEncoded
//    @POST("server/category/update_category.php")
//    io.reactivex.Observable<String> updateCategory(@Field("id") int id,@Field("name") String name,@Field("imgPath") String imgPath);
//    @FormUrlEncoded
//    @POST("server/category/delete_category.php")
//    io.reactivex.Observable<String> deleteCategory(@Field("id") int id);
//    @FormUrlEncoded
//    @POST("getDrink.php")
//    io.reactivex.Observable<List<Drink>> getDrink(@Field("menuid") int menuid);
//
//    @FormUrlEncoded
//    @POST("server/product/add_product.php")
//    io.reactivex.Observable<String> addNewProduct(@Field("name") String name,
//                                                  @Field("imgPath") String imgPath,
//                                                  @Field("price") String price,
//                                                  @Field("menuId") int menuId);
//    @Multipart
//    @POST("server/product/upload_product_img.php")
//    Call<String> uploadProductFile(@Part MultipartBody.Part file);
//
//
//
//    @FormUrlEncoded
//    @POST("server/product/update_product.php")
//    io.reactivex.Observable<String> updateProduct(@Field("id") String id,@Field("name") String name,@Field("imgPath") String imgPath ,@Field("price") String price, @Field("menuId") String menuId);
//    @FormUrlEncoded
//    @POST("server/product/delete_product.php")
//    io.reactivex.Observable<String> deleteProduct(@Field("id") String id);
//
//    @FormUrlEncoded
//    @POST("server/order/getOrder.php")
//    io.reactivex.Observable<List<Order>> getOrder(
//            @Field("status") String status);
//    @FormUrlEncoded
//    @POST("updatetoken.php")
//    Call<String> updatetoken(@Field("phone") String phone,
//                             @Field("token") String token, @Field("isServerToken") String isServerToken);
//
//
//    @FormUrlEncoded
//    @POST("server/order/update_order_status.php")
//    io.reactivex.Observable<String> update_order_status(
//            @Field("status") int status,
//            @Field("phone") String phone,
//            @Field("order_id") long order_id);
//
//    @FormUrlEncoded
//    @POST("getToken.php")
//    Call<Token> getToken(@Field("phone") String phone,
//                         @Field("isServerToken") String isServerToken);
}
