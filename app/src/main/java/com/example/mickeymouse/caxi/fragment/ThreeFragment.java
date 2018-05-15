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
import com.example.mickeymouse.caxi.adapter.ShareAdapter;
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


public class ThreeFragment extends Fragment {

    private static final String TAG = OneFragment.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;


    private List<FilterData> filterDataArrayList1 = new ArrayList<FilterData>();
    private ListView listView;
    private ShareAdapter adapter1;


    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionManager(getActivity());

        View view = inflater.inflate(R.layout.fragment_three, null);


        listView = (ListView) view.findViewById(R.id.sharingdata);
        adapter1 = new ShareAdapter(getActivity(), filterDataArrayList1);
        listView.setAdapter(adapter1);


        pDialog = new ProgressDialog(getActivity().getApplicationContext());


        if (AppConfig.getInstance(getActivity().getApplicationContext()).isOnline()) {

            String username = session.userNameSession();

            getsharingdata(username);

        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filterDataArrayList1.clear();
        adapter1.notifyDataSetChanged();

    }

    /**
     * function to verify login details in mysql db
     */
    private void getsharingdata(final String username) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USER_RIDE_LIST_SHARE_RIDE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        //Toast.makeText(getActivity().getApplicationContext(), "No Data Available for Selected Date", Toast.LENGTH_LONG).show();

                    } else {


                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            FilterData filterData1 = new FilterData();

                            filterData1.setBookid(jsonObject.getString("share_req_user"));
                            filterData1.setUserName(session.userNameSession());

                            String shareStatus = jsonObject.getString("is_share");

                            if (shareStatus.equals("1")) {
                                filterData1.setCarType("Share Accepted");
                            } else {
                                filterData1.setCarType("Pending ");
                            }

                            filterData1.setTime(jsonObject.getString("share_with_user"));

                            filterDataArrayList1.add(filterData1);

                            Log.e(TAG, "TIME" + jsonObject.getString("pick_time"));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                    adapter1.notifyDataSetChanged();

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
