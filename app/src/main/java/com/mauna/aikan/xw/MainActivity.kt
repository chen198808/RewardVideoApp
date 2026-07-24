package com.mauna.aikan.xw
    
    import android.content.Context
    import android.content.SharedPreferences
    import android.graphics.Color
    import android.os.Bundle
    import android.view.Menu
    import android.view.MenuItem
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import com.google.android.material.snackbar.Snackbar
    import com.mauna.aikan.xw.ad.AdBridge
    import com.mauna.aikan.xw.ad.RewardVideoActivity
    import com.mauna.aikan.xw.databinding.ActivityMainBinding
    import java.text.SimpleDateFormat
    import java.util.*
    
    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        private var coins = 0
        private var todayWatchCount = 0
        private var todayDate = ""
        private val rewardPerAd = 10
        private val bonusThreshold = 5
        private val bonusMultiplier = 2
        private val redeemCost = 100
        private lateinit var prefs: SharedPreferences
        // 百益联盟广告位ID
        private val rewardCodeId = "A00001"
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
            prefs = getSharedPreferences("coin_prefs", Context.MODE_PRIVATE)
            loadData()
            checkDailyReset()
            // 初始化百益广告SDK
            AdBridge.init(this, "100000")
            AdBridge.loadRewardVideo(rewardCodeId)
            setupClickListeners()
            updateCoinDisplay()
        }
    
        private fun setupClickListeners() {
            binding.btnWatchAd.setOnClickListener { showRewardedAd() }
            binding.btnRedeem.setOnClickListener { attemptRedeem() }
        }
    
        private fun loadData() {
            coins = prefs.getInt("coins", 0)
            todayWatchCount = prefs.getInt("watch_count", 0)
            todayDate = prefs.getString("today_date", "") ?: ""
        }
    
        private fun saveData() {
            prefs.edit().apply {
                putInt("coins", coins)
                putInt("watch_count", todayWatchCount)
                putString("today_date", todayDate)
                apply()
            }
        }
    
        private fun checkDailyReset() {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            if (todayDate != today) {
                todayDate = today
                todayWatchCount = 0
                saveData()
            }
        }
    
        private fun showRewardedAd() {
            binding.btnWatchAd.text = "⏳ 广告加载中…"
            binding.btnWatchAd.isEnabled = false
            // 加载广告
            AdBridge.loadRewardVideo(rewardCodeId)
            // 设置回调并播放
            RewardVideoActivity.setCallback(object : RewardVideoActivity.RewardVideoCallback {
                override fun onReward() {
                    runOnUiThread {
                        var earned = rewardPerAd
                        if (todayWatchCount < bonusThreshold) earned *= bonusMultiplier
                        coins += earned
                        todayWatchCount++
                        saveData()
                        updateCoinDisplay()
                        binding.btnWatchAd.text = "▶️ 看视频赚金币"
                        binding.btnWatchAd.isEnabled = true
                        val bonus = if (todayWatchCount <= bonusThreshold) "（双倍🔥）" else ""
                        Snackbar.make(binding.root, "🎉 获得 $earned 金币！$bonus", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.parseColor("#FF6D00")).setTextColor(Color.WHITE).show()
                    }
                }
                override fun onClose() {
                    runOnUiThread {
                        binding.btnWatchAd.text = "▶️ 看视频赚金币"
                        binding.btnWatchAd.isEnabled = true
                    }
                }
                override fun onError(msg: String) {
                    runOnUiThread {
                        binding.btnWatchAd.text = "😅 点击重试"
                        binding.btnWatchAd.isEnabled = true
                        Toast.makeText(this@MainActivity, "广告加载失败", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            // 打开广告Activity
            val intent = android.content.Intent(this, RewardVideoActivity::class.java)
            startActivity(intent)
        }
    
        private fun attemptRedeem() {
            if (coins >= redeemCost) {
                AlertDialog.Builder(this)
                    .setTitle("🎁 确认兑换")
                    .setMessage("消耗 $redeemCost 金币兑换奖励？")
                    .setPositiveButton("兑换") { _, _ ->
                        coins -= redeemCost
                        saveData()
                        updateCoinDisplay()
                        Snackbar.make(binding.root, "🎉 兑换成功！", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.parseColor("#43A047")).setTextColor(Color.WHITE).show()
                    }
                    .setNegativeButton("再想想", null).show()
            } else {
                Snackbar.make(binding.root, "😅 还差 ${redeemCost - coins} 金币", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.parseColor("#E65100")).setTextColor(Color.WHITE).show()
            }
        }
    
        private fun updateCoinDisplay() {
            binding.textCoins.text = coins.toString()
        }
    
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }
    
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_reset -> {
                    AlertDialog.Builder(this).setTitle("重置").setMessage("确定重置所有数据？")
                        .setPositiveButton("确定") { _, _ ->
                            coins = 0
                            todayWatchCount = 0
                            todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            saveData()
                            updateCoinDisplay()
                        }.setNegativeButton("取消", null).show()
                    true
                }
                R.id.action_about -> {
                    AlertDialog.Builder(this).setTitle("📱 关于")
                        .setMessage("看广告赚金币 v1.0.0\n\n观看激励视频广告赚取金币！\n每天前5次双倍奖励 🔥")
                        .setPositiveButton("知道了", null).show()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }