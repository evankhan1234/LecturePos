package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xact.idea.lecturepos.Database.Model.Customer;

import xact.idea.lecturepos.Interface.CustomerInterface;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SpinnerFilter;
import xact.idea.lecturepos.Utils.SpinnerFilterForCustomer;

public class CustomerListAdadpter extends RecyclerView.Adapter<CustomerListAdadpter.PlaceTagListiewHolder> implements Filterable {


    SpinnerFilterForCustomer filter;
    private Activity mActivity = null;
    public List<Customer> messageEntities;
    CustomerInterface bookItemInterfaces;

    public  List<String> item;
    public CustomerListAdadpter(Activity activity, List<Customer> messageEntitie, List<String> item,CustomerInterface customerInterface) {
        mActivity = activity;
        this.messageEntities = messageEntitie;
        this.bookItemInterfaces = customerInterface;
        this.item = item;

    }


    @Override
    public CustomerListAdadpter.PlaceTagListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new CustomerListAdadpter.PlaceTagListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerListAdadpter.PlaceTagListiewHolder holder, final int position) {
        // UserList messageEntitie= messageEntities.get(position);
        holder.text1.setText(item.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookItemInterfaces.onClick(item.get(position),position);
            }
        });


    }

    @Override
    public int getItemCount() {

        return item.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SpinnerFilterForCustomer(item, this);
        }
        return filter;
    }

    public class PlaceTagListiewHolder extends RecyclerView.ViewHolder {

        private TextView text1;



        public PlaceTagListiewHolder(View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.text1);



        }
    }




}