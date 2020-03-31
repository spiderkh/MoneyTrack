package com.bhaskar.moneytrack;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewDetailsAdapter extends RecyclerView.Adapter<ViewDetailsAdapter.MyViewHolder> {

    private static final String TAG = "ViewDetailsAdapter";
    Context context;
    String dbUsedForFirebase;
    ArrayList<Post> postList;
    private FirebaseUser currentUser;
    String Email1, UserRole;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    ProfileDetail pd;
    final String[] ukid = new String[1];
    final Double[] am = new Double[1];
    final String[] comment = new String[1];
    final String[] stat = new String[1];
    Double totalAmount = 0.0;

    int i = 0;


    public ViewDetailsAdapter(Context c, ArrayList<Post> p) {

        this.context = c;
        this.postList = p;
        Log.d(TAG, "postListSize p" + p.size());
        Log.d(TAG, "postListSize" + this.postList.size());


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbUsedForFirebase = context.getResources().getString(R.string.firebaseDbUsed);
        if (currentUser != null) {

            Email1 = currentUser.getEmail();
            // Toast.makeText(HomePage.this,"Email= "+Email1,Toast.LENGTH_SHORT).show();

            Log.d(TAG, "profile= " + Email1);
            pd = new ProfileDetail(Email1, dbUsedForFirebase);
            //  Profile p=getUserName1.getProfileDetail();
            // Log.d(TAG,"profile profile detail= "+p.toString());
        } else {
            Toast.makeText(context, "Loggin Failed", Toast.LENGTH_SHORT).show();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardviewtiles, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "OnBindViewHolder");
        Log.d(TAG, "postListSize onBind" + this.postList.size());
        holder.Sno.setText(Integer.toString(position + 1));
        holder.Datev.setText(postList.get(position).getDate());
        Log.d(TAG, "DateV= " + postList.get(position).getDate());
        holder.Catg.setText(postList.get(position).getDropcategory());
        Log.d(TAG, "Catg= " + postList.get(position).getDropcategory());
        holder.Amount.setText(postList.get(position).getAmount().toString());
        Log.d(TAG, "Amount= " + postList.get(position).getAmount().toString());
        holder.Comment.setText(postList.get(position).getComment());
        Log.d(TAG, "Comment= " + postList.get(position).getComment());
        holder.Users.setText(postList.get(position).getUsernName());
        Log.d(TAG, "Users= " + postList.get(position).getUsernName());
        holder.Status2.setText(postList.get(position).getStatus());
        if (postList.get(position).getUpdated().equals("Y")) {
            holder.Status2.setBackgroundResource(R.color.statusupdated);

        } else {

            if (postList.get(position).getStatus().equalsIgnoreCase("ND")) {
                holder.Status2.setBackgroundResource(R.color.statusnd);


            } else {
                holder.Status2.setBackgroundResource(R.color.statusd);
            }
        }


        Log.d(TAG, "Status= " + postList.get(position).getStatus());


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Sno, Datev, Catg, Amount, Comment, Users, Status2, updted;


        public MyViewHolder(View itemView) {
            super(itemView);

            Sno = itemView.findViewById(R.id.snoV);
            Datev = itemView.findViewById(R.id.dateV);
            Catg = itemView.findViewById(R.id.catgV);
            Amount = itemView.findViewById(R.id.amountV);
            Comment = itemView.findViewById(R.id.commentV);
            Users = itemView.findViewById(R.id.userV);
            Status2 = itemView.findViewById(R.id.statusV);
            updted = itemView.findViewById(R.id.updated);


            Status2.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    i = 0;
                    // String UserName="NU";
                    Profile p = pd.getAllDetails();
                    UserRole = p.getRole();
                    Log.d(TAG, "CLicked ");
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();


                    if (UserRole.equalsIgnoreCase("Admin")) {
                        // Toast.makeText(context,"CLicked By "+p.getName(),Toast.LENGTH_SHORT).show();

                        String sts = postList.get(getAdapterPosition()).getStatus();
                        Log.d(TAG, "status sts= " + sts);
                        final String uk = postList.get(getAdapterPosition()).getUniquekey();
                        String shortName = postList.get(getAdapterPosition()).getShortName();
                        Log.d(TAG, "uk 11 = " + uk);


                        //  Toast.makeText(context,sts,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"uniquekey= "+uk,Toast.LENGTH_SHORT).show();
                        if (sts.equalsIgnoreCase("D") || sts.equalsIgnoreCase("ND")) //ND should be there changed to D for testing
                        {
                            Log.d(TAG, "dddd= ");
                            final Profile pi = pd.getAllDetails();
                            //actual db
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode());
                            //test db  databaseReference= FirebaseDatabase.getInstance().getReference().child("test").child("users").child("expenses").child(pi.getGroupCode());
                            StringBuilder sb6 = new StringBuilder();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {
                                    Log.d(TAG, "GRoupCode= " + pi.getGroupCode());


                                    for (DataSnapshot dataSnapshot : dataSnapshot5.getChildren()) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                            String mm = dataSnapshot1.getKey();
                                            Log.d(TAG, "Month = " + mm);
                                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                String md = dataSnapshot2.getKey();
                                                Log.d(TAG, "Month_date = " + md);


                                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                    ukid[0] = dataSnapshot3.getKey();
                                                    Log.d(TAG, "ukid[0] 11 = " + ukid[0]);


                                                    if (ukid[0].equals(uk)) {

                                                        Log.d(TAG, "inside Found Uk");
                                                        Log.d(TAG, "inside Found uk 22=" + uk);
                                                        Log.d(TAG, "inside Found Ukid[0] 22=" + ukid[0]);

                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                            Log.d(TAG, "keyM= " + dataSnapshot4.getKey());

                                                            if (dataSnapshot4.getKey().equals("amount")) {
                                                                Long amnt = (Long) dataSnapshot4.getValue();
                                                                am[0] = Double.valueOf(amnt);
                                                                sb6.append(amnt.toString());
                                                                Log.d(TAG, "amou=" + am[0]);

                                                            }
                                                            if (dataSnapshot4.getKey().equals("comment")) {
                                                                comment[0] = dataSnapshot4.getValue(String.class);

                                                                Log.d(TAG, "cmt= " + dataSnapshot4.getValue());


                                                            }
                                                            if (dataSnapshot4.getKey().equals("status")) {
                                                                stat[0] = dataSnapshot4.getValue(String.class);
                                                                Log.d(TAG, "stat= " + dataSnapshot4.getValue());


                                                            }
                                                            Log.d(TAG, "before Timer");

                                                            if (stat[0] == null && comment[0] == null && am[0] == null) {

                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Log.d(TAG, "Inside Timer");


                                                                    }
                                                                }, 1000);

                                                            }

                                                            Log.d(TAG, "After Timer");

                                                            if (i == 0) {
                                                                i++;

                                                                //Password Dailog Box
                                                                Dialog epicDialog = new Dialog(context);
                                                                epicDialog.setContentView(R.layout.updatedelet);
                                                                Button btnUpdate = epicDialog.findViewById(R.id.updatebtnid);
                                                                Button viewBtn = epicDialog.findViewById(R.id.donebtnid);
                                                                Button btnDelet = epicDialog.findViewById(R.id.deletebtnid);
                                                                EditText passvlaue = epicDialog.findViewById(R.id.passChangeauth);
                                                                Button verifyBtn = epicDialog.findViewById(R.id.verifypassbtn);
                                                                ImageView okimg = epicDialog.findViewById(R.id.verifyok);
                                                                ImageView failedimg = epicDialog.findViewById(R.id.verifyfailed);
                                                                LinearLayout layout1 = epicDialog.findViewById(R.id.layoutofbtn);


                                                                ImageView btnNo = epicDialog.findViewById(R.id.noIdOnExitid);
                                                                epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                epicDialog.show();

                                                                verifyBtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        String passV = passvlaue.getText().toString();
                                                                        if (passV.isEmpty()) {
                                                                            passvlaue.setError("Password Required");
                                                                            passvlaue.requestFocus();
                                                                            return;
                                                                        } else {
                                                                            mAuth.signInWithEmailAndPassword(Email1, passvlaue.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        okimg.setVisibility(View.VISIBLE);
                                                                                        layout1.setVisibility(View.VISIBLE);
                                                                                        passvlaue.setText("");

                                                                                    } else {
                                                                                        okimg.setVisibility(View.GONE);
                                                                                        layout1.setVisibility(View.GONE);
                                                                                        failedimg.setVisibility(View.VISIBLE);
                                                                                        Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show();
                                                                                    }


                                                                                }
                                                                            });
                                                                        }


                                                                    }
                                                                });

                                                                //Finding Total Amount and copying in text view ----start-----
                                                                StringBuilder sb5 = new StringBuilder();
                                                                final String[] prefix = {""};


                                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm);
                                                                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                            Log.d(TAG, "dataSnapshot1 key = " + dataSnapshot1.getKey());

                                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {


                                                                                Post post = dataSnapshot2.getValue(Post.class);


                                                                                if (post.getShortName().equalsIgnoreCase(shortName) && post.getStatus().equalsIgnoreCase("ND")) {

                                                                                    totalAmount += post.getAmount();
                                                                                    sb5.append(prefix[0]);
                                                                                    prefix[0] = "+";
                                                                                    sb5.append(post.getAmount());

                                                                                    Log.d(TAG, "totalAmount==" + totalAmount);
                                                                                }


                                                                            }


                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                                //  ----find Amount end -------------

                                                                viewBtn.setOnClickListener(new View.OnClickListener() {
                                                                    String amountText = "";

                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Log.d(TAG, "sb= " + sb5.toString());
                                                                        Dialog dialog = new Dialog(context);
                                                                        dialog.setContentView(R.layout.done_layout);
                                                                        Button doneButton = dialog.findViewById(R.id.doneBtnid);
                                                                        Button doneAllButton = dialog.findViewById(R.id.doneallbtnid);
                                                                        final EditText editText = dialog.findViewById(R.id.totalAmountText);
                                                                        ImageView exitButtonImage = dialog.findViewById(R.id.noIdOnExitid1);
                                                                        Button copyButton1 = dialog.findViewById(R.id.copybtn);
                                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                        dialog.show();

                                                                        if (totalAmount != 0.0) {
                                                                            editText.setText(totalAmount + " = " + sb5.toString());
                                                                        }


                                                                        copyButton1.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                copyTextFunction(context, editText.getText());

                                                                            }
                                                                        });
                                                                        exitButtonImage.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                epicDialog.dismiss();
                                                                                dialog.dismiss();

                                                                            }
                                                                        });
                                                                        doneButton.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Log.d(TAG, "done ukid 33=" + ukid[0]);
                                                                                Log.d(TAG, "done uk 33=" + uk);

                                                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm).child(md).child(uk).child("status");
                                                                                databaseReference1.setValue("D");
                                                                                epicDialog.dismiss();
                                                                                dialog.dismiss();


                                                                            }
                                                                        });

                                                                        doneAllButton.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                                //shortName
                                                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm);
                                                                                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                            Log.d(TAG, "dataSnapshot1 key = " + dataSnapshot1.getKey());

                                                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                                Log.d(TAG, "dataSnapshot2 key= " + dataSnapshot2.getKey());
                                                                                                Post post = dataSnapshot2.getValue(Post.class);
                                                                                                Log.d(TAG, "post1= " + post.toString());
                                                                                                Log.d(TAG, "shortName= " + shortName);

                                                                                                if (post.getShortName().equalsIgnoreCase(shortName) && post.getStatus().equalsIgnoreCase("ND")) {
                                                                                                    Log.d(TAG, "monthDAte= " + dataSnapshot1.getKey());
                                                                                                    Log.d(TAG, "uniqkey= " + dataSnapshot2.getKey());


                                                                                                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm).child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("status");
                                                                                                    databaseReference3.setValue("D");
                                                                                                    epicDialog.dismiss();
                                                                                                    dialog.dismiss();

                                                                                                }


                                                                                            }

                                                                                        }


                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });


                                                                            }
                                                                        });


                                                                    }
                                                                });


                                                                btnDelet.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        //test db  DatabaseReference databaseReference22=FirebaseDatabase.getInstance().getReference().child("test").child("users").child("expenses").child(pi.getGroupCode()).child(mm).child(md);
                                                                        DatabaseReference databaseReference22 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm).child(md);
                                                                        Log.d(TAG, "del ukid=" + ukid[0]);

                                                                        databaseReference22.child(uk).setValue(null);
                                                                        epicDialog.dismiss();


                                                                    }
                                                                });

                                                                btnUpdate.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                        epicDialog.dismiss();
                                                                        if (stat[0] != null && comment[0] != null && am[0] != null) {

                                                                            Dialog epicDialog1 = new Dialog(context);
                                                                            epicDialog1.setContentView(R.layout.update_layout);
                                                                            EditText input1 = epicDialog1.findViewById(R.id.UpdateAmount);
                                                                            EditText input2 = epicDialog1.findViewById(R.id.UpdateComment);
                                                                            EditText input3 = epicDialog1.findViewById(R.id.UpdateStatus);
                                                                            ImageView btnNo = epicDialog1.findViewById(R.id.UpdateExitid);
                                                                            ImageView backBtno = epicDialog1.findViewById(R.id.idforbackfromupdate);
                                                                            Button UpdateBtn = epicDialog1.findViewById(R.id.Updatebtndailog);


                                                                            input1.setText(am[0].toString());
                                                                            input2.setText(comment[0]);
                                                                            input3.setText(stat[0]);

                                                                            epicDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                            epicDialog1.show();
                                                                            btnNo.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {
                                                                                    epicDialog1.dismiss();
                                                                                }
                                                                            });
                                                                            backBtno.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {

                                                                                    epicDialog.show();
                                                                                    epicDialog1.dismiss();
                                                                                }
                                                                            });
                                                                            UpdateBtn.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {

                                                                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                                                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses").child(pi.getGroupCode()).child("2020").child(mm).child(md).child(uk);
                                                                                    databaseReference1.child("status").setValue(input3.getText().toString());
                                                                                    databaseReference1.child("comment").setValue(input2.getText().toString());
                                                                                    databaseReference1.child("amount").setValue(Double.parseDouble(input1.getText().toString()));
                                                                                    databaseReference1.child("updated").setValue("Y");
                                                                                    epicDialog1.dismiss();

                                                                                }
                                                                            });


                                                                        }

                                                                    }
                                                                });
                                                                btnNo.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                                                                        epicDialog.dismiss();

                                                                    }
                                                                });

                                                            }

                                                        }


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


                        }


                    }


                }
            });

        }
    }

    private void copyTextFunction(Context context2, Editable text) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context2, "Text copied Successfully", Toast.LENGTH_SHORT).show();

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context2, "Text copied Successfully", Toast.LENGTH_SHORT).show();
        }


    }

    private void getAllViewDetailsForEdit(String ukid) {


    }


}
