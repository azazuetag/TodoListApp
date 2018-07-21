package com.mtw.juancarlos.todolistapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mtw.juancarlos.todolistapp.database.TaskEntry

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.helper.ItemTouchHelper
import com.mtw.juancarlos.todolistapp.database.AppDatabase
import com.mtw.juancarlos.todolistapp.helper.doAsync


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: TaskAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    val taskList: List<TaskEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //addTasks()
        viewManager = LinearLayoutManager(this)
        viewAdapter = TaskAdapter(taskList, this, { task: TaskEntry -> onItemClickListener(task) })

        recyclerViewTasks.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // Set the layout for the RecyclerView to be a linear layout, which measures and
            // positions items within a RecyclerView into a linear list
            layoutManager = viewManager

            // Initialize the adapter and attach it to the RecyclerView
            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        }


        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            //viewHoder tiene un registro de la tabla
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Here is where you'll implement swipe to delete


                doAsync{
                    val position = viewHolder.adapterPosition
                    val tasks= viewAdapter.getTasks()
                    AppDatabase.getInstance((this@MainActivity))?.taskDao()?.deleteTask(tasks[position])
                    retrieveTasks()
                }.execute()

            }
        }).attachToRecyclerView(recyclerViewTasks)

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        fab.setOnClickListener { view ->
            // Create a new intent to start an AddTaskActivity
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*fun addTasks(){
        taskList.add(TaskEntry(1,"Tarea 1",1,Date()))
        taskList.add(TaskEntry(2,"Tarea 2",2,Date()))

    }*/

    private fun onItemClickListener(task: TaskEntry) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        val intent = Intent(this,AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, task.id)
        startActivity(intent)
        //Toast.makeText(this, "Clicked item" + task.description, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveTasks()
    }

    private fun retrieveTasks() {
        doAsync {
            //POr que esta dentro de otro objeto
            val tasks = AppDatabase.getInstance(this@MainActivity)?.taskDao()?.loadAllTask()
            runOnUiThread {
                // esto hace que se ejecute en el hilo orincipal
                viewAdapter.setTask(tasks!!)
            }
        }.execute()
    }
}
    //TODO (9) Consultar todas las tareas en onResume
    //TODO (10) Crear clase doAsync : AsyncTask quitar allowmainThreadQuery
    //TODO (11) Implementar DeleteTask swipe
    // TODO (12) Query parameter en TaskDao loadTaskbyId
    // TODO (14) LOG onResume
    //TODO (16) Change TaskDao add LiveData to loadAllTask()
    //TODO (17) Change TaskDao add LiveData to loadTaskById
    //TODO (18) ViewModel to MainActivity
    //TODO (19) ADD ViewModel to AddTaskActivity