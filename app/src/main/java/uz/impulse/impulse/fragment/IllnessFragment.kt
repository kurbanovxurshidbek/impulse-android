package uz.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import uz.impulse.impulse.R
import uz.impulse.impulse.data.local.AppDatabase
import uz.impulse.impulse.data.remote.ApiClient
import uz.impulse.impulse.data.remote.model.Illness
import uz.impulse.impulse.data.remote.services.IllnessService
import uz.impulse.impulse.databinding.FragmentIllnessBinding
import uz.impulse.impulse.viewmodel.IllnessViewModel
import uz.impulse.impulse.viewmodel.factory.IllnessViewModelFactory
import uz.impulse.impulse.viewmodel.repository.IllnessRepository

class IllnessFragment : BaseFragment() {
    private val TAG = IllnessFragment::class.java.simpleName
    private var _binding: FragmentIllnessBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IllnessViewModel
    private lateinit var appDatabase: AppDatabase

    var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIllnessBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    private fun initViews() {
        setupViewModel()
        appDatabase = AppDatabase.getInstance(requireContext())

        id = arguments!!.getInt("id")
        binding.ivBack.setOnClickListener { findNavController().navigate(R.id.illnessBackToFirstAid) }
        binding.tvBack.setOnClickListener { findNavController().navigate(R.id.illnessBackToFirstAid) }

        if (appDatabase.illnessDao().getAllIllness().isEmpty()) {
            showProgress(requireActivity())
            viewModel.getAllIllness()
            viewModel.responseAllIllness.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    storeToLocal(response.body()!!)
                    dismissLoading()
                    setUI()
                }
            }
        } else {
            setUI()
        }
    }

    private fun setUI() {
        val illness = appDatabase.illnessDao().getIllnessById(id!!)
        binding.apply {
            Glide.with(this@IllnessFragment)
                .load(illness.photoUrl)
                .into(ivPhoto)
            when (illness.id) {
                1 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                2 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                3 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                4 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                    llInfo4.visibility = View.VISIBLE
                    tvInfo4.text = illness.info4
                    llInfo5.visibility = View.VISIBLE
                    tvInfo5.text = illness.info5
                    llInfo6.visibility = View.VISIBLE
                    tvInfo6.text = illness.info6
                    llInfo7.visibility = View.VISIBLE
                    tvInfo7.text = illness.info7
                    llInfo8.visibility = View.VISIBLE
                    tvInfo8.text = illness.info8
                }
                5 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                6 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                7 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                }
                8 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                9 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                10 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                11 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                    llInfo4.visibility = View.VISIBLE
                    tvInfo4.text = illness.info4
                    llInfo5.visibility = View.VISIBLE
                    tvInfo5.text = illness.info5
                    llInfo6.visibility = View.VISIBLE
                    tvInfo6.text = illness.info6
                    llInfo7.visibility = View.VISIBLE
                    tvInfo7.text = illness.info7
                }
                12 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo2.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                13 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                14 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                15 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                    llInfo4.visibility = View.VISIBLE
                    tvInfo4.text = illness.info4
                    llInfo5.visibility = View.VISIBLE
                    tvInfo5.text = illness.info5
                }
                16 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                }
                17 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                }
                18 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                }
                19 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                    llInfo4.visibility = View.VISIBLE
                    tvInfo4.text = illness.info4
                }
                20 -> {
                    llInfo1.visibility = View.VISIBLE
                    tvInfo1.text = illness.info1
                    llInfo2.visibility = View.VISIBLE
                    tvInfo2.text = illness.info2
                    llInfo3.visibility = View.VISIBLE
                    tvInfo3.text = illness.info3
                    llInfo4.visibility = View.VISIBLE
                    tvInfo4.text = illness.info4
                }
            }
        }
    }

    private fun storeToLocal(list: List<Illness>) {
        list.forEach {
            appDatabase.illnessDao().addIllness(it)
        }
    }

    private fun setupViewModel() {
        val service = ApiClient.createServiceWithAuth(IllnessService::class.java)
        val repository = IllnessRepository(service)
        val factory = IllnessViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[IllnessViewModel::class.java]
    }
}