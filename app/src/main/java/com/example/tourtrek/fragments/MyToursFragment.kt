package com.example.tourtrek.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tourtrek.R
import com.example.tourtrek.viewmodels.MyToursViewModel

class MyToursFragment : Fragment() {

    private lateinit var myToursViewModel: MyToursViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        myToursViewModel = ViewModelProviders.of(this).get(MyToursViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)

        myToursViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }
}