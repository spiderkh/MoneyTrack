package com.bhaskar.moneytrack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileDetail {
    Context context;

    private String dbUsedForFirebase;
    private static final String TAG = "ProfileDetail";
    private String mailId;
    private Profile profile;
    DatabaseReference mDatabase;
    Profile pp = null;


    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }


    public ProfileDetail(final String mailId, String dbUsedForFirebase) {
        Log.d(TAG, "profile inside ProfileDetail mail= " + mailId);
        Log.d(TAG, "dbUsedForFirebase= " + dbUsedForFirebase);
        this.mailId = mailId;
        this.dbUsedForFirebase = dbUsedForFirebase;
        mDatabase = FirebaseDatabase.getInstance().getReference(dbUsedForFirebase).child("profile");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "profile inside 1st datasnapshot");
                    for (DataSnapshot dataSnapshot3 : dataSnapshot1.getChildren()) {
                        Log.d(TAG, "gg==" + dataSnapshot3.getKey());
                        for (DataSnapshot dataSnapshot2 : dataSnapshot3.getChildren()) {
                            if (mailId.equals(dataSnapshot2.getValue())) {
                                profile = dataSnapshot3.getValue(Profile.class);
                                Log.d(TAG, "profile inside 2nd datasnapshot");
                                Log.d(TAG, "ppp in profdet= " + profile.toString());
                                // p[0] =pp;
                            }


                        }
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public Profile getAllDetails() {


        Log.d(TAG, "profile inside getALLDEtails");

        Log.d(TAG, "profile inside  getALlDetails pp=" + profile.toString());


        return profile;
    }

}
