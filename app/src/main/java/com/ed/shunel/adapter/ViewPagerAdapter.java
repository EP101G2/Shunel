package com.ed.shunel.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ed.shunel.AllProductFragment;
import com.ed.shunel.EarringsFragment;
import com.ed.shunel.Fragrance_earringsFragment;
import com.ed.shunel.HotProductFragment;
import com.ed.shunel.LabelFragment;
import com.ed.shunel.NecklaceFragment;
import com.ed.shunel.NoticeFragment;
import com.ed.shunel.Perfume_necklaceFragment;
import com.ed.shunel.RingProductFragment;
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
            case 0: //熱門
                return new HotProductFragment();
            case 1: //促銷
                return new SettingFragment();
            case 2: //全部  flag=3
                return new AllProductFragment();
            case 3: //香氛項鍊 flag=4
                return new Perfume_necklaceFragment();
            case 4: //香氛耳環 flag=5
                return new Fragrance_earringsFragment();
            case 5: //項鍊 flag=6
                return new NecklaceFragment();
            case 6: //耳環 flag=7
                return new EarringsFragment();
            case 7: //戒指 flag=8
                return new RingProductFragment();
        }


        return LabelFragment.newInstance(position);
    }

}
