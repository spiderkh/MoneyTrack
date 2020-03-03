package com.bhaskar.moneytrack;

import android.support.annotation.NonNull;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetUserNameFromMail {
    //private String mailid;
    private String signupName;











    private DatabaseReference mDatabase;

    public GetUserNameFromMail(String signupNamee) {
        this.signupName = signupName;


    }



    public String getSignupName() {
        return signupName;
    }

    public void setSignupName(String signupName) {
        this.signupName = signupName;
    }



    public   String getUserName(String sName)
    {
        sName=sName.toUpperCase();
        return String.valueOf(sName.charAt(0));
    }

    
}
