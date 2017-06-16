package orgm.androidtown.app_location;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import orgm.androidtown.app_location.singleton.Tdata;

/**
 * Created by InKyung on 2017-06-01.
 */

public class FriendLocation {
    Context mContext;
    TMapPoint FriendPoint,MyLocation;
    Button FindFriend,Place;

    TMapData tmapdata= Tdata.getInstance().GetTmapdata();
    TMapView tmapview=Tdata.getInstance().GetTmapview();
    public  FriendLocation(Button Friend, Context context){
        FindFriend=Friend;
        mContext=context;

        //친구 고르는 기능
        findFriendLocation();
        GetFriendLocation();
    }

    public void findFriendLocation(){
        FindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFriendLocation();
                FindPath();
            }
        });

    }

    public void SetMylocation(TMapPoint Current){
        MyLocation=Current;
    }

    public void GetFriendLocation(){
        String Url="http://163.180.117.118:23023";
        String User="/"+"111";
        String Friend="/"+"123";
        String userUrl="/location";//location의 아이드 추가해야한다.

        StringRequest FriendLocationRequest=new StringRequest(Request.Method.GET, Url+userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for ( int i = 0 ; i< jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FriendPoint=new TMapPoint(jsonObject.getDouble("ResLat"),jsonObject.getDouble("ResLon"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
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
                return params;
            }
        } ;
        FriendLocationRequest.setShouldCache(false);
        Volley.newRequestQueue(mContext).add(FriendLocationRequest) ;
    }

    public void FindPath() {
        tmapdata.findPathData(FriendPoint, Tdata.getInstance().GetCurrentLocation(), new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(2);
                tmapview.addTMapPath(tMapPolyLine);
                tmapview.addTMapPolyLine("dd", tMapPolyLine);
            }
        });
    }
  //     tmapview.setOnTouchListener(new View.OnTouchListener() {
  //         @Override
  //         public boolean onTouch(View v, MotionEvent event) {
  //             Log.d("GETXy", event.getX() + " " + event.getY());
  //             TMapPoint clickMapPoint = tmapview.getTMapPointFromScreenPoint(event.getX(), event.getY());
  //             Log.d("ClickedMapPoint", clickMapPoint.getLongitude() + " " + clickMapPoint.getLatitude());

  //             if (FriendPoint == null) {
  //                 Log.d("error","FriendPoint null ");
  //                 FriendPoint=new TMapPoint(37.566474,126.985022);
  //             }
  //             tmapdata.findPathData(FriendPoint, clickMapPoint, new TMapData.FindPathDataListenerCallback() {
  //                 @Override
  //                 public void onFindPathData(TMapPolyLine tMapPolyLine) {
  //                     Log.d("tMapPolyLine", "tMapPolyLine");
  //                     tMapPolyLine.setLineColor(Color.BLUE);
  //                     tMapPolyLine.setLineWidth(2);
  //                     tmapview.addTMapPath(tMapPolyLine);
  //                     tmapview.addTMapPolyLine("dd", tMapPolyLine);
  //                 }
  //             });

  //             return false;
  //         }

  //     });


}
