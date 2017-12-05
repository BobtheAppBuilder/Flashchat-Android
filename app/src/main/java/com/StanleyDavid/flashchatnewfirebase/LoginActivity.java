package com.StanleyDavid.flashchatnewfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mRegisterUser;
    private Button mLoginUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mRegisterUser = (Button) findViewById(R.id.login_register_button);
        mLoginUser = (Button) findViewById(R.id.login_sign_in_button);

        if(getIntent() != null) {
            String mRegisteredEmail = getIntent().getStringExtra("email");
            String mRegisteredPassword = getIntent().getStringExtra("password");

            mEmailView.setText(mRegisteredEmail);
            mPasswordView.setText(mRegisteredPassword);
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_password || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInExistingUser(v);
            }
        });

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser(v);
            }
        });

        // TODO: Grab an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.StanleyDavid.flashchatnewfirebase.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        if (!isEmailValid(email)){
           mEmailView.requestFocus();
           mEmailView.setError("Please enter a valid email address");
        } else if("".equals(password)){
            mPasswordView.requestFocus();
            mPasswordView.setError("Please enter your password");
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();
                    Log.d("Firebase", "attemptLogin() Successful!");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                } else{
                    try {
                        Toast.makeText(LoginActivity.this, "Sign-in Failed", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "attemptLogin() Failed!");
                        showErrorDialog("There was a problem signing in!");
                        Log.d("Firebase", task.getException() + ": " + task.getException().getMessage());
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        mEmailView.setError(getString(R.string.error_user_not_found));
                        mEmailView.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    } catch (Exception e) {
                        Log.d("Firebase", e.getClass().getName() + ": " + e.getMessage());
                        finish();
                    }
                }
                }
            });
    }
    // TODO: Show error on screen with an alert dialog
    protected void showErrorDialog(String message){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Oops")
                .setPositiveButton(android.R.string.ok, null)
                .show();

    }
    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        if (!"".equals(email)) {
            if (email.matches("[a-zA-Z0-9]*@.*\\.com")) {
                return true;
            }
        }
        return false;
    }

}