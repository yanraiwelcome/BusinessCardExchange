package com.project.businesscardexchange;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.google.gson.Gson;
import com.project.businesscardexchange.models.BusinessCard;

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

        String mimeType = "application/com.project.businesscardexchange";
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

        //GENERATE NFC MESSAGE
        return new NdefMessage(
                new NdefRecord[]{
                        new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                mimeBytes,
                                null,
                                payLoad),
                        NdefRecord.createApplicationRecord("com.project.businesscardexchange")
                });
    }




}


