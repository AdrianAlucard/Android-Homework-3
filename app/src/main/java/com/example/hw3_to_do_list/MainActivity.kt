package com.example.hw3_to_do_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val FILE_NAME = "TaskList"
    private val TASK_LIST = "tasks"
    private var taskList = mutableListOf<String>()
    private lateinit var taskListAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load()
    }

    override fun onResume() {
        Log.d(TAG, "Resuming MainActivity")
        super.onResume()
        reload()
    }

    private fun load() {
        Log.d(TAG, "Loading shared pref data")
        val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val tasks = sharedPreferences.getString(TASK_LIST, "") ?: ""

        if(tasks.isNotEmpty()) {
            Log.d(TAG, "Shared preferences not empty :: Loading shared preferences")
            val sType = object : TypeToken<MutableList<String>>() {}.type // create object of typetoken

            val savedTaskList = Gson().fromJson<MutableList<String>>(tasks, sType)
            taskList = mutableListOf()
            taskList.addAll(savedTaskList)
            Log.d(TAG, "Saved tasks: \n $taskList")
            taskListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
            task_list.adapter = taskListAdapter
            setOnTaskListListener()
        } else {
            Log.d(TAG, "No shared pref data to load")
        }
    }

    private fun reload() {
        // reload logic to avoid extra adapter object creations
        // just reload the list from the json and pass updates to instantiated objects
        Log.d(TAG, "Reloading MainActivity with refreshed data")
        val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val tasks = sharedPreferences.getString(TASK_LIST, "") ?: ""

        if(tasks.isNotEmpty()) {
            val sType = object : TypeToken<MutableList<String>>() {}.type
            val savedTaskList = Gson().fromJson<MutableList<String>>(tasks, sType)
            taskList.clear()
            taskList.addAll(savedTaskList)
            taskListAdapter.notifyDataSetChanged()
        }
    }

    private fun setOnTaskListListener() {
        task_list.setOnItemLongClickListener{parent, view, position, id ->
            val taskToDelete = parent.getItemAtPosition(position).toString()
            taskList.remove(taskToDelete)
            val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(TASK_LIST, Gson().toJson(taskList))
            editor.apply()
            taskListAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Deleted $taskToDelete", Toast.LENGTH_SHORT).show()
            if(taskList.isEmpty()) Toast.makeText(this, "All tasks are completed!", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun addToDoClick(view: View) {
        Log.d(TAG, "Starting second activity")
        val secondActivity = Intent(this, SecondActivity::class.java)
        startActivity(secondActivity)
    }
}
