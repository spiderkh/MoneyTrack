package com.bhaskar.moneytrack;

import com.google.firebase.database.DatabaseReference;

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


    public String getUserName(String sName) {
        sName = sName.toUpperCase();
        return String.valueOf(sName.charAt(0));
    }


}
