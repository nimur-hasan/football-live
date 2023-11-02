package com.nehalappstudio.footballlive.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.nehalappstudio.footballlive.PrivacyPolicy
import com.nehalappstudio.footballlive.R


class More : Fragment() {

    private lateinit var rlAbout: RelativeLayout
    private lateinit var rlRateMe: RelativeLayout
    private lateinit var rlShare: RelativeLayout
    private lateinit var rlPrivacyPolicy: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false);

        rlAbout = view.findViewById(R.id.rlAboutUs)
        rlRateMe = view.findViewById(R.id.rlRateMe)
        rlShare = view.findViewById(R.id.rlShare)
        rlPrivacyPolicy = view.findViewById(R.id.rlPrivacyPolicy)

        rlAbout.setOnClickListener {
            showInfoAlertDialog()
        }

        rlShare.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"

            // Get the package name
            val packageName = requireContext().packageName

            val shareContent = "https://play.google.com/store/apps/details?id="+packageName

            // Set the content to share
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareContent)

            // Create a chooser dialog
            val chooserIntent = Intent.createChooser(
                sharingIntent, "Share via"
            )

            if (sharingIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooserIntent)
            } else {
                // Handle the case where there are no apps to handle the intent
            }
        }

        rlRateMe.setOnClickListener {
            val webUri = Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            startActivity(webIntent)
        }

        rlPrivacyPolicy.setOnClickListener {
            startActivity(Intent(requireContext(), PrivacyPolicy::class.java))
        }

        return view
    }

    private fun showInfoAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("About Us")
        builder.setMessage("Live Football is an app that provides you Live score, schedule and leagues.")
        builder.setPositiveButton("OK") { dialog, _ ->
            // Handle the OK button click (if needed)
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}