package com.example.mathschool;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mathschool.model.Student;
import com.example.mathschool.services.ProgressService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 20;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private GoogleSignInClient googleSignInClient;
    private EditText login;
    private EditText password;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp( this);
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        GoogleSignInOptions go=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).build();
        googleSignInClient= GoogleSignIn.getClient(this,go);
        currentUser = auth.getCurrentUser();
        //Checks if a user is already logged in.
        if(currentUser != null){
            Intent intent=new Intent(MainActivity.this,SelectSchoolYear.class);
            startActivity(intent);
        }
    }

    private void setListeners() {
        login=findViewById(R.id.editText_LOGIN);
        password=findViewById(R.id.editText_PASSWORD);
        Button buttonGUEST = findViewById(R.id.buttonGUEST);
        Button buttonLOGIN = findViewById(R.id.buttonLOGIN);
        Button buttonGOOGLE = findViewById(R.id.buttonGOOGLE);
        Button buttonREGISTER = findViewById(R.id.buttonREGISTER);
        buttonGUEST.setOnClickListener(view -> {
                    Intent i=new Intent(getApplicationContext(),SelectSchoolYear.class);
                    startActivity(i);
        });
        buttonGOOGLE.setOnClickListener(v -> {
            Intent intent=googleSignInClient.getSignInIntent();
            startActivityForResult(intent,RC_SIGN_IN);

        });
        buttonLOGIN.setOnClickListener(v -> {
            String email=login.getText().toString();
            String pass=password.getText().toString();
            loginUser(email,pass);

        });
        buttonREGISTER.setOnClickListener(v -> {
            auth.signOut();
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.login_dialog);
            Button create=dialog.findViewById(R.id.returnBUTTON);
            EditText loginForm=dialog.findViewById(R.id.formLOGIN);
            EditText passwordForm=dialog.findViewById(R.id.formPASSWORD);
            EditText nameForm=dialog.findViewById(R.id.formNAME);
            create.setOnClickListener(v1 -> {
                String name=nameForm.getText().toString();
                String userMail=loginForm.getText().toString();
                String password=passwordForm.getText().toString();
                if (
                  password.isEmpty()||password.isBlank()||password==null||
                  userMail.isEmpty()||userMail.isBlank()||userMail==null||
                  name.isEmpty()||name.isBlank()||name==null
                ){
                    Toast.makeText(getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_LONG).show();
                }else{
                    createUser(userMail,password,name);
                }
            });
            dialog.show();
        });
    }



    private void loginUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        currentUser=auth.getCurrentUser();
                        saveLoginInfo();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failed",e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    private void createUser(String email, String pass,String name) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        currentUser=task.getResult().getUser();
                        currentUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    saveLoginInfo();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Log.d("errors","errors : "+e.getMessage()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount account;
            try {
                account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                saveLoginInfo();
            }
        });
    }

    private void saveLoginInfo() {
        FirebaseUser user=auth.getCurrentUser();
        HashMap<String, Object> map=new HashMap<>();
        map.put("id",user.getUid());
        map.put("name",user.getDisplayName());
        if (user.getPhotoUrl()!=null){
            map.put("profile",user.getPhotoUrl().toString());
        }
        Thread saveThread=new Thread(new Runnable() {
            AtomicBoolean onTokenReady=new AtomicBoolean(false);
            String token="";
            ProgressService progressService=new ProgressService();
            @Override
            public void run() {
                auth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        token=getTokenResult.getToken();
                        onTokenReady.set(true);
                    }
                });
                while (!onTokenReady.get());
                Student student = null;
                try {
                    student = progressService.saveStudent(new Student(null, auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getUid()), token);
                    map.put("id_back-end",student.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                firebaseDatabase.getReference().child("users").child(user.getUid()).setValue(map);
                Intent intent=new Intent(MainActivity.this,SelectSchoolYear.class);
                runOnUiThread(() -> startActivity(intent));
            }
        });
        saveThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
    }
}