package xact.idea.lecturepos.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import xact.idea.lecturepos.Adapter.BookListAdapter;
import xact.idea.lecturepos.Adapter.ItemAdapter;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Interface.BookItemInterface;
import xact.idea.lecturepos.InvoiceActivity;
import xact.idea.lecturepos.ItemActivity;
import xact.idea.lecturepos.ItemAdjustmentActivity;
import xact.idea.lecturepos.ItemReturnActivity;
import xact.idea.lecturepos.R;

public class SpinnerDialogFor implements Filterable {
    ArrayList<String> items;
    Activity context;
    String dTitle,closeTitle="Close";
    OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    int pos;
    int style;
    boolean cancellable=false;
    boolean showKeyboard=false;
    SpinnerFilter filter;
    ArrayAdapter adapter;
    BookListAdapter mAdapters;
    String values;
    String types;
    public SpinnerDialogFor(Activity activity, ArrayList<String> items, String dialogTitle,String value,String type) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.values = value;
        this.types = type;
    }


    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View v = context.getLayoutInflater().inflate(R.layout.layout_spinner, null);
        TextView rippleViewClose = (TextView) v.findViewById(R.id.close);
        TextView title = (TextView) v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText(closeTitle);
        title.setText(dTitle);
       // final ListView listView = (ListView) v.findViewById(R.id.list);
        final EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        if(isShowKeyboard()){
            showKeyboard(searchBox);
        }
        searchBox.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
        adapter = new ArrayAdapter<>(context, R.layout.items_view, items);
         RecyclerView rcl_this_customer_list =v. findViewById(R.id.rcl_this_customer_list);

        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        mAdapters = new BookListAdapter(context, items,bookItemInterface);

        rcl_this_customer_list.setAdapter(mAdapters);

       // listView.setAdapter(adapter);
        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.getWindow().getAttributes().windowAnimations = style;//R.style.DialogAnimations_SmileWindow;

//        rcl_this_customer_list.setON(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView t = (TextView) view.findViewById(R.id.text1);
//                for (int j = 0; j < items.size(); j++) {
//               //
//                    if (t.getText().toString().equalsIgnoreCase(items.get(j).toString())) {
//                        pos = j;
//                    }
//                }
//                onSpinerItemClick.onClick(t.getText().toString(), pos);
//                closeSpinerDialog();
//            }
//        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s= searchBox.getText().toString();

                mAdapters.getFilter().filter(searchBox.getText().toString());
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpinerDialog();
            }
        });
        try {
            alertDialog.setCancelable(isCancellable());
            alertDialog.setCanceledOnTouchOutside(isCancellable());
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BookItemInterface bookItemInterface = new BookItemInterface() {
        @Override
        public void Id(String value) {
            if (values.equals("S")){
                Book book = Common.bookRepository.getBook(value.substring(value.length() - 16));


                if (book != null) {
                    Intent intent = new Intent(context, ItemActivity.class);
                    intent.putExtra("EXTRA_SESSION", book.BARCODE_NUMBER);
                    context.startActivity(intent);
                    context.finish();
                    //infoDialog.dismiss();
                } else {
                    ///  Toast.makeText(InvoiceActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }
            }
           else if (values.equals("A")){
                Book book = Common.bookRepository.getBook(value.substring(value.length() - 16));


                if (book != null) {
                    Intent intent = new Intent(context, ItemAdjustmentActivity.class);
                    intent.putExtra("EXTRA_SESSION", book.BARCODE_NUMBER);
                    intent.putExtra("TYPE", types);
                    context.startActivity(intent);
                    context.finish();
                    //infoDialog.dismiss();
                } else {
                    ///  Toast.makeText(InvoiceActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }
            }
            else if (values.equals("R")) {
                Book book = Common.bookRepository.getBook(value.substring(value.length() - 16));


                if (book != null) {
                    Intent intent = new Intent(context, ItemReturnActivity.class);
                    intent.putExtra("EXTRA_SESSION", book.BARCODE_NUMBER);
                    context.startActivity(intent);
                    context.finish();
                    //infoDialog.dismiss();
                } else {
                    ///  Toast.makeText(InvoiceActivity.this, "No Books Found", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
    public void closeSpinerDialog() {
        hideKeyboard();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void hideKeyboard(){
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    private void showKeyboard(final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }

    private boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    private boolean isShowKeyboard() {
        return showKeyboard;
    }

    public void setShowKeyboard(boolean showKeyboard) {
        this.showKeyboard = showKeyboard;
    }





    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SpinnerFilter(items,mAdapters);
            Toast.makeText(context, "xvx", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "xvx", Toast.LENGTH_SHORT).show();
        }
        return filter;
    }
}
