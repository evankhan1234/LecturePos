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

import xact.idea.lecturepos.BarcodeActivity;
import xact.idea.lecturepos.Interface.ClickInterface;
import xact.idea.lecturepos.ItemActivity;
import xact.idea.lecturepos.Model.ItemModel;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemModelListiewHolder> {


    private Activity mActivity = null;
    private ClickInterface mClickInterface= null;
    private List<ItemModel> messageEntities;
    int row_index=0;
    //    ItemModelClickInterface ItemModelClickInterface;
    public ItemAdapter(Activity activity, List<ItemModel> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
     //   mClickInterface=ClickInterface;
        //mClick = mClicks;

    }


    @Override
    public ItemAdapter.ItemModelListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new ItemAdapter.ItemModelListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ItemModelListiewHolder holder, final int position) {

        //   int row_index;
        String text = "<b><font color=#000 >Book Name :  </font></b> <font color=#358ED3>"+messageEntities.get(position).BookName+"</font>";
        String text1 = "<b><font color=#000 >Discount :  </font></b> <font color=#358ED3>"+messageEntities.get(position).Discount+"</font>";
        String text4 = "<b><font color=#000 >Total :  </font></b> <font color=#358ED3>"+messageEntities.get(position).Amount+"</font>";
        String text2 = "<font color=#358ED3>"+messageEntities.get(position).Quantity+"</font> <b><font color=#000 > * BDT </font></b>";
        String text3 = "<font color=#358ED3>"+messageEntities.get(position).Price+"</font> ";
        holder.text_book.setText(Html.fromHtml(text));
        holder.text_quantity.setText(Html.fromHtml(text2));
        holder.text_price.setText(Html.fromHtml(text3));
        holder.text_discount.setText(Html.fromHtml(text1));
        holder.text_total.setText(Html.fromHtml(text4));
        holder.text_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ItemActivity.class);
                intent.putExtra("EXTRA_SESSION", "update");
                intent.putExtra("id", position);
                mActivity.startActivity(intent);

            }
        });
        holder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.arrayList.remove(position);
                notifyDataSetChanged();
            }
        });



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




        public ItemModelListiewHolder(View itemView) {
            super(itemView);

            text_book = itemView.findViewById(R.id.text_book);
            text_quantity = itemView.findViewById(R.id.text_quantity);
            text_price = itemView.findViewById(R.id.text_price);
            text_discount = itemView.findViewById(R.id.text_discount);
            text_total = itemView.findViewById(R.id.text_total);
            text_update = itemView.findViewById(R.id.text_update);
            text_delete = itemView.findViewById(R.id.text_delete);



        }
    }
}