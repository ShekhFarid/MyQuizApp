package com.shekhfarid.myquizapp.home.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.shekhfarid.myquizapp.R;
import com.shekhfarid.myquizapp.databinding.ItemQuestionBinding;

import java.util.List;

public class Question_Adepter extends RecyclerView.Adapter<Question_Adepter.ViewHolder>{

    private List<Question_Model> questionModelList;
    private Question_Adepter.AdepterListener listener;
    ItemQuestionBinding questionBinding;

    public Question_Adepter(List<Question_Model> questionModelList) {
        this.questionModelList = questionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        questionBinding = DataBindingUtil.inflate(inflater, R.layout.item_question,parent,false);
        View view = questionBinding.getRoot();
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setListener(AdepterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        questionBinding.idNo.setText((position + 1) + "");
        questionBinding.itemQuestion.setText(questionModelList.get(position).getQuestion());
        questionBinding.itemAText.setText(questionModelList.get(position).getaOption());
        questionBinding.itemBText.setText(questionModelList.get(position).getbOption());
        questionBinding.itemCText.setText(questionModelList.get(position).getcOption());
        questionBinding.itemDText.setText(questionModelList.get(position).getdOption());
        questionBinding.itemAnswerText.setText(questionModelList.get(position).getRightAns());

        if(listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(questionModelList.get(position),position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public interface AdepterListener{
        public void onItemClick(Question_Model question_model, int position);
    }
}
