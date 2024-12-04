package scansort.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class compteActivity extends AppCompatActivity {

    private static final String TAG  = "CompteActivity";
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private TextView score;
    private Button submitButton;
    private Member member;
    private boolean buttonClicked;
    private ProgressBar progressBar;

    private String LASTNAME; //EditText
    private String FIRSTNAME;  //EditText
    private String EMAIL; //EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        buttonClicked = false;
        progressBar = (ProgressBar) findViewById(R.id.activity_compte_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        lastName = (EditText) findViewById(R.id.activity_compte_editText_lastname);
        firstName = (EditText) findViewById(R.id.activity_compte_editText_firstname);
        email = (EditText) findViewById(R.id.activity_compte_editText_email);
        score = (TextView) findViewById(R.id.activity_compte_textView_score_displayed);
        submitButton = (Button) findViewById(R.id.activity_compte_button);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String UserID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Member").child(UserID);
        //mDatabase.getDatabase();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LASTNAME = lastName.getText().toString().trim();
                FIRSTNAME = firstName.getText().toString().trim();
                EMAIL = email.getText().toString().trim();

                if(TextUtils.isEmpty(EMAIL)&& TextUtils.isEmpty(LASTNAME)&&TextUtils.isEmpty(FIRSTNAME)){
                    lastName.setError("Your last name is required");
                    firstName.setError("Your first name is required");
                    email.setError("An email address is required");
                    return;
                }
                if(TextUtils.isEmpty(LASTNAME)){
                    lastName.setError("Your last name is required");
                    return;
                }
                if(TextUtils.isEmpty(FIRSTNAME)){
                    firstName.setError("Your first name is required");
                    return;
                }
                if(TextUtils.isEmpty(EMAIL)){
                    email.setError("Email is required");
                    return;
                }

                else{
                    lastName.setHint(LASTNAME);
                    firstName.setHint(FIRSTNAME);
                    email.setHint(EMAIL);
                    buttonClicked = true;
                    //Toast.makeText(compteActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            lastName.setHint((String) dataSnapshot.child("lastName").getValue().toString());
                            firstName.setHint((String) dataSnapshot.child("firstName").getValue().toString());
                            email.setHint((String) dataSnapshot.child("email").getValue().toString());
                            if(buttonClicked){

                                member = new Member(LASTNAME,FIRSTNAME,EMAIL);
                                member.setScore((String) dataSnapshot.child("score").getValue().toString());
                                member.setPassword((String) dataSnapshot.child("password").getValue().toString());
                                mDatabase.setValue(member);
                                buttonClicked = false;
                                Toast.makeText(compteActivity.this, "User's data updated", Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                lastName.setHint((String) dataSnapshot.child("lastName").getValue().toString());
                firstName.setHint((String) dataSnapshot.child("firstName").getValue().toString());
                email.setHint((String) dataSnapshot.child("email").getValue().toString());
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


    }
}
