package xact.idea.lecturepos.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import xact.idea.lecturepos.Fragment.ChallanListFragment;
import xact.idea.lecturepos.Fragment.ChallanRecieveListFragment;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ChallanListFragment tab1 = new ChallanListFragment();
                return tab1;
            case 1:
                ChallanRecieveListFragment tab2 = new ChallanRecieveListFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}