package scansort.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Password;
    private EditText Email;
    private Button ButtonLogin;
    private TextView SignUp;
    private TextView ForgtoPassword;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;

    //private TextView map;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //télécharge le fichier layout

        Password = (EditText) findViewById(R.id.main_activity_password);
        Email = (EditText) findViewById(R.id.main_activity_email);
        ButtonLogin = (Button) findViewById(R.id.activity_main_button);
        SignUp = (TextView) findViewById(R.id.activity_main_to_signup_from_login);
        ForgtoPassword = (TextView) findViewById(R.id.activity_main_pwd_forgot);
        fAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.activity_main_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //map = (TextView) findViewById(R.id.activity_main_map);

        if (fAuth.getCurrentUser() != null){
            //Sans appuyé sur le boutton login, si un user ne s'est pas déconnecté,
            // il/elle sera redirigé(e) vers la page Menu
            startActivity(new Intent(getApplicationContext(), Menu.class));
            finish();
        }

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Password.setError("Password is required");
                    return;
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = fAuth.getCurrentUser();
                                        //updateUI(user);
                                        Intent menuActivity = new Intent(MainActivity.this, Menu.class);
                                        startActivity(menuActivity); // Démarre une activité
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        // updateUI(null);
                                    }
                                }
                            });
                }
            }
        });


        SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUpActivity); // Démarre une activité
                finish();
            }

        });

        ForgtoPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent ForgotPwdActivity = new Intent(MainActivity.this, ForgetPwd.class);
                startActivity(ForgotPwdActivity); // Démarre une activité
                finish();
            }

        });
/*
        map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapActivity); // Démarre une activité
                finish();
            }

        });*/
    }//end of onCreate method


}
