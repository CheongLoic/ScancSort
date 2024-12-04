package scansort.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import scansort.fr.R;
import scansort.fr.model.Question;
import scansort.fr.model.QuestionBank;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        System.out.println("PlayActivity::onCreate()");

        mQuestionBank = this.generateQuestions();

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 7;
        }

        mEnableTouchEvents = true;

        // Wire widgets
        mQuestionTextView = (TextView) findViewById(R.id.activity_play_question_text);
        mAnswerButton1 = (Button) findViewById(R.id.activity_play_answer1_btn);
        mAnswerButton2 = (Button) findViewById(R.id.activity_play_answer2_btn);
        mAnswerButton3 = (Button) findViewById(R.id.activity_play_answer3_btn);
        mAnswerButton4 = (Button) findViewById(R.id.activity_play_answer4_btn);


        // Use the tag property to 'name' the buttons
        mAnswerButton1.setTag(0);
        mAnswerButton2.setTag(1);
        mAnswerButton3.setTag(2);
        mAnswerButton4.setTag(3);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if (responseIndex == mCurrentQuestion.getAnswerIndex()) {
            // Good answer
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            // Wrong answer
            Toast.makeText(this, "Réponse incorrecte!", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;

                // If this is the last question, ends the game.
                // Else, display the next question.
                if (--mNumberOfQuestions == 0) {
                    // End the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Votre score est " + mScore + " /7")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // End the activity
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void displayQuestion(final Question question) {
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question("Comment appelle-t-on les gros déchets comme les vieux frigos, les meubles cassés, les machines à laver ?",
                Arrays.asList("Les gênants", "Les embarassants", "Les encombrants", "Les déchets lourds"),
                2);

        Question question2 = new Question("Comment se débarrassait-on des déchets avant l'invention des poubelles ?",
                Arrays.asList("On jetait tout par terre dans la rue", "On brûlait tout", "On enterrait tout au fond du jardin", "On les gardait"),
                2);

        Question question3 = new Question("A quoi correspond la poubelle de couleur verte ?",
                Arrays.asList("Au verre", "Au plastique", "Aux autres déchets", "Ça n'existe pas"),
                0);

        Question question4 = new Question("Tu dois acheter un kilo de pâtes. Il vaut mieux acheter :",
                Arrays.asList("2 paquets de 500g", "1 paquet de 1 kilo", "4 paquets de 250g", "8 paquets de 125g"),
                1);

        Question question5 = new Question("Combien de temps met un sac en plastique jeté dans la nature pour se décomposer ?",
                Arrays.asList("45 ans", "450 ans", "Il ne se décompose jamais", "1 jour"),
                1);

        Question question6 = new Question("Une immense nappe de déchets flotte dans l'Océan Pacifique. Elle a la taille…",
                Arrays.asList("d'un stade de foot", "de la France", "d'une piscine olympique", "aucune idée"),
                1);

        Question question7 = new Question("Combien de temps met une bouteille de verre jetée dans la nature pour se décomposer ?",
                Arrays.asList("Elle ne se décompose jamais", "Entre 4000 et 5000 ans", "Entre 400 et 500 ans", "Entre 5 et 10 ans"),
                1);

        Question question8 = new Question("Dans quelle poubelle jette-t-on les piles ?",
                Arrays.asList("Dans la poubelle verte", "Dans la poubelle bleue", "Dans aucune des 2", "N'importe"),
                2);

        Question question9 = new Question("Peux tu mettre un emballage de pizza en carton dans la poubelle jaune ?",
                Arrays.asList("Non", "Oui", "Ça dépend", "Aucune idée"),
                2);

        Question question10 = new Question("Chaque jour, l'ensemble des français produit beaucoup de déchets. Avec tous ces déchets on pourrait remplir…",
                Arrays.asList("1950 piscines olympiques", "19500 piscines olympiques", "195000 piscines olympiques", "195 piscines olympiques"),
                2);

        Question question11 = new Question("En triant correctement nos déchets, avec 19 000 boites de conserves récupérées, je peux reconstruire:",
                Arrays.asList("Une trottinette", "Une machine à laver", "Un vélo", "Une voiture"),
                3);

        Question question12 = new Question("De plus en plus souvent, nous trouvons sur le marché des produits dont l’emballage porte un \"point vert\" : un logo rond formé de deux flèches inversées. Quel est sa signification:",
                Arrays.asList("Produit recyclable", "Produit recyclé", "Produit de qualité", "L'entreprise qui fabrique le produit aide à la collecte et au tri des déchets d’emballage"),
                3);

        Question question13 = new Question("Que doit-on faire avec les médicaments trop vieux ?",
                Arrays.asList("On les jette à la poubelle", "On les rapporte à la pharmacie", "On les jette dans la rue", "On les jette dans les toilettes"),
                0);

        Question question14 = new Question("Combien de temps met un pneu de voiture jeté dans la nature pour se décomposer ?",
                Arrays.asList("1000 ans", "50 ans", "500 ans", "Il ne se décompose jamais"),
                3);

        Question question15 = new Question("Qui a inventé la poubelle au XIX siècle ?",
                Arrays.asList("Maximilien déchet", "Eugène poubelle", "Henri ordures", "Christian Denis"),
                1);

        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10,
                question11,
                question12,
                question13,
                question14,
                question15));
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("PlayActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("PlayActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("PlayActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("PlayActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("PlayActivity::onDestroy()");
    }
}