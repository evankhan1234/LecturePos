package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import xact.idea.lecturepos.InvoiceActivity;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Constant;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

import static xact.idea.lecturepos.Utils.Utils.rounded;

public class PrintSalesAdapter extends RecyclerView.Adapter<PrintSalesAdapter.SalesDetailPrintModelListiewHolder> {


    private Activity mActivity = null;
    private List<SalesDetailPrintModel> messageEntities;
    int row_index = 0;

    //    SalesDetailPrintModelClickInterface SalesDetailPrintModelClickInterface;
    public PrintSalesAdapter(Activity activity, List<SalesDetailPrintModel> messageEntitie) {
        mActivity = activity;
        messageEntities = messageEntitie;
        //mClick = mClicks;

    }


    @Override
    public PrintSalesAdapter.SalesDetailPrintModelListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_print_sales_details, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new PrintSalesAdapter.SalesDetailPrintModelListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PrintSalesAdapter.SalesDetailPrintModelListiewHolder holder, final int position) {

        //   int row_index;
       Log.e("SDFsf","SDfs"+messageEntities.get(position).BookName);
        holder.text_book.setText(messageEntities.get(position).BookNameBangla);
           holder.text_quantity.setText(String.valueOf(messageEntities.get(position).Quantity));
        double ss=Double.parseDouble(messageEntities.get(position).BookPrice)* (1-Double.parseDouble(messageEntities.get(position).Discount)/100);
       holder.text_rate.setText(String.valueOf(rounded(ss,2)));

        double price = messageEntities.get(position).Quantity * Double.parseDouble(messageEntities.get(position).BookPrice)* (1-Double.parseDouble(messageEntities.get(position).Discount)/100);
        holder.text_price.setText(String.valueOf(rounded(price,2)));

//        char[] ch = new char[(String.valueOf(messageEntities.get(position).Quantity).length())];
//
//        // Copy character by character into array
//        for (int i = 0; i < (String.valueOf(messageEntities.get(position).Quantity).length()); i++) {
//            ch[i] = (String.valueOf(messageEntities.get(position).Quantity).charAt(i));
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        // Printing content of array
//        for (char c : ch) {
//            if (c == '1') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('1', '১');
//                stringBuilder.append(replaceString);
//            } else if (c == '2') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('2', '২');
//                stringBuilder.append(replaceString);
//            } else if (c == '3') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('3', '৩');
//                stringBuilder.append(replaceString);
//            } else if (c == '4') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('4', '৪');
//                stringBuilder.append(replaceString);
//            } else if (c == '5') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('5', '৫');
//                stringBuilder.append(replaceString);
//            } else if (c == '6') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('6', '৬');
//                stringBuilder.append(replaceString);
//            } else if (c == '7') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('7', '৭');
//                stringBuilder.append(replaceString);
//            } else if (c == '8') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('8', '৮');
//                stringBuilder.append(replaceString);
//            } else if (c == '9') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('9', '৯');
//                stringBuilder.append(replaceString);
//            } else if (c == '0') {
//                String s = String.valueOf(c);
//                String replaceString;
//                replaceString = s.replace('0', '০');
//                stringBuilder.append(replaceString);
//            }
//
//            String q = stringBuilder.toString();
//            holder.text_quantity.setText(q);
//
//            /////////////////////////////
//            char[] chBookPrice = new char[(String.valueOf(messageEntities.get(position).BookPrice).length())];
//
//            // Copy character by character into array
//            for (int i = 0; i < (String.valueOf(messageEntities.get(position).BookPrice).length()); i++) {
//                chBookPrice[i] = (String.valueOf(messageEntities.get(position).BookPrice).charAt(i));
//            }
//
//            StringBuilder stringBuilderBookPrice = new StringBuilder();
//            // Printing content of array
//            for (char c1 : chBookPrice) {
//                if (c1 == '1') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('1', '১');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '2') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('2', '২');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '3') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('3', '৩');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '4') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('4', '৪');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '5') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('5', '৫');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '6') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('6', '৬');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '7') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('7', '৭');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '8') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('8', '৮');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '9') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('9', '৯');
//                    stringBuilderBookPrice.append(replaceString);
//                } else if (c1 == '0') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('0', '০');
//                    stringBuilderBookPrice.append(replaceString);
//                }
//                else if (c1 == '.') {
//                    String s = String.valueOf(c1);
//                    String replaceString;
//                    replaceString = s.replace('.', '.');
//                    stringBuilderBookPrice.append(replaceString);
//                }
//                String prices = stringBuilderBookPrice.toString();
//                holder.text_rate.setText(prices);
//            }
//
//            /////////////////////////////
//            char[] chBookTotalPrice = new char[(String.valueOf(price).length())];
//
//            // Copy character by character into array
//            for (int i = 0; i < (String.valueOf(price).length()); i++) {
//                chBookTotalPrice[i] = (String.valueOf(price).charAt(i));
//            }
//
//            StringBuilder stringBuilderBookTotalPrice = new StringBuilder();
//            // Printing content of array
//            for (char c2 : chBookTotalPrice) {
//                if (c2 == '1') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('1', '১');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '2') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('2', '২');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '3') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('3', '৩');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '4') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('4', '৪');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '5') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('5', '৫');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '6') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('6', '৬');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '7') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('7', '৭');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '8') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('8', '৮');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '9') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('9', '৯');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                } else if (c2 == '0') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('0', '০');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                }
//                else if (c2 == '.') {
//                    String s = String.valueOf(c2);
//                    String replaceString;
//                    replaceString = s.replace('.', '.');
//                    stringBuilderBookTotalPrice.append(replaceString);
//                }
//
//                String prices = stringBuilderBookTotalPrice.toString();
//                holder.text_price.setText(prices);
//           }
       // }
//        if (messageEntities.get(position).Quantity))
//        String replaceString=s1.replace('a','e');


    }

    @Override
    public int getItemCount() {
        Log.e("evan", "sd" + messageEntities.size());
        return messageEntities.size();
    }

    public class SalesDetailPrintModelListiewHolder extends RecyclerView.ViewHolder {

        private TextView text_book;
        private TextView text_quantity;
        private TextView text_rate;
        private TextView text_price;


        public SalesDetailPrintModelListiewHolder(View itemView) {
            super(itemView);

            text_book = itemView.findViewById(R.id.text_book);
            text_quantity = itemView.findViewById(R.id.text_quantity);
            text_rate = itemView.findViewById(R.id.text_rate);
            text_price = itemView.findViewById(R.id.text_price);


        }
    }
}