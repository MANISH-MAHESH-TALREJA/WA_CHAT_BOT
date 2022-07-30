package net.manish.wabot.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import net.manish.wabot.fragment.ContactsFragment;
import net.manish.wabot.fragment.CustomReplyFragment;
import net.manish.wabot.fragment.HomeFragment;
import net.manish.wabot.fragment.StatisticsFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public TabLayoutAdapter(Context context2, FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        context = context2;
        totalTabs = i;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new HomeFragment();
        }
        if (i == 1) {
            return new CustomReplyFragment();
        }
        if (i == 2) {
            return new ContactsFragment();
        }
        if (i != 3) {
            return null;
        }
        return new StatisticsFragment();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
