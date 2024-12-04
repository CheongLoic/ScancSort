package scansort.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class PlayNiveauActivity extends AppCompatActivity {

    private Button debutantButton;
    private Button intermediaireButton;
    private Button difficileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_niveau);

        debutantButton = (Button) findViewById(R.id.activity_niveau_debutant_btn);
        intermediaireButton = (Button) findViewById(R.id.activity_niveau_intermediaire_btn);
        difficileButton = (Button) findViewById(R.id.activity_niveau_difficile_btn);

        intermediaireButton.setEnabled(false);
        difficileButton.setEnabled(false);

        debutantButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openPlayActivity();
            }
        });
    }

    public void openPlayActivity(){
        Intent intent = new Intent(PlayNiveauActivity.this, PlayActivity.class);
        startActivity(intent);
    }
}