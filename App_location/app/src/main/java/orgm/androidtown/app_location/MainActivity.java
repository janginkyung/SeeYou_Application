package orgm.androidtown.app_location;
//implements  android.support.v7.app.ActionBar.TabListener
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
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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
public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {
    First fragment1,fragment2,fragment3 ;
    Fourth fragment4 ;

    LinearLayout tablayout ;

    android.support.v7.app.ActionBar bar;
    String url ;
    EditText edit ;

    private android.support.v7.app.ActionBar.Tab tabMap;
    private android.support.v7.app.ActionBar.Tab tabfindroute;
    private android.support.v7.app.ActionBar.Tab tabFriend;
    private android.support.v7.app.ActionBar.Tab tabBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tablayout=(LinearLayout)findViewById(R.id.linearlayout) ;
        bar=getSupportActionBar() ;
        setContentView(R.layout.activity_main) ;

        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        fragment1=new First() ;
        fragment2=fragment1;
        fragment3=fragment1;
        fragment4=new Fourth() ;

        tabMap = bar.newTab();
        tabFriend = bar.newTab();
        tabBus = bar.newTab();
        tabfindroute=bar.newTab() ;
        tabMap.setIcon(R.drawable.earth);
        tabMap.setTabListener(this) ;
        tabfindroute.setIcon(R.drawable.placeholder) ;
        tabfindroute.setTabListener(this) ;
        tabFriend.setIcon(R.drawable.friendship);
        tabFriend.setTabListener(this) ;
        tabBus.setIcon(R.drawable.buses) ;
        tabBus.setTabListener(this) ;
        bar.addTab(tabMap);
        bar.addTab(tabfindroute) ;
        bar.addTab(tabFriend);
        bar.addTab(tabBus);

        edit =new EditText(this) ;
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_main) ;
        edit.setLayoutParams(new Toolbar.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       int color= Color.parseColor("#FAFAFA") ;
        edit.getBackground().clearColorFilter();
        edit.setBackgroundColor(color);
       // relativeLayout.addView(edit);

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
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        int position=tab.getPosition() ;
        Fragment selected=null ;
        if(position==0){
            selected=fragment1 ;
        }else if(position==1) {selected=fragment1;
}
        else if(position==2)selected=fragment3 ;
        else selected=fragment4 ;

        getSupportFragmentManager().beginTransaction().replace(R.id.container1,selected).commit() ;
}

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
