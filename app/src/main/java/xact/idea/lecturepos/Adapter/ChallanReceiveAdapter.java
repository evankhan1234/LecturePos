package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.ChallanActivity;
import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.ChallanDetails;
import xact.idea.lecturepos.Model.ChallanDetailsModelFor;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;
import xact.idea.lecturepos.Utils.Utils;

public class ChallanReceiveAdapter extends RecyclerView.Adapter<ChallanReceiveAdapter.ChallanListiewHolder> {


    private Activity mActivity = null;
    private List<Challan> messageEntities;
    boolean row_index=true;
    private ChallanDetailsAdapter mAdapters;
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    //    ChallanClickInterface ChallanClickInterface;
    public ChallanReceiveAdapter(Activity activity, List<Challan> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //mClick = mClicks;

    }


    @Override
    public ChallanReceiveAdapter.ChallanListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_challan_receive_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new ChallanReceiveAdapter.ChallanListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChallanReceiveAdapter.ChallanListiewHolder holder, final int position) {
        double total;
        if (messageEntities.get(position).TOTAL_VALUE!=null){
            total=Double.parseDouble(messageEntities.get(position).TOTAL_VALUE);

        }
        else {
            total=0;
        }        String challan_no = "<b><font color=#000 >Challan Code :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_CODE+"</font>";
        String challan_packet = "<b><font color=#000 >Total Packet :  </font></b> <font color=#358ED3>"+messageEntities.get(position).NO_OF_PACKATE+"</font>";
        String challan_date = "<b><font color=#000 >Date :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_DATE+"</font>";
        String challan_quantity = "<b><font color=#000 >Quantity :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_QTY+"</font>";
        String challan_value = "<b><font color=#000 >Total Price :  </font></b> <font color=#358ED3>"+ Utils.getCommaValue(total)+"</font>";
        holder.text_challan_no.setText(Html.fromHtml(challan_no));
        holder.text_challan_date.setText(Html.fromHtml(challan_date));
        holder.text_challan_packet.setText(Html.fromHtml(challan_packet));
        holder.text_total_value.setText(Html.fromHtml(challan_value));
        holder.text_challan_quantity.setText(Html.fromHtml(challan_quantity));
   //     holder.btn_receive.setVisibility(View.GONE);
        //   int row_index;

        if (row_index){

            holder.view_color.setBackgroundColor(mActivity.getResources().getColor(R.color.accept));
            row_index=false;
        }
        else {
            holder.view_color.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
            row_index=true;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialogView(mActivity,position);

            }

        });

    }

    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }
    public class ChallanListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_challan_no;
        private TextView text_challan_quantity;
        private TextView text_challan_packet;
        private TextView text_challan_date;
        private TextView text_total_value;
        private Button btn_receive;

        private View view_color;


        public ChallanListiewHolder(View itemView) {
            super(itemView);

            text_challan_no = itemView.findViewById(R.id.text_challan_no);
            text_challan_quantity = itemView.findViewById(R.id.text_challan_quantity);
            text_challan_packet = itemView.findViewById(R.id.text_challan_packet);
            text_challan_date = itemView.findViewById(R.id.text_challan_date);
            text_total_value = itemView.findViewById(R.id.text_total_value);
           // btn_receive = itemView.findViewById(R.id.btn_receive);

            view_color = itemView.findViewById(R.id.view_color);

        }
    }
    public  void showInfoDialogView(final Context mContext, final int position)
    {

        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_challlan_receive, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        TextView tv_info = infoDialog.findViewById(R.id.tv_info);
        Button btn_yes = infoDialog.findViewById(R.id.btn_ok);
        Button btn_no = infoDialog.findViewById(R.id.btn_cancel);
        Button btn_now = infoDialog.findViewById(R.id.btn_now);
        LinearLayout linear = infoDialog.findViewById(R.id.linear);
        TextView tv_total = infoDialog.findViewById(R.id.tv_total);
        TextView spinerTitle = infoDialog.findViewById(R.id.spinerTitle);
        RecyclerView rcl_this_customer_list = infoDialog.findViewById(R.id.rcl_this_customer_list);
        tv_info.setText("Are you want to receive ??");
        linear.setVisibility(View.GONE);
        tv_info.setVisibility(View.GONE);
        btn_now.setVisibility(View.VISIBLE);
        spinerTitle.setText(" Challan Code: "+messageEntities.get(position).CHALLAN_CODE);
        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        compositeDisposable.add(Common.challanDetailsRepository.getChallanDetailsFor(messageEntities.get(position).CHALLAN_NO).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<ChallanDetailsModelFor>>() {
            @Override
            public void accept(List<ChallanDetailsModelFor> units) throws Exception {
                Log.e("data","data"+new Gson().toJson(units));
                int price = 0;

                for (ChallanDetailsModelFor challanDetailsModelFor: units){
                    Log.e("Dxfxf","xf"+challanDetailsModelFor.BOOK_NET_PRICE);
                    price+=Integer.parseInt(challanDetailsModelFor.BOOK_NET_PRICE)*Integer.parseInt(challanDetailsModelFor.CHALLAN_BOOK_QTY);
                    // price+=d;
                }
                tv_total.setText("Total Price: "+String.valueOf(Utils.getCommaValue(price)));
                mAdapters = new ChallanDetailsAdapter(mActivity, units);
                rcl_this_customer_list.setAdapter(mAdapters);
            }
        }));

        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = formatter.format(date);
                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);
                Common.challanRepositoy.updateReciver("Y",messageEntities.get(position).CHALLAN_NO,currentDate+" "+currentTime,currentDate);
                //  ChallanDetails challanDetails =Common.challanDetailsRepository.getChallanDetails(messageEntities.get(position).CHALLAN_NO);

                Flowable<List<ChallanDetails>> units=  Common.challanDetailsRepository.getChallanDetailsItemById(Integer.parseInt(messageEntities.get(position).CHALLAN_NO));
                for (ChallanDetails challanDetails : units.blockingFirst()) {
                    BookStock bookStocks =Common.bookStockRepository.getBookStock(challanDetails.F_BOOK_NO);
                    int qty =Common.bookStockRepository.maxValue(challanDetails.F_BOOK_NO);
                    if (bookStocks!=null){
                        BookStock bookStock = new BookStock();
                        bookStock.BOOK_ID=challanDetails.F_BOOK_NO;
                        bookStock.LAST_UPDATE_DATE_APP=date;
                        bookStock.LAST_UPDATE_DATE=currentDate+" "+currentTime;
                        bookStock.STORE_ID= SharedPreferenceUtil.getUserID(mContext);
                        bookStock.id=bookStocks.id;
                        bookStock.BOOK_NET_MRP=Double.parseDouble(challanDetails.BOOK_NET_PRICE);
                        bookStock.QTY_NUMBER= Integer.parseInt(challanDetails.CHALLAN_BOOK_QTY)+qty;

                        int total=Integer.parseInt(challanDetails.CHALLAN_BOOK_QTY)+qty;
                        bookStock.BOOK_NET_PRICES= Double.parseDouble(challanDetails.BOOK_NET_PRICE)*total;

                        Common.bookStockRepository.updateBookStock(bookStock);
                    }
                    else {
                        BookStock bookStock = new BookStock();
                        bookStock.BOOK_ID=challanDetails.F_BOOK_NO;
                        bookStock.LAST_UPDATE_DATE_APP=date;
                        bookStock.BOOK_NET_MRP=Double.parseDouble(challanDetails.BOOK_NET_PRICE);
                        bookStock.LAST_UPDATE_DATE=currentDate+" "+currentTime;
                        bookStock.STORE_ID=SharedPreferenceUtil.getUserID(mContext);
                        bookStock.QTY_NUMBER= Integer.parseInt(challanDetails.CHALLAN_BOOK_QTY);
                        bookStock.BOOK_NET_PRICES= Double.parseDouble(challanDetails.BOOK_NET_PRICE)*Integer.parseInt(challanDetails.CHALLAN_BOOK_QTY);

                        Common.bookStockRepository.insertToBookStock(bookStock);
                    }


                }
                SharedPreferenceUtil.saveShared(mActivity, SharedPreferenceUtil.USER_SYNC, "green");
                SharedPreferenceUtil.saveShared(mActivity, SharedPreferenceUtil.USER_SUNC_DATE_TIME, currentDate+" "+currentTime+ "");
                ((ChallanActivity)mActivity).fixed();
                notifyDataSetChanged();
                infoDialog.dismiss();

            }
        });
        btn_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        infoDialog.show();



    }
}