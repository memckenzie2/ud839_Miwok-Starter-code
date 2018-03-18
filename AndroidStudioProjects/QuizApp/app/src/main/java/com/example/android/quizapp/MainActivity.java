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

    //String to store missed questions for later display to player.
    private String missedQuestions = "You missed the following questions:\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Generates solutions to the random problems
        solutionQ4 = orderOpProb4();
        solutionQ5 = orderOpProb5();
        solutionQ2 = randomQ2();
    }

    public void onSubmit(View view) {
        int correct = 0;

        //Pulls state of all response views
        //Response to Question 1
        CheckBox check1Q1 = findViewById(R.id.q1check1);
        CheckBox check2Q1 = findViewById(R.id.q1check2);
        CheckBox check3Q1 = findViewById(R.id.q1check3);
        CheckBox check4Q1 = findViewById(R.id.q1check4);

        //Reduces state of checkboxes to single true/false - true if ANY checkbox checked and false if none selected.
        boolean checkBoxSelected = check1Q1.isChecked() || check2Q1.isChecked() || check3Q1.isChecked() || check4Q1.isChecked();

        //Response to Question 2
        EditText editTextQ2 = findViewById(R.id.question2Edit);
        String responseQ2 = editTextQ2.getText().toString();

        //Response Question 3
        RadioGroup q3Group = findViewById(R.id.q3rradiogroup);
        int radioGroupQ3 = q3Group.getCheckedRadioButtonId();

        //Response to Question 4
        EditText editTextQ4 = findViewById(R.id.question4Edit);
        String responseQ4 = editTextQ4.getText().toString();

        //Response to Question 5
        EditText editTextQ5 = findViewById(R.id.question5Edit);
        String responseQ5 = editTextQ5.getText().toString();

        //Check all questions have a response. If not, highlights unanswered question in red and then displays a toast message
        if(allQuestionsAnswer(checkBoxSelected, responseQ2, radioGroupQ3, responseQ4, responseQ5)){
            Toast toast = new Toast(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Uh-oh, you're not done! Please answer the questions highlighted in red.",
                    Toast.LENGTH_LONG).show();
        }
        //If all questions has a response, check answers, score quiz, and display results in a toast message.
        else{

            //Question 1, Check boxes 2 and 3 should be the only ones selected
            boolean q1Result = markQ1(check1Q1.isChecked(), check2Q1.isChecked(), check3Q1.isChecked(), check4Q1.isChecked());
            int correctCount = q1Result ? 1 : 0;
            correct += correctCount;

            //Question 2
            boolean q2Result = markQ2(responseQ2);
            correctCount = q2Result ? 1 : 0;
            correct += correctCount;

            //Question 3
            RadioButton radioCorrectQ3 =  findViewById(R.id.q3radio4);
            boolean q3Result = markQ3(radioCorrectQ3.isChecked());
            correctCount = q3Result ? 1 : 0;
            correct += correctCount;

            //Question 4
            boolean q4Result = markQ4(Integer.parseInt(responseQ4));
            correctCount = q4Result ? 1 : 0;
            correct += correctCount;

            //Question 5
            boolean q5Result = markQ5(Integer.parseInt(responseQ5));
            correctCount = q5Result ? 1 : 0;
            correct += correctCount;

            //Calculate score and display as toast message
            finalScore(correct);
        }

    }

    //Updates question 2 Textview with problem statement
    private void displayProb2(String questionChar) {
        TextView quantityTextView = findViewById(R.id.question2);
        quantityTextView.setText("2. What does the " + questionChar + " in the acronym PEMDAS stand for?");
    }

    //Updates question 5 Textview with problem statement
    private void displayProb5(String problem) {
        TextView quantityTextView = findViewById(R.id.question5);
        quantityTextView.setText(problem);
    }

    //Updates question 5 Textview with problem statement
    private void displayProb4(String problem) {
        TextView quantityTextView = findViewById(R.id.question4);
        quantityTextView.setText(problem);
    }

    /*
    Chooses a random letter from PEMDAS, updates the question, and assigns the appropriate answer for it.
    */
    public String randomQ2(){
        String question = "";
        String answer = "";
        Random randNum = new Random();
        int randAns = randNum.nextInt(5);

        switch(randAns){
            case 0: question = "P";
                answer = "parentheses";
                break;
            case 1: question = "E";
                answer = "exponents";
                break;
            case 2: question = "M";
                answer = "multiplication";
                break;
            case 3: question = "D";
                answer = "division";
                break;
            case 4: question = "A";
                answer = "addition";
                break;
            case 5: question = "S";
                answer = "subtraction";
                break;
        }

        displayProb2(question);
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


    private boolean allQuestionsAnswer(boolean checkBoxSelected, String editText2, int radioQ3,String editText4, String editText5){
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

        TextView q3 = findViewById(R.id.question3);
        if(radioQ3 == -1){
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

    private boolean markQ1(boolean check1, boolean check2, boolean check3, boolean check4){
        if (!check1 && check2 && check3 && !check4) {
            return true;
        } else{
            missedQuestions = missedQuestions + "Question 1\n" + R.string.q1 + "\n";
            return false;
        }
    }

    private boolean markQ2(String responseQ2){
        if ( responseQ2.toLowerCase() == solutionQ2) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 2\n" + R.string.q2 + "\n";
            return false;
        }
    }

    private boolean markQ3(boolean correctQ3){
        if (correctQ3) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 3\n" + R.string.q3 + "\n";
            return false;
        }
    }

    private boolean markQ4(int responseQ4){
        if ( responseQ4 == solutionQ4) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 4\n" + R.string.q4 + "\n";
            return false;
        }
    }

    private boolean markQ5(int responseQ5){
        if ( responseQ5 == solutionQ5) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 5\n" + R.string.q5 + "\n";
            return false;
        }
    }

    private void finalScore(int correct){
        double percentCorrect = (double)correct/5 * 100;
        String scoreMessage;

        scoreMessage = "You earned " + Double.toString(percentCorrect) + "%. You got " + Integer.toString(correct) + "/5 correct!";

        Toast toast = new Toast(getApplicationContext());
        Toast.makeText(getApplicationContext(), scoreMessage,
                Toast.LENGTH_LONG).show();
    }
}

