package com.example.currencyconverter.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.viewmodel.CurrencyResultState
import com.example.currencyconverter.viewmodel.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setAppCompatSpinners(binding)
        setButtonConvertOnClick(binding)
        observeCurrencyState(binding)
        setButtonReplaceOnClick(binding)
    }

    private fun observeCurrencyState(
        binding: ActivityMainBinding
    ) = lifecycleScope.launchWhenStarted {
        with(binding) {
            viewModel.currencyState.collect { currencyResultState ->
                when (currencyResultState) {
                    is CurrencyResultState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is CurrencyResultState.Success -> {
                        progressBar.visibility = View.GONE
                        textViewResult.setTextColor(Color.BLACK)
                        textViewResult.text = currencyResultState.result
                    }
                    is CurrencyResultState.Error -> {
                        progressBar.visibility = View.GONE
                        textViewResult.setTextColor(Color.RED)
                        textViewResult.text = currencyResultState.message
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setButtonConvertOnClick(
        binding: ActivityMainBinding
    ) = with(binding) {
        buttonConvert.setOnClickListener {
            hideKeyboard()
            textViewResult.text = resources.getString(R.string.empty)
            viewModel.getCurrencies(
                currencyFrom = appCompatSpinnerFrom
                    .getItemAtPosition(appCompatSpinnerFrom.selectedItemPosition)
                    .toString()
                    .lowercase(),
                currencyTo = appCompatSpinnerTo
                    .getItemAtPosition(appCompatSpinnerTo.selectedItemPosition)
                    .toString()
                    .lowercase(),
                amountString = textInputEditTextAmount.text.toString()
            )
        }
    }

    private fun setAppCompatSpinners(
        binding: ActivityMainBinding
    ) = with(binding) {
        val list = mutableListOf<String>()
        viewModel.list.forEach {
            list.add(it.uppercase(Locale.getDefault()))
        }
        val listForArrayAdapter = list.toTypedArray()
        val arrayAdapter = ArrayAdapter(
            this@MainActivity,
            R.layout.one_item_spinner_dropdown,
            R.id.text_view_title,
            listForArrayAdapter
        )
        arrayAdapter.setDropDownViewResource(R.layout.one_item_spinner_dropdown)
        appCompatSpinnerFrom.adapter = arrayAdapter
        appCompatSpinnerTo.adapter = arrayAdapter
        val initialPositionFrom = 63
        val initialPositionTo = 182
        appCompatSpinnerFrom.setSelection(initialPositionFrom)
        appCompatSpinnerTo.setSelection(initialPositionTo)
    }

    private fun setButtonReplaceOnClick(
        binding: ActivityMainBinding
    ) = with(binding) {
        materialCardViewReplace.setOnClickListener {
            val currentPositionFrom = appCompatSpinnerFrom.selectedItemPosition
            val currentPositionTo = appCompatSpinnerTo.selectedItemPosition
            appCompatSpinnerFrom.setSelection(currentPositionTo)
            appCompatSpinnerTo.setSelection(currentPositionFrom)
        }
    }

    private fun hideKeyboard() =
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
}