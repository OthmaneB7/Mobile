package com.example.bariki_othmane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bariki_othmane.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ForgOtp extends AppCompatActivity {
    Button verify_btn;
    EditText phoneNoEnteredByTheUser;
    ProgressBar progressBar;
    //database
    FirebaseDatabase database;
    DatabaseReference reference;
    String vericationCodeBySystem;
    String new_mdp,num_tel,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forg_otp);
        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        new_mdp = getIntent().getStringExtra("EXTRA_USER");
        num_tel = getIntent().getStringExtra("tel");
        username = getIntent().getStringExtra("username");
        sendVerificationCodeToUser(num_tel,"+212");
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = phoneNoEnteredByTheUser.getText().toString();
                if (code.isEmpty() || code.length()<6){
                    phoneNoEnteredByTheUser.setError("Wrong OTP");
                    phoneNoEnteredByTheUser.requestFocus();
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });

    }
    private void sendVerificationCodeToUser(String phoneNo,String ext) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+"+ext+""+ phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            vericationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code =phoneAuthCredential.getSmsCode();
            if (code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ForgOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vericationCodeBySystem,codeByUser);
        signInTheUserBycredentials(credential);
    }

    private void signInTheUserBycredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgOtp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //
                            database = FirebaseDatabase.getInstance();
                            String caller = getIntent().getStringExtra("caller");
                            Intent intent = new Intent(ForgOtp.this,ChangeSuccess.class);
                            if(caller.equals("activity2")) {
                                intent.putExtra("caller","activity2");

                                reference = database.getReference("Costumers");
                                reference.child(username).child("password").setValue(new_mdp);
                            }else {
                                intent.putExtra("caller","CapitainLogin");
                                reference = database.getReference("Driver");
                                reference.child(username).child("password").setValue(new_mdp);
                            }

                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(ForgOtp.this,task.getException().getMessage(), Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }

    public void return_to_first_step(View view) {
        Intent intent = new Intent(ForgOtp.this,SetNewPassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}