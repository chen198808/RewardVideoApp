package com.mauna.aikan.xw.ad;
    
    import android.content.Context;
    import android.util.Log;
    import com.beizi.sdk.ByManager;
    
    public class AdBridge {
        private static final String TAG = "AdBridge";
        private static boolean initialized = false;
        private static Context appContext;
        private static String currentCodeId = "";
    
        public static void init(Context context, String appId) {
            appContext = context.getApplicationContext();
            try {
                ByManager.getInstance().init(appContext, appId);
                initialized = true;
                Log.d(TAG, "百益广告SDK初始化成功");
            } catch (Exception e) {
                Log.e(TAG, "初始化失败: " + e.getMessage());
            }
        }
    
        public static void loadRewardVideo(String codeId) {
            currentCodeId = codeId;
            try {
                ByManager.getInstance().loadRewardVideo(appContext, codeId);
                Log.d(TAG, "激励视频已请求: " + codeId);
            } catch (Exception e) {
                Log.e(TAG, "加载失败: " + e.getMessage());
            }
        }
    
        public static boolean isInitialized() {
            return initialized;
        }
    
        public static String getCurrentCodeId() {
            return currentCodeId;
        }
    }