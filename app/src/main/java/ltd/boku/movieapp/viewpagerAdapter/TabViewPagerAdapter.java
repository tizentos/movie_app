package ltd.boku.movieapp.viewpagerAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import ltd.boku.movieapp.fragments.ReviewFragment;
import ltd.boku.movieapp.fragments.TrailerFragment;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
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
        switch(position){
            case 0:
                ReviewFragment reviewFragment=new ReviewFragment();
                return  reviewFragment;
            case 1:
                TrailerFragment trailerFragment=new TrailerFragment();
                return trailerFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
