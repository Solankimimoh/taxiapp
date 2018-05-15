package com.example.mickeymouse.caxi.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mickeymouse.caxi.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class ShareRide extends AppCompatActivity implements View.OnClickListener {


    public static final String TAG = "SampleActivityBase";
    private static final int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DROP = 2;

    private int mYear, mMonth, mDay;

    private TextView pickup_location;
    private TextView drop_location;
    private TextView pickup_date_txt;

    private Button searchride;

    LatLng pickupLatLng;
    LatLng dropLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_ride);

        initview();

        pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(1);
            }
        });

        drop_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(2);
            }
        });


        // Retrieve the TextViews that will display details about the selected place.

    }

    private void initview() {
        pickup_location = (TextView) findViewById(R.id.search_pickup);
        drop_location = (TextView) findViewById(R.id.drop_location);
        pickup_date_txt = (TextView) findViewById(R.id.pickup_date);
        searchride = (Button) findViewById(R.id.search_ride);
        pickup_date_txt.setOnClickListener(this);
        searchride.setOnClickListener(this);

    }


    private void openAutocompleteActivity(Integer codeID) {
        if (codeID == 1) {
            try {
                // The autocomplete activity requires Google Play Services to be available. The intent
                // builder checks this and throws an exception if it is not the case.
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_PICKUP);
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play Services is either not installed or not up to date. Prompt
                // the user to correct the issue.
                GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates that Google Play Services is not available and the problem is not easily
                // resolvable.
                String message = "Google Play Services is not available: " +
                        GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } else if (codeID == 2) {
            try {
                // The autocomplete activity requires Google Play Services to be available. The intent
                // builder checks this and throws an exception if it is not the case.
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_DROP);
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play Services is either not installed or not up to date. Prompt
                // the user to correct the issue.
                GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates that Google Play Services is not available and the problem is not easily
                // resolvable.
                String message = "Google Play Services is not available: " +
                        GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_PICKUP) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(TAG, "Place Selected: " + place.getName());


                pickupLatLng = place.getLatLng();

                // Format the place's details and display them in the TextView.
//                Toast.makeText(BookRide.this, formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()), Toast.LENGTH_LONG).show();
                pickup_location.setText(place.getName() + "-" + place.getAddress());

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Toast.makeText(ShareRide.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_DROP) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                dropLatLng = place.getLatLng();


                // Format the place's details and display them in the TextView.
//                Toast.makeText(BookRide.this, formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()), Toast.LENGTH_LONG).show();
                drop_location.setText(place.getName() + "-" + place.getAddress());

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Toast.makeText(ShareRide.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    @Override
    public void onClick(View v) {

        if (v == pickup_date_txt) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            pickup_date_txt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        } else if (v == searchride) {


            if (pickup_location.getText().toString().equals(getResources().getString(R.string.pikcup_location))
                    || drop_location.getText().toString().equals(getResources().getString(R.string.drop_location))
                    || pickup_date_txt.getText().toString().equals(getResources().getString(R.string.pickup_date))) {
                Toast.makeText(ShareRide.this, "Please Fill the Booking Details", Toast.LENGTH_LONG).show();
            } else {



                String pickupDate = pickup_date_txt.getText().toString();

                Intent intent = new Intent(ShareRide.this, ShareRideListView.class);

                Bundle args = new Bundle();
                args.putParcelable("pickup_LatLng", pickupLatLng);
                args.putParcelable("drop_LatLng", dropLatLng);
                args.putString("pickup_date_txt", pickupDate);

                intent.putExtra("bundle", args);
                startActivity(intent);

            }
        }
    }

}
