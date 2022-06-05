package com.bangkit.artnesia.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.ProductData
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.data.model.ProductModel
import com.bangkit.artnesia.databinding.FragmentHomeBinding
import com.bangkit.artnesia.databinding.FragmentShopBinding
import com.bangkit.artnesia.ui.adapter.CoverProductAdapter
import com.bangkit.artnesia.ui.adapter.ProductAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.*

class ShopFragment : Fragment() {

    private var _binding : FragmentShopBinding? = null
    private val binding get() = _binding as FragmentShopBinding

    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var newProductAdapter: ProductAdapter
    private lateinit var newProductList: ArrayList<Product>
    private lateinit var newProductRV: RecyclerView

    private lateinit var saleProductAdapter: ProductAdapter
    private lateinit var saleProductList: ArrayList<Product>
    private lateinit var saleProductRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        val slideModel : ArrayList<SlideModel> = arrayListOf()
        slideModel.add(SlideModel("https://i0.wp.com/sahabatnesia.com/wp-content/uploads/2016/10/jaran-kepang.jpg?resize=700%2C465"))
        slideModel.add(SlideModel("https://cdn-2.tstatic.net/manado/foto/bank/images/pesta-kesenian-bali-1.jpg"))
        slideModel.add(SlideModel("https://s3-kemenparekraf.s3.ap-southeast-1.amazonaws.com/EKONOMI_KREATIF_SENI_PERTUNJUKAN_shutterstock_1446792446_oki_cahyo_nugroho_d60c5c608e.jpg"))
        slideModel.add(SlideModel("https://akcdn.detik.net.id/community/media/visual/2019/11/02/2472af9a-055d-46dc-bff3-630d5ed510e5_43.jpeg?w=250&q="))
        slideModel.add(SlideModel("https://cdnaz.cekaja.com/media/2020/06/1505_Artikel-CA20_-kesenian-tradisional-khas-jawa-barat.jpg"))
        binding.isShop.setImageList(slideModel , ScaleTypes.CENTER_CROP)

        getNewProduct()
        getSaleProduct()
    }

    private fun getNewProduct(){
        newProductRV = binding.newRecView
        newProductRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        newProductRV.setHasFixedSize(true)

        newProductList = arrayListOf()

        newProductAdapter  = ProductAdapter(requireActivity() , newProductList)

        newProductRV.adapter = newProductAdapter

        getProductData(newProductList, newProductAdapter)
    }

    private fun getSaleProduct(){
        saleProductRV = binding.saleRecView
        saleProductRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saleProductRV.setHasFixedSize(true)

        saleProductList = arrayListOf()

        saleProductAdapter  = ProductAdapter(requireActivity() , saleProductList)

        saleProductRV.adapter = saleProductAdapter

        getProductData(saleProductList, saleProductAdapter)
    }

    private fun getProductData(productlist : ArrayList<Product>, productAdapt: ProductAdapter) {
        mFireStore.collection("products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error!=null){
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            //productArrayList.add(dc.document.toObject(Product::class.java))
                            val product = dc.document.toObject(Product::class.java)
                            product.product_id = dc.document.id

                            productlist.add(product)
                        }
                    }
                    //randomize list
                    productlist.shuffle()

                    productAdapt.notifyDataSetChanged()
                }
            })
    }
}