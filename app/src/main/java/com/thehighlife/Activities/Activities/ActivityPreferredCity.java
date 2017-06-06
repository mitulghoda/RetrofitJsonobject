package com.thehighlife.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thehighlife.Activities.Adapters.PreferredCitiesAdapter;
import com.thehighlife.Activities.Model.AllCityModel.AllCityMainResponse;
import com.thehighlife.Activities.Model.AllCityModel.AllcityListResponse;
import com.thehighlife.Activities.Model.PreffredCityResponse.PreffredCityResponse;
import com.thehighlife.Activities.Utils.Constants;
import com.thehighlife.Activities.Utils.ExpandableHeightListView;
import com.thehighlife.Activities.Utils.SessionManager;
import com.thehighlife.Activities.Utils.UIUtil;
import com.thehighlife.Activities.WebServices.ApiHandler;
import com.thehighlife.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ActivityPreferredCity extends AppCompatActivity {
    private TextView txt_title,txt_save;
    private ImageView iv_back,iv_search;
    private ExpandableHeightListView listView_cities;
    PreferredCitiesAdapter adapter;
    List<AllcityListResponse> list_all_cities = new ArrayList<>();
    public static List<AllcityListResponse> list_all_cities_0000 = new ArrayList<>();
    //Boolean[] status;
    List<String> list_loc_string = new ArrayList<>();


    SessionManager session;
    EditText edt_search_city;
    CheckBox cb_selectall;
    ArrayList<String> getid = new ArrayList<>();
    LinearLayout ll_main;

    public static ArrayList<Boolean> status = new ArrayList<>();
    RelativeLayout header;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preferred_city);
        session = new SessionManager(ActivityPreferredCity.this);
        idMapping();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list_all_cities.clear();
        list_all_cities_0000.clear();
        status.clear();

        getAllCitiesApi();
        setListner();

    }


    ///////////id mapping////////////////

    private void idMapping() {

        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_title.setText("Preferred Cities");
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_search= (ImageView)findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
        txt_save = (TextView)findViewById(R.id.txt_save);
        listView_cities = (ExpandableHeightListView)findViewById(R.id.listView_interest_cities);
        listView_cities.setExpanded(true);
        edt_search_city= (EditText)findViewById(R.id.edt_search_city);
        cb_selectall = (CheckBox)findViewById(R.id.cb_selectall);

        ll_main = (LinearLayout)findViewById(R.id.ll_main);

        edt_search_city.addTextChangedListener(passwordWatcher);
        header = (RelativeLayout)findViewById(R.id.header);
//        if(session.getUserId().equals("199")){
//            header.setBackgroundColor(getResources().getColor(R.color.view_color));
//        }


    }

    private void getAllCitiesApi() {

        if (!UIUtil.checkNetwork(ActivityPreferredCity.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityPreferredCity.this);
            return;
        }

        UIUtil.showDialog(ActivityPreferredCity.this);
        ApiHandler.getApiService().getallcities(getallcitieslistviewMap(), new Callback<AllCityMainResponse>() {

            @Override
            public void success(AllCityMainResponse allCityMainResponse, Response response) {
                UIUtil.dismissDialog();

                if (allCityMainResponse == null) {
                 //   UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityPreferredCity.this);
                    return;
                }

                if (allCityMainResponse.getData() == null) {
             //       UIUtil.showCustomDialog("Alert", allCityMainResponse.getMessage(), ActivityPreferredCity.this);
                    return;
                }

                if (allCityMainResponse.getStatus() == null) {
                  //  UIUtil.showCustomDialog("Alert", allCityMainResponse.getMessage(), ActivityPreferredCity.this);
                    return;
                }

                if (allCityMainResponse.getStatus().equals("FAIL")) {

                 //   UIUtil.showCustomDialog("Alert", "" + allCityMainResponse.getMessage(), ActivityPreferredCity.this);
                    return;
                }
                if (allCityMainResponse.getStatus().equals("SUCCESS")) {

                    ll_main.setVisibility(View.VISIBLE);

                    list_all_cities = allCityMainResponse.getData();
                    // list_all_cities_0000 = allCityMainResponse.getData();

                    //   status=new Boolean[list_all_cities.size()];

                    for(int i =0; i<list_all_cities.size();i++){
                        list_loc_string.add(list_all_cities.get(i).getCityName());

                    }


                    Collections.sort(list_loc_string, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareToIgnoreCase(s2);
                        }
                    });


                    Collections.sort(list_all_cities, new Comparator<AllcityListResponse>() {
                        @Override
                        public int compare(AllcityListResponse s1, AllcityListResponse s2) {
                            return s1.getCityName().compareToIgnoreCase(s2.getCityName());
                        }
                    });


                    list_all_cities_0000.clear();

                    for (int i = 0; i < list_all_cities.size(); i++) {

                        if (list_all_cities.get(i).getIsSelected().equalsIgnoreCase("true")){

                            // status[i]=true;

                            status.add(true);
                        }
                        else {

                            //  status[i]=false;

                            status.add(false);
                        }
                        list_all_cities_0000.add(list_all_cities.get(i));
                    }

                   /* for(int i=0;i<list_all_cities.size();i++) {
                        list_all_cities_0000.add(list_all_cities.get(i));
                    }*/




                    adapter = new PreferredCitiesAdapter(ActivityPreferredCity.this, list_all_cities);
                    listView_cities.setAdapter(adapter);

                }

            }


            @Override
            public void failure(RetrofitError error) {
                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //  UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityPreferredCity.this);
            }


        });





    }

    /////////////on click////////////

    private void setListner() {
        Log.e("Size", ""+ActivityPreferredCity.list_all_cities_0000.size());

        Log.e("status",""+status.size());
        cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(cb_selectall.isChecked()){

                    Log.e("chekedstatus",""+status.size());

                    PreferredCitiesAdapter.list_CityId.clear();


                    for (int i=0;i<status.size();i++)
                    {

                        //  status[i]=true;
                        ActivityPreferredCity.list_all_cities_0000.get(i).setIsSelected("true");
                        list_all_cities.get(i).setIsSelected("true");
                        status.set(i , true);

                    }

                    adapter.notifyDataSetChanged();

                    //  Toast.makeText(ActivityPreferredCity.this,"all",Toast.LENGTH_SHORT).show();
                }

                if(!cb_selectall.isChecked()){

                    Log.e("unchekedstatus",""+status.size());

                    PreferredCitiesAdapter.list_CityId.clear();

                    for (int i=0;i<status.size();i++)
                    {

                        //status[i]=false;
                        ActivityPreferredCity.list_all_cities_0000.get(i).setIsSelected("false");
                        list_all_cities.get(i).setIsSelected("false");
                        status.set(i , false);

                    }
                    adapter.notifyDataSetChanged();
                    //  Toast.makeText(ActivityPreferredCity.this,"all",Toast.LENGTH_SHORT).show();
                }
            }
        });




        edt_search_city.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                cb_selectall.setEnabled(false);
                String text = edt_search_city.getText().toString().toLowerCase();
                adapter.filter(text);
                check();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }


        });






        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager sessionManager = new SessionManager(ActivityPreferredCity.this);
                String str_UserId = sessionManager.getUserId();


//                if (PreferredCitiesAdapter.list_CityId != null && !PreferredCitiesAdapter.list_CityId.isEmpty()) {
//
//
//
//
//                }else {
//                    Toast.makeText(ActivityPreferredCity.this, "Please select city", Toast.LENGTH_SHORT).show();
//                }


                getid.clear();

                for(int i = 0; i<list_all_cities.size(); i++){

                    if (list_all_cities.get(i).getIsSelected().equalsIgnoreCase("true"))
                    {
                        getid.add(String.valueOf(list_all_cities.get(i).getCityid()));

                    }
                    else {
                        Log.e("XXXXXXX" , "" + list_all_cities.get(i).getCityid());
                    }

                }

                callPreffredCity();

            }
        });

    }

    private void check() {

        if(edt_search_city.getText().length()==0){
            cb_selectall.setEnabled(true);
        }
    }

    private void callPreffredCity() {


        if (!UIUtil.checkNetwork(ActivityPreferredCity.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityPreferredCity.this);
            return;
        }

        UIUtil.showDialog(ActivityPreferredCity.this);

        ApiHandler.getApiService().addPreffredCity(getsavecityymap(), new Callback<PreffredCityResponse>() {
            @Override
            public void success(PreffredCityResponse preffredCityResponse, Response response) {
                UIUtil.dismissDialog();

                if (preffredCityResponse == null) {
                    UIUtil.dismissDialog();
                //    UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityPreferredCity.this);
                    return;
                }

                if (preffredCityResponse.getStatus() == null) {
                    UIUtil.dismissDialog();
                //    UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityPreferredCity.this);
                    return;
                }

                if (preffredCityResponse.getStatus().equals("FAIL")) {

                    UIUtil.dismissDialog();
              //      UIUtil.showCustomDialog("Alert", preffredCityResponse.getMessage(), ActivityPreferredCity.this);
                    return;
                }
                if (preffredCityResponse.getStatus().equals("SUCCESS")) {

//                    PreferredCitiesAdapter.list_CityId.clear();
//                    UIUtil.dismissDialog();
//                    UIUtil.showCustomDialog("Success", "Your preferred cities have been added successfully.", ActivityPreferredCity.this);
//                    adapter.notifyDataSetChanged();

                    Intent i = new Intent(ActivityPreferredCity.this, MainActivity.class);
                    i.putExtra(Constants.DETAILACTIVITY, Constants.HOME);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);

                }
            }

            @Override
            public void failure(RetrofitError error) {

                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());


                //     UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityPreferredCity.this);

            }
        });

    }

    private Map<String, String> getsavecityymap() {

        // String list_comma = UIUtil.getCommaSapratedStringFromList(PreferredCitiesAdapter.list_CityId);

        String CATID = String.valueOf(getid);
        Map<String, String> map = new HashMap<>();
        map.put("userid",""+session.getUserId());
        map.put("city",CATID.toString().replace("[", "").replace("]", ""));
        Log.e("city",""+map);


        return map;
    }


    private Map<String, String> getallcitieslistviewMap() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + session.getUserId());

        Log.e("getfavoriteslistviewMap", "" + map);

        return map;
    }


    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(edt_search_city.getText().length()==0){
                cb_selectall.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
         if(edt_search_city.getText().length()==0){
             cb_selectall.setEnabled(true);
         }
        }
    };

}
