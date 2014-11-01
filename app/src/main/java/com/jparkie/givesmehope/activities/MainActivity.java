package com.jparkie.givesmehope.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.fragments.HotFragment;
import com.jparkie.givesmehope.fragments.TrendingFragment;
import com.jparkie.givesmehope.fragments.VoteFragment;
import com.jparkie.givesmehope.views.SlidingTabLayout;

public class MainActivity extends ActionBarActivity {
    private MainPagerAdapter mMainPagerAdapter;

    private Toolbar mMainToolbar;
    private SlidingTabLayout mMainSlidingTabLayout;
    private ViewPager mMainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        mMainSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.mainSlidingTabs);
        mMainViewPager = (ViewPager)findViewById(R.id.mainViewPager);

        // Initialize mMainViewPager.
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mMainViewPager.setAdapter(mMainPagerAdapter);
        // This statement allows all the fragments to be retained in memory as much as possible such that on swipe, the fragment does not need to reload.
        mMainViewPager.setOffscreenPageLimit(mMainPagerAdapter.getCount() - 1);

        // Initialize mMainSlidingTabLayout.
        mMainSlidingTabLayout.setCustomTabView(R.layout.tab_layout_main, android.R.id.text1);
        mMainSlidingTabLayout.setDistributeEvenly(true);
        mMainSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accentPinkA200));
        mMainSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do Nothing.
            }

            @Override
            public void onPageSelected(int position) {
                actionSetTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do Nothing.
            }
        });
        mMainSlidingTabLayout.setViewPager(mMainViewPager);

        // Initialize mMainToolbar as the Action Bar.
        mMainToolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);
        mMainToolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(mMainToolbar);
        actionSetTitle(mMainViewPager.getCurrentItem());
    }

    private void actionSetTitle(int position) {
        switch (position) {
            case MainPagerAdapter.HOT_POSITION:
                getSupportActionBar().setTitle(R.string.fragment_title_hot);
                break;
            case MainPagerAdapter.TRENDING_POSITION:
                getSupportActionBar().setTitle(R.string.fragment_title_trending);
                break;
            case MainPagerAdapter.VOTE_POSITION:
                getSupportActionBar().setTitle(R.string.fragment_title_vote);
                break;
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        public final static int HOT_POSITION = 0;
        public final static int TRENDING_POSITION = 1;
        public final static int VOTE_POSITION = 2;

        public MainPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case HOT_POSITION:
                    return new HotFragment();
                case TRENDING_POSITION:
                    return new TrendingFragment();
                case VOTE_POSITION:
                    return new VoteFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable iconDrawable = null;

            switch (position) {
                case HOT_POSITION:
                    iconDrawable = getResources().getDrawable(R.drawable.ic_whatshot_white_24dp);
                    break;
                case TRENDING_POSITION:
                    iconDrawable = getResources().getDrawable(R.drawable.ic_trending_up_white_24dp);
                    break;
                case VOTE_POSITION:
                    iconDrawable = getResources().getDrawable(R.drawable.ic_thumbs_up_down_white_24dp);
                    break;
            }

            if (iconDrawable == null) {
                return null;
            } else {
                iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());

                SpannableString spannableString = new SpannableString(" ");
                ImageSpan imageSpan = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BOTTOM);

                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                return spannableString;
            }
        }
    }
}
