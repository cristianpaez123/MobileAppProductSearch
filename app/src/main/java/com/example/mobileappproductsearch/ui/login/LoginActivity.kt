package com.example.mobileappproductsearch.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.ActivityLoginBinding
import com.example.mobileappproductsearch.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupEdgeToEdge()
        setupListeners()
        setupObservers()
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

    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginUiState.Loading -> loadingState()
                is LoginViewModel.LoginUiState.Success -> successState()
                is LoginViewModel.LoginUiState.Error -> errorState(state)
            }
        }
    }

    private fun loadingState() = showLoadingOverlay(true)

    private fun showLoadingOverlay(show: Boolean) {
        binding.includeLoadingOverlay.loadingOverlay.setVisibility(if (show) View.VISIBLE else View.GONE)
    }

    private fun successState() {
        showLoadingOverlay(false)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun errorState(state: LoginViewModel.LoginUiState.Error) {
        showLoadingOverlay(false)
        val message = state.messageRes?.let { getString(it) }
            ?: state.message
            ?: getString(R.string.error_unexpected)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}