package com.ed.shunel.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ed.shunel.AllProductFragment;
import com.ed.shunel.HotProductFragment;
import com.ed.shunel.LabelFragment;
import com.ed.shunel.NoticeFragment;
import com.ed.shunel.SettingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 8;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new HotProductFragment();
            case 1:
                return new SettingFragment();
            case 2:
                return new AllProductFragment();
        }


        return LabelFragment.newInstance(position);
    }

}
