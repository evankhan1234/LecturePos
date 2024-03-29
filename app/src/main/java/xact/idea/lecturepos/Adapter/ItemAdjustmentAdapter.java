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

import java.util.List;

import xact.idea.lecturepos.AdjustmentActivity;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;
import xact.idea.lecturepos.Database.Model.Items;
import xact.idea.lecturepos.Interface.ClickInterface;
import xact.idea.lecturepos.InvoiceActivity;
import xact.idea.lecturepos.ItemActivity;
import xact.idea.lecturepos.ItemAdjustmentActivity;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class ItemAdjustmentAdapter extends RecyclerView.Adapter<ItemAdjustmentAdapter.ItemModelListiewHolder> {


    private Activity mActivity = null;
    private ClickInterface mClickInterface= null;
    private List<ItemAdjustment> messageEntities;
    boolean row_index=true;
    //    ItemModelClickInterface ItemModelClickInterface;
    public ItemAdjustmentAdapter(Activity activity, List<ItemAdjustment> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //   mClickInterface=ClickInterface;
        //mClick = mClicks;

    }


    @Override
    public ItemAdjustmentAdapter.ItemModelListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new ItemAdjustmentAdapter.ItemModelListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdjustmentAdapter.ItemModelListiewHolder holder, final int position) {

        //   int row_index;


        String text = messageEntities.get(position).BookNameBangla;

        String text2 = String.valueOf(messageEntities.get(position).Quantity);
        holder.text_book.setText(Html.fromHtml(text));
        holder.text_quantity.setText(Html.fromHtml(text2));
//        holder.text_price.setText(Html.fromHtml(text3));
//        holder.text_discount.setText(Html.fromHtml(text1));
//        holder.text_total.setText(Html.fromHtml(text4));
        holder.text_stock.setVisibility(View.GONE);
        holder.text_price.setVisibility(View.GONE);
        holder.text_discount.setVisibility(View.GONE);
        holder.text_total.setVisibility(View.GONE);


        holder.text_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ItemAdjustmentActivity.class);
                intent.putExtra("EXTRA_SESSION", "update");
                Log.e("BookId","BookId"+messageEntities.get(position).BookId);
                intent.putExtra("id", messageEntities.get(position).BookName);
                mActivity.startActivity(intent);

            }
        });
        holder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.itemAdjustmentRepository.emptyItemsById( messageEntities.get(position).id);




                notifyDataSetChanged();
               // ((AdjustmentActivity)mActivity).fixed();
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

    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }
    public class ItemModelListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_book;
        private TextView text_quantity;
        private TextView text_price;
        private TextView text_discount;
        private TextView text_total;
        private TextView text_update;
        private TextView text_delete;
        private TextView text_stock;
        private View view_color;




        public ItemModelListiewHolder(View itemView) {
            super(itemView);

            text_book = itemView.findViewById(R.id.text_book);
            text_quantity = itemView.findViewById(R.id.text_quantity);
            text_price = itemView.findViewById(R.id.text_price);
            text_discount = itemView.findViewById(R.id.text_discount);
            text_total = itemView.findViewById(R.id.text_total);
            text_update = itemView.findViewById(R.id.text_update);
            text_delete = itemView.findViewById(R.id.text_delete);
            view_color = itemView.findViewById(R.id.view_color);
            text_stock = itemView.findViewById(R.id.text_stock);



        }
    }
}