package com.bhaskar.moneytrack;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //TextView tv=(TextView)findViewById(R.id.splashText);
        TextView iv = (TextView) findViewById(R.id.splashtxt);


        Typeface localTypeface2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        iv.setTypeface(localTypeface2);

        //  ImageView ivmsg=(ImageView)findViewById(R.id.splashmsg);
        Animation anima = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.myanimation);

      /*  RotateAnimation animation= new RotateAnimation(0,1080, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(5000);*/

        iv.startAnimation(anima);
        Animation animd = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.downtoup);
        // ivmsg.startAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "Enter In Timer= ");
                mAuth = FirebaseAuth.getInstance();


                if (mAuth.getCurrentUser() != null) {
                    Log.d(TAG, "User ALreay Logged In with Id= " + mAuth.getCurrentUser());
                    finish();
                    Intent intent1 = new Intent(SplashScreen.this, FragmentMain.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();


                } else {
                    Log.d(TAG, "User Not Logged In");
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }


            }
        }, 2000);


    }
}
