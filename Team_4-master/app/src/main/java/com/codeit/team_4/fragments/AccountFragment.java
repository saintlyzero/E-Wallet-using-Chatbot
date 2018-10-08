package com.codeit.team_4.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeit.team_4.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
        private GraphView graphView;
        SharedPreferences prefs;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        prefs = this.getActivity().getSharedPreferences(
                "userDetailsSharedPref", MODE_PRIVATE);

        StringRequest stringRequest = makeRequest();
        stringRequest.setTag("defTAG");
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(stringRequest);
        StringRequest stringRequest1 = getBalance();
        stringRequest1.setTag("defTAG1");
        requestQueue.add(stringRequest1);

        View view  = inflater.inflate(R.layout.fragment_account, container, false);

        graphView = (GraphView) view.findViewById(R.id.graph);
        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.getViewport().setScrollableY(true); // enables vertical scrolling
        graphView.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphView.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        return view;
    }


    void drawGraph(JSONArray jsonArray,JSONArray jsonArray1 )throws Exception
    {
        DataPoint[] dataPoints = new DataPoint[jsonArray.length()];
        DataPoint[] dataPoints1 = new DataPoint[jsonArray1.length()];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<jsonArray.length();i++)
        {
            Date d = simpleDateFormat.parse(jsonArray.getJSONObject(i).getString("date"));
            Log.d("AccountFragment",d.toString());
            dataPoints[i] = new DataPoint(d,jsonArray.getJSONObject(i).getInt("credit"));
        }

        for(int i=0;i<jsonArray1.length();i++)
        {
            Date d = simpleDateFormat.parse(jsonArray1.getJSONObject(i).getString("date"));
            Log.d("AccountFragment",d.toString());
            dataPoints1[i] = new DataPoint(d,jsonArray1.getJSONObject(i).getInt("debit"));
        }

        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphView.getViewport().setMinX(simpleDateFormat.parse(jsonArray.getJSONObject(0).getString("date")).getTime());
        graphView.getViewport().setMaxX(simpleDateFormat.parse(jsonArray.getJSONObject(jsonArray.length()-1).getString("date")).getTime());

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        lineGraphSeries.setTitle("Debit History");
        lineGraphSeries.setColor(Color.RED);
        graphView.addSeries(lineGraphSeries);

        LineGraphSeries<DataPoint> lineGraphSeries1 = new LineGraphSeries<>(dataPoints1);
        lineGraphSeries1.setTitle("Credit History");
        lineGraphSeries1.setColor(Color.GREEN);
        graphView.addSeries(lineGraphSeries1);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    StringRequest makeRequest() {
        String SEND_URL = "http://team4codeit.000webhostapp.com/CreditDebit.php";

        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AccountFragment",response);
                try {
                    drawGraph(new JSONObject(response).getJSONArray("creditDetails"),new JSONObject(response).getJSONArray("debitDetails"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error","error fetching details via volley");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("mobile",prefs.getString("phone","error"));
                Log.d("AccountFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }
    StringRequest getBalance() {
        String SEND_URL = "http://team4codeit.000webhostapp.com/getBalance.php";

        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AccountFragment",response);
                TextView textView = AccountFragment.this.getActivity().findViewById(R.id.tvbalance);
                textView.setText(textView.getText()+" "+response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error","error fetching details via volley");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("mobile",prefs.getString("phone","error"));
                Log.d("AccountFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }


}
