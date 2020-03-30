package com.bhaskar.moneytrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




public class MonthLimit {

    private int limit;
    private  Profile p;
    Context context;
    SharedPreferences   pref;
    DateFormat monthDateFormat=new SimpleDateFormat("MMMM");
    Date date = new Date();
    final String currentMonthName= monthDateFormat.format(date);
    String dbUsedForFirebase;






    DatabaseReference databaseReference;
    private static final String TAG = "MonthLimit";
    double sumAmount=0.0;

    public MonthLimit(final Context context) {
      this.context=context;
      this.dbUsedForFirebase=context.getResources().getString(R.string.firebaseDbUsed);


    }

    public int getLimit() {

        return limit;
    }

    public void setLimit(final int limit) {


        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("profile");
         databaseReference1.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                 {
                     String key=dataSnapshot1.getKey();
                     Log.d(TAG,"key = "+key);
                     Log.d(TAG,"dbUsedForFirebase= "+dbUsedForFirebase);



                     DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("profile").child(key).child("limit");
                     databaseReference2.setValue(limit);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });




    }
}
