package com.example.android.radencynthya_1202152326_modul5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Database dtBase;
    private RecyclerView rcView;
    private Adapter adapter;
    private ArrayList<AddData> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set title menjadi To Do List
        setTitle("To Do List");

        //Merefer semua variabel yang ada
        //mengakses recyclerview
        rcView = findViewById(R.id.rec_view);
        //membuat araylist baru
        data_list = new ArrayList<>();
        //membuat database baru
        dtBase = new Database(this);
        //memanggil method readdata
        dtBase.readdata(data_list);

        //menginisialisasi shared preference
        SharedPreferences sharedP = this.getApplicationContext().getSharedPreferences("Preferences", 0);
        int color = sharedP.getInt("Colourground", R.color.white);

        //membuat adapter baru
        adapter = new Adapter(this,data_list, color);
        //mengatur perubahan ukuran
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //inisiasi adapter untuk recycler view
        rcView.setAdapter(adapter);

        //menjalankan method hapus data pada list to do
        hapusgeser();
    }

    //membuat method untuk menghapus item pada to do list
    public void hapusgeser(){
        //membuat touch helper baru untuk recycler view
        ItemTouchHelper.SimpleCallback touchcall = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                AddData current = adapter.getData(position);
                //apabila item di swipe ke arah kiri
                if(direction==ItemTouchHelper.LEFT){

                    //remove item yang dipilih dengan mengenali todonya sebagai primary key
                    if(dtBase.removedata(current.getTodo())){

                        //menghapus data
                        adapter.deleteData(position);

                        //membuat snack bar dan pemberitahuan bahwa item sudah terhapus dengan durasi 2 sekon
                        Snackbar.make(findViewById(R.id.coordinator), "List Telah Terhapus", 2000).show();
                    }
                }
            }
        };
        //menentukan itemtouchhelper untuk recycler view
        ItemTouchHelper touchhelp = new ItemTouchHelper(touchcall);
        touchhelp.attachToRecyclerView(rcView);
    }

    //ketika menu di dalam activity di buat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //method yang dijalankan ketika item di pilih
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mendapatkan id dari item
        int id = item.getItemId();

        //apabila item yang dipilih adalah setting
        if (id==R.id.action_settings){

            //untuk membuat intent ke menu Settings
            Intent intent = new Intent(MainActivity.this, Settings.class);
            //untuk memulai intent
            startActivity(intent);
            //untuk menutup aktivitas setelah intent dijalankan
            finish();
        }
        return true;
    }

    //method yang akan dijalankan ketika tombol add di klik
    public void addlist(View view) {
        //intent ke class add to do
        Intent intent = new Intent(MainActivity.this, AddToDo.class);
        //memulai intent
        startActivity(intent);
    }
}

