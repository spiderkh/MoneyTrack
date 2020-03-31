package com.bhaskar.moneytrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText email, pass;
    TextView forgotPass;
    Button loginbtn;
    String login_email, login_pass;
    private static final String TAG = "Login";
    ProgressBar loginprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.LoginEmail);
        pass = findViewById(R.id.LoginPass);
        loginbtn = findViewById(R.id.LoginBtn);
        forgotPass = findViewById(R.id.forgotPassText);
        loginprogressbar = findViewById(R.id.LoginProgressBar);

        TextView iv = (TextView) findViewById(R.id.loginappname);


        Typeface localTypeface2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        iv.setTypeface(localTypeface2);
        findViewById(R.id.LoginBtn).setOnClickListener(this);
        findViewById(R.id.GoToSignUpPage).setOnClickListener(this);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ResetPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }


    private void LoginUser() {
        login_email = email.getText().toString();
        login_pass = pass.getText().toString();
        Log.d(TAG, "Email= " + login_email);
        Log.d(TAG, "Pass= " + login_pass);

        if (login_email.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if (login_pass.isEmpty()) {
            pass.setError("Password Is Requred");
            pass.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
            email.setError("Enter Valid Email");
            email.requestFocus();
            return;

        }
        if (login_pass.length() < 6) {
            pass.setError("Minimum lenght of password should be 6");
            pass.requestFocus();
            return;
        }
        loginprogressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(login_email, login_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginprogressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Login.this, FragmentMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences pref = getSharedPreferences("profilePref", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json11 = pref.getString("ProfileObject", "noValue");
                    if (json11.equalsIgnoreCase("noValue")) {
                        Log.d(TAG, "SharedPreference not Exist");
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "SharedPreference Exist");
                        SharedPreferences.Editor prefsEditor = pref.edit();
                        prefsEditor.remove("ProfileObject");
                        prefsEditor.commit();
                        startActivity(intent);

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed Try Again", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.LoginBtn) {
            LoginUser();
        }
        if (view.getId() == R.id.GoToSignUpPage) {
            Intent intent = new Intent(Login.this, SignUp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
