package orgm.androidtown.app_location;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
public class MainActivity extends ActionBarActivity implements TMapGpsManager.onLocationChangedCallback, android.support.v7.app.ActionBar.TabListener {
    TMapView tmapview ;
    TMapGpsManager gps ;
    android.support.v7.app.ActionBar bar;
    Customadapter adapter ;
    String url ;
    EditText edit ;
    static ViewPager viewPager ;
    private android.support.v7.app.ActionBar.Tab tabMap;
    private android.support.v7.app.ActionBar.Tab tabfindroute;
    private android.support.v7.app.ActionBar.Tab tabFriend;
    private android.support.v7.app.ActionBar.Tab tabBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bar=getSupportActionBar() ;
        setContentView(R.layout.activity_main) ;

        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
        tabMap = bar.newTab();
        tabFriend = bar.newTab();
        tabBus = bar.newTab();
        tabfindroute=bar.newTab() ;

        adapter=new Customadapter(getLayoutInflater()) ;
        edit =new EditText(this) ;
        viewPager=(ViewPager)findViewById(R.id.viewpager) ;
        viewPager.setAdapter(adapter);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_main) ;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabMap.setIcon(R.drawable.placeholder);
        tabMap.setTabListener( this) ;
        tabfindroute.setText("길") ;
        tabfindroute.setTabListener(this) ;
        tabFriend.setText("가수별");
        tabFriend.setTabListener((android.support.v7.app.ActionBar.TabListener) this) ;
        tabBus.setIcon(R.drawable.buses) ;
        tabBus.setTabListener((android.support.v7.app.ActionBar.TabListener) this) ;

        bar.addTab(tabMap);
        bar.addTab(tabfindroute) ;
        bar.addTab(tabFriend);
        bar.addTab(tabBus);

//        tmapview=new TMapView(this)  ;
//        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
//        tmapview.setZoomLevel(15);
//        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
//        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
//        tmapview.setSightVisible(true);
//        tmapview.setTrackingMode(true);
//        relativeLayout.addView(tmapview);

        edit.setLayoutParams(new Toolbar.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       int color= Color.parseColor("#FAFAFA") ;
        edit.getBackground().clearColorFilter();
        edit.setBackgroundColor(color);
       // relativeLayout.addView(edit);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        String query=null ;
        url="https://apis.skplanetx.com/tmap/pois?version=1&searchKeyword=";
        try {
            query = URLEncoder.encode("카카오프렌즈", "UTF-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } ;
        url += query;
        Log.d("url", url);

            //Toast.makeText(getApplicationContext(), "getCenterPoint 지금 위치는 "+latitude+" " +longtitude , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChange(Location location) {
        TMapPoint tpoint=gps.getLocation() ;
        double latitude= tpoint.getLatitude() ;
        double longtitude=tpoint.getLongitude() ;
        tmapview.setCenterPoint(longtitude, latitude);
        Toast.makeText(getApplicationContext(), "getLocationPoint 지금 위치는 "+latitude+" " +longtitude , Toast.LENGTH_LONG).show();
    }


    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

}
