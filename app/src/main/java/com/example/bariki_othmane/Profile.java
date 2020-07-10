package com.example.bariki_othmane;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class Profile extends AppCompatActivity {

    private static final String TAG = null;
    private static boolean HADCHANDEPROFILE =false ;
    ImageView button,profile_image;
    TextView username;
    TextInputLayout fullName, password, email, phoneNumber;
    DatabaseReference reference;
    String callerV=null;
    UserHelper _Driver = new UserHelper();
    UserHelperCo _User = new UserHelperCo();
    static int REQUESCODE = 1;
    int TAKE_IMAGE_CODE = 1001;
    public Uri imguri;
    String str;
    FirebaseStorage mStorage = FirebaseStorage.getInstance();
    public String MyUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //hooks Profile
        username = findViewById(R.id.username_profile);
        fullName = findViewById(R.id.fullName_profile);
        password = findViewById(R.id.password_profile);
        email = findViewById(R.id.email_profile);
        phoneNumber = findViewById(R.id.phone_profile);
        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
        String caller = getIntent().getStringExtra("caller");
        callerV = caller;
        if(caller.equals("capitainLogin")){
            reference = FirebaseDatabase.getInstance().getReference("Driver");
            UserHelper driver = (UserHelper) getIntent().getSerializableExtra("driver");
            _Driver = driver;
            username.setText(driver.getUsername());
            fullName.getEditText().setText(driver.getFullName());
            password.getEditText().setText(driver.getPassword());
            email.getEditText().setText(driver.getEmail());
            email.setEnabled(false);
            phoneNumber.getEditText().setText(driver.getPhoneNumber());
            phoneNumber.setEnabled(false);
            // Reference to an image file in Cloud Storage

        }
        else{
            reference = FirebaseDatabase.getInstance().getReference("Costumers");
            UserHelperCo user = (UserHelperCo) getIntent().getSerializableExtra("user");
            _User = user;
            username.setText(user.getUsername());
            fullName.getEditText().setText(user.getFullName());
            password.getEditText().setText(user.getPassword());
            email.getEditText().setText(user.getEmail());
            email.setEnabled(false);
            phoneNumber.getEditText().setText(user.getPhoneNumber());
            phoneNumber.setEnabled(false);
        }
        button = findViewById(R.id.back_nav);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.super.onBackPressed();
            }
        });
    }


    public void update_profile(View view) {

        if (callerV.equals("capitainLogin")) {
            /*StorageReference mStorageReference = mStorage.getReference("ImageProfile/"+"Driver"+_Driver.getUsername()+".jpg");
            Uri file = Uri.fromFile(new File(_Driver.getImage_URL()));


            mStorageReference.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
            */
            reference.child(_Driver.getUsername()).child("image_URL").setValue(_Driver.getImage_URL());
        } else {
            reference.child(_User.getUsername()).child("image_URL").setValue(_User.getImage_URL());
        }
            //fin StorageImg -_-

        if(!validateFullName() | !validatePassword()){
            return;
        }
        else if(isNameChanged() || isPasswordChanged() || HADCHANDEPROFILE ){
            Toast.makeText(this,"Vos informations sont misent à jour",Toast.LENGTH_SHORT).show();
        }
        else {

        }

    }
    private boolean isPasswordChanged() {
        if(callerV.equals("capitainLogin")){
            if(! _Driver.getPassword().equals(password.getEditText().getText().toString())){
                reference.child(_Driver.getUsername()).child("password").setValue(password.getEditText().getText().toString());
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if(! _User.getFullName().equals(password.getEditText().getText().toString())){
                reference.child(_User.getUsername()).child("password").setValue(password.getEditText().getText().toString());
                return true;
            }
            else{
                return false;
            }
        }
    }

    private boolean isNameChanged() {
        if(callerV.equals("capitainLogin")){
            if(! _Driver.getFullName().equals(fullName.getEditText().getText().toString())){
                reference.child(_Driver.getUsername()).child("fullName").setValue(fullName.getEditText().getText().toString());
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if(! _User.getFullName().equals(fullName.getEditText().getText().toString())){
                reference.child(_User.getUsername()).child("fullName").setValue(fullName.getEditText().getText().toString());
                return true;
            }
            else{
                return false;
            }
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


    private void FileChooser() {
        //TODO: open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if( requestCode==REQUESCODE && resultCode == RESULT_OK && data!=null){
            imguri = data.getData();
            if(callerV.equals("capitainLogin")) {
                Glide.with(this /* context */)
                        .load(imguri)
                        .circleCrop()
                        .fallback(R.drawable.driver)
                        .into(profile_image);
                _Driver.setImage_URL(imguri.toString());

            }
            else {
                Glide.with(this /* context */)
                        .load(imguri)
                        .circleCrop()
                        .fallback(R.drawable.client)
                        .into(profile_image);
                _User.setImage_URL(imguri.toString());
            }

        }
    }


}