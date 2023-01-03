package net.globalpc.myplantdiary.ui.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
// import kotlinx.android.synthetic.main.fragment_main.*

import net.globalpc.myplantdiary.R
import net.globalpc.myplantdiary.databinding.FragmentMainBinding
import java.util.jar.Manifest


class MainFragment : Fragment() {

    private val CAMERA_REQUEST_CODE: Int = 1998
    val CAMERA_PERMISSION_REQUEST_CODE: Int = 1997

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // return inflater.inflate(R.layout.fragment_main, container, false)

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.plants.observe(viewLifecycleOwner, Observer { plants
            ->
            binding.actPlantName.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    plants
                )
            )
        })

        binding.btnTakePhoto.setOnClickListener {
            // prepTakePhoto()

            getPhoto()
        }
    }

    private fun getPhoto() {
        activity?.let {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                invokeCamera()
            } else {
                permReqLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                invokeCamera()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.camera_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun invokeCamera() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent -> takePictureIntent.resolveActivity( requireContext().packageManager )?.also {
            startActivityForResult( takePictureIntent, CAMERA_REQUEST_CODE )
        }
        }
        // var i = 1 + 1
    }

    /* private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent -> takePictureIntent.resolveActivity( requireContext().packageManager )?.also {
            startActivityForResult( takePictureIntent, CAMERA_REQUEST_CODE )
        }
        }
    } */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == RESULT_OK ) {

            if( requestCode == CAMERA_REQUEST_CODE ) {
                // now we can get the thumbnail

                val imageBitmap = data!!.extras!!.get("data") as Bitmap

                binding.imgPlant.setImageBitmap(imageBitmap)
            }
        }

    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == RESULT_OK ) {

            if( requestCode == CAMERA_REQUEST_CODE ) {
                // now we can get the thumbnail

                val imageBitmap = data!!.extras!!.get("data") as Bitmap

                binding.imgPlant.setImageBitmap(imageBitmap)
            }
        }

    } */

    /**
     * See if we have permission or not.
     */

    /* private fun prepTakePhoto() {
        if( ContextCompat.checkSelfPermission( requireContext(), android.Manifest.permission.CAMERA  )  == PackageManager.PERMISSION_GRANTED ) {
            takePhoto()
        }
        else {
            val permissionRequest = arrayOf( android.Manifest.permission.CAMERA )

            requestPermissions( permissionRequest, CAMERA_PERMISSION_REQUEST_CODE )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when( requestCode ) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if( grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePhoto()
                }
                else {
                    Toast.makeText( requireContext(), "Unable to take photo without permission.", Toast.LENGTH_LONG )
                }
            }
        }
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent -> takePictureIntent.resolveActivity( requireContext().packageManager )?.also {
                startActivityForResult( takePictureIntent, CAMERA_REQUEST_CODE )
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == RESULT_OK ) {

            if( requestCode == CAMERA_REQUEST_CODE ) {
                // now we can get the thumbnail

                val imageBitmap = data!!.extras!!.get("data") as Bitmap

                binding.imgPlant.setImageBitmap(imageBitmap)
            }
        }

    } */
}