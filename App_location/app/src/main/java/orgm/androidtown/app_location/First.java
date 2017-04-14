package orgm.androidtown.app_location;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapView;


/**
 * A simple {@link Fragment} subclass.
 */
public class First extends Fragment {

    TMapView tmapview ;
    TMapGpsManager gps ;
    Context mcontext ;
    public First() {
        mcontext=getActivity() ;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tmapview=new TMapView(mcontext)  ;
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);
        tmapview.setTrackingMode(true);
        ViewGroup viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_first,container,false) ;
        viewGroup.addView(tmapview);
        // Inflate the layout for this fragment
        return viewGroup ;
    }

}
