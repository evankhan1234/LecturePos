package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.SalesMasterListiewHolder> {

    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Activity mActivity = null;
    private List<SalesMaster> messageEntities;
    private SalesDetailsAdapter mAdapters;
    int row_index=0;
    //    SalesMasterClickInterface SalesMasterClickInterface;
    public InvoiceAdapter(Activity activity, List<SalesMaster> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //mClick = mClicks;

    }


    @Override
    public InvoiceAdapter.SalesMasterListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invoice_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new InvoiceAdapter.SalesMasterListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvoiceAdapter.SalesMasterListiewHolder holder, final int position) {

        //   int row_index;
//        String text = "<b><font color=#000 >Book Name :  </font></b> <font color=#358ED3>"+messageEntities.get(position).BookName+"</font>";
//        String text1 = "<b><font color=#000 >Discount :  </font></b> <font color=#358ED3>"+messageEntities.get(position).Discount+"</font>";
//        String text4 = "<b><font color=#000 >Total :  </font></b> <font color=#358ED3>"+messageEntities.get(position).Amount+"</font>";
//        String text2 = "<font color=#358ED3>"+messageEntities.get(position).Quantity+"</font> <b><font color=#000 > * BDT </font></b>";
//        String text3 = "<font color=#358ED3>"+messageEntities.get(position).Price+"</font> ";
        holder.text_name.setText(messageEntities.get(position).CustomerName);
        holder.text_contact_number.setText(messageEntities.get(position).StoreId);
        holder.text_invoice.setText(messageEntities.get(position).InvoiceNumber);
        holder.text_code.setText(messageEntities.get(position).RetailCode);
        holder.text_invoice_date.setText(messageEntities.get(position).InvoiceDates);
        holder.text_discount.setText(String.valueOf(messageEntities.get(position).Discount));
        holder.text_total.setText(String.valueOf(messageEntities.get(position).InvoiceAmount));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                false
        );

        holder.rcl_this_customer_list.setLayoutManager(linearLayoutManager);
        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItemById(messageEntities.get(position).id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
            @Override
            public void accept(List<SalesDetails> units) throws Exception {
                Log.e("data","data"+new Gson().toJson(units));
                mAdapters = new SalesDetailsAdapter(mActivity, units);
                holder.rcl_this_customer_list.setAdapter(mAdapters);

            }
        }));





    }
    private void detailsListData() {
        //  showLoadingProgress(mActivity);


    }

    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }
    public class SalesMasterListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_name;
        private TextView text_contact_number;
        private TextView text_invoice;
        private TextView text_code;
        private TextView text_invoice_date;
        private TextView text_discount;
        private TextView text_total;
        private RecyclerView rcl_this_customer_list;




        public SalesMasterListiewHolder(View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.text_name);
            text_contact_number = itemView.findViewById(R.id.text_contact_number);
            text_invoice = itemView.findViewById(R.id.text_invoice);
            text_code = itemView.findViewById(R.id.text_code);
            text_invoice_date = itemView.findViewById(R.id.text_invoice_date);
            text_discount = itemView.findViewById(R.id.text_discount);
            text_total = itemView.findViewById(R.id.text_total);
            rcl_this_customer_list = itemView.findViewById(R.id.rcl_this_customer_list);



        }
    }

}