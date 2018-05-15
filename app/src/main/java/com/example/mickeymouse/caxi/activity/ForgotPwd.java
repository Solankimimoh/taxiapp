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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPwd extends AppCompatActivity {

    private static final String TAG = SingupScreen.class.getSimpleName();

    EditText email;
    EditText newPwd;
    EditText newrePwd;
    EditText mobile;
    Button submit;


    private ProgressDialog pDialog;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);


        initview();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailData = email.getText().toString().trim();
                String mobileData = mobile.getText().toString().trim();
                String newpwdData = newPwd.getText().toString().trim();
                String newrepwdData = newrePwd.getText().toString().trim();

                if (!emailData.isEmpty() && !mobileData.isEmpty() && !newpwdData.isEmpty() && !newrepwdData.isEmpty()) {
// onClick of button perform this simplest code.
                    if (emailData.matches(emailPattern)) {
                        if (isValidPhone(mobileData) && mobileData.length() == 10) {
                            if (newpwdData.equals(newrepwdData)) {
                                if (AppConfig.getInstance(ForgotPwd.this).isOnline()) {
                                    forgotpwd(emailData, newpwdData, mobileData);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internet Connection Not Found ! Try Again", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                Toast.makeText(getApplicationContext(), "Password and Re-Password Not Match ", Toast.LENGTH_LONG).show();
                                newPwd.setText("");
                                newrePwd.setText("");

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Movile Number", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
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

    private void forgotpwd(final String email, final String password,  final String mobile) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FORGOT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "FORGOT Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(ForgotPwd.this, LoginScreen.class);
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

    private void initview() {

        email = (EditText) findViewById(R.id.forgot_singup_email);
        newPwd = (EditText) findViewById(R.id.forgot_signup_passrod);
        newrePwd = (EditText) findViewById(R.id.forgot_signup_repassword);
        mobile = (EditText) findViewById(R.id.forgot_signup_mobile);

        submit = (Button) findViewById(R.id.forgot_signup_btn);
    }
}
