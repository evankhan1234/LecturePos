package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xact.idea.lecturepos.Interface.BookItemInterface;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SpinnerFilter;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.PlaceTagListiewHolder> implements Filterable {


    SpinnerFilter filter;
    private Activity mActivity = null;
    public List<String> messageEntities;
    BookItemInterface bookItemInterfaces;


    public BookListAdapter(Activity activity, List<String> messageEntitie, BookItemInterface bookItemInterface) {
        mActivity = activity;
        this.messageEntities = messageEntitie;
        this.bookItemInterfaces = bookItemInterface;

    }


    @Override
    public BookListAdapter.PlaceTagListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new BookListAdapter.PlaceTagListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookListAdapter.PlaceTagListiewHolder holder, final int position) {
        // UserList messageEntitie= messageEntities.get(position);
        holder.text1.setText(messageEntities.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookItemInterfaces.Id((messageEntities.get(position)));
            }
        });


    }

    @Override
    public int getItemCount() {

        return messageEntities.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SpinnerFilter(messageEntities, this);
        }
        return filter;
    }

    public class PlaceTagListiewHolder extends RecyclerView.ViewHolder {

        private TextView text1;



        public PlaceTagListiewHolder(View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.text1);



        }
    }




}