package uz.impulse.impulse.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import uz.impulse.impulse.R
import uz.impulse.impulse.databinding.ActivityMainBinding


class MainActivity : uz.impulse.impulse.activity.BaseActivity() {
    private val TAG = MainActivity::class.java.toString()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            val navController = findNavController(R.id.fragmentContainerView)
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}