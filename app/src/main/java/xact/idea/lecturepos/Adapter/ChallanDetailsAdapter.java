package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xact.idea.lecturepos.Database.Model.ChallanDetails;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Interface.CustomerInterface;
import xact.idea.lecturepos.Model.ChallanDetailsModelFor;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SpinnerFilterForCustomer;

public class ChallanDetailsAdapter extends RecyclerView.Adapter<ChallanDetailsAdapter.PlaceTagListiewHolder>  {

    boolean row_index=true;
    SpinnerFilterForCustomer filter;
    private Activity mActivity = null;
    public List<ChallanDetailsModelFor> messageEntities;
    CustomerInterface bookItemInterfaces;

    public  List<String> item;
    public ChallanDetailsAdapter(Activity activity, List<ChallanDetailsModelFor> messageEntitie) {
        mActivity = activity;
        this.messageEntities = messageEntitie;


    }


    @Override
    public ChallanDetailsAdapter.PlaceTagListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_challan_details_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new ChallanDetailsAdapter.PlaceTagListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChallanDetailsAdapter.PlaceTagListiewHolder holder, final int position) {
        String challan_no = "<b><font color=#000 >Book Name :  </font></b> <font color=#358ED3>"+messageEntities.get(position).BookName+"</font>";
        String challan_packet = "<b><font color=#000 >Total Packet :  </font></b> <font color=#358ED3>"+messageEntities.get(position).BOOK_NET_PRICE+"</font>";
        String challan_date = "<b><font color=#000 >Quantity :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_BOOK_QTY+"</font>";
        String challan_quantity = "<b><font color=#000 >Book No :  </font></b> <font color=#358ED3>"+messageEntities.get(position).F_BOOK_NO+"</font>";
        String challan_value = "<b><font color=#000 >Total Price :  </font></b> <font color=#358ED3>"+messageEntities.get(position).BOOK_NET_PRICE+"</font>";
        holder.text_challan_no.setText(Html.fromHtml(challan_no));
        holder.text_challan_date.setText(Html.fromHtml(challan_date));
       // holder.text_challan_packet.setText(Html.fromHtml(challan_packet));
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

    }

    @Override
    public int getItemCount() {

        return messageEntities.size();
    }



    public class PlaceTagListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_challan_no;
        private TextView text_challan_quantity;
        private TextView text_challan_packet;
        private TextView text_challan_date;
        private TextView text_total_value;
        private Button btn_receive;

        private View view_color;


        public PlaceTagListiewHolder(View itemView) {
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




}