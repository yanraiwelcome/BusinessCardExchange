package com.project.businesscardexchange.yan.businesscardexchange.ui;

import android.content.Context;
import android.util.Log;

import com.project.businesscardexchange.yan.businesscardexchange.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by CrazyCoder on 6/9/2016.
 */
public class WifiFilehelper {
    private static final int BUFFER = 80000;
    public void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public void unzip(String _zipFile, String _targetLocation) {

        //create target location folder if not exist
        dirChecker(_targetLocation);

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_targetLocation + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

*/

    public static void writeToFile( Context context,final String fileContents, File filePath) {
        try {
            FileWriter out = new FileWriter(filePath);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
           // Logger.logError(TAG, e);
        }
    }
    public static String readFromFile(String txt_name,Context context) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream (new File(txt_name)); //context.openFileInput(txt_name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
/*
    public static BusinessCardRealm readObject(String txt_name,Context context)
    {
        BusinessCardRealm address;

        try{

            FileInputStream fin = new FileInputStream(txt_name);
            ObjectInputStream ois = new ObjectInputStream(fin);
            address = (BusinessCardRealm) ois.readObject();
            ois.close();

            return address;

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void writeObject(Context context,BusinessCardRealm myObj, File filePath)
    {
        try{

        FileOutputStream fout = new FileOutputStream(filePath.getPath());
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(myObj);
        oos.close();
        System.out.println("Done");
            Log.e("Write","Ok");

    }catch(Exception ex){
        ex.printStackTrace();
            Log.e("Write","Error:"+ex.getLocalizedMessage());
    }
    }

    */

    static  public void copyFile(String inputFilePath, String outputPath,String outputFilePath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFilePath);
            out = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }


    }
    static public File getOutputMediaFile(int type,String timeStamp,String myPhototype)
    {
        // External sdcard location
        File mediaStorageDir = MyApplication.getCardLocation();

        //OR
  /*
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsolutePath()+File.separator+Config.IMAGE_DIRECTORY_NAME);
*/
//

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d("test", "Oops! Failed create "  + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;

        if(type == 7)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + File.separator + "DATA" + ".txt");
        }
        else {
            if (myPhototype.equals("image_own")) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + timeStamp + File.separator + "PHOTO" + ".jpg");
            } else {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + timeStamp + File.separator + "LOGO" + ".jpg");
            }
        }

        return mediaFile;
    }

}
