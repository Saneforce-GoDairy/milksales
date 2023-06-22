package com.saneforce.milksales.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saneforce.milksales.Activity.Util.ModelDynamicView;
import com.saneforce.milksales.Activity.Util.SelectionModel;
import com.saneforce.milksales.Activity.Util.UpdateUi;
import com.saneforce.milksales.Activity.ViewActivity;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterForDynamicView extends BaseAdapter {
    Context context;
    ArrayList<ModelDynamicView> array = new ArrayList<>();
    RelativeLayout dyn_lay, r_lay_sec, r_lay_fist;
    static UpdateUi updateUi;
    Button btn_upload;
    RelativeLayout rlay_list;
    ListView dyn_list;
    LinearLayout dyn_l_lay;
    SharedPreferences share;
    private String strFarmerType = "";

    public AdapterForDynamicView(Context context, ArrayList<ModelDynamicView> array) {
        this.context = context;
        this.array = array;
        share = this.context.getSharedPreferences("key", 0);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.dummy_layout1, viewGroup, false);
        TextView txt_label = (TextView) view.findViewById(R.id.txt_label);
        RelativeLayout rlay_spin = (RelativeLayout) view.findViewById(R.id.rlay_spin);
        RelativeLayout rlay_date = (RelativeLayout) view.findViewById(R.id.rlay_date);
        RelativeLayout rlay_currency = (RelativeLayout) view.findViewById(R.id.rlay_currency);
        RelativeLayout rlay_spin_fdt = (RelativeLayout) view.findViewById(R.id.rlay_spin_fdt);
        RelativeLayout rlay_spin_tdt = (RelativeLayout) view.findViewById(R.id.rlay_spin_tdt);
        rlay_list = (RelativeLayout) view.findViewById(R.id.rlay_list);
        dyn_lay = (RelativeLayout) view.findViewById(R.id.dyn_lay);
        dyn_l_lay = (LinearLayout) view.findViewById(R.id.dyn_l_lay);
        dyn_list = view.findViewById(R.id.dyn_list);
        r_lay_sec = view.findViewById(R.id.r_lay_sec);
        r_lay_fist = view.findViewById(R.id.r_lay_fist);
        //RelativeLayout rlay_head=(RelativeLayout) view.findViewById(R.id.rlay_head);
        RelativeLayout rlay_upload = (RelativeLayout) view.findViewById(R.id.rlay_upload);
        TextView spin_txt = (TextView) view.findViewById(R.id.spin_txt);
        TextView spin_txt_fdt = (TextView) view.findViewById(R.id.spin_txt_fdt);
        TextView spin_txt_tdt = (TextView) view.findViewById(R.id.spin_txt_tdt);
        TextView txt_currency = (TextView) view.findViewById(R.id.txt_currency);
        TextView txt_upload = (TextView) view.findViewById(R.id.txt_upload);
        EditText edt_field = (EditText) view.findViewById(R.id.edt_field);
        EditText edt_field_numeric = (EditText) view.findViewById(R.id.edt_field_numeric);
        EditText edt_feed = (EditText) view.findViewById(R.id.edt_feed);
        EditText edt_field_currency = (EditText) view.findViewById(R.id.edt_field_currency);
        btn_upload = (Button) view.findViewById(R.id.btn_upload);

        /*LinearLayout.LayoutParams params_rr = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT,(int) LinearLayout.LayoutParams.WRAP_CONTENT);
        params_rr.setMargins(0,7,0,0);
        r_lay_sec.setLayoutParams(params_rr);*/

        final ModelDynamicView mm = array.get(i);
        rlay_spin.setVisibility(View.GONE);
        edt_field.setVisibility(View.GONE);
        edt_field_numeric.setVisibility(View.GONE);
        edt_feed.setVisibility(View.GONE);
        rlay_date.setVisibility(View.GONE);
        rlay_currency.setVisibility(View.GONE);
        rlay_upload.setVisibility(View.GONE);
        dyn_lay.setVisibility(View.GONE);
        txt_upload.setVisibility(View.GONE);
        rlay_list.setVisibility(View.GONE);
        dyn_list.setVisibility(View.GONE);
        dyn_l_lay.setVisibility(View.GONE);
        txt_label.setVisibility(View.VISIBLE);
//        rlay_head.setVisibility(View.GONE);

        txt_label.setText(mm.getFieldname());
        if (mm.getMandatory().equalsIgnoreCase("0"))
            txt_label.setText(mm.getFieldname() + "*");
        else if (!TextUtils.isEmpty(mm.getFieldname()))
            txt_label.setText(mm.getFieldname());
        else
            txt_label.setVisibility(View.GONE);

        Log.v("getting_view_id", mm.getViewid() + "");
        if (mm.getViewid().equalsIgnoreCase("1")) {
            edt_field.setVisibility(View.VISIBLE);
            if (!mm.getCtrl_para().equalsIgnoreCase("0")) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_field.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_field.setText(mm.getValue());

        } else if (mm.getViewid().equalsIgnoreCase("2")) {
            edt_feed.setVisibility(View.VISIBLE);
            if (!mm.getCtrl_para().equalsIgnoreCase("0")) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_feed.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_feed.setText(mm.getValue());

        } else if (mm.getViewid().equalsIgnoreCase("3") || mm.getViewid().equalsIgnoreCase("18") || mm.getViewid().equalsIgnoreCase("24")) {
            edt_field_numeric.setVisibility(View.VISIBLE);
            if (mm.getViewid().equalsIgnoreCase("24")) {
                edt_field_numeric.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else {
                edt_field_numeric.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                edt_field_numeric.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
            }
            if (!mm.getCtrl_para().equalsIgnoreCase("0")) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_field_numeric.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_field_numeric.setText(mm.getValue());

        } else if (mm.getViewid().equalsIgnoreCase("4") || mm.getViewid().equalsIgnoreCase("5")
                || mm.getViewid().equalsIgnoreCase("6") || mm.getViewid().equalsIgnoreCase("7")
                || mm.getViewid().equalsIgnoreCase("8") || mm.getViewid().equalsIgnoreCase("11")) {
            rlay_spin.setVisibility(View.VISIBLE);
            Log.v("inside_spinner", i + " value " + mm.getValue());
            if (!TextUtils.isEmpty(mm.getValue()))
                spin_txt.setText(mm.getValue());

            if (mm.getViewid().equalsIgnoreCase("4"))
                strFarmerType = mm.getValue();

        } else if (mm.getViewid().equalsIgnoreCase("15") || mm.getViewid().equalsIgnoreCase("16") || mm.getViewid().equalsIgnoreCase("17")) {
            rlay_upload.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getValue())) {
                txt_upload.setVisibility(View.VISIBLE);
                String[] ar = mm.getValue().split(",");
                Log.v("printing_storage_val", mm.getValue() + " length " + ar.length);
                int arrs = ar.length;
                txt_upload.setText(String.valueOf(arrs));
            }
        } else if (mm.getViewid().equalsIgnoreCase("9") || mm.getViewid().equalsIgnoreCase("12")) {
            rlay_date.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getValue()))
                spin_txt_fdt.setText(mm.getValue());
            if (!TextUtils.isEmpty(mm.getTvalue()))
                spin_txt_tdt.setText(mm.getTvalue());
        } else if (mm.getViewid().equalsIgnoreCase("20")) {
            dyn_lay.setVisibility(View.VISIBLE);
            createDynamicView(mm.getA_list());
        } else if (mm.getViewid().equalsIgnoreCase("22")) {
            txt_label.setTextColor(Color.BLACK);
            r_lay_sec.setBackgroundResource(0);
        } else if (mm.getViewid().equalsIgnoreCase("13")) {
            dyn_lay.setVisibility(View.VISIBLE);
            createDynamicViewCheckbox(mm.getA_list());
        } else if (mm.getViewid().equalsIgnoreCase("23")) {
            txt_label.setTextColor(Color.BLACK);
            r_lay_sec.setBackgroundResource(0);
            dyn_l_lay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getValue())) {
                String[] splitbycomma = mm.getValue().split(",");
                for (int k = 0; k < splitbycomma.length; k++) {
                    String scode = splitbycomma[k].substring(0, splitbycomma[k].indexOf(";"));
                    String snam = splitbycomma[k].substring(splitbycomma[k].indexOf(";") + 1);
                    if (k == splitbycomma.length - 1)
                        createDynamicRowITem(snam, Integer.parseInt(scode), 1);
                    else
                        createDynamicRowITem(snam, Integer.parseInt(scode), 0);
                }
            }
        } else if (mm.getViewid().equalsIgnoreCase("19")) {
            r_lay_sec.setBackgroundResource(0);
            rlay_list.setVisibility(View.VISIBLE);
            dyn_list.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params_r = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            params_r.setMargins(0, 0, 0, 0);
            r_lay_sec.setLayoutParams(params_r);
            r_lay_fist.setLayoutParams(params_r);

           /* LinearLayout.LayoutParams params_r = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT,(int) LinearLayout.LayoutParams.WRAP_CONTENT);
            params_r.setMargins(0,0,0,0);
            r_lay_sec.setLayoutParams(params_r);*/

            try {
                JSONArray jj = new JSONArray(mm.getValue().toString());
                JSONArray filterArr = new JSONArray();
                if (jj.length() > 1) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.MATCH_PARENT, 1800);
                    dyn_list.setLayoutParams(params);

                    if (!Common_Class.isNullOrEmpty(strFarmerType)) {
                        for (int f = 0; f < jj.length(); f++) {
                            JSONObject obj = jj.getJSONObject(f);
                            if (strFarmerType.startsWith(obj.getString("Farmer_type"))) {
                                filterArr.put(obj);
                            }
                        }


                    }

                }
                Log.v("printing_json", jj.toString());
                FilterDemoAdapter adpt = new FilterDemoAdapter(context, strFarmerType.equalsIgnoreCase("") ? jj : filterArr, mm.getTvalue(), 0, mm.getSlno());
                // FilterAdapter ff = new FilterAdapter(context, jj, mm.getTvalue(), 0,"0");
                dyn_list.setAdapter(adpt);

            } catch (Exception e) {
            }
        }

        edt_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
            }
        });

        edt_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
            }
        });

        edt_field_numeric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mm.getViewid().equalsIgnoreCase("15"))
                    updateUi.update(15, i);
                else if (mm.getViewid().equalsIgnoreCase("16"))
                    updateUi.update(16, i);
                else
                    updateUi.update(17, i);
               /* else
                    popupCapture();*/
            }
        });

        rlay_spin_fdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mm.getViewid().equalsIgnoreCase("9"))
                    updateUi.update(8, i);
                else
                    updateUi.update(12, i);
            }
        });

        rlay_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mm.getViewid()) {
                    case "11":
                        updateUi.update(11, i);
                        break;
                    case "5":
                        updateUi.update(5, i);
                        break;
                    case "4":
                        updateUi.update(4, i);
                        break;
                    case "6":
                        updateUi.update(6, i);
                        break;
                    case "7":
                        updateUi.update(7, i);
                        break;
                    case "8":
                        updateUi.update(8, i);
                        break;
                }
