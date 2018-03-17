package com.example.android.quizapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String solutionQ2;
    private int solutionQ4;
    private String questionText4;
    private int solutionQ5;
    private String questionText5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solutionQ4 = orderOpProb4();
        solutionQ5 = orderOpProb5();
        solutionQ2 = randomQ2();
    }

    public void onSubmit(View view) {
        int correct = 0;
        CheckBox check1Q1 = findViewById(R.id.q1check1);
        CheckBox check2Q1 = findViewById(R.id.q1check2);
        CheckBox check3Q1 = findViewById(R.id.q1check3);
        CheckBox check4Q1 = findViewById(R.id.q1check4);

        EditText editTextQ2 = findViewById(R.id.question2Edit);
        String responseQ2 = editTextQ2.getText().toString();

        EditText editTextQ4 = findViewById(R.id.question4Edit);
        String responseQ4 = editTextQ4.getText().toString();

        EditText editTextQ5 = findViewById(R.id.question5Edit);
        String responseQ5 = editTextQ5.getText().toString();

        boolean checkBoxSelected = check1Q1.isChecked() || check2Q1.isChecked() || check3Q1.isChecked() || check4Q1.isChecked();

        if(allQuestionsAnswer(checkBoxSelected, responseQ2, responseQ4, responseQ5)){
            Toast toast = new Toast(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Uh-oh, you're not done! Please answer the questions highlighted in red.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            //String to store missed questions to player.
            String missedQuestions = "You missed the following questions:\n";

            //Question 1, Check boxes 2 and 3 should be the only ones selected


            if (!check1Q1.isChecked() && check2Q1.isChecked() && check3Q1.isChecked() && !check4Q1.isChecked()) {
                correct += 1;
            } else{
                missedQuestions = missedQuestions + "Question 1\n" + R.string.q1 + "\n";
            }

            //Question 2


            if ( responseQ2.toLowerCase() == solutionQ2) {
                correct += 1;
            }
            else{
                missedQuestions = missedQuestions + "Question 2\n" + R.string.q2 + "\n";
            }

            //Question 3
            RadioButton radioCorrectQ3 =  findViewById(R.id.q3radio4);

            if (radioCorrectQ3.isChecked()) {
                correct += 1;
            }
            else{
                missedQuestions = missedQuestions + "Question 3\n" + R.string.q3 + "\n";
            }

            //Question 4

            if ( Integer.parseInt(responseQ4) == solutionQ4) {
                correct += 1;
            }
            else{
                missedQuestions = missedQuestions + "Question 4\n" + questionText4 + "\n";
            }

            //Question 5

            if ( Integer.parseInt(responseQ5) == solutionQ5) {
                correct += 1;
            }
            else{
                missedQuestions = missedQuestions + "Question 5\n" + questionText5 + "\n";
            }

            double percentCorrect = (double)correct/5 * 100;
            String scoreMessage;

            scoreMessage = "You earned " + Double.toString(percentCorrect) + "%. You got " + Integer.toString(correct) + "/5 correct!";


            Toast toast = new Toast(getApplicationContext());
            Toast.makeText(getApplicationContext(), scoreMessage,
                    Toast.LENGTH_LONG).show();


        }

    }

    private void displayProb5(String problem) {
        TextView quantityTextView = findViewById(R.id.question5);
        quantityTextView.setText(problem);
    }

    private void displayProb4(String problem) {
        TextView quantityTextView = findViewById(R.id.question4);
        quantityTextView.setText(problem);
    }


    /*
    Chooses a random letter from PEMDAS, updates the question, and assigns the appropriate answer for it.
    */
    public String randomQ2(){
        String answer = "parentheses";
        return answer;
    }

    /*
    Generates random numbers to populate: z + a ÷ (d - b * c)
    To generate a problem with a whole number solution the random numbers must be generated such that...
    a must be divisible by the correct solution to (d - b * c)

    This will be done by generating b, c, and d first and generating a to be a multiple of the result of (d - b * c).
     */
    public int orderOpProb5(){

        Random rand = new Random();
        int solution;

        int b = rand.nextInt(10) + 1;
        int c = rand.nextInt(10) + 1;
        int d = rand.nextInt(10) + 1;
        int aDiv = rand.nextInt(7) + 1;
        int a = aDiv * (d + b * c);
        int z = rand.nextInt(10) + 1;
        solution = z - aDiv;

        questionText5 = "5. Solve: \n\n   " + Integer.toString(z) + " - " +  Integer.toString(a) + " ÷ " + "(" + Integer.toString(d) +" + " + Integer.toString(b) + " × " + Integer.toString(c) + ")";
        displayProb5(questionText5);
        return solution;
    }

    /*
    Generates random numbers to populate: a - b * c + e
    To generate a problem with a whole number solution the random numbers must be generated such that...
    a must be divisible by the correct solution to (d - b * c)  AND the most common incorrect solutions (solving inside parentheses from left to right)
    This ensures a reasonable answer is given even if the student makes a mistake.
    This will be done by generating b, c, d, and f first and generating a to be a multiple of the result of (d - b * c). The multiple will be the solution to the problem.
     */
    public int orderOpProb4(){

        Random rand = new Random();
        int solution;

        int a = rand.nextInt(50) + 1;
        int b = rand.nextInt(50) + 1;
        int c = rand.nextInt(50) + 1;
        int d = rand.nextInt(7) + 1;
        solution = a - b * c + d;

        questionText4 = "4. Solve: \n\n   " + Integer.toString(a) + " - " +  Integer.toString(b)  + " × " + Integer.toString(c) + "+" + Integer.toString(d);
        displayProb4(questionText4);
        return solution;
    }


    boolean allQuestionsAnswer(boolean checkBoxSelected, String editText2, String editText4, String editText5){
        boolean emptyAnswer = false;

        TextView q1 = findViewById(R.id.question1);
        if(!checkBoxSelected){

            q1.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q1.setTextColor(Color.parseColor("#304fff"));
        }

        TextView q2 = findViewById(R.id.question2);
        if(TextUtils.isEmpty(editText2)){
            q2.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q2.setTextColor(Color.parseColor("#304fff"));
        }

        RadioGroup q3Group = findViewById(R.id.q3rradiogroup);

        TextView q3 = findViewById(R.id.question3);
        if(q3Group.getCheckedRadioButtonId() == -1){
            q3.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q3.setTextColor(Color.parseColor("#304fff"));
        }

        TextView q4 = findViewById(R.id.question4);
        if(TextUtils.isEmpty(editText4)){
            q4.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q4.setTextColor(Color.parseColor("#304fff"));
        }

        TextView q5 = findViewById(R.id.question5);
        if(TextUtils.isEmpty(editText5)){
            q5.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q5.setTextColor(Color.parseColor("#304fff"));
        }

        return emptyAnswer;
    }
}
