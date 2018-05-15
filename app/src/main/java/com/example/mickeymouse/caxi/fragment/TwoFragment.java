package com.example.mickeymouse.caxi.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.adapter.HistoryAdapter;
import com.example.mickeymouse.caxi.app.AppConfig;
import com.example.mickeymouse.caxi.app.AppController;
import com.example.mickeymouse.caxi.helper.SessionManager;
import com.example.mickeymouse.caxi.model.FilterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TwoFragment extends Fragment {

    private static final String TAG = TwoFragment.class.getSimpleName();


    private ProgressDialog pDialog;
    private SessionManager session;


    private List<FilterData> filterDataArrayList = new ArrayList<FilterData>();
    private ListView listView;
    private HistoryAdapter adapter;


    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        session = new SessionManager(getActivity());

        View view = inflater.inflate(R.layout.fragment_two, null);


        listView = (ListView) view.findViewById(R.id.history);
        adapter = new HistoryAdapter(getActivity(), filterDataArrayList);
        listView.setAdapter(adapter);


        pDialog = new ProgressDialog(getActivity().getApplicationContext());


        if (AppConfig.getInstance(getActivity().getApplicationContext()).isOnline()) {

            String drivername = session.userNameSession();

            getridedetails(drivername);

        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    /**
     * function to verify login details in mysql db
     */
    private void getridedetails(final String username) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_RIDE_LIST_PRIVIOUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                       // Toast.makeText(getActivity().getApplicationContext(), "No Data Available", Toast.LENGTH_LONG).show();

                    } else {


                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            FilterData filterData = new FilterData();

                            filterData.setBookid(jsonObject.getString("book_id"));
                            filterData.setUserName(jsonObject.getString("user_id"));
                            filterData.setCarType(jsonObject.getString("pick_date"));
                            filterData.setTime(jsonObject.getString("pick_time"));


                            filterDataArrayList.add(filterData);


                            Log.e(TAG, "TIME" + jsonObject.getString("pick_time"));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);


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
        if ((pDialog != null) && pDialog.isShowing())
            // pDialog.dismiss();
            pDialog = null;
    }

}
