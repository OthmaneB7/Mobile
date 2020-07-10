package com.example.bariki_othmane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.io.Serializable;

public class signUppage3 extends AppCompatActivity {
    //variable
    ImageView back_btn;
    Button next,inscription;
    TextView title;
    //database variable
    CountryCodePicker extension;
    //FirebaseDatabase database;
    //DatabaseReference reference;
    TextInputLayout numTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_uppage3);
        back_btn = findViewById(R.id.image_signUpPage3);
        inscription = findViewById(R.id.inscriptionSignUp3);
        next = findViewById(R.id.nextSignUp1);
        title = findViewById(R.id.titleSignup1);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUppage3.this,signUpPage2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //..hooks database
        numTel = findViewById(R.id.phone_id);
        extension = findViewById(R.id.extension);

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateNonumTel()){
                    return;
                }
                //database = FirebaseDatabase.getInstance();
                //reference = database.getReference("Costumers");
                //extraire les données
                String numTel_str = numTel.getEditText().getText().toString();
                String ext_str = extension.getSelectedCountryCode().toString();


                UserHelperCo user = (UserHelperCo) getIntent().getSerializableExtra("EXTRA_USER2");
                user.setPhoneNumber(numTel_str);
                user.setExt(ext_str);

                Intent intent = new Intent(signUppage3.this,verifyPhone.class);
                intent.putExtra("EXTRA_USER3",(Serializable)user);
                startActivity(intent);
            }
        });
    }
    private boolean validateNonumTel(){
        String val = numTel.getEditText().getText().toString();
        if (val.isEmpty()) {
            numTel.setError("ce champs ne peut pas être vide");
            return false;
        } else {
            numTel.setError(null);
            return true;
        }
    }
}
