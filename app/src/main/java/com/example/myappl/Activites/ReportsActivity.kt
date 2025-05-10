package com.example.myappl.Activites

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappl.Adapter.ReportsAdapter
import com.example.myappl.Add_signal_Activity
import com.example.myappl.R
import com.example.myappl.Signal_ditail_Activity
import com.example.myappl.classes.Report
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ReportsActivity : AppCompatActivity() {

    private lateinit var Add : FloatingActionButton
    private lateinit var searchView :EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var reportsList: ArrayList<Report>
    private lateinit var adapter: ReportsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports_recycler)



        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()


        recyclerView = findViewById(R.id.reportsRecyclerView)
        Add = findViewById(R.id.fab)
        searchView = findViewById(R.id.search)
        recyclerView.layoutManager = GridLayoutManager(this@ReportsActivity, 1)

        reportsList = ArrayList()
        adapter = ReportsAdapter(this,reportsList)
        recyclerView.adapter = adapter

        fetchReportsFromFirestore(dialog)
        Add.setOnClickListener{ var intent=Intent(this,Add_signal_Activity::class.java)
            startActivity(intent)}

        searchView.doOnTextChanged { text, _, _, _ ->
           if (text != null) {
             val  listeNouveaux = reportsList.filter { it.titre.lowercase().startsWith(text.toString().lowercase()) }
               adapter.searchDataList(ArrayList(listeNouveaux))

           }


       }



    }



    private fun fetchReportsFromFirestore(dialog: AlertDialog) {
        FirebaseFirestore.getInstance().collection("reports")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                reportsList.clear()
                for (document in result) {
                    val report = Report(
                        titre = document.getString("titre") ?: "",
                        description = document.getString("description") ?: "",
                        category = document.getString("typeProblem") ?: "",
                        location = document.getString("location") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                    reportsList.add(report)
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()

                if (reportsList.isEmpty()) {
                    Toast.makeText(this, "Il n'y a aucun rapport.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                dialog.dismiss()
                Toast.makeText(this, "Erreur lors de la récupération des données: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}
