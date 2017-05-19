package orgm.androidtown.app_location;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
public class First extends Fragment implements TMapGpsManager.onLocationChangedCallback, TMapView.OnClickListenerCallback,View.OnClickListener{
    private boolean m_bTrackingMode=true ;
    private Context mcontext ;

    private TMapGpsManager tmapgps=null ;
    private TMapView tmapview ;
    private TMapData tmapdata=new TMapData() ;

    private int mMarkerID ;
    private ArrayList<TMapPoint> m_tmapPoint=new ArrayList<TMapPoint>() ;
    private ArrayList<String> mArrayMarkerID=new ArrayList<String>() ;
    private ArrayList<MapPoint> m_mapPoint=new ArrayList<MapPoint>() ;

    private String address ;
    private Double lat=null ;
    private Double lon=null ;


    public First() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity()!=null){
            mcontext=getActivity() ;
        }
        tmapview=new TMapView(mcontext)  ;
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");

        addPoint() ;
        showMarkerPoint() ;

        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps=new TMapGpsManager(mcontext) ;
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        tmapgps.OpenGps();

        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
               lat=tMapMarkerItem.latitude ;
                lon=tMapMarkerItem.longitude ;

                tmapdata.convertGpsToAddress(lat,lon,new TMapData.ConvertGPSToAddressListenerCallback(){
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        address=s ;
                    }
                });
                Toast.makeText(mcontext,"주소: "+address,Toast.LENGTH_SHORT).show();
            }
        });

        ViewGroup viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_first,container,false) ;
        viewGroup.addView(tmapview);



        final TMapMarkerItem Markeritem=new TMapMarkerItem() ;

        TMapPoint startpt=new TMapPoint(37.5248, 126.93);
        TMapPoint endpt=new TMapPoint(37.4601, 128.0428);

        Markeritem.setTMapPoint(startpt);
        Markeritem.setName("출발지");
        Markeritem.setVisible(TMapMarkerItem.VISIBLE);

        Bitmap bitmap= BitmapFactory.decodeResource(getContext().getResources(),R.drawable.buses) ;
        Markeritem.setIcon(bitmap);

        tmapdata.findPathData(startpt, endpt, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    Log.d("tMapPolyLine","tMapPolyLine");
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tmapview.addTMapPolyLine("TestID",tMapPolyLine);
                    tmapview.addMarkerItem("Testmarker",Markeritem);
                }
            });


        return viewGroup ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void addPoint() {//핀을 꼽을 포인트를 배열에 add
    m_mapPoint.add(new MapPoint("강남",37.510350,127.066847)) ;

    }

    public void showMarkerPoint(){
        for(int i=0 ; i<m_mapPoint.size() ; i++) {
            TMapPoint point=new TMapPoint(m_mapPoint.get(i).getLatitude(),m_mapPoint.get(i).getLongtitude()) ;
            TMapMarkerItem item1 =new TMapMarkerItem() ;
            Bitmap bitmap=null ;
            bitmap=BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.point) ;

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap=BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.point) ;
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i=BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.point) ;

            item1.setCalloutRightButtonImage(bitmap_i);

           String strID=String.format("pmarked%d",mMarkerID++ ) ;
            tmapview.addMarkerItem(strID,item1);
            mArrayMarkerID.add(strID);

        }

    }

    @Override
    public void onLocationChange(Location location) {
       if(m_bTrackingMode){
           tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
       }
    }

    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        Log.d("mainactivity","점찍은 위치의 위도와 경도"+tMapPoint.getLatitude()+" "+tMapPoint.getLongitude());
        return true;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
        return false;
    }
}
