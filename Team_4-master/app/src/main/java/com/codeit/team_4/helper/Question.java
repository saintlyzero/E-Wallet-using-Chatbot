package com.codeit.team_4.helper;

/**
 * Created by Shubham_pc on 05/11/2017.
 */

public class Question{
    private int qid;
    private String question;

    private String answer;

    public Question( String question,  String answer )
    {

        this.question=question;

        this.answer=answer;
    }






    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
