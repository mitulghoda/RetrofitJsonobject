package com.thehighlife.Activities.WebServices;


import com.google.gson.JsonObject;
import com.thehighlife.Activities.Model.AboutUsModel.AboutUsMainResponse;
import com.thehighlife.Activities.Model.AddToWalletModel.AddToWalletResponse;
import com.thehighlife.Activities.Model.AllCategoriesGridviewModel.AllcategoriesMainResponse;
import com.thehighlife.Activities.Model.AllCityModel.AllCityMainResponse;
import com.thehighlife.Activities.Model.CodeAvilableMainModel.CodeAvailableOrNot;
import com.thehighlife.Activities.Model.EditProfileModel.EditProfileMainResponse;
import com.thehighlife.Activities.Model.ForgotPasswordUser.ForgotPassword;
import com.thehighlife.Activities.Model.GetLikeAndDislikeModel.GetLikeDisLikeResponse;
import com.thehighlife.Activities.Model.GetMenuCategoryResponse.GetMenuCategoryResponse;
import com.thehighlife.Activities.Model.GetVideoModel.VideoMainResponse;
import com.thehighlife.Activities.Model.GetVoucherCodeMainResponse.VoucherCodeMainResponse;
import com.thehighlife.Activities.Model.HighLifeTrendsModel.HighLifeTrendsMainResponse;
import com.thehighlife.Activities.Model.HomeOffersModel.HomeOfferesMainResponse;
import com.thehighlife.Activities.Model.LikeAndDislikeModel.LikeAndDisLikeClass;
import com.thehighlife.Activities.Model.LoginUser.Login;
import com.thehighlife.Activities.Model.NearByOffersModel.NearByOffersMainResponse;
import com.thehighlife.Activities.Model.NotificationModel.NotificationMainResponse;
import com.thehighlife.Activities.Model.PreffredCategoryResponse.PreffredCategoryResponse;
import com.thehighlife.Activities.Model.PreffredCityResponse.PreffredCityResponse;
import com.thehighlife.Activities.Model.ResendOtpModel.ResendOtpMainResponse;
import com.thehighlife.Activities.Model.SearchOffersModel.SearchOffersMainResponse;
import com.thehighlife.Activities.Model.SubCategoryGridviewModel.SubCategoryGridMainResponse;
import com.thehighlife.Activities.Model.SubCategoryListviewModel.SubCategoryListviewMainResponse;
import com.thehighlife.Activities.Model.VerifyOTPModel.VerifyOTPMainResponse;
import com.thehighlife.Activities.Model.WalletCountModel.WalletCount;
import com.thehighlife.Activities.Model.WalletListModel.WalletListMainResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.EncodedPath;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;



public interface WebServices {


     //////Registration/////
    @Headers("Content-Type: application/json")
    @POST("/registration")
    public  void register(@Body JsonObject bean, Callback<JsonObject> callback);


    //////Login/////
    @POST("/login")
    public  void userlogin(@QueryMap Map<String, String> map,
                           Callback<Login> callback);

    //////Forgot password/////
    @POST("/forgetpass")
    public void forgotpassword(@QueryMap Map<String, String> map, Callback<ForgotPassword> callback);



    //////OTP/////
    @POST("/newverifyOpt")
    public  void userverifyotp(@QueryMap Map<String, String> map,
                               Callback<VerifyOTPMainResponse> callback);




    ////// Resend OTP/////
    @POST("/resendOtp")
    public  void resendotp(@QueryMap Map<String, String> map,
                           Callback<ResendOtpMainResponse> callback);


    //////Change  password/////
    @POST("/changePassword")
    public  void changepassword(@Body JsonObject bean, Callback<JsonObject> callback);


    //////add feedback/////
    @POST("/feedback")
    public void addfeedback(@Body JsonObject bean, Callback<JsonObject> callback);


    ////all category////
    @GET("/getCategory/{userid}")
    public void allcategories(@EncodedPath("userid") String userid, Callback<AllcategoriesMainResponse> callback);


    //////Sub category gridview/////
    @GET("/getSubCategorybyCategoryId")
    public void getsubcategoriesgrid(@QueryMap Map<String, String> map, Callback<SubCategoryGridMainResponse> callback);


    //////Get all cities/////
    @GET("/getAllCitiesForPrefered")
    public void getallcities(@QueryMap Map<String, String> map, Callback<AllCityMainResponse> callback);


    //////Get all categorieslist for preferred/////
    @GET("/getAllCategoriesForPrefered")
    public void getallavailablecategories(@QueryMap Map<String, String> map, Callback<AllcategoriesMainResponse> callback);


    //////EditProfile without image/////
    @POST("/userProfieEdit")
    public  void editprofilewithoutimage(@QueryMap Map<String, String> map, @Body JsonObject bean, Callback<JsonObject> callback);



    //////EditProfile with image/////
    @Multipart
    @POST("/userProfieEditImage")
    public void updateUserProfilewithImage(@Part("file") TypedFile file, @PartMap Map<String, String> map, Callback<EditProfileMainResponse> callback);



    //////Sub category listview/////
    @GET("/getOfferbySubcategoryId")
    public void getsubcategorieslistview(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);


    //////Add To Favorites////
    @POST("/addToFavorite")
    public void addtofavorites(@Body JsonObject bean, Callback<JsonObject> callback);




    //////Add To wallet////
    @POST("/addOfferinWallet")
    public void addtowallet(@Body JsonObject bean, Callback<JsonObject> callback);



    //////Favorites Listview/////
    @GET("/listFavorite")
    public void getFavoriteslistview(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);



