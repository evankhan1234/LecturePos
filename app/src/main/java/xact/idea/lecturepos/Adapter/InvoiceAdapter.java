package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import xact.idea.lecturepos.Database.Model.ChallanDetails;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.InvoiceActivity;

import xact.idea.lecturepos.InvoicePrintActivity;
import xact.idea.lecturepos.InvoicePrintAgainActivity;
import xact.idea.lecturepos.Model.ChallanDetailsModelFor;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.InvoiceFilter;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.SalesMasterListiewHolder> implements Filterable {

    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Activity mActivity = null;
    public List<SalesMaster> messageEntities;
    private SalesDetailsAdapter mAdapters;
    boolean row_index=true;
    double value;
    double value2;
    InvoiceFilter filter;
    String valueFor;
    //    SalesMasterClickInterface SalesMasterClickInterface;
    public InvoiceAdapter(Activity activity, List<SalesMaster> messageEntitie, String value) {
        mActivity = activity;
        messageEntities = messageEntitie;
        valueFor=value;
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
       String name = "<b><font color=#000 ></font></b> <font color=#358ED3>"+messageEntities.get(position).CustomerName+"</font>";
       String store = "<b><font color=#000 >Store Id :  </font></b> <font color=#358ED3>"+messageEntities.get(position).StoreId+"</font>";
       String invoice_number = "<b><font color=#000 ></font></b> <font color=#358ED3>"+messageEntities.get(position).InvoiceNumber+"</font>";
       String retail_code = "<b><font color=#000 >Retail Code :  </font></b> <font color=#358ED3>"+messageEntities.get(position).RetailCode+"</font>";
       String invoice_date = "<b><font color=#000 ></font></b> <font color=#358ED3>"+messageEntities.get(position).InvoiceDates+"</font>";
       String invoice_total = "<b><font color=#000 >Total :  </font></b> <font color=#358ED3>"+messageEntities.get(position).InvoiceAmount+"</font>";
//        String text4 = "<b><font color=#000 >Total :  </font></b> <font color=#358ED3>"+messageEntities.get(position).Amount+"</font>";
//        String text2 = "<font color=#358ED3>"+messageEntities.get(position).Quantity+"</font> <b><font color=#000 > * BDT </font></b>";
//        String text3 = "<font color=#358ED3>"+messageEntities.get(position).Price+"</font> ";
        holder.text_name.setText(Html.fromHtml(name));
        holder.text_contact_number.setText(Html.fromHtml(invoice_date));
        holder.text_invoice.setText(Html.fromHtml(invoice_number));
        holder.text_code.setText(Html.fromHtml(retail_code));
    //    holder.text_invoice_date.setText(Html.fromHtml(invoice_date));
       // holder.text_discount.setText(String.valueOf(messageEntities.get(position).Discount));
        holder.text_total.setText(String.valueOf(Html.fromHtml(invoice_total)));

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
//                mActivity,
//                    LinearLayoutManager.HORIZONTAL,
//                false
//        );
        if (messageEntities.get(position).TrnType.equals("A")){
            holder.text_create_invoice.setVisibility(View.GONE);
            holder.text_total.setVisibility(View.GONE);
            holder.text_create_invoice_adjustment.setVisibility(View.VISIBLE);
        }
        else {
            holder.text_create_invoice.setVisibility(View.VISIBLE);
            holder.text_create_invoice_adjustment.setVisibility(View.GONE);

        }
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rcl_this_customer_list.setLayoutManager(lm);

//        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItemById(messageEntities.get(position).InvoiceId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
//            @Override
//            public void accept(List<SalesDetails> units) throws Exception {
//                Log.e("data","data"+new Gson().toJson(units));
//                mAdapters = new SalesDetailsAdapter(mActivity, units);
//                holder.rcl_this_customer_list.setAdapter(mAdapters);
//
//            }
//        }));
//        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
//            @Override
//            public void accept(List<SalesDetails> units) throws Exception {
//                Log.e("hgh","data"+new Gson().toJson(units));
//
//
//            }
//        }));

        holder.text_create_invoice_adjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialogView(mActivity,position);
            }
        });
        holder.text_create_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d=Constant.rate;
                if (Constant.rate.equals("rate1"))
                {
                    compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItemById(messageEntities.get(position).InvoiceId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
                        @Override
                        public void accept(List<SalesDetails> units) throws Exception {
                            Log.e("data","data"+new Gson().toJson(units));

                            for (SalesDetails salesDetails:units){
                                // value+=salesDetails.MRP;
                                //   double ss=salesDetails.MRP* (1-salesDetails.Discount/100);

                                value2 +=salesDetails.Quantity * salesDetails.MRP* (1-salesDetails.Discount/100);
                            }
                            //tv_total.setText("Total Price: "+String.valueOf(price));

                            Customer customer=Common.customerRepository.getCustomerss(messageEntities.get(position).CustomerName);
                            if (customer!=null){
                                if (Constant.rate.equals("rate1"))
                                {
                                    Intent intent = new Intent(mActivity, InvoicePrintAgainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    SharedPreferenceUtil.saveShared(mActivity, SharedPreferenceUtil.USER_TEST, messageEntities.get(position).InvoiceId);
                                    intent.putExtra("sub",String.valueOf(rounded(value2,2)));
                                    intent.putExtra("customerName", customer.ShopName);
                                    intent.putExtra("value", valueFor);
                                    intent.putExtra("invoiceId", messageEntities.get(position).InvoiceId);
                                    mActivity.startActivity(intent);
                                    mActivity.finish();
                                }

                            }
                            else {
                                Toast.makeText(mActivity, "Customer is not valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));

                }


                //infoDialog.dismiss();
            }
        });
        if (row_index){

            holder.view_color.setBackgroundColor(mActivity.getResources().getColor(R.color.light_yellow));
            row_index=false;
        }
        else {
            holder.view_color.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
            row_index=true;
        }



    }

    public  void showInfoDialogView(final Context mContext, final int position)
    {

        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_sales_details_print, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        TextView tv_info = infoDialog.findViewById(R.id.tv_info);

        Button btn_no = infoDialog.findViewById(R.id.btn_cancel);
        Button btn_ok = infoDialog.findViewById(R.id.btn_ok);

     //   LinearLayout linear = infoDialog.findViewById(R.id.linear);
       // TextView tv_total = infoDialog.findViewById(R.id.tv_total);
        TextView spinerTitle = infoDialog.findViewById(R.id.spinerTitle);
        RecyclerView rcl_this_customer_list = infoDialog.findViewById(R.id.rcl_this_customer_list);

//        linear.setVisibility(View.GONE);
     //   tv_info.setVisibility(View.GONE);

        spinerTitle.setText(" Adjustment Number: "+messageEntities.get(position).InvoiceNumber);
        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);


        compositeDisposable.add(Common.salesDetailsRepository.getSalesDetailsItemById(messageEntities.get(position).InvoiceId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesDetails>>() {
            @Override
            public void accept(List<SalesDetails> units) throws Exception {
                Log.e("data","data"+new Gson().toJson(units));

                for (SalesDetails salesDetails:units){
                   // value+=salesDetails.MRP;
                 //   double ss=salesDetails.MRP* (1-salesDetails.Discount/100);

                    value +=salesDetails.Quantity * salesDetails.MRP* (1-salesDetails.Discount/100);
                }
                //tv_total.setText("Total Price: "+String.valueOf(price));
                mAdapters = new SalesDetailsAdapter(mActivity, units);
                rcl_this_customer_list.setAdapter(mAdapters);

            }
        }));


        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer=Common.customerRepository.getCustomerss(messageEntities.get(position).CustomerName);
                if (customer!=null){
                    Intent intent = new Intent(mActivity, InvoicePrintAgainActivity.class);

                    intent.putExtra("sub",String.valueOf(rounded(value,2)));


                    intent.putExtra("customerName", customer.ShopName);
                    intent.putExtra("invoiceId", messageEntities.get(position).InvoiceId);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                    infoDialog.dismiss();
                }
                else {
                    Toast.makeText(mActivity, "Customer is not valid", Toast.LENGTH_SHORT).show();
                    infoDialog.dismiss();
                }

            }
        });
        infoDialog.show();



    }
    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new InvoiceFilter(messageEntities, this);
        }
        return filter;
    }

    public class SalesMasterListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_name;
        private TextView text_create_invoice_adjustment;
        private TextView text_contact_number;
        private TextView text_invoice;
        private TextView text_code;
        private TextView text_invoice_date;
        private TextView text_discount;
        private TextView text_view_details;
        private TextView text_create_invoice;
        private TextView text_total;
        private RecyclerView rcl_this_customer_list;
        private View view_color;




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
            view_color = itemView.findViewById(R.id.view_color);
            text_view_details = itemView.findViewById(R.id.text_view_details);
            text_create_invoice = itemView.findViewById(R.id.text_create_invoice);
            text_create_invoice_adjustment = itemView.findViewById(R.id.text_create_invoice_adjustment);


        }
    }

}