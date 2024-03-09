package com.webmintinfotech.ecom.base;

import android.app.Application;
import co.paystack.android.PaystackSdk;
import com.webmintinfotech.ecom.R;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class BaseApplication extends Application {

    private static BaseApplication app;

    public static BaseApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        PaystackSdk.initialize(getApplicationContext());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Poppins-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()))
                .build());
    }
}
