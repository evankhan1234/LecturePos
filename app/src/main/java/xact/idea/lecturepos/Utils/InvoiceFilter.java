package xact.idea.lecturepos.Utils;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import xact.idea.lecturepos.Adapter.BookListAdapter;
import xact.idea.lecturepos.Adapter.InvoiceAdapter;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public class InvoiceFilter extends Filter {
    private List<SalesMaster> items ;
    private InvoiceAdapter adapter;

    public InvoiceFilter(List<SalesMaster> filterList, InvoiceAdapter adapter) {
        this.adapter = adapter;
        this.items = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<SalesMaster> filteredPlayers = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                String   test = constraint.toString().replaceAll("\\p{P}","");
                if (items.get(i).InvoiceNumber.toUpperCase().contains(constraint.toString())) {
                    filteredPlayers.add(items.get(i));
                }
            }
            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        } else {
            results.count = items.size();
            results.values = items;
        }
        return results;
    }
    InvoiceFilter filter;
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.messageEntities = (ArrayList<SalesMaster>) results.values;
        adapter.notifyDataSetChanged();
    }
}