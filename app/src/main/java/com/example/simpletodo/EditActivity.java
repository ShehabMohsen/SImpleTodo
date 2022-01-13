package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText updateItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateItem = findViewById(R.id.updateItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit task");

        updateItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        //When user is done editing, the button will be used to save the text
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an intent which will contain the results
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                //Pass the results back to the MainActivity
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, updateItem.getText().toString());
                intent.putExtra("Position", getIntent().getIntExtra("Position", 0));
                setResult(RESULT_OK, intent);
                //Go Back to Main Activity
                finish();
;            }
        });

    }
}