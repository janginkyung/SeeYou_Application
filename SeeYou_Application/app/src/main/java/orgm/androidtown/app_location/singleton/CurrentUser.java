package orgm.androidtown.app_location.singleton;
/**
 * Created by InKyung on 2017-06-12.
 */


public class CurrentUser {
    private static final CurrentUser ourInstance = new CurrentUser();

    public static CurrentUser getInstance() {
        return ourInstance;
    }


    private String userID;
    private String userName;
    private String userEmail;
    private String userPhotoURL;

    private CurrentUser() {

    }

    public java.lang.String getUserID() {
        return userID;
    }

    public void setUserID(java.lang.String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    public java.lang.String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(java.lang.String userEmail) {
        this.userEmail = userEmail;
    }

    public java.lang.String getUserPhotoURL() {
        return userPhotoURL;
    }

    public void setUserPhotoURL(java.lang.String userPhotoURL) {
        this.userPhotoURL = userPhotoURL;
    }


}
