package xact.idea.lecturepos.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xact.idea.lecturepos.Adapter.CustomerListAdadpter;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Interface.CustomerInterface;
import xact.idea.lecturepos.R;


public class SpinnerDialogCustomer implements Filterable {

    List<Customer> items;
    List<String> item;
    Activity context;
    String dTitle, closeTitle = "Close";
    CustomerInterface onSpinerItemClick;
    AlertDialog alertDialog;
    int pos;
    int style;
    boolean cancellable = false;
    boolean showKeyboard = false;
    CustomerListAdadpter mAdapters;
    SpinnerFilterForCustomer filter;
    CustomerInterface customerInterface;
    public SpinnerDialogCustomer(Activity activity, List<Customer> items,List<String> item, String dialogTitle,CustomerInterface customerInterface) {
        this.items = items;
        this.item = item;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.dTitle = dialogTitle;
        this.dTitle = dialogTitle;
        this.customerInterface = customerInterface;
    }



    public void bindOnSpinerListener(CustomerInterface onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }


    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View v = context.getLayoutInflater().inflate(R.layout.layout_spinner, null);
        TextView rippleViewClose = (TextView) v.findViewById(R.id.close);
        TextView title = (TextView) v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText(closeTitle);
        title.setText(dTitle);
        final ListView listView = (ListView) v.findViewById(R.id.list);
        final EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        if (isShowKeyboard()) {
            showKeyboard(searchBox);
        }
//        for (Customer customer : items){
//            item.add(customer.ShopName +" "+ customer.Name+" "+ customer.MobileNumber);
//
//        }
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.items_view, item);
////        listView.setAdapter(adapter);

        RecyclerView rcl_this_customer_list =v. findViewById(R.id.rcl_this_customer_list);

        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        mAdapters = new CustomerListAdadpter(context, items,item,customerInterface);

        rcl_this_customer_list.setAdapter(mAdapters);
        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.getWindow().getAttributes().windowAnimations = style;//R.style.DialogAnimations_SmileWindow;

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView t = (TextView) view.findViewById(R.id.text1);
//                for (int j = 0; j < items.size(); j++) {
//                    if (t.getText().toString().equalsIgnoreCase(items.get(j).toString())) {
//                        pos = j;
//                    }
//                }
//                onSpinerItemClick.onClick(items.get(pos).ShopName, pos);
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
                mAdapters.getFilter().filter(searchBox.getText().toString());
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpinerDialog();
            }
        });
        alertDialog.setCancelable(isCancellable());
        alertDialog.setCanceledOnTouchOutside(isCancellable());
        alertDialog.show();
    }

    public void closeSpinerDialog() {
        hideKeyboard();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    private void showKeyboard(final EditText ettext) {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext, 0);
                               }
                           }
                , 200);
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
            filter = new SpinnerFilterForCustomer(item,mAdapters);
            Toast.makeText(context, "xvx", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "xvx", Toast.LENGTH_SHORT).show();
        }
        return filter;
    }
}
