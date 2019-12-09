package xact.idea.lecturepos.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xact.idea.lecturepos.Adapter.ChallanAdapter;
import xact.idea.lecturepos.Adapter.ChallanReceiveAdapter;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.R;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;


public class ChallanRecieveListFragment extends Fragment {
    Activity mActivity;
    CorrectSizeUtil correctSizeUtil;
     CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView rcl_this_customer_list;
    ChallanReceiveAdapter mAdapters;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_challan_recieve_list, container, false);

        mActivity=getActivity();
        correctSizeUtil= correctSizeUtil.getInstance(getActivity());
        correctSizeUtil.setWidthOriginal(1080);
        correctSizeUtil.correctSize(view);
        rcl_this_customer_list=view.findViewById(R.id.rcl_this_customer_list);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_this_customer_list.setLayoutManager(lm);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChallan();
    }

    private void displayChallanItems(List<Challan> challans) {
        //  showLoadingProgress(mActivity);
        mAdapters = new ChallanReceiveAdapter(mActivity, challans);

        rcl_this_customer_list.setAdapter(mAdapters);


    }
    private  void loadChallan() {

        compositeDisposable.add(Common.challanRepositoy.getList("Y").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Challan>>() {
            @Override
            public void accept(List<Challan> customers) throws Exception {
                Log.e("SDfd","Dgd"+customers);
                displayChallanItems(customers);
            }
        }));

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}
