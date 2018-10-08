package com.codeit.team_4.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeit.team_4.MainNavigationActivity;
import com.codeit.team_4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    private final String[] name = new String[1];
    private final String[] phone = new String[1];
    private final String[] email = new String[1];
    private final String[] balance = new String[1];

    //private String uid;

    public static final String USERDETAILSSHAREDPREF = "userDetailsSharedPref";
    SharedPreferences userDetails;

    public static final String TAG = "UserReqTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(this);

        if(auth.getCurrentUser()!= null) {
            startActivity(new Intent(this, MainNavigationActivity.class));
            //goToActivity(getSharedPreferences(USERDETAILSSHAREDPREF, 0).getString("userType", ""));
            finish();
        }

        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button resetPassword = (Button) findViewById(R.id.btn_reset_password);
        Button signUp = (Button) findViewById(R.id.btn_register);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String ipemail = inputEmail.getText().toString();
                final String ippassword = inputPassword.getText().toString();

                if (TextUtils.isEmpty(ipemail)) {
                    inputEmail.setError(getString(R.string.enter_email));
                    return;
                }

                if (TextUtils.isEmpty(ippassword)) {
                    inputPassword.setError(getString(R.string.enter_password));
                    return;
                }

                if (ippassword.length() < 6) {
                    inputPassword.setError(getString(R.string.minimum_password));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(ipemail, ippassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /* If sign in fails, display a message to the user. If sign in succeeds
                           the auth state listener will be notified and logic to handle the
                           signed in user can be handled in the listener.*/

                        if(!task.isSuccessful()){
                            // error in login
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        } else if (!auth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "Login failed, please verify from the email sent to your account", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            /*if (auth.getCurrentUser() != null) {
                                uid = auth.getCurrentUser().getUid();
                            }*/

                            StringRequest stringRequest = makeRequest();
                            stringRequest.setTag(TAG);

                            requestQueue.add(stringRequest);
                        }
                    }
                });
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    StringRequest makeRequest() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/login.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Log.d("hola",response);
                try {
                   // JSONObject jsonRootObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject= jsonArray.getJSONObject(0);
                    name[0] = jsonObject.optString("name");
                    phone[0] = jsonObject.optString("phone");
                    email[0] = jsonObject.optString("email");
                    balance[0] = jsonObject.optString("balance");

                 //   Toast.makeText(LoginActivity.this,response, Toast.LENGTH_LONG).show();

                    if (saveUserDetailsSharedPref()) {
                        goToActivity();
                    }
                    progressBar.setVisibility(View.GONE);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this,"error"+e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("email",inputEmail.getText().toString());
                return params;
            }
        };
    }

    boolean saveUserDetailsSharedPref() {
        userDetails = getSharedPreferences(USERDETAILSSHAREDPREF, 0);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString("name", name[0]);
        editor.putString("phone", phone[0]);
        editor.putString("email", email[0]);
        editor.putString("balance", balance[0]);
        boolean status = editor.commit();
        Log.d("LoginActivity", String.valueOf(userDetails.getInt(USERDETAILSSHAREDPREF,1)));
        return status;
    }

    void goToActivity() {
        startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
    }

}
