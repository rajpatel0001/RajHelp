package com.raj.myutils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyUtils {
    public static String TAG = "STORAGE_TAG";
    public static boolean issave = false;
    private static int BUFFER_SIZE = 6 * 1024;
    public static long totalapksize = 0;
    public static long totalImgsize = 0;
    public static long totalVideosize = 0;
    public static long totalAudiosize = 0;
    public static long totalDocssize = 0;

    public static int totalImg,totalVideo,totalAudio,totalApks,totalDocs;

    public static String create_folder(String foldername) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + foldername);
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        } else {
            File folder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC + File.separator + "/" + foldername)));
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        }

    }

    public static String create_hidden_folder(String foldername) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/." + foldername);
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        } else {
            File folder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + "/." + foldername)));
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        }
    }

    public static String create_folder_with_sub_folder(String foldername, String subfolder) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + foldername + "/" + subfolder);
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        } else {
            File folder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + foldername + File.separator + subfolder)));
            Log.d(TAG, "createDir:" + folder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder.getAbsolutePath();
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);

        } catch (IOException e) {
            // handle exception
        }
        return bitmap;

    }

    public static String saveimagepng(Context context, Bitmap bitmap, String path, String name) {

        try {
            File file2 = new File(path);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2, name);
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast(context, "Saved Successfully " + file3);
            String absolutePath = file3.getAbsolutePath();
            MediaScannerConnection.scanFile(context, new String[]{file3.getPath()}, null, null);
            return absolutePath;

        } catch (Exception unused) {
            Toast(context, "Failed to Save");
            return null;
        }
    }

    public static String saveimagejpg(Context context, Bitmap bitmap, String path, String name) {
        try {
            File file2 = new File(path);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2, name);
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (issave) {
                Toast(context, "Saved Successfully");

            }
            String absolutePath = file3.getAbsolutePath();
            MediaScannerConnection.scanFile(context, new String[]{file3.getPath()}, null, null);
            return absolutePath;

        } catch (Exception unused) {
            Toast(context, "Failed to Save");
            return null;
        }
    }

    public static void downloadvideo(Context context, String url, String savepath, String filename) {
        DownloadFileFromURL downloadTask = new DownloadFileFromURL(context, savepath, filename);
        downloadTask.execute(url);
    }

    public static void Toast(Context context, String str) {
        Toast(context, str, 0);
    }

    public static void Toast(Context context, String str, int i) {
        Toast.makeText(context, str, i).show();
    }

    public static ArrayList<File> getfolderdata(String folderpath) {
        ArrayList<File> datapath = new ArrayList<>();
        File checkfolderpath = new File(folderpath);
        if (checkfolderpath.exists()) {
            File listoffiles = new File(folderpath);
            Log.d(TAG, "onResume: " + listoffiles);

            listoffiles.lastModified();
            File[] songlist = listoffiles.listFiles();
            ///sort by date modified
//            if (songlist != null && songlist.length > 1) {
//                Collections.sort(Arrays.asList(songlist), new Comparator<File>() {
//                    public int compare(File o1, File o2) {
//                        long lastModifiedO1 = o1.lastModified();
//                        long lastModifiedO2 = o2.lastModified();
//                        return (lastModifiedO2 < lastModifiedO1) ? -1 : ((lastModifiedO1 > lastModifiedO2) ? 1 : 0);
//                    }
//                });
//            }

            if (songlist != null && songlist.length > 1) {
                Arrays.sort(songlist, new Comparator<File>() {
                    @Override
                    public int compare(File object1, File object2) {
                        return object1.getName().compareTo(object2.getName());
                    }
                });
            }
            if (songlist != null) {
                if (songlist.length != 0) {

                    for (int i = 0; i < songlist.length; i++) {
                        datapath.add(songlist[i]);
                    }
                    return datapath;
                }
            }
        }
        return null;
    }

    public static Bitmap getBitmapFromFile(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return bitmap;
    }

    public static boolean deletefile(Context context, String pathofdelete) {
        File deleteimage = new File(pathofdelete);
        if (deleteimage.exists()) {
            if (deleteimage.delete()) {
                MediaScannerConnection.scanFile(context, new String[]{deleteimage.getPath()}, null, null);
                return true;
            }
            return false;
        }
        return false;
    }


    private static class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;
        String pathFile = "";

        Context context;
        String pathFolder = "";
        String nameoffile = "";


        public DownloadFileFromURL(Context context, String pathFolder, String nameoffile) {
            this.context = context;
            this.pathFolder = pathFolder;
            this.nameoffile = nameoffile;
        }


        @Override
        protected String doInBackground(String... sUrl) {


            pathFile = pathFolder + "/" + nameoffile;

            MediaScannerConnection.scanFile(context, new String[]{pathFile}, null, null);
            File futureStudioIconFile = new File(pathFolder);
            if (!futureStudioIconFile.exists()) {
                futureStudioIconFile.mkdirs();
            }


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d(TAG, "doInBackground: connectioncode " + connection.getResponseCode());
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Please Enter Valid Name";
                }

                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();

                Log.e(TAG, "doInBackground: " + pathFile);
                output = new FileOutputStream(pathFile);

                //  output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + name + ".mp3");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {

                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return pathFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait...");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pd.setProgress(Integer.parseInt(String.valueOf(progress[0])));
        }

        @Override
        protected void onPostExecute(String result) {

            if (pd != null) {
                pd.dismiss();
                if (result != null) {
                    MediaScannerConnection.scanFile(context, new String[]{result}, null, null);
                }
                Toast.makeText(context, "Downloaded" + result, Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static ArrayList<String> getasset_folder_data(Context mContext, String folder) {
        ArrayList<String> list = new ArrayList<>();
        list.clear();
        try {
            String[] arr = mContext.getAssets().list(folder);
            for (String str : arr) {
                list.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void share_app(Context context) {

        String shareBody = "https://play.google.com/store/apps/details?id="
                + context.getPackageName();
        Intent sharingIntent = new Intent(
                Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    public static void rate_app(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri
                    .parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + context.getPackageName())));
        }
    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = null;

        try {
            if (contactId != null) {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
//              assert inputStream != null;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    public static void OpenApp(Context context2, String str) {
        Intent launchIntentForPackage = context2.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage != null) {
            context2.startActivity(launchIntentForPackage);
        } else {
            Toast(context2, "App Not Available");
        }
    }

    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
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

//    public static ArrayList<Record> loadData(Context context) {
//        ArrayList<Record> images = new ArrayList<>();
//        Gson gson = new Gson();
//        SharedPreferences sharedpreferences = context.getSharedPreferences("MyRecord", Context.MODE_PRIVATE);
//        String json = sharedpreferences.getString("recordlist", null);
//        Type type = new TypeToken<ArrayList<Record>>() {
//        }.getType();
//        images = gson.fromJson(json, type);
//        return images;
//
//    }
//
//    public static void saveData(Context context,ArrayList<Record> images) {
//        Gson gson = new Gson();
//        String json = gson.toJson(images);
//        SharedPreferences sharedpreferences = context.getSharedPreferences("MyRecord", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("recordlist", json);
//        editor.apply();
//    }

    public static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        return formateMilliSeccond(Long.parseLong(durationStr));
    }

    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    public static void zip(String files, String zipFile, Context context) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

//            for (int i = 0; i < files.size(); i++) {
            FileInputStream fi = new FileInputStream(files);
            origin = new BufferedInputStream(fi, BUFFER_SIZE);
            try {
                ZipEntry entry = new ZipEntry(files.substring(files.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
            } finally {
                origin.close();
            }
//            }

        } finally {
            out.close();
            MediaScannerConnection.scanFile(context, new String[]{zipFile}, null, null);

        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void statusCheck(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context);

        }
    }

    private static void buildAlertMessageNoGps(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void turnGPSOn(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public static void initLocation(Context context) {

        if (checkAndRequestPermissions(context)) {
//            Toast.makeText(getContext(),"Permission Granded",Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkAndRequestPermissions(Context context) {
        final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        return false;
    }

    public static String create_folder_in_app_package_dir(Context context, String subfolder) {
        File directory = context.getFilesDir();
        File file = new File(directory, subfolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();

    }

    public static Bitmap uriToBitmap(Uri selectedFileUri, Context context) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void Resize(Bitmap bit, View v) {

        int layoutwidth = v.getWidth();
        int layoutheight = v.getHeight();
        int imagewidth = bit.getWidth();
        int imageheight = bit.getHeight();
        int newwidth = 0;
        int newheight = 0;
        if (imagewidth >= imageheight) {
            newwidth = layoutwidth;
            newheight = (newwidth * imageheight) / imagewidth;
            if (newheight > layoutheight) {
                newwidth = (layoutheight * newwidth) / newheight;
                newheight = layoutheight;
            }
        } else {
            newheight = layoutheight;
            newwidth = (newheight * imagewidth) / imageheight;
            if (newwidth > layoutwidth) {
                newheight = (newheight * layoutwidth) / newwidth;
                newwidth = layoutwidth;
            }
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newwidth, newheight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        v.setLayoutParams(layoutParams);
    }

    public static Long getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        Long absolutePathOfImage = 0L;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            if (cursor.getLong(column_index_data) != 0)
                absolutePathOfImage = absolutePathOfImage + cursor.getLong(column_index_data);
        }
        return absolutePathOfImage;
    }

    public static Long getAllShownVideo(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        Long absolutePathOfImage = 0L;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            if (cursor.getLong(column_index_data) != 0)
                absolutePathOfImage = absolutePathOfImage + cursor.getLong(column_index_data);
        }
        return absolutePathOfImage;
    }

    public static Long getAllShownAudio(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        Long absolutePathOfImage = 0L;

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            if (cursor.getLong(column_index_data) != 0)
                absolutePathOfImage = absolutePathOfImage + cursor.getLong(column_index_data);
        }
        return absolutePathOfImage;
    }


    public static void getaudiolist(Context context) {
        String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri storage = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
        };
        String selectionMimeType = MediaStore.Files.FileColumns.DATA + " like ? AND " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ?  ";
        String[] selectionArgsPdf = new String[]{INTERNAL_STORAGE_PATH + "%", "%" + ".mp3%", "%" + ".wav"};
        Cursor c = context.getContentResolver().query(storage, projection, selectionMimeType, selectionArgsPdf, null);
        if (c != null && c.moveToFirst()) {
            do {
                long size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                totalAudiosize = totalAudiosize + size;
                totalAudio=totalAudio+1;
            } while (c.moveToNext());
        } else {
        }
    }

    public static void getimglist(Context context) {
        String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri storage = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
        };
        String selectionMimeType = MediaStore.Files.FileColumns.DATA + " like ? AND " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ?  ";
        String[] selectionArgsPdf = new String[]{INTERNAL_STORAGE_PATH + "%", "%" + ".jpg%", "%" + ".jpeg", "%" + ".webp", "%" + ".png"};
        Cursor c = context.getContentResolver().query(storage, projection, selectionMimeType, selectionArgsPdf, null);
        if (c != null && c.moveToFirst()) {
            do {
                long size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                totalImgsize = totalImgsize + size;
                totalImg = totalImg+1;
            } while (c.moveToNext());
        } else {
        }
    }

    public static void getvideolist(Context context) {
        String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri storage = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
        };
        String selectionMimeType = MediaStore.Files.FileColumns.DATA + " like ? AND " +
                MediaStore.Files.FileColumns.DATA + " like ? ";
        String[] selectionArgsPdf = new String[]{INTERNAL_STORAGE_PATH + "%", "%" + ".mp4%"};
        Cursor c = context.getContentResolver().query(storage, projection, selectionMimeType, selectionArgsPdf, null);
        if (c != null && c.moveToFirst()) {
            do {
                long size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                totalVideosize = totalVideosize + size;
                totalVideo=totalVideo+1;
            } while (c.moveToNext());
        } else {
        }
    }

    public static void getapklist(Context context) {
        String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri storage = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
        };
        String selectionMimeType = MediaStore.Files.FileColumns.DATA + " like ? AND " +
                MediaStore.Files.FileColumns.DATA + " like ? ";
        String[] selectionArgsPdf = new String[]{INTERNAL_STORAGE_PATH + "%", "%" + ".apk%"};
        Cursor c = context.getContentResolver().query(storage, projection, selectionMimeType, selectionArgsPdf, null);
        if (c != null && c.moveToFirst()) {
            do {
                long size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                totalapksize = totalapksize + size;
                totalApks = totalApks+1;
            } while (c.moveToNext());
        } else {
        }
    }

    public static void getDocumentlist(Context context) {
        String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri storage = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
        };
        String selectionMimeType = MediaStore.Files.FileColumns.DATA + " like ? AND " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? OR " +
                MediaStore.Files.FileColumns.DATA + " like ? ";
        String[] selectionArgsPdf = new String[]{INTERNAL_STORAGE_PATH + "%", "%" + ".pdf%", "%" + ".doc", "%" + ".ppt", "%" + ".xls", "%" + ".txt", "%" + ".dox", "%" + ".xlsx"};
        Cursor c = context.getContentResolver().query(storage, projection, selectionMimeType, selectionArgsPdf, null);
        if (c != null && c.moveToFirst()) {
            do {
                long size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                totalDocssize = totalDocssize + size;
                totalDocs=totalDocs+1;
            } while (c.moveToNext());
        } else {
        }
    }
    public static long getFileSize(final File file) {

        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

    public static void copyFile(String inputPath, String outputPath) {
        File sourceLocation = new File(inputPath);
        File targetLocation = new File(outputPath);
        try {
            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;
            // moving the file to another directory
            if(actionChoice ==1){
                if(sourceLocation.renameTo(targetLocation)){
                    Log.v(TAG, "Move file successful.");
                }else{
                    Log.v(TAG, "Move file failed.");
                }
            }
            // we will copy the file
            else{
                // make sure the target file exists
                if(sourceLocation.exists()){

                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();

                    Log.v(TAG, "Copy file successful.");

                }else{
                    Log.v(TAG, "Copy file failed. Source file missing.");
                }

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "copyFile: err "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "copyFile: er "+e.getMessage());
        }

    }

    public static void moveFile(String inputPath , String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.getParentFile().mkdirs();
            }
            in = new FileInputStream(inputPath );
            out = new FileOutputStream(outputPath );

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file
            out.flush();
            out.close();
            out = null;
            // delete the original file
            new File(inputPath ).delete();
        }

        catch (FileNotFoundException fnfe1) {
            Log.d(TAG, "moveFile: errr " +fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.d(TAG, "moveFile: errr1  " +e.getMessage());
        }

    }
}