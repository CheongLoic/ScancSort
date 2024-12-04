package scansort.fr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {
    private static final String TAG  = "Menu Activity";
    private Button scanButton;
    private Button mapButton;
    private Button compteButton;
    private Button playButton;
    private Button logoutButton;
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        scanButton = (Button) findViewById(R.id.scanButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        compteButton = (Button) findViewById(R.id.compteButton);
        playButton = (Button) findViewById(R.id.playButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        /*
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String UserID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Member").child(UserID);
        mDatabase.getDatabase();*/




        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openScanActivity();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openMapActivity();
            }
        });

        compteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                opencompteActivity();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openPlayNiveauActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                openMainActivity();
                finish();
            }
        });



    }




    public void openScanActivity() {
        Intent intent = new Intent(Menu.this, ScanActivity.class);
        startActivity(intent);
    }


    public void opencompteActivity(){
        Intent intent = new Intent( Menu.this, compteActivity.class);
        startActivity(intent);
    }

    public void openPlayNiveauActivity(){
        Intent intent = new Intent(Menu.this, PlayNiveauActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(Menu.this, MainActivity.class);
        startActivity(intent);

    }

    public void openMapActivity(){
        Intent intent = new Intent(Menu.this, MapsActivity.class);
        startActivity(intent);

    }

}