    /////delete from favorites//////

    @POST("/deleteFavorite")
    public void deletefromfavorites(@Body JsonObject bean, Callback<JsonObject> callback);




    /////delete from walletlist//////

    @POST("/deleteFromWallet")
    public void deletefromwallet(@Body JsonObject bean, Callback<JsonObject> callback);

    //////Get all Home page offers////
    @GET("/getHomePageOffers")
    public void gethomeoffer(@QueryMap Map<String, String> map, Callback<HomeOfferesMainResponse> callback);


    @GET("/getAllCategoriesWithSubCategories")
    public void getHomeMenu(@QueryMap Map<String, String> map, Callback<GetMenuCategoryResponse> callback);



    //////Add Preffred category////
    @POST("/prefCategory")
    public void addPreffredCategory(@QueryMap Map<String, String> map, Callback<PreffredCategoryResponse> callback);


    //////Add Preffred city////
    @POST("/prefCity")
    public void addPreffredCity(@QueryMap Map<String, String> map, Callback<PreffredCityResponse> callback);


    ///////wallet Listview//////
    @GET("/listOfferinWallet")
    public void getWalletistview(@QueryMap Map<String, String> map, Callback<WalletListMainResponse> callback);


    ///////HighLife Trends Listview//////
    @GET("/getHighlifeTrends")
    public void getHifhLifeTrendsList(Callback<HighLifeTrendsMainResponse> callback);


    ///////About Us//////
    @GET("/aboutUs")
    public void getaboutus(Callback<AboutUsMainResponse> callback);



    //////Near by offers////
    @GET("/nearByOffers")
    public  void getnearbyoffers(@QueryMap Map<String, String> map,
                                 Callback<NearByOffersMainResponse> callback);


    @GET("/nearByOffersAsCityWise")
    public  void getnearbyofferssearch(@QueryMap Map<String, String> map,
                                 Callback<NearByOffersMainResponse> callback);



    //////search by offers////
    @POST("/searchOffers")
    public  void getsearchresult(@QueryMap Map<String, String> map,
                                 Callback<SearchOffersMainResponse> callback);




    @GET("/getNotificationForOffers")
    public  void getnotificationlist(@QueryMap Map<String, String> map,
                                     Callback<NotificationMainResponse> callback);

    @GET("/getCountFromWallet")
    public  void getwalletcount(@QueryMap Map<String, String> map,
                                Callback<WalletCount> callback);


   ///////////scan barcode/////////
    @POST("/redeemOffer")
    public  void scanQRCode(@QueryMap Map<String, String> map, @Body JsonObject bean, Callback<JsonObject> callback);


    //////////Like and dislike Offer /////////


    @POST("/updateLikeDislikeOffers")
    public void updatelikeanddislike(@QueryMap Map<String, String> map, Callback<LikeAndDisLikeClass> callback);




    @GET("/getLikeDislikeOffers")
    public void getlikeanddislike(@QueryMap Map<String, String> map, Callback<GetLikeDisLikeResponse> callback);



    @GET("/sortOnOffers")
    public void filteroffer(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);



    ///////////////get Voucher code/////////////////////
    @GET("/getVoucherCodeForOffer")
    public  void getvouchercode(@QueryMap Map<String, String> map,
                                Callback<VoucherCodeMainResponse> callback);



    ////cancel voucher code/////
    @POST("/cancelVoucherCodeForOffer")
    public void cancelvoucher(@QueryMap Map<String, String> map, Callback<AboutUsMainResponse> callback);




    ////send voucher code/////
    @POST("/sendVoucherCode")
    public void sendvoucher(@QueryMap Map<String, String> map, Callback<AboutUsMainResponse> callback);




    ////forrgot email code/////
    @POST("/sendEmailByCellNo")
    public void forgotemail(@QueryMap Map<String, String> map, Callback<ForgotPassword> callback);




    //////filter by offer title/////
    @GET("/filterByOffertitle")
    public void filterByOfferTitle(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);


    //////filter by offer type/////
    @GET("/filterByOfferType")
    public void filterByOfferType(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);


    //////filter by offer eXPIRYDATE/////
    @GET("/filterByOfferExpiryDate")
    public void filterByOfferExpiry(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);


    //////filter by offer title/////
    @GET("/filterByOfferCities")
    public void filterByOfferCity(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);




    //////filter by offer price/////
    @GET("/filterByOfferAmount")
    public void filterByOfferPrice(@QueryMap Map<String, String> map, Callback<SubCategoryListviewMainResponse> callback);


    //////////////getvideourl//////////
    ////send voucher code/////
    @POST("/downloadSplashScreenForUser")
    public void getvideourl(@QueryMap Map<String, String> map, Callback<VideoMainResponse> callback);

    ////////////videosucess////////

    @POST("/downloadSuccesfullUpdation")
    public void getvideourlsucess(@QueryMap Map<String, String> map, Callback<AddToWalletResponse> callback);



    //////////code available or not//////////
    @GET("/isCouponCodeAvailableFortheOffer")
    public void isvouchercodeavilable(@QueryMap Map<String, String> map, Callback<CodeAvailableOrNot> callback);


    //////////code available or not//////////
    @POST("/paymentEntry")
    public void paymentsucess(@QueryMap Map<String, String> map, Callback<CodeAvailableOrNot> callback);


    ///////////delete notifications//////////////
    @POST("/deleteNotifications")
    public void deletenotifications(@QueryMap Map<String, String> map, Callback<CodeAvailableOrNot> callback);


}







