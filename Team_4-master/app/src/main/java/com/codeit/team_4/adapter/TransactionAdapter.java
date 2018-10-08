package com.codeit.team_4.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeit.team_4.R;
import com.codeit.team_4.helper.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Transaction> productList;

    public TransactionAdapter(Context mCtx, List<Transaction> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.transaction_card, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Transaction product = productList.get(position);

        holder.TV_Name.setText(product.getName());
        holder.TV_pno.setText(product.getAccount());
        holder.TV_amount.setText(product.getAmount());
        holder.TV_type.setText(product.getType());
        holder.TV_desc.setText(product.getDescription());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView TV_Name,TV_pno,TV_amount,TV_type,TV_desc ;

        ProductViewHolder(View itemView) {
            super(itemView);

            TV_Name = itemView.findViewById(R.id.cardname);
            TV_pno = (TextView) itemView.findViewById(R.id.cardphone);
            TV_amount = (TextView) itemView.findViewById(R.id.cardamount);
            TV_type = (TextView) itemView.findViewById(R.id.cardtype);
            TV_desc = (TextView) itemView.findViewById(R.id.carddescription);
        }
    }

}
