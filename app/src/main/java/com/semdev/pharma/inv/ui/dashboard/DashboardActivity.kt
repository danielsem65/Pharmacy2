package com.semdev.pharma.inv.ui.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.semdev.pharma.inv.R
import com.semdev.pharma.inv.databinding.ActivityDashboardBinding
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private val adapter = MedicineAdapter()

    private var backPressCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = DashboardViewModel(application)

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupWebView()
        setupSwipeRefresh()
        observeUiState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.loadMedicines()
                binding.webView.reload()
                true
            }
            R.id.menu_about -> {
                showAboutDialog()
                true
            }
            R.id.menu_exit -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        backPressCount++
        when (backPressCount) {
            1 -> {
                binding.containerList.isVisible = false
                binding.webView.isVisible = true
                Snackbar.make(binding.root, "WebView", Snackbar.LENGTH_SHORT).show()
            }
            2 -> {
                binding.containerList.isVisible = true
                binding.webView.isVisible = false
                backPressCount = 0
                Snackbar.make(binding.root, "Dashboard", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.editSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                cacheMode = WebSettings.LOAD_DEFAULT
                javaScriptEnabled = true
            }
            webViewClient = WebViewClient()
            loadUrl("https://scabpharmacy.com/")
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMedicines()
            binding.webView.reload()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefresh.isRefreshing = false
                    when (state) {
                        is DashboardViewModel.UiState.Loading -> {
                            binding.swipeRefresh.isRefreshing = true
                        }
                        is DashboardViewModel.UiState.Success -> {
                            adapter.submitList(state.medicines)
                            binding.textEmpty.isVisible = state.medicines.isEmpty()
                        }
                        is DashboardViewModel.UiState.Offline -> {
                            adapter.submitList(state.medicines)
                            binding.textEmpty.isVisible = state.medicines.isEmpty()
                            Snackbar.make(binding.root, "Loaded from offline cache", Snackbar.LENGTH_SHORT).show()
                        }
                        is DashboardViewModel.UiState.Error -> {
                            adapter.submitList(emptyList())
                            binding.textEmpty.isVisible = true
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("ABOUT THE APPLICATION")
            .setIcon(R.drawable.icon_sticky_note_2_round)
            .setMessage(
                "This application allows pharmacy customers to easily view current medicine prices and details.\n\n" +
                "Key features include:\n\n" +
                "\u2022 Comprehensive Medicine Listing: Browse a complete list of available medications with their current prices.\n" +
                "\u2022 Detailed Medicine Information: View detailed information about each medicine, including description, dosage, and other relevant details, simply by clicking on its name.\n" +
                "\u2022 Up-to-Date Pricing: Rest assured that all prices displayed are current and reflect the pharmacy\u0027s most recent updates.\n\n" +
                "This application is ideal for pharmacy customers who want to quickly check prices and access detailed information about medications before visiting the pharmacy. It provides a convenient way to stay informed about medication costs and details."
            )
            .setPositiveButton("Okay!") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
