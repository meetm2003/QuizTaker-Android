package com.example.quiztaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Quiz_page extends AppCompatActivity {

    private List<Question> questionList;
//    private LinearLayout questionLayout;
//    private TextView queTV;
//    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

//        questionLayout = findViewById(R.id.questionLayout);

        questionList = createQuestions();

        // Display the first question
            displayQuestions();
        Button checkAnswersButton = findViewById(R.id.checkAnswersButton);
        checkAnswersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalScore = calculateTotalScore();
                Intent intent = new Intent(Quiz_page.this, quiz_start_page.class);
                intent.putExtra("TOTAL_SCORE", totalScore);
                startActivity(intent);
            }
        });
    }
    private List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("What is the capital of France?", new String[]{"Paris", "Rome", "Berlin", "Madrid"}, 0));
        questions.add(new Question("What is the largest planet in our solar system?", new String[]{"Jupiter", "Saturn", "Mars", "Earth"}, 1));
        questions.add(new Question("What is the chemical symbol for water?", new String[]{"H2O", "CO2", "NaCl", "O2"}, 2));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", new String[]{"William Shakespeare", "Jane Austen", "Charles Dickens", "Mark Twain"}, 0));
        questions.add(new Question("What is the powerhouse of the cell?", new String[]{"Mitochondria", "Nucleus", "Ribosome", "Golgi apparatus"}, 3));
        questions.add(new Question("What year did the Titanic sink?", new String[]{"1912", "1901", "1923", "1939"}, 0));
        questions.add(new Question("Who painted the Mona Lisa?", new String[]{"Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Michelangelo"}, 0));
        questions.add(new Question("What is the chemical symbol for gold?", new String[]{"Au", "Ag", "Fe", "Cu"}, 1));
        questions.add(new Question("Who discovered penicillin?", new String[]{"Alexander Fleming", "Isaac Newton", "Albert Einstein", "Marie Curie"}, 0));
        questions.add(new Question("What is the tallest mammal?", new String[]{"Giraffe", "Elephant", "Hippopotamus", "Rhino"}, 0));

        return questions;
    }

    private void displayQuestions() {
        try {
            LinearLayout questionLayout = findViewById(R.id.questionLayout);
            questionLayout.removeAllViews();

            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                TextView questionTextView = new TextView(this);
                // Set text size for questionTextView
                questionTextView.setTextSize(22); // Adjust text size as needed
                // Set layout parameters for questionTextView
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                // Add margins to questionTextView
                params.setMargins(0, 19, 0, 16); // Adjust margins as needed
                questionTextView.setLayoutParams(params);
                questionTextView.setText(question.getQuestion());
                questionLayout.addView(questionTextView);

                RadioGroup optionsRadioGroup = new RadioGroup(this);
                optionsRadioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                String[] options = question.getOptions();
                for (int j = 0; j < options.length; j++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    radioButton.setText(options[j]);
                    radioButton.setTextSize(18);
                    optionsRadioGroup.addView(radioButton);
                }

                questionLayout.addView(optionsRadioGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Exception: " + e.getMessage());
        }
    }

    private int calculateTotalScore() {
        int totalScore = 0;
        LinearLayout questionLayout = findViewById(R.id.questionLayout);

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            RadioGroup optionsRadioGroup = (RadioGroup) questionLayout.getChildAt(i * 2 + 1);
            int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedOptionId != -1) {
                RadioButton selectedOption = findViewById(selectedOptionId);
                int selectedOptionIndex = optionsRadioGroup.indexOfChild(selectedOption);
                if (selectedOptionIndex == question.getAnswer()) {
                    totalScore++;
                }
            }
        }
        return totalScore;
    }
}