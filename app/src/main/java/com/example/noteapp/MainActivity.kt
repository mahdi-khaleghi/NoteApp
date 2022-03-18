package com.example.noteapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)

        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.layoutManager = LinearLayoutManager(this)
        notesRV.adapter = noteRVAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(
            this,
            Observer { list ->
                list?.let { noteRVAdapter.updateList(it) }
            })
        addFAB.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEditNoteActivity::class.java))
        }
    }

    @SuppressLint("ResourceType", "InflateParams")
    override fun onDeleteIconClick(note: Note) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Note will be deleted!")
            .setPositiveButton("Delete",
                DialogInterface.OnClickListener { dialog, id ->
                    viewModel.deleteNote(note)
                    Toast.makeText(this, "${note.noteTitle} Deleted!", Toast.LENGTH_SHORT).show()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        builder.create()
        builder.show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
    }
}