package com.saneforce.milksales.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.ParentListInterface;
import com.saneforce.milksales.Model_Class.HeaderCat;
import com.saneforce.milksales.Model_Class.HeaderName;
import com.saneforce.milksales.Model_Class.ProceedCartModel;
import com.saneforce.milksales.Model_Class.Product;
import com.saneforce.milksales.Model_Class.ProductUnitBox;
import com.saneforce.milksales.Model_Class.ProductUnitModel;
import com.saneforce.milksales.Model_Class.Product_Array;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.ParentListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCategoryActivity extends AppCompatActivity implements Master_Interface {
    TextView toolHeader, toolTime, toolSlash, toolCutOFF, grandTotal, item_count, txtClosing;
    ImageView imgBack;

    Product_Array product_array;
    ArrayList<String> list;
    RecyclerView mRecyclerView;
    List<String> productHeaderList, productChildList;
    String productID;
    EditText toolSearch;
    Integer productCode;
    ParentListAdapter event_list_parent_adapter;
    ArrayList<Product_Array> Product_Array_List;

    /* Submit button */
    LinearLayout proceedCart;
    int j;
    int sum = 0;

    String time;
    SimpleDateFormat simpleDateFormat;
    Calendar calander;

    ArrayList<String> mResponseProductID;
    JSONObject person1;
    JSONObject PersonObjectArray;
    ArrayList<String> listV = new ArrayList<>();
    String JsonDatas;
    String SF_CODE, DIVISION_CODE, CUTT_OFF_CODE;

    String seachName;

    List<HeaderName> headerNameArrayList;
    List<Product> eventsArrayList;
    Timestamp timestamp, ts2;
    int b3;
    /*CHECKING*/
    ArrayList<String> mHeaderNameValue;
    LinearLayout bottomLinear;
    Shared_Common_Pref shared_common_pref;


    /*for product unit*/
    CustomListViewDialog customDialog;
    Common_Model mCommon_model_spinner;
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Type userType;
    Gson gson;
    List<ProductUnitModel> mProductUnitModel;
    String productUnitId, productUnitType = "";
    String ProductModelId;

    ProceedCartModel mProceedCartModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_category);

        getToolbar();

        @SuppressLint("WrongConstant")
        SharedPreferences sh
                = getSharedPreferences("MyPrefs",
                MODE_APPEND);
        SF_CODE = Shared_Common_Pref.Sf_Code;
        DIVISION_CODE = Shared_Common_Pref.Div_Code;
        CUTT_OFF_CODE = Shared_Common_Pref.StateCode;

        shared_common_pref = new Shared_Common_Pref(this);
        gson = new Gson();
        //Log.e("CAT_Details", SF_CODE);
        //Log.e("CAT_Details", DIVISION_CODE);
        //Log.e("CAT_Details", CUTT_OFF_CODE);

        SubCategoryHeader();
        currentTime();
        checking();
        /* 2020-4-9 00:00:00*/
        Product_Array_List = new ArrayList<Product_Array>();
        Product_Array_List.clear();

        list = new ArrayList<>();
        productHeaderList = new ArrayList<>();
        productChildList = new ArrayList<>();
        mResponseProductID = new ArrayList<>();

        mHeaderNameValue = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        grandTotal = (TextView) findViewById(R.id.total_amount);
        item_count = (TextView) findViewById(R.id.item_count);
        mRecyclerView.setNestedScrollingEnabled(false);
        proceedCart = (LinearLayout) findViewById(R.id.Linear_proceed_cart);


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        bottomLinear = (LinearLayout) findViewById(R.id.bottom_linear);
        proceedCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String STR = grandTotal.getText().toString();
                //Log.e("STRVALUE", STR);
                if (STR.equals("0")) {
                    Toast.makeText(OrderCategoryActivity.this, "Please choose to cart", Toast.LENGTH_SHORT).show();
                } else {
                    SaveDataValue();
                }

            }
        });


    }

    /*Toolbar*/
    public void getToolbar() {

        toolHeader = (TextView) findViewById(R.id.toolbar_title);
        toolHeader.setText(R.string.pri_orders);
        toolTime = (TextView) findViewById(R.id.current_time);
        toolSlash = (TextView) findViewById(R.id.slash);
        toolSlash.setText("/");
        toolCutOFF = (TextView) findViewById(R.id.cut_off_time);
        txtClosing = (TextView) findViewById(R.id.text_closing);
        txtClosing.setText("Order Closing Time");
        txtClosing.setVisibility(View.VISIBLE);
        toolSearch = (EditText) findViewById(R.id.toolbar_search);
        toolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                event_list_parent_adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void checking() {
        timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("format" + timestamp);

        ts2 = Timestamp.valueOf("2020-11-03 15:56:00");
        //compares ts1 with ts2
        b3 = timestamp.compareTo(ts2);
        if (b3 == 0) {
            // bottomLinear.setVisibility(View.GONE);
            //  startActivity(new Intent(CategoryActivity.this, TimeOut.class));
        } else if (b3 > 0) {
            //   bottomLinear.setVisibility(View.GONE);
            //  startActivity(new Intent(CategoryActivity.this, TimeOut.class));
            System.out.println("TimeSpan1 value is greater");
        } else {
            //  bottomLinear.setVisibility(View.VISIBLE);
            System.out.println("TimeSpan2 value is greater");
        }

    }


    /*Current Time for Cuttoff Process*/
    public void currentTime() {

        String setTIME = "15:56:00";
        toolCutOFF.setText(setTIME);
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));
                if (toolTime.getText().equals(setTIME)) {
                    // Toast.makeText(getApplicationContext(), "Your data has been submitted successfully", Toast.LENGTH_LONG).show();
                    //    bottomLinear.setVisibility(View.GONE);
                    //   startActivity(new Intent(CategoryActivity.this, TimeOut.class));
                } else {

                }
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    /*subCategory header Listview*/
    public void SubCategoryHeader() {
        //Log.e("CAT_Details", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        //Log.e("CAT_Details", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
        //Log.e("CAT_Details", shared_common_pref.getvalue(Shared_Common_Pref.StateCode));
        long startTime = System.currentTimeMillis();

        String tempalteValue = "{\"tableName\":\"category_master\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HeaderCat> ca = apiInterface.SubCategory(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", tempalteValue);

        ca.enqueue(new Callback<HeaderCat>() {
            @Override
            public void onResponse(Call<HeaderCat> call, Response<HeaderCat> response) {
                if (response.isSuccessful()) {

                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

                    HeaderCat headerCat = response.body();
                    //Log.e("RESPOSNE", headerCat.toString());
                    headerNameArrayList = headerCat.getData();
                    HeaderName mHeaderName = new HeaderName();
                    for (int i = 0; i < headerNameArrayList.size(); i++) {
                        String str = headerNameArrayList.get(i).getName();
                        mHeaderName.setName(str);
                        mHeaderNameValue.add(str);
                        //Log.e("HEADER_NAME", String.valueOf(mHeaderNameValue));
                        eventsArrayList = headerNameArrayList.get(i).getProduct();
                        for (int j = 0; j < eventsArrayList.size(); j++) {
                            productUnitId = String.valueOf(eventsArrayList.get(j).getId());
                            //Log.e("Product_code_value", productUnitId);

                        }
                        childListData(eventsArrayList, headerCat, headerNameArrayList);

                    }
                }
            }

            @Override
            public void onFailure(Call<HeaderCat> call, Throwable t) {
                //Log.e("RESPOSNE", "response.body().toString()");
            }
        });
    }


    // ChildListAdapter
    private void childListData(List<Product> eventsArrayLists, HeaderCat headerCat, List<HeaderName> headerNameArrayLists) {


        for (j = 0; j < eventsArrayLists.size(); j++) {
            productID = eventsArrayLists.get(j).getId();
            productCode = eventsArrayLists.get(j).getProductCatCode();
            mResponseProductID.add(String.valueOf(productID));
            seachName = eventsArrayLists.get(j).getName();
            mHeaderNameValue.add(seachName);

            //Log.e("PRODUCT_TYPE_VALUE", "PRODUCT_TYPE" + productUnitType);
            event_list_parent_adapter = new ParentListAdapter(headerCat, headerNameArrayLists, eventsArrayLists, OrderCategoryActivity.this, mHeaderNameValue, modelRetailDetails, new ParentListInterface() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClickParentInter(String value, int totalValue, String itemID, Integer positionValue, String productName, String productCode, Integer productQuantiy, String catImage, String catName, String productUnit, Integer Value) {

                    if (Value == 1) {
                        getWindow().getDecorView().clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
                    }

                    if (Product_Array_List.size() == 0) {
                        sum = sum + productQuantiy * Integer.parseInt(productCode);
                        grandTotal.setText("" + sum);
                        shared_common_pref.save("Total_amount", String.valueOf(sum));
                        item_count.setText("Items:" + "1");
                        Product_Array_List.add(new Product_Array(itemID, productName, productQuantiy, productQuantiy * Integer.parseInt(productCode), Integer.parseInt(productCode), catImage, catName, productUnit));
                        System.out.println("First_Product_Added" + Product_Array_List.size());

                    } else {
                        System.out.println("PRODUCT_Array_SIzeElse" + Product_Array_List.size());
                        int Total_Size = Product_Array_List.size();
                        for (int i = 0; i < Total_Size; i++) {
                            product_array = Product_Array_List.get(i);
                            if (itemID == product_array.getProductcode()) {
                                System.out.println("Product_Code" + itemID);
                                System.out.println("Existing_Code" + product_array.getProductcode());
                                System.out.println("Position_Count" + i);
                                Product_Array_List.remove(i);
                                System.out.println("PRODUCT_Array_SIZE" + Product_Array_List.size());
                                Total_Size = Total_Size - 1;
                                System.out.println("AlreadyExist" + product_array.getProductcode());
                            }

                        }

                        Product_Array_List.add(new Product_Array(itemID, productName, productQuantiy, productQuantiy * Integer.parseInt(productCode), Integer.parseInt(productCode), catImage, catName, productUnit));
                        int sum = 0;

                        //Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(Product_Array_List));
                        for (int i = 0; i < Product_Array_List.size(); i++) {
                            sum = sum + Product_Array_List.get(i).getSampleqty();
                            System.out.println(" " + "  " + Product_Array_List.get(i).getProductname() + "Qty" + "  " + Product_Array_List.get(i).getProductqty() + "SampleQty" + "  " + Product_Array_List.get(i).getSampleqty());
                            if (Product_Array_List.get(i).getProductqty() == 0) {

                                Product_Array_List.remove(i);
                            }

                        }
                        grandTotal.setText("" + sum);

                        item_count.setText("Items:" + Product_Array_List.size());
                        //Log.e("DATA_CHECKING", String.valueOf(Product_Array_List.size()));
                    }



                    /*Product_code*/
                    List<JSONObject> myJSONObjects = new ArrayList<JSONObject>(Product_Array_List.size());


                    JSONArray personarray = new JSONArray();
                    PersonObjectArray = new JSONObject();
                    JSONObject fkeyprodcut = new JSONObject();

                    for (int z = 0; z < Product_Array_List.size(); z++) {
                        person1 = new JSONObject();


                        try {

                            //adding items to first json object
                            person1.put("product_Name", Product_Array_List.get(z).getProductname());
                            person1.put("product_code", Product_Array_List.get(z).getProductcode());
                            person1.put("Qty", Product_Array_List.get(z).getProductqty());
                            person1.put("PQty", Product_Array_List.get(z).getProductRate());
                            person1.put("cb_qty", 0);
                            person1.put("free", 0);
                            person1.put("Product_Sale_Unit", Product_Array_List.get(z).getProductUnit());
                            person1.put("f_key", fkeyprodcut);
                            fkeyprodcut.put("activity_stockist_code", "Activity_Stockist_Report");

                            myJSONObjects.add(person1);
                            listV.add(String.valueOf((person1)));
                            personarray.put(person1);
                            PersonObjectArray.put("Activity_Stk_POB_Report", personarray);
                            String JsonData = PersonObjectArray.toString();

                            System.out.println("Activity_Stk_POB_Report: " + JsonData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    /*Sending to view cart*/
                    ArrayList<JSONObject> reportJSON = new ArrayList<>();
                    JSONObject ReportObjectArray = new JSONObject();
                    JSONArray jsonElements = new JSONArray();
                    JSONObject js;

                    for (int z = 0; z < Product_Array_List.size(); z++) {

                        if (Product_Array_List.get(z).getProductqty() != 0) {
                            js = new JSONObject();

                            try {
                                js.put("product_Name", Product_Array_List.get(z).getProductname());
                                js.put("product_code", Product_Array_List.get(z).getProductcode());
                                js.put("Qty", Product_Array_List.get(z).getProductqty());
                                js.put("Samp", Product_Array_List.get(z).getProductRate());
                                js.put("Cat_Image", Product_Array_List.get(z).getCatImage());
                                js.put("Cat_Name", Product_Array_List.get(z).getCatName());
                                reportJSON.add(js);
                                jsonElements.put(js);
                                ReportObjectArray.put("Activity_Stk_POB_Report", jsonElements);
                                JsonDatas = ReportObjectArray.toString();
                                System.out.println("Activity_Stk_POB_Report:sssssssssss " + JsonDatas);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

                @Override
                public void onProductUnit(String productSaleUnit, String productItemId) {
                 /*   modelRetailDetails.clear();
                    RetailerType(productItemId);*/

                    //Log.e("OrderCategoryActivity", productSaleUnit + " " + productItemId);

                }

                @Override
                public void ProductImage(String ImageUrl) {
                    //Log.e("Image_URl", ImageUrl);
                    Intent intent = new Intent(OrderCategoryActivity.this, ProductImageView.class);
                    intent.putExtra("ImageUrl", ImageUrl);
                    startActivity(intent);
                }


            });

            mRecyclerView.setAdapter(event_list_parent_adapter);

        }


    }




    /*SAVE DATA FROM */

    public void SaveDataValue() {

        Gson gson = new Gson();

        String jsonCars = gson.toJson(Product_Array_List);

        //Log.e("PRODUCCT_LIST", String.valueOf(Product_Array_List));
        //Log.e("PRODUCCT_LIST", "fg");

        Intent mIntent = new Intent(OrderCategoryActivity.this, ViewCartActivity.class);
        mIntent.putExtra("list_as_string", jsonCars);
        startActivity(mIntent);

    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    AlertDialogBox.showDialog(OrderCategoryActivity.this, "", "Do you want to exit?", "Yes", "NO", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                           /* SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                            Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                            if (CheckIn == true) {
                                Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                                Dashboard.putExtra("Mode", "CIN");
                                startActivity(Dashboard);
                            } else
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));*/
                            onSuperBackPressed();

                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                        }
                    });
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }




    /*Product Unit peice */


    public void RetailerType(String productItemId) {
        String commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/UnitConversion");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiInterface.GetRouteObject(QueryString, commonworktype);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Log.e("MAsterSyncView_Result", response.body() + "");
                System.out.println("Route_Matser" + response.body().toString());
                //Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));


                userType = new TypeToken<ArrayList<ProductUnitModel>>() {
                }.getType();
                mProductUnitModel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < mProductUnitModel.size(); i++) {

                    ProductModelId = String.valueOf(mProductUnitModel.get(i).getProductCode());

                    //Log.e("Inner_id_123456", productItemId);
                    //Log.e("Inner_id", ProductModelId);
                    if (productItemId.equals(ProductModelId)) {
                        String name = mProductUnitModel.get(i).getName();
                        mCommon_model_spinner = new Common_Model(ProductModelId, name, "flag");
                        //Log.e("LeaveType_Request", ProductModelId);
                        //Log.e("LeaveType_Request", name);
                        modelRetailDetails.add(mCommon_model_spinner);
                    }


                }

                customDialog = new CustomListViewDialog(OrderCategoryActivity.this, modelRetailDetails, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {

            productUnitType = myDataset.get(position).getName();
            //Log.e("PRODUCT_TYPE_VALUE_A", productUnitType);
            //Log.e("PRODUCT_TYPE_VALUE_A", String.valueOf(myDataset.size()));
            ProductUnitBox productUnitBox = new ProductUnitBox(productUnitType, myDataset.get(position).getId(), 0);


            //  modelRetailDetails.clear();
        }
    }
}