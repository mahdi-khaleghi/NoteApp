package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEdt: EditText
    lateinit var noteDescriptionEdt: EditText
    lateinit var addUpdateBtn: Button
    lateinit var viewModel: NoteViewModel
    var noteID = -1
    var flag: Boolean = false

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edite_note)

        noteTitleEdt = findViewById(R.id.idEdtNoteTitle)
        noteDescriptionEdt = findViewById(R.id.idEdtNoteDescription)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        val noteType = intent.getStringExtra("noteType")
        if (noteType == "Edit") {
            noteTitleEdt.isEnabled = false
            noteDescriptionEdt.isEnabled = false
            addUpdateBtn.text = "Edit"
            flag = true

            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteID", 0)

//            addUpdateBtn.text = "Update"
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDescription)
        } else {
            addUpdateBtn.text = "Save Note"
        }

        addUpdateBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()
            val sdf = SimpleDateFormat("dd,MM,yyyy - HH:mm")
            val currentDate: String = sdf.format(Date())

            if (noteType == "Edit") {
                if (flag) {
                    noteTitleEdt.isEnabled = true
                    noteDescriptionEdt.isEnabled = true
                    addUpdateBtn.text = "Update"
                    flag = false
                    return@setOnClickListener
                } else {
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                        val updateNote = Note(noteTitle, noteDescription, currentDate)
                        updateNote.id = noteID
                        viewModel.updateNote(updateNote)
                        Toast.makeText(this, "Note Updated...", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    viewModel.addNote(Note(noteTitle, noteDescription, currentDate))
                    Toast.makeText(this, "Note Added...", Toast.LENGTH_SHORT).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}