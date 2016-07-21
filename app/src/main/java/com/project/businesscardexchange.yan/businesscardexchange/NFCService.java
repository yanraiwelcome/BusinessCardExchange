package com.project.businesscardexchange.yan.businesscardexchange;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCard;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

public class NFCService{


    private Context mContext;
    private int mPosition;
    private BusinessCard mBCard;
    private Gson gson = new Gson();

    public NFCService(Context mContext, int mPosition, BusinessCard mBCard){

        this.mContext = mContext;
        this.mPosition  = mPosition;
        this.mBCard =mBCard;

    }


   public   NdefMessage createMessage(){

        String mimeType = "application/com.project.businesscardexchange.yan.businesscardexchange";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

        //GENERATE PAYLOAD


//        String toJson = gson.toJson(new BusinessCard(prefs.getString(COMPANY_NAME, "NA"), prefs.getString(EMAIL_ADDRESS, "NA"),
//                prefs.getString(NAME, "NA"), prefs.getString(PHONE, "NA"), prefs.getString(WEBSITE_URL, "NA"),
//                prefs.getString(PHONE_DIRECT_LINE,"NA"),prefs.getString(STREET,"NA"),prefs.getString(PHOTO,"NA"), prefs.getString(POST,"NA"),
//                prefs.getString(CITY,"NA"), prefs.getString(STATE,"NA"),
//                prefs.getString(ZIPCODE,"NA")));
//
       //GENERATE PAYLOAD

       /* String toJson = gson.toJson(new BusinessCard(mBCard.getCompanyName(),mBCard.getEmailAddress(),mBCard.getName(),mBCard.getPhone(),
                mBCard.getWebsiteUrl(),mBCard.getDirectPhone(),mBCard.getStreet(),mBCard.getPhoto(),
                mBCard.getPost(),mBCard.getCity(),mBCard.getState(),mBCard.getZipCode(),mBCard.getPhotocompanylogo()));
        */
       String toJson = gson.toJson(mBCard);
       byte[] payLoad = toJson.getBytes();

       String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+ File.separator+MyApplication.ZIP_DIRECTORY_NAME;
       // Log.d(tag,"inputPath:"+inputPath);
       File file = new File(inputPath+mBCard.getTimestamp()+".zip");
       //  Log.d(tag,"filePath:"+file.getAbsolutePath());

       if (file.exists())
       {
          // String zipName = mBCard.getTimestamp();
          // byte[] payLoad =  Uri.fromFile(new File(zipName)).get;
           FileInputStream fileInputStream=null;
          // File fileZip = new File(zipName);
          // byte[] bFile = new byte[(int) fileZip.length()];
           byte[] bFile = new byte[(int) file.length()];
           try{
               //convert file into array of bytes
               fileInputStream = new FileInputStream(file);
               fileInputStream.read(bFile);
               fileInputStream.close();

           }catch(Exception e){
               e.printStackTrace();
           }
           byte[] payLoad2 =  bFile;
           //GENERATE NFC MESSAGE
           return new NdefMessage(
                   new NdefRecord[]{
                           //In first record we are sending json
                           new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                   mimeBytes,
                                   null,
                                   payLoad),
                           //In below second record we are sending zip file
                           new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                   mimeBytes,
                                   null,
                                   payLoad2),
                           NdefRecord.createApplicationRecord("com.project.businesscardexchange.yan.businesscardexchange")
                   });

       }
       else
       {
           Toast.makeText(mContext, "Can not share :"+mBCard.getName(), Toast.LENGTH_SHORT).show();
           byte[] payLoad2 = null;
           //GENERATE NFC MESSAGE
           return new NdefMessage(
                   new NdefRecord[]{
                           new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                   mimeBytes,
                                   null,
                                   payLoad),
                           new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                   mimeBytes,
                                   null,
                                   payLoad2),
                           NdefRecord.createApplicationRecord("com.project.businesscardexchange.yan.businesscardexchange")
                   });
       }


    }




}


