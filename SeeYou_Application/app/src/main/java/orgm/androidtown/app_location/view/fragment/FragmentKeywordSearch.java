package orgm.androidtown.app_location.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.singleton.Tdata;

public class FragmentKeywordSearch extends Fragment {

    private Context mcontext;

    private EditText editText;
    private ImageButton imageButton;
    private TMapView currentTmapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            mcontext = getActivity();
        }

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_keyword_search, container, false);
        currentTmapView = Tdata.getInstance().GetTmapview();
        initView(viewGroup);

        return viewGroup;
    }

    private void initView(ViewGroup viewGroup) {
        editText = (EditText) viewGroup.findViewById(R.id.searchEditText);

        imageButton = (ImageButton) viewGroup.findViewById(R.id.searchKeywordImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString();
                TMapPoint keywordTmapPoint;
                // TODO: 2017. 6. 13. 경희대학교 국제캠퍼스 마커찍고
                Tdata.getInstance().GetTmapdata().findAllPOI(keyword, 100, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        TMapPOIItem item = arrayList.get(0);
                        TMapPoint keywordMapPoint = item.getPOIPoint();

                        TMapMarkerItem tItem = new TMapMarkerItem();
                        tItem.setTMapPoint(keywordMapPoint);
                        tItem.setName("내 위치");
                        tItem.setVisible(TMapMarkerItem.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.point);
                        tItem.setIcon(bitmap);
                        Tdata.getInstance().GetTmapview().addMarkerItem("mygps",tItem);
                        Tdata.getInstance().GetTmapview().setCenterPoint(keywordMapPoint.getLongitude(),keywordMapPoint.getLatitude());

                        findgPOI(keywordMapPoint);

                    }
                });
            }
        });
    }

    private void findgPOI(TMapPoint point){
        final TMapData tmapdata= Tdata.getInstance().GetTmapdata();
        tmapdata.findAroundNamePOI(point, "놀거리", 10, 10, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {

                for(int i=0 ; i<arrayList.size() ; i++){
                    TMapPOIItem item=arrayList.get(i) ;
                    Log.d("findAllPOI", "POI Name: " + item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                            "Point: " + item.getPOIPoint().toString());

                    TMapPoint marker=item.getPOIPoint();
                    addMarkerpoint(marker,i+"",0,item.getPOIName().toString());//0의 값은 놀거리와 식음료 구분

                }
            }
        });
        tmapdata.findAroundNamePOI(point, "식음료", 10, 10, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                for(int j=0 ; j<arrayList.size() ; j++){
                    TMapPOIItem item=arrayList.get(j) ;
                    Log.d("findAllPOI", "POI Name: " + item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                            "Point: " + item.getPOIPoint().toString());

                    TMapPoint marker=item.getPOIPoint();
                    addMarkerpoint(marker,(10+j)+"",1,item.getPOIName().toString());//1의 값은 놀거리와 식음료 구분
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

        currentTmapView.addMarkerItem(id,marker);
        Log.d("여기에",id);
    }




}
