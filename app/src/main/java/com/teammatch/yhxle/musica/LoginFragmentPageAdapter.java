package com.teammatch.yhxle.musica;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


/**
 * Created by yhxle on 10/30/2017.
 */

public class LoginFragmentPageAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tab_array;
    public LoginFragmentPageAdapter(FragmentManager fm) { super(fm); }
    public ArrayList<String> getTabs() { return tab_array; }
    public void setTabs(ArrayList<String> tabs) { this.tab_array = tabs; }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return LoginFragment.newInstance();
        }
        return SignupFragment.newInstance();
    }

    @Override
    public int getCount() {
        return tab_array.size();
    }
}
