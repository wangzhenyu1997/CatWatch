package com.wangzhenyu.catwatch

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.wangzhenyu.catwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//test
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val navController = Navigation.findNavController(this, R.id.fragment)
        val configuration: AppBarConfiguration =
            AppBarConfiguration.Builder(binding.bottomNavigationView.menu).build()

        //设置标题栏
        NavigationUI.setupActionBarWithNavController(this, navController, configuration)
        //设置切换逻辑，menu和graph中的每一个id要一样
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || findNavController(R.id.fragment).navigateUp()
    }
}