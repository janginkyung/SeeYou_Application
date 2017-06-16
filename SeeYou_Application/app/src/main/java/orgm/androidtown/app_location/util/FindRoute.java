package orgm.androidtown.app_location.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.singleton.Tdata;

/**
 * Created by InKyung on 2017-06-13.
 */

public class FindRoute {

    private TMapView tMapView;
    private TMapData tMapData;

    public FindRoute() {
        tMapView = Tdata.getInstance().GetTmapview();
        tMapData = Tdata.getInstance().GetTmapdata();
    }


    public void drawFriendAndMeRoute(Double lat, Double lon, Context context) {
        TMapMarkerItem tFriendItem = new TMapMarkerItem();
        tFriendItem.setTMapPoint(new TMapPoint(lat,lon));
        tFriendItem.setName("친구 위치");
        tFriendItem.setVisible(TMapMarkerItem.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_friend);
        tFriendItem.setIcon(bitmap);
        tMapView.addMarkerItem("FriendGps",tFriendItem);

        // 내 위치 띄우기.
        TMapMarkerItem myItem = new TMapMarkerItem();
        myItem.setTMapPoint(Tdata.getInstance().GetCurrentLocation());
        myItem.setName("내 위치");
        myItem.setVisible(TMapMarkerItem.VISIBLE);
        myItem.setIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_myloc));
        tMapView.addMarkerItem("MyGps",myItem);


    }
}
