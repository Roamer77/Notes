package com.val.trymvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.val.trymvvm.Entities.Note;
import com.val.trymvvm.ViewModel.NoteViewModel;
import com.val.trymvvm.adaptors.RecyclerViewAdaptor;
import com.val.trymvvm.interfaces.OnItemClick;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdaptor recyclerViewAdaptor;


    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");
        recyclerViewAdaptor = new RecyclerViewAdaptor();

        actionButton = findViewById(R.id.fab_create_new_note);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdaptor);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {

                recyclerViewAdaptor.submitList(notes); //связан с темой ListAdaptor. Обновляет лист
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //делаем запрос в другую активити. По завершению она отправит в эту активити такое же значение как и ADD_NOTE_REQUEST
                //ADD_NOTE_REQUEST нужно что бы однозначно идентифицировать активити
                Intent intent = new Intent(MainActivity.this, CreateNewNoteActivity.class);
                //текущая активити становиться родительской для активити которая будет вызвана
                // из-за жтого этот способ возможен
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(recyclerViewAdaptor.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(), "Удалена одна запись", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);
        recyclerViewAdaptor.setOnItemClickListener(new OnItemClick() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, CreateNewNoteActivity.class);
                intent.putExtra(CreateNewNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(CreateNewNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(CreateNewNoteActivity.EXTRA_PRIORITY, note.getPriority());
                intent.putExtra(CreateNewNoteActivity.EXTRA_ID, note.getId());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }


    // перегрузим данный метод для того что бы получить результирующие данные от
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // сравниваем ADD_NOTE_REQUEST и resultCode оторый мы отправили со второй активити
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CreateNewNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CreateNewNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(CreateNewNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(getApplicationContext(), "Добавлена новая запись", Toast.LENGTH_LONG).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CreateNewNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CreateNewNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(CreateNewNoteActivity.EXTRA_PRIORITY, 1);
            int id = data.getIntExtra(CreateNewNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getApplicationContext(), "Запись не может быть обновлена", Toast.LENGTH_LONG).show();
            }

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(getApplicationContext(), "Запись обновлена", Toast.LENGTH_LONG).show();

        }
    }


}
