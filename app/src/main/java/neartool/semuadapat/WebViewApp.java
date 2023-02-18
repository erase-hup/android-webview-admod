package neartool.semuadapat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

// ***********************************//
// ** Code By Near Hoshinova
// ** Github: github.com/GarudaID
// ** Clean Code
// ***********************************//

public class WebViewApp extends Application {
    private static final String ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID;

    static  Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        if (Config.PUSH_ENABLED) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            OneSignal.initWithContext(this);
            OneSignal.setAppId(ONESIGNAL_APP_ID);

            OneSignal.setNotificationOpenedHandler(
                    new OneSignal.OSNotificationOpenedHandler() {
                        @Override
                        public void notificationOpened(OSNotificationOpenedResult result) {
                            // Capture Launch URL (App URL) here
                            // String url = null;

                            JSONObject data = result.getNotification().getAdditionalData();
                            String launchUrl = result.getNotification().getLaunchURL();
                            ;
                            if (data!=null && data.has("url"))
                            {
                                try {
                                    url = data.getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("openURL", launchUrl);
                            intent.putExtra("ONESIGNAL_URL", url);

                            Log.i("OneSignalExample", "openURL = " + launchUrl);
                            startActivity(intent);
                        }
                    });
        }


        if ((Config.SHOW_BANNER_AD) || (Config.SHOW_FULL_SCREEN_AD)) {
            if (Config.USE_FACEBOOK_ADS) {
                AudienceNetworkAds.initialize(this);
                AdSettings.addTestDevice("ur test device phone");
            } else {
                MobileAds.initialize(this, null);
            }
        }
    }
}