package com.bala.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    public FirebaseAuth mAuth;
    EditText emailTextInput;
    EditText passwordTextInput;
    Button signInButton;
    Button forgotPasswordButton;
    Button sendVerifyMailAgainButton;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailTextInput = findViewById(R.id.signInEmailTextInput);
        passwordTextInput = findViewById(R.id.signInPasswordTextInput);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        sendVerifyMailAgainButton = findViewById(R.id.verifyEmailAgainButton);
        errorView = findViewById(R.id.signInErrorView);

        sendVerifyMailAgainButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailTextInput.getText().toString().contentEquals("")) {


                    errorView.setText("Email cant be empty");


                } else if (passwordTextInput.getText().toString().contentEquals("")) {

                    errorView.setText("Password cant be empty");

                } else {


                    mAuth.signInWithEmailAndPassword(emailTextInput.getText().toString(), passwordTextInput.getText().toString())
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (user != null) {
                                            if (user.isEmailVerified()) {

                                                
                                                System.out.println("Email Verified : " + user.isEmailVerified());
                                                Intent HomeActivity = new Intent(SignInActivity.this, MainActivity.class);
                                                setResult(RESULT_OK, null);
                                                startActivity(HomeActivity);
                                                SignInActivity.this.finish();


                                            } else {

                                                sendVerifyMailAgainButton.setVisibility(View.VISIBLE);
                                                errorView.setText("Please Verify your EmailID and SignIn");

                                            }
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        if (task.getException() != null) {
                                            errorView.setText(task.getException().getMessage());
                                        }

                                    }

                                }
                            });


                }


            }
        });


        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPasswordActivity = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordActivity);
                SignInActivity.this.finish();

            }
        });


    }
}
