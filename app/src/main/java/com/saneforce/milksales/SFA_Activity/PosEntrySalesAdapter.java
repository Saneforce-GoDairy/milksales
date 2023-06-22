package com.saneforce.milksales.SFA_Activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal;

import java.util.ArrayList;
import java.util.List;

public class PosEntrySalesAdapter extends RecyclerView.Adapter<PosEntrySalesAdapter.MyViewHolder>  {
    View rootView;

    Context context;
    List<Product_Details_Modal> listt;

    ArrayList<String> amtArray = new ArrayList<String>();

    boolean isOnTextChanged = false;
    int final_total = 0;

    TextView tvTotalExpense;



    public PosEntrySalesAdapter(Context applicationContext, List<Product_Details_Modal> list) {
        this.context = applicationContext;
        listt = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        rootView = ((POS_SalesEntryActivity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        tvTotalExpense = (TextView) rootView.findViewById(R.id.totalExpense);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pos_countersales_listitem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.productname.setText(listt.get(position).getName());


        holder.rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //using this boolean because sometime when user enter value in edittxt
                //afterTextchanged runs twice to prevent this, i m making use of this variable.
                isOnTextChanged = true;
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //so this will trigger each time user enter value in editText box

                String s=editable.toString();
                listt.get(position).setentryValue(s);
                final_total = 0;
                if (isOnTextChanged) {
                    isOnTextChanged = false;
                    try {
                        final_total = 0;
                        for (int i = 0; i <= position; i++) {
                            int inposition1 = position;
                            if (i != position) {
                                //store 0  where user select position in not equal/
                                amtArray.add("0");
                            }else {
                                // store user entered value to Array list (ExpAmtArray) at particular position
                                amtArray.add("0");
                                amtArray.set(inposition1,editable.toString());
                                break;
                            }
                        }
                        // for statement to loop to the array, to calculate the Expense total.
                        for (int i = 0; i <= amtArray.size() - 1; i++) {
                            int tempTotalExpenase = Integer.parseInt(amtArray.get(i));
                            final_total  = final_total + tempTotalExpenase;
                        }
                        tvTotalExpense.setText(String.valueOf(final_total));
                    }catch (NumberFormatException e)
                    {
                        // catch is used because, when used enter value in editText and remove the value it
                        // it will trigger NumberFormatException, so to prevent it and remove data value from array ExpAmtArray
                        //then
                        // re-perform loop total expense calculation and display the total.
                        final_total = 0;
                        for (int i = 0; i <= position; i++) {
                            Log.d("TimesRemoved", " : " + i);
                            int newposition = position;
                            if (i == newposition) {
                                amtArray.set(newposition,"0");
                            }
                        }
                        for (int i = 0; i <= amtArray.size() - 1; i++) {
                            int tempTotalExpenase = Integer.parseInt(amtArray.get(i));
                            final_total  = final_total + tempTotalExpenase;
                        }
                        tvTotalExpense.setText(String.valueOf(final_total));
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return listt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productname, rate;


        public MyViewHolder(View view) {
            super(view);
            productname = view.findViewById(R.id.sales_productName);
            rate = view.findViewById(R.id.edtPos_salesPrice);

        }
    }
}
