package com.bhaskar.moneytrack;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Frame_details extends Fragment {
    private static final String TAG = "Frame_details";
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Post> pList;
    ArrayList<String> monthList;
    ViewDetailsAdapter MyAdapter;
    Spinner spinner;
    String getListMonth,Email1;
    private FirebaseUser currentUser;
    String getMonthName,getYearName;
    Button monthBtn;
    Profile pd;
    String groupCode;
    TextView tas;
    Double sumAmount= 0.0;
    ProgressBar prog;
    CircleProgress cpb;
    private AdView mAdView;
    Double totalAmountDeposit=40000.0;
    private InterstitialAd interstitialAd;
    Context context;
    float[] yData =new float[6];  //={10.f,20.7f,12f,30f,25f,40f,21f};
    String[] xData={"Chicken","Veg","Dmart","Travel","Other","water"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG","Inside Frame Details");

        View v=inflater.inflate(R.layout.activity_view_details,container,false);



        TextView ts=v.findViewById(R.id.dett);
        MobileAds.initialize(getContext(),
                String.valueOf(R.string.TestAdmobAppId));
        mAdView = v.findViewById(R.id.adViewBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        recyclerView= v.findViewById(R.id.recylcerviewid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        spinner= v.findViewById(R.id.MonthList);
        tas=v.findViewById(R.id.TotalAmountShow);
       // prog=v.findViewById(R.id.AmountLeftProgressBar);
        cpb=v.findViewById(R.id.circle_progress);
        pList=new ArrayList<Post>();
        DateFormat monthDateFormat=new SimpleDateFormat("MMMM-yyyy");
        Date date = new Date();
        String monthYearString=monthDateFormat.format(date);
        String[] monthYearStringArray=monthYearString.split("-");
        getMonthName=monthYearStringArray[0];
        getYearName=monthYearStringArray[1];
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            Email1 = currentUser.getEmail();
            Log.d(TAG,"profile= "+Email1);
        }
        else {
            Toast.makeText(getContext(),"Loggin Failed",Toast.LENGTH_SHORT).show();
        }

        try {
            SharedPreferences mPrefs = this.getActivity().getSharedPreferences("profilePref",Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("ProfileObject", "xvlaue");
            if (!json.equalsIgnoreCase("xvlaue"))
            {
                pd = gson.fromJson(json, Profile.class);
                Log.d(TAG, "name= " + pd.getName());
                groupCode=pd.getGroupCode();
                totalAmountDeposit=pd.getLimit();
            }


        }catch (Exception e)
        {
            Log.d(TAG,"Error in Exception ProfileShow");
            Log.d(TAG,e.getMessage());
        }

        Log.d(TAG,"GroupCode= "+groupCode);
        monthBtn= v.findViewById(R.id.monthbtn);


        getViewDataByMonth(getMonthName);

        if(monthList==null){
            Log.d(TAG,"inside month List == null");
            monthList=MonthArrayList();
        }
        else {
            Log.d(TAG,"inside month List != null");
            Log.d(TAG,"inside month List != null size= "+monthList.size());

        }

        final ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,monthList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                getListMonth = spinner.getSelectedItem().toString();
                Log.d(TAG,"getListMonth= "+getListMonth);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(),"Nothing Selected",Toast.LENGTH_SHORT).show();
            }
        });


        cpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitialAd = new InterstitialAd(getContext());
                interstitialAd.setAdUnitId(getString(R.string.TestAdmobInter1)); //test id ending with 712

                interstitialAd.loadAd(new AdRequest.Builder().build());






                Dialog epicDialog=new Dialog(getContext());
                epicDialog.setContentView(R.layout.activity_pie_chart_class);
                PieChart pieChart=epicDialog.findViewById(R.id.piechartid);
                ImageView btnNo=epicDialog.findViewById(R.id.piechartExit);

                AdView   mAdView1 = epicDialog.findViewById(R.id.adViewBanner2);
                AdRequest adRequest1 = new AdRequest.Builder().build();
                mAdView1.loadAd(adRequest1);
                epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                epicDialog.show();
                pieChart.setHoleRadius(10f);
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(FragmentMain.this,"No",Toast.LENGTH_SHORT).show();
                        if (interstitialAd.isLoaded()) {
                            interstitialAd.show();
                            interstitialAd.setAdListener(new AdListener() {


                                                             @Override
                                                             public void onAdClosed() {
                                                                 epicDialog.dismiss();

                                                                 Log.d(TAG, "ads Closed");


                                                             }
                                                         }

                            );

                        } else {
                        }
                    }

                });

                addDataSet(pieChart);

            }
        });



        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Toast.makeText(ViewDetails.this,"Ffrom Btn"+getListMonth,Toast.LENGTH_SHORT).show();
                getViewDataByMonth(getListMonth);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);




            }
        });

        return v;
    }

    private void addDataSet(PieChart pieChart) {

        getXandYdata(pList);



        Log.d(TAG, "addDataSet: ");
        ArrayList<PieEntry> yEntry=new ArrayList<>();
        ArrayList<String> xEntry=new ArrayList<>();

        for(int i=0;i<yData.length;i++)
        {
            yEntry.add(new PieEntry(yData[i],xData[i]));
        }

        for (int i=0;i<xData.length;i++)
        {
            xEntry.add(xData[i]);
        }
        PieDataSet pieDataSet=new PieDataSet(yEntry,"");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.WHITE);



        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.rgb(63,81,181));
        colors.add(Color.rgb(255,87,34));
        colors.add(Color.rgb(139,195,74));
        colors.add(Color.rgb(0,150,136));
        colors.add(Color.rgb(233,30,99));
        colors.add(Color.rgb(96,125,139));
        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void getXandYdata(ArrayList<Post> pList) {

        Double veg,chicken,dmart,water,travel,other;
        veg=chicken=dmart=water=travel=other=0.0;
        for (Post p:pList)
        {
           /* CatList.add("Veg");
            CatList.add("Chicken");
            CatList.add("Dmart");
            CatList.add("Water");
            CatList.add("Rent");
            CatList.add("Massi");
            CatList.add("Mainten");
            CatList.add("Wifi");
            CatList.add("Gas");
            CatList.add("Travel");
            CatList.add("Other");*/

            if(p.getDropcategory().equalsIgnoreCase("Veg"))
            {
                veg+=p.getAmount();

            }
            else  if(p.getDropcategory().equalsIgnoreCase("Chicken"))
            {
                chicken+=p.getAmount();
            }
            else  if(p.getDropcategory().equalsIgnoreCase("Dmart"))
            {
                dmart+=p.getAmount();
            }
            else  if(p.getDropcategory().equalsIgnoreCase("Water"))
            {
                water+=p.getAmount();
            }
            else  if(p.getDropcategory().equalsIgnoreCase("Travel"))
            {
                travel+=p.getAmount();
            }
            else  if(p.getDropcategory().equalsIgnoreCase("Other"))
            {
                other+=p.getAmount();
            }
        }




        Float[] yData1={chicken.floatValue(),veg.floatValue(),dmart.floatValue(),travel.floatValue(),other.floatValue(),water.floatValue()};
        int i=0;
        for (Float val:yData1)
        {
            yData[i]=val;
            i++;

        }
       i=0;




    }


    public void getViewDataByMonth(final String currentmonthname) {

        if (!currentmonthname.equals("See By Month"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("expenses").child(groupCode).child(getYearName);
            Log.d(TAG,"Before PList clear");

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pList.clear();
                    sumAmount=0.0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String mm = dataSnapshot1.getKey();
                        Log.d(TAG, "Month = " + mm);


                        if (mm.equals(currentmonthname)) {

                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                Log.d(TAG, "Month_date = " + dataSnapshot2.getKey());

                                if (dataSnapshot2.getKey().equalsIgnoreCase("limit")) {
                                    Log.d(TAG,"Limit= "+dataSnapshot2.getValue());
                                }
                                else
                                    {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    Post p = dataSnapshot3.getValue(Post.class);
                                     Log.d(TAG, "Post = " + p.toString());
                                        pList.add(p);

                                }
                            }
                            }
                        }


                    }
                    if (pList.size() == 0) {
                        Toast.makeText(getContext(), "No Data For This Month", Toast.LENGTH_SHORT).show();
                    }

                      /* SharedPreferences   plistPref=getContext().getSharedPreferences("PlistPref",Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = plistPref.edit();
                        Gson gson1 = new Gson();
                        String json = gson1.toJson(pList);
                        prefsEditor.putString("PlistObject", json);
                        prefsEditor.commit();*/


                    for (Post posty:pList)
                    {
                        //Veg,Chicken,Dmart,Water,Gas,Travel,Other
                        if(posty.getDropcategory().equalsIgnoreCase("Veg")||posty.getDropcategory().equalsIgnoreCase("Chicken")||posty.getDropcategory().equalsIgnoreCase("Dmart")||posty.getDropcategory().equalsIgnoreCase("Water")||posty.getDropcategory().equalsIgnoreCase("Gas")||posty.getDropcategory().equalsIgnoreCase("Travel")||posty.getDropcategory().equalsIgnoreCase("Other"))
                        {

                            sumAmount+=posty.getAmount();
                        }
                    }

                    tas.setText(sumAmount.toString());
                    Log.d(TAG,"TotalAmountShow= "+totalAmountDeposit);
                    Double amountLeft=totalAmountDeposit-sumAmount;
                    int perCent= (int) ((sumAmount*100)/totalAmountDeposit);
                   // prog.setProgress(perCent);
                    cpb.setProgress(perCent);
                    Log.d(TAG,"getColor before= "+Integer.toHexString(cpb.getFinishedColor()).toUpperCase().substring(2));


                   //default green white
                    int bgColor =Color.rgb(000, 255, 000);
                    int txtColor =Color.rgb(255, 255, 255);
                    int unfinesdColor=Color.rgb(55,71,79);

                   if(perCent>=95)
                    {  Log.d(TAG, "percent Color above 95 ");
                    //red white
                        bgColor =Color.rgb(255, 000, 000);
                        txtColor =Color.rgb(255, 255, 255);
                    }
                    else if(perCent>=80 &&perCent<95){
                        //orange white
                        Log.d(TAG, "percent Color between 90 to 95 ");
                       bgColor =Color.rgb(255, 128, 000);
                       txtColor =Color.rgb(255, 255, 255);
                    }
                    else if(perCent>=65 &&perCent<80){
                        //yellow black
                        Log.d(TAG, "percent Color between 70 to 90 ");
                       bgColor =Color.rgb(255, 255, 000);
                       txtColor =Color.rgb(000, 000, 000);
                    }

                    cpb.setUnfinishedColor(unfinesdColor);
                    cpb.setFinishedColor(bgColor);
                    cpb.setTextColor(txtColor);
                /*prog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#80DAEB"),
                        android.graphics.PorterDuff.Mode.MULTIPLY);*/

                  //  LayerDrawable progressBarDrawable = (LayerDrawable) prog.getProgressDrawable();
                    // Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
                  //  Drawable progressDrawable = progressBarDrawable.getDrawable(1);
                   /* if(perCent<=90)
                    {
                        //backgroundDrawable.setColorFilter(ContextCompat.getColor(ViewDetails.this, R.color.startRed), PorterDuff.Mode.SRC_IN);
                        progressDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {
                        progressDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.startRed), PorterDuff.Mode.SRC_IN);
                    }*/



                     Log.d(TAG,"pList= bef"+pList.toString());
                    Collections.sort(pList, new Comparator<Post>() {
                        @Override
                        public int compare(Post p1, Post p2) {

                            int value1 = p2.getDate().compareTo(p1.getDate());
                            if (value1 == 0) {
                                return p2.getTime().compareTo(p1.getTime());

                            }
                            return value1;


                        }

                    });
                    Log.d(TAG,"pList= after"+pList.toString());
                    MyAdapter = new ViewDetailsAdapter(getContext(), pList);
                    recyclerView.setAdapter(MyAdapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(getActivity(),"Select Month",Toast.LENGTH_SHORT).show();
        }
    }





    public ArrayList<String> MonthArrayList()
    {
        Log.d(TAG,"inside month List ArrayLsit");
        final ArrayList<String> re = new ArrayList<String>();

        re.add(0,"See By Month");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child("expenses").child(groupCode).child(getYearName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(re!=null)
                {
                    re.clear();
                    re.add(0,"See By Month");

                }
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String mm= dataSnapshot1.getKey();
                    Log.d(TAG,"Month in arrayList = "+mm);
                    re.add(mm);

                }
                Collections.sort(re, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        SimpleDateFormat sdf=new SimpleDateFormat("MMMM");

                        try {
                            return sdf.parse(o1).compareTo(sdf.parse(o2));
                        } catch (ParseException e) {
                            return o1.compareTo(o2);
                        }


                    }
                });




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return re;
    }

}
