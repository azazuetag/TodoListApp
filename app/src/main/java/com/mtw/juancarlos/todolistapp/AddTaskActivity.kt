package com.mtw.juancarlos.todolistapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.PersistableBundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_task.*
import android.widget.RadioGroup
import com.mtw.juancarlos.todolistapp.database.AppDatabase
import com.mtw.juancarlos.todolistapp.database.TaskEntry
import com.mtw.juancarlos.todolistapp.helper.doAsync
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        // Extra for the task ID to be received in the intent
        val EXTRA_TASK_ID = "extraTaskId"
        // Extra for the task ID to be received after rotation
        val INSTANCE_TASK_ID = "instanceTaskId"
        // Constants for priority
        val PRIORITY_HIGH = 1
        val PRIORITY_MEDIUM = 2
        val PRIORITY_LOW = 3
        // Constant for default task id to be used when not in update mode
        private val DEFAULT_TASK_ID = -1
        // Constant for logging
        private val TAG = AddTaskActivity::class.java.simpleName
    }

    private var mTaskId = DEFAULT_TASK_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        saveButton.setOnClickListener {
            onSaveButtonClicked()
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            saveButton.text = getString(R.string.update_button).toString()
            if (mTaskId === DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getLongExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID.toLong()).toInt()
                doAsync{
                    val task = AppDatabase.getInstance(this@AddTaskActivity)?.taskDao()?.loadTaskById(mTaskId.toLong())
                    runOnUiThread{
                        populateUI(task!!)
                    }

                }.execute()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private fun populateUI(task: TaskEntry) {
        if (task == null) return
        editTextTaskDescription.setText(task.description)
        setPriorityInViews(task.priority)
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {
        // Not yet implemented
        // TODO (8) Save Task
        val descripcion = editTextTaskDescription.text.toString()
        val prioridad = getPriorityFromViews()

        val taskEntry = TaskEntry(description = descripcion,priority = prioridad, updatedAt = Date())
        doAsync{
            if (mTaskId == DEFAULT_TASK_ID){
                AppDatabase.getInstance(this)!!.taskDao().insertTask(taskEntry)
            } else{
                taskEntry.id = mTaskId.toLong()
                AppDatabase.getInstance(this)!!.taskDao().updateTask(taskEntry)
            }
            finish()
        }.execute()

    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    fun getPriorityFromViews(): Int {
        var priority = 1
        val checkedId = (findViewById<View>(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when (checkedId) {
            R.id.radButton1 -> priority = PRIORITY_HIGH
            R.id.radButton2 -> priority = PRIORITY_MEDIUM
            R.id.radButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton3)
        }
    }


}
