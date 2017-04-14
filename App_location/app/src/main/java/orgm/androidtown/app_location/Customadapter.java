package orgm.androidtown.app_location;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by InKyung on 2017-04-14.
 */

public class Customadapter extends PagerAdapter {
    LayoutInflater inflater ;
    public Customadapter(LayoutInflater inflater) {
        this.inflater=inflater ;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View view=null ;
       switch (position)
       {
           case 0:
               view=inflater.inflate(R.layout.fragment_first,null) ;
               break ;
           case 1:
               view=inflater.inflate(R.layout.fragment_second,null) ;
               break ;
           case 2:
               view=inflater.inflate(R.layout.fragment_third,null) ;
               break;
           case 3:
               view=inflater.inflate(R.layout.fragment_fourth,null) ;
               break ;
       }

        if(view!=null){
            container.addView(view);
        }
        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object ;
    }
}
