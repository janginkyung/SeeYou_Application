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
import android.widget.EditText;

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

    EditText startpt, endpt ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startpt=(EditText)getView().findViewById(R.id.editText2) ;
        endpt=(EditText)getView().findViewById(R.id.editText1) ;
       tmapdata=new TMapData() ;
if(startpt==null)
        Log.d("fragment_second_first","null") ;
        Log.d("fragment_second_first","notnull") ;
        tmapdata.findAllPOI("SKT타워", 100, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                for(int i=0 ; i<arrayList.size() ; i++){
                    TMapPOIItem item=arrayList.get(i) ;
                    Log.d("findAllPOI","POI Name: " + item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                            "Point: " + item.getPOIPoint().toString());
                }
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_second_first, container, false);
    }
}
