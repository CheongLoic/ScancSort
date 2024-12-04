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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText LastName;
    private EditText FirstName;
    private EditText Password1;
    private EditText Password2;
    private EditText Email;
    private Button ButtonSignUp;
    private TextView BackToLogin;
    private FirebaseAuth fAuth;
    private DatabaseReference reff;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LastName = (EditText) findViewById(R.id.activity_signup_lastname);
        FirstName = (EditText) findViewById(R.id.activity_signup_firstname);
        Email = (EditText) findViewById(R.id.activity_signup_email);
        Password1 = (EditText) findViewById(R.id.activity_signup_password);
        Password2 = (EditText) findViewById(R.id.activity_signup_password2);
        ButtonSignUp = (Button) findViewById(R.id.activity_signup_button);
        BackToLogin = (TextView) findViewById(R.id.activity_signup_backToLogin);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            Intent menuActivity = new Intent(SignUp.this, Menu.class);
            startActivity(menuActivity); // Démarre une activité
            finish(); // ?????
        }

        ButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastname= LastName.getText().toString().trim();
                String firstname = FirstName.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password1 = Password1.getText().toString().trim();
                String password2 = Password2.getText().toString().trim();

                if(TextUtils.isEmpty(lastname)){
                    LastName.setError(" Last name is required");
                    return;
                }
                if(TextUtils.isEmpty(firstname)){
                    FirstName.setError("First name is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password1) ){
                    Password1.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    Password2.setError("Password is required");
                    return;
                }


                if (password1.equals(password2)){
                    fAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                String UserID = user.getUid();
                                member = new Member();
                                reff = FirebaseDatabase.getInstance().getReference().child("Member").child(UserID);
                                        member.setLastName(LastName.getText().toString().trim());
                                        member.setEmail(Email.getText().toString().trim());
                                        member.setFirstName(FirstName.getText().toString().trim());
                                        member.setPassword(Password1.getText().toString().trim());
                                        member.setScore("0/7");
                                        reff.setValue(member);
                                        //Toast.makeText(SignUp.this,"Successful data", Toast.LENGTH_LONG).show();

                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignUp.this, "New user created", Toast.LENGTH_SHORT).show();

                                //updateUI(user);
                                Intent menuActivity = new Intent(SignUp.this, Menu.class);
                                startActivity(menuActivity);
                                finish();
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed."+ task.getException(), Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
                }

                else{
                    Password2.setError("Password is not the same !");
                    return;
                }
            }
        });


        BackToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(SignUp.this, MainActivity.class);
                startActivity(mainActivity); // Démarre une activité
                finish();
            }

        });
    }
}
