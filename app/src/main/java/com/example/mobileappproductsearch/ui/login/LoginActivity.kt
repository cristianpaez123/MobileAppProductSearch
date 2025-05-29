package com.example.mobileappproductsearch.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.ActivityLoginBinding
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupEdgeToEdge()
        setupListeners()
        observeUiState()
    }

    private fun setupViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainContent)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Initial -> Unit
                        is UiState.EmptyData -> Unit
                        is UiState.Loading -> loadingState()
                        is UiState.Success -> successState()
                        is UiState.Error -> errorState(state)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        showLoadingOverlay(true)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoadingOverlay(show: Boolean) {
        binding.includeLoadingOverlay.loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun successState() {
        showLoadingOverlay(false)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun errorState(error: UiState.Error) {
        showLoadingOverlay(false)
        val message = when (error) {
            is UiState.Error.MessageRes -> getString(error.resId)
            is UiState.Error.MessageText -> error.message
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}