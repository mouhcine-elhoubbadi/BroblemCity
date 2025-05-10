package com.example.myappl

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.myappl.Activites.ReportsActivity
import com.example.myappl.location.MapPickerActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream


class Add_signal_Activity : AppCompatActivity() {


    private lateinit var imageView: ImageView
    private lateinit var spinner: Spinner
    private lateinit var descriptionInput: EditText
    private lateinit var titreInput: EditText
    private lateinit var locationButton: Button
    private lateinit var sendBtn: Button
    private lateinit var txtSelectedProblem: TextView

    private var imageUri: Uri? = null
    private var locationText: String = ""

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2
    private val CAMERA_PERMISSION_REQUEST_CODE = 101

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    lateinit var cloudinary: Cloudinary
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_signal)

        val config = hashMapOf(
            "cloud_name" to "dkvqk1pfx",
            "api_key" to "794498174883981",
            "api_secret" to "E32qWVjNIXdtA-V8MhkoE_Px714"
        )
        cloudinary = Cloudinary(config)




        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        imageView = findViewById(R.id.image)
        spinner = findViewById(R.id.spiner)
        descriptionInput = findViewById(R.id.descriptionInput)
        titreInput = findViewById(R.id.titreInput)
        locationButton = findViewById(R.id.location)
        sendBtn = findViewById(R.id.sendbtn)
        txtSelectedProblem = findViewById(R.id.Affiche)

        setupSpinner()

        sendBtn.setOnClickListener {
            uploadData() }
        locationButton.setOnClickListener {
            getLocationAutomatically()
        }
        imageView.setOnClickListener { showImagePickerDialog() }
    }

    private fun setupSpinner() {
        val problemsCity = arrayOf(
            "Voirie et routes", "Éclairage public", "Déchets et propreté", "Eau et assainissement",
            "Sécurité publique", "Animaux errants", "Mobilier urbain", "Autre"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, problemsCity)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                txtSelectedProblem.text = problemsCity[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Caméra", "Galerie")
        AlertDialog.Builder(this)
            .setTitle("Choisir l'image depuis")
            .setItems(options) { _, which ->
                if (which == 0) requestCameraPermissionAndOpenCamera()
                else openGallery()
            }.show()
    }

    private fun requestCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_", ".jpg",
            getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        )
        imageUri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        startActivityForResult(intent, CAMERA_REQUEST)
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(Intent.createChooser(intent, "choisir photo "), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            val location_name = data.getStringExtra("location_name") ?: "Lieu inconnu"
            locationText = "$location_name"
            locationButton.text = "Localisation : $locationText"
        }


        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)
                }
                CAMERA_REQUEST -> imageView.setImageURI(imageUri)
            }
        }
    }

    private fun getLocationAutomatically() {
        val intent = Intent(this, MapPickerActivity::class.java)
        startActivityForResult(intent, 200)
    }

    private fun uploadData() {
        val titre = titreInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val typeProblem = txtSelectedProblem.text.toString()

        if (titre.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "voulez remplir les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_layout)
            .setCancelable(false)
            .create()
        dialog.show()

        if (imageUri != null) {
            val filePath = FileUtils.getFile(this, imageUri!!)
            Thread {
                try {
                    val uploadResult = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap())
                    val imageUrl = uploadResult["secure_url"] as String

                    runOnUiThread {

                        saveReportToFirestore(titre, description, typeProblem, locationText, imageUrl, dialog)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        dialog.dismiss()
                        Toast.makeText(this, "L'image n'a pas pu être chargée: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        } else {
            saveReportToFirestore(titre, description, typeProblem, locationText, "", dialog)
        }
    }
    object FileUtils {
        fun getFile(context: Context, uri: Uri): File? {
            return when (uri.scheme) {
                "file" -> File(uri.path!!)
                "content" -> {
                    val fileName = "temp_${System.currentTimeMillis()}.jpg"
                    val file = File(context.cacheDir, fileName)
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                    file
                }
                else -> null
            }
        }

    }

    private fun saveReportToFirestore(
        titre: String,
        description: String,
        typeProblem: String,
        location: String,
        imageUrl: String,
        dialog: AlertDialog
    ) {
        val report = hashMapOf(
            "titre" to titre,
            "description" to description,
            "typeProblem" to typeProblem,
            "location" to location,
            "imageUrl" to imageUrl,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("reports")
            .add(report)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "Le rapport a été envoyé avec succès.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ReportsActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Échec de l'envoi du rapport", Toast.LENGTH_SHORT).show()
            }
    }
}
