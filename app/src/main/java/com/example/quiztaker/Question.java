package com.example.quiztaker;

public class Question {
    private String queText;
    private String[] options;
    private int answer;

    public Question(String queText, String[] options, int answer){
        this.queText = queText;
        this.options = options;
        this.answer = answer;
    }
    public String getQuestion(){
        return queText;
    }
    public String[] getOptions(){
        return  options;
    }
    public int getAnswer(){
        return answer;
    }
}
