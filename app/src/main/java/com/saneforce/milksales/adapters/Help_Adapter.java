package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.On_ItemCLick_Listner;
import com.saneforce.milksales.Model_Class.Help_Model;
import com.saneforce.milksales.R;

import java.util.List;

public class Help_Adapter extends RecyclerView.Adapter<Help_Adapter.MyViewHolder> {

    private List<Help_Model> Leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    On_ItemCLick_Listner mAdapterOnClick;
    Integer dummy;
    private int type;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, question, answer, leavedays;
        LinearLayout qnada, onlyquestion;
        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            qnada = view.findViewById(R.id.qnada);
            onlyquestion = view.findViewById(R.id.onlyquestion);
            question = view.findViewById(R.id.question);
            answer = view.findViewById(R.id.answer);
        }
    }


    public Help_Adapter(List<Help_Model> Leave_Approval_ModelsList, int rowLayout, Context context, int type, On_ItemCLick_Listner listner) {
        this.Leave_Approval_ModelsList = Leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = listner;
        this.context = context;
        this.type = type;
    }

    @Override
    public Help_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);


        return new Help_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Help_Adapter.MyViewHolder holder, int position) {
        Help_Model Help_Model = Leave_Approval_ModelsList.get(position);
        if (type == 1) {
            holder.textviewname.setText(Help_Model.getText());
            holder.onlyquestion.setVisibility(View.VISIBLE);
            holder.qnada.setVisibility(View.GONE);
        } else {
            holder.question.setText(Help_Model.getName());
            holder.answer.setText(Help_Model.getText());
            holder.onlyquestion.setVisibility(View.GONE);
            holder.qnada.setVisibility(View.VISIBLE);
        }


        holder.textviewname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leave_Approval_ModelsList.size();
    }
}