package com.thehighlife.Activities.Activities;

import android.content.Context;
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
import android.widget.Toast;

import com.thehighlife.Activities.Adapters.PreferredCategoryAdapter;
import com.thehighlife.Activities.Model.AllCategoriesGridviewModel.AllCategoriesListReponse;
import com.thehighlife.Activities.Model.AllCategoriesGridviewModel.AllcategoriesMainResponse;
import com.thehighlife.Activities.Model.PreffredCategoryResponse.PreffredCategoryResponse;
import com.thehighlife.Activities.Utils.Constants;
import com.thehighlife.Activities.Utils.ExpandableHeightListView;
import com.thehighlife.Activities.Utils.SessionManager;
import com.thehighlife.Activities.Utils.UIUtil;
import com.thehighlife.Activities.WebServices.ApiHandler;
import com.thehighlife.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ActivityPreferredCategories extends AppCompatActivity {

    private ExpandableHeightListView listView_interest_areas;
    private Context context;
    private TextView txt_title, txt_save;
    private ImageView iv_back, iv_search;
    PreferredCategoryAdapter adapter;
    List<AllCategoriesListReponse> list_all_category= new ArrayList<>();
    EditText edt_search_categories;
    public static JSONArray jsonArray;
    JSONObject jsonObject;
    SessionManager session;
    ArrayList<String> getid = new ArrayList<>();
    CheckBox cb_selectall;

    LinearLayout ll_main,ll_select_all;


    public static ArrayList<Boolean> status = new ArrayList<>();
    public static List<AllCategoriesListReponse> list_all_cat_0000 = new ArrayList<>();
    RelativeLayout header;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preferred_categories);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;

        session = new SessionManager(ActivityPreferredCategories.this);
        idMapping();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list_all_category.clear();
        list_all_cat_0000.clear();
        status.clear();

        getAllCatgoriesApi();
        setListner();



    }

    ///////////id mapping////////////////

    private void idMapping() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Preferred Categories");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
        txt_save = (TextView) findViewById(R.id.txt_save);
        listView_interest_areas = (ExpandableHeightListView) findViewById(R.id.listView_interest_areas);
        listView_interest_areas.setExpanded(true);
        edt_search_categories = (EditText)findViewById(R.id.edt_search_categories);
        cb_selectall = (CheckBox)findViewById(R.id.cb_selectall);
        cb_selectall.setVisibility(View.VISIBLE);

        ll_main = (LinearLayout)findViewById(R.id.ll_main);

        ll_select_all = (LinearLayout)findViewById(R.id.ll_select_all);
        edt_search_categories.addTextChangedListener(passwordWatcher);

        header = (RelativeLayout)findViewById(R.id.header);
