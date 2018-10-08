package com.codeit.team_4.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeit.team_4.R;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundTransferFragment extends Fragment {

    SharedPreferences prefs;

    public FundTransferFragment() {
        // Required empty public constructor
    }

    public static FundTransferFragment newInstance() {
        return new FundTransferFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = this.getActivity().getSharedPreferences(
                "userDetailsSharedPref", MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_fund_transfer, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageButton b = this.getActivity().findViewById(R.id.transferimgbtn1);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FundTransferFragment.this.getActivity(), ContactPickerActivity.class)

                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                                .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT,1)
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
                        startActivityForResult(intent,1);

                    }


                }

                );
        Button button = this.getActivity().findViewById(R.id.transferbtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = makeRequest();
                stringRequest.setTag("defTAG");
                RequestQueue requestQueue = Volley.newRequestQueue(FundTransferFragment.this.getActivity());
                requestQueue.add(stringRequest);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FundTransferFragment","onActivityResult entered");


            // we got a result from the contact picker

            // process contacts
        if (requestCode == 1 && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {
            List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            for (Contact contact : contacts) {
                Log.d("FundTransferFragment", contact.getPhone(0));
                String s = contact.getPhone(0);
                s = s.replaceAll("\\s+","");
                s = s.substring(s.length()-10,s.length());
                EditText t = (EditText)FundTransferFragment.this.getActivity().findViewById(R.id.transferet1);
                t.setText(s);
            }


        }

    }

    StringRequest makeRequest() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/transaction.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Log.d("FundTransferFragment",response);
                        if(response.equals("1")) {
                            Log.d("FundTransferFragment", "Successful");
                            Toast.makeText(FundTransferFragment.this.getActivity(), "transfer successful", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals("-1")) {
                            Log.d("FundTransferFragment", "Unsuccessful");
                            Toast.makeText(FundTransferFragment.this.getActivity(), "Less balance ", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals("0")) {
                            Log.d("FundTransferFragment", "Unsuccessful");
                            Toast.makeText(FundTransferFragment.this.getActivity(), "user not found", Toast.LENGTH_SHORT).show();
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
                EditText editText = (EditText) FundTransferFragment.this.getActivity().findViewById(R.id.transferet1);
                Map<String,String> params = new HashMap<>();
                params.put("sName",prefs.getString("name","error"));
                params.put("sPhone",prefs.getString("phone","error"));
                params.put("rPhone",editText.getText().toString());
                editText = (EditText) FundTransferFragment.this.getActivity().findViewById(R.id.transferamt);
                params.put("amount",editText.getText().toString());
                editText = (EditText) FundTransferFragment.this.getActivity().findViewById(R.id.transferdesc);
                params.put("description",editText.getText().toString());


                Log.d("StatementFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }



}
