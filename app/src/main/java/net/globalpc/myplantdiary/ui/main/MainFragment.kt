package net.globalpc.myplantdiary.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_main.*

import net.globalpc.myplantdiary.R


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.plants.observe(viewLifecycleOwner, Observer {
            plants
                ->
            actPlantName.setAdapter( ArrayAdapter( requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, plants  ) )
        })
    }

}