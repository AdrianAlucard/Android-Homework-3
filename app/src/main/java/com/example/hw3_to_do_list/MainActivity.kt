package com.example.hw3_to_do_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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

    override fun onRestart() {
        Log.d(TAG, "Restarting MainActivity")
        super.onRestart()
        load()
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
        } else {
            Log.d(TAG, "No shared pref data to load")
        }
    }

    fun addToDoClick(view: View) {
        Log.d(TAG, "Starting second activity")
        val secondActivity = Intent(this, SecondActivity::class.java)
        startActivity(secondActivity)
    }
}
