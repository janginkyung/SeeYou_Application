package orgm.androidtown.app_location.view.activity;
//implements  android.support.v7.app.ActionBar.TabListener

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import orgm.androidtown.app_location.FriendLocation;
import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.singleton.Tdata;
import orgm.androidtown.app_location.view.fragment.FragmentTmap;
import orgm.androidtown.app_location.view.fragment.Fourth;
import orgm.androidtown.app_location.view.fragment.FragmentFriendList;
import orgm.androidtown.app_location.view.fragment.FragmentKeywordSearch;



public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {
    FragmentTmap fragment1;
    Fourth fragment2;
    FragmentKeywordSearch fragmentKeywordSearch;
    FragmentFriendList fragmentFriendList;
    //fragment_second_first fragment3;
    LinearLayout menu1, menu2, find;


    FriendLocation friendLocation;
    android.support.v7.app.ActionBar bar;
    String url;
    EditText edit;

    private android.support.v7.app.ActionBar.Tab tabMap;
    private android.support.v7.app.ActionBar.Tab tabFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bar = getSupportActionBar();
        setContentView(R.layout.activity_main);
        menu2 = (LinearLayout) findViewById(R.id.linearlayout3);
        find = (LinearLayout) findViewById(R.id.linearlayout4);

        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        fragment1 = new FragmentTmap();
        fragment2 = new Fourth();
        fragmentKeywordSearch = new FragmentKeywordSearch();
        fragmentFriendList = new FragmentFriendList();

        tabMap = bar.newTab();
        tabMap.setIcon(R.drawable.earth);
        tabMap.setTabListener(this);

        tabFriend = bar.newTab();
        tabFriend.setIcon(R.drawable.friendship);
        tabFriend.setTabListener(this);

        bar.addTab(tabMap);
        bar.addTab(tabFriend);

        edit = new EditText(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        edit.setLayoutParams(new Toolbar.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        int color = Color.parseColor("#FAFAFA");
        edit.getBackground().clearColorFilter();
        edit.setBackgroundColor(color);

        sktUrlTest();
        initButton();
        //Toast.makeText(getApplicationContext(), "getCenterPoint 지금 위치는 "+latitude+" " +longtitude , Toast.LENGTH_LONG).show();
    }

    private void sktUrlTest() {
        Tdata.getInstance().init(getApplicationContext());
        String query = null;
        url = "https://apis.skplanetx.com/tmap/pois?version=1&searchKeyword=";
        try {
            query = URLEncoder.encode("카카오프렌즈", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ;
        url += query;
        Log.d("url", url);
    }

    private void initButton() {
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","click");
                menu2.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().add(R.id.container1, fragmentKeywordSearch).commit();

            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().add(R.id.container1,fragmentFriendList).commit();

            }
        });
    }




    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        int position = tab.getPosition();
        Fragment selected = null;
        if (position == 0) {
            selected = fragment1;
            menu2.setVisibility(View.INVISIBLE);

        } else if (position == 1) {

            menu2.setVisibility(View.VISIBLE);
            menu2.bringToFront();
           // FriendNameTextview.setVisibility(View.VISIBLE);
           // GetLocationButton.setVisibility(View.VISIBLE);
           // friendLocation = new FriendLocation(GetLocationButton, getApplicationContext());
            selected = fragment1;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container1, selected).commit();
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }



    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }



}
