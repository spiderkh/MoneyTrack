package com.bhaskar.moneytrack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecylcerTest extends AppCompatActivity {
    String dbUsedForFirebase;
    private static final String TAG = "RecyclerTest";
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Post> pList;
    ViewDetailsAdapter MyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylcer_test);
        dbUsedForFirebase = getResources().getString(R.string.firebaseDbUsed);

        recyclerView = findViewById(R.id.recylcerviewid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pList = new ArrayList<Post>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(dbUsedForFirebase).child("expenses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String mm = dataSnapshot1.getValue().toString();
                    Log.d(TAG, "Month = " + mm);
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {


                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            Post p = dataSnapshot3.getValue(Post.class);
                            Log.d(TAG, "Post = " + p.toString());

                            pList.add(p);

                        }


                    }


                }
                MyAdapter = new ViewDetailsAdapter(RecylcerTest.this, pList);
                recyclerView.setAdapter(MyAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
