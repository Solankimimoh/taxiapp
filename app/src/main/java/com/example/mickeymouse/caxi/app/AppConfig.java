package com.example.mickeymouse.caxi.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppConfig {


    public static String BASE_IP_ADDRESS = "http://dc4itsolutions.in/grocery/user/js/caxi/";

    // Server user login url
    public static String URL_LOGIN = BASE_IP_ADDRESS + "api/login.php";

    // Server user register url
    public static String URL_REGISTER = BASE_IP_ADDRESS + "api/register.php";

    // Server user register url
    public static String URL_FORGOT = BASE_IP_ADDRESS + "api/forgotpwd.php";

    // server user booking ;
    public static String URL_BOOK_RIDE = BASE_IP_ADDRESS + "api/bookride.php";


    //get booking details
    public static String URL_USER_RIDE_LIST_NEXT = BASE_IP_ADDRESS + "api/listusernextride.php";

    //get sharing  details
    public static String URL_USER_SHARE_RIDE_LIST = BASE_IP_ADDRESS + "api/listusernextride.php";


    public static String URL_USER_RIDE_LIST_SHARE_RIDE = BASE_IP_ADDRESS + "api/sharingridehistory.php";


    public static String URL_GET_LIST_RIDE = BASE_IP_ADDRESS + "api/searchshareride.php";


    public static String URL_GET_SHARE_REQUEST_LIST = BASE_IP_ADDRESS + "api/requestlist.php";


    public static String URL_GET_HISTORY = BASE_IP_ADDRESS + "api/driverhistoryride.php";

    public static String URL_GET_RIDE_LIST_PRIVIOUS = BASE_IP_ADDRESS + "api/userridenext.php";


    public static String URL_GET__BOOKID_DATA = BASE_IP_ADDRESS + "api/bookiddata.php";


    public static String URL_GET_SHARE_REQUEST_DATA = BASE_IP_ADDRESS + "api/sharerequest.php";


    public static String URL_GET_SHARE_ACCEPT = BASE_IP_ADDRESS + "api/accept.php";


    private static AppConfig instance = new AppConfig();
    ConnectivityManager connectivityManager;

    boolean connected = false;
    static Context context;

    public static AppConfig getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.e("connectivity", e.toString());
        }
        return connected;
    }
}
