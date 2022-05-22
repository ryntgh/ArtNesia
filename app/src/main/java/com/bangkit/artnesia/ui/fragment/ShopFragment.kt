package com.bangkit.artnesia.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.ProductData
import com.bangkit.artnesia.data.model.ProductModel
import com.bangkit.artnesia.ui.adapter.CoverProductAdapter
import com.bangkit.artnesia.ui.adapter.ProductAdapter

class ShopFragment : Fragment() {
    lateinit var rvCover: RecyclerView
    lateinit var rvNewProduct: RecyclerView
    lateinit var rvSaleProduct: RecyclerView

    private lateinit var coverAdapter: CoverProductAdapter
    private lateinit var newProductAdapter: ProductAdapter
    private lateinit var saleProductAdapter: ProductAdapter

    private fun getCover(): List<ProductModel> {
        return ProductData.generateProduct()
    }

    private fun getNewProduct(): List<ProductModel>{
        return ProductData.generateProduct()
    }

    private fun getSaleProduct(): List<ProductModel>{
        return ProductData.generateProduct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        rvCover = view.findViewById(R.id.coverRecView)
        rvNewProduct = view.findViewById(R.id.newRecView)
        rvSaleProduct = view.findViewById(R.id.saleRecView)

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        coverAdapter = CoverProductAdapter(requireActivity())
        coverAdapter.setData(getCover())
        newProductAdapter = ProductAdapter(requireActivity())
        newProductAdapter.setData(getNewProduct())
        saleProductAdapter = ProductAdapter(requireActivity())
        saleProductAdapter.setData(getSaleProduct())

        //for cover
        rvCover.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCover.setHasFixedSize(true)
        rvCover.adapter = coverAdapter

        //for new product
        rvNewProduct.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvNewProduct.setHasFixedSize(true)
        rvNewProduct.adapter = newProductAdapter

        //for sale product
        rvSaleProduct.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvSaleProduct.setHasFixedSize(true)
        rvSaleProduct.adapter = newProductAdapter
    }
}