package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class employer_landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_landing);
        Button signOutButton = findViewById(R.id.logout2);
        signOutButton.setOnClickListener(this::onClicklogout);
        Button roleSwitch = findViewById(R.id.role);
        roleSwitch.setOnClickListener(this::onClickRole);
        Button jobPost = findViewById(R.id.post_job);
        jobPost.setOnClickListener(this::onClickJobPost);
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(employer_landing.this, SignInActivity.class));
        }
    }
    public void onClicklogout(View view) {
        /** Sign-out implementation adapted from FirebaseUI Auth guide.
         Source: Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
         */
        if (view.getId() == R.id.logout2) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // User is signed out. Start the sign-in process again.
                            startActivity(new Intent(employer_landing.this, SignInActivity.class));
                            finish();
                        }
                    });

        }
    }
    public void onClickRole(View view) {
        Intent roleSwitch= (new Intent(employer_landing.this, employee_landing.class));
        startActivity(roleSwitch);
    }
    public void onClickJobPost(View view) {
        Intent jobPost = (new Intent(employer_landing.this, employer_job_creation.class));
        startActivity(jobPost);
    }
}