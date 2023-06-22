package com.saneforce.milksales.MVP;

import android.util.Log;

import com.saneforce.milksales.Model_Class.Route_Master;

import java.util.ArrayList;

public class MasterSync_Implementations  implements Main_Model.presenter, Main_Model.GetRoutemastersyncResult.OnFinishedListenerroute {

    private Main_Model.MasterSyncView mainView;

    private Main_Model.GetRoutemastersyncResult GetRoutemastersyncResult;
    public MasterSync_Implementations(Main_Model.MasterSyncView mainView,Main_Model.GetRoutemastersyncResult routemastersyncResult) {
        this.mainView = mainView;

        GetRoutemastersyncResult=routemastersyncResult;
        Log.e("MainPresenterImpl","MainPresenterImpl");
    }

    @Override
    public void onDestroy() {

        mainView = null;

    }
    @Override
    public void onRefreshButtonClick() {

    }

    @Override
    public void requestDataFromServer() {
        Log.e("Send_requestfromserver","Send data");

        GetRoutemastersyncResult.GetRouteResult(this);
        //GetRoutemastersyncResult.g(this);
    }

    @Override
    public void onFinishedroute(ArrayList<Route_Master> noticeArrayList) {
        if(mainView != null){
            mainView.setDataToRoute(noticeArrayList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFinishedrouteObject(Object noticeArrayList, int position) {
        mainView.setDataToRouteObject(noticeArrayList,position);
        mainView.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        if(mainView != null){
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}