package com.codeit.team_4.login;

import android.content.Intent;
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
import com.codeit.team_4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, phone, password, confirmPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.register_progressbar);

        name = (EditText) findViewById(R.id.register_name);
        email = (EditText) findViewById(R.id.register_email);
        phone = (EditText) findViewById(R.id.register_phone);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);

        Button register = (Button) findViewById(R.id.register_btn_signup);
        Button login = (Button) findViewById(R.id.register_btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pass = password.getText().toString();
                final String confpass = confirmPassword.getText().toString();
                final String emailid = email.getText().toString();
                final String mobile = phone.getText().toString();
                final String uname = name.getText().toString();

                if (TextUtils.isEmpty(emailid)) {
                    email.setError(getString(R.string.enter_email));
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError(getString(R.string.enter_password));
                    return;
                }
                if (pass.length() < 6) {
                    password.setError(getString(R.string.minimum_password));
                    return;
                }
                if (TextUtils.isEmpty(confpass)) {
                    confirmPassword.setError(getString(R.string.enter_confirm_password));
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    email.setError(getString(R.string.enter_mobile));
                    return;
                }
                if (mobile.length() < 10) {
                    password.setError(getString(R.string.minimum_mobile));
                    return;
                }
                if (TextUtils.isEmpty(uname)) {
                    email.setError(getString(R.string.enter_name));
                    return;
                }
                if (!pass.equals(confpass)) {
                    confirmPassword.setError(getString(R.string.password_dont_match));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Verification email sent! Please check your email and verify!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed, try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Log.d("register", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Registration successful! please Login.", Toast.LENGTH_SHORT).show();

                            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                            StringRequest stringRequest = makeRequest();
                            stringRequest.setTag("registration");
                            requestQueue.add(stringRequest);

                            // FirebaseUser user = auth.getCurrentUser();
                            auth.signOut();

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            //finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    StringRequest makeRequest() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/register.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("registerhola",response);
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
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", phone.getText().toString());
                params.put("balance", "5000");
                return params;
            }
        };
    }

}
