package net.manish.wabot.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.adapter.TabLayoutAdapter;
import net.manish.wabot.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private int MULTIPLE_PERMISSIONS = 100;
    public Context context;
    private SharedPreference preference;
    ActivityMainBinding thisb;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;
        preference = new SharedPreference(this);
        final Menu menu = thisb.bottomNavigationView.getMenu();
        for(int i = 0; i < menu.size(); i++) {
            SpannableString spannableString = new SpannableString(menu.getItem(i).getTitle());
            spannableString.setSpan(new MenuSpannable(MainActivity.this),0,spannableString.length(),0);
            menu.getItem(i).setTitle(spannableString);
        }
        setSupportActionBar(thisb.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabLayout();
        thisb.imgSendDirectMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SendDirectMessageActivity.class));
                //TMAdsUtils.loadInterAd(context);
                //TMAdsUtils.showInterAd(context,new Intent(MainActivity.this, SendDirectMessageActivity.class));

            }
        });

        thisb.menuWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                //TMAdsUtils.loadInterAd(context);
                //TMAdsUtils.showInterAd(context,new Intent(MainActivity.this, WebActivity.class));
            }
        });
        thisb.menuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, settingsActivity.class));
                //TMAdsUtils.loadInterAd(context);
                //TMAdsUtils.showInterAd(context,new Intent(MainActivity.this, settingsActivity.class));
            }
        });
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).create().show();
    }



    private void tabLayout() {
        thisb.mainViewPager.setAdapter(new TabLayoutAdapter(this, getSupportFragmentManager(), 4));
        thisb.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.menuHome:
                        thisb.mainViewPager.setCurrentItem(0);
                        break;
                    case R.id.menuCustom:
                        thisb.mainViewPager.setCurrentItem(1);
                        break;
                    case R.id.menuContacts:
                        thisb.mainViewPager.setCurrentItem(2);
                        break;
                    case R.id.menuStatistic:
                        thisb.mainViewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });
        thisb.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                      thisb.bottomNavigationView.getMenu().findItem(R.id.menuHome).setChecked(true);
                        break;
                      case 1:
                      thisb.bottomNavigationView.getMenu().findItem(R.id.menuCustom).setChecked(true);
                          break;
                      case 2:
                      thisb.bottomNavigationView.getMenu().findItem(R.id.menuContacts).setChecked(true);
                          break;
                      case 3:
                      thisb.bottomNavigationView.getMenu().findItem(R.id.menuStatistic).setChecked(true);
                          break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private static class MenuSpannable extends MetricAffectingSpan {
        int color = R.color.actionBarColor;
        int size = 40;
        Context context;

        public MenuSpannable(Context context) {
             this.context=context;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setColor(context.getResources().getColor(color));
            p.setTextSize(size);
            p.setLetterSpacing((float) 0.01);
            Typeface typeface= ResourcesCompat.getFont(context, R.font.outfit_semibold);
            p.setTypeface(typeface);

        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor( context.getResources().getColor(color));
            tp.setTextSize(size);
            tp.setLetterSpacing((float) 0.01);
            Typeface typeface= ResourcesCompat.getFont(context, R.font.outfit_semibold);
            tp.setTypeface(typeface);

        }
        private void setSelected(boolean selected){
            if(selected){
                color = R.color.actionBarColor;
                size = 40;
            }else{
                color = R.color.disable_color;
                size = 20;
            }
        }
    }
}
