package com.example.bariki_othmane;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class signUpPage1 extends AppCompatActivity {
    //variable
    ImageView back_btn;
    Button next;
    TextView title;
    TextInputLayout fullName, username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page1);
        //Hooks
        back_btn = findViewById(R.id.image_signUpPage1);
        next = findViewById(R.id.nextSignUp1);
        title = findViewById(R.id.titleSignup1);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpPage1.this, activity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });
        //Hooks Database
        fullName = findViewById(R.id.name_id);
        username = findViewById(R.id.username_id);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);


    }

    public void nextSignUp1(View view) {

        if(!validateEmail() | !validateFullName() | !validatePassword() | !validateUserName()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), signUpPage2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //______________Database__________________________//
        String fullName_str = fullName.getEditText().getText().toString();
        String username_str = username.getEditText().getText().toString();
        String email_str = email.getEditText().getText().toString();
        String password_str = password.getEditText().getText().toString();
        //call the helper Class
        UserHelperCo user = new UserHelperCo(fullName_str, email_str, username_str, password_str);
        intent.putExtra("EXTRA_USER", (Serializable) user);
        //______________fin Database_______________________//

        //add transition
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(back_btn, "backSignUp1");
        pairs[1] = new Pair<View, String>(next, "nextSignup1");
        pairs[2] = new Pair<View, String>(title, "backSignUp1");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signUpPage1.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }


    //validation de donnée
    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString();
        if (val.isEmpty()) {
            fullName.setError("ce champs ne peut pas être vide");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateUserName() {
        String val = username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username.setError("ce champs ne peut pas être vide");
            return false;
        } else if(val.length()>=15){
                username.setError("trop long !");
                return false;
        }
        else if (!val.matches(noWhiteSpace)){
                username.setError("les espaces sont interdits !");
                return false;
        }
        else {

            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("ce champs ne peut pas être vide");
            return false;
        }else if(!val.matches(emailPattern)){
            email.setError("adresse email invalid");
            return false;
        }
        else {

           email.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String RegPassword="^" + // start-of-string\n
                //"(?=.*[0-9])"+// a digit must occur at least once\n
                //"(?=.*[a-z])" +// a lower case letter must occur at least once\n
                "(?=.*[A-Za-z])"+// any letter\n
                "(?=.*[@#$%^&+=])"+// a special character must occur at least once\n
                "(?=\\S+$)"+// no whitespace allowed in the entire string\n
                ".{4,}"+//anything, at least 4 places though\n
                "$";// end-of-string
        if (val.isEmpty()) {
            password.setError("ce champs ne peut pas être vide");
            return false;
        }else if(!val.matches(RegPassword)){
            password.setError("mot de passe faible");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }


}
