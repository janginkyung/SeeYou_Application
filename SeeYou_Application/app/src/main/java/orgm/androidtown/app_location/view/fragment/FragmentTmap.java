package orgm.androidtown.app_location.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.singleton.CurrentUser;
import orgm.androidtown.app_location.singleton.Tdata;
import orgm.androidtown.app_location.util.FindRoute;

import static orgm.androidtown.app_location.view.fragment.FragmentFriendList.selectedFriendId;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTmap extends Fragment implements TMapView.OnClickListenerCallback {
    private Context mcontext;
    private LocationManager locationmanger ;

    private Location CurrentLocation;
    private boolean   isGPSenbled = false;
    private boolean isNetworkEnabled = false;

    private boolean check = false;
    private static final int MODE_SEARCH_FOOD = 1;
    private static final int MODE_ROUTE = 2;
    private int mode = 1;
    private double lat;
    private double lon;

    TextView friendNameTextview;
    Button getLocationButton;

    private TMapView tMapView;
    private TMapData tmapdata;
    private FindRoute findRoute;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() != null) {
            mcontext = getActivity();
        }

        tMapView = Tdata.getInstance().GetTmapview();
        tmapdata = Tdata.getInstance().GetTmapdata();
        findRoute = new FindRoute();

        locationmanger = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        friendNameTextview = (TextView) viewGroup.findViewById(R.id.FriendNameTextview);
        getLocationButton = (Button) viewGroup.findViewById(R.id.GetLocationButton);
        //viewlistner.GetTextButtonView(GetLocationButton, FriendNameTextview);


        viewGroup.addView(Tdata.getInstance().GetTmapview());
        setLocationManger();

        Button button = (Button) viewGroup.findViewById(R.id.button7);
        button.bringToFront();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    Tdata.getInstance().GetTmapview().setCenterPoint(lon, lat);
                }
            }
        });

        pointPlace();
        final ImageButton changeModeButton = (ImageButton) viewGroup.findViewById(R.id.modeSearchFunButton);
        changeModeButton.bringToFront();
        changeModeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mode == MODE_SEARCH_FOOD) {
                    mode = MODE_ROUTE;
                    changeModeButton.setImageResource(R.drawable.icon_route);
                    Tdata.getInstance().GetTmapview().removeAllMarkerItem();
                    routingPlace();

                }
                else if(mode == MODE_ROUTE){
                    mode = MODE_SEARCH_FOOD;
                    changeModeButton.setImageResource(R.drawable.icon_food);
                    Tdata.getInstance().GetTmapview().removeAllMarkerItem();
                    pointPlace();
                }
            }
        });

        ConnectServer();
        return viewGroup;
    }
    private String Url ;
    private LocationListener mLocationListner= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude(); //경도
            lat = location.getLatitude();   //위도
            Tdata.getInstance().SetCurrentLocation(new TMapPoint(lat,lon));
            updateLocation(new TMapPoint(lat,lon),Request.Method.PUT);

            Log.d("동작","onLocationChanged");
            check = true;

            // TODO: 2017. 6. 13. 변수 추가 되어있을때 친구 위치 받아오기!
            // 변수 하나 추가...!

            if(selectedFriendId != null) {
                String url;
                url="http://163.180.117.118:23023/location/" + selectedFriendId;

                StringRequest request=new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for ( int i = 0 ; i< jsonArray.length() ; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Double lat = jsonObject.getDouble("latitude");
                                        Double lon = jsonObject.getDouble("lontitude");
                                        // 친구 위치 띄우기
                                        findRoute.drawFriendAndMeRoute(lat,lon,mcontext);
                                        Log.d("나의 위치는2 ?", Tdata.getInstance().GetCurrentLocation().getLongitude() + " " + Tdata.getInstance().GetCurrentLocation().getLatitude());
                                        Log.d("너의 위치는2 ? ", lon + " " + lat);
                                        // 경로 안내 띄우기.
                                        tmapdata.findPathData(Tdata.getInstance().GetCurrentLocation(),new TMapPoint(lat,lon), new TMapData.FindPathDataListenerCallback() {
                                            @Override
                                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                                tMapPolyLine.setLineColor(Color.BLUE);
                                                tMapPolyLine.setLineWidth(2);
                                                tMapView.removeTMapPath();
                                                tMapView.addTMapPath(tMapPolyLine);
                                                tMapView.addTMapPolyLine("route",tMapPolyLine);

                                                Log.d("위치데이터", "받아옴");
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }) ;

                request.setShouldCache(false);
                Volley.newRequestQueue(getContext()).add(request) ;
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };;

    public FragmentTmap() {
        // Required empty public constructor
    }


 //  public void addPoint() {//핀을 꼽을 포인트를 배열에 add
 //      m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
 //  }

 //  public void showMarkerPoint() {
 //      for (int i = 0; i < m_mapPoint.size(); i++) {
 //          TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongtitude());
 //          TMapMarkerItem item1 = new TMapMarkerItem();
 //          Bitmap bitmap = null;
 //          bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

 //          item1.setTMapPoint(point);
 //          item1.setName(m_mapPoint.get(i).getName());
 //          item1.setVisible(item1.VISIBLE);

 //          item1.setIcon(bitmap);

 //          bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);
 //          item1.setCalloutTitle(m_mapPoint.get(i).getName());
 //          item1.setCalloutSubTitle("서울");
 //          item1.setCanShowCallout(true);
 //          item1.setAutoCalloutVisible(true);

 //          Bitmap bitmap_i = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.point);

 //          item1.setCalloutRightButtonImage(bitmap_i);

 //          String strID = String.format("pmarked%d", mMarkerID++);
 //          tmapview.addMarkerItem(strID, item1);
 //          mArrayMarkerID.add(strID);
 //      }

 //  }

    private void SetGpsNetwork(){
        if(!isNetworkEnabled){
            //show dialog to allow user to enable location settings
            AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
            dialog.setTitle("GPS&NETWORK 설정 ");
            dialog.setMessage("GPS나 NETWORK 활성화 시켜주세요. ");

            dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                }
            });

            dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //nothing to do
                }
            });
            dialog.show();
        }

    }


    private void setLocationManger() {
        isNetworkEnabled=locationmanger.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ;
        isGPSenbled=locationmanger.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
        SetGpsNetwork() ;
        if(isNetworkEnabled) {
            locationmanger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,mLocationListner);
            CurrentLocation = locationmanger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (isGPSenbled) {
            locationmanger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListner);
            if(CurrentLocation == null) {
                CurrentLocation = locationmanger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

        if (CurrentLocation != null) {
            check = true;
            lat = CurrentLocation.getLatitude();
            lon = CurrentLocation.getLongitude();

            TMapMarkerItem tItem = new TMapMarkerItem();
            tItem.setTMapPoint(new TMapPoint(lat,lon));
            tItem.setName("내 위치");
            tItem.setVisible(TMapMarkerItem.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.point);
            tItem.setIcon(bitmap);
            Tdata.getInstance().GetTmapview().addMarkerItem("mygps",tItem);
            Tdata.getInstance().GetTmapview().setCenterPoint(lon,lat);
            Tdata.getInstance().SetCurrentLocation(new TMapPoint(lat,lon));
            updateLocation(new TMapPoint(lat,lon),Request.Method.POST);

        }
    }

    private void ConnectServer(){
        Url="http://163.180.117.118:23023";
        String posturl="/user";
        StringRequest request=new StringRequest(Request.Method.POST, Url+posturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("connect","on response 호출됨"+response) ;
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
                params.put("UserId","100") ;
                params.put("UserName","ddd") ;

                return params;
            }
        } ;

        request.setShouldCache(false);
        Volley.newRequestQueue(mcontext).add(request) ;
    }

    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return true;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return false;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   viewlistner=null;
    }


    public void updateLocation(final TMapPoint current,int method){//locationchanged 부분과 처음 위치를 받아오는 setLocationManger에 들어가야 한다.
        Log.d("동작","updateLocation");
        Url="http://163.180.117.118:23023/";
        String update="location/user";
        StringRequest request=new StringRequest(method, Url+update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("connect","updateLocation 실행되었습니다.") ;
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
                params.put("userId", CurrentUser.getInstance().getUserID()) ;
                params.put("latitude",Double.toString(current.getLatitude())) ;
                params.put("lontitude",Double.toString(current.getLongitude())) ;
                return params;
            }
        } ;

        request.setShouldCache(false);
        Volley.newRequestQueue(mcontext).add(request) ;
    }


    public void pointPlace(){//누른 지점과 관련된 주변의 놀거리 식음료를 찾아주는 함수
        tMapView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final TMapPoint clickMapPoint ;
                clickMapPoint = Tdata.getInstance().GetTmapview().getTMapPointFromScreenPoint(event.getX(), event.getY());
                Log.d("ClickedMapPoint", clickMapPoint.getLongitude() + " " + clickMapPoint.getLatitude());
                findgPOI(clickMapPoint);// 그 지점의 주변 poi찾아준다.
                return false;
            }
        });
    }

    private void routingPlace() {
        tMapView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final TMapPoint clickMapPoint;
                clickMapPoint =  Tdata.getInstance().GetTmapview().getTMapPointFromScreenPoint(event.getX(), event.getY());
                Log.d("ClickedMapPoint", clickMapPoint.getLongitude() + " " + clickMapPoint.getLatitude());

                tmapdata.findPathData(Tdata.getInstance().GetCurrentLocation(),new TMapPoint(clickMapPoint.getLatitude(), clickMapPoint.getLongitude()), new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                        tMapPolyLine.setLineColor(Color.BLUE);
                        tMapPolyLine.setLineWidth(2);
                        tMapView.removeTMapPath();
                        tMapView.addTMapPath(tMapPolyLine);
                        tMapView.addTMapPolyLine("route",tMapPolyLine);

                        Log.d("위치데이터", "받아옴");
                    }
                });

               // findgPOI(clickMapPoint);// 그 지점의 주변 poi찾아준다.
                return false;
            }
        });
    }

    private void findgPOI(TMapPoint point){

        tmapdata.findAroundNamePOI(point, "놀거리", 2, 10, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                for(int i=0 ; i<arrayList.size() ; i++){
                    TMapPOIItem item=arrayList.get(i) ;
                    addMarkerpoint(item.getPOIPoint(),i+"",0,item.getPOIName());//0의 값은 놀거리와 식음료 구분

                }
            }
        });
        tmapdata.findAroundNamePOI(point, "식음료", 2, 10, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                for(int i=0 ; i<arrayList.size() ; i++){
                    TMapPOIItem item=arrayList.get(i) ;
                    addMarkerpoint(item.getPOIPoint(),i+"",1,item.getPOIName());//1의 값은 놀거리와 식음료 구분
                    //tmapview.removeAllTMapPOIItem();
                }
            }
        });
    }


    public void addMarkerpoint(TMapPoint point,String id,int place_id,String place_name){
        TMapMarkerItem marker=new TMapMarkerItem();
        marker.setTMapPoint(point);
        marker.setVisible(TMapMarkerItem.VISIBLE);

        marker.setCanShowCallout(true);
        marker.setCalloutTitle(place_name);

        Bitmap bitmap;
        if(place_id==0)//놀거리를 표현
            bitmap= BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.icon_fun);
        else//식음료를 표현
            bitmap= BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.icon_fun);

        marker.setIcon(bitmap);
        tMapView.addMarkerItem(id,marker);
        tMapView.setOnTouchListener(null);
    }


}

