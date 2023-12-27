package com.saneforce.godairy.Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Model_Class.Approval;
import com.saneforce.godairy.Model_Class.DateReport;
import com.saneforce.godairy.Model_Class.HeaderCat;
import com.saneforce.godairy.Model_Class.Location;
import com.saneforce.godairy.Model_Class.Model;
import com.saneforce.godairy.Model_Class.POSDataList;
import com.saneforce.godairy.Model_Class.PrimaryNoOrderList;
import com.saneforce.godairy.Model_Class.ReportDataList;
import com.saneforce.godairy.Model_Class.RetailerViewDetails;
import com.saneforce.godairy.Model_Class.TpMyDayPlanExtra;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("Db_v300.php?")
    Call<Model> login(@Query("axn") String axn, @Query("Email") String Email, @Query("UserID") String UserID, @Query("Pwd") String Password, @Query("AppVer") String AppVer, @Query("DvID") String deveiceId);


    @POST("db_new_activity.php?")
    Call<Object> GettpWorktypeFields(@QueryMap Map<String, String> params);

    @POST("Db_v300.php?")
    Call<ResponseBody> setOutletStatus(@Query("axn") String axn, @QueryMap Map<String, String> params);

    /*
        shift time*
        @GET("Db_Native.php?")
        Call<List<Example>>shiftTime(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code);
    */
    /*shift time*/
    @GET("Db_v300.php?")
    Call<JsonArray> getSetups(@Query("axn") String axn, @Query("rSF") String Sf_code);




    /* get Data as Array List */
    @GET("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("Sf_code") String Sf_code);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Field("data") String data);


    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@QueryMap Map<String, String> params, @Field("data") String body);

    // @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getStockAudit(@Query("axn") String axn, @Query("div") String div);


    @GET("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("Sf_code") String Sf_code, @Query("dte") String date);

    /*Locations*/
    @GET("Db_v300.php?")
    Call<List<Location>> location(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("Sf_code") String Sf_code);

    /*sending data*/
    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonObject> JsonSave(@Query("axn") String axn, @Query("Ekey") String Ekey , @Query("divisionCode") String divisionCode, @Query("Sf_code") String Sf_code, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    /*sending data*/
    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonObject> JsonSave(@Query("axn") String axn, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/poscountersalesentry")
    Call<JsonObject> posCounterEntrySave(@Query("divisionCode") String divisionCode, @Field("data") String body);

    /*sending data*/
    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonObject> getDataList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDataArrayListDist(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("distributorId") String distributorId, @Query("desig") String desig, @Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDayReport(@Query("date") String date, @Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonObject> getDataList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonArray> getDataArrayList(@Query("axn") String axn, @Query("Priod") int Priod, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<JsonObject> getDataObjectList(@Query("axn") String axn, @Query("Priod") int Priod, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/dynamictp")
    Call<Object> Tb_Mydayplannew(@QueryMap Map<String, String> params, @Field("data") String body);

    @FormUrlEncoded
    @POST("db_activity.php?axn=get/view")
    Call<ResponseBody> getView(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_activity.php?axn=get/dashboard_particulars")
    Call<ResponseBody> getDasboardParticulars(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_activity.php?axn=get/menu")
    Call<ResponseBody> getMenu(@Field("data") String userData);

    @Multipart
    @POST("db_activity.php?axn=upload/procpic")
    Call<ResponseBody> uploadProcPic(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("db_activity.php?axn=save/view")
    Call<ResponseBody> saveView(@Field("data") String userData);


    /*LEAVE APPROVAL*/

    @GET("Db_v300.php?")
    Call<List<Approval>> approval(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("Sf_code") String Sf_code, @Query("rSF") String rSf, @Query("State_Code") String State_code);

    @Multipart
    @POST("Db_v300.php")
    Call<Result> uploadImage(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JsonObject> GetResponseBody(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                     @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("month") String CMonth, @Query("year") String CYr,
                                     @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<JsonObject> DCRSave(@QueryMap Map<String, String> params, @Field("data") String body);


    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/taexecptionapprove")
    Call<JsonObject> DCRSaves(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<Object> GetTPObject(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn,
                             @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300_i.php?")
    Call<Object> GetPJPApproval(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sfCode,
                                                         @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn,
                                                         @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=table/list")
    Call<Object> GettpRespnse(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                              @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("CMonth") String CMonth, @Query("CYr") String CYr,
                              @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<Object> GetTPObject1(@Query("AMod") String Amod, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                              @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn,
                              @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JsonObject> leaveSubmit(@Query("sf_name") String SfName, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                 @Query("State_Code") String StateCode, @Query("desig") String desig, @Field("data") String data);

    @POST("Db_v300.php?axn=get/LeaveAvailabilityCheck")
    Call<Object> remainingLeave(@Query("Year") String Year, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                @Query("rSF") String rSF, @Query("State_Code") String StateCode);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=get/tknPerm")
    Call<Object> availabilityLeave(@Query("PDt") String PDT, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                   @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Field("data") String data);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JsonObject> mmDates(@Query("id") String ID, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String RSF, @Query("State_Code") String StateCode, @Field("data") String data);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=get/calpriod")
    Call<Object> mmDate(@Query("id") String ID, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                        @Query("rSF") String RSF, @Query("State_Code") String StateCode, @Field("data") String data);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=GetMissed_Punch")
    Call<Object> missedPunch(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Field("data") String data);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JsonObject> SubmitmissedPunch(@Query("sf_name") String SFName, @Query("Ekey") String Ekey, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                       @Query("State_Code") String StateCode, @Query("desig") String desig, @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php?")
    Call<Object> GetRouteObject(@QueryMap Map<String, String> params,
                                @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<Object> GetRouteObject(@QueryMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<Object> Tb_Mydayplan(@QueryMap Map<String, String> params, @Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=distlocation/update")
    Call<Object> updateDistLatLng(@QueryMap Map<String, String> params, @Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<Object> Getwe_Status(@Query("Priod") String Amod, @Query("sfCode") String sFCode,
                              @Query("axn") String axn, @Query("Status") String status,
                              @Field("data") String data);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/travelmode")
    Call<ResponseBody> getTravelMode(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/fieldforce_hq")
    Call<ResponseBody> gethq(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/fieldforce_hq")
    Call<JsonArray> getBusTo(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/Expensedate")
    Call<JsonArray> getTADate(@Field("data") String userData);

    @Multipart
    @POST("db_new_activity.php?axn=upload/img")
    Call<ResponseBody> uploadimg(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @Multipart
    @POST("db_new_activity.php?axn=upload/start")
    Call<ResponseBody> uploadkmimg(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/daexp")
    Call<ResponseBody> getDailyAllowance(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/startkmdetails")
    Call<ResponseBody> getStartKmDetails(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/daexp")
    Call<ResponseBody> saveDailyAllowance(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/expsendtoapproval")
    Call<ResponseBody> submitOfApp(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/allowance")
    Call<ResponseBody> getAllowance(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/taapproval")
    Call<ResponseBody> getTAAproval(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/taapprove")
    Call<ResponseBody> saveTAApprove(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=reject/taapprove")
    Call<ResponseBody> rejectTAApprove(@Field("data") String userData);

    /*Done by Karthic */

    /*category*/
    @FormUrlEncoded
    @POST("Db_v301.php?axn=table/list")
    Call<HeaderCat> SubCategory(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                @Query("rSF") String rSF, @Query("State_Code") String StateCode,
                                @Field("data") String data);

    /*submitValue*/
    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JsonObject> submitValue(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                 @Field("data") String data);

    /*submitValue*/
    @FormUrlEncoded
    @POST("Db_v300.php?axn=dcr/save")
    Call<JSONArray> submitValueA(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                 @Field("data") String data);

    /*ReportView*/
    @POST("db_v14.php?axn=get/ViewReport")
    Call<ReportDataList> reportValues(@Query("Sf_code") String sFCode, @Query("fromdate") String fromdate, @Query("todate") String todate);

    /*DateReportView*/
    @POST("db_v14.php?axn=get/ViewReport_Details")
    Call<DateReport> dateReport(@Query("Order_Id") String rsfCode, @Query("Sf_code") String sFCode);

    /*Retailer Details*/

    @FormUrlEncoded
    @POST("Db_v300.php?axn=get/FieldForce_HQ")
    Call<JsonArray> GetHAPLocation(@Query("divisionCode") String disvisonCode, @Query("sf_code") String sFCode, @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=get/FieldForce_HQ")
    Call<Object> getFieldForce_HQ(@Query("divisionCode") String disvisonCode, @Query("sf_code") String sFCode, @Field("data") String data);

    /*Retailer View Details*/
    @POST("Db_v300.php?axn=get/precall")
    Call<RetailerViewDetails> getRetailerDetails(@Query("divisionCode") String divisionCode, @Query("sf_code") String sFCode, @Query("Msl_No") String retailerID);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<Object> Get_Object(@QueryMap Map<String, String> params, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<JsonObject> addNewRetailer(@QueryMap Map<String, String> params, @Field("data") String body);

//    @Multipart
//    @POST("Db_v300.php")
//    Call<JsonObject> addNewRetailer(@QueryMap Map<String, String> params, @Field("data") String body, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=table/list")
    Call<JsonArray> retailerClass(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode, @Query("rSF") String rSF,
                                  @Query("State_Code") String StateCode, @Field("data") String data);

    @POST("Db_V13.php?axn=get/precall")
    Call<RetailerViewDetails> retailerViewDetails(@Query("Msl_No") String retailerID, @Query("divisionCode") String divisionCode, @Query("sfCode") String sfCode);

    @POST("Db_V13.php?axn=GetERTList")
    Call<JsonObject> ERTDetails(@Query("sfCode") String sFCode);

    @FormUrlEncoded
    @POST("Db_V13.php?")
    Call<Object> GetRouteObjects(@QueryMap Map<String, String> params, @Field("data") String data);


    /*Permission Select Hours*/
    @GET("Db_V13.php?")
    Call<JsonObject> permissionHours(@Query("axn") String axn, @Query("start_at") String start_at, @Query("Shift_TimeFlag") String Shift_TimeFlag);

    @FormUrlEncoded
    @POST("Db_v300.php")
    Call<Object> getHolidayStatus(@Query("AMod") String Amod, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode, @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn, @Field("data") String data);


    @FormUrlEncoded
    @POST("db_new_activity.php")
    Call<Object> getHolidayStatuss(@Query("AMod") String Amod, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode, @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn, @Field("data") String data);

    @POST("Db_v300.php?axn=get/track")
    Call<ResponseBody> getMap(@Query("SF_Code") String SfCode, @Query("Dt") String Date);

    @POST("db_new_activity.php?axn=get/expenseapprovallist")
    Call<JsonArray> getApprovalList(@Query("SF_Code") String SF_Code);

    @POST("db_new_activity.php?axn=get/vwexceptionstatus")
    Call<JsonArray> getDaException(@Query("sfCode") String SF_Code);

    @POST("db_new_activity.php?axn=get/expensesubdatestatus")
    Call<JsonArray> getTaViewStatus(@Query("SF_Code") String SF_Code);

    @POST("db_new_activity.php?axn=get/taapprovehistory")
    Call<JsonArray> getTaApprovHistory(@Query("SF_Code") String SF_Code);


    @FormUrlEncoded
    @POST("db_new_activity.php?axn=get/expensesflist")
    Call<JsonArray> getApprovalDisplay(@Field("data") String data);

    @Multipart
    @POST("db_new_activity.php?axn=upload/Taimg")
    Call<ResponseBody> taImage(@Query("Img_U_key") String ImgkeyCode,
                               @Query("U_key") String keyCode,
                               @Query("HeadTravel") String HeadTravel,
                               @Query("Mode") String Mode,
                               @Query("Date") String date,
                               @Query("sfCode") String sfcode,
                               @Query("From") String from,
                               @Query("To") String To,
                               @Part MultipartBody.Part file);


    @Multipart
    @POST("db_new_activity.php?axn=upload/checkinimage")
    Call<ResponseBody> CheckImage(@Query("sfCode") String sfcode,
                                  @Part MultipartBody.Part files);


    @Multipart
    @POST("db_new_activity.php?axn=upload/checkinimage")
    Call<ResponseBody> onTAFileUpload(@Query("sfCode") String sfcode, @Query("FileName") String FileName, @Query("Mode") String Mode,
                                      @Part MultipartBody.Part file);


    @Multipart
    @POST("db_new_activity.php?axn=upload/checkinimage")
    Single<ResponseBody> onFileUpload(@Query("sfCode") String sfcode, @Query("FileName") String FileName, @Query("Mode") String Mode,
                                      @Part MultipartBody.Part file);


    @Multipart
    @POST("db_new_activity.php?axn=upload/checkinimage")
    Call<ResponseBody> outletFileUpload(@Query("sfCode") String sfcode, @Query("FileName") String FileName, @Query("Mode") String Mode,
                                        @Part MultipartBody.Part file);


    @POST("db_new_activity.php?axn=get/TA_Image")
    Call<JsonArray> allPreview(@Query("U_key") String keyCode,
                               @Query("HeadTravel") String HeadTravel,
                               @Query("Mode") String Mode,
                               @Query("Date") String date,
                               @Query("sfCode") String sfcode);

    @POST("db_new_activity.php?axn=delete/ta_image")
    Call<JsonObject> dltePrvws(@Query("U_key") String keyCode,
                               @Query("Img_U_key") String HeadTravel,
                               @Query("Date") String date,
                               @Query("sfCode") String sfcode);

    @POST("Db_V13.php?axn=get/GateEntryHome")
    Call<JsonArray> gteDta(@Query("Sf_code") String sfCode, @Query("TodayDate") String Tdate);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/expLodgeException")
    Call<JsonObject> daExpen(@Field("data") String daDATA);

    @FormUrlEncoded
    @POST("db_new_activity.php?axn=save/editstartactivity")
    Call<JsonObject> upteAllowance(@Field("data") String body);


    @FormUrlEncoded
    @POST("db_v310.php?axn=save/salescalls")
    Call<JsonObject> saveCalls(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/projectionnew")
    Call<JsonObject> saveProjection(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/stockaudit")
    Call<JsonObject> saveStockAudit(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?")
    Call<JsonObject> saveVanSales(@Query("axn") String axn, @Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?")
    Call<JsonObject> savePOSStock(@Query("axn") String axn, @Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);


    @FormUrlEncoded
    @POST("db_v310.php?")
    Call<JsonObject> saveIndent(@Query("axn") String axn, @Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/salesreturn")
    Call<JsonObject> saveSalesReturn(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/posorder")
    Call<JsonObject> savePOS(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);


    @FormUrlEncoded
    @POST("db_v310.php?axn=save/primaryorder")
    Call<JsonObject> savePrimaryOrder(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/posCounterSalesEntry")
    Call<JsonObject> savePOSEntrysales(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/inshopsorder")
    Call<JsonObject>  saveInshopsOrder(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/invoice")
    Call<JsonObject> saveInvoice(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Query("loginType") String loginType, @Field("data") String toString);

    @FormUrlEncoded
    @POST("MyPHP.php?axn=save_complementary_invoice")
    Call<ResponseBody> saveComplementaryInvoice(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Query("loginType") String loginType, @Field("data") String toString);

    @FormUrlEncoded
    @POST("db_v310.php?axn=save/grnentry")
    Call<JsonObject> saveGrn(@Query("divisionCode") String div_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/otherbrandentry")
    Call<JsonObject> saveOtherBrand(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/popentry")
    Call<JsonObject> savePOP(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getDataArrayListA(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getDataArrayListA(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("db_v310.php?axn=get/expensedatedetailsnew")
    Call<JsonObject> getTAdateDetails(@Field("data") String userData);

    /*Devaition Entry*/
    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<Object> GetExceptionRoutes(@QueryMap Map<String, String> params, @Field("data") String data);

    /*Save Devaition Entry*/
    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/exception")
    Call<JsonObject> deviationSave(@Query("sf_name") String SfName, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode, @Query("State_Code") String StateCode, @Query("desig") String desig, @Field("data") String data);
    /*Save Devaition Entry*/
    @FormUrlEncoded
    @POST("Db_v300.php?axn=Save/DivInf")
    Call<JsonObject> blockAppData(@Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/allowance")
    Call<ResponseBody> saveAllowance(@Field("data") String userData);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/recallallowance")
    Call<ResponseBody> RecallSave(@Field("data") String userData);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=update/distlatlng")
    Call<JsonObject> distLatLngUpdate(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/taapprove")
    Call<JsonObject> taApprove(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300.php?axn=get/ondutystat")
    Call<JsonObject> getOnDutyStatus(@Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v300.php?axn=save/ondutyupdate")
    Call<JsonObject> viewStatusUpdate(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/tacumulativeapprove")
    Call<JsonObject> taCumulativeApprove(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300_i.php?axn=approve/pjpdetails")
    Call<JsonObject> pjpApprove(@Query("sfCode") String sFCode, @Query("rSF") String rSF,@Query("Confirmed_Date") String ConfirmedDate,@Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<ResponseBody> updateAllowance(@Query("axn") String axn, @Field("data") String userData);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getAndUpdate(@Query("axn") String axn, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonArray> getLDGAllowance(@Query("axn") String axn, @Field("data") String body);

    @Multipart
    @POST("db_new_activity.php?axn=upload/Taimg")
    Call<ResponseBody> uploadOutletImage(
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<ResponseBody> sendUpldPhotoErrorMsg(@Query("axn") String axn, @Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/qpsentry")
    Call<ResponseBody> submitQPSData(@Field("data") String body);


    @FormUrlEncoded
    @POST("Db_v310.php?axn=get/savepaymententry")
    Call<ResponseBody> submitPayData(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/placeid")
    Call<ResponseBody> submitMarkedData(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=get/placeid")
    Call<ResponseBody> getMarkedData(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=get/orderdetailsfrinv")
    Call<ResponseBody> getInvoiceOrderDetails(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=get/categorywiseretailerdata")
    Call<ResponseBody> getLastThreeMnthsData(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<ResponseBody> GetRouteObject310(@QueryMap Map<String, String> params,
                                         @Field("data") String data);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/coolerinfo")
    Call<JsonObject> approveCIEntry(@Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=approve/qpsentry")
    Call<JsonObject> approveQPSEntry(@Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=save/popapprove")
    Call<JsonObject> approvePOPEntry(@Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<JsonObject> submit(@Query("axn") String axn, @Field("data") String toString);

    @POST("Db_v300.php?")
    Call<ResponseBody> getPendingOutletsCategory(@Query("axn") String axn);

    @POST("Db_v300.php?")
    Call<ResponseBody> getOutletsApprovalHistory(@Query("axn") String axn);

    @POST("Db_v300.php?")
    Call<ResponseBody> getDataPendingOutlets(@Query("axn") String axn, @Query("ListedDrCode") String ListedDrCode);

    @POST("Db_v300.php?")
    Call<ResponseBody> getPendingOutlets(@Query("axn") String axn, @QueryMap Map<String, String> params);

    @POST("copy.php?")
    Call<ResponseBody> getPendingOrdersCount(@QueryMap Map<String, String> params);

    @POST("copy.php?")
    Call<ResponseBody> loadData(@QueryMap Map<String, String> params);

    // <----------MyPHP.php----------->

    @FormUrlEncoded
    @POST("MyPHP.php?")
    Call<ResponseBody> getUniversalData(@QueryMap Map<String, String> params, @Field("data") String data);

    @POST("MyPHP.php?")
    Call<ResponseBody> getUniversalData(@QueryMap Map<String, String> params);

    // - - - - - - - - - - - - - - - - - - - - -

    //    @FormUrlEncoded
    @GET("Db_v300_i.php?")
    Call<ResponseBody> getInshopRetailer(@Query("axn") String axn,
                                         @Query("divisionCode") String divisionCode,
                                         @Query("rSF") String rSF,
                                         @Query("sfCode") String sfCode,
                                         @Query("State_Code") String stateCode,
                                         @Query("c_date") String body);

    @GET("Db_v300_i.php?")
    Call<ResponseBody> getInshopRetailer1(@Query("axn") String axn,
                                          @Query("divisionCode") String divisionCode,
                                          @Query("rSF") String rSF,
                                          @Query("sfCode") String sfCode,
                                          @Query("State_Code") String stateCode,
                                          @Query("c_date") String body);
//                                          @Query("Entry_Type")String type);

    @FormUrlEncoded
    @POST("Db_v300_i.php?")
    Call<ResponseBody> savJSONArray(@Field("data") String data,
                                    @Query("axn") String ax,
                                    @Query("divisionCode") String divisionCode,
                                    @Query("sfCode") String sfCode);

    @FormUrlEncoded
    @POST("Db_v300_i.php?")
    Call<ResponseBody> inshopSave(@Query("divisionCode") String divisionCode,@Query("Sf_code") String Sf_code,@Query("axn") String axn, @Field("data") String body);

    @GET("Db_v300_i.php?axn=get/poscountersales")
    Call<POSDataList> getpos1(@Query("sfCode") String sFCode, @Query("fromdate") String fromdate, @Query("todate") String todate);

    @GET("Db_v300_i.php?axn=get/grn")
    Call<ResponseBody> getgrn(@Query("distributorERP") String sFCode, @Query("fromdate") String fromdate, @Query("todate") String todate);

    @FormUrlEncoded
    @POST("Db_v300_i.php?axn=save/pendinggrn")
    Call<JsonObject> GRNSave(@Query("SFCode") String sFCode, @Query("divCode") String divisionCode, @Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v300_i.php?axn=grnSync")
    Call<JsonObject> GRNSync(@Field("data") String body);

    @FormUrlEncoded
    @POST("Db_v310.php?")
    Call<ResponseBody> universalAPIRequest(@QueryMap Map<String, String> params, @Field("data") String data);

    @POST("jfs/v1/app/authenticate")
    Call<ResponseBody> authenticate(@Header("x-trace-id") String uniqueKey, @Body RequestBody jsonObject);

    @POST("payments/jfs/v1/payments/intent")
    Call<ResponseBody> MakeTransaction(@HeaderMap Map<String, String> headerMap, @Body RequestBody jsonObject);

    @POST("Db_v310.php")
    Call<ResponseBody> primaryNoOrderReasonSubmit(@Query("axn") String axn,
                                                  @Query("reason") String reason,
                                                  @Query("sf_code") String sfCode,
                                                  @Query("erp_code") String distribute_code,
                                                  @Query("distribute_name") String distribute_name,
                                                  @Query("lat") String lat,
                                                  @Query("lan") String lan,
                                                  @Query("time_date") String time_date);

    @POST("Db_v310.php")
    Call<ResponseBody> getPrimaryNoOrderList(@Query("axn") String axn,
                                                   @Query("erp_code") String distribute_code);

    @POST("Db_v310.php")
    Call<ResponseBody> getProcPlant(@Query("axn") String axn);

    @Multipart
    @POST("Db_v310.php")
    Call<ResponseBody> submitProcCollectionCenterLo(@Query("axn") String axn,
                                                    @Query("company") String company,
                                                    @Query("plant") String plant,
                                                    @Query("sap_center_code") String sap_center_code,
                                                    @Query("sap_center_name") String sap_center_name,
                                                    @Query("center_address") String center_address,
                                                    @Query("lactlis_potential_lpd") String lactlis_potential_lpd,
                                                    @Query("no_enrolled_farmers") String no_enrolled_farmers,
                                                    @Query("competitor1") String competitor1,
                                                    @Query("competitor1_txt") String competitor1_txt,
                                                    @Query("active_flag") String active_flag,
                                                    @Query("created_dt") String created_dt,
                                                    @Part MultipartBody.Part image);


    @GET(ApiClient.CONFIG_URL)
    Call<ResponseBody> getBaseConfig();
}