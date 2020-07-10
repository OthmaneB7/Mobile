package com.example.bariki_othmane;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class signUpDriverPage2 extends AppCompatActivity {
    //variable
    ImageView back_btn;
    Button next;
    TextView title;
    //radioButton
    RadioGroup radioGroup;
    RadioButton radioButton;
    DatePicker birthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_driver_page2);
        //Hooks
        back_btn = findViewById(R.id.image_signUpPage2);
        next = findViewById(R.id.nextSignUp1);
        title = findViewById(R.id.titleSignup1);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpDriverPage2.this, signUpDriverPage1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //Hooks Database
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        birthday = (DatePicker) findViewById(R.id.birthday);

    }

    public void nextSignUp2(View view) {
        Intent intent = new Intent(getApplicationContext(),signUpDriverPage3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        UserHelper user = (UserHelper) getIntent().getSerializableExtra("EXTRA_USER");
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        Toast.makeText(signUpDriverPage2.this,radioButton.getText(), Toast.LENGTH_SHORT).show();
        String radioButton_str = radioButton.getText().toString();

        user.setGender(radioButton_str);
        //birthday_____
        int day = birthday.getDayOfMonth();
        int month = birthday.getMonth() + 1;
        int year = birthday.getYear();
        Toast.makeText(signUpDriverPage2.this,Integer.toString(day)+"-"+Integer.toString(month)+"-"+Integer.toString(year), Toast.LENGTH_SHORT).show();
        user.setBirthday(Integer.toString(day)+"-"+Integer.toString(month)+"-"+Integer.toString(year));
        //add transition
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(back_btn, "backSignUp1");
        pairs[1] = new Pair<View, String>(next, "nextSignup1");
        pairs[2] = new Pair<View, String>(title, "backSignUp1");
        //Extra
        intent.putExtra("EXTRA_USER2",(Serializable)user);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signUpDriverPage2.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

}
