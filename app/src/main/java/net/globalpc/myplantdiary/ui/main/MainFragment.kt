package net.globalpc.myplantdiary.ui.main

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
// import kotlinx.android.synthetic.main.fragment_main.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED

import net.globalpc.myplantdiary.R
import net.globalpc.myplantdiary.databinding.FragmentMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class MainFragment : Fragment() {

    private var uri: Uri? = null
    private lateinit var currentImagePath: String
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

            prepTakePhoto()
            // getPhoto()
            // getCameraPicture();
        }
    }

    private fun getCameraPicture() {

        if( hasCameraPermission() === PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED ) {

            // The user has already granted permissions for these activities. Toggle the camera!
            invokeTheCamera();
        }
        else {

            // The user has not granted permissions, so we must request.
            requestMultiplePermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private val requestMultiplePermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions() ) {
        resultsMap ->

        var permissionGranted = false

        resultsMap.forEach {
            if( it.value == true ) {
                permissionGranted = it.value
            }
            else {
                permissionGranted = it.value;
                return@forEach
            }
        }

        if( permissionGranted ) {
            invokeTheCamera()
        }
        else {
            Toast.makeText( context, getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG).show();
        }
    }

    private fun invokeTheCamera() {
        // var i = 1 + 1;

        val file = createImageFile()

        try {
            uri = context?.let { FileProvider.getUriForFile(it, "net.globalpc.myplantdiary.fileprovider", file) };
        }
        catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}" );
            var foo = e.message
        }
    }

    private fun createImageFile(): File {

        val timestamp = SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( Date() );
        val imageDir = activity?.getExternalFilesDir( Environment.DIRECTORY_PICTURES );

        return File.createTempFile(
            "Specimen_${timestamp}",
            ".jpg",
            imageDir
        ).apply {  currentImagePath = absolutePath };

        getTheCameraImageFile.launch(uri)
    }

    private val getTheCameraImageFile = registerForActivityResult( ActivityResultContracts.TakePicture() ) {
        success ->

            if( success ) {
                Log.i( TAG, "Image Location: $uri" );
                binding.imgPlant.setImageURI( uri );
            }
            else {
                Log.e( TAG, "Image not saved. $uri" );
            }
    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission( requireContext(), android.Manifest.permission.CAMERA);
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission( requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

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

    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
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

        // var i = 1 + 1

        val file = createImageFile()

        try {
            uri = FileProvider.getUriForFile(requireContext(), "net.globalpc.myplantdiary.fileprovider", file)
        }
        catch ( e: Exception ) {
            Log.e( TAG, "Error: ${e.message}")
            var foo = e.message
        }

        // getCameraImageFile.launch( null )

        // getCameraImage.launch(uri)
        getCameraImage.launch( null )

        /*
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent -> takePictureIntent.resolveActivity( requireContext().packageManager )?.also {
            startActivityForResult( takePictureIntent, CAMERA_REQUEST_CODE )
        }
        } */
    }

    /*
    private val getCameraImageFile = registerForActivityResult( ActivityResultContracts.TakePicture() ) {

        success ->

        // if( success != null && success == true ) {
            // if we are here, we have a valid intent.
           // val photoFile : File = createImageFile()
            //photoFile?.also {
              //  FileProvider.getUriForFile( context, "net.globalpc.myplanydiary.fileprovider", success  )
            // }
        // }
        // else {
            // Toast.makeText( context, "Unable to save photo.", Toast.LENGTH_LONG ).show()
            // Log.e(TAG,"Unable to save photo.")
        // }
    }
    */

    private val getCameraImage = registerForActivityResult( ActivityResultContracts.TakePicturePreview() ) {
        bitmap -> if( bitmap != null ) {
            // Log.i(TAG,"Image Location: $uri")
            Log.i(TAG,"Image Location: $uri")

            // val imageBitmap = data!!.extras!!.get("data") as Bitmap

            binding.imgPlant.setImageBitmap(bitmap)
        }
        else {
            // Log.e(TAG,"Image not saved. $uri")
            Log.e(TAG,"Image not saved.")
        }
    }

    /* private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent -> takePictureIntent.resolveActivity( requireContext().packageManager )?.also {
            startActivityForResult( takePictureIntent, CAMERA_REQUEST_CODE )
        }
        }
    } */

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == RESULT_OK ) {

            if( requestCode == CAMERA_REQUEST_CODE ) {
                // now we can get the thumbnail

                val imageBitmap = data!!.extras!!.get("data") as Bitmap

                binding.imgPlant.setImageBitmap(imageBitmap)
            }
        }

    }*/

    /**
     * See if we have permission or not.
     */

    private fun prepTakePhoto() {
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

                    // Permission granted. Let's do stuff.
                    takePhoto()
                }
                else {
                    Toast.makeText( requireContext(), "Unable to take photo without permission.", Toast.LENGTH_LONG ).show();
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

    }
}