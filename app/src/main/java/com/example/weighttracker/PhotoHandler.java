package com.example.weighttracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoHandler {

    public static File writePhoto (Activity a, Bitmap b, Entry entry) throws IOException{
        String storageState = Environment.getExternalStorageState();

        File file = null;
        if (storageState.equals( Environment.MEDIA_MOUNTED ) ) {
            //get the directory
            File dir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            //Make unique name for the pic
            String filename = ""+entry.getId();

            //create new file
            file = new File (dir + filename);

            long freeSpace = dir.getFreeSpace();
            int bytesNeeded = b.getByteCount();
            if (bytesNeeded*1.5 < freeSpace) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    boolean result = b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    if (result) {
                        return file;
                    } else {
                        throw new IOException("Problem compressing bitmap to output stream");
                    }
                } catch (Exception e) {
                    throw new IOException("Problem accessing file");
                }
            } else {
                throw new IOException("Not enough free space");
            }
        } else {
            throw new IOException("Cannot find storage");
        }
    }

    public static File writeProfilePic (Activity a, Bitmap b) throws IOException{
        String storageState = Environment.getExternalStorageState();

        File file = null;
        if (storageState.equals( Environment.MEDIA_MOUNTED ) ) {
            //get the directory
            File dir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            //Make unique name for the pic
            String filename = "ProfilePic";

            //create new file
            file = new File (dir + filename);

            long freeSpace = dir.getFreeSpace();
            int bytesNeeded = b.getByteCount();
            if (bytesNeeded*1.5 < freeSpace) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    boolean result = b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    if (result) {
                        return file;
                    } else {
                        throw new IOException("Problem compressing bitmap to output stream");
                    }
                } catch (Exception e) {
                    throw new IOException("Problem accessing file");
                }
            } else {
                throw new IOException("Not enough free space");
            }
        } else {
            throw new IOException("Cannot find storage");
        }
    }
}
