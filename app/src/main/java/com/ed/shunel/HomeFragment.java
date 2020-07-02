package com.ed.shunel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ed.shunel.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

//ppp
    private final static String TAG="HomeFragment";
    private FragmentActivity activity;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    private int[] label={R.string.Popular_product,R.string.Promotion,R.string.All,R.string.Perfume_necklace,R.string.Fragrance_earrings,R.string.Necklace,R.string.Earrings};




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tabs);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(label[position]);
                    }
                }).attach();
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }


    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity);
        return adapter;
    }



    //ViewPager2滑動動畫
    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {

        private static final float MIN_SCALE = 0.85f ;
        private static final float MIN_ALPHA = 0.5f ;

        public void transformPage ( View view , float position ) {
            int pageWidth = view . getWidth ();
            int pageHeight = view . getHeight ();

            if ( position < - 1 ) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view . setAlpha ( 0f );

            } else if ( position <= 1 ) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math . max ( MIN_SCALE , 1 - Math . abs ( position ));
                float vertMargin = pageHeight * ( 1 - scaleFactor ) / 2 ;
                float horzMargin = pageWidth * ( 1 - scaleFactor ) / 2 ;
                if ( position < 0 ) {
                    view . setTranslationX ( horzMargin - vertMargin / 2 );
                } else {
                    view . setTranslationX (- horzMargin + vertMargin / 2 );
                }

                // Scale the page down (between MIN_SCALE and 1)
                view . setScaleX ( scaleFactor );
                view . setScaleY ( scaleFactor );

                // Fade the page relative to its size.
                view . setAlpha ( MIN_ALPHA +
                        ( scaleFactor - MIN_SCALE ) /
                                ( 1 - MIN_SCALE ) * ( 1 - MIN_ALPHA ));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view . setAlpha ( 0f );
            }
        }


    }
}
