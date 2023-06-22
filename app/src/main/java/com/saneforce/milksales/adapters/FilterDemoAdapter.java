package com.saneforce.milksales.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.saneforce.milksales.Activity.ViewActivity;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterDemoAdapter extends BaseAdapter {
    RelativeLayout lay_header,lay;
    LinearLayout lin_lay1,lin_lay2;
    boolean pic=true;
    boolean phone=true;
    ArrayList<String> header=new ArrayList<>();
    ArrayList<String> footer=new ArrayList<>();
    LinearLayout rlay;
    int ki=0;
    Context context;
    View header_view;

    JSONArray ja;
    String value,target;
    int x;
    SharedPreferences share;
    boolean forHeader=false;
    CardView card_count;
    public FilterDemoAdapter(Context context, JSONArray ja,String value,int x,String target) {
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
        view= LayoutInflater.from(context).inflate(R.layout.row_item_list,viewGroup,false);
        lay_header=view.findViewById(R.id.lay_header);
        lay=view.findViewById(R.id.lay);
        lin_lay1=view.findViewById(R.id.lin_lay1);
        lin_lay2=view.findViewById(R.id.lin_lay2);
        header_view=view.findViewById(R.id.header_view);
        rlay=new LinearLayout(context);
        rlay.setOrientation(LinearLayout.VERTICAL);
        card_count=view.findViewById(R.id.card_count);
        ki=0;
        if(ja.length()==1) {
            RelativeLayout.LayoutParams params_r = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            params_r.setMargins(0, 0, 0, 0);
            card_count.setLayoutParams(params_r);
        }
        try {
            JSONObject js=ja.getJSONObject(i);
            createTxtView(js);
            Log.v("printing_current_json", js.toString());
            /* Log.v("printing_current_json", ja.length()+" "+ja.toString());

             */
            //txt_sf.setText("helllow");
           /* Log.v("printing_current_jsonob", js.toString()+" ivalue "+i);
            createTxtView(js);*/




            if(x==0 && !(target.equalsIgnoreCase("null") || target.equalsIgnoreCase("0"))) {
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
                        share=context.getSharedPreferences("key",0);
                        try {
                            JSONArray jjj=new JSONArray(share.getString("keys", ""));
                            if (jjj.length()==0) {
                                edit = share.edit();
                                JSONObject jkey1=new JSONObject();
                                JSONArray jar=new JSONArray();
                                js.put("PK_ID", ViewActivity.key);
                                js.put("FK_ID", "");
                                jkey1.put(ViewActivity.header, js);
                                jar.put(jkey1);
                                Log.v("printing_shareing23",jar.toString());
                                edit.putString("keys", jar.toString());
                                edit.putString("pk", ViewActivity.key);
                                edit.commit();
                            }
                            else{
                                edit = share.edit();
                                JSONObject jkey1=new JSONObject();
                                js.put("PK_ID", ViewActivity.key);
                                js.put("FK_ID", share.getString("pk",""));
                                jkey1.put(ViewActivity.header, js);
                                jjj.put(jkey1);
                                Log.v("printing_shareing",jjj.toString());
                                edit.putString("keys", jjj.toString());
                                edit.putString("pk", ViewActivity.key);
                                edit.commit();
                            }
                        }catch (Exception e){}
                        Intent i = new Intent(context, ViewActivity.class);
                        i.putExtra("frmid", target);
                        i.putExtra("frmname", "Existing Farmer");
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
                String[] splitBysemi=splitbyComma[i].split(";");
                createDynamicView(splitBysemi[2],splitBysemi[3],splitBysemi[0],splitBysemi[1],js.getString(splitBysemi[0]));
            }
            if(!forHeader)
                header_view.setVisibility(View.GONE);
            lay_header.addView(rlay);
        }catch (Exception e){
            Log.v("printing_exception",e.getMessage()+"");
        }

    }
    public TextView createHeader(int x,String value){
        TextView txt=new TextView(context);
        txt.setText(value);
        if(x==0){
            txt.setId(9);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_bold);
            txt.setTypeface(typeface);
            txt.setTextSize(15f);
            txt.setTextColor(Color.BLACK);
        }
        else{
            txt.setId(99);
          /*  LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0,5,0,0);
            txt.setLayoutParams(params2);*/
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto);
            txt.setTypeface(typeface);
            txt.setTextSize(13f);
            txt.setTextColor(Color.BLACK);
        }
        return txt;
    }

    public void createDynamicView(String type,String pic,String colname,String name,String val){

        if(type.equalsIgnoreCase("Y")) {
            forHeader=true;
            if (type.equalsIgnoreCase("Y") && pic.equalsIgnoreCase("0")) {
                rlay.addView(createHeader(ki, val));
                ki = 9;
            } else if (type.equalsIgnoreCase("Y") && pic.equalsIgnoreCase("P")) {
                ImageView img = new ImageView(context);
                img.setImageResource(R.drawable.call_icon);
                img.setId(899);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
                params2.addRule(RelativeLayout.ALIGN_PARENT_END);
                params2.addRule(RelativeLayout.CENTER_VERTICAL);
                params2.setMargins(0, 0, 15, 0);
                img.setLayoutParams(params2);
                lay_header.addView(img);
                rlay.addView(createHeader(ki, val));
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(val) || val.length()<10){
                            Toast.makeText(context," Invalid phone number ",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+val));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(callIntent);
                        }
                    }
                });
            }
            else if (type.equalsIgnoreCase("Y") && !pic.equalsIgnoreCase("0")) {
                CircleImageView img = new CircleImageView(context);
                img.setImageResource(R.drawable.profile_list);
                img.setId(89);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
                params1.addRule(RelativeLayout.CENTER_VERTICAL);
                img.setLayoutParams(params1);
                lay_header.addView(img);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
                params2.addRule(RelativeLayout.RIGHT_OF, img.getId());
                params2.addRule(RelativeLayout.CENTER_VERTICAL);
                params2.setMargins(25, 0, 0, 0);
                rlay.setLayoutParams(params2);
            }
        }
        else{
            if(TextUtils.isEmpty(name))
                createDynamicview(0,val);
            else{
                createDynamicview(0,name);
                createDynamicview(1,val);
            }
        }


    }

    public void createDynamicview(int x,String val){
        TextView txt=new TextView(context);
        Log.v("txt_str_view",val);
        if(val.contains("date") && val.contains(",")){
            val=val.substring(val.indexOf(":")+2,val.indexOf(" "));
        }
        txt.setText(val);
        txt.setTextSize(15f);
        txt.setSingleLine(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(10,10,0,10);
        txt.setLayoutParams(layoutParams);
        if(x==0) {
            txt.setTextColor(Color.parseColor("#94959C"));
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto);
            txt.setTypeface(typeface);
            lin_lay1.addView(txt);
        }
        else {
            txt.setTextColor(Color.parseColor("#000000"));
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
            txt.setTypeface(typeface);
            lin_lay2.addView(txt);
        }
    }
}


