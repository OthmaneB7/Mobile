package com.example.bariki_othmane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeSuccess extends AppCompatActivity {
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_success);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caller = getIntent().getStringExtra("caller");
                if (caller.equals("activity2")) {
                    Intent intent = new Intent(ChangeSuccess.this, activity2.class);
                    intent.putExtra("caller","ChangeSuccess");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ChangeSuccess.this, CapitainLogin.class);
                    intent.putExtra("caller","ChangeSuccess");
                    startActivity(intent);
                }
            }

        });
    }
}