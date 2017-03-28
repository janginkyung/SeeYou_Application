package orgm.androidtown.app_location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {
    LocationManager location;
    TMapView tmapview ;

    LocationListener locationlistner ;
    TMapGpsManager tmaps ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout=new RelativeLayout(this) ;
        tmapview=new TMapView(this) ;
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tmaps=new TMapGpsManager(MainActivity.this) ;

        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);
        tmapview.setTrackingMode(true);
        relativeLayout.addView(tmapview);

        TMapMarkerItem tourmarker=new TMapMarkerItem() ;
        TMapPoint tpoint=new TMapPoint(127.0835028,37.239326) ;
        tourmarker.setTMapPoint(tpoint);
        tourmarker.setVisible(TMapMarkerItem.VISIBLE);
        tmapview.setCenterPoint(127.0835028,37.239326,false);
        tmapview.addMarkerItem("hi",tourmarker);

        locationlistner= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double latitude = location.getLatitude();
                Double longtitude = location.getLongitude();
                Log.d("Mainactivity","showcurrent 내위치:"+latitude+" "+longtitude) ;
                tmapview.setLocationPoint(longtitude, latitude);
                showcurrentmap(latitude, longtitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        setContentView(relativeLayout);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void showcurrentmap(Double latitude,Double longtitude){
        Toast.makeText(getApplicationContext(),"showcurrentmap 실행 ",Toast.LENGTH_LONG).show();

        Log.d("Mainactivity","showcurrent 내위치:"+latitude+" "+longtitude) ;
        TMapMarkerItem tourmarker=new TMapMarkerItem() ;
        TMapPoint tpoint=new TMapPoint(longtitude,latitude) ;
        tourmarker.setTMapPoint(tpoint);
        tourmarker.setVisible(TMapMarkerItem.VISIBLE);
        tmapview.setCenterPoint(longtitude,latitude,false);
        tmapview.addMarkerItem("hi",tourmarker);
     }

}
