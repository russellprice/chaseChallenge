package com.example.chasechallenge.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.chasechallenge.mapToCityItemList
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chasechallenge.R
import com.example.chasechallenge.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater).apply {
            viewModel = this@MainFragment.viewModel
            cityRecycler.apply {
                layoutManager = LinearLayoutManager(this@MainFragment.context)
                adapter = this@MainFragment.adapter
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
    }

    override fun onResume() {
        super.onResume()
        methodRequiresOnePermission()
        initObserver()
    }

    //todo handle this better
    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.cities.collectLatest {
                adapter.clear()
                adapter.addAll(
                    it.mapToCityItemList()
                )
                adapter.notifyDataSetChanged()


            }
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun methodRequiresOnePermission() {
        if (EasyPermissions.hasPermissions(activity, ACCESS_COARSE_LOCATION)) {

            fusedLocationClient.lastLocation.addOnSuccessListener {

                Log.d("TEST", "${it.latitude} | ${it.longitude}")
                viewModel.fetchCity(it.longitude, it.latitude)
            }

        } else {
            // Do not have permission, request it
            EasyPermissions.requestPermissions(
                host = activity as MainActivity,
                rationale = getString(R.string.permission_coarse_location),
                requestCode = REQUEST_CODE_LOCATION,
                perms = arrayOf(ACCESS_COARSE_LOCATION)
            )
        }
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 1
    }
}
