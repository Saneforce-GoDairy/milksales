package com.saneforce.milksales.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saneforce.milksales.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewerActivity extends AppCompatActivity {

    String pdfurl = "", pdfFile = "";
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pdf_viewer);
            pdfView = (PDFView) findViewById(R.id.pdfView);
            pdfurl = String.valueOf(getIntent().getSerializableExtra("PDF_ONE"));
            pdfFile = String.valueOf(getIntent().getSerializableExtra("PDF_FILE"));
            Log.v("KARTHIC_URl", pdfurl);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File outputPath;
                    String sUrl = pdfurl.replace("file://", "");
                    if (pdfFile.equalsIgnoreCase("local")) {

                        outputPath = new File(sUrl);
                    }
//
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("application/pdf");
//                    shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + outputPath));
//                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(Intent.createChooser(shareIntent, "Share it"));


                    else {
                        File url = new File(sUrl);
                        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/happdf/" + url.getName();
                        outputPath = new File(filePath);
                    }
                    Uri fileUri = FileProvider.getUriForFile(PdfViewerActivity.this, PdfViewerActivity.this.getApplicationContext().getPackageName() + ".provider", outputPath);


                    Intent intent = ShareCompat.IntentBuilder.from(PdfViewerActivity.this)
                            .setType("*/*")
                            .setStream(fileUri)
                            .setChooserTitle("Choose bar")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(intent);

                }
            });

            if (pdfFile.equalsIgnoreCase("local")) {
                pdfView.fromFile(new File(pdfurl)).load();
            } else {
               // pdfurl=pdfurl.replaceAll("https:","http:");
                new RetrivePDFfromUrl().execute(pdfurl);
            }
        } catch (Exception e) {
            Log.v("PdfViewerActivity:", e.getMessage());
        }
    }

    /*  private Boolean downloadAndSaveFile(String server, int portNumber,
                                          String user, String password, String filename, File localFile)
              throws IOException {
          FTPClient ftp = null;

          try {
              ftp = new FTPClient();
              ftp.connect(server, portNumber);
              //Log.d(LOG_TAG, "Connected. Reply: " + ftp.getReplyString());

              ftp.login(user, password);
              //Log.d(LOG_TAG, "Logged in");
              ftp.setFileType(FTP.BINARY_FILE_TYPE);
              //Log.d(LOG_TAG, "Downloading");
              ftp.enterLocalPassiveMode();

              OutputStream outputStream = null;
              boolean success = false;
              try {
                  outputStream = new BufferedOutputStream(new FileOutputStream(
                          localFile));
                  success = ftp.retrieveFile(filename, outputStream);
              } finally {
                  if (outputStream != null) {
                      outputStream.close();
                  }
              }

              return success;
          } finally {
              if (ftp != null) {
                  ftp.logout();
                  ftp.disconnect();
              }
          }
      }
  */
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        ProgressDialog dialog;

        public RetrivePDFfromUrl() {
            dialog = new ProgressDialog(PdfViewerActivity.this);
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

                try {
                    if (pdfFile.equalsIgnoreCase("web")) {
                        String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
                        File outputPath = new File(fileUrl);
                        String fileName = outputPath.getName();  // -> maven.pdf
                        String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/happdf/";
                        File folder = new File(directory_path);

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        File pdfFile = new File(folder, fileName);

                        try {
                            pdfFile.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FileDownloader.downloadFile(fileUrl, pdfFile);
                    }
                } catch (Exception e) {
                    Log.v("download:", e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            if (dialog.isShowing()) {
                pdfView.fromStream(inputStream).load();

                dialog.dismiss();
                Toast.makeText(PdfViewerActivity.this, "Downloaded Successfully...", Toast.LENGTH_LONG).show();
                /*View view = findViewById(R.id.fab);
                Snackbar.make(pdfView, "Downloaded Successfully...", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.BLACK)
                        .setTextColor(Color.WHITE)
                        .setAction("Action", null).show();*/
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("please wait.");
            dialog.show();
        }
    }
}