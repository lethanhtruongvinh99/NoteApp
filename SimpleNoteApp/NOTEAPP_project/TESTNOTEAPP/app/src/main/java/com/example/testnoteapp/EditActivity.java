package com.example.testnoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    public String atTit, atContent, atTag, atDate;
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SP_Note",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(MainActivity.dataSet);
        editor.putString("SPNOTE", data);
        editor.apply();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Intent intent = getIntent();
        final EditText tvTit, tvContent, tvTag;
        final TextView tvDate;
        final int pos;
        tvTit = (EditText)findViewById(R.id.edtTit);
        tvContent = (EditText) findViewById(R.id.edtContent);
        tvDate = (TextView) findViewById(R.id.edtDate);
        tvTag = (EditText) findViewById(R.id.edtTag);

        tvTit.setText(intent.getStringExtra("TIT"));
        tvContent.setText(intent.getStringExtra("CONTENT"));
        tvDate.setText(intent.getStringExtra("DATE"));
        tvTag.setText(intent.getStringExtra("TAG"));

        pos = intent.getIntExtra("POSITION",-1);
        tvTit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                atTit = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                atContent = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    atTag = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTit.onEditorAction(EditorInfo.IME_ACTION_DONE);
                Intent intent1 = new Intent();
                intent1.putExtra("POSITION",pos);
                if (atTit==null){
                   atTit = intent.getStringExtra("TIT");
                }
                intent1.putExtra("TIT",atTit);
                if (atContent==null){
                   atContent = intent.getStringExtra("CONTENT");
                }
                intent1.putExtra("CONTENT",atContent);
                if (atTag==null){
                    atTag  = intent.getStringExtra("TAG");
                }
                intent1.putExtra("TAG",atTag);


                //DATE

                SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
                Date now= new Date();
                atDate = format.format(now).toString();

                intent1.putExtra("DATE",atDate);

                if (intent.getStringExtra("TIT")!=null){
                    setResult(MainActivity.EDIT,intent1);
                }
                else setResult(MainActivity.ADD,intent1);
                saveData();
                finish();

            }
        });

    }
}
