package com.example.researchbuddy.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.researchbuddy.R;
import com.example.researchbuddy.component.researcher.ui.project_page_fragment.InterviewFragment;
import com.example.researchbuddy.component.researcher.ui.project_page_fragment.ObservationsFragment;
import com.example.researchbuddy.component.researcher.ui.project_page_fragment.QuestionnaireFragment;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.questionnaires, R.string.observations, R.string.call_interviews};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below)
        switch (position){
            case 0:
                return QuestionnaireFragment.newInstance(0);
            case 1:
                return ObservationsFragment.newInstance(0);
            case 2:
                return InterviewFragment.newInstance(0);
            default:
                return QuestionnaireFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}