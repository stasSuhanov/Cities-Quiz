package com.mytest.android.citiesapp.ui.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mytest.android.citiesapp.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {

    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutAppBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.telegramLink.movementMethod = LinkMovementMethod.getInstance()
        binding.citiesInfoFileLink.movementMethod = LinkMovementMethod.getInstance()
        binding.citiesFlagsLink.movementMethod = LinkMovementMethod.getInstance()
    }
}