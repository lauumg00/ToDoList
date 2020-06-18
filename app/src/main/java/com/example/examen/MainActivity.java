package com.example.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private MyDB db;
    private RecyclerView rv;
    private ArrayList<Task> list;
    private Boolean aux;
    private WordListAdapter adapter;
    private EditText editSearch;
    //Para pasar mi setAdapter a otras clases y refrescar
    private static MainActivity ma;

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            cambiar();
            return true;
        }
        if(item.getItemId()==R.id.night_mode){
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if(nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("WHAT ToDo");
        String title = actionBar.getTitle().toString();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icono);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        ma = this;
        aux = false;
        db = new MyDB(this);
        rv = findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Task>();
        editSearch = findViewById(R.id.editTextSearch);

        adapter = new WordListAdapter(list, this);
        rv.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback helper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int pos = viewHolder.getAdapterPosition()+1;
                list.remove(pos);
                //adapter.removeItem(pos);
                db.deleteItem(pos);
            }
        };
        ItemTouchHelper itemTouch = new ItemTouchHelper(helper);
        itemTouch.attachToRecyclerView(rv);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear();
            }
        });

    }

    private void filter(String text){
        ArrayList<Task> filteredList = new ArrayList<>();
        for (Task t : list){
            if (t.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(t);
            }
        }
        adapter.filterList(filteredList);
    }

    //Refrescar pantalla
    public void setAdapter() {
        Cursor c = db.selectRecords(aux);
        list = new ArrayList<Task>();
        if (c.getCount() > 0){
            do {
                list.add(new Task(c.getInt(0),c.getBlob(1), c.getString(2),c.getString(3),c.getString(4)));
            } while(c.moveToNext());
        }
        adapter = new WordListAdapter(list,this,c);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


    //Mandar a TaskDetail y crear nueva tarea
    public void crear() {
        Intent i = new Intent(this, TaskDetail.class);
        startActivityForResult(i,0);
    }

    //Que se vean todas las tareas
    public void cambiar(){
        aux ^= true;
        setAdapter();
    }

    public static  MainActivity getMa(){
        return ma;
    }

}
