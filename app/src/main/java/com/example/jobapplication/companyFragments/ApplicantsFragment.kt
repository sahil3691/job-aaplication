package com.example.jobapplication.companyFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobapplication.R
import com.example.jobapplication.adapters.JobAdapter
import com.example.jobapplication.adapters.JobAdapter2
import com.example.jobapplication.databinding.FragmentApplicantsBinding
import com.example.jobapplication.models.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApplicantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplicantsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentApplicantsBinding
    private lateinit var recyclerView : RecyclerView
    private lateinit var jobAdapter: JobAdapter2
    private var jobList = mutableListOf<Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentApplicantsBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_applicants, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.applicantRv


        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter
        // Initialize adapter with context
        jobAdapter = JobAdapter2(requireContext(), jobList)
        recyclerView.adapter = jobAdapter

        loadCompanyJobs()
    }


    private fun loadCompanyJobs() {
        val companyId = FirebaseAuth.getInstance().currentUser!!.uid
        val jobRef = FirebaseDatabase.getInstance().getReference("Companies").child(companyId)
            .child("jobs")
        jobRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()
                for (jobIdSnapshot in snapshot.children) {
                    val jobId = jobIdSnapshot.getValue(String::class.java)
                    if(jobId!=null) {
                        val jobRef =
                            FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
                        jobRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(jobSnapshot: DataSnapshot) {
                                val job = jobSnapshot.getValue(Job::class.java)
                                job?.let { jobList.add(it) }
                                jobAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApplicantsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicantsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}