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
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
//    private static int[] TAB_TITLES = new int[]{R.string.questionnaires, R.string.observations, R.string.call_interviews};
//    private static int[] TAB_TITLES = new int[]{R.string.questionnaires, R.string.observations, R.string.call_interviews};
    private Context mContext;
    private ProjectModel project;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ProjectModel project) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        this.project = project;
    }

    @Override
    public Fragment getItem(int position) {
/*        switch (position) {
            case 0:
                return QuestionnaireFragment.newInstance(mContext, 0);
            case 1:
                return ObservationsFragment.newInstance(0);
            case 2:
                return InterviewFragment.newInstance(0);
            default:
                return QuestionnaireFragment.newInstance(mContext, position + 1);
        }*/
        return  matchFragmentToCollectionTYpe(project.getCollectionTypes().get(position), position);
    }

    private Fragment matchFragmentToCollectionTYpe(CollectionTypes collectionType, int position) {

        switch (collectionType) {
            case CALL_INTERVIEW:
                return InterviewFragment.newInstance(position);
            case OBSERVATION:
                return ObservationsFragment.newInstance(mContext, position);
            case FORMS:
                return QuestionnaireFragment.newInstance(mContext, position);
            default:
                return QuestionnaireFragment.newInstance(mContext, position);

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
        return project.getCollectionTypes().get(position).toString();
    }

    @Override
    public int getCount() {
        return project.getCollectionTypes().size();
    }
}