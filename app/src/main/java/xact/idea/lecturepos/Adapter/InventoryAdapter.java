package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xact.idea.lecturepos.Database.Model.BookStock;
import xact.idea.lecturepos.Model.StockModel;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.CCDashboardListiewHolder> {


    private Activity mActivity = null;
    private List<StockModel> messageEntities;
    int row_index = 0;

    public InventoryAdapter(Activity activity, List<StockModel> messageEntitie) {
        mActivity = activity;
       messageEntities = messageEntitie;
        //mClick = mClicks;
        // clickInterface=clickInterfaces;
    }


    @Override
    public InventoryAdapter.CCDashboardListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_inventory, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new InventoryAdapter.CCDashboardListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InventoryAdapter.CCDashboardListiewHolder holder, final int position) {


        Log.e("Evan", "SDfs" + messageEntities.get(position).BookName);
       holder.text_name.setHint(String.valueOf(messageEntities.get(position).BookName));
       holder.text_quantity.setHint(String.valueOf(messageEntities.get(position).QTY_NUMBER));
       holder.text_mrp.setHint(String.valueOf(messageEntities.get(position).BOOK_NET_MRP));
       holder.text_net_price.setHint(String.valueOf(messageEntities.get(position).BOOK_NET_PRICES));






    }

    @Override
    public int getItemCount() {
        // Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }

    public class CCDashboardListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_name;
        private TextView text_quantity;
        private TextView text_net_price;
        private TextView text_mrp;


        public CCDashboardListiewHolder(View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.text_name);
            text_quantity = itemView.findViewById(R.id.text_quantity);
            text_net_price = itemView.findViewById(R.id.text_net_price);
            text_mrp = itemView.findViewById(R.id.text_mrp);


        }
    }
}