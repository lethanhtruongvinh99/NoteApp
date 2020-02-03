package com.example.testnoteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {
    public static String SP_Key = "Shared Preferences";
    public static ArrayList<Note> dataSet, filter;
    public static List<Integer> oldPos = new ArrayList<>();
    public static int EDIT = 2;
    public static int REQ_CODE = 3;
    public static int ADD = 1;
    public static int delItem, delItemOldPos;
    ListView mListView;
    public static NoteAdapter adapter;
    EditText searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Note note1 = new Note("Note1","This is Note 1 ");
//        Note note2 = new Note("Note2","This is Note 2 ");
//        Note note3 = new Note("Note3","This is Note 2 ");
//        Note note4 = new Note("Note4","This is Note 2 ");

//        SharedPreferences sharedPreferences = getSharedPreferences(SP_Key, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
        loadData();


//
//        dataSet.add(note1);
//        dataSet.add(note2);
//        dataSet.add(note3);
//        dataSet.add(note4);
        mListView = (ListView)findViewById(R.id.listView);

        adapter = new NoteAdapter(this,dataSet);

        mListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                int filterPos = position; // save the Position when filtering
                if (oldPos.size()!= 0) {
                        position = oldPos.get(position);
                }
                intent.putExtra("POSITION", position);
                intent.putExtra("TIT", dataSet.get(position).getTitle());
                intent.putExtra("CONTENT", dataSet.get(position).getContent());
                intent.putExtra("DATE",dataSet.get(position).getDate());
                intent.putExtra("TAG",dataSet.get(position).getTag());

                startActivityForResult(intent, REQ_CODE);

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                delItemOldPos = position;
                if (oldPos.size()!=0){
                    position = oldPos.get(position);
                }
                else position = position;
                delItem = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(delItem);
                                saveData();

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                adapter.notifyDataSetChanged();

                return true;
            }
        });


        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                startActivityForResult(intent, REQ_CODE);
            }
        });
        searchView = (EditText) findViewById(R.id.search);


        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == MainActivity.EDIT) {
                String newTit, newContent, newDate, newTag;
                int pos = data.getIntExtra("POSITION", -1);
                newTit = data.getStringExtra("TIT");
                newContent = data.getStringExtra("CONTENT");
                newDate = data.getStringExtra("DATE");
                newTag = data.getStringExtra("TAG");
                dataSet.get(pos).setTitle(newTit);
                dataSet.get(pos).setContent(newContent);
                dataSet.get(pos).setDate(newDate);
                dataSet.get(pos).setTag(newTag);
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == REQ_CODE) {
            if (resultCode == MainActivity.ADD) {
                String newTit, newContent, newDate, newTag;
                int pos = data.getIntExtra("POSITION", -1);
                newTit = data.getStringExtra("TIT");
                newContent = data.getStringExtra("CONTENT");
                newDate = data.getStringExtra("DATE");
                newTag = data.getStringExtra("TAG");
                if (newTit == null) {
                    newTit = "NewNote";
                }
                if (newTag == null){
                    newTag = "Default";
                }
                if (newContent == null){
                    newContent = "New Content";
                }
                Note newNote = new Note(newTit, newContent, newDate, newTag);
                dataSet.add(newNote);
                adapter.notifyDataSetChanged();
            }
        }
        saveData();
    }
    public void loadData(){

        SharedPreferences sharedPreferences = getSharedPreferences("SP_Note",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("SPNOTE",null);
        Type type =  new TypeToken<ArrayList<Note>>(){}.getType();
        dataSet = gson.fromJson(json,type);
        if (dataSet == null){
            dataSet = new ArrayList<>();
        }
    }
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SP_Note",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataSet);
        editor.putString("SPNOTE", json);
        editor.apply();

    }


}
