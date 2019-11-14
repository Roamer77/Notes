package com.val.trymvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class CreateNewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.val.trymvvm.extraTitle";
    public static final String EXTRA_DESCRIPTION = "com.val.trymvvm.extraDescription";
    public static final String EXTRA_PRIORITY = "com.val.trymvvm.extraPriority";
    public static final String EXTRA_ID = "com.val.trymvvm.extraId";

    private NumberPicker numberPicker;
    private EditText newNoteTitle;
    private EditText newNoteDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);

        newNoteTitle = findViewById(R.id.newNote_title_EditText);
        newNoteDescription = findViewById(R.id.newNote_description_EditText);

        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) { //если в интенте присудствует id то меням title
            Log.e("MyTag","Зашел в if в onCreate");
            getSupportActionBar().setTitle("Edit Note");
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            newNoteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            newNoteDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        } else {
            getSupportActionBar().setTitle("Add Note");
        }
    }

    private void saveNote() {
        String description = newNoteDescription.getText().toString();
        String title = newNoteTitle.getText().toString();
        Integer priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Заполните поля для ввода", Toast.LENGTH_LONG).show();

        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRIORITY, priority);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_TITLE, title);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            intent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_1:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
