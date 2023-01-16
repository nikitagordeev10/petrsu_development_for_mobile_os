package com.example.lab_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMyButtonClick(View view)
    {
        // выводим сообщение
        Toast.makeText(this, "Привет мир!", Toast.LENGTH_SHORT).show();
    }

    public void startNewActivity(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

}
