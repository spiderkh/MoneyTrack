package com.bhaskar.moneytrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText email,pass,name,mobile,groupcode,groupname;
    TextView verifyGroupCode;
    Button gotologin;
    double limitinprof=0.0;
    Button signupbtn;
    String signup_email,singup_pass,signup_name,signup_mobile,signup_groupcode,signup_groupname,signup_role;
    ProgressBar signupprogressbar;
    Spinner spinner;
    ArrayList<Profile> profileList;
    int GroupcodeVerified=0;
    int codeValidity=0;

    int uiu=0;
    boolean groupNamereturn;
    private DatabaseReference mDatabase,databaseReference;

    private static final String TAG = "SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        email= findViewById(R.id.SignUpEmail);
        pass= findViewById(R.id.SignUpPass);
        name= findViewById(R.id.SignUpName);
        mobile= findViewById(R.id.SignUpMobile);
        groupcode= findViewById(R.id.SignUpGroupCode);
        groupname= findViewById(R.id.SignUpGroupName);
        signupbtn= findViewById(R.id.SignUpBtn);
        gotologin= findViewById(R.id.GoToLoginPage);
        spinner= findViewById(R.id.RoleList);
        verifyGroupCode=findViewById(R.id.verifyGroupCode);
        TextView iv=(TextView)findViewById(R.id.signupappname);


        Typeface localTypeface2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        iv.setTypeface(localTypeface2);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        signupprogressbar= findViewById(R.id.SignUpProgressBar);
        findViewById(R.id.SignUpBtn).setOnClickListener(this);
        findViewById(R.id.GoToLoginPage).setOnClickListener(this);


        ArrayList<String> RoleList=new ArrayList<>();
                RoleList.add("Select Your Role!");
        RoleList.add("Admin");
        RoleList.add("User");
        final ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinnerstyle,RoleList);
        dataAdapter.setDropDownViewResource(R.layout.myspinnerstyle);
        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                signup_role=spinner.getSelectedItem().toString();
                if(spinner.getSelectedItem().toString().equals("Admin"))
                {
                    GroupcodeVerified=3;
                    groupname.setText("");
                    groupname.setVisibility(View.VISIBLE);
                    GroupCodeGenerator();

                     Log.d(TAG,"Limit ii= "+limitinprof);

                    groupcode.setVisibility(View.GONE);
                    verifyGroupCode.setVisibility(View.GONE);

                }
                else if(spinner.getSelectedItem().toString().equals("User"))
                {
                    groupcode.setText("");
                    groupcode.setVisibility(View.VISIBLE);
                    GroupcodeVerified=0;
                    verifyGroupCode.setVisibility(View.VISIBLE);
                    groupname.setVisibility(View.GONE);
                    uiu=1;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SignUp.this,"Nothing Selected",Toast.LENGTH_SHORT).show();
            }
        });

        verifyGroupCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupcodeVerified=1;
                Log.d(TAG,"Verify Clicked");

                signup_groupcode=groupcode.getText().toString();
                if (signup_groupcode.isEmpty())
                {
                    groupcode.setError("Enter Group Code");
                    groupcode.requestFocus();
                    return;
                }

                Log.d(TAG,"entered Code= "+signup_groupcode);
                Log.d(TAG,"groupCOdeChecker= "+GroupCodeChecker(signup_groupcode,"groupNameFinder"));


            }
        });

    }


    private void RegisterUser()
    {
        signup_email=email.getText().toString();
        singup_pass=pass.getText().toString();
        signup_name=name.getText().toString();
        signup_mobile=mobile.getText().toString();

        signup_groupcode=groupcode.getText().toString();
  Log.d(TAG,"Gropverifie="+GroupcodeVerified);

        if(uiu==0)
        {
            signup_groupname=groupname.getText().toString();
        }
        else if(uiu==1)
        {
            if (GroupcodeVerified==0 ||GroupcodeVerified==2 )
            {
                verifyGroupCode.setError("VerifyGroupCodeFirst");
                verifyGroupCode.requestFocus();
                return;
            }
            else {
                signup_groupname=groupname.getText().toString();
            }
            Log.d(TAG,"GroupName code= "+signup_groupcode);

        }

        Log.d(TAG, "Email= "+signup_email);
        Log.d(TAG, "Pass= "+singup_pass);

        if (signup_email.isEmpty())
        {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if (signup_groupname.isEmpty())
        {
            groupname.setError("GroupName is Required");
            groupname.requestFocus();
            return;
        }
        if (singup_pass.isEmpty())
        {
            pass.setError("Password Is Requred");
            pass.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signup_email).matches())
        {
            email.setError("Enter Valid Email");
            email.requestFocus();
            return;

        }
        if(singup_pass.length()<6)
        {
           pass.setError("Minimum lenght of password should be 6");
           pass.requestFocus();
           return;
        }
        if(signup_mobile.length()!=10)
        {
            mobile.setError("Please Eneter Correct Mobile Number");
            mobile.requestFocus();
            return;
        }


        signupprogressbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(signup_email, singup_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    signupprogressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        String userId = mDatabase.push().getKey();

                        //String uniqueKey, String name, String email, String role, String mobile, String groupCode, String groupName
                        // Toast.makeText(SignUp.this,"inside Register= "+signup_groupname,Toast.LENGTH_SHORT).show();

                        GetUserNameFromMail getUserNameFromMail = new GetUserNameFromMail(signup_name);

                        String nickName = getUserNameFromMail.getUserName(signup_name);
                        if (signup_groupname == null)
                            signup_groupname = "noGroupName";

                        SharedPreferences pref = getSharedPreferences("profilePref", Context.MODE_PRIVATE);
                        String json11 = pref.getString("ProfileObject", "noValue");
                        if (json11.equalsIgnoreCase("noValue")) {
                            Log.d(TAG, "SharedPreference not Exist");

                        } else {
                            Log.d(TAG, "SharedPreference Exist");
                            SharedPreferences.Editor prefsEditor = pref.edit();
                            prefsEditor.remove("ProfileObject");
                            prefsEditor.commit();

                        }

                        Log.d(TAG,"limit"+limitinprof);
                            Profile profile = new Profile(userId, signup_name, signup_email, signup_role, signup_mobile, signup_groupcode, signup_groupname,nickName, limitinprof);
                         Log.d(TAG,"pp= "+profile.toString());
                            mDatabase.child("profile").child(signup_groupcode).child(userId).setValue(profile);
                            Intent intent = new Intent(SignUp.this, FragmentMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);



                    } else {

                        Log.d(TAG, "ErrorReason= " + task.getException().getMessage());
                        if (task.getException().getMessage().equalsIgnoreCase("The email address is already in use by another account.")) {
                            Toast.makeText(getApplicationContext(), "Email Already Registered", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });


    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.SignUpBtn)
        {
            RegisterUser();
        }

        if(view.getId()==R.id.GoToLoginPage)
        {
            Intent intent=new Intent(SignUp.this,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void GroupCodeGenerator()
    {

        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        groupcode.setText(String.valueOf(n));
        if(!GroupCodeChecker(String.valueOf(n),"groupCodeGenerator"))
        {
            GroupCodeGenerator();
        }
    }

    public boolean GroupCodeChecker(final String gc, final String wherecall)
    {

        final boolean[] rr = {true};


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("profile");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uu="";


                outerLoop:
                for(DataSnapshot dataSnapshot3:dataSnapshot.getChildren())
                {


                    Log.d(TAG,"first Child= "+dataSnapshot3.getKey());

                    if (gc.equals(dataSnapshot3.getKey())) {
                        codeValidity=1;
                        if(wherecall.equalsIgnoreCase("groupNameFinder"))
                        {


                            for (DataSnapshot dataSnapshot1 : dataSnapshot3.getChildren()) {

                                Profile profile = dataSnapshot1.getValue(Profile.class);
                                Log.d(TAG, "Prof =" + profile.toString());
                                    groupname.setText(profile.getGroupName());
                                    limitinprof = profile.getLimit();
                                    Log.d(TAG, "gName inside= " + groupname.getText().toString());
                                    break outerLoop;



                            }
                        }
                        else if (wherecall.equalsIgnoreCase("groupCodeGenerator")) {
                                Log.d(TAG, "GroupCodeGenerator");
                                rr[0] = false;
                                limitinprof = 15000.0;

                                break outerLoop;
                            }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
int kk=0; // for toast display once  only after success
        if(codeValidity==0)
        {
            Log.d(TAG, " code VAlidity=0 GroupcodeVerified =" + GroupcodeVerified);
            if(GroupcodeVerified!=3)
             {
                 Toast.makeText(SignUp.this,"Invalid Code Try Again",Toast.LENGTH_SHORT).show();
                 verifyGroupCode.setError("VerifyGroupCodeFirst");
                 verifyGroupCode.requestFocus();
                 GroupcodeVerified=2;
             }


        }
        else {
            if(kk==0) {
                Toast.makeText(SignUp.this, "Code Verified Successfully", Toast.LENGTH_SHORT).show();
                groupcode.setFocusable(false);
                groupcode.setEnabled(false);
                groupcode.setCursorVisible(false);
                groupcode.setFocusableInTouchMode(false);

                codeValidity = 0;
                Log.d(TAG, "code Valid after toast success= " + codeValidity);
                kk++;
            }
        }

        Log.d(TAG,"return GroupCodeChecker= "+rr[0]);
        return rr[0];
    }

    public boolean GroupNameFinder(final String gc)
    {
        final boolean[] ee = {false};
        final String[] gn = {""};


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("profile").child(gc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            boolean rr=true;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String uk=dataSnapshot1.getKey();
                    Log.d(TAG,"GroupName uk="+uk);

                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                    {

                        if(gc==dataSnapshot2.getValue())
                        {
                            Log.d(TAG,"GroupName uk matched="+uk);
                            DatabaseReference databaseReference33=databaseReference.child(uk).child("groupName");
                            databaseReference33.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    gn[0] = dataSnapshot.getValue(String.class);
                                    Log.d(TAG,"GroupName= "+gn[0]);
                                    signup_groupname=gn[0];
                                    ee[0] =true;




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                      if(!rr)
                      break;
                    }
                   if(!rr)
                   break;

                }
                if(rr)
                {
                    Toast.makeText(SignUp.this,"Invalid GroupCode Please Try Again",Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


return ee[0];
    }

}
