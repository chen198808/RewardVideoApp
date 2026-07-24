package com.mauna.aikan.xw.ad;
    
    import android.app.Activity;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.FrameLayout;
    import com.beizi.sdk.ByManager;
    import com.beizi.sdk.listener.RewardVideoListener;
    
    public class RewardVideoActivity extends Activity {
        private static final String TAG = "RewardVideoActivity";
        private static RewardVideoCallback sCallback;
    
        public interface RewardVideoCallback {
            void onReward();
            void onClose();
            void onError(String msg);
        }
    
        public static void setCallback(RewardVideoCallback callback) {
            sCallback = callback;
        }
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FrameLayout container = new FrameLayout(this);
            container.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));
            container.setBackgroundColor(0xFF000000);
            setContentView(container);
    
            Log.d(TAG, "激励视频广告页面启动");
    
            try {
                ByManager.getInstance().playRewardVideo(this, AdBridge.getCurrentCodeId(),
                    new RewardVideoListener() {
                        @Override
                        public void onAdLoaded() {
                            Log.d(TAG, "广告加载完成");
                        }
    
                        @Override
                        public void onAdClose() {
                            Log.d(TAG, "广告关闭");
                            if (sCallback != null) sCallback.onClose();
                            finish();
                        }
    
                        @Override
                        public void onAdReward() {
                            Log.d(TAG, "获得奖励");
                            if (sCallback != null) sCallback.onReward();
                        }
    
                        @Override
                        public void onAdShow() {
                            Log.d(TAG, "广告展示");
                        }
    
                        @Override
                        public void onAdError(String msg) {
                            Log.e(TAG, "广告错误: " + msg);
                            if (sCallback != null) sCallback.onError(msg);
                        }
                    });
            } catch (Exception e) {
                Log.e(TAG, "播放失败: " + e.getMessage());
                if (sCallback != null) sCallback.onError(e.getMessage());
                finish();
            }
        }
    
        @Override
        protected void onDestroy() {
            super.onDestroy();
        }
    }