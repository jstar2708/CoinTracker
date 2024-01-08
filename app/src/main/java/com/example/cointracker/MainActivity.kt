package com.example.cointracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cointracker.databinding.ActivityMainBinding
import com.example.cointracker.presentation.adapters.CoinAdapter
import com.example.cointracker.presentation.viewmodels.CoinViewModel
import com.example.cointracker.presentation.viewmodels.ViewEvents
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CoinViewModel
    private val adapter = CoinAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.state.observe(this) {
            if (it.isLoading) {
                showProgress()
            } else if (it.error.isNotEmpty()) {
                showError(it.error)
            }
            else {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        showData()
                        addTabs(viewModel.getFiats())
                    }
                    adapter.updateList(viewModel.getCoinData())
                }
            }
        }

        binding.tabLayout.addOnTabSelectedListener(this)

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getCoinDetailData()
            viewModel.getCoinExchangeData(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString())
            binding.lastUpdated.text = convertIntoTime()
            binding.swipeLayout.isRefreshing = false
        }

        binding.retryButton.setOnClickListener {
            viewModel.getCoinDetailData()
            viewModel.getCoinExchangeData(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString())
            binding.lastUpdated.text = convertIntoTime()
        }

        viewModel.viewEvents.observe(this) {
            when (it) {
                is ViewEvents.UpdateTimer -> {
                    binding.dataUpdatesIn.text = convertIntoMin(it.time)
                }
                is ViewEvents.SetLastUpdateTime -> {
                    binding.lastUpdated.text = convertIntoTime()
                }
            }
        }
    }

    private fun convertIntoTime(): String {
        val calendar: Calendar = Calendar.getInstance()
        val hour24hrs: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = calendar.get(Calendar.MINUTE)
        return "Last updated at $hour24hrs:$minutes"
    }

    private fun convertIntoMin(time: Long): String {
        return String.format(
            "Data updates in %02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    private fun showData() {
        binding.retryButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.tabLayout.visibility = View.VISIBLE
    }

    private fun addTabs(fiat: List<String>) {
        fiat.forEach { t ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().apply {
                text = t
            })
        }
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        if (binding.tabLayout.tabCount == 1) {
            binding.tabLayout.visibility = View.GONE
        }
        binding.retryButton.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.error.text = message
        binding.retryButton.visibility = View.VISIBLE
        binding.error.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        if (binding.tabLayout.tabCount == 1) {
            binding.tabLayout.visibility = View.GONE
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewModel.currentTabText = tab?.text.toString()
        viewModel.getCoinExchangeData(tab?.text.toString())
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}