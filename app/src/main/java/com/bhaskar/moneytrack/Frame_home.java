package com.bhaskar.moneytrack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Frame_home extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Spinner spinnercat;
    EditText comt, amot;
    TextView appupdatemsg;
    String CommentText, DropCategory;
    Button SubmitBtn, ViewBtn;
    double Amount;
    private DatabaseReference mDatabase;
    ProgressBar loginprogressbar,cpb;
    Profile pp;
    ProfileDetail pd;
    String Email1 = "noMail";
    Boolean dailogupdate,forceupdate,appmsg;
    String appfullMsg;




    private static final String TAG = "Frame_home";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_home_page, container, false);


        spinnercat = v.findViewById(R.id.Category);
        comt = v.findViewById(R.id.CommentText);
        amot = v.findViewById(R.id.AmountText);
        appupdatemsg=v.findViewById(R.id.AppUpdateMsg);
        SubmitBtn = v.findViewById(R.id.submitbtn1);
        loginprogressbar = v.findViewById(R.id.LoginProgressBarHome);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");




        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Log.d(TAG,"key of users= "+dataSnapshot1.getKey());
                    if(dataSnapshot1.getKey().equals("updatedailog"))
                    {
                        dailogupdate=dataSnapshot1.getValue(Boolean.class);
                        Log.d(TAG,"inside dailogupdate ="+dailogupdate);
                        if (dailogupdate)
                        {
                            try {
                                AppUpdateChecker1 appUpdateChecker = new AppUpdateChecker1(getActivity());  //pass the activity in constructure
                                appUpdateChecker.checkForUpdate(false);
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG,"error =+"+e.getMessage());
                            }

                        }
                    }


                    if(dataSnapshot1.getKey().equals("AppMessageInHome"))
                    {
                        appfullMsg=dataSnapshot1.getValue(String.class);
                        Log.d(TAG,"inside appmessageinhome ="+appfullMsg);

                    }

                    if(dataSnapshot1.getKey().equals("appmsg"))
                    {
                        appmsg=dataSnapshot1.getValue(Boolean.class);
                        Log.d(TAG,"inside appmsg ="+appmsg);
                        if(appmsg)
                        {
                            if (appfullMsg!=null)
                            {
                                appupdatemsg.setText(appfullMsg);
                                appupdatemsg.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                    if(dataSnapshot1.getKey().equals("forceupdate"))
                    {
                        forceupdate=dataSnapshot1.getValue(Boolean.class);
                        Log.d(TAG,"inside Forceupdate ="+forceupdate);
                        if(forceupdate)
                        {


                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getActivity().getPackageName())));


                        }
                    }




                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG,"dailog upadtae = "+dailogupdate);
        Log.d(TAG,"Force upadtae = "+forceupdate);







        ArrayList<String> CatList=new ArrayList<>();
        CatList.add("Veg");
        CatList.add("Chicken");
        CatList.add("Dmart");
        CatList.add("Water");
        CatList.add("Rent");
        CatList.add("Massi");
        CatList.add("Mainten");
        CatList.add("Wifi");
        CatList.add("Gas");
        CatList.add("Travel");
        CatList.add("Other");

        final ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getContext(),R.layout.myspinnerstylehome,CatList);
        dataAdapter.setDropDownViewResource(R.layout.myspinnerstylehome);
        spinnercat.setAdapter(dataAdapter);

      /*  int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
       if(versionCode==4)
       {
           appupdatemsg.setVisibility(View.VISIBLE);
       }
*/
        currentUser= mAuth.getCurrentUser();
        if (currentUser != null) {

            Email1 = currentUser.getEmail();
            // Toast.makeText(HomePage.this,"Email= "+Email1,Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getContext(),"Log In Again",Toast.LENGTH_SHORT).show();
        }

        pd=new ProfileDetail(Email1);


        final String finalEmail = Email1;



        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommentText=comt.getText().toString();
                DropCategory=String.valueOf(spinnercat.getSelectedItem());
                try {
                    Amount = Double.parseDouble(amot.getText().toString());
                }catch (NumberFormatException r)
                {
                    amot.setError("Enter Valid Amount");
                    amot.requestFocus();
                    return;
                }
                if (CommentText.isEmpty())
                {
                    comt.setError("Comment is Required");
                    comt.requestFocus();
                    return;
                }
                if (amot.getText().toString().isEmpty())
                {
                    amot.setError("Amount Cannot Be Empty");
                    amot.requestFocus();
                    return;
                }

                loginprogressbar.setVisibility(View.VISIBLE);
                Date currentTime = Calendar.getInstance().getTime();

                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                DateFormat timeDateFormat=new SimpleDateFormat("HH:mm");

                DateFormat monthDateFormat=new SimpleDateFormat("MMMM-yyyy");
                Date date = new Date();
                String strDate = dateFormat.format(date);
                String monthYearString=monthDateFormat.format(date);
                String[] monthYearStringArray=monthYearString.split("-");

                String getMonthName= monthYearStringArray[0];
                String yearName=monthYearStringArray[1];
                String getTime= timeDateFormat.format(date);


                //Toast.makeText(HomePage.this,"post Added Comment= "+CommentText+" Amount= "+Amount+" DropCategory= "+DropCategory+" Date= "+strDate,Toast.LENGTH_SHORT).show();





                pp=pd.getAllDetails();

                Log.d(TAG,"profile after Method in HomePAge");
                Log.d(TAG,"profile in Home Page= "+pp.toString());
                String UserRole=pp.getRole();
                String status="ND";
                if(UserRole.equalsIgnoreCase("Admin"))
                {
                    status="D";
                }


                String userId = mDatabase.push().getKey();
                //Toast.makeText(HomePage.this,"UserID= "+userId,Toast.LENGTH_SHORT).show();
                Post post=new Post(DropCategory,Amount,CommentText,strDate, finalEmail,getMonthName,yearName,getTime,pp.getShortName(),status,userId,"N");

                mDatabase.child("expenses").child(pp.getGroupCode()).child(yearName).child(getMonthName).child(strDate).child(userId).setValue(post);
                loginprogressbar.setVisibility(View.GONE);

                amot.setText("");
                comt.setText("");
                Toast.makeText(getContext(),"Successfuly Updated",Toast.LENGTH_SHORT).show();







            }
        });






        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String Email = currentUser.getEmail();
            // email.setText(Email);
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //  FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser()== null) {
            Log.d(TAG, "User Not Logged In");

            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);



        }

    }







}
