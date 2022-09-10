package com.erefem.stargazing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.erefem.stargazing.adapter.LogbookAdapter;
import com.erefem.stargazing.database.AppDatabase;
import com.erefem.stargazing.database.entitas.Logbook;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class  ObsLogBook extends AppCompatActivity {

    RecyclerView recyclerView;
    //List<MyModel> myModelList;
    //CustomAdapter customAdapter;

    ProgressDialog progressDialog;
    private AppDatabase database;
    private LogbookAdapter logbookAdapter;
    private List<Logbook> list = new ArrayList<>();
    private AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obs_log_book);
        recyclerView = findViewById(R.id.rvLogBook);
        setTitle("Observation Log Book");

        database = AppDatabase.getInstance(getApplicationContext());
        list.clear();
        list.addAll(database.logbookDao().getAll());
        logbookAdapter = new LogbookAdapter(getApplicationContext(),list);
        logbookAdapter.setDialog(new LogbookAdapter.Dialog() {
            @Override
            public void onClick(int position) {
                final CharSequence[] dialogItem = {"Edit","Delete"};
                dialog = new AlertDialog.Builder(ObsLogBook.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(ObsLogBook.this,AddObsLogBook.class);
                                intent.putExtra("uid", list.get(position).uid);
                                startActivity(intent);
                                break;
                            case 1:
                                Logbook logbook = list.get(position);
                                database.logbookDao().delete(logbook);
                                onStart();
                                break;
                        }
                    }
                });
                dialog.show();

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(logbookAdapter);

        FloatingActionButton fab1 = findViewById(R.id.fab1);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ObsLogBook.this,AddObsLogBook.class);
                startActivity(intent);

            }

        });


    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart(){
        super.onStart();
        list.clear();
        list.addAll(database.logbookDao().getAll());
        logbookAdapter.notifyDataSetChanged();
    }

}