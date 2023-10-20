package com.milksales.godairy.SFA_Adapter;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class rcylPayledgerAda  extends RecyclerView.Adapter<rcylPayledgerAda.MyViewHolder>{
    JSONArray jLists;
    int RowLayout;
    Context context;

    public rcylPayledgerAda(JSONArray jList, int rowLayout, Context mcontext){
        jLists=jList;
        RowLayout=rowLayout;
        context = mcontext;
    }

    @NonNull
    @Override
    public rcylPayledgerAda.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(RowLayout, parent, false);
        return new rcylPayledgerAda.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rcylPayledgerAda.MyViewHolder holder, int position) {
        try{
            JSONObject jItem=jLists.getJSONObject(position);

            holder.tvDate.setText(jItem.getString("LedgDate"));
            holder.tvDebit.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.getDouble("Debit")));
            holder.tvCredit.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.getDouble("Credit")));
            holder.tvBal.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(jItem.getDouble("Balance")));
            if(jItem.getDouble("Balance")>=0){
                holder.tvBal.setTextColor(context.getResources().getColor(R.color.greentext));
            }else{
                holder.tvBal.setTextColor(context.getResources().getColor(R.color.color_red));
            }
        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return jLists.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvDebit,tvCredit,tvBal;
        LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            try {
                parent_layout = view.findViewById(R.id.parent_layout);

                tvDate = itemView.findViewById(R.id.tvDate);
                tvDebit = itemView.findViewById(R.id.tvDebit);
                tvCredit = itemView.findViewById(R.id.tvCredit);
                tvBal = itemView.findViewById(R.id.tvBalance);

            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }
    }
}
