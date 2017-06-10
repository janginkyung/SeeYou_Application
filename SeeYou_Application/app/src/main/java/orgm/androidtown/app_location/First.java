package orgm.androidtown.app_location;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class First extends Fragment implements TMapView.OnClickListenerCallback {
    private Context mcontext;
    private LocationManager locationmanger ;
    private TMapView tmapview;

    private Location CurrentLocation;
    private boolean   isGPSenbled = false;
    private boolean isNetworkEnabled = false;

    private boolean check = false;
    private double lat;
    private double lon;

    private GetTextButtonViewListner viewlistner;
    TextView FriendNameTextview;
    Button GetLocationButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            mcontext = getActivity();
        }

        tmapview = Tdata.getInstance().GetTmapview();
        locationmanger = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        FriendNameTextview = (TextView) viewGroup.findViewById(R.id.FriendNameTextview);
        GetLocationButton = (Button) viewGroup.findViewById(R.id.GetLocationButton);
        viewlistner.GetTextButtonView(GetLocationButton, FriendNameTextview);

        viewGroup.addView(tmapview);

        setLocationManger();

        Button button = (Button) viewGroup.findViewById(R.id.button7);
        button.bringToFront();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    tmapview.setCenterPoint(lon, lat);
                }
            }
        });

        ConnectServer();
        return viewGroup;
    }
    private String Url ;
    private LocationListener mLocationListner= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude(); //경도
            lat = location.getLatitude();   //위도
            Tdata.getInstance().SetCurrentLocation(new TMapPoint(lat,lon));
            check = true;
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
    };;

    public First() {
        // Required empty public constructor
    }


 //  public void addPoint() {//핀을 꼽을 포인트를 배열에 add
 //      m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
 //  }

 //  public void showMarkerPoint() {
 //      for (int i = 0; i < m_mapPoint.size(); i++) {
 //          TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongtitude());
 //          TMapMarkerItem item1 = new TMapMarkerItem();
 //          Bitmap bitmap = null;
 //          bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

 //          item1.setTMapPoint(point);
 //          item1.setName(m_mapPoint.get(i).getName());
 //          item1.setVisible(item1.VISIBLE);

 //          item1.setIcon(bitmap);

 //          bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);
 //          item1.setCalloutTitle(m_mapPoint.get(i).getName());
 //          item1.setCalloutSubTitle("서울");
 //          item1.setCanShowCallout(true);
 //          item1.setAutoCalloutVisible(true);

 //          Bitmap bitmap_i = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

 //          item1.setCalloutRightButtonImage(bitmap_i);

 //          String strID = String.format("pmarked%d", mMarkerID++);
 //          tmapview.addMarkerItem(strID, item1);
 //          mArrayMarkerID.add(strID);
 //      }

 //  }

    private void SetGpsNetwork(){
        if(!isNetworkEnabled){
            //show dialog to allow user to enable location settings
            AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
            dialog.setTitle("GPS&NETWORK 설정 ");
            dialog.setMessage("GPS나 NETWORK 활성화 시켜주세요. ");

            dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                }
            });

            dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //nothing to do
                }
            });
            dialog.show();
        }

    }


    private void setLocationManger() {
        isNetworkEnabled=locationmanger.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ;
        isGPSenbled=locationmanger.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
        SetGpsNetwork() ;
        if(isNetworkEnabled) {
            locationmanger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,mLocationListner);
            CurrentLocation = locationmanger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (isGPSenbled) {
            locationmanger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListner);
            if(CurrentLocation == null) {
                CurrentLocation = locationmanger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

        if (CurrentLocation != null) {
            check = true;
            lat = CurrentLocation.getLatitude();
            lon = CurrentLocation.getLongitude();
            Tdata.getInstance().SetCurrentLocation(new TMapPoint(lat,lon));
        }
    }

    private void ConnectServer(){
        Url="http://172.16.15.160:23023";
        String posturl="/user";
        StringRequest request=new StringRequest(Request.Method.POST, Url+posturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("connect","on response 호출됨"+response) ;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =new HashMap<>() ;
                params.put("UserId","100") ;
                params.put("UserName","ddd") ;

                return params;
            }
        } ;

        request.setShouldCache(false);
        Volley.newRequestQueue(mcontext).add(request) ;
    }

    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return true;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return false;
    }

    public interface GetTextButtonViewListner{
        void GetTextButtonView(Button b,TextView t);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GetTextButtonViewListner){
            viewlistner=(GetTextButtonViewListner)context;
        }else{
            throw  new RuntimeException(context.toString()+"must implement GetTextButtonViewListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewlistner=null;
    }
}

