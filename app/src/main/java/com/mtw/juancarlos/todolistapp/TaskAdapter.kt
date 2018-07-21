package com.mtw.juancarlos.todolistapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mtw.juancarlos.todolistapp.database.TaskEntry
import kotlinx.android.synthetic.main.task_layout.view.*
import java.text.SimpleDateFormat
import android.support.v4.content.ContextCompat
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import java.util.*
import kotlin.collections.ArrayList

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
class TaskAdapter(private var mTaskEntries:List<TaskEntry>, private val mContext: Context, private val clickListener: (TaskEntry) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return TaskViewHolder(layoutInflater.inflate(R.layout.task_layout, parent, false))
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(mTaskEntries[position], mContext, clickListener)
    }

    /**
     * Returns the number of items to display.
     */
    override fun getItemCount(): Int = mTaskEntries.size

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    fun setTask(taskEntries: List<TaskEntry>){
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    fun getTasks(): List<TaskEntry> = mTaskEntries


    // Inner class for creating ViewHolders
    class TaskViewHolder (itemView:View) :RecyclerView.ViewHolder(itemView) {

        fun bind (task:TaskEntry, context: Context, clickListener: (TaskEntry) -> Unit){
                //Set values
                itemView.taskDescription.text = task.description
                itemView.taskUpdatedAt.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(task.updatedAt).toString()
                // Programmatically set the text and color for the priority TextView
                itemView.priorityTextView.text = task.priority.toString()
                val priorityCircle = itemView.priorityTextView.background as GradientDrawable
                // Get the appropriate background color based on the priority
                val priorityColor = getPriorityColor(task.priority,context)
                priorityCircle.setColor(priorityColor)

                itemView.setOnClickListener{ clickListener(task)}
        }

        /*
        Helper method for selecting the correct priority circle color.
        P1 = red, P2 = orange, P3 = yellow
        */
        fun getPriorityColor(priority: Int, mContext: Context): Int {
            var priorityColor = 0

            when (priority) {
                1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
                2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
                3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
                else -> {
                }
            }
            return priorityColor
        }
    }

}
