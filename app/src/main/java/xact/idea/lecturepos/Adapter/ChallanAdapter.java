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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.LoginActivity;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.CustomDialog;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class ChallanAdapter extends RecyclerView.Adapter<ChallanAdapter.ChallanListiewHolder> {


    private Activity mActivity = null;
    private List<Challan> messageEntities;

    boolean row_index=true;
    //    ChallanClickInterface ChallanClickInterface;
    public ChallanAdapter(Activity activity, List<Challan> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //mClick = mClicks;

    }


    @Override
    public ChallanAdapter.ChallanListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_challan_new_list, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new ChallanAdapter.ChallanListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChallanAdapter.ChallanListiewHolder holder, final int position) {
        String challan_no = "<b><font color=#000 >Challan No :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_NO+"</font>";
        String challan_packet = "<b><font color=#000 >Total Packet :  </font></b> <font color=#358ED3>"+messageEntities.get(position).NO_OF_PACKATE+"</font>";
        String challan_date = "<b><font color=#000 >Challan Date :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_DATE+"</font>";
        String challan_quantity = "<b><font color=#000 >Challan Quantity :  </font></b> <font color=#358ED3>"+messageEntities.get(position).CHALLAN_QTY+"</font>";
        String challan_value = "<b><font color=#000 >Total Price :  </font></b> <font color=#358ED3>"+messageEntities.get(position).TOTAL_VALUE+"</font>";
        holder.text_challan_no.setText(Html.fromHtml(challan_no));
        holder.text_challan_date.setText(Html.fromHtml(challan_date));
        holder.text_challan_packet.setText(Html.fromHtml(challan_packet));
        holder.text_total_value.setText(Html.fromHtml(challan_value));
        holder.text_challan_quantity.setText(Html.fromHtml(challan_quantity));
        holder.btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(mActivity,position);

            }

        });
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
    public  void showInfoDialog(final Context mContext,final int position) {

        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_pop_up_nav, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        TextView tv_info = infoDialog.findViewById(R.id.tv_info);
        Button btn_yes = infoDialog.findViewById(R.id.btn_ok);
        Button btn_no = infoDialog.findViewById(R.id.btn_cancel);
        tv_info.setText("Are you want to receive ??");

        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = formatter.format(date);
                SimpleDateFormat formatters = new SimpleDateFormat("hh:mm a");
                Date dates = new Date(System.currentTimeMillis());
                String currentTime = formatters.format(dates);
                Common.challanRepositoy.updateReciver("Y",messageEntities.get(position).CHALLAN_NO,currentDate+" "+currentTime,currentDate);
                notifyDataSetChanged();
                infoDialog.dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        infoDialog.show();
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
            btn_receive = itemView.findViewById(R.id.btn_receive);
            view_color = itemView.findViewById(R.id.view_color);


        }
    }
}