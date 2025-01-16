package com.shambavi.thericecompany.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shambavi.thericecompany.Activitys.NotificationsActivity
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.Logins.LoginActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {


        binding.imgNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationsActivity::class.java))
        }
        binding.linearSearch.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

}