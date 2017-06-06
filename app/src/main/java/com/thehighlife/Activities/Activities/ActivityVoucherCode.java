package com.thehighlife.Activities.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thehighlife.Activities.Fragments.WalletFragment;
import com.thehighlife.Activities.Model.AboutUsModel.AboutUsMainResponse;
import com.thehighlife.Activities.Model.CodeAvilableMainModel.CodeAvailableOrNot;
import com.thehighlife.Activities.Model.GetVoucherCodeMainResponse.VoucherCodeMainResponse;
import com.thehighlife.Activities.Model.ScanQrCodeModel.ScanQrCode;
import com.thehighlife.Activities.Model.WalletCountModel.WalletCount;
import com.thehighlife.Activities.Model.WalletListModel.WalletListviewResponse;
import com.thehighlife.Activities.Payment.PaymentActivity;
import com.thehighlife.Activities.Utils.Constants;
import com.thehighlife.Activities.Utils.SessionManager;
import com.thehighlife.Activities.Utils.UIUtil;
import com.thehighlife.Activities.WebServices.ApiHandler;
import com.thehighlife.Activities.application.TheHighLife;
import com.thehighlife.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityVoucherCode extends AppCompatActivity {
    private TextView txt_title,txt_offer,txt_validupto,txt_scan,txt_voucher_code,txt_reedem,txt_wallet,txt_cancel,txt_voucher_your,txt_amount,txt_brand;
    private ImageView iv_back,iv_search,iv_pdt_logo,iv_hot;
    int int_Position;
    private ImageLoader imageLoader;
    SessionManager session;
    String scanned_code;
    private LinearLayout voucher_detail;
    private ImageView tab1_iv, tab2_iv, tab3_iv, tab4_iv,tab5_iv;

    String title;
    String cancel_reason;
    Boolean redeem = false;
    LinearLayout ll_Alerts,ll_alertdetails,ll_alerts_top;
    String amount;
    String str_amount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_voucher_code);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        session = new SessionManager(ActivityVoucherCode.this);
        imageLoader = ImageLoader.getInstance();
        getData();
        idMapping();
        settab();
        getWalletCount();
        setData();
        setListner();

        WalletFragment.refresh =true;

    }

    @Override
    public void onResume() {

        super.onResume();
        getData();
        setData();

    }

    private void getData() {


        if(Constants.position !=null){
            int_Position = Constants.position;
        }


//        Bundle b = getIntent().getExtras();
//
//        if (b != null && !b.isEmpty()) {
//
//            int_Position = b.getInt("");
//
//        }
    }

    ///////////id mapping////////////////

    private void idMapping() {

        txt_title =(TextView)findViewById(R.id.txt_title);
        txt_title.setText("Voucher Details");
        txt_offer=(TextView)findViewById(R.id.txt_offer);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_search= (ImageView)findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
        iv_pdt_logo= (ImageView)findViewById(R.id.iv_pdt_logo);
        txt_validupto = (TextView)findViewById(R.id.txt_validupto);
        txt_scan = (TextView)findViewById(R.id.txt_scan);
        voucher_detail= (LinearLayout)findViewById(R.id.voucher_detail);
        txt_voucher_code = (TextView)findViewById(R.id.txt_voucher_code);
        txt_reedem  = (TextView)findViewById(R.id.txt_reedem);

        txt_amount =(TextView)findViewById(R.id.txt_amount);
        txt_brand =(TextView)findViewById(R.id.txt_brand);

        tab1_iv = (ImageView) findViewById(R.id.iv_home);
        tab2_iv = (ImageView) findViewById(R.id.iv_favorite);
        tab3_iv = (ImageView) findViewById(R.id.iv_notifications);
        tab4_iv = (ImageView) findViewById(R.id.iv_wallet);
        tab5_iv = (ImageView) findViewById(R.id.iv_faq);
        txt_wallet= (TextView) findViewById(R.id.txt_wallet);
        txt_cancel= (TextView) findViewById(R.id.txt_cancel);
        txt_voucher_your =(TextView)findViewById(R.id.txt_voucher_your);

        iv_hot= (ImageView)findViewById(R.id.iv_hot);

        ll_Alerts= (LinearLayout)findViewById(R.id.ll_alerts);
        ll_alertdetails= (LinearLayout)findViewById(R.id.ll_alerts_details);
        ll_alerts_top= (LinearLayout)findViewById(R.id.ll_alerts_top);
        ll_alertdetails.setVisibility(View.GONE);





    }

    private void settab() {

        tab1_iv.setImageResource(R.drawable.foot_home_inactive);
        tab2_iv.setImageResource(R.drawable.foot_favourite_inactive);
        tab3_iv.setImageResource(R.drawable.foot_iconmap_unactive);
        tab4_iv.setImageResource(R.drawable.foot_wallet_inactive);
        tab5_iv.setImageResource(R.drawable.foot_faq_inactive);
    }
    private void setData(){

        Log.e("set","set");


        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;


        if (list_AllDta !=null && !list_AllDta.isEmpty()){


            String convertedDateTime = "";
            convertedDateTime = UIUtil.convertDate("dd-MM-yyyy", list_AllDta.get(int_Position).getOfferExpiry());
            txt_offer.setText(list_AllDta.get(int_Position).getOfferTitle());


            txt_validupto.setText("Valid till " + convertedDateTime);


            if(list_AllDta.get(int_Position).getOfftype()!=null){
                if( list_AllDta.get(int_Position).getOfftype().equals("PAID")){


                    if(list_AllDta.get(int_Position).getAmount() !=null)
                    {
                        txt_amount.setVisibility(View.VISIBLE);
                        txt_amount.setText("Voucher Price :" +list_AllDta.get(int_Position).getAmount()+ getResources().getString(R.string.Rs));

                    }


                }
                else{
                    txt_amount.setVisibility(View.GONE);
                    txt_amount.setText("Price : FREE");
                }
            }


            txt_brand.setText(""+list_AllDta.get(int_Position).getBrandName());

            imageLoader.displayImage(list_AllDta.get(int_Position).getOfferImage(), iv_pdt_logo, TheHighLife.getProductImageDisplayOption(ActivityVoucherCode.this));




            if(list_AllDta.get(int_Position).getIsHotOffer() !=null){

                if( list_AllDta.get(int_Position).getIsHotOffer()==true){

                    iv_hot.setVisibility(View.VISIBLE);

                }
                else{

                    iv_hot.setVisibility(View.GONE);
                }

            }


            if(list_AllDta.get(int_Position).getIsReedemed()){

                redeem = true;
                txt_reedem.setText("REDEEMED");

            }

            if(list_AllDta.get(int_Position).getVoucherCode() !=null){

                scanned_code = list_AllDta.get(int_Position).getVoucherCode();
                txt_voucher_code.setText(scanned_code);
                Constants.vouchercode=""+scanned_code;
                txt_voucher_your.setVisibility(View.VISIBLE);
                voucher_detail.setVisibility(View.VISIBLE);
                txt_scan.setVisibility(View.GONE);
            }



            if(Constants.payment !=null){
                if(Constants.payment.equals("yes")){

//
//                    scanned_code = Constants.vouchercode;
//                    txt_voucher_code.setText("22");
//
//                    txt_voucher_your.setVisibility(View.VISIBLE);
//                    voucher_detail.setVisibility(View.VISIBLE);
//                    txt_scan.setVisibility(View.GONE);

                    getVoucherCodeApi();

                    paymentsuccess();



                }
            }





        }



    }

    private void paymentsuccess() {

        ApiHandler.getApiService().paymentsucess(getsuccessPmap(), new Callback<CodeAvailableOrNot>() {
            @Override
            public void success(CodeAvailableOrNot verifyOTPMainResponse, Response response) {

                Log.e("response", "" + verifyOTPMainResponse.toString());



                if (verifyOTPMainResponse == null) {
                    UIUtil.showCustomDialog("Alert", " Something went wrong", ActivityVoucherCode.this);
                    return;
                }

                if (verifyOTPMainResponse.getStatus() == null) {
                    UIUtil.showCustomDialog("Alert", verifyOTPMainResponse.getMessage(), ActivityVoucherCode.this);
                    return;
                }

                if (verifyOTPMainResponse.getStatus().toUpperCase().equals("FAIL")) {

                    UIUtil.showCustomDialog("Alert", "" + verifyOTPMainResponse.getMessage(), ActivityVoucherCode.this);
                    return;
                }


                if (verifyOTPMainResponse.getStatus().toUpperCase().equals("SUCCESS")) {
//                    UIUtil.showCustomDialog("Alert", "" + verifyOTPMainResponse.getMessage(), ActivityVoucherCode.this);
//                    return;
                }

                //  UIUtil.showCustomDialog("Info", "" + login.getMessage(), ActivityLogin.this);
            }


            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //      UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityVerifyOtp.this);
            }
        });


    }



    private Map<String, String> getsuccessPmap() {

        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;
        Map<String, String> map = new HashMap<>();

        map.put("userId", session.getUserId());


        String expiry  = list_AllDta.get(int_Position).getAmount();



        String[] seprt = expiry.split("\\.");


        if (seprt.length != 0) {

            for (int i = 0; i < seprt.length; i++) {

                String mydata = seprt[i];

                Log.e("mydata", "" + mydata);

            }

            str_amount = seprt[0] ;


            Log.e("AMOUNT",str_amount);
        }


       if(str_amount !=null){
           map.put("amount", str_amount);
       }

        map.put("offerId", list_AllDta.get(int_Position).getOfferid());

        return map;
    }

    /////////////on click////////////

    private void setListner() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        txt_scan.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(ActivityVoucherCode.this);
//                integrator.initiateScan();
//
//
//            }
//
//
//        });

        txt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

                list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;


                if(list_AllDta.get(int_Position).getOfftype()!=null){
                    if( list_AllDta.get(int_Position).getOfftype().equals("PAID")){


                        if(list_AllDta.get(int_Position).getAmount() !=null)
                        {

                            amount =list_AllDta.get(int_Position).getAmount();
                            Constants.amount = amount;

                            checkVoucher();


                        }


                    }
                    else{
                        getVoucherCodeApi();
                    }
                }



            //    getVoucherCodeApi();










                // voucher_detail.setVisibility(View.VISIBLE);
                //   txt_scan.setVisibility(View.GONE);


            }
        });


        tab1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ActivityVoucherCode.this, MainActivity.class);
                i.putExtra(Constants.DETAILACTIVITY, Constants.HOME);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);


            }
        });

        tab2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(ActivityVoucherCode.this, MainActivity.class);
                i.putExtra(Constants.DETAILACTIVITY, Constants.FAVORITES);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);



            }
        });

        tab3_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ActivityVoucherCode.this, MainActivity.class);
                i.putExtra(Constants.DETAILACTIVITY, Constants.NEARBY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);
            }
        });

        tab4_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(ActivityVoucherCode.this, MainActivity.class);
                i.putExtra(Constants.DETAILACTIVITY, Constants.WALLET);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);

            }
        });
        tab5_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(ActivityVoucherCode.this, MainActivity.class);
                i.putExtra(Constants.DETAILACTIVITY, Constants.MORE);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);

            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
