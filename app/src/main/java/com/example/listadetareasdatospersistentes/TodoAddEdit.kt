package com.example.listadetareasdatospersistentes

import TodoItems.TodoDb
import TodoItems.TodoItemData
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_todo_item.*
import kotlinx.android.synthetic.main.activity_todo_add_edit.*
import java.text.SimpleDateFormat
import java.util.*

class TodoAddEdit : AppCompatActivity() {
    var todoDbController:TodoDb.Controller? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_add_edit)
        /* Back button support */
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        /* formulario */
        formSetUp()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun formSetUp() {
        when(intent.getStringExtra("TYPE")) {
            "ADD" -> formSetUpAdd()
            "EDIT" -> formSetUpEdit()
        }
        vTodoInputDate.setOnClickListener {
            showDatePicker()
        }
        vTodoInputImage.setOnFocusChangeListener { view, isFocus ->
            val uri = vTodoInputImage.text.toString().trim()
            if(!isFocus && uri.isNotEmpty()) {
                updateImage(uri)
            }
        }
        vTodoBtnCancel.setOnClickListener { onBackPressed() }
    }
    fun formSetUpAdd(){
        setTitle("Agregar tarea")
        todoDbController = TodoDb.Controller(this)
        var todoItem: TodoItemData
        vTodoBtnSave.setOnClickListener {
            // Validaciones
            val intent = Intent().apply {
                putExtra("TITLE", vTodoInputTitle.text.toString())
                putExtra("MESSAGE", vTodoInputMessage.text.toString())
                putExtra("DATE", vTodoInputDate.text.toString())
                putExtra("IMAGE_URI", vTodoInputImage.text.toString())
            }
            todoItem = TodoItemData(intent.getIntExtra("ID", -1),vTodoInputTitle.text.toString(),vTodoInputMessage.text.toString(),vTodoInputDate.text.toString(),vTodoInputImage.text.toString())
            todoDbController!!.insert(todoItem)
            setResult(Activity.RESULT_OK, intent)
            onBackPressed()
        }
    }
    fun formSetUpEdit(){
        setTitle("Editar tarea")
        todoDbController = TodoDb.Controller(this)
        var todoItem: TodoItemData
        vTodoInputTitle.setText(intent.getStringExtra("TITLE"))
        vTodoInputMessage.setText(intent.getStringExtra("MESSAGE "))
        vTodoInputDate.setText(intent.getStringExtra("DATE"))
        intent.getStringExtra("IMAGE_URI")?.let {
            vTodoInputImage.setText(it)
            updateImage(it)
        }
        vTodoBtnSave.setOnClickListener {
            // Validaciones
            val intent = Intent().apply {
                putExtra("ID", intent.getIntExtra("ID", -1))
                putExtra("TITLE", vTodoInputTitle.text.toString())
                putExtra("MESSAGE", vTodoInputMessage.text.toString())
                putExtra("DATE", vTodoInputDate.text.toString())
                putExtra("IMAGE_URI", vTodoInputImage.text.toString())
            }
            println(intent.getIntExtra("id",-1).toInt())
            todoItem = TodoItemData(intent.getIntExtra("ID", -1),vTodoInputTitle.text.toString(),vTodoInputMessage.text.toString(),vTodoInputDate.text.toString(),vTodoInputImage.text.toString())
            todoDbController!!.update(todoItem)
            setResult(Activity.RESULT_OK, intent)
            onBackPressed()
        }

    }

    fun showDatePicker() {
        var formatDate = SimpleDateFormat("dd/MM/YYYY", Locale.US)
        val getDate = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            val selectDate = Calendar.getInstance()
            selectDate.set(Calendar.YEAR, year)
            selectDate.set(Calendar.MONTH, month)
            selectDate.set(Calendar.DAY_OF_MONTH, day)
            val date = formatDate.format(selectDate.time)
            vTodoInputDate.setText(date)
        }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }
    fun updateImage(uri:String) {
        Picasso.get().load(uri).into(vImagePreview)
    }
}