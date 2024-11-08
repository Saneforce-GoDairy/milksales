package com.saneforce.godairy.Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Model_Class.Approval;
import com.saneforce.godairy.Model_Class.DateReport;
import com.saneforce.godairy.Model_Class.HeaderCat;
import com.saneforce.godairy.Model_Class.Location;
import com.saneforce.godairy.Model_Class.Model;
import com.saneforce.godairy.Model_Class.POSDataList;
import com.saneforce.godairy.Model_Class.ReportDataList;
import com.saneforce.godairy.Model_Class.RetailerViewDetails;
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
import retrofit2.http.FieldMap;
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
    @POST("Db_v310.php?")
    Call<JsonObject> getData310List(@Query("axn") String axn, @Query("divisionCode") String divisionCode, @Query("sfCode") String Sf_code, @Query("rSF") String rSF, @Query("State_Code") String State_code, @Query("desig") String desig, @Field("data") String body);

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

    @Multipart
    @POST("Db_v300.php")
    Call<Result> addImage(@Part MultipartBody.Part file);

    @POST("Db_v300.php")
    Call<ResponseBody> changePassword(@Query("axn") String axn,
                                      @Query("sf_code") String sf_code,
                                      @Query("Mode") String Mode,
                                      @Query("div_Code") String divisionCode,
                                      @Query("old_password") String old_password,
                                      @Query("new_password") String new_password);

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
    @POST("Db_v300_usha.php")
    Call<JsonObject> dayreport(@Query("sf_name") String SfName, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode);

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

    @FormUrlEncoded
    @POST("MyPHP.php?")
    Call<ResponseBody> getDataFromMyPHP(@FieldMap Map<String, Object> params);

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

    @POST("Procurement.php")
    Call<ResponseBody> getProcPlant(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getProcPlant2(@Query("axn") String axn,
                                    @Query("company") String company);

    @Multipart
    @POST("Procurement.php")
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

    @POST("Procurement.php")
    Call<ResponseBody> getProcCenterList(@Query("axn") String axn);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcFarmerCreation(@Query("axn") String axn,
                                                    @Query("center") String center,
                                                    @Query("farmer_gategory") String plant,
                                                    @Query("farmer_name") String farmer_name,
                                                    @Query("farmer_addr") String farmer_addr,
                                                    @Query("phone_number") String phone_number,
                                                    @Query("pin_code") String pin_code,
                                                    @Query("cow_total") String cow_total,
                                                    @Query("buffalo_total") String buffalo_total,
                                                    @Query("cow_available_ltrs") String cow_available_ltrs,
                                                    @Query("buffalo_available_ltrs") String buffalo_available_ltrs,
                                                    @Query("milk_supply_company") String milk_supply_company,
                                                    @Query("interested_supply") String interested_supply,
                                                    @Query("active_flag") String active_flag,
                                                    @Query("created_dt") String created_dt,
                                                    @Part MultipartBody.Part image);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcAgronomist(@Query("axn") String axn,
                                                @Query("company") String company,
                                                @Query("plant") String plant,
                                                @Query("center_name") String center_name,
                                                @Query("farmer_name") String farmer_name,
                                                @Query("product_type") String product_type,
                                                @Query("teat_dip") String teat_dip,
                                                @Query("service_type") String service_type,
                                                @Part MultipartBody.Part image1,
                                                @Part MultipartBody.Part image2,
                                                @Query("fodder_dev_acres") String fodder_dev_acres,
                                                @Part MultipartBody.Part image3,
                                                @Query("farmer_enrolled") String farmer_enrolled,
                                                @Query("farmer_inducted") String farmer_inducted,
                                                @Query("active_flag") String active_flag,
                                                @Query("created_dt") String created_dt);


    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcVeterinary(@Query("axn") String axn,
                                            @Query("company") String company,
                                            @Query("plant") String plant,
                                            @Query("center_name") String center_name,
                                            @Query("farmer_name") String farmer_name,
                                            @Query("service_type") String service_type,
                                            @Part MultipartBody.Part image1,
                                            @Query("seed_sale") String seed_sale,
                                            @Query("mineral_mixture") String mineral_mixture,
                                            @Query("fodder_setts_sale_kg") String fodder_setts_sale_kg,
                                            @Query("cattle_feed_order_kg") String cattle_feed_order_kg,
                                            @Query("teat_dip_cup") String teat_dip_cup,
                                            @Query("evm_treatment") String evm_treatment,
                                            @Part MultipartBody.Part image2,
                                            @Query("case_type") String case_type,
                                            @Query("identified_farmer_count") String identified_farmer_count,
                                            @Query("farmer_enrolled") String farmer_enrolled,
                                            @Query("farmer_inducted") String farmer_inducted,
                                            @Query("active_flag") String active_flag,
                                            @Query("created_dt") String created_dt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcAIT(@Query("axn") String axn,
                                     @Query("company") String company,
                                     @Query("plant") String plant,
                                     @Query("center_name") String center_name,
                                     @Query("farmer_name_code") String farmer_name_code,
                                     @Query("breed_name") String breed_name,
                                     @Part MultipartBody.Part image1,
                                     @Query("service_type_ai") String service_type_ai,
                                     @Query("service_type2") String service_type2,
                                     @Query("pd_verification") String pd_verification,
                                     @Query("calfbirth_verification") String calfbirth_verification,
                                     @Query("mineral_mixture_kg") String mineral_mixture_kg,
                                     @Query("seed_sales") String seed_sales,
                                     @Query("active_flag") String active_flag,
                                     @Query("created_dt") String created_dt);

    @POST("Procurement.php")
    Call<ResponseBody> submitProcAgentVisit(@Query("axn") String axn,
                                     @Query("visit_agent") String visit_agent,
                                     @Query("company") String company,
                                     @Query("total_milk_available") String total_milk_available,
                                     @Query("our_company_ltrs") String our_company_ltrs,
                                     @Query("competitor_rate") String competitor_rate,
                                     @Query("our_company_rate") String our_company_rate,
                                     @Query("demand") String demand,
                                     @Query("supply_start_dt") String supply_start_dt,
                                     @Query("active_flag") String active_flag,
                                     @Query("created_dt") String created_dt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcQuality(@Query("axn") String axn,
                                            @Query("company") String company,
                                            @Query("plant") String plant,
                                            @Query("mass_balance") String mass_balance,
                                            @Query("milk_collection") String milk_collection,
                                            @Part MultipartBody.Part image1,
                                            @Part MultipartBody.Part image2,
                                            @Query("mbrt") String mbrt,
                                            @Query("rejection") String rejection,
                                            @Query("spl_cleaning") String spl_cleaning,
                                            @Query("cleaning_efficiency") String cleaning_efficiency,
                                            @Query("vehicle_with_hood") String vehicle_with_hood,
                                            @Part MultipartBody.Part image3,
                                            @Query("vehicle_without_hood") String vehicle_without_hood,
                                            @Part MultipartBody.Part image4,
                                            @Query("chemicals") String chemicals,
                                            @Query("stock") String stock,
                                            @Query("milk") String milk,
                                            @Query("awareness_program") String awareness_program,
                                            @Query("no_of_fat") String no_of_fat,
                                            @Query("no_of_snf") String no_of_snf,
                                            @Query("no_of_weight") String no_of_weight,
                                            @Query("active_flag") String active_flag,
                                            @Query("created_dt") String created_dt
                                         );
    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcMaintenance(@Query("axn") String axn,
                                             @Query("company") String company,
                                             @Query("plant") String plant,
                                             @Query("equipment") String equipment,
                                             @Query("repair_type") String repair_type,
                                             @Query("active_flag") String active_flag,
                                             @Query("created_dt") String created_dt,
                                             @Part MultipartBody.Part image1,
                                             @Query("others") String others);

    @POST("Procurement.php")
    Call<ResponseBody> submitProcAsset(@Query("axn") String axn,
                                             @Query("company") String company,
                                             @Query("plant") String plant,
                                             @Query("asset_type") String asset_type,
                                             @Query("comments") String comments,
                                             @Query("active_flag") String active_flag,
                                             @Query("created_dt") String created_dt
    );

    @POST("Procurement.php")
    Call<ResponseBody> getAgronomistReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getAITReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getVeterinaryReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getQualityReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getMaintenanceIssueReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getMaintenanceRegularReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getExistingAgentReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getCollectionCenterReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getAssetReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getFarmerCreationReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getSubDivision(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getStates(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getDistricts(@Query("axn") String axn,
                                    @Query("stateCode") String stateCode);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcMaintenRegular(@Query("axn") String axn,
                                                @Query("company") String company,
                                                @Query("plant") String plant,
                                                @Query("bmc_hrs_run") String bmc_hrs_run,
                                                @Query("bmc_volume_coll") String bmc_volume_coll,
                                                @Query("cc_hrs_running") String cc_hrs_running,
                                                @Query("cc_volume_coll") String cc_volume_coll,
                                                @Query("ibt_running_hrs") String ibt_running_hrs,
                                                @Query("dg_set_running") String dg_set_running,
                                                @Part MultipartBody.Part image,
                                                @Query("power_factor") String power_factor,
                                                @Query("pipeline_condition") String pipeline_condition,
                                                @Query("leakage") String leakage,
                                                @Query("scale") String scale,
                                                @Query("per_book") String per_book,
                                                @Query("physical") String physical,
                                                @Query("etp") String etp,
                                                @Query("hot_water") String hot_water,
                                                @Query("factory_license_ins") String factory_license_ins,
                                                @Query("active_flag") String active_flag,
                                                @Query("created_dt") String created_dt,
                                                @Part MultipartBody.Part image1,
                                                @Part MultipartBody.Part image2
    );

    @POST("Procurement.php")
    Call<ResponseBody> submitProcExistingCenterVist(@Query("axn") String axn,
                                                @Query("pouring_act") String pouring_act,
                                                @Query("opening_time") String opening_time,
                                                @Query("closing_time") String closing_time,
                                                @Query("no_of_farmer") String no_of_farmer,
                                                @Query("volume") String volume,
                                                    @Query("avg_fat") String avg_fat,
                                                    @Query("avg_snf") String avg_snf,
                                                    @Query("avg_rate") String avg_rate,
                                                    @Query("cans_load") String cans_load,
                                                    @Query("cans_returned") String cans_returned,
                                                    @Query("cattle_feed") String cattle_feed,
                                                    @Query("other_stock") String other_stock,
                                                    @Query("echo_milk_clean_activity") String echo_milk_clean_activity,
                                                    @Query("machine_condition") String machine_condition,
                                                    @Query("loan_farmer_issue") String loan_farmer_issue,
                                                    @Query("issue_frm_farmer_side") String issue_frm_farmer_side,
                                                    @Query("asset_verification") String asset_verification,
                                                    @Query("rename_village") String rename_village,
                                                    @Query("active_flag") String active_flag,
                                                    @Query("created_dt") String created_dt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcExistingFarmerVisitSka(@Query("axn") String str,
                                                        @Part MultipartBody.Part audio,
                                                        @Part MultipartBody.Part image,
                                                        @Query("customer") String customer,
                                                        @Query("customer_details") String customer_details,
                                                        @Query("purpose_visit") String purpose_visit,
                                                        @Query("price") String price,
                                                        @Query("asset") String asset,
                                                        @Query("cans") String cans,
                                                        @Query("remarks_type") String remarks_type,
                                                        @Query("remarks_text") String remarks_text,
                                                        @Query("active_flag") String active_flag,
                                                        @Query("created_dt") String created_dt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> submitProcFarmerCreationSka(@Query("axn") String str,
                                                   @Part MultipartBody.Part audio,
                                                   @Part MultipartBody.Part image,
                                                   @Query("name") String name,
                                                   @Query("village") String village,
                                                   @Query("type") String type,
                                                   @Query("competitor") String competitor,
                                                   @Query("remarks_type") String remarks_type,
                                                   @Query("remarks_text") String remarks_text,
                                                   @Query("active_flag") String active_flag,
                                                   @Query("created_dt") String created_dt);

    @POST("Procurement.php")
    Call<ResponseBody> getProcCustomFormModule(@Query("axn") String axn, @Query("isPrimary") int isPrimary);

    @POST("Procurement.php")
    Call<ResponseBody> getProcCustomFormFieldLists(@Query("axn") String axn,
                                                   @Query("module_id") String module_id);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> save1JSONArray(@Query("axn") String axn,
                                      @Part("data")RequestBody data,
                                      @Query("divisionCode") String divisionCode,
                                      @Query("sfCode") String sfCode);

    @POST("Procurement.php")
    Call<ResponseBody> getCustomFormReportsModuleList(@QueryMap Map<String,Object>queryParrams);

    @POST("Procurement.php")
    @FormUrlEncoded
    Call<ResponseBody> getCustomFormDataPreview(@QueryMap Map<String, Object> queryParrams,
                                    @FieldMap Map<String, Object> fieldParrams);

    @POST("Procurement.php")
    Call<ResponseBody>getCustomFormMater(@QueryMap Map<String,Object>queryParrams);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> saveProAgent(@Query("axn") String str,
                                    @Part MultipartBody.Part agent,
                                    @Query("agent_name") String agent_name,
                                    @Query("state") String state,
                                    @Query("district") String district,
                                    @Query("town") String town,
                                    @Query("coll_center") String coll_center,
                                    @Query("ag_category") String ag_category,
                                    @Query("company") String company,
                                    @Query("addr") String addr,
                                    @Query("pin_code") String pin_code,
                                    @Query("city") String city,
                                    @Query("mobile_no") String mobile_no,
                                    @Query("email") String email,
                                    @Query("incentive_amt") String incentive_amt,
                                    @Query("cartage_amt") String cartage_amt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> updateProAgent(@Query("axn") String str,
                                      @Query("id") String id,
                                    @Part MultipartBody.Part agent,
                                    @Query("agent_name") String agent_name,
                                    @Query("state") String state,
                                    @Query("district") String district,
                                    @Query("town") String town,
                                    @Query("coll_center") String coll_center,
                                    @Query("ag_category") String ag_category,
                                    @Query("company") String company,
                                    @Query("addr") String addr,
                                    @Query("pin_code") String pin_code,
                                    @Query("city") String city,
                                    @Query("mobile_no") String mobile_no,
                                    @Query("email") String email,
                                    @Query("incentive_amt") String incentive_amt,
                                    @Query("cartage_amt") String cartage_amt);


    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> saveProFarmerCrea2(@Query("axn") String str,
                                    @Part MultipartBody.Part agent,
                                          @Query("farmer_name") String farmer_name,
                                    @Query("state") String state,
                                    @Query("district") String district,
                                    @Query("town") String town,
                                          @Query("coll_center") String coll_center,
                                          @Query("fa_category") String fa_category,
                                          @Query("addr") String addr,
                                          @Query("pin_code") String pin_code,
                                          @Query("city") String city,
                                          @Query("mobile_no") String mobile_no,
                                          @Query("email") String email,
                                          @Query("incentive_amt") String incentive_amt,
                                          @Query("cartage_amt") String cartage_amt);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> updateProFarmer(@Query("axn") String str,
                                       @Part MultipartBody.Part farmerImg,
                                       @Query("id") String id,
                                       @Query("farmer_name")String farmer_name,
                                       @Query("state") String state,
                                       @Query("district") String district,

                                       @Query("town")String town,
                                       @Query("coll_center") String coll_center,
                                       @Query("fa_category") String fa_category,

                                       @Query("addr") String addr,
                                       @Query("pin_code") String pin_code,

    @Query("city") String city,
                                       @Query("mobile_no") String mobile_no,

                                       @Query("email") String email,
                                       @Query("incentive_amt") String incentive_amt,
                                       @Query("cartage_amt") String cartage_amt);

    @POST("Procurement.php")
    Call<ResponseBody> saveProcMilkCollEntry(@Query("axn") String axn,
                                             @Query("session") String session,
                                             @Query("milk_type") String milk_type,
                                             @Query("customer_name") String customer_name,
                                             @Query("customer_no") String customer_no,
                                             @Query("cans") String cans,
                                             @Query("milk_weight") String milk_weight,
                                             @Query("total_milk_qty") String total_milk_qty,
                                             @Query("milk_sample_no") String milk_sample_no,
                                             @Query("fat") String fat,
                                             @Query("snf") String snf,
                                             @Query("clr") String clr,
                                             @Query("milk_rate") String milk_rate,
                                             @Query("total_milk_amt") String total_milk_amt,
                                             @Query("date") String date,
                                             @Query("active_flag") String active_flag,
                                             @Query("coll_entry_date") String coll_entry_date);

    @POST("Procurement.php")
    Call<ResponseBody> getAgentReport(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getCustomers(@Query("axn") String axn);

    @POST("Procurement.php")
    Call<ResponseBody> getMilkColl(@Query("axn") String axn);

    @Multipart
    @POST("Procurement.php")
    Call<ResponseBody> proCollCenterCr(@Query("axn") String axn,
                                       @Part MultipartBody.Part centerImg,
                                       @Query("center_name") String center_name,
                                       @Query("state")String state,
                                       @Query("district") String district,
                                       @Query("plant")String plant,
                                       @Query("business_addr")String business_addr,
                                       @Query("owner_name")String owner_name,
                                       @Query("owner_addr1")String owner_addr1,
                                       @Query("owner_pincode")String owner_pincode,
                                       @Query("mobile")String mobile,
                                       @Query("email")String email);

    @GET(ApiClient.CONFIG_URL)
    Call<ResponseBody> getBaseConfig();

    @POST("Db_v310.php?")
    Call<ResponseBody>getPendPayDets(@QueryMap Map<String,Object>queryParrams);

    @Multipart
    @POST("Db_v300.php?")
    Call<ResponseBody> getResponses(@Part("data") RequestBody data,
                                    @QueryMap Map<String, Object> queryParrams);
    @FormUrlEncoded
    @POST("db_v310.php?axn=save/vansales")
    Call<JsonObject> saveVanInvoice(@Query("divisionCode") String div_code, @Query("Sf_code") String sf_code, @Query("loginType") String loginType, @Field("data") String toString);

    @FormUrlEncoded
    @POST("Db_v310.php?axn=get/vanapprovedata")
    Call<ResponseBody> getVanApproveData(@Field("data") String body);

    @POST("Procurement.php")
    Call<ResponseBody> getRateCardPrice(@Query("axn") String axn,
                                        @Query("state_code") String State_Code,
                                        @Query("milk_type") String milk_type);
}