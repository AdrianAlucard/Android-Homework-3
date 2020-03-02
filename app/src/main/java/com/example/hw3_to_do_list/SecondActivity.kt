package com.example.hw3_to_do_list

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    private val TAG = "SecondActivity"
    private val FILE_NAME = "TaskList"
    private val TASK_LIST = "tasks"
    private lateinit var taskList:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        taskList = loadTaskList()
    }

    fun goBack(view: View) {
        finish()
    }

    fun saveAndGoBack(view: View) {
        if(isUserTaskValid()) {
                Log.d(TAG, "Save and Go Back $user_task")
                createAndSaveTask(user_task.text.toString())
                Log.d(TAG, "Saved Task and returning to MainActivity")
                finish()
        }
    }

    private fun loadTaskList(): MutableList<String> {
        val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val tasks = sharedPreferences.getString(TASK_LIST, "") ?: ""

        if(tasks.isNotEmpty()) {
            Log.d(TAG, "Shared preferences not empty \n Loading shared preferences")
            val sType = object : TypeToken<List<String>>() {}.type // create object of typetoken

            val savedTaskList = Gson().fromJson<List<String>>(tasks, sType)
            taskList = mutableListOf()
            taskList.addAll(savedTaskList)
            Log.d(TAG, "Saved tasks: \n $taskList")
            return taskList
        }
        return mutableListOf()
    }

    private fun createAndSaveTask(task: String) {
        val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        taskList.add(task)
        Log.d(TAG, "Task List after createAndSave $taskList")
        editor.putString(TASK_LIST, Gson().toJson(taskList))
        editor.apply()
    }

    private fun isUserTaskValid(): Boolean {
        return isUserTextEmpty() && isDuplicateTask()
    }

    private fun isDuplicateTask(): Boolean {
      return when(taskList.contains(user_task.text.toString())) {
            true -> {Log.d(TAG, "user text is duplicate");
                    Toast.makeText(this, "${user_task.text} is a duplicate", Toast.LENGTH_SHORT).show();
                    return false}
            false -> {true}
        }
    }

    private fun isUserTextEmpty(): Boolean {
        return when(user_task.text.toString().isEmpty()) {
            true -> {Log.d(TAG, "user text is empty");
                Toast.makeText(this, "Must enter a task in field", Toast.LENGTH_SHORT).show();
                return false}
            false -> {true}
        }
    }
}
