package com.guo.duoduo.p2pmanager.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by CrazyCoder on 6/9/2016.
 */
public class WifiFilehelper {
    private static final int BUFFER = 8000000;

    public static void unzip(String _zipFile, String _targetLocation) {

        if(_targetLocation.contains(".zip")) {
            _targetLocation = _targetLocation.substring(0, _targetLocation.lastIndexOf('.'));
            _targetLocation=_targetLocation+File.separator;//"_other"+File.separator;

        }
        //create target location folder if not exist
        dirChecker(_targetLocation);
        Log.e("unzip","_targetLocation:"+_targetLocation);

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                    Log.e("unzip","dirChecker:"+ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_targetLocation + ze.getName());

                    Log.e("unzip","FileOutputStream:"+_targetLocation + ze.getName());

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
    private static void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

/*
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
    public static void writeToFile( Context context,final String fileContents, File filePath) {
        try {
            FileWriter out = new FileWriter(filePath);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
           // Logger.logError(TAG, e);
        }
    }
    public String readFromFile(String txt_name,Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(txt_name);

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
    */
    }
