package xact.idea.lecturepos.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

import xact.idea.lecturepos.LoginActivity;
import xact.idea.lecturepos.R;

public  class Utils {
    private static CustomProgressDialog sPdLoading = null;
    public static void showInfoDialog(final Context mContext) {

        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_pop_up_nav, null);

        infoDialog.setContentView(v);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout main_root = infoDialog.findViewById(R.id.main_root);
        Button btn_yes = infoDialog.findViewById(R.id.btn_ok);
        Button btn_no = infoDialog.findViewById(R.id.btn_cancel);

        CorrectSizeUtil.getInstance((Activity) mContext).correctSize(main_root);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceUtil.removeShared(mContext,SharedPreferenceUtil.TYPE_USER_ID);
                infoDialog.dismiss();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity) mContext).finish();
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
    public static double rounded(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static void dismissLoadingProgress() {


        if (CustomProgressDialog.sPdCount <= 1) {
            if (sPdLoading != null && sPdLoading.isShowing())
                sPdLoading.dismiss();
            CustomProgressDialog.sPdCount--;
        } else {
            CustomProgressDialog.sPdCount--;
        }
    }
    public static boolean broadcastIntent(Context context,View view) {
        // registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Snackbar snackbar = Snackbar
                    .make(view, "Connected Mobile Network", Snackbar.LENGTH_LONG);
            snackbar.show();
            return  true;
        }
        else  if ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            Snackbar snackbar = Snackbar
                    .make(view, "Connected WIFI Network", Snackbar.LENGTH_LONG);
            snackbar.show();
            return  true;
        }
        else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED){

            return  false;
        }

        return  false;
    }
    public static void showLoadingProgress(final Context context) {

        if (CustomProgressDialog.sPdCount <= 0) {
            CustomProgressDialog.sPdCount = 0;
            sPdLoading = null;
            try {
                sPdLoading = new CustomProgressDialog(context, R.style.CustomDialogTheme);
                if (!sPdLoading.isShowing())
                    sPdLoading.show();
                if (Build.VERSION.SDK_INT > 10) {
                    LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View loadingV = inflator.inflate(R.layout.layout_dialog_spinner, null);
                    CorrectSizeUtil.getInstance((Activity) context).correctSize(loadingV);
                    sPdLoading.setContentView(loadingV);
                } else {
                    String message = "Loading...";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            CustomProgressDialog.sPdCount++;
        } else {
            CustomProgressDialog.sPdCount++;
        }


    }

}
