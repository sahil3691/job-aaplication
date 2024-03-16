package com.example.jobapplication.companyFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.jobapplication.R
import com.example.jobapplication.company.CompanyMainActivity
import com.example.jobapplication.databinding.FragmentAddJobBinding
import com.example.jobapplication.models.Job
import com.example.jobapplication.models.company
import com.example.jobapplication.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.time.Duration

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddJobFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddJobFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentAddJobBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var postion : String
    private lateinit var skills : String
    private lateinit var workExperience : String
    private lateinit var location : String
    private lateinit var description : String
    private lateinit var upload : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddJobBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    private fun uploadpost(postion: String, skills: String, workExperience: String, location: String, description: String, salary : String,duration: String) {
        LoadingDialog.showDialog(requireContext())
        FirebaseDatabase.getInstance().getReference("Companies").child(firebaseAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val company = snapshot.getValue(company::class.java)
                    if(company!=null){
                        val jobId = generateJobId()

                        val job = Job(
                            jobId = jobId,
                            postion = postion,
                            description = description,
                            skills = skills,
                            workExperience = workExperience,
                            location = location,
                            companyName = company.companyName,
                            salary = salary,
                            duration = duration
                        )
                        val jobRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
                        jobRef.setValue(job).addOnCompleteListener {
                            task -> if(task.isSuccessful){
                                LoadingDialog.hideDialog()
                            Toast.makeText(requireContext(), "Job Uploaded successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(requireContext(), CompanyMainActivity::class.java)


                        }else{
                            Toast.makeText(requireContext(), "Something went wrong!",Toast.LENGTH_LONG).show()
                        }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_add_job, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        upload = binding.upload

        upload.setOnClickListener {
            // Fetch values when the upload button is clicked
            val position = binding.etPosition.text.toString()
            val skills = binding.etSkills.text.toString()
            val workExperience = binding.etWe.text.toString()
            val location = binding.etLocation.text.toString()
            val description = binding.etDescription.text.toString()
            var salary = binding.etSalary.text.toString()
            var duration = binding.etDuration.text.toString()



            if (position.isNotEmpty() && skills.isNotEmpty() && workExperience.isNotEmpty() &&
                location.isNotEmpty() && description.isNotEmpty()) {
                uploadpost(position, skills, workExperience, location, description, salary, duration)
            } else {
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddJobFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddJobFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun generateJobId(): String {
        return FirebaseDatabase.getInstance().getReference("Jobs").push().key ?: ""
    }
}