package com.saneforce.milksales.Activity.Util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Common_Class.Util;

import java.io.File;


public class ImageFilePath {
    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        Shared_Common_Pref shared_common_pref=new Shared_Common_Pref(context);

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


        Util util = new Util();
        String fileName ="EK" + shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + System.nanoTime() + "." + util.getFileExtension(context, uri);
        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "/" + fileName);
        util.createFile(context, uri, file);
        return file.getPath();


        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//
//            final String docId = DocumentsContract.getDocumentId(uri);
//            final String[] split = docId.split(":");
//            final String type = split[0];
//
//            Uri contentUri = null;
//            if ("image".equals(type)) {
//                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            } else if ("video".equals(type)) {
//                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//            } else if ("audio".equals(type)) {
//                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//            } else/* if ("document".equals(type))*/ {
//                Util util = new Util();
//                String fileName = Shared_Common_Pref.ImageUKey + "." + util.getFileExtension(context, uri);
//                final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "/" + fileName);
//                util.createFile(context, uri, file);
//                return file.getPath();
//
//            }
//            final String selection = "_id=?";
//            final String[] selectionArgs = new String[]{
//                    split[1]
//            };
//
//            return getDataColumn(context, contentUri, selection, selectionArgs);
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//
//            return getDataColumn(context, uri, null, null);
//        } else {
//            return uri.getPath();
//        }

    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
