package com.bottomup.android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bottomup.android.R;
import com.bottomup.android.SwipeDisabledViewPager;
import com.bottomup.android.common.Config;
import com.bottomup.android.fragments.BooksFragment;
import com.bottomup.android.fragments.BottomupFragment;
import com.bottomup.android.fragments.LecturesFragment;
import com.bottomup.android.fragments.ProfileFragment;
import com.bottomup.android.fragments.ReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    private SwipeDisabledViewPager viewPager;
    private TextView titleTxt;

    private BooksFragment booksFragment;
    private BottomupFragment bottomupFragment;
    private LecturesFragment lecturesFragment;
    private ProfileFragment profileFragment;
    private ReportsFragment reportsFragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        booksFragment = new BooksFragment(new BooksFragment.ChangeBooksWebviewUrlListner() {
            @Override
            public void changedWebviewUrl(String url) {
                changeTabIndexFromUrl(url);
            }
        });
        bottomupFragment = new BottomupFragment(new BottomupFragment.ChangeBottomupWebviewUrlListner() {
            @Override
            public void changedWebviewUrl(String url) {
                changeTabIndexFromUrl(url);
            }
        });
        lecturesFragment = new LecturesFragment(new LecturesFragment.ChangeLecturesWebviewUrlListner() {
            @Override
            public void changedWebviewUrl(String url) {
                changeTabIndexFromUrl(url);
            }
        });
        profileFragment = new ProfileFragment(new ProfileFragment.ChangeProfileWebviewUrlListner() {
            @Override
            public void changedWebviewUrl(String url) {
                changeTabIndexFromUrl(url);
            }
        });
        reportsFragment = new ReportsFragment(new ReportsFragment.ChangeReportsWebviewUrlListner() {
            @Override
            public void changedWebviewUrl(String url) {
                changeTabIndexFromUrl(url);
            }
        });

        titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getResources().getString(R.string.bottomup_title));

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(5);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_category:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_video:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_favorite:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_profile:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_home);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 0) {
                    titleTxt.setText(getResources().getString(R.string.book_nav_title));
                } else if (viewPager.getCurrentItem() == 1) {
                    titleTxt.setText(getResources().getString(R.string.lecture_nav_title));
                } else if (viewPager.getCurrentItem() == 2) {
                    titleTxt.setText(getResources().getString(R.string.bottomup_title));
                } else if (viewPager.getCurrentItem() == 3) {
                    titleTxt.setText(getResources().getString(R.string.report_nav_title));
                } else if (viewPager.getCurrentItem() == 4) {
                    titleTxt.setText(getResources().getString(R.string.profile_nav_title));
                } else {
                    titleTxt.setText(R.string.app_name);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    booksFragment.refreshWebview();
                } else if (viewPager.getCurrentItem() == 1) {
                    lecturesFragment.refreshWebview();
                } else if (viewPager.getCurrentItem() == 2) {
                    bottomupFragment.refreshWebview();
                } else if (viewPager.getCurrentItem() == 3) {
                    reportsFragment.refreshWebview();
                } else if (viewPager.getCurrentItem() == 4) {
                    profileFragment.refreshWebview();
                }
            }
        });

        findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    void changeTabIndexFromUrl(String changedUrl){
        if (changedUrl.equals(Config.SERVER_URL)) {
            navigation.setSelectedItemId(R.id.navigation_home);
        }else if (changedUrl.equals(Config.FACEBOOK_URL)) {
            navigation.setSelectedItemId(R.id.navigation_category);
        }else if (changedUrl.equals(Config.SERVER_URL + Config.BOOKS_URL)) {
            navigation.setSelectedItemId(R.id.navigation_video);
        }else if (changedUrl.equals(Config.SCHEDULE_URL)) {
            navigation.setSelectedItemId(R.id.navigation_favorite);
        }else if (changedUrl.equals(Config.SERVER_URL + Config.PROFILE_URL)) {
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (item.getTitle().equals(getResources().getString(R.string.setting_title))) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("fromAc", 2);
            startActivity(intent);
        }else if (item.getTitle().equals(getResources().getString(R.string.terms_menu_title))) {
            Intent intent = new Intent(this, TermsAndUseActivity.class);
            intent.putExtra("fromAc", 0);
            startActivity(intent);
        }
        return false;
    }


    class MyAdapter extends FragmentStatePagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return booksFragment;
                case 1:
                    return lecturesFragment;
                case 2:
                    return bottomupFragment;
                case 3:
                    return reportsFragment;
                case 4:
                    return profileFragment;
                default:
                    throw new IllegalArgumentException("Unknown Tab!!");
            }
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
