package com.sumit.myswipeproduct.presentation

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.connectivitychecker.ConnectivityChangeListener
import com.sumit.myswipeproduct.connectivitychecker.NetworkChangeReceiver
import com.sumit.myswipeproduct.databinding.FragmentHomeScreenBinding
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.presentation.adapter.ProductDetailsAdapter
import com.sumit.myswipeproduct.responsehandler.APIResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment(), ConnectivityChangeListener {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    private val networkChangeReceiver = NetworkChangeReceiver(this)

    private var productDetailsAdapter: ProductDetailsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerReceiver()
        bindViews()
        bindObservers()

    }

    private fun bindObservers() {
        with(homeScreenViewModel) {
            productDataList.observe(viewLifecycleOwner) {
                when (it) {
                    is APIResponse.Loading -> {
                        handleLoading(true)
                    }

                    is APIResponse.Success -> {
                        handleLoading(false)
                        productDetailsAdapter?.updateData(it.data)

                    }

                    is APIResponse.Error -> {
                        handleLoading(false)
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.no_data_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerFrameLayoutProductDetailsPage.visibility = View.VISIBLE
        } else {
            binding.shimmerFrameLayoutProductDetailsPage.visibility = View.GONE
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireActivity().registerReceiver(networkChangeReceiver, filter)
    }

    private fun bindViews() {
        setUpRecyclerViewAdapter()
        binding.swipeRefreshHomePageMessages.setOnRefreshListener {
            fetchAllProducts(fromServer = true)
            binding.swipeRefreshHomePageMessages.isRefreshing = false
        }
        fetchAllProducts(fromServer = false)
    }

    private fun setUpRecyclerViewAdapter() {
        productDetailsAdapter = ProductDetailsAdapter(mutableListOf())
        binding.rvProductDetails.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productDetailsAdapter
        }
    }

    private fun fetchAllProducts(fromServer: Boolean) {
        homeScreenViewModel.getProductDataList(fromServer)
    }

    override fun onNetworkConnected() {
        fetchAllProducts(fromServer = true)
    }

    override fun onNetworkDisconnected() {
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.no_internet),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
        _binding = null
    }

    private fun unregisterReceiver() {
        requireActivity().unregisterReceiver(networkChangeReceiver)
    }

}