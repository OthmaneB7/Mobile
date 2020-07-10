package com.example.bariki_othmane;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Help extends AppCompatActivity {
    ImageView button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //hooks
        button = findViewById(R.id.back_nav);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help.super.onBackPressed();
            }
        });


    }

    public void sendMail(View view) {
        try {
            Intent sendmail = new Intent(Intent.ACTION_SEND);
            sendmail.setData(Uri.parse(("mailto:" + "Othmane.bariki@gmail.com")));
            sendmail.setType("text/html");
            sendmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"Othmane.bariki@gmail.com"});
            sendmail.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...");
            sendmail.setPackage("com.google.android.gm");

            startActivity(sendmail);
        } catch (Exception e) {
            Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show();
        }

    }

    public void Call(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+212687888546"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}
