package com.bangkit.artnesia.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.Article
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.data.model.User
import com.bangkit.artnesia.databinding.FragmentHomeBinding
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.LiteratureAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mUserDetails: User

    private lateinit var literatureAdapter: LiteratureAdapter
    private lateinit var literatureList: ArrayList<Literature>
    private lateinit var literatureRV: RecyclerView

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slideModel : ArrayList<SlideModel> = arrayListOf()

        (activity as AppCompatActivity).supportActionBar?.hide()

        slideModel.add(SlideModel("https://s3-kemenparekraf.s3.ap-southeast-1.amazonaws.com/EKONOMI_KREATIF_SENI_PERTUNJUKAN_shutterstock_1446792446_oki_cahyo_nugroho_d60c5c608e.jpg"))
        slideModel.add(SlideModel("https://akcdn.detik.net.id/community/media/visual/2019/11/02/2472af9a-055d-46dc-bff3-630d5ed510e5_43.jpeg?w=250&q="))
        slideModel.add(SlideModel("https://cdnaz.cekaja.com/media/2020/06/1505_Artikel-CA20_-kesenian-tradisional-khas-jawa-barat.jpg"))
        slideModel.add(SlideModel("https://cdn-2.tstatic.net/manado/foto/bank/images/pesta-kesenian-bali-1.jpg"))

        binding.isHome.setImageList(slideModel , ScaleTypes.CENTER_CROP)

        getUserDetails()
        getLiterature()
        getArticle()
    }

    private fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    private fun userDetailsSuccess(user: User) {
        mUserDetails = user
        binding.tvHomeProfileName.text = user.name
    }

    private fun getUserDetails() {
        mFireStore.collection("users")
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(this.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!
                userDetailsSuccess(user)
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    private fun getLiterature(){
        literatureRV = binding.exploreRecView
        literatureRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        literatureRV.setHasFixedSize(true)

        literatureList = arrayListOf()

        literatureAdapter  = LiteratureAdapter(requireActivity() , literatureList)

        literatureRV.adapter = literatureAdapter

        getLiteratureData()
    }

    private fun getArticle() {
        articleRV = binding.articleRecView
        articleRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        articleRV.setHasFixedSize(true)

        articleList = arrayListOf()

        articleAdapter = ArticleAdapter(requireActivity(), articleList)

        articleRV.adapter = articleAdapter

        getArticleData()
    }

    private fun getLiteratureData() {
        mFireStore.collection("literature")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
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
                            val lit = dc.document.toObject(Literature::class.java)
                            lit.literature_id = dc.document.id

                            literatureList.add(lit)
                        }
                    }
                    literatureList.shuffle()

                    literatureAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun getArticleData() {
        mFireStore.collection("article")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val artc = dc.document.toObject(Article::class.java)
                            artc.article_id = dc.document.id

                            articleList.add(artc)
                        }
                    }
                    articleList.shuffle()

                    articleAdapter.notifyDataSetChanged()
                }
            })
    }
}