package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button signUpButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.email_edit_text_signup);
        passwordEditText = findViewById(R.id.password_edit_text_signup);
        signUpButton = findViewById(R.id.sign_up_button_signup);

        setup();
    }

    private void setup() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showHome();
                            } else {
                                Snackbar.make(findViewById(R.id.my_coordinator_layout_login_signup), R.string.error_authentication, 3000)
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showHome() {
        Intent mapIntent = new Intent(SignUpActivity.this, MapsActivity.class);
        mapIntent.putExtra("email", emailEditText.getText().toString());
        startActivity(mapIntent);
    }
}