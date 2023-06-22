package com.saneforce.milksales.SFA_Activity;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by thiru on 03/03/2021.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {
    void inject(Outlet_Info_Activity activity);
    void inject(Dist_Locations activity);
    void inject(Order_Category_Select activity);
    void inject(Reports_Outler_Name activity);
    void inject(Outlet_Report_View activity);
    void inject(Outet_Report_Details activity);
    void inject(Nearby_Outlets activity);
    void inject(Dashboard_Route activity);
}
