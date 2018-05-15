package com.example.mickeymouse.caxi.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mickeymouse.caxi.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MickeyMouse on 30-Mar-17.
 */

public class BookRide extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    public static final String TAG = "SampleActivityBase";
    private static final int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DROP = 2;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView pickup_location;
    private TextView pickup_date_txt;
    private TextView pickup_time;
    String car_type;
    private Button view_ride;

    LatLng pickupLatLng;
    LatLng dropLatLng;
    private Spinner choose_car;

    private TextView drop_location;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_bookride);


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


        ArrayList<String> choose_car = new ArrayList<String>();
        choose_car.add("Micro Car - 5/km");
        choose_car.add("Mini Car - 10/km");
        choose_car.add("Prmie Car - 20/km");

        car_type = "Micro Car - 5/km";


        this.choose_car.setOnItemSelectedListener(this);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choose_car);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        this.choose_car.setAdapter(dataAdapter);


        // Retrieve the TextViews that will display details about the selected place.

    }

    private void initview() {
        pickup_location = (TextView) findViewById(R.id.open_button);
        drop_location = (TextView) findViewById(R.id.drop_location);
        choose_car = (Spinner) findViewById(R.id.choose_car);
        pickup_date_txt = (TextView) findViewById(R.id.pickup_date);
        pickup_time = (TextView) findViewById(R.id.pickup_time);
        view_ride = (Button) findViewById(R.id.view_ride);

        pickup_time.setOnClickListener(this);
        pickup_date_txt.setOnClickListener(this);
        view_ride.setOnClickListener(this);

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
                        Toast.makeText(BookRide.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BookRide.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        car_type = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        } else if (v == pickup_time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            pickup_time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } else if (v == view_ride) {


            if (pickup_location.getText().toString().equals(getResources().getString(R.string.pikcup_location))
                    || drop_location.getText().toString().equals(getResources().getString(R.string.drop_location))
                    || pickup_date_txt.getText().toString().equals(getResources().getString(R.string.pickup_date))
                    || pickup_time.getText().toString().equals(getResources().getString(R.string.pickup_time))) {
                Toast.makeText(BookRide.this, "Please Fill the Booking Details", Toast.LENGTH_LONG).show();
            } else {


                String pickupDate = pickup_date_txt.getText().toString();
                String pickupTIme = pickup_time.getText().toString();
                Intent intent = new Intent(BookRide.this, ViewRide.class);
                intent.putExtra("pickup_LatLng", pickupLatLng);
                intent.putExtra("drop_LatLng", dropLatLng);
                intent.putExtra("car_type", car_type);
                intent.putExtra("pickup_date_txt", pickupDate);
                intent.putExtra("pickup_time", pickupTIme);
                startActivity(intent);

            }
        }
    }

}

