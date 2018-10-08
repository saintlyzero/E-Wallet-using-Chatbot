package com.codeit.team_4.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeit.team_4.R;
import com.codeit.team_4.adapter.TransactionAdapter;
import com.codeit.team_4.helper.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class StatementFragment extends Fragment {
    RecyclerView recyclerView;
    List<Transaction> transactions;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    EditText fromDate;
    EditText toDate;
    Button button;
    SimpleDateFormat simpleDateFormat;
    Date from;
    Date to;
    public StatementFragment() {
        // Required empty public constructor
    }

    public static StatementFragment newInstance() {
        return new StatementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        return inflater.inflate(R.layout.fragment_statement, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromDate = this.getActivity().findViewById(R.id.fromDate);
        toDate = this.getActivity().findViewById(R.id.toDate);
        button = (Button)this.getActivity().findViewById(R.id.statbutton);

        StringRequest stringRequest = makeRequest();
        stringRequest.setTag("defTAG");
        requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(stringRequest);
        prefs = this.getActivity().getSharedPreferences(
                "userDetailsSharedPref", MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    from = simpleDateFormat.parse(fromDate.getText().toString());
                    to = simpleDateFormat.parse(toDate.getText().toString());
                    StringRequest stringRequest1 = makeRequest1();
                    requestQueue.add(stringRequest1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    StringRequest makeRequest() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/getAllTransactions.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j  = new JSONObject(response).getJSONArray("data");
                    ShowCards(j);
                } catch (JSONException e) {
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
                Log.d("StatementFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }
    StringRequest makeRequest1() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/getTransactionsByDate.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", response);
                    JSONArray j  = new JSONObject(response).getJSONArray("data");
                    ShowCards(j);
                } catch (JSONException e) {
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
                params.put("sDate",fromDate.getText().toString());
                params.put("eDate",toDate.getText().toString());
                Log.d("StatementFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }

    void ShowCards(JSONArray jsonArray) throws JSONException {
        transactions= new ArrayList<>();
        recyclerView=(RecyclerView)this.getActivity().findViewById(R.id.recylcerView);

        String name="";
        String acc="";
        ArrayList<String> type = new ArrayList<String>();
        ArrayList<String> sender = new ArrayList<String>();
        ArrayList<String> receiver = new ArrayList<String>();
        ArrayList<String> amount = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();
        ArrayList<String> rname = new ArrayList<String>();
        ArrayList<String> sname = new ArrayList<String>();

        for(int i=0;i<jsonArray.length();i++)
        {
            type.add(jsonArray.getJSONObject(i).getString("type"));
            sender.add(jsonArray.getJSONObject(i).getString("sender"));
            receiver.add(jsonArray.getJSONObject(i).getString("receiver"));
            amount.add(jsonArray.getJSONObject(i).getString("amount"));
            date.add(jsonArray.getJSONObject(i).getString("date"));
            description.add(jsonArray.getJSONObject(i).getString("description"));
            rname.add(jsonArray.getJSONObject(i).getString("receiver_name"));
            sname.add(jsonArray.getJSONObject(i).getString("sender_name"));
            Log.d("test123",description.get(i));
            if(type.get(i).equals("0"))
            {
                 name=rname.get(i);
                 acc=receiver.get(i);
            }
            else
            {
                name=sname.get(i);
                acc=sender.get(i);
            }

            transactions.add(new Transaction(name,acc,type.get(i),amount.get(i),description.get(i)));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        TransactionAdapter adapter = new TransactionAdapter(this.getActivity(),transactions);
        recyclerView.setAdapter(adapter);
    }

}
