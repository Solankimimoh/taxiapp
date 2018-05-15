package com.example.mickeymouse.caxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.app.AppConfig;
import com.example.mickeymouse.caxi.app.AppController;
import com.example.mickeymouse.caxi.helper.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewRide extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {


    private static final String TAG = SingupScreen.class.getSimpleName();

    private GoogleMap mMap;
    private Button confrim_ride;
    private ProgressDialog pDialog;
    private SessionManager session;
    LatLng pickupLatLng;
    LatLng dropLatLng;
    String cartype;
    String pickup_date_tx;
    String pickup_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewride);
        session = new SessionManager(ViewRide.this);
        initview();




        pickupLatLng = getIntent().getParcelableExtra("pickup_LatLng");
        dropLatLng = getIntent().getParcelableExtra("drop_LatLng");
        cartype = getIntent().getExtras().getString("car_type");
        pickup_date_tx = getIntent().getExtras().getString("pickup_date_txt");
        pickup_time = getIntent().getExtras().getString("pickup_time");

        confrim_ride.setText("Book Now " + "-(" + cartype + ")");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void initview() {

        confrim_ride = (Button) findViewById(R.id.confirm_ride);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        confrim_ride.setOnClickListener(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        DrawRouteMaps.getInstance(this)
                .draw(pickupLatLng, dropLatLng, mMap);
        DrawMarker.getInstance(this).draw(mMap, pickupLatLng, R.drawable.pickup, "Pickup Location");
        DrawMarker.getInstance(this).draw(mMap, dropLatLng, R.drawable.drop, "Drop Location");


        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(pickupLatLng)
                .include(dropLatLng).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

    }

    @Override
    public void onClick(View v) {

        if (v == confrim_ride) {
            if (AppConfig.getInstance(ViewRide.this).isOnline()) {


                String pickup_location = pickupLatLng.latitude + "," + pickupLatLng.longitude;

                String drop_location = dropLatLng.latitude + "," + dropLatLng.longitude;

                String car_type = cartype.toString();

                String pick_date = pickup_date_tx.toString();

                String pick_time = pickup_time.toString();

                bookngData(pickup_location, drop_location, car_type, pick_date, pick_time);

            }
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void bookngData(final String pickup_loc, final String drop_loc, final String car_type, final String pick_date, final String pick_time) {
        // Tag used to cancel the request
        String tag_string_req = "req_booking";

        final String username = session.userNameSession();


        pDialog.setMessage("Booking ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOK_RIDE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Booking Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "Booking Success", Toast.LENGTH_LONG).show();

//                        // Launch login activity
                        Intent intent = new Intent(ViewRide.this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                       BookRide bookRide=new BookRide();

                        bookRide.finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pickup", pickup_loc);
                params.put("drop", drop_loc);
                params.put("car_type", car_type);
                params.put("pick_date", pick_date);
                params.put("pick_time", pick_time);
                params.put("user_id", username);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