//                if (mm.getViewid().equalsIgnoreCase("11"))
//                    updateUi.update(11, i);
            }
        });

        rlay_spin_tdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mm.getViewid().equalsIgnoreCase("9"))
                    updateUi.update(9, i);
                else
                    updateUi.update(13, i);
            }
        });

        return view;
    }

    public static void bindListernerForDateRange(UpdateUi pp) {
        updateUi = pp;
    }

    public void createDynamicView(final ArrayList<SelectionModel> arr) {
        RadioGroup rg;
        RadioButton rb1, rb2, rb3, rb4;
        rg = new RadioGroup(context);
        for (int i = 0; i < arr.size(); i++) {
            rb1 = new RadioButton(context);
            rb1.setId(i);

            if (arr.get(i).isClick())
                rb1.setChecked(true);

            rb1.setText(arr.get(i).getTxt());
            rg.addView(rb1);
        }

        rg.setOrientation(RadioGroup.VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;//150
        params.topMargin = 5;
        rg.setLayoutParams(params);
        dyn_lay.addView(rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.v("radio_button_id", checkedId + "and value" + arr.get(checkedId).getTxt());
                for (int i = 0; i < arr.size(); i++) {
                    if (checkedId == i)
                        arr.get(i).setClick(true);
                    else
                        arr.get(i).setClick(false);
                }

            }
        });
    }

    public void createDynamicRowITem(String name, int id, int x) {
        RelativeLayout ll = new RelativeLayout(context);
        ll.setId(id);
        if (x == 0)
            ll.setBackgroundResource(R.drawable.rect_light_black);
        else
            ll.setBackgroundResource(R.drawable.rect_light_black_btm);
        TextView txt = new TextView(context);
        txt.setText(name);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setTextSize(15f);
        txt.setPadding(28, 28, 28, 28);
        ll.addView(txt);
        ImageView img = new ImageView(context);
        img.setImageResource(R.drawable.fwd_icon);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(0, 0, 10, 0);
        img.setLayoutParams(params);

        ll.addView(img);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("printing_form_id", ll.getId() + " name ");
                share = context.getSharedPreferences("key", 0);
                SharedPreferences.Editor edit = share.edit();
                try {
                    JSONArray jjj = new JSONArray(share.getString("keys", ""));
                    if (jjj.length() == 0) {

                        JSONObject jkey1 = new JSONObject();
                        JSONArray jar = new JSONArray();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", "");
                        jkey1.put(ViewActivity.header, js);
                        jar.put(jkey1);
                        Log.v("printing_shareing23", jar.toString());
                        edit.putString("keys", jar.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    } else {
                        edit = share.edit();
                        JSONObject jkey1 = new JSONObject();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", share.getString("pk", ""));
                        jkey1.put(ViewActivity.header, js);
                        jjj.put(jkey1);
                        Log.v("printing_shareing", jjj.toString());
                        edit.putString("keys", jjj.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    }
                } catch (Exception e) {
                }
                Intent i = new Intent(context, ViewActivity.class);
                i.putExtra("frmid", String.valueOf(ll.getId()));
                if (String.valueOf(ll.getId()).equalsIgnoreCase("12"))
                    i.putExtra("frmname", "Competitor Activity");

                else if (String.valueOf(ll.getId()).equalsIgnoreCase("8"))
                    i.putExtra("frmname", "General Activities");

                i.putExtra("btn_need", "0");
                i.putExtra("fab", "1");
                context.startActivity(i);
            }
        });
        dyn_l_lay.addView(ll);
    }

    public void createDynamicViewCheckbox(final ArrayList<SelectionModel> arr) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 150;
        params.topMargin = 5;
        for (int i = 0; i < arr.size(); i++) {
            final CheckBox cb = new CheckBox(context);
            cb.setId(i);
            if (arr.get(i).isClick())
                cb.setChecked(true);
            cb.setText(arr.get(i).getTxt());
            cb.setLayoutParams(params);
            ll.addView(cb);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.v("printing_checked", cb.getId() + " id");
                    if (!b) {
                        cb.setChecked(false);
                        arr.get(cb.getId()).setClick(false);
                    } else {
                        cb.setChecked(true);
                        arr.get(cb.getId()).setClick(true);
                    }

                }
            });
        }
        dyn_lay.addView(ll);
    }
}
