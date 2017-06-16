package orgm.androidtown.app_location.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.model.User;
import orgm.androidtown.app_location.singleton.Tdata;
import orgm.androidtown.app_location.util.FindRoute;

import static orgm.androidtown.app_location.singleton.Tdata.tmapdata;
import static orgm.androidtown.app_location.singleton.Tdata.tmapview;


public class FragmentFriendList extends Fragment {

    ViewGroup rootView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String selectedFriendId = null;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return null;
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friend_list, container, false);
        if (getActivity() != null) {
            mContext = getActivity();
        }
        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        RecyclerView reviewRecyclerView = (RecyclerView)rootView.findViewById(R.id.userRecycleView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        reviewRecyclerView.setLayoutManager(mLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.scrollToPosition(0);

        final ArrayList<User> users = new ArrayList<>();
        // TODO: 2017. 6. 11. users 데이터 추가.
        final UserAdapter userAdapter = new UserAdapter(users);
        reviewRecyclerView.setAdapter(userAdapter);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // TODO: 2017. 6. 10.  서버로 데이터 요청 보내기

        String url = "http://163.180.117.118:23023/users";
        JsonArrayRequest userLoginRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               for(int i =0 ;i < response.length() ; i++) {
                   try {

                       JSONObject jsonObject = response.getJSONObject(i);
                       User newUser = new User();
                       newUser.setUserID(jsonObject.getString("userId"));
                       newUser.setUserName(jsonObject.getString("userName"));
                       newUser.setUserEmail(jsonObject.getString("userEmail"));
                       newUser.setUserPhotoURL(jsonObject.getString("userPhotoUrl"));
                       users.add(newUser);


                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
               userAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(userLoginRequest);




    }


    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        private ArrayList<User> users;

        public UserAdapter(ArrayList<User> users) {
            this.users = users;
        }

        @Override
        public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User userInfo = users.get(position);

            Picasso.with(getContext()).load(userInfo.getUserPhotoURL()).into(holder.userImage);
            holder.userTitle.setText(userInfo.getUserName());
            holder.userEmail.setText(userInfo.getUserEmail());

            final ViewHolder viewHolder = (ViewHolder)holder;

            viewHolder.userItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedFriendId = userInfo.getUserID();
                    String url;
                    url="http://163.180.117.118:23023/location/" + selectedFriendId;
                    Tdata.getInstance().GetTmapview().removeMarkerItem("mygps");

                    StringRequest request=new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for ( int i = 0 ; i< jsonArray.length() ; i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            // 친구 위치 띄우기
                                            Double lat = jsonObject.getDouble("latitude");
                                            Double lon = jsonObject.getDouble("lontitude");
                                            FindRoute findRoute = new FindRoute();
                                            findRoute.drawFriendAndMeRoute(lat,lon,mContext);
                                            Log.d("나의 위치는 ?", Tdata.getInstance().GetCurrentLocation().getLongitude() + " " + Tdata.getInstance().GetCurrentLocation().getLatitude());
                                            Log.d("너의 위치는 ? ", lon + " " + lat);
                                            tmapdata.findPathData(Tdata.getInstance().GetCurrentLocation(),new TMapPoint(lat,lon), new TMapData.FindPathDataListenerCallback() {
                                                @Override
                                                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                                    tMapPolyLine.setLineColor(Color.BLUE);
                                                    tMapPolyLine.setLineWidth(2);
                                                    tmapview.removeTMapPath();
                                                    tmapview.addTMapPath(tMapPolyLine);
                                                    tmapview.addTMapPolyLine("route",tMapPolyLine);
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
                            });

                    request.setShouldCache(false);
                    Volley.newRequestQueue(getContext()).add(request) ;
                    Toast.makeText(getContext(),userInfo.getUserName() + "가 선택되었습니다",Toast.LENGTH_SHORT).show();

                }
            });
        }



        @Override
        public int getItemCount() {
            return users.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ConstraintLayout userItem;
            public CircleImageView userImage;
            public TextView userTitle;
            public TextView userEmail;

            public ViewHolder(View view) {
                super(view);
                userItem = (ConstraintLayout)view.findViewById(R.id.userConstraintLayout);
                userImage = (CircleImageView)view.findViewById(R.id.userImageView);
                userTitle = (TextView)view.findViewById(R.id.userNameTextView);
                userEmail = (TextView)view.findViewById(R.id.userEmailTextView);
            }
        }



    }



}
