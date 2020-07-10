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

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class signUpPage2 extends AppCompatActivity {
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
        setContentView(R.layout.sign_up_page2);
        //Hooks
        back_btn = findViewById(R.id.image_signUpPage2);
        next = findViewById(R.id.nextSignUp1);
        title = findViewById(R.id.titleSignup1);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpPage2.this,signUpPage1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //Hooks Database
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        birthday = (DatePicker) findViewById(R.id.birthday);


    }
    public void nextSignUp2(View view) {
        Intent intent = new Intent(getApplicationContext(),signUppage3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //______________________________DataBase_________________________//

        UserHelperCo user = (UserHelperCo) getIntent().getSerializableExtra("EXTRA_USER");
        //..get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        //..find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);

        String radioButton_str = radioButton.getText().toString();

        user.setGender(radioButton_str);
        //___birthday_____
        int day = birthday.getDayOfMonth();
        int month = birthday.getMonth() + 1;
        int year = birthday.getYear();

        user.setBirthday(Integer.toString(day)+"-"+Integer.toString(month)+"-"+Integer.toString(year));
        intent.putExtra("EXTRA_USER2",(Serializable)user);
        //____________________________fin Database______________________________//
        //..add transition
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(back_btn, "backSignUp1");
        pairs[1] = new Pair<View, String>(next, "nextSignup1");
        pairs[2] = new Pair<View, String>(title, "backSignUp1");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signUpPage2.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

}
