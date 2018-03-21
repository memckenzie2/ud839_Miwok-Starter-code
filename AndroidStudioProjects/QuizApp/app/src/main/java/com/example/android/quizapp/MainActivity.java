package com.example.android.quizapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    private String missedQuestions;
    /*

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Generates solutions to the random problems
        solutionQ4 = orderOpProb4();
        solutionQ5 = orderOpProb5();
        solutionQ2 = randomQ2();
        missedQuestions = "You missed the following questions:\n";
    }

    /*
    This method is called when the order button is clicked.
     */
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

    /* Updates question 2 Textview with problem statement
     * @param questionChar is the letter from PEMDAS that is being used for the current version of the question.
     */
    private void displayProb2(String questionChar) {
        TextView quantityTextView = findViewById(R.id.question2);
        quantityTextView.setText("2. What does the " + questionChar + " in the acronym PEMDAS stand for?");
    }

    /* Updates question 4 Textview with problem statement
    * @param problem is the problem statement.
    */
    private void displayProb4(String problem) {
        TextView quantityTextView = findViewById(R.id.question4);
        quantityTextView.setText(problem);
    }

    /* Updates question 5 Textview with problem statement
     * @param problem is the problem statement..
     */
    private void displayProb5(String problem) {
        TextView quantityTextView = findViewById(R.id.question5);
        quantityTextView.setText(problem);
    }

    /*
    Chooses a random letter from PEMDAS, generates a problem statement for question 2 to be displayed, and returns the correct solution for the problem.
    */
    public String randomQ2(){
        String question = "";
        String answer = "";
        Random randNum = new Random();
        int randAns = randNum.nextInt(5);

        //Chooses which letter from PEMDAS with equal probability based on randomly generated value from 0-5.
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

        //Updates the Question 2 Textview
        displayProb2(question);

        return answer;
    }

    /*
    Generates random numbers to populate question 4's expression: a - b * c + d
     */
    public int orderOpProb4(){

        Random rand = new Random();
        int solution;

        int a = rand.nextInt(50) + 1;
        int b = rand.nextInt(50) + 1;
        int c = rand.nextInt(50) + 1;
        int d = rand.nextInt(7) + 1;
        solution = a - b * c + d;

        questionText4 = "4. Solve: \n\n   " + Integer.toString(a) + " - " + Integer.toString(b) + " × " + Integer.toString(c) + " + " + Integer.toString(d);
        displayProb4(questionText4);
        return solution;
    }


    /*
    Generates random numbers to populate question 5's expression: z + a ÷ (d - b * c)
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
    Ensures that all questions have answers and if not sets the question textview to red.
    Displays a toastview to user asking them to answer questions in red.
    Return true if all questiuons have a response, false if one or more do not
    @param checkBoxSelected is true if a CheckBox for question 1 is selected
    @param editText2 the contents of the EditText field for question 2
    @param radioQ3 the id of the selected button in the radiobutton group for question 3. Will be -1 if no button is selected.
    @param editText4 the contents of the EditText field for question 4
    @param editText5 the contents of the EditText field for question 5
     */
    private boolean allQuestionsAnswer(boolean checkBoxSelected, String editText2, int radioQ3,String editText4, String editText5){
        boolean emptyAnswer = false;

        //Check if at least one checkbox is selected,
        TextView q1 = findViewById(R.id.question1);
        if(!checkBoxSelected){

            q1.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q1.setTextColor(Color.parseColor("#0026ca"));
        }

        //Check if edittext for question 3 has input
        TextView q2 = findViewById(R.id.question2);
        if(TextUtils.isEmpty(editText2)){
            q2.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q2.setTextColor(Color.parseColor("#0026ca"));
        }

        //check if radio button is selected
        TextView q3 = findViewById(R.id.question3);
        if(radioQ3 == -1){
            q3.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q3.setTextColor(Color.parseColor("#0026ca"));
        }

        //Check if edittext for question 4 has input
        TextView q4 = findViewById(R.id.question4);
        if(TextUtils.isEmpty(editText4)){
            q4.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q4.setTextColor(Color.parseColor("#0026ca"));
        }

        //Check if edittext for question 5 has input
        TextView q5 = findViewById(R.id.question5);
        if(TextUtils.isEmpty(editText5)){
            q5.setTextColor(Color.RED);
            emptyAnswer = true;
        }
        else{
            q5.setTextColor(Color.parseColor("#0026ca"));
        }

        return emptyAnswer;
    }

    /*
    Checks the response to question 1 against the correct solution.
    Correct response is 2nd and 3rd checkboxes selected and none others.
    Returns true if correct.
    @param check1 is the status of the 1st checkbox option
    @param check2 is the status of the 2nd checkbox option
    @param check3 is the status of the 3rd checkbox option
    @param check4 is the status of the 4th checkbox option
     */
    private boolean markQ1(boolean check1, boolean check2, boolean check3, boolean check4){
        if (!check1 && check2 && check3 && !check4) {
            return true;
        } else{
            missedQuestions = missedQuestions + "Question 1\n" + R.string.q1 + "\n";
            return false;
        }
    }

    /*
    Checks the response to question 2 against the correct solution.
    Correct response is stored in solutionQ2 and is randomly selected from possible PEMDAS values at launch.
    Returns true if correct.
    @param responseQ2 is the contents of the edittext for question 2.
     */
    private boolean markQ2(String responseQ2){
        if ( responseQ2.toLowerCase().equals(solutionQ2)) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 2\n" + R.string.q2 + "\n";
            return false;
        }
    }

    /*
    Checks the response to question 3 by checking status of correct response (4th option)
    Returns true if correct.
    @param responseQ3 is the status of the correct radiobutton option - it is true if selected.
     */
    private boolean markQ3(boolean correctQ3){
        if (correctQ3) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 3\n" + R.string.q3 + "\n";
            return false;
        }
    }

    /*
    Checks the response to question 4 against the correct solution.
    Correct response is stored in solutionQ4 from the resulting randomly generated values for problem that are initialized during launch..
    Returns true if correct.
    @param responseQ4 is the contents of the edittext for question 4.
     */
    private boolean markQ4(int responseQ4){
        if ( responseQ4 == solutionQ4) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 4\n" + R.string.q4 + "\n";
            return false;
        }
    }

    /*
    Checks the response to question 5 against the correct solution.
    Correct response is stored in solutionQ4 from the resulting randomly generated values for problem that are initialized during launch..
    Returns true if correct.
    @param responseQ5 is the contents of the edittext for question 5.
     */
    private boolean markQ5(int responseQ5){
        if ( responseQ5 == solutionQ5) {
            return true;
        }
        else{
            missedQuestions = missedQuestions + "Question 5\n" + R.string.q5 + "\n";
            return false;
        }
    }

    /*
    Calculates the final score as a percentage of correct / number of questions.
    Displays a toast message to user with resulting percentage and ratio.
    @param correct is the number of correct answers
     */
    private void finalScore(int correct){
        double percentCorrect = (double)correct/5 * 100;
        String scoreMessage;

        scoreMessage = "You earned " + Double.toString(percentCorrect) + "%. You got " + Integer.toString(correct) + "/5 correct!";

        Toast toast = new Toast(getApplicationContext());
        Toast.makeText(getApplicationContext(), scoreMessage,
                Toast.LENGTH_LONG).show();
    }
}

