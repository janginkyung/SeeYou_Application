package orgm.androidtown.app_location;

import android.content.Context;
import android.util.Log;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

/**
 * Created by InKyung on 2017-06-07.
 */
public class Tdata {
    public static TMapPoint CurrentLocation;
    public static TMapView tmapview;
    public static TMapData tmapdata;
    private static Tdata ourInstance;
    public static int location_id;
    public static int user_id;

    public static Tdata getInstance() {
        if(ourInstance==null)
            ourInstance=new Tdata();
        return ourInstance;
    }

    private Tdata() {
    }

    public TMapView GetTmapview(){
        return tmapview;
    }

    public void init(Context context){
        SetTmapview(context);
        SetTmapdata();
    }
    private void SetTmapview(Context context){
        tmapview = new TMapView(context);
        tmapview.setSKPMapApiKey("7b20d64c-023f-3225-a224-d888b951f720");
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSightVisible(true);
    }

    public TMapData GetTmapdata(){
        return tmapdata;
    }

    private void SetTmapdata(){
        tmapdata=new TMapData();
    }

    public void SetCurrentLocation(TMapPoint current){
        CurrentLocation=current;
    }
    public TMapPoint GetCurrentLocation(){
        return CurrentLocation;
    }
    public void SetLocation_id(int integer){
        location_id=integer;
    }
public int GetLocation_id(){
    return location_id;
}
    public void SetUser_id(int integer){
        user_id=integer;
    }
    public int GetUser_id(){
        return user_id;
    }

}
