package com.codeit.team_4.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeit.team_4.R;
import com.codeit.team_4.adapter.QuestionAdapter;
import com.codeit.team_4.helper.Question;

import java.util.ArrayList;
import java.util.List;

public class FAQFragment extends Fragment {
    RecyclerView recyclerView;
    List<Question> questionList;

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView)this.getActivity().findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(FAQFragment.this.getActivity()));
        List<Question> questionList;
        questionList= new ArrayList<>();
        questionList.add(new Question("What is Fixed Deposit?","A fixed deposit (FD) is a financial instrument provided by banks which provides investors with a higher rate of interest than a regular savings account, until the given maturity date. It may or may not require the creation of a separate account."));
        questionList.add(new Question("Cheque?","It is written by an individual to transfer amount between two accounts of the same bank or a different bank and the money is withdrawn form the account."));
        questionList.add(new Question("Demat account","The way in which a bank keeps money in a deposit account in the same way the Depository company converts share certificates into electronic form and keep them in a Demat account."));
        questionList.add(new Question("E-banking","It is a type of banking in which we can conduct financial transactions electronically. RTGS, Credit cards, Debit cards etc come under this category."));
        questionList.add(new Question("fiscal deficit?","It is the amount of Funds borrowed by the government to meet the expenditures."));
        questionList.add(new Question("Electronic fund transfer","In this we use Automatic teller machine, wire transfer and computers to move funds between different accounts in different or same bank."));
        questionList.add(new Question("Leverage ratio","It is a financial ratio which gives us an idea or a measure of a company’s ability to meet its financial losses."));
        questionList.add(new Question("Fund liquidity","It is the ability of converting an investment quickly into cash with no loss in value."));
        questionList.add(new Question("Repo rate","Commercial banks borrow funds by the RBI if there is any shortage in the form of rupees. If this rate increases it becomes expensive to borrow money from RBI and vice versa."));
        questionList.add(new Question("Universal banking","When financial institutions and banks undertake activities related to banking like investment, issue of debit and credit card etc then it is known as universal banking"));
        questionList.add(new Question("Virtual banking","Internet banking is sometimes known as virtual banking. It is called so because it has no bricks and boundaries. It is controlled by the world wide web"));

        QuestionAdapter adapter = new QuestionAdapter(FAQFragment.this.getActivity(), questionList);
        recyclerView.setAdapter(adapter);


    }

    public FAQFragment() {
        // Required empty public constructor
    }

    public static FAQFragment newInstance() {
        return new FAQFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

}
