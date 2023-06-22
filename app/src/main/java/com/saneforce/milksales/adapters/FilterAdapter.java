package com.saneforce.milksales.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saneforce.milksales.Activity.ViewActivity;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class FilterAdapter extends BaseAdapter {
    LinearLayout lin_lay1,lin_lay2,lin_lay3,lay;
    Context context;

    JSONArray ja;
    String value,target;
    int x;
    SharedPreferences share;

    public FilterAdapter(Context context, JSONArray ja,String value,int x,String target) {
        this.context = context;
        this.ja=ja;
        this.value=value;
        this.target=target;
        this.x=x;
    }

    @Override
    public int getCount() {
        return ja.length();
    }

    @Override
    public Object getItem(int i) {
       /* try {
            return ja.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_filter,viewGroup,false);
        lin_lay1=view.findViewById(R.id.lin_lay1);
        lin_lay2=view.findViewById(R.id.lin_lay2);
        lin_lay3=view.findViewById(R.id.lin_lay3);
        lay=view.findViewById(R.id.lay);

        try {
            JSONObject js=ja.getJSONObject(i);
            createTxtView(js);
           /* Log.v("printing_current_json", ja.length()+" "+ja.toString());

            */
            //txt_sf.setText("helllow");
           /* Log.v("printing_current_jsonob", js.toString()+" ivalue "+i);
            createTxtView(js);*/



            if(x==0 && !target.equalsIgnoreCase("null")) {
                lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("printing_current_json", js.toString());
                        JSONArray jj = new JSONArray();
                        jj.put(js);
                        share=context.getSharedPreferences("existing",0);
                        SharedPreferences.Editor edit=share.edit();
                        edit.putString("exist","E");
                        edit.putString("fab","1");
                        edit.putString("value",value);
                        edit.putString("arr",jj.toString());
                        edit.commit();
                        Intent i = new Intent(context, ViewActivity.class);
                        i.putExtra("frmid", target);
                        i.putExtra("btn_need", "0");
                        context.startActivity(i);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("printing_zip_value",e.getMessage());
        }
        return view;
    }
    public void createTxtView(JSONObject js){

        String[] splitbyComma=value.split(",");

        try{

            for(int i=0;i<splitbyComma.length;i++){
                if(splitbyComma[i].length()-1 == splitbyComma[i].indexOf(";")) {
                    createDynamicview(0, js.getString(splitbyComma[i].substring(0, splitbyComma[i].indexOf(";"))));
                    createDynamicview(1, "");
                }
                else{
                    createDynamicview(0,splitbyComma[i].substring(splitbyComma[i].indexOf(";")+1));
                    createDynamicview(1,js.getString(splitbyComma[i].substring(0,splitbyComma[i].indexOf(";"))));
                }

            }
        }catch (Exception e){}

    }

    public void createDynamicview(int x,String val){
        TextView txt=new TextView(context);
        Log.v("txt_str_view",val);
        txt.setText(val);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setTextSize(15f);
        txt.setSingleLine(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(10,5,0,0);
        txt.setLayoutParams(layoutParams);
        if(x==0)
            lin_lay1.addView(txt);
        else
            lin_lay3.addView(txt);
    }
}

