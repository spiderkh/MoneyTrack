package com.bhaskar.moneytrack;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.logging.SocketHandler;

public class FragmentMain extends AppCompatActivity {
    ProfileDetail pd;
    MonthLimit ml;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String Email1="";
    private static final String TAG = "FragmentMain";
    SharedPreferences pref;
    Gson gson;
    String dbUsedForFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        pref=getSharedPreferences("profilePref",Context.MODE_PRIVATE);
        dbUsedForFirebase=getResources().getString(R.string.firebaseDbUsed);
        gson = new Gson();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Frame_home()).commit();
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        if (currentUser != null) {

            Email1 = currentUser.getEmail();
            // Toast.makeText(HomePage.this,"Email= "+Email1,Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getApplicationContext(),"Log In Again",Toast.LENGTH_SHORT).show();
        }
        pd=new ProfileDetail(Email1,dbUsedForFirebase);
        ml=new MonthLimit(FragmentMain.this);


    }
    private  BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment=null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new Frame_home();
                        break;
                       case R.id.nav_viewDetails:
                           try {
                               String json11 = pref.getString("ProfileObject", "noValue");
                               if (json11.equalsIgnoreCase("noValue")) {
                                   Log.d(TAG, "SharedPrtef Not Exists ");
                                   Log.d(TAG, "profile pd= " + pd.getAllDetails().toString());
                                   Profile pf = pd.getAllDetails();
                                   Log.d(TAG, "SharedPrtef return NoValue");
                                   SharedPreferences.Editor prefsEditor = pref.edit();
                                   String json = gson.toJson(pf);
                                   prefsEditor.putString("ProfileObject", json);
                                   prefsEditor.commit();
                               } else {
                                   Log.d(TAG, "SharedPrtef  Exists wow go ahead WIthout Worry");
                               }
                           }catch (Exception e)
                           {
                               Log.d(TAG,"inside cath of FragmentMain for ViewDetails ");
                               Log.d(TAG,e.getMessage());
                               break;
                           }
                           selectedFragment = new Frame_details();
                            break;
                        case R.id.nav_profile:
                            try{
                                String json1 = pref.getString("ProfileObject", "noValue");
                                if(json1.equalsIgnoreCase("noValue"))
                                {
                                    Log.d(TAG,"profile pd profileshow= "+pd.getAllDetails().toString());
                                    Profile pf=pd.getAllDetails();
                                    Log.d(TAG,"SharedPrtef return NoValue");
                                    SharedPreferences.Editor prefsEditor = pref.edit();
                                    String json=gson.toJson(pf);
                                    prefsEditor.putString("ProfileObject", json);
                                    prefsEditor.commit();


                                }
                                else
                                {
                                    Log.d(TAG, "SharedPrtef profile  Exists wow go ahead WIthout Worry");
                                }
                                selectedFragment = new Frame_profile();


                            }catch (Exception e)
                            {
                                Log.d(TAG,"inside cath of FragmentMain for ProfileShow ");
                                Log.d(TAG,e.getMessage());

                                break;
                            }

                            break;
                    }

                    try {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }catch (NullPointerException v)
                    {
                        Toast.makeText(getApplicationContext(),"Please Wait Data is Loading.....",Toast.LENGTH_SHORT).show();
                    }





                    return true;
                }


            };

    @Override
    public void onBackPressed(){




      /*AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMain.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(R.layout.exit_layout);
        Button btnYes=findViewById(R.id.yesIdOnExit);
        Button btnNo=findViewById(R.id.noIdOnExit);



        builder.setMessage("Do you Realy want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/

        Dialog epicDialog=new Dialog(this);
        epicDialog.setContentView(R.layout.exit_layout);
        ImageView btnYes=epicDialog.findViewById(R.id.yesIdOnExit);
        ImageView btnNo=epicDialog.findViewById(R.id.noIdOnExit);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(FragmentMain.this,"Yes",Toast.LENGTH_SHORT).show();
                epicDialog.dismiss();
                finish();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(FragmentMain.this,"No",Toast.LENGTH_SHORT).show();
                epicDialog.dismiss();

            }
        });





    }

}
