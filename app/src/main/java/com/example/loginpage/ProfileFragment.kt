package com.example.loginpage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.loginpage.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Profile"
        toolbar.setNavigationOnClickListener {
            // Kembali ke fragment Home
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, HomeFragment())
                ?.commit()

            // Reset bottom navigation ke Home
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.selectedItemId = R.id.nav_home
        }

        // Set data profil
        binding.tvDeveloperName.text = "Tahnia Siti Aisah"
        binding.tvNimLabel.text = "NIM: 2457301141"
        binding.tvJurusanLabel.text = "Sistem Informasi"

        // Setup Social Media Click Listeners
        setupSocialMediaLinks()
    }

    private fun setupSocialMediaLinks() {
        // GitHub
        binding.layoutGithub.setOnClickListener {
            openUrl("https://github.com/tahnia13")
        }

        // Instagram
        binding.layoutInstagram.setOnClickListener {
            openUrl("https://www.instagram.com/thnia.aish")
        }

        // LinkedIn
        binding.layoutLinkedin.setOnClickListener {
            openUrl("https://www.linkedin.com/in/tahnia-siti-aisah-885214344/?isSelfProfile=false")
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Tidak dapat membuka link", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}