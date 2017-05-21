package orgm.androidtown.app_location;

import android.content.Context;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class fragment_second_first extends Fragment {
    private TMapData tmapdata;
    private Context mcontext;

    private String start, end;

    private EditText startpt, endpt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_fragment_second_first, container, false);
        if (getActivity() != null) {
            mcontext = getActivity();
        }
        startpt = (EditText) viewGroup.findViewById(R.id.editText2);
        startpt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        break;
                    default:
                        start = startpt.getText().toString();
                        return false;
                }
                return true;
            }
        });
        endpt = (EditText) viewGroup.findViewById(R.id.editText1);
        endpt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        break;
                    default:
                        end = endpt.getText().toString();
                        tmapdata.findAllPOI(end, 100, new TMapData.FindAllPOIListenerCallback() {
                            @Override
                            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                                for (int i = 0; i < arrayList.size(); i++) {
                                    TMapPOIItem item = arrayList.get(i);
                                    Log.d("findAllPOI", "POI Name: " + item.getPOIName().toString() + ", " +
                                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                            "Point: " + item.getPOIPoint().toString());
                                }
                            }
                        });
                        tmapdata.findAroundNamePOI(new TMapPoint(37.386800, 127.122955), "놀거리", 1, 100, new TMapData.FindAroundNamePOIListenerCallback() {
                            @Override
                            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                               for(int i=0 ; i<arrayList.size() ; i++){
                                   TMapPOIItem item=arrayList.get(i) ;
                                   Log.d("findAllPOI", "놀거리 Name: " + item.getPOIName().toString() + ", " +
                                           "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                           "Point: " + item.getPOIPoint().toString());
                               }
                            }
                        });
                        return false;
                }
                return true;
            }
        });
        tmapdata = new TMapData();

        return viewGroup;
    }


}
