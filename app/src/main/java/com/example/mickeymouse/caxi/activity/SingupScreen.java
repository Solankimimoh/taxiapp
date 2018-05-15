package com.example.mickeymouse.caxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.app.AppConfig;
import com.example.mickeymouse.caxi.app.AppController;
import com.example.mickeymouse.caxi.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingupScreen extends AppCompatActivity {

    private static final String TAG = SingupScreen.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_re_password;
    private EditText signup_fullname;
    private EditText signup_mobile;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_singup);

        init();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(RegisterActivity.this,
//                    MainActivity.class);
//            startActivity(intent);
//            finish();
//        }


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.getInstance(SingupScreen.this).isOnline()) {

                    String email = signup_email.getText().toString().trim();
                    String password = signup_password.getText().toString().trim();
                    String rePassword = signup_re_password.getText().toString().trim();
                    String fullName = signup_fullname.getText().toString().trim();
                    String mobile = signup_mobile.getText().toString().trim();

                    if (!email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty() && !fullName.isEmpty()) {
                        if (email.matches(emailPattern)) {

                            if (isValidPhone(mobile) && mobile.length() == 10) {
                                if (password.equals(rePassword)) {
                                    if (AppConfig.getInstance(SingupScreen.this).isOnline()) {
                                        registerUser(email, password, fullName, mobile);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Internet Connection Not Found ! Try Again", Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    Toast.makeText(getApplicationContext(), "Password and Re-Password Not Match ", Toast.LENGTH_LONG).show();
                                    signup_password.setText("");
                                    signup_re_password.setText("");

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect mobile length ", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Internet Connection Not Found ! Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * .,
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String email, final String password, final String fullName, final String mobile) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(SingupScreen.this, LoginScreen.class);
                        startActivity(intent);
                        finish();
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
                params.put("email", email);
                params.put("password", password);
                params.put("fullName", fullName);
                params.put("mobile", mobile);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void init() {

        signup_email = (EditText) findViewById(R.id.singup_email);
        signup_password = (EditText) findViewById(R.id.signup_passrod);
        signup_re_password = (EditText) findViewById(R.id.signup_repassword);
        signup_fullname = (EditText) findViewById(R.id.layout_signup_fullname);
        signup_mobile = (EditText) findViewById(R.id.signup_mobile);
        signup_btn = (Button) findViewById(R.id.signup_btn);

    }

}
