package com.saneforce.milksales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FilterRecycler extends RecyclerView.Adapter<FilterRecycler.MyViewHolder> {
    Context context;
    LinearLayout lin_lay1,lin_lay2,lin_lay3,lay;
    JSONArray ja;
    String value;
    int x;
    public FilterRecycler(Context context, JSONArray ja,String value,int x) {
        this.context = context;
        this.ja=ja;
        this.value=value;
        this.x=x;
        Log.v("json_lenght",ja.length()+" value "+value);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_filter,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            Log.v("printing_current_json", ja.toString());
            JSONObject js=ja.getJSONObject(i);
            createTxtView(js,myViewHolder);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ja.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout lin_lay1,lin_lay2,lin_lay3,lay;
        public MyViewHolder(@NonNull View view) {
            super(view);
            lin_lay1=view.findViewById(R.id.lin_lay1);
            lin_lay2=view.findViewById(R.id.lin_lay2);
            lin_lay3=view.findViewById(R.id.lin_lay3);
            lay=view.findViewById(R.id.lay);
        }
    }

    public void createTxtView(JSONObject js,MyViewHolder myViewHolder){

        String[] splitbyComma=value.split(",");

        try{

            for(int i=0;i<splitbyComma.length;i++){
                if(splitbyComma[i].length()-1 == splitbyComma[i].indexOf("/")) {
                    createDynamicview(0, js.getString(splitbyComma[i].substring(0, splitbyComma[i].indexOf("/"))),myViewHolder);
                    createDynamicview(1, "",myViewHolder);
                }
                else{
                    createDynamicview(0,splitbyComma[i].substring(splitbyComma[i].indexOf("/")+1),myViewHolder);
                    createDynamicview(1,js.getString(splitbyComma[i].substring(0,splitbyComma[i].indexOf("/"))),myViewHolder);

                }

            }
        }catch (Exception e){}

    }

    public void createDynamicview(int x,String val,MyViewHolder myViewHolder){
        TextView txt=new TextView(context);
        txt.setText(val);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setTextSize(15f);
        txt.setSingleLine(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(10,5,0,0);
        txt.setLayoutParams(layoutParams);
        if(x==0)
            myViewHolder.lin_lay1.addView(txt);
        else
            myViewHolder.lin_lay3.addView(txt);
    }

}
