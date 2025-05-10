package com.example.myappl.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappl.Activites.ReportsActivity
import com.example.myappl.R
import com.example.myappl.Signal_ditail_Activity
import com.example.myappl.classes.Report



class ReportsAdapter(private var context: ReportsActivity, private var reportsList: List<Report>) :
    RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {



    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val titre: TextView = itemView.findViewById(R.id.titreText)
            val description: TextView = itemView.findViewById(R.id.descriptionText)
            val typeProblem: TextView = itemView.findViewById(R.id.typeProblemText)
            val location: TextView = itemView.findViewById(R.id.locationText)
            val image: ImageView = itemView.findViewById(R.id.imageReport)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val report = reportsList[position]
                    val intent = Intent(context, Signal_ditail_Activity::class.java).apply {
                        putExtra("typeprobleme", report.category)
                        putExtra("reportTitle", report.titre)
                        putExtra("reportDescription", report.description)
                        putExtra("reportImage", report.imageUrl)
                        putExtra("time", report.timestamp)
                        putExtra("location", report.location)

                    }
                    context.startActivity(intent)
                }
            }
        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_recycler_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
       // Glide.with(context).load(reportsList[position].imageUrl).into(holder.image)
        val report = reportsList[position]
        holder.titre.text = report.titre
        holder.description.text = report.description
        holder.typeProblem.text = report.category
        holder.location.text = report.location


        if (report.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(report.imageUrl)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.sample_image)
        }



    }

    override fun getItemCount(): Int = reportsList.size

    fun searchDataList(searchList: ArrayList<Report>) {
        this.reportsList = searchList
        notifyDataSetChanged()
    }

}
