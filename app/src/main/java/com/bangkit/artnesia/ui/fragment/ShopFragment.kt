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
import com.bangkit.artnesia.databinding.FragmentHomeBinding
import com.bangkit.artnesia.databinding.FragmentShopBinding
import com.bangkit.artnesia.ui.adapter.CoverProductAdapter
import com.bangkit.artnesia.ui.adapter.ProductAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class ShopFragment : Fragment() {

    private var _binding : FragmentShopBinding? = null
    private val binding get() = _binding as FragmentShopBinding

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
        _binding = FragmentShopBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        val slideModel : ArrayList<SlideModel> = arrayListOf()
        slideModel.add(SlideModel("https://cdn-2.tstatic.net/manado/foto/bank/images/pesta-kesenian-bali-1.jpg"))
        slideModel.add(SlideModel("https://s3-kemenparekraf.s3.ap-southeast-1.amazonaws.com/EKONOMI_KREATIF_SENI_PERTUNJUKAN_shutterstock_1446792446_oki_cahyo_nugroho_d60c5c608e.jpg"))
        slideModel.add(SlideModel("https://akcdn.detik.net.id/community/media/visual/2019/11/02/2472af9a-055d-46dc-bff3-630d5ed510e5_43.jpeg?w=250&q="))
        slideModel.add(SlideModel("https://cdnaz.cekaja.com/media/2020/06/1505_Artikel-CA20_-kesenian-tradisional-khas-jawa-barat.jpg"))
        binding.isShop.setImageList(slideModel , ScaleTypes.CENTER_CROP)

        coverAdapter = CoverProductAdapter(requireActivity())
        coverAdapter.setData(getCover())
        newProductAdapter = ProductAdapter(requireActivity())
        newProductAdapter.setData(getNewProduct())
        saleProductAdapter = ProductAdapter(requireActivity())
        saleProductAdapter.setData(getSaleProduct())

        //for cover
        binding.coverRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.coverRecView.setHasFixedSize(true)
        binding.coverRecView.adapter = coverAdapter

        //for new product
        binding.newRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newRecView.setHasFixedSize(true)
        binding.newRecView.adapter = newProductAdapter

        //for sale product
        binding.saleRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.saleRecView.setHasFixedSize(true)
        binding.saleRecView.adapter = newProductAdapter
    }
}