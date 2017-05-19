package orgm.androidtown.app_location;

/**
 * Created by InKyung on 2017-05-19.
 */

public class MapPoint {
    private String name ;
    private double latitude ;
    private double longtitude ;

    public MapPoint(){

    }

    public MapPoint( String name ,double latitude, double longtitude){
        this.name=name ;
        this.latitude=latitude ;
        this.longtitude=longtitude ;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
