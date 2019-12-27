package xact.idea.lecturepos.Utils;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import xact.idea.lecturepos.Adapter.BookListAdapter;
import xact.idea.lecturepos.Adapter.InventoryAdapter;
import xact.idea.lecturepos.Model.StockModel;

public class InventoryFilter extends Filter {
    private List<StockModel> items ;
    private InventoryAdapter adapter;

    public InventoryFilter(List<StockModel> filterList, InventoryAdapter adapter) {
        this.adapter = adapter;
        this.items = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<StockModel> filteredPlayers = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                String   test = constraint.toString().replaceAll("\\p{P}","");
                if (items.get(i).BookName.toUpperCase().contains(constraint.toString())) {
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
    SpinnerFilter filter;
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.messageEntities = (ArrayList<StockModel>) results.values;
        adapter.notifyDataSetChanged();
    }
}