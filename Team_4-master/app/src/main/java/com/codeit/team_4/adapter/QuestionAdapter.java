package com.codeit.team_4.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeit.team_4.R;
import com.codeit.team_4.helper.Question;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Question> productList;

    public QuestionAdapter(Context mCtx, List<Question> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.question_list, null);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        Question product = productList.get(position);

        //loading the image
        holder.textViewQuestion.setText(product.getQuestion());

        holder.textViewAns.setText(product.getAnswer());
        if(position ==0)
        {
            holder.expandableLayout.expand();
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ExpandableLayout.OnExpansionUpdateListener  {

        TextView textViewQuestion ;
        private ExpandableLayout expandableLayout;
        TextView textViewAns;

        ProductViewHolder(View itemView) {

            super(itemView);

            textViewQuestion = itemView.findViewById(R.id.tv_question);
            textViewAns = (TextView) itemView.findViewById(R.id.tv_correctAnswer);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expanded_menu);

            //expandableLayout.setOnClickListener(this);
            textViewQuestion.setOnClickListener(this);
            //textViewans = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View view) {
            expandableLayout.toggle();
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            // recyclerView.smoothScrollToPosition(getAdapterPosition());
        }
    }
}