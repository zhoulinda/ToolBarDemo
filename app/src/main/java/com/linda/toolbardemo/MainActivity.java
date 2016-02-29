package com.linda.toolbardemo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhoulinda on 16/2/29.
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.mToolbar)
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();

    }

    private void initView(){
//        StatusBarCompat.compat(this,R.color.colorPrimary);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }
}
