package orgm.androidtown.app_location;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import org.xml.sax.SAXException;
import android.widget.TabHost ;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends TabActivity implements TMapGpsManager.onLocationChangedCallback {
    TMapView tmapview ;
    TMapGpsManager gps ;
    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      // window.setStatusBarColor(Integer.parseInt("#E86262"));

        tmapview=new TMapView(this) ;
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);
        tmapview.setTrackingMode(true);
        relativeLayout.addView(tmapview);

        Resources res = getResources();
        TabHost tabHost = getTabHost(); //탭을 붙이기위한 탭호스객체선언
        TabHost.TabSpec spec; //탭호스트에 붙일 각각의 탭스펙을 선언 ; 각 탭의 메뉴와 컨텐츠를 위한 객체
        Intent intent; //각탭에서 사용할 인텐트 선언
//// 탭엑티비티 무조건 0번째 탭이 선택되어지는 버그를 회피하기 위한 코드
//        intent = new Intent(this, BlankActivity.class);
//        spec = tabHost.newTabSpec("").setIndicator("")
//                .setContent(intent);
//        tabHost.addTab(spec);
//        tabHost.getTabWidget()
//                .getChildTabViewAt(0).setVisibility(View.GONE);

        //인텐트 생성
        intent = new Intent().setClass(this, FirstTab.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("textList").setIndicator("",res.getDrawable(R.drawable.placeholder)).setContent(intent);
        spec.setContent(R.id.tab1) ;
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, SecondTab.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("result").setIndicator("친구").setContent(intent);
        spec.setContent(R.id.tab2) ;
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, ThirdTab.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("help").setIndicator("",res.getDrawable(R.drawable.bus)).setContent(intent);
        spec.setContent(R.id.tab3) ;
        tabHost.addTab(spec);

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


}
