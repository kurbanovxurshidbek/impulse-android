package uz.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.impulse.impulse.R
import uz.impulse.impulse.adapter.FirstAidItemAdapter
import uz.impulse.impulse.data.remote.ApiClient
import uz.impulse.impulse.data.remote.services.IllnessService
import uz.impulse.impulse.databinding.FragmentFirstAidBinding
import uz.impulse.impulse.model.FirstAidItem
import uz.impulse.impulse.utils.SpacesItemDecoration
import uz.impulse.impulse.viewmodel.IllnessViewModel
import uz.impulse.impulse.viewmodel.factory.IllnessViewModelFactory
import uz.impulse.impulse.viewmodel.repository.IllnessRepository

class FirstAidFragment : BaseFragment() {
    private val TAG = FirstAidFragment::class.java.simpleName

    private var _binding: FragmentFirstAidBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstAidBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        navController = findNavController()
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val decoration = SpacesItemDecoration(2)
            recyclerView.addItemDecoration(decoration)

            recyclerView.adapter = FirstAidItemAdapter(this@FirstAidFragment, getAllItems())
        }
    }

    fun openIllnessPage(position: Int) {
        val args = Bundle()
        args.putInt("id", position)
        navController.navigate(R.id.firstAidToIllness, args)
    }

    private fun getAllItems(): List<FirstAidItem> {
        val items = ArrayList<FirstAidItem>()
        items.add(FirstAidItem())
        items.add(FirstAidItem(R.drawable.ic_hypothermia, getString(R.string.str_hypothermia)))
        items.add(FirstAidItem(R.drawable.ic_meningits, getString(R.string.str_meningitis)))
        items.add(FirstAidItem(R.drawable.ic_poison, getString(R.string.str_poisoning)))
        items.add(FirstAidItem(R.drawable.ic_psychological, getString(R.string.str_psychological)))
        items.add(FirstAidItem(R.drawable.ic_epilepsy, getString(R.string.str_seizure)))
        items.add(FirstAidItem(R.drawable.ic_stings, getString(R.string.str_stings)))
        items.add(FirstAidItem(R.drawable.ic_strains, getString(R.string.str_strains)))
        items.add(FirstAidItem(R.drawable.ic_allergies, getString(R.string.str_allergies)))
        items.add(FirstAidItem(R.drawable.ic_stroke, getString(R.string.str_stroke)))
        items.add(FirstAidItem(R.drawable.ic_asthma, getString(R.string.str_asthma_attack)))
        items.add(FirstAidItem(R.drawable.ic_unconscious, getString(R.string.str_unconscious)))
        items.add(FirstAidItem(R.drawable.ic_bleeding, getString(R.string.str_bleeding)))
        items.add(FirstAidItem(R.drawable.ic_broken_bone, getString(R.string.str_broken_bone)))
        items.add(FirstAidItem(R.drawable.ic_burns, getString(R.string.str_burns)))
        items.add(FirstAidItem(R.drawable.ic_choking, getString(R.string.str_choking)))
        items.add(FirstAidItem(R.drawable.ic_diabetic, getString(R.string.str_diabetic_emergency)))
        items.add(FirstAidItem(R.drawable.ic_distress, getString(R.string.str_distress)))
        items.add(FirstAidItem(R.drawable.ic_head_injury, getString(R.string.str_head_injury)))
        items.add(FirstAidItem(R.drawable.ic_heart_attack, getString(R.string.str_heart_attack)))
        items.add(FirstAidItem(R.drawable.ic_heat_stroke, getString(R.string.str_heat_stroke)))
        return items
    }
}