package com.example.simpletodo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "item position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    itemsAdapter itemsAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);



        loadItems();


        itemsAdapter.OnLongClickListener onLongCLickListener = new itemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
            //Delete the item from the model
            items.remove(position);
            //Notify the adapter the position at which the model was deleted
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Task removed", Toast.LENGTH_SHORT).show();
            saveItems();
            }
        };



        itemsAdapter.OnClickListener onClickListener = new itemsAdapter.OnClickListener(){
            @Override
            public void onItemClicked(int position){
            Log.d("MainActivity", "Single click at position " + position);
            //Create new activity
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            //Pass the data being edited
            i.putExtra(KEY_ITEM_TEXT, items.get(position));
            i.putExtra("Position", position);
            //display the activity
            EditActivityResultLauncher.launch(i);
            }
        };



        itemsAdapter = new itemsAdapter(items, onLongCLickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                //Add item to model
                items.add(todoItem);
                //Notify Adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
    ActivityResultLauncher<Intent> EditActivityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                String itemText = data.getStringExtra(KEY_ITEM_TEXT);
                int position = data.getExtras().getInt("Position");
                items.set(position, itemText);
                itemsAdapter.notifyItemChanged(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Task updated", Toast.LENGTH_SHORT).show();

            }
            else {
                Log.w("MainActivity", "Unknown call to onActivityResult");
            }
        }
    });

    private File getDataFIle(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFIle(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //This function saves tasks by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFIle(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}