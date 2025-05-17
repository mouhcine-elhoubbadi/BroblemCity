package com.example.myappl.fragment

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myappl.R
import java.util.Locale

class ProfileFragment : Fragment() {

    private val languages = listOf("en", "ar", "fr")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", 0)
        val savedLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"

      /*val spinner = view.findViewById<Spinner>(R.id.spinner).apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
            setSelection(languages.indexOf(savedLanguage))
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLanguage = languages[position]
                    if (selectedLanguage != savedLanguage) {
                        setLocale(selectedLanguage)
                        sharedPreferences.edit().putString("LANGUAGE", selectedLanguage).apply()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }*/
    }

    private fun setLocale(language: String) {
        Locale.setDefault(Locale(language))
        val config = Configuration(requireContext().resources.configuration).apply {
            setLocale(Locale(language))
        }
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        activity?.recreate()
    }
}
