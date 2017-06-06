package com.thehighlife.Activities.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thehighlife.Activities.GcmNotifications.GCMClientManager;
import com.thehighlife.Activities.Model.RegisterUser.Register;
import com.thehighlife.Activities.Utils.Constants;
import com.thehighlife.Activities.Utils.SessionManager;
import com.thehighlife.Activities.Utils.SoftKeyboardLsnedRelativeLayout;
import com.thehighlife.Activities.Utils.UIUtil;
import com.thehighlife.Activities.WebServices.ApiHandler;
import com.thehighlife.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityRegister extends AppCompatActivity {
    private TextView txt_SignIn,txt_Submit,txt_tandc;
    private EditText edt_user_name,edt_user_email,edt_user_mob_no,edt_user_dob,edt_user_password,edt_client_password;
    private CheckBox cb_agree_terms;
    SessionManager session;

    String PROJECT_NUMBER= Constants.PROJECT_NUMBER;
    GCMClientManager pushClientManager;
    String dev_id;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        session = new SessionManager(ActivityRegister.this);
        pushClientManager = new GCMClientManager(ActivityRegister.this, PROJECT_NUMBER);
        idMapping();
        setListner();
        getGCMID();

        SoftKeyboardLsnedRelativeLayout layout = (SoftKeyboardLsnedRelativeLayout) findViewById(R.id.rel_mail);
        layout.addSoftKeyboardLsner(new SoftKeyboardLsnedRelativeLayout.SoftKeyboardLsner() {
            @Override
            public void onSoftKeyboardShow() {
                Log.d("SoftKeyboard", "Soft keyboard shown");
                txt_SignIn.setVisibility(View.GONE);

            }

            @Override
            public void onSoftKeyboardHide() {
                Log.d("SoftKeyboard", "Soft keyboard hidden");

                txt_SignIn.setVisibility(View.VISIBLE);

            }
        });


    }

    ///////////id mapping////////////////

    private void idMapping() {

        txt_SignIn = (TextView)findViewById(R.id.txt_signin);
        txt_Submit= (TextView)findViewById(R.id.txt_submit);
        edt_user_name = (EditText)findViewById(R.id.edt_user_name);
        edt_user_email = (EditText)findViewById(R.id.edt_user_email);
        edt_user_mob_no = (EditText)findViewById(R.id.edt_user_mob_no);
        edt_user_dob = (EditText)findViewById(R.id.edt_user_dob);
        edt_user_password = (EditText)findViewById(R.id.edt_user_password);
        edt_client_password = (EditText)findViewById(R.id.edt_client_password);
        cb_agree_terms = (CheckBox)findViewById(R.id.cb_agree_terms);
        txt_tandc = (TextView)findViewById(R.id.txt_tandc);

        String text ="Terms and Conditions";
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        txt_tandc.setText(spanString);


    }


    private void hideKeyboad(){
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            txt_SignIn.setVisibility(View.INVISIBLE);
        } else {
            txt_SignIn.setVisibility(View.VISIBLE);
        }
    }





    private void getGCMID() {

        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                dev_id = registrationId;

                Log.e("Registration id", registrationId);

            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    /////////////on click////////////

    private void setListner() {


        txt_tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityRegister.this,ActivityTermsAndConditions.class);
                startActivity(i);
            }
        });


        edt_user_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingDate();
            }
        });

        txt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UIUtil.checkNetwork(ActivityRegister.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityRegister.this);
                    return;
                }

                if (edt_user_name.getText().toString().trim().length() == 0) {
                    UIUtil.showCustomDialog("Alert", "Please enter your  Username  ", ActivityRegister.this);
                    return;
                }
                if (edt_user_email.getText().toString().trim().length() == 0) {

                    UIUtil.showCustomDialog("Alert", "Please enter  Email Address", ActivityRegister.this);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        edt_user_email.getText().toString()).matches()) {
                    UIUtil.showCustomDialog("Alert", "Please enter valid  Email Address", ActivityRegister.this);
                    return;
                }
                if (edt_user_mob_no.getText().toString().trim().length() == 0) {
                    UIUtil.showCustomDialog("Alert", "Please enter your Mobile Number ", ActivityRegister.this);
                    return;
                }

                if (edt_user_mob_no.getText().toString().trim().length() != 10) {
                    UIUtil.showCustomDialog("Alert", "Please enter valid Mobile Number ", ActivityRegister.this);
                    return;
                }
                if (edt_user_dob.getText().toString().trim().length() == 0) {
                    UIUtil.showCustomDialog("Alert", "Please select your Date of Birth ", ActivityRegister.this);
                    return;
                }

                if (edt_user_password.getText().toString().trim().length() == 0) {
                    UIUtil.showCustomDialog("Alert", "Please enter your Password", ActivityRegister.this);
                    return;
                }

                if (edt_client_password.getText().toString().trim().length() == 0) {
                    UIUtil.showCustomDialog("Alert", "Please enter your Unique Client Password", ActivityRegister.this);
                    return;
                }

                if (!cb_agree_terms.isChecked()) {
                    UIUtil.showCustomDialog("Alert", "Please accept Terms and Conditions", ActivityRegister.this);
                    return;
                }


                UIUtil.showDialog(ActivityRegister.this);



                JsonObject obj = new JsonObject();
                JsonObject reg_obj = new JsonObject();
                reg_obj.addProperty("email",""+edt_user_email.getText());
                reg_obj.addProperty("userName",""+edt_user_name.getText());
                reg_obj.addProperty("password",""+edt_user_password.getText());
                String convertedDateTime = "";
                convertedDateTime = UIUtil.convertDateTime("dd-MM-yyyy", "yyyy-MM-dd", ""+edt_user_dob.getText());
                reg_obj.addProperty("dob",""+convertedDateTime);
                reg_obj.addProperty("cellNo",""+ edt_user_mob_no.getText());
                reg_obj.addProperty("clientPassword","12345");
                reg_obj.addProperty("deviceId",dev_id);
                reg_obj.addProperty("mobileType","0");

                obj.add("reg_obj", reg_obj);



                ApiHandler.getApiService().register( reg_obj , new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {

                        UIUtil.dismissDialog();

                        Gson gson = new Gson();

                        Register register = gson.fromJson( jsonObject.toString(), Register.class );

                        Log.e("response",""+register.toString());

                        if (register == null) {
                            UIUtil.showCustomDialog("Alert", "something went wrong..", ActivityRegister.this);
                            return;
                        }
                        if (register.getData() == null) {
                            UIUtil.showCustomDialog("Alert", register.getMessage(), ActivityRegister.this);
                            return;
                        }



                        if (register.getStatus() == null) {
                            UIUtil.showCustomDialog("Alert", register.getMessage(), ActivityRegister.this);
                            return;
                        }

                        if (register.getStatus().equals("FAIL")) {

                            UIUtil.showCustomDialog("Alert", "" + register.getMessage(), ActivityRegister.this);
                            return;
                        }
                        if (register.getStatus().equals("SUCCESS")) {
                     //       session.createSession(register.getData());


                            Intent i_submit = new Intent(ActivityRegister.this, ActivityVerifyOtp.class);
                            i_submit.putExtra("phonnumber", edt_user_mob_no.getText().toString());
                            i_submit.putExtra("email", edt_user_email.getText().toString());
                            startActivity(i_submit);
                            UIUtil.toast(ActivityRegister.this, "You are successfully registered.");
                            Log.e("success", "" + register.getMessage());




                        }


                    }


                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.dismissDialog();
                        error.printStackTrace();
                        error.getMessage();
                        Log.e("error",""+error.getMessage());
                        //  UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityRegister.this);
                    }
                });

            }
        });



        txt_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    private void gettingDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(ActivityRegister.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String convertedDateTime = "";
                        convertedDateTime = UIUtil.convertDateTime("yyyy-M-d", "dd-MM-yyyy", (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                        edt_user_dob.setText(convertedDateTime);
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpd.show();
    }



    private Map<String, String> getregisterMap() {
        Map<String, String> map = new HashMap<>();
        map.put("user_email",""+edt_user_email.getText());
        map.put("user_name",""+edt_user_name.getText());
        map.put("user_phone", "" + edt_user_mob_no.getText());
        map.put("user_dob", ""+edt_user_dob.getText());
        map.put("user_password", "" + edt_user_password.getText());
        return map;
    }
}