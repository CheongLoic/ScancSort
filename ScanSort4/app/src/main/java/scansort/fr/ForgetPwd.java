package scansort.fr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPwd extends AppCompatActivity {

    private EditText Email;
    private Button Button;
    private TextView BackToLogin;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        Email = (EditText) findViewById(R.id.activity_forget_email);
        Button = (Button) findViewById(R.id.activity_forget_button);
        BackToLogin = (TextView) findViewById(R.id.activity_forget_backToLogin);
        fAuth = FirebaseAuth.getInstance();

        Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String mail = Email.getText().toString();

                if(TextUtils.isEmpty(mail)){
                    Email.setError("Email is required");
                    return;
                }
                else {

                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ForgetPwd.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            Intent mainActivity = new Intent(ForgetPwd.this, MainActivity.class);
                            startActivity(mainActivity); // Démarre une activité
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgetPwd.this, "Error ! Failure to send the link to reset password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(ForgetPwd.this, MainActivity.class);
                startActivity(mainActivity); // Démarre une activité
                finish();
            }
        });
    }
}
