package orgm.androidtown.app_location;

import android.content.Context;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class fragment_second_first extends Fragment {
    TMapData tmapdata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       tmapdata=new TMapData() ;
        TMapPoint startpt=new TMapPoint(100,200) ;
        TMapPoint endpt=new TMapPoint(30.12,27.65) ;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_second_first, container, false);
    }
}
