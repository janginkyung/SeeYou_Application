package orgm.androidtown.app_location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {
    LocationManager location;
    Mylocationlistner listner;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));


        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listner = new Mylocationlistner();

        requestmylocation();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void requestmylocation() {
        int mintime = 10000;
        float mindistance = 0;

        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, mintime, mindistance, listner);
        location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mintime, mindistance, listner);
        Location lastlocation = location.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastlocation != null) {
            Double latitude = lastlocation.getLatitude();
            Double longtitude = lastlocation.getLongitude();
            Toast.makeText(getApplicationContext(),"가장 최근의  내 위치 정보: " + latitude + ", " + longtitude,Toast.LENGTH_LONG).show();

        }
    }



    class Mylocationlistner implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            Double latitude = location.getLatitude();
            Double longtitude = location.getLongitude();
            Toast.makeText(getApplicationContext(),"내 위치 정보: " + latitude + ", " + longtitude,Toast.LENGTH_LONG).show();

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
    }


}
