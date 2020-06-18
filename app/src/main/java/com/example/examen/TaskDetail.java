package com.example.examen;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class TaskDetail extends AppCompatActivity {
    private TextView date, creationdate;
    private EditText task;
    private CheckBox check;
    private Date d;
    private ImageView img;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);
        date = findViewById(R.id.textView4);
        creationdate = findViewById(R.id.textView);
        task = findViewById(R.id.editText2);
        check = (CheckBox) findViewById(R.id.checkBox);
        this.img = findViewById(R.id.imageView2);
        if (getIntent().getExtras() != null){
            if (getIntent().getStringExtra("COMPLETE").equals("true")){
                creationdate.setText("Completed");
                check.setChecked(true);
                task.setEnabled(false);
                check.setEnabled(false);
            }
            Bitmap bmp = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("IMAGE"), 0, getIntent().getByteArrayExtra("IMAGE").length);
            this.img.setImageBitmap(bmp);
            task.setText(getIntent().getStringExtra("NAME"));
            creationdate.setText(getIntent().getStringExtra("DATE"));
        }
    }

    public byte[] imgToByte(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }

    public void save(View view) {
        String complete = "false";
        d = new Date();
        Drawable draw = img.getDrawable();
        if (check.isChecked()){
            complete="true";
        }
        if (getIntent().getExtras() != null){
            MyDB.updateItem(getIntent().getIntExtra("ID",0),imgToByte(draw), task.getText().toString(),d.toString(), complete);
        } else{
            setResult(0);
            MyDB.createRecords(imgToByte(draw),task.getText().toString(),d.toString(),complete);
            Toast t = Toast.makeText(this,task.getText()+" has been created", Toast.LENGTH_LONG);
            t.show();
        }
        MainActivity.getMa().setAdapter();
        finish();

    }

    public void putImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.imageView2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard changes?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    public void cancel(View view) {
        dialog();
        builder.show();
    }
}
