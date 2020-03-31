package com.bhaskar.moneytrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class Frame_profile extends Fragment {
    String dbUsedForFirebase;
    TextView name, role, mail, groupcode, saveProfile;
    EditText phone, groupname, limit;
    ImageView editProfile, logout;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "Frame_profile";
    String Email1 = "";
    Profile pd;
    DatabaseReference mDatabase, mDatabase1;
    String profKey = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profileshow, container, false);
        mAuth = FirebaseAuth.getInstance();
        dbUsedForFirebase = this.getActivity().getResources().getString(R.string.firebaseDbUsed);
        name = v.findViewById(R.id.pname);
        role = v.findViewById(R.id.prole);
        mail = v.findViewById(R.id.pemail);
        phone = v.findViewById(R.id.pphone);
        groupcode = v.findViewById(R.id.pgroupcode);
        groupname = v.findViewById(R.id.pgroupname);
        currentUser = mAuth.getCurrentUser();
        limit = v.findViewById(R.id.plimit);
        editProfile = v.findViewById(R.id.editProfile);
        saveProfile = v.findViewById(R.id.saveProfile);
        logout = v.findViewById(R.id.logoutProfile);

        mDatabase = FirebaseDatabase.getInstance().getReference(dbUsedForFirebase);

        if (currentUser != null) {

            Email1 = currentUser.getEmail();
            // Toast.makeText(HomePage.this,"Email= "+Email1,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Log In Again", Toast.LENGTH_SHORT).show();
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile.setVisibility(View.VISIBLE);
                editProfile.setVisibility(View.GONE);
                if (pd.getRole().equalsIgnoreCase("Admin")) {
                    limit.setFocusable(true);
                    limit.setEnabled(true);
                    limit.setCursorVisible(true);
                    limit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    limit.setFocusableInTouchMode(true);
                    limit.setTextColor(getResources().getColor(R.color.alleditTextCOlor));
                    groupname.setFocusable(true);
                    groupname.setEnabled(true);
                    groupname.setCursorVisible(true);
                    groupname.setFocusableInTouchMode(true);
                    groupname.setTextColor(getResources().getColor(R.color.alleditTextCOlor));
                }

                phone.setFocusable(true);
                phone.setEnabled(true);
                phone.setCursorVisible(true);
                phone.setInputType(InputType.TYPE_CLASS_NUMBER);
                phone.setFocusableInTouchMode(true);
                phone.setTextColor(getResources().getColor(R.color.alleditTextCOlor));


            }
        });


        try {
            SharedPreferences mPrefs = this.getActivity().getSharedPreferences("profilePref", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("ProfileObject", "");
            pd = gson.fromJson(json, Profile.class);
            Log.d(TAG, "name= " + pd.toString());
            name.setText(pd.getName());
            role.setText(pd.getRole());
            mail.setText(pd.getEmail());
            phone.setText(pd.getMobile());
            groupcode.setText(pd.getGroupCode());
            groupname.setText(pd.getGroupName());
            limit.setText(pd.getLimit().toString());


        } catch (Exception e) {
            Log.d(TAG, "Error in Exception ProfileShow");
            Log.d(TAG, e.getMessage());
        }

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Double limitValue = Double.valueOf(limit.getText().toString());
                String grName = groupname.getText().toString();
                String phoneNo = phone.getText().toString();

                if (grName.isEmpty()) {
                    groupname.setError("GroupName Cannot be empty");
                    groupname.requestFocus();
                    return;
                }
                if (phoneNo.isEmpty()) {
                    phone.setError("Mobile Number Cannot be Empty");
                    phone.requestFocus();
                    return;
                }
                if (limitValue == 0.0) {
                    limit.setError("Limit Cannot be 0");
                    limit.requestFocus();
                    return;
                }
                if (phoneNo.length() != 10) {
                    phone.setError("Please Eneter Correct Mobile Number");
                    phone.requestFocus();
                    return;
                }

                saveProfile.setVisibility(View.GONE);
                editProfile.setVisibility(View.VISIBLE);

                if (pd.getRole().equalsIgnoreCase("Admin")) {
                    limit.setFocusable(false);
                    limit.setEnabled(false);
                    limit.setCursorVisible(false);
                    limit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    limit.setFocusableInTouchMode(false);
                    groupname.setFocusable(false);
                    groupname.setEnabled(false);
                    groupname.setCursorVisible(false);
                    groupname.setFocusableInTouchMode(false);
                    groupname.setTextColor(getResources().getColor(R.color.textColorGrayDark));
                    limit.setTextColor(getResources().getColor(R.color.textColorGrayDark));
                }

                phone.setFocusable(false);
                phone.setEnabled(false);
                phone.setCursorVisible(false);
                phone.setFocusableInTouchMode(false);
                phone.setTextColor(getResources().getColor(R.color.textColorGrayDark));


                Log.d(TAG, "gc= " + pd.getGroupCode());
                Log.d(TAG, "pd valu=" + pd.toString());
                pd.setLimit(limitValue);
                pd.setGroupName(grName);
                pd.setMobile(phoneNo);
                Log.d(TAG, "pd value= after set " + pd.toString());
                final ArrayList<String> prof5 = new ArrayList<>();
                //testCOde FirebaseDatabase.getInstance().getReference().child("test").child("users").child("profile").child(pd.getGroupCode()).child(pd.getUniqueKey()).child("mobile").setValue(pd.getMobile());
                FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("profile").child(pd.getGroupCode()).child(pd.getUniqueKey()).child("mobile").setValue(pd.getMobile());

                mDatabase.child("profile").child(pd.getGroupCode()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String ProfKey = dataSnapshot1.getKey();
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                Log.d(TAG, "key= " + dataSnapshot2.getKey());
                                Log.d(TAG, "Value= " + dataSnapshot2.getValue());
                                if (dataSnapshot2.getKey().equalsIgnoreCase("groupCode")) {
                                    Log.d(TAG, "key in= " + dataSnapshot2.getKey());
                                    Log.d(TAG, "Value= inside  " + dataSnapshot2.getValue());
                                    if (dataSnapshot2.getValue().equals(pd.getGroupCode())) {
                                        Log.d(TAG, "entered in groupcode set VAlue");
                                        //testCode DatabaseReference ds= FirebaseDatabase.getInstance().getReference().child("test").child("users").child("profile").child(pd.getGroupCode()).child(ProfKey);
                                        DatabaseReference ds = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("profile").child(pd.getGroupCode()).child(ProfKey);
                                        ds.child("limit").setValue(pd.getLimit());
                                        ds.child("groupName").setValue(pd.getGroupName());


                                        try {
                                            SharedPreferences mPrefs3 = getContext().getSharedPreferences("profilePref", Context.MODE_PRIVATE);
                                            Gson gson = new Gson();
                                            SharedPreferences.Editor prefsEditor = mPrefs3.edit();
                                            Log.d(TAG, "pd value= before adding in shared" + pd.toString());
                                            String json = gson.toJson(pd);
                                            prefsEditor.putString("ProfileObject", json);
                                            prefsEditor.commit();


                                        } catch (Exception e) {
                                            Log.d(TAG, "Error in Exception ProfileShow");
                                            Log.d(TAG, e.getMessage());
                                        }
                                    }
                                }
                            }
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Log.d(TAG, "plist size" + prof5.size());

               /* for (String aa:prof5)
                {
                    Log.d(TAG,"key= "+aa);

                    mDatabase1=FirebaseDatabase.getInstance().getReference("users").child("profile").child(pd.getGroupCode()).child(aa).child("limit");
                    mDatabase1.setValue(limitValue);
                }*/


            }
        });


        return v;
    }
}
