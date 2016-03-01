package com.linda.toolbardemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by zhoulinda on 16/2/29.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView(){
//        StatusBarCompat.compat(this,R.color.colorPrimary);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        // App Logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        // Title
        toolbar.setTitle("My Title");
        // Sub Title
        toolbar.setSubtitle("Sub title");

        setSupportActionBar(toolbar);

        // Navigation icon 要设定在 setSupoortActionBar() 之后才起作用

        // 否则会出现 back button
//        toolbar.setNavigationIcon(R.mipmap.ab_android);

        // Menu item click 的监听事件一样要设定在 setSupportActionBar() 之后才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        ActionBar bar = getSupportActionBar();
        if (bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        initTabsValue();//配置PagerSlidingTabStrip
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(viewPager);

        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                colorChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * mPagerSlidingTabStrip默认值配置
     *
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                SuperAwesomeCardFragment.getBackgroundBitmapPosition(position));
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null){

				    /* 界面颜色UI统一性处理,看起来更Material一些 */
                    mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                    mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                    // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                    mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));

                    toolbar.setBackgroundColor(vibrant.getRgb());
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        // 很明显，这两货是新API才有的。
                        window.setStatusBarColor(colorBurn(vibrant.getRgb()));
//                    window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                    }
                }
            }
        });
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues
     *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *            Android中我们一般使用它的16进制，
     *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    };

    /* ***************FragmentPagerAdapter***************** */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "分类", "主页", "热门推荐", "热门收藏", "本月热榜", "今日热榜", "专栏", "随机" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SuperAwesomeCardFragment.newInstance(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }
}
