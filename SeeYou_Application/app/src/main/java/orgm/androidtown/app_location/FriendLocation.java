package orgm.androidtown.app_location;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skp.Tmap.TMapPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by InKyung on 2017-06-01.
 */

public class FriendLocation {
    Context mContext;
    TMapPoint FriendPoint,MyLocation;
    Button FindFriend,Place;

    public  FriendLocation(Button Friend, Context context){
        //MyLocation=CurrentLocation;
        FindFriend=Friend;
        //Place=Hotplace;
        mContext=context;

//        FindFriend.setVisibility(View.INVISIBLE);
 //       Place.setVisibility(View.INVISIBLE);

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

    public void GetFriendLocation(){
        String Url="http://172.16.15.160:23023";
        String posturl=Url+"/user";
        posturl+="/111"+"/123"+"/location";//요청을 보내는 사람, 받는 사람 순서
        RequestQueue queue=Volley.newRequestQueue(mContext);

    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, posturl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("ㅎㅎㅎㅎ",response.toString());

                        Log.d("connect","JSON oject 실행");
     //                   String id = response.getString("ReqLat");
     //                   String recordDate = response.getString("ReqLon");
     //                   Log.d("connect",id+" "+recordDate);
                    // catch (JSONException e){
                     //   e.printStackTrace();
                     // }
                }
            }, new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("connect","json 에서 에러 발생");
        }
    });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest) ;

    }

    public void FindPath(){



    }
}
