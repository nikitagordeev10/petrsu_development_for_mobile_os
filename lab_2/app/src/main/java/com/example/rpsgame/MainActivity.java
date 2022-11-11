package com.example.rpsgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button b_rock, b_paper, b_scissors;
    ImageView iv_cpu, iv_me;

    String myChoise, cpuChoise, result;

    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_cpu = (ImageView) findViewById(R.id.iv_cpu);
        iv_me = (ImageView) findViewById(R.id.iv_me);

        b_rock = (Button) findViewById(R.id.b_rock);
        b_paper = (Button) findViewById(R.id.b_paper);
        b_scissors = (Button) findViewById(R.id.b_scissors);

        r = new Random();

        b_rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myChoise = "rock";
                iv_me.setImageResource(R.drawable.rock);
                calcualte();
            }
        });

        b_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myChoise = "paper";
                iv_me.setImageResource(R.drawable.paper);
                calcualte();
            }
        });

        b_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myChoise = "scissors";
                iv_me.setImageResource(R.drawable.scissors);
                calcualte();
            }
        });
    }

    public void calcualte (){
        int cpu = r.nextInt(3);
        if(cpu == 0){
            cpuChoise = "rock";
            iv_cpu.setImageResource(R.drawable.rock);
        }
        else if(cpu == 1){
            cpuChoise = "paper";
            iv_cpu.setImageResource(R.drawable.paper);
        }
        else if(cpu == 2){
            cpuChoise = "scissors";
            iv_cpu.setImageResource(R.drawable.scissors);
        }

        if(myChoise.equals("rock") && cpuChoise.equals("scissors")){
            result = "you win";
        }
        else if(myChoise.equals("rock") && cpuChoise.equals("paper")){
            result = "you lose";
        }
        else if(myChoise.equals("paper") && cpuChoise.equals("rock")){
            result = "you win";
        }
        else if(myChoise.equals("paper") && cpuChoise.equals("scissors")){
            result = "you lose";
        }
        else if(myChoise.equals("scissors") && cpuChoise.equals("paper")){
            result = "you win";
        }
        else if(myChoise.equals("scissors") && cpuChoise.equals("rock")){
            result = "you lose";
        }
        else if(myChoise.equals("scissors") && cpuChoise.equals("scissors")){
            result = "draw";
        }
        else if(myChoise.equals("rock") && cpuChoise.equals("rock")){
            result = "draw";
        }
        else if(myChoise.equals("paper") && cpuChoise.equals("paper")){
            result = "draw";
        }


        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}