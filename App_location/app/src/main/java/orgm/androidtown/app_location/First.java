package orgm.androidtown.app_location;


import android.content.Context;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class First extends Fragment implements TMapGpsManager.onLocationChangedCallback, TMapView.OnClickListenerCallback{
    TMapView tmapview ;
    TMapGpsManager gps ;
    Context mcontext ;

    LinearLayout tabmenu ;


    public First() {

        // Required empty public constructor
    }
    public First Changefragment(int i) {
        Log.d("mainactivity" ,"tabmenu is  null") ;
if(tabmenu!=null) {
    Log.d("mainactivity" ,"tabmenu is not null") ;

    if (i == 0) {
        tabmenu.setVisibility(View.INVISIBLE);
    } else {
        tabmenu.setVisibility(View.VISIBLE);
        Log.d("mainactivity" ,"tabmenu is visible") ;

    }
    // Required empty public constructor
}
        return this ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity()!=null){
            mcontext=getActivity() ;
        }
        tmapview=new TMapView(mcontext)  ;
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);
        boolean click=tmapview.setClick() ;
        ViewGroup viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_first,container,false) ;
        viewGroup.addView(tmapview);
        tabmenu=(LinearLayout)viewGroup.findViewById(R.id.linearlayout) ;
        // Inflate the layout for this fragment
        return viewGroup ;
    }

    @Override
    public void onLocationChange(Location location) {
        Log.d("mainactivity","onLocationChange 문 실행") ;
        TMapPoint tpoint=gps.getLocation() ;
        double latitude= tpoint.getLatitude() ;
        double longtitude=tpoint.getLongitude() ;
        tmapview.setCenterPoint(longtitude, latitude);
        Toast.makeText(mcontext, "getLocationPoint 지금 위치는 "+latitude+" " +longtitude , Toast.LENGTH_LONG).show();
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
