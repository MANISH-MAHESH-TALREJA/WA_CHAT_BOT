package net.manish.wabot.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import net.manish.wabot.R;
import net.manish.wabot.adapter.TabLayoutAdapter;
import net.manish.wabot.databinding.ActivityMainBinding;

import java.util.Objects;


public class MainActivity extends AppCompatActivity
{
    public Context context;
    ActivityMainBinding activityMainBinding;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;
        final Menu menu = activityMainBinding.bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++)
        {
            SpannableString spannableString = new SpannableString(menu.getItem(i).getTitle());
            spannableString.setSpan(new MenuSpannable(MainActivity.this), 0, spannableString.length(), 0);
            menu.getItem(i).setTitle(spannableString);
        }
        setSupportActionBar(activityMainBinding.mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        tabLayout();
        activityMainBinding.imgSendDirectMsg.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SendDirectMessageActivity.class)));

        activityMainBinding.menuSettings.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }


    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setNegativeButton("Cancel", null).setPositiveButton("Yes", (dialogInterface, i) ->
        {
            MainActivity.super.onBackPressed();
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).create().show();
    }


    private void tabLayout()
    {
        activityMainBinding.mainViewPager.setAdapter(new TabLayoutAdapter(getSupportFragmentManager(), 4));
        activityMainBinding.bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            int itemId = item.getItemId();
            if (itemId == R.id.menuHome)
            {
                activityMainBinding.mainViewPager.setCurrentItem(0);
            } else if (itemId == R.id.menuCustom)
            {
                activityMainBinding.mainViewPager.setCurrentItem(1);
            } else if (itemId == R.id.menuContacts)
            {
                activityMainBinding.mainViewPager.setCurrentItem(2);
            } else if (itemId == R.id.menuStatistic)
            {
                activityMainBinding.mainViewPager.setCurrentItem(3);
            }
            return true;
        });

        activityMainBinding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        activityMainBinding.bottomNavigationView.getMenu().findItem(R.id.menuHome).setChecked(true);
                        break;
                    case 1:
                        activityMainBinding.bottomNavigationView.getMenu().findItem(R.id.menuCustom).setChecked(true);
                        break;
                    case 2:
                        activityMainBinding.bottomNavigationView.getMenu().findItem(R.id.menuContacts).setChecked(true);
                        break;
                    case 3:
                        activityMainBinding.bottomNavigationView.getMenu().findItem(R.id.menuStatistic).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }


    private static class MenuSpannable extends MetricAffectingSpan
    {
        int color = R.color.actionBarColor;
        int size = 30;
        Context context;

        public MenuSpannable(Context context)
        {
            this.context = context;
        }

        @Override
        public void updateMeasureState(TextPaint p)
        {
            p.setColor(context.getResources().getColor(color));
            p.setTextSize(size);
            p.setLetterSpacing((float) 0.01);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_regular);
            p.setTypeface(typeface);

        }

        @Override
        public void updateDrawState(TextPaint tp)
        {
            tp.setColor(context.getResources().getColor(color));
            tp.setTextSize(size);
            tp.setLetterSpacing((float) 0.01);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_regular);
            tp.setTypeface(typeface);

        }
    }
}
