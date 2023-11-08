package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    List<Retailer_Modal_List> Retailer_Modal_ListFilter;
    List<Retailer_Modal_List> mRetailer_Modal_List;
    private int mTabPos = -1;
    private String mSearchText = "", mCategory = "", mCatType = "", mSubCategory = "", mFreezer = "";
    private String mRetType = "1";
    String mActivityName;

    Context mContext;

    public TabAdapter(FragmentManager fm, int tabPos, List<Retailer_Modal_List> retailer_Modal_List, String RetType, Context context, String name, String category, String catType, String subCategory, String mFreezer) {
        super(fm);
        this.mRetailer_Modal_List = retailer_Modal_List;
        this.mRetType = RetType;
        this.mContext = context;
        this.mActivityName = name;
        this.mCategory = category;
        this.mCatType = catType;
        this.mSubCategory = subCategory;
        this.mFreezer = mFreezer;

        Log.v("tabAdapter: ", "pos:" + tabPos);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        OutletFilter(position);
        if (mActivityName.equalsIgnoreCase("Dashboard_Route"))
            fragment = new Dashboard_Route.AllDataFragment(Retailer_Modal_ListFilter, position);
        else
            fragment = new VanSalesDashboardRoute.AllDataFragment(Retailer_Modal_ListFilter, position);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void notifyData(List<Retailer_Modal_List> retailer_Modal_List, int tabPos, String filterText, String RetType, String category, String catType, String subCategory, String mFreezer) {
        this.mTabPos = tabPos;
        this.mRetType = RetType;
        this.mRetailer_Modal_List = retailer_Modal_List;
        this.mSearchText = filterText;
        this.mCategory = category;
        this.mCatType = catType;
        this.mSubCategory = subCategory;
        this.mFreezer = mFreezer;
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = (position == 0) ? "BTG" : position == 1 ? "Invoice" : position == 2 ? "Order" : "No Order";
        OutletFilter(position);
        title = title + "\n"+Retailer_Modal_ListFilter.size();
        return title;
    }


    public void OutletFilter(int flag) {


        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(mContext);
        Retailer_Modal_ListFilter = new ArrayList<>();
        String Route_id = shared_common_pref.getvalue(Constants.Route_Id);

        if (mRetailer_Modal_List != null) {
            String val = shared_common_pref.getvalue(Constants.RETAILER_STATUS);
            String sMode = flag == 0 ? "BTG" : flag == 1 ? "invoice" : flag == 2 ? "order" : "no order";


            for (int i = 0; i < mRetailer_Modal_List.size(); i++) {
                Log.v("categoryTypes:res:", "freezer:" + mFreezer + ":" + mRetailer_Modal_List.get(i).getFreezer_required() + mRetailer_Modal_List.get(i).getCategory_Universe_Id() + " :filter:" + mCatType
                        + " cat:" + mRetailer_Modal_List.get(i).getOutletClass() + " :sub:" + mRetailer_Modal_List.get(i).getSpeciality());

                String outletType = mRetailer_Modal_List.get(i).getType() == null ? "0" : mRetailer_Modal_List.get(i).getType();
                if (val.indexOf(mRetailer_Modal_List.get(i).getId() + sMode) > -1 &&
                        (Route_id.equalsIgnoreCase("") || Route_id.equalsIgnoreCase(mRetailer_Modal_List.get(i).getTownCode())) &&
                        (mRetType.equalsIgnoreCase("") || outletType.equalsIgnoreCase(mRetType))
                        && (mSearchText.equalsIgnoreCase("") || (flag == mTabPos &&
                        (";" + mRetailer_Modal_List.get(i).getName().toLowerCase()).indexOf(";" + mSearchText.toLowerCase()) > -1) ||
                        (flag != mTabPos)) && (Common_Class.isNullOrEmpty(mCategory) || mCategory.equalsIgnoreCase("ALL") ||
                        mCategory.equalsIgnoreCase(mRetailer_Modal_List.get(i).getOutletClass())) &&
                        (mCatType.equalsIgnoreCase("") || (mRetailer_Modal_List.get(i).getCategory_Universe_Id() != null && ((mCatType.contains(mRetailer_Modal_List.get(i).getCategory_Universe_Id())) || (mRetailer_Modal_List.get(i).getCategory_Universe_Id().contains(mCatType)))))
                        && (mSubCategory.equalsIgnoreCase("") || mSubCategory.equalsIgnoreCase("ALL") || (mRetailer_Modal_List.get(i).getSpeciality() != null && mRetailer_Modal_List.get(i).getSpeciality().equalsIgnoreCase(mSubCategory)))
                        && (Common_Class.isNullOrEmpty(mFreezer) || (!Common_Class.isNullOrEmpty(mRetailer_Modal_List.get(i).getFreezer_required()) && mFreezer.equalsIgnoreCase(mRetailer_Modal_List.get(i).getFreezer_required())))) {
                    Retailer_Modal_ListFilter.add(mRetailer_Modal_List.get(i));
                }
            }
        }

        Log.v("Outlet:" + flag, " filtersize:" + Retailer_Modal_ListFilter.size());

    }


}