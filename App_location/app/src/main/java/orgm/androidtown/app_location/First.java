package orgm.androidtown.app_location;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;

import javax.xml.parsers.ParserConfigurationException;


/**
 * A simple {@link Fragment} subclass.
 */
public class First extends Fragment implements TMapView.OnClickListenerCallback {
    private Context mcontext;

    private LocationManager locationmanger;
    private TMapView tmapview;
    private TMapData tmapdata = new TMapData();

    private int mMarkerID;
    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    private String address;
    private Location CurrentLocation;
    private TMapPoint CurrentPoint;
    private boolean isGPSenbled = false, isNetworkEnabled = false;

    private boolean check = false;
    private double lat;
    private double lon;

    private LocationListener mLocationListner;

    public First() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            mcontext = getActivity();
        }

        initMapview();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);
        viewGroup.addView(tmapview);

        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("ㅗㅗㅗㅗㅗㅗㅗ", "onLocationChanged, location:" + location);
                lon = location.getLongitude(); //경도
                lat = location.getLatitude();   //위도
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
        };

        setLocationManger();

        Button button = (Button) viewGroup.findViewById(R.id.button7);
        button.bringToFront();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    tmapview.setCenterPoint(lon, lat);
                    CurrentPoint = new TMapPoint(lat, lon);
                }
            }
        });

        //   tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
        //       @Override
        //       public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        //           lat=tMapMarkerItem.latitude ;
        //           lon=tMapMarkerItem.longitude ;
//
        //           tmapdata.convertGpsToAddress(lat,lon,new TMapData.ConvertGPSToAddressListenerCallback(){
        //               @Override
        //               public void onConvertToGPSToAddress(String s) {
        //                   address=s ;
        //               }
        //           });
        //      //    Toast.makeText(mcontext,"주소: "+address,Toast.LENGTH_SHORT).show();
        //       }
        //   });

        // final TMapMarkerItem Markeritem=new TMapMarkerItem() ;
//
        // TMapPoint startpt=new TMapPoint(37.5248, 126.93);
        // TMapPoint endpt=new TMapPoint(37.4601, 128.0428);
//
        // Markeritem.setTMapPoint(startpt);
        // Markeritem.setName("출발지");
        // Markeritem.setVisible(TMapMarkerItem.VISIBLE);
//
        // Bitmap bitmap= BitmapFactory.decodeResource(getContext().getResources(),R.drawable.buses) ;
        // Markeritem.setIcon(bitmap);
//
        // tmapdata.findPathData(startpt, endpt, new TMapData.FindPathDataListenerCallback() {
        //         @Override
        //         public void onFindPathData(TMapPolyLine tMapPolyLine) {
        //             Log.d("tMapPolyLine","tMapPolyLine");
        //             tMapPolyLine.setLineColor(Color.BLUE);
        //             tMapPolyLine.setLineWidth(2);
        //             tmapview.addTMapPolyLine("TestID",tMapPolyLine);
        //             tmapview.addMarkerItem("Testmarker",Markeritem);
        //         }
        //     });

        return viewGroup;
    }

    public void addPoint() {//핀을 꼽을 포인트를 배열에 add
        m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
    }

    public void showMarkerPoint() {
        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongtitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarked%d", mMarkerID++);
            tmapview.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }

    }

    private void initMapview() {
        tmapview = new TMapView(mcontext);
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");

        locationmanger = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);

        addPoint();
        showMarkerPoint();

        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);

        tmapview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("GETXy", event.getX() + " " + event.getY());
                TMapPoint clickMapPoint = tmapview.getTMapPointFromScreenPoint(event.getX(), event.getY());
                Log.d("ClickedMapPoint", clickMapPoint.getLongitude() + " " + clickMapPoint.getLatitude());

                if (CurrentLocation != null) {
                    tmapdata.findPathData(CurrentPoint, clickMapPoint, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            Log.d("tMapPolyLine", "tMapPolyLine");
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tmapview.addTMapPath(tMapPolyLine);
                            tmapview.addTMapPolyLine("dd", tMapPolyLine);
                        }
                    });
                }
                return false;
            }

        });
    }

    private void setLocationManger() {
        isGPSenbled = locationmanger.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationmanger.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isNetworkEnabled){
            //show dialog to allow user to enable location settings
            AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
            dialog.setTitle("네트워크로 위치 받아오기");
            dialog.setMessage("dd");

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
        }
    }


    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
        Log.d("mainactivity", "점찍은 위치의 위도와 경도" + tMapPoint.getLatitude() + " " + tMapPoint.getLongitude());
        return true;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
        Log.d("mainactivity", "점찍은 위치의 위도와 경도" + tMapPoint.getLatitude() + " " + tMapPoint.getLongitude());
        return false;
    }



}