//        if(session.getUserId().equals("199")){
//            header.setBackgroundColor(getResources().getColor(R.color.view_color));
//        }
    }






    private void getAllCatgoriesApi() {

        if (!UIUtil.checkNetwork(ActivityPreferredCategories.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityPreferredCategories.this);
            return;
        }


        UIUtil.showDialog(ActivityPreferredCategories.this);


        ApiHandler.getApiService().getallavailablecategories(getPrefredAreaMap(), new Callback<AllcategoriesMainResponse>() {
            @Override
            public void success(AllcategoriesMainResponse allcategoriesMainResponse, Response response) {

                UIUtil.dismissDialog();
                if (allcategoriesMainResponse.getData() == null) {
                 //   UIUtil.showCustomDialog("Alert", allcategoriesMainResponse.getMessage(), ActivityPreferredCategories.this);
                    return;
                }

                if (allcategoriesMainResponse == null) {
                //    UIUtil.showCustomDialog("Alert", allcategoriesMainResponse.getMessage(), ActivityPreferredCategories.this);
                    return;
                }

                if (allcategoriesMainResponse.getStatus() == null) {
                   // UIUtil.showCustomDialog("Alert", allcategoriesMainResponse.getMessage(), ActivityPreferredCategories.this);
                    return;
                }

                if (allcategoriesMainResponse.getStatus().equals("FAIL")) {

                  //  UIUtil.showCustomDialog("Alert", "" + allcategoriesMainResponse.getMessage(), ActivityPreferredCategories.this);
                    return;
                }
                if (allcategoriesMainResponse.getStatus().equals("SUCCESS")) {

                    ll_main.setVisibility(View.VISIBLE);



                    list_all_category = allcategoriesMainResponse.getData();
                    // list_all_cities_0000 = allCityMainResponse.getData();

                    //   status=new Boolean[list_all_cities.size()];
                    list_all_cat_0000.clear();
                    for (int i = 0; i < list_all_category.size(); i++) {

                        if (list_all_category.get(i).getIsSelected().equalsIgnoreCase("true")){

                            // status[i]=true;

                            status.add(true);
                        }
                        else {

                            //  status[i]=false;

                            status.add(false);
                        }
                        list_all_cat_0000.add(list_all_category.get(i));
                    }

                   /* for(int i=0;i<list_all_cities.size();i++) {
                        list_all_cities_0000.add(list_all_cities.get(i));



                    }*/






                    adapter = new PreferredCategoryAdapter(ActivityPreferredCategories.this, list_all_category);
                    listView_interest_areas.setAdapter(adapter);



                }

            }

            @Override
            public void failure(RetrofitError error) {

                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //  UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityPreferredCategories.this);

            }
        });


    }
    private void check() {

        if(edt_search_categories.getText().length()==0){
            cb_selectall.setEnabled(true);
        }
    }





    /////////////on click////////////

    private void setListner() {

        cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(cb_selectall.isChecked()){

                    PreferredCategoryAdapter.list_CatId.clear();

                    for (int i=0;i<status.size();i++)
                    {

                        //  status[i]=true;
                        ActivityPreferredCategories.list_all_cat_0000.get(i).setIsSelected("true");
                        list_all_category.get(i).setIsSelected("true");
                        status.set(i , true);

                    }

                    adapter.notifyDataSetChanged();

                    //  Toast.makeText(ActivityPreferredCity.this,"all",Toast.LENGTH_SHORT).show();
                }

                if(!cb_selectall.isChecked()){

                    PreferredCategoryAdapter.list_CatId.clear();

                    for (int i=0;i<status.size();i++)
                    {

                        //status[i]=false;
                        ActivityPreferredCategories.list_all_cat_0000.get(i).setIsSelected("false");
                        list_all_category.get(i).setIsSelected("false");
                        status.set(i , false);

                    }
                    adapter.notifyDataSetChanged();
                    //  Toast.makeText(ActivityPreferredCity.this,"all",Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        edt_search_categories.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                cb_selectall.setEnabled(false);
                String text = edt_search_categories.getText().toString().toLowerCase();
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




        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager sessionManager = new SessionManager(ActivityPreferredCategories.this);
                String str_UserId = sessionManager.getUserId();

                jsonArray = new JSONArray();

                getid.clear();


                for(int i = 0; i<list_all_category.size(); i++){

                    if (list_all_category.get(i).getIsSelected().equalsIgnoreCase("true"))
                    {
                        getid.add(String.valueOf(list_all_category.get(i).getId()));

                    }
                    else {
                        Log.e("XXXXXXX" , "" + list_all_category.get(i).getId());
                    }

                }

                callPreffredCategory();
            }
        });

    }

    private void callPreffredCategory() {


        if (!UIUtil.checkNetwork(ActivityPreferredCategories.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityPreferredCategories.this);
            return;
        }

        UIUtil.showDialog(ActivityPreferredCategories.this);

        ApiHandler.getApiService().addPreffredCategory(getsavecatymap(), new Callback<PreffredCategoryResponse>() {
            @Override
            public void success(PreffredCategoryResponse preffredCategoryResponse, Response response) {
                UIUtil.dismissDialog();

                if (preffredCategoryResponse == null) {
                    UIUtil.dismissDialog();
                 //   UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityPreferredCategories.this);
                    return;
                }

                if (preffredCategoryResponse.getStatus() == null) {
                    UIUtil.dismissDialog();
               //     UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityPreferredCategories.this);
                    return;
                }

                if (preffredCategoryResponse.getStatus().equals("FAIL")) {

                    UIUtil.dismissDialog();
                //    UIUtil.showCustomDialog("Alert", preffredCategoryResponse.getMessage(), ActivityPreferredCategories.this);
                    return;
                }
                if (preffredCategoryResponse.getStatus().equals("SUCCESS")) {

//                    PreferredCategoryAdapter.list_CatId.clear();
//
//                    UIUtil.dismissDialog();
//                    UIUtil.showCustomDialog("Success", "Your preferred categories have been added successfully.", ActivityPreferredCategories.this);
//                    adapter.notifyDataSetChanged();


                    Intent i = new Intent(ActivityPreferredCategories.this, MainActivity.class);
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
                //    UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityPreferredCategories.this);

            }
        });

    }



    private Map<String, String> getPrefredAreaMap() {

        SessionManager sessionManager = new SessionManager(ActivityPreferredCategories.this);
        String str_UserId = sessionManager.getUserId();
        Map<String, String> map = new HashMap<>();
        map.put("userId", str_UserId);

        return map;
    }


    private Map<String, String> getsavecatymap() {


        //       String list_comma = UIUtil.getCommaSapratedStringFromList(PreferredCategoryAdapter.list_CatId);

        // text = textList.toString().replace("[", "").replace("]", "");

        String CATID = String.valueOf(getid);

        if (CATID.length()==0){
            Toast.makeText(ActivityPreferredCategories.this,"toast",Toast.LENGTH_SHORT).show();

        }

        Map<String, String> map = new HashMap<>();
        map.put("userid",""+session.getUserId());
        map.put("categoryId",CATID.toString().replace("[", "").replace("]", ""));
        Log.e("map", "" + map);


        return map;
    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(edt_search_categories.getText().length()==0){
                cb_selectall.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
            if(edt_search_categories.getText().length()==0){
                cb_selectall.setEnabled(true);
            }
        }
    };


}