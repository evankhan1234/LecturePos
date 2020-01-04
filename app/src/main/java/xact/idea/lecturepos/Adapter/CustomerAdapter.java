package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.CustomerDetailsActivity;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesMaster;
import xact.idea.lecturepos.InvoiceActivity;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.Utils;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerListiewHolder> {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Activity mActivity = null;
    private List<Customer> messageEntities;
 
//    CustomerClickInterface CustomerClickInterface;
    public CustomerAdapter(Activity activity, List<Customer> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //mClick = mClicks;

    }


    @Override
    public CustomerAdapter.CustomerListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new CustomerAdapter.CustomerListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerAdapter.CustomerListiewHolder holder, final int position) {

        //   int row_index;
        holder.text_store_id.setVisibility(View.GONE);
        Log.e("SDFsf","SDfs"+messageEntities.get(position).Name);

        holder.text_customer.setText(messageEntities.get(position).Name);
        holder.text_mobile.setText(messageEntities.get(position).MobileNumber);
        holder.text_store_id.setText(messageEntities.get(position).StoreId);
        holder.text_name.setText(messageEntities.get(position).ShopName);
        holder.text_create_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(mActivity, InvoiceActivity.class));
                intent.putExtra("data",messageEntities.get(position).ShopName);
                mActivity.startActivity(intent);
                mActivity.finish();
                Constant.name=messageEntities.get(position).ShopName;
            }
        });
        holder.text_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(mActivity, CustomerDetailsActivity.class));
                intent.putExtra("ShopName",messageEntities.get(position).ShopName);
                intent.putExtra("Name",messageEntities.get(position).Name);
                mActivity.startActivity(intent);
               // mActivity.finish();
              //  Constant.name=messageEntities.get(position).ShopName;
            }
        });

        compositeDisposable.add(Common.salesMasterRepository.getSalesMasterList(messageEntities.get(position).ShopName).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SalesMaster>>() {
            @Override
            public void accept(List<SalesMaster> userActivities) throws Exception {
                //  displayCustomerItems(userActivities);
                Log.e("fsd","dfsdf"+new Gson().toJson(userActivities));
                // Log.e("fsd","dfsdf"+date);
                double credit = 0;
                for (SalesMaster salesMaster:userActivities){

                    if (salesMaster.PayMode.equals("Credit")){
                        credit+=salesMaster.NetValue;
                    }
                   // loadSub(salesMaster.InvoiceId);
                }
                String name = "<b><font color=#000 >Total Credit :  </font></b> <font color=#358ED3>"+Utils.getCommaValue(credit)+"</font>";
                holder.text_code.setText(Html.fromHtml(name));

            }
        }));


    }

    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }
    public class CustomerListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_customer;
        private TextView text_store_id;
        private TextView text_mobile;
        private TextView text_code;
        private TextView text_name;
        private TextView text_create_invoice;
        private TextView text_view_details;




        public CustomerListiewHolder(View itemView) {
            super(itemView);

            text_customer = itemView.findViewById(R.id.text_customer);
            text_store_id = itemView.findViewById(R.id.text_store_id);
            text_mobile = itemView.findViewById(R.id.text_mobile);
            text_code = itemView.findViewById(R.id.text_code);
            text_name = itemView.findViewById(R.id.text_name);
            text_create_invoice = itemView.findViewById(R.id.text_create_invoice);
            text_view_details = itemView.findViewById(R.id.text_view_details);



        }
    }
}