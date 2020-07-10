package com.example.bariki_othmane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.io.Serializable;

public class signUpDriverPage3 extends AppCompatActivity {
    //variable
    ImageView back_btn;
    Button next,inscription;
    TextView title;
    TextInputLayout cin,blaka,numTel;
    CountryCodePicker extension;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_driver_page3);

        back_btn = findViewById(R.id.image_signUpPage3);
        inscription = findViewById(R.id.nextSignUp3);
        title = findViewById(R.id.titleSignup1);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpDriverPage3.this,signUpDriverPage2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //hooks_database
        cin = findViewById(R.id.cin_id);
        numTel = findViewById(R.id.phone_id);
        blaka = findViewById(R.id.blaka_id);
        extension = findViewById(R.id.extension);


        //
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateMatricule() | !validateNumTel() | !validateblaka()){
                    return;
                }



                //extraire les données
                String numTel_str = numTel.getEditText().getText().toString();
                String ext_str = extension.getSelectedCountryCode().toString();
                String blaka_str = blaka.getEditText().getText().toString();
                String cin_str = cin.getEditText().getText().toString();


                UserHelper user = (UserHelper) getIntent().getSerializableExtra("EXTRA_USER2");
                user.setPhoneNumber(numTel_str);
                user.setExt(ext_str);
                user.setNumIimmatriculation(blaka_str);
                user.setCin(cin_str);

                //reference.child(user.username).setValue(user);
                Intent intent = new Intent(signUpDriverPage3.this,verifyPhoneCapitain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXTRA_USER3",(Serializable)user);
                startActivity(intent);


            }
        });

    }
    private boolean validateNumTel(){
        String val = numTel.getEditText().getText().toString();
        if (val.isEmpty()) {
            numTel.setError("ce champs ne peut pas être vide");
            return false;
        } else {

            numTel.setError(null);
            numTel.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateblaka(){
        String val = cin.getEditText().getText().toString();
        if (val.isEmpty()) {
            cin.setError("ce champs ne peut pas être vide");
            return false;
        } else {

            cin.setError(null);
            cin.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateMatricule() {
        String val = blaka.getEditText().getText().toString();
        if (val.isEmpty()) {
            blaka.setError("ce champs ne peut pas être vide");
            return false;
        } else {

            blaka.setError(null);
            blaka.setErrorEnabled(false);
            return true;
        }
    }
}
