package com.example.bariki_othmane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class vehicule extends AppCompatActivity {
    LinearLayout recycle;
    RecyclerView users_recycler;
    RecyclerView.Adapter adapter;
    TextInputLayout blaka, cin;
    UserHelper driver;
    ImageView backBtn, imageVehicule;
    Button uploadImage;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicule);
        //hooks

        imageVehicule = findViewById(R.id.image_vehicule);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageVehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        uploadImage = findViewById(R.id.upload_image);


        //

        backBtn = findViewById(R.id.back_nav);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicule.super.onBackPressed();
            }
        });
        blaka = findViewById(R.id.plaque);
        cin = findViewById(R.id.cin);
        //récuperer les données
        driver = (UserHelper) getIntent().getSerializableExtra("user");
        //remplir les fields

        blaka.getEditText().setText(driver.getNumIimmatriculation());
        cin.getEditText().setText(driver.getCin());
        //
        blaka.setEnabled(false);
        cin.setEnabled(false);
        //vehicule
        recycle = findViewById(R.id.recycle);
        users_recycler = findViewById(R.id.all_users);

        users_recycler();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageVehicule.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("En cours ... ");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image ajoutée .", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Erreur .",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage : " + (int) progressPercent + " % ");
                    }
                });

    }

    private void users_recycler() {
        users_recycler.setHasFixedSize(true);
        users_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<UserHelperClass> all_users = new ArrayList<>();
        all_users.add(new UserHelperClass(R.drawable.truck01));
        all_users.add(new UserHelperClass(R.drawable.truck01));
        all_users.add(new UserHelperClass(R.drawable.truck01));
        all_users.add(new UserHelperClass(R.drawable.truck01));

        adapter = new User_Adapter(all_users);

        users_recycler.setAdapter(adapter);


    }

    public void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

}
