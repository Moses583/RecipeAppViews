package com.ravemaster.recipeapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ravemaster.recipeapp.fragments.FeedFragment;
import com.ravemaster.recipeapp.fragments.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FeedFragment();
            case 1:
                return new SearchFragment();
            default:
                return new FeedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
