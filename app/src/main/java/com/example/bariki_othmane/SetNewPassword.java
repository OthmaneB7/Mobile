package com.example.bariki_othmane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SetNewPassword extends AppCompatActivity {
    Button ok;

    TextInputLayout new_mp,new_mp2,numTel,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        new_mp = findViewById(R.id.new_mp);
        numTel = findViewById(R.id.numTel);
        username = findViewById(R.id.username);

        new_mp2 = findViewById(R.id.new_mp2);
        ok = findViewById(R.id.ok);


    }
    private boolean validatePassword() {
        String val = new_mp.getEditText().getText().toString();
        String RegPassword="^" + // start-of-string\n
                //"(?=.*[0-9])"+// a digit must occur at least once\n
                //"(?=.*[a-z])" +// a lower case letter must occur at least once\n
                "(?=.*[A-Za-z])"+// any letter\n
                "(?=.*[@#$%^&+=])"+// a special character must occur at least once\n
                "(?=\\S+$)"+// no whitespace allowed in the entire string\n
                ".{4,}"+//anything, at least 4 places though\n
                "$";// end-of-string
        if (val.isEmpty()) {
            new_mp.setError("ce champs ne peut pas être vide");
            return false;
        }else if(!val.matches(RegPassword)){
            new_mp.setError("mot de passe faible");
            return false;
        }
        else {
            new_mp.setError(null);
            return true;
        }
    }

    public void Reset(View view) {
        if(!validatePassword()){
            return;
        }
        if(!new_mp.getEditText().getText().toString().equals(new_mp2.getEditText().getText().toString())){
            new_mp2.setError("le deuxième mot de passe est différent du premier");
            return;
        }
        Intent intent = new Intent(SetNewPassword.this,ForgOtp.class);
        intent.putExtra("EXTRA_USER", new_mp.getEditText().getText().toString());
        intent.putExtra("tel", numTel.getEditText().getText().toString());
        intent.putExtra("username", username.getEditText().getText().toString());
        String caller = getIntent().getStringExtra("caller");
        if (caller.equals("activity2")){
            intent.putExtra("caller", "activity2");
        }else {
            intent.putExtra("caller", "CapitainLogin");
        }

        startActivity(intent);
    }
}