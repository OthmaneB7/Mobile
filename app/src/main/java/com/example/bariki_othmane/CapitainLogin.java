package com.example.bariki_othmane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class CapitainLogin extends AppCompatActivity {

    ImageView Back;
    Button buttonInscriptions,login,mdpOublier;
    ProgressBar progressBar;
    TextInputLayout username,password;
    //variable remember me
    CheckBox checkboxx;
    private  boolean auth=false;
    private UserHelper user00=new UserHelper();
    TextInputLayout usernameV,passwordV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitain_login);

        mdpOublier = findViewById(R.id.mdpOublier);
        mdpOublier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapitainLogin.this,SetNewPassword.class);
                intent.putExtra("caller", "CapitainLogin");
                startActivity(intent);
            }
        });
        //-----------------------------------------------------------------------------------------//

        SharedPreferences preferences =getSharedPreferences("checkbox2",MODE_PRIVATE);
        String checkbox2 = preferences.getString("checkbox2","");

        //-----progressBar------//
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        String x = getIntent().getStringExtra("caller");
        if(x.equals("menu")){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("checkbox2","false");
            editor.apply();
            auth=false;
        }else if (checkbox2.equals("true")){
            progressBar.setVisibility(View.VISIBLE);
            final String username = preferences.getString("username","");
            //database synchronization
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver");
            Query checkUser = reference.orderByChild("username").equalTo(username);
            final UserHelper user = new UserHelper();
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fullName = dataSnapshot.child(username).child("fullName").getValue(String.class);
                    String gender = dataSnapshot.child(username).child("gender").getValue(String.class);
                    String birthday = dataSnapshot.child(username).child("birthday").getValue(String.class);
                    String email = dataSnapshot.child(username).child("email").getValue(String.class);
                    String phoneNumber = dataSnapshot.child(username).child("phoneNumber").getValue(String.class);
                    String password = dataSnapshot.child(username).child("password").getValue(String.class);
                    //--vehicule info
                    String cin = dataSnapshot.child(username).child("cin").getValue(String.class);
                    String numIimmatriculation = dataSnapshot.child(username).child("numIimmatriculation").getValue(String.class);

                    Toast.makeText(CapitainLogin.this,fullName+"/1" , Toast.LENGTH_SHORT).show();
                    user.setFullName(fullName);
                    user.setGender(gender);
                    user.setBirthday(birthday);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setUsername(username);
                    user.setPhoneNumber(phoneNumber);

                    user.setCin(cin);
                    user.setNumIimmatriculation(numIimmatriculation);


                    usernameV = findViewById(R.id.username_id);
                    passwordV = findViewById(R.id.password_id);
                    usernameV.getEditText().setText(username);
                    passwordV.getEditText().setText(password);


                    startMyActivity(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(CapitainLogin.this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }
        checkboxx=findViewById(R.id.checkBox2);
        //-----------------------------------------------------------------------------------------//

        //remember me
        checkboxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    auth=true;
                    //Toast.makeText(CapitainLogin.this, "Checked", Toast.LENGTH_SHORT).show();
                }else{
                    auth=false;
                    //Toast.makeText(CapitainLogin.this, "UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //--fin remember me

        Back = findViewById(R.id.backBtn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapitainLogin.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //
        //_____login_user hooks
        username = findViewById(R.id.username_id);
        password = findViewById(R.id.password_id);
        //_____fin login
        buttonInscriptions = findViewById(R.id.button2);
        buttonInscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapitainLogin.this,signUpDriverPage1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(auth);
            }
        });

    }
    //validation de donnée
    private boolean validateUserName() {
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            username.setError("ce champs ne peut pas être vide");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            password.setError("ce champs ne peut pas être vide");
            return false;
        }
        else {

            password.setError(null);
            return true;
        }
    }

    public void loginUser(boolean auth){
        //validate login info
        progressBar.setVisibility(View.VISIBLE);
        if(!validatePassword() | !validateUserName()){
            progressBar.setVisibility(View.GONE);
            return;
        }
        else{
            isUser(auth);
        }
    }

    public void isUser(final boolean authi) {
        final String usernameIn = username.getEditText().getText().toString().trim();
        final String passwordIn = password.getEditText().getText().toString().trim();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver");
        Query checkUser = reference.orderByChild("username").equalTo(usernameIn);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username.setError(null);
                    username.setEnabled(false);

                    String passwordFromDB = dataSnapshot.child(usernameIn).child("password").getValue(String.class);
                    if(passwordFromDB.equals(passwordIn)){
                        password.setError(null);
                        password.setEnabled(false);
                        SharedPreferences preferences = getSharedPreferences("checkbox2",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        //--remember me
                        if(authi) {
                            reference.child(usernameIn).child("auth").setValue(true);
                            editor.putString("checkbox2","true");
                            editor.apply();

                        }
                        else{
                            reference.child(usernameIn).child("auth").setValue(false);
                            editor.putString("checkbox2","false");
                            editor.apply();

                        }
                        //--fin remember me

                        //--profile info
                        String Fname = dataSnapshot.child(usernameIn).child("fullName").getValue(String.class);
                        String username = dataSnapshot.child(usernameIn).child("username").getValue(String.class);
                        String gender = dataSnapshot.child(usernameIn).child("gender").getValue(String.class);
                        String birthday = dataSnapshot.child(usernameIn).child("birthday").getValue(String.class);
                        String email = dataSnapshot.child(usernameIn).child("email").getValue(String.class);
                        String phoneNumber = dataSnapshot.child(usernameIn).child("phoneNumber").getValue(String.class);
                        String password = dataSnapshot.child(usernameIn).child("password").getValue(String.class);
                        String imgURL = dataSnapshot.child(usernameIn).child("image_URL").getValue(String.class);
                        //--vehicule info
                        String cin = dataSnapshot.child(usernameIn).child("cin").getValue(String.class);
                        String numIimmatriculation = dataSnapshot.child(usernameIn).child("numIimmatriculation").getValue(String.class);

                        editor.putString("username",username);
                        editor.apply();
                        //--holder user to next activity
                        user00.setFullName(Fname);
                        user00.setUsername(username);
                        user00.setGender(gender);
                        user00.setBirthday(birthday);
                        user00.setCin(cin);
                        user00.setPassword(password);
                        user00.setPhoneNumber(phoneNumber);
                        user00.setEmail(email);
                        user00.setNumIimmatriculation(numIimmatriculation);

                        user00.setImage_URL(imgURL);
                        startMyActivity(user00);


                    }else{
                        progressBar.setVisibility(View.GONE);
                        password.setError("Mot de passe incorrecte");
                        password.requestFocus();

                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    username.setError("utilisateur innexistant");
                    username.requestFocus();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void To_Reset_Password(View view) {
        Intent intent = new Intent(CapitainLogin.this,mdpOublier.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startMyActivity(UserHelper user){
        Intent intent = new Intent(CapitainLogin.this,MapBox.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //---------user info
        //Toast.makeText(CapitainLogin.this, user.fullName+"/2", Toast.LENGTH_SHORT).show();
        intent.putExtra("currentCapitain", (Serializable) user);
        intent.putExtra("caller", "CapitainLogin");
        final Thread pause = new Thread();
        try {
            pause.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }



}