//                if(redeem){
//                    UIUtil.showCustomDialog("Alert", "this offer has been redeemed by you already...", ActivityVoucherCode.this);
//
//                }
//
//                else{
//                    cancelapi();
//                }


            }
        });

        txt_reedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(redeem){
                    UIUtil.showCustomDialog("Alert", "This offer code has already been redeemed by you.", ActivityVoucherCode.this);

                }

                else{

                    showDaolog();
                }







            }
        });


        ll_Alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ll_alertdetails.isShown()) {
                    collapse(ll_alertdetails);
                 //   expand(ll_alerts_top);

                } else {
                    expand(ll_alertdetails);
                  //  collapse(ll_alerts_top);

                }

            }
        });


    }

    private void checkVoucher() {

        UIUtil.showDialog(ActivityVoucherCode.this);
        ApiHandler.getApiService().isvouchercodeavilable(getverifyOTPmap(), new Callback<CodeAvailableOrNot>() {
            @Override
            public void success(CodeAvailableOrNot verifyOTPMainResponse, Response response) {

                Log.e("response", "" + verifyOTPMainResponse.toString());
                UIUtil.dismissDialog();


                if (verifyOTPMainResponse == null) {
                    UIUtil.showCustomDialog("Alert", " Something went wrong", ActivityVoucherCode.this);
                    return;
                }

                if (verifyOTPMainResponse.getStatus() == null) {
                    UIUtil.showCustomDialog("Alert", "Voucher code exhausted, unable to issue new voucher code at this time.", ActivityVoucherCode.this);
                    return;
                }

                if (verifyOTPMainResponse.getStatus().toUpperCase().equals("FAIL")) {

                    UIUtil.showCustomDialog("Alert", "Voucher code exhausted, unable to issue new voucher code at this time.", ActivityVoucherCode.this);
                    return;
                }


                if (verifyOTPMainResponse.getStatus().toUpperCase().equals("SUCCESS")) {

                    Intent i = new Intent(ActivityVoucherCode.this,PaymentActivity.class);

                    startActivity(i);
                }

                //  UIUtil.showCustomDialog("Info", "" + login.getMessage(), ActivityLogin.this);
            }


            @Override
            public void failure(RetrofitError error) {
                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //      UIUtil.showCustomDialog("Alert", " something went wrong..", ActivityVerifyOtp.this);
            }
        });



    }

    private Map<String, String> getverifyOTPmap() {

        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;
        Map<String, String> map = new HashMap<>();
        map.put("userId", session.getUserId());
        map.put("amount", list_AllDta.get(int_Position).getAmount());
        map.put("offerId", list_AllDta.get(int_Position).getOfferid());

        return map;
    }
    /////////Animation to expand the view//////
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    /////////Animation to collapse the view//////
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private void callSendVoucherApi() {

        UIUtil.showDialog(ActivityVoucherCode.this);
        ApiHandler.getApiService().sendvoucher(sendmap(), new Callback<AboutUsMainResponse>() {

            @Override
            public void success(AboutUsMainResponse walletCount, Response response) {
                UIUtil.dismissDialog();


                if (walletCount == null) {
                    UIUtil.showCustomDialog("Alert", " something went wrong...", ActivityVoucherCode.this);
                    return;
                }

                if (walletCount.getData() == null) {

                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;

                }


                if (walletCount.getStatus() == null) {

                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;


                }

                if (walletCount.getStatus().toUpperCase().equals("FAIL")) {


                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;


                }
                if (walletCount.getStatus().toUpperCase().equals("SUCCESS")) {



                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;
                }

            }


            @Override
            public void failure(RetrofitError error) {
                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //   UIUtil.showCustomDialog("Alert", "Oops something went wrong..", getActivity());
            }


        });
    }

    private void cancelapi() {
        UIUtil.showDialog(ActivityVoucherCode.this);
        ApiHandler.getApiService().cancelvoucher(cancelmap(), new Callback<AboutUsMainResponse>() {

            @Override
            public void success(AboutUsMainResponse walletCount, Response response) {
                UIUtil.dismissDialog();


                if (walletCount == null) {
                    UIUtil.showCustomDialog("Alert", " something went wrong...", ActivityVoucherCode.this);
                    return;
                }

                if (walletCount.getData() == null) {

                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    finish();

                    return;

                }


                if (walletCount.getStatus() == null) {

                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;


                }

                if (walletCount.getStatus().toUpperCase().equals("FAIL")) {


                    UIUtil.showCustomDialog("Alert", "" + walletCount.getMessage(), ActivityVoucherCode.this);
                    return;


                }
                if (walletCount.getStatus().toUpperCase().equals("SUCCESS")) {


                    finish();

                }

            }


            @Override
            public void failure(RetrofitError error) {
                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //   UIUtil.showCustomDialog("Alert", "Oops something went wrong..", getActivity());
            }


        });

    }

    private void getVoucherCodeApi() {



        UIUtil.showDialog(ActivityVoucherCode.this);
        ApiHandler.getApiService().getvouchercode(getvouhercodeMap(), new Callback<VoucherCodeMainResponse>() {

            @Override
            public void success(VoucherCodeMainResponse walletCount, Response response) {
                UIUtil.dismissDialog();


                if (walletCount == null) {
                    UIUtil.showCustomDialog("Alert", " something went wrong...", ActivityVoucherCode.this);
                    return;
                }

                if (walletCount.getData() == null) {

                    UIUtil.showCustomDialog("Alert", "Voucher code exhausted, unable to issue new voucher code at this time.", ActivityVoucherCode.this);
                    return;

                }


                if (walletCount.getStatus() == null) {

                    UIUtil.showCustomDialog("Alert", "Voucher code exhausted, unable to issue new voucher code at this time.", ActivityVoucherCode.this);
                    return;


                }

                if (walletCount.getStatus().toUpperCase().equals("FAIL")) {


                    UIUtil.showCustomDialog("Alert", "Voucher code exhausted, unable to issue new voucher code at this time.", ActivityVoucherCode.this);
                    return;


                }
                if (walletCount.getStatus().toUpperCase().equals("SUCCESS")) {
                    scanned_code = walletCount.getData();
                    txt_voucher_code.setText(scanned_code);
                    Constants.vouchercode=""+walletCount.getData();

                    voucher_detail.setVisibility(View.VISIBLE);
                    txt_voucher_your.setVisibility(View.VISIBLE);
                    txt_scan.setVisibility(View.GONE);



                }

            }


            @Override
            public void failure(RetrofitError error) {
                UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //   UIUtil.showCustomDialog("Alert", "Oops something went wrong..", getActivity());
            }


        });
    }




//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if (scanResult != null) {
//            String re = scanResult.getContents();
//
//            scanned_code = ""+re;
//            if(scanned_code.length()!=0 &&(scanned_code !=null)){
//                callScanCode();
//            }
//        }
//        // else continue with any other code you need in the method
//
//    }








    private void callScanCode() {


        Log.e("vou",Constants.vouchercode);
        Log.e("se",Constants.sendto);






        if(Constants.vouchercode !=null && Constants.sendto !=null){




            Constants.payment="no";

            ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

            list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;

            UIUtil.showDialog(ActivityVoucherCode.this);

            JsonObject obj = new JsonObject();
            JsonObject voucher_obj = new JsonObject();
            voucher_obj.addProperty("userid",""+session.getUserId());
            voucher_obj.addProperty("offerid",""+list_AllDta.get(int_Position).getOfferid());
            voucher_obj.addProperty("categoryid",""+list_AllDta.get(int_Position).getCategoryid());
            voucher_obj.addProperty("subcategoryid",""+list_AllDta.get(int_Position).getSubcategoryid());
            voucher_obj.addProperty("walletId",""+list_AllDta.get(int_Position).getWalletId());


            obj.add("reg_obj", voucher_obj);


            Log.e("JsonObj", "" + voucher_obj);



            ApiHandler.getApiService().scanQRCode(getscsncodemap(), voucher_obj, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {

                    UIUtil.dismissDialog();

                    Gson gson = new Gson();

                    ScanQrCode scanQrCode = gson.fromJson(jsonObject.toString(), ScanQrCode.class);

                    Log.e("response", "" + scanQrCode.toString());



                    if (scanQrCode == null) {
                        UIUtil.showCustomDialog("Alert", ""+scanQrCode.getMessage() ,ActivityVoucherCode.this);
                        return;
                    }

                    if (scanQrCode.getStatus() == null) {
                        UIUtil.showCustomDialog("Alert",""+scanQrCode.getMessage() , ActivityVoucherCode.this);
                        return;
                    }

                    if (scanQrCode.getStatus().equals("FAIL")) {

                        UIUtil.showCustomDialog("Alert",""+scanQrCode.getMessage() , ActivityVoucherCode.this);
                        return;
                    }
                    if (scanQrCode.getStatus().equals("SUCCESS")) {
                        UIUtil.showCustomDialog("Alert", "Thank you for redeeming your voucher code.", ActivityVoucherCode.this);
                        redeem=true;
                        txt_reedem.setText("REDEEMED");

                        return;
                    }


                }


                @Override
                public void failure(RetrofitError error) {
                    UIUtil.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Log.e("error", "" + error.getMessage());
                    //      UIUtil.showCustomDialog("Alert", "Oops something went wrong..", ActivityVoucherCode.this);
                }
            });
        }


    }

    private void showDaolog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(ActivityVoucherCode.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_redeem);


        dialog.show();


        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        TextView txt_no = (TextView) dialog.findViewById(R.id.txt_no);

        final RadioGroup  radioGroup1 = (RadioGroup)dialog.findViewById(R.id.radioGroup1);
        final RadioButton radio_1 = (RadioButton) dialog.findViewById(R.id.radio_1);
        final RadioButton  radio_2 = (RadioButton)dialog. findViewById(R.id.radio_2);
        final  RadioButton radio_3 = (RadioButton)dialog. findViewById(R.id.radio_3);
        final  RadioButton radio_4 = (RadioButton)dialog. findViewById(R.id.radio_4);


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == R.id.radio_1) {



                } else if(checkedId == R.id.radio_2) {



                }
                else if (checkedId == R.id.radio_3) {



                }
                else if (checkedId == R.id.radio_4) {



                }




            }


        });

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

                list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;


                int selectedId = radioGroup1.getCheckedRadioButtonId();





                // find which radioButton is checked by id

                if(selectedId == radio_1.getId()) {

                    Log.e("1","1");
                    cancel_reason = "SMS";

                    if(list_AllDta.get(int_Position).getOfftype() !=null){


                        if(list_AllDta.get(int_Position).getOfftype().equals("PAID")){

                            Constants.sendto=cancel_reason;
                            dialog.dismiss();
                            callScanCode();
                        }

                        else  if(list_AllDta.get(int_Position).getOfftype().equals("FREE")){
                            Constants.sendto=cancel_reason;
                            dialog.dismiss();
                            callScanCode();

                        }
                    }

                    dialog.dismiss();
                    return;

                } else if(selectedId == radio_2.getId()) {

                    Log.e("2","2");
                    cancel_reason = "EMAIL";
                    //  callScanCode();


                    if(list_AllDta.get(int_Position).getOfftype() !=null){

                        if(list_AllDta.get(int_Position).getOfftype() !=null){
                            if(list_AllDta.get(int_Position).getOfftype().equals("PAID")){
                                Constants.sendto=cancel_reason;
                                dialog.dismiss();
                                callScanCode();
                            }

                            else  if(list_AllDta.get(int_Position).getOfftype().equals("FREE")){
                                Constants.sendto=cancel_reason;
                                dialog.dismiss();
                                callScanCode();
                            }
                        }
                    }
                    return;

                } else if(selectedId == radio_3.getId()) {

                    Log.e("3","3");
                    cancel_reason = "BOTH";

                    if(list_AllDta.get(int_Position).getOfftype() !=null){
                        if(list_AllDta.get(int_Position).getAmount() !=null){
                            amount =list_AllDta.get(int_Position).getAmount();
                            Constants.amount = amount;
                        }


                        if(list_AllDta.get(int_Position).getOfftype().equals("PAID")){
                            Constants.sendto=cancel_reason;
                            dialog.dismiss();
                            callScanCode();

                        }

                        else  if(list_AllDta.get(int_Position).getOfftype().equals("FREE")){
                            Constants.sendto=cancel_reason;
                            dialog.dismiss();
                            callScanCode();
                        }

                        // callScanCode();
                    }
                    return;

                }

                else {

                    Log.e("4","4");
                    cancel_reason = "NONE";
                    if(list_AllDta.get(int_Position).getOfftype() !=null){


                        if(list_AllDta.get(int_Position).getOfftype().equals("PAID")){
                            Constants.sendto=cancel_reason;
                            dialog.dismiss();
                            callScanCode();
                        }

                        else  if(list_AllDta.get(int_Position).getOfftype().equals("FREE")){
                            dialog.dismiss();
                            Constants.sendto=cancel_reason;
                            callScanCode();
                        }

                    }



                    return;

                }


            }
        });




        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private Map<String, String> getscsncodemap() {


        Log.e("vou",Constants.vouchercode);
        Log.e("se",Constants.sendto);


        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;

        Map<String, String> map = new HashMap<>();
    //    map.put("key", "ad1xNc6f");




        map.put("vouchercode", Constants.vouchercode );
        map.put("sendBy", Constants.sendto );
        //   map.put("walletId", list_AllDta.get(int_Position).getWalletId() );



        Log.e("getscsncodemap", "" + map);

        return map;
    }


    private void getWalletCount() {

        //  UIUtil.showDialog(getActivity());

        ApiHandler.getApiService().getwalletcount(getwaletcountMap(), new Callback<WalletCount>() {

            @Override
            public void success(WalletCount walletCount, Response response) {
                // UIUtil.dismissDialog();


                if (walletCount == null) {

                }

                if (walletCount.getData() == null) {

                }


                if (walletCount.getStatus() == null) {

                }

                if (walletCount.getStatus().toUpperCase().equals("FAIL")) {


                }
                if (walletCount.getStatus().toUpperCase().equals("SUCCESS")) {

                    if (walletCount.getData() <= 9) {
                        txt_wallet.setText("" + walletCount.getData());
                    } else {
                        txt_wallet.setText("9+");
                    }

                }

            }


            @Override
            public void failure(RetrofitError error) {
                //UIUtil.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Log.e("error", "" + error.getMessage());
                //   UIUtil.showCustomDialog("Alert", "Oops something went wrong..", getActivity());
            }


        });


    }






    private Map<String, String> getwaletcountMap() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + session.getUserId() );


        Log.e("getwaletcountMap", "" + map);

        return map;
    }


    private Map<String, String> getvouhercodeMap() {

        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + session.getUserId() );
        map.put("offerId", "" + list_AllDta.get(int_Position).getOfferid() );
        map.put("walletId", "" + list_AllDta.get(int_Position).getWalletId() );


        Log.e("getwaletcountMap", "" + map);

        return map;
    }
    private Map<String, String> cancelmap() {

        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + session.getUserId() );
        map.put("offerId", "" + list_AllDta.get(int_Position).getOfferid() );
        map.put("code", "" + scanned_code);



        Log.e("getwaletcountMap", "" + map);

        return map;
    }



    private Map<String, String> sendmap() {

        ArrayList<WalletListviewResponse> list_AllDta= new ArrayList<>();

        list_AllDta = (ArrayList<WalletListviewResponse>) WalletFragment.listview_wallet;

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + session.getUserId() );
        map.put("sendBy", cancel_reason );
        map.put("code", "" + scanned_code);



        Log.e("getwaletcountMap", "" + map);

        return map;
    }

}


