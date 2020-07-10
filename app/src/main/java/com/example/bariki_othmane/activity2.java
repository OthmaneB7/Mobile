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

public class activity2 extends AppCompatActivity {
    ImageView Back;
    Button buttonInscriptions,login,mdpOublier;
    TextInputLayout username,password;
    TextInputLayout usernameV,passwordV;
    ProgressBar progressBar;
    //variable remember me
    CheckBox checkBoxx;
    private boolean auth=false;
    private  UserHelperCo user00=new UserHelperCo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
        mdpOublier = findViewById(R.id.mdpOublier);
        mdpOublier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity2.this,SetNewPassword.class);
                intent.putExtra("caller", "activity2");
                startActivity(intent);
            }
        });
        //-----progressBar------//
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //_____login_user hooks
        username = findViewById(R.id.username_id);
        password = findViewById(R.id.password_id);
        //_____fin login
        //-----------------------------------------------------------------------------------------//

        SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("checkbox","");
        String x = getIntent().getStringExtra("caller");

        if(x.equals("menu")) {
            Toast.makeText(activity2.this, "after logout", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("checkbox", "false");
            editor.apply();
            auth = false;
        } else if (checkbox.equals("true") ){
            progressBar.setVisibility(View.VISIBLE);

            final String username = preferences.getString("username","");
            //database synchronization
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Costumers");
            Query checkUser = reference.orderByChild("username").equalTo(username);
            final UserHelperCo user = new UserHelperCo();
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fullName = dataSnapshot.child(username).child("fullName").getValue(String.class);
                    String gender = dataSnapshot.child(username).child("gender").getValue(String.class);
                    String birthday = dataSnapshot.child(username).child("birthday").getValue(String.class);
                    String email = dataSnapshot.child(username).child("email").getValue(String.class);
                    String phoneNumber = dataSnapshot.child(username).child("phoneNumber").getValue(String.class);
                    String password = dataSnapshot.child(username).child("password").getValue(String.class);
                    String imgURL = dataSnapshot.child(username).child("image_URL").getValue(String.class);

                    usernameV = findViewById(R.id.username_id);
                    passwordV = findViewById(R.id.password_id);
                    usernameV.getEditText().setText(username);
                    passwordV.getEditText().setText(password);

                    Toast.makeText(activity2.this,fullName+"/1" , Toast.LENGTH_SHORT).show();
                    user.setFullName(fullName);
                    user.setUsername(username);
                    user.setGender(gender);
                    user.setBirthday(birthday);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setPhoneNumber(phoneNumber);
                    user.setImage_URL(imgURL);

                    startMyActivity(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(activity2.this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }
        checkBoxx=findViewById(R.id.checkBox);
        //
        checkBoxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    auth=true;
                    //Toast.makeText(activity2.this, "Checked", Toast.LENGTH_SHORT).show();
                }else{
                    auth=false;
                    //Toast.makeText(activity2.this, "UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //--fin remember me

        Back = findViewById(R.id.backBtn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity2.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //
        buttonInscriptions = findViewById(R.id.button2);

        buttonInscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity2.this,signUpPage1.class);
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
    //remember me



    //validation de donnée
    private boolean validateUserName() {
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            username.setError("ce champs ne peut pas être vide");
            return false;
        }
        else {
            //Hide error
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
            //Hide error
            password.setError(null);
            return true;
        }
    }

    public void loginUser(boolean auth){
        //validate login info
        progressBar.setVisibility(View.VISIBLE);
        if(!validatePassword() | !validateUserName()){
            return;
        }
        else{
            isUser(auth);
        }
    }

    public void isUser(final boolean authi) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Costumers");
            final String usernameIn = username.getEditText().getText().toString().trim();
            final String passwordIn = password.getEditText().getText().toString().trim();

            Query checkUser = reference.orderByChild("username").equalTo(usernameIn);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        username.setError(null);
                        username.setEnabled(false);

                        String passwordFromDB = dataSnapshot.child(usernameIn).child("password").getValue(String.class);
                        if (passwordFromDB.equals(passwordIn)) {
                            password.setError(null);
                            password.setEnabled(false);
                            //--remember me
                            SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            if(authi) {
                                reference.child(usernameIn).child("auth").setValue(true);
                                editor.putString("checkbox","true");
                                editor.apply();
                            }
                            else{
                                reference.child(usernameIn).child("auth").setValue(false);
                                editor.putString("checkbox","false");
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
                            //--holder user to next activity
                            editor.putString("username",username);
                            editor.apply();

                            user00.setFullName(Fname);
                            user00.setUsername(username);
                            user00.setGender(gender);
                            user00.setBirthday(birthday);
                            user00.setPhoneNumber(phoneNumber);
                            user00.setEmail(email);
                            user00.setPassword(password);
                            user00.setImage_URL(imgURL);

                            startMyActivity(user00);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            password.setError("Mot de passe incorrecte");
                            password.requestFocus();
                        }
                    } else {
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
        Intent intent = new Intent(activity2.this,mdpOublier.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startMyActivity(UserHelperCo user){
        Intent intent = new Intent(activity2.this,MapBox.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Toast.makeText(activity2.this, user.fullName+"/2", Toast.LENGTH_SHORT).show();
        //---------user info---------------//
        intent.putExtra("currentUser", (Serializable) user);
        intent.putExtra("caller", "activity2");
        final Thread pause = new Thread();
        try {
            pause.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }
}
