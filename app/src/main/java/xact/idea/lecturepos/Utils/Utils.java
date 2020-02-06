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

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                SharedPreferenceUtil.removeShared(mContext,SharedPreferenceUtil.TYPE_USER_ID);
                infoDialog.dismiss();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity) mContext).finishAffinity();
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
    public static String getValue(String value)
    {
        char[] chBookTotalPrice = new char[(String.valueOf(value).length())];
//
        // Copy character by character into array
        for (int i = 0; i < (String.valueOf(value).length()); i++) {
            chBookTotalPrice[i] = (String.valueOf(value).charAt(i));
        }

        StringBuilder stringBuilderBookTotalPrice = new StringBuilder();
        // Printing content of array
        for (char c2 : chBookTotalPrice) {
            if (c2 == '1') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('1', '১');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '2') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('2', '২');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '3') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('3', '৩');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '4') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('4', '৪');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '5') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('5', '৫');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '6') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('6', '৬');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '7') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('7', '৭');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '8') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('8', '৮');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '9') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('9', '৯');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '0') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('0', '০');
                stringBuilderBookTotalPrice.append(replaceString);
            } else if (c2 == '.') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('.', '.');
                stringBuilderBookTotalPrice.append(replaceString);
            }
            else if (c2 == ',') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace(',', ',');
                stringBuilderBookTotalPrice.append(replaceString);
            }
            else if (c2 == '-') {
                String s = String.valueOf(c2);
                String replaceString;
                replaceString = s.replace('.', '.');
                stringBuilderBookTotalPrice.append(replaceString);
            }


        }
        String prices = stringBuilderBookTotalPrice.toString();
        return prices;
    }
    public static String decimal(double value) {
        DecimalFormat df = new DecimalFormat("#.00");
        String angleFormated = df.format(value);
        return angleFormated;
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
    public static String getCommaSeperatorValue(double price){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#,##,##,###.00");
        String output = df.format(price);
        return output+" Tk";

    }
    public static String getCommaValue(double price){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#,##,##,###.00");
        String output = df.format(price);
        return output+" Tk";

    }
    public static String getOnlyCommaValue(double price){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#,##,##,###.00");
        String output = df.format(price);
        return output;

    }
}
