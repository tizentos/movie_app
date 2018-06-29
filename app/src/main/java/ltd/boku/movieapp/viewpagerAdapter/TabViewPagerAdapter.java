package ltd.boku.movieapp.viewpagerAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import ltd.boku.movieapp.fragments.ReviewFragment;
import ltd.boku.movieapp.fragments.TrailerFragment;

public class TabViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "TabViewPagerAdapter";

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle: entering: "+ position);
        switch(position){
            case 0:
                return "Reviews";
            case 1:
                return "Trailers";
        }
        return null;

    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: entering : " + position);
        switch(position){
            case 0:
                Log.d(TAG, "getItem: in review");
                ReviewFragment reviewFragment=new ReviewFragment();
                return  reviewFragment;
            case 1:
                Log.d(TAG, "getItem: in trailer");
                TrailerFragment trailerFragment=new TrailerFragment();
                return trailerFragment;
        }
        Log.d(TAG, "getItem: null");
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
