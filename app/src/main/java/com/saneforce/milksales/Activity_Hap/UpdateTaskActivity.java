package com.saneforce.milksales.Activity_Hap;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Model_Class.EventCapture;
import com.saneforce.milksales.R;

public class UpdateTaskActivity extends Activity {

    private EditText editTextDesc, editTextFinishBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_task);
        editTextDesc = findViewById(R.id.editTextDesc);

        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        final EventCapture task = (EventCapture) getIntent().getSerializableExtra("task");


        loadTask(task);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask(task);
            }
        });


        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialogBox.showDialog(UpdateTaskActivity.this, "", "Are you surely want to delete?", "Yes", "No", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        deleteTask(task);
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                    }
                });
            }
        });

    }

    private void loadTask(EventCapture task) {
        editTextDesc.setText(task.getDesc());
        editTextFinishBy.setText(task.getFinishBy());



    }

    private void updateTask(final EventCapture task) {
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();


        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final EventCapture task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}
