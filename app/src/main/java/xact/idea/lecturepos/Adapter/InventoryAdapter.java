package xact.idea.lecturepos.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.CCDashboardListiewHolder> {


    private Activity mActivity = null;
    //  private List<Department> messageEntities;
    int row_index = 0;

    public InventoryAdapter(Activity activity) {
        mActivity = activity;
        //messageEntities = messageEntitie;
        //mClick = mClicks;
        // clickInterface=clickInterfaces;
    }


    @Override
    public InventoryAdapter.CCDashboardListiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_inventory, null);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);


        return new InventoryAdapter.CCDashboardListiewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InventoryAdapter.CCDashboardListiewHolder holder, final int position) {


        //    Log.e("Evan", "SDfs" + messageEntities.get(position));
        //  holder.btn_department.setHint(messageEntities.get(position).DepartmentName);






    }

    @Override
    public int getItemCount() {
        // Log.e("evan", "sd" + messageEntities.size());
        return 20;
    }

    public class CCDashboardListiewHolder extends RecyclerView.ViewHolder {

        private Button btn_department;


        public CCDashboardListiewHolder(View itemView) {
            super(itemView);

            //  btn_department = itemView.findViewById(R.id.btn_department);


        }
    }
}