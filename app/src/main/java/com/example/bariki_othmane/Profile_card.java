package com.example.bariki_othmane;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bariki_othmane.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_card extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE =1 ;
    TextInputLayout fullName, username, email, phoneNumber;
    DatabaseReference reference;
    ImageView back;
    Button appeler;
    public UserHelper _Driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_card);

        fullName = findViewById(R.id.fullName_profile);
        username = findViewById(R.id.username_profile);
        email = findViewById(R.id.email_profile);
        phoneNumber = findViewById(R.id.phone_profile);
        back = findViewById(R.id.back_map);
        appeler = findViewById(R.id.appeler);

        reference = FirebaseDatabase.getInstance().getReference("Driver");
        UserHelper driver = (UserHelper) getIntent().getSerializableExtra("driver");
        _Driver = driver;
        username.getEditText().setText(driver.getUsername());
        fullName.getEditText().setText(driver.getFullName());
        email.getEditText().setText(driver.getEmail());
        phoneNumber.getEditText().setText(driver.getPhoneNumber());


        username.setEnabled(false);
        email.setEnabled(false);
        fullName.setEnabled(false);
        phoneNumber.setEnabled(false);

        fullName.setBoxStrokeColor(getResources().getColor(R.color.gray));
        username.setBoxStrokeColor(getResources().getColor(R.color.gray));
        email.setBoxStrokeColor(getResources().getColor(R.color.gray));
        phoneNumber.setBoxStrokeColor(getResources().getColor(R.color.gray));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile_card.super.onBackPressed();
            }
        });

        appeler.setBackgroundColor(getResources().getColor(R.color.map_water));
        appeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               makeCall();
            }
        });


    }

    public void makeCall()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + _Driver.getPhoneNumber()));
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            startActivity(intent);

        } else {

            requestPermission();
        }
    }

    private void requestPermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Profile_card.this,Manifest.permission.CALL_PHONE))
        {
        }
        else {

            ActivityCompat.requestPermissions(Profile_card.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                }
                break;
        }
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

}
