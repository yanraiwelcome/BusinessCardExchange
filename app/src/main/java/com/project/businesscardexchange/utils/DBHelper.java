package com.project.businesscardexchange.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.businesscardexchange.models.BusinessCard;

import java.util.ArrayList;

/**
 * Created by sandeep on 12/24/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    //HashMap for index and its values
    ArrayList<BusinessCard> cardList;

    public static final String DATABASE_NAME = "bizcard.db";
    public static final String BIZCARD_TABLE_NAME = "mycards";

    private static final int DATABASE_VERSION = 2;

    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "create table "+BIZCARD_TABLE_NAME +
                        " (timestamp text ,"+// primary key," +
                        "isOwn integer default 1," +
                        "name text," +
                        "companyName text," +
                        "websiteUrl text," +
                        "phone text," +
                        "emailAddress text," +
                        "directPhone text," +
                        "photo text,post text,street text,city text,state text,zipCode text, photocompanylogo text, countryName text  )"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BIZCARD_TABLE_NAME);
        onCreate(db);
    }


    public ArrayList<BusinessCard> getAllCards() {
        try
        {
            //"SELECT * from data_field where type_id = type_id"
            //return ArrayList<HashMap<String,String>		data = new ArrayList<String>();
            SQLiteDatabase sqlite = this.getWritableDatabase();
            String   selectAll = "select * from "+BIZCARD_TABLE_NAME+" order by name ASC";

            Cursor cursor = sqlite.rawQuery(selectAll, null);
            cardList = new ArrayList<BusinessCard>();
            cursor.moveToFirst();

            do {
                BusinessCard cur_card = new BusinessCard();

                cur_card.setTimestamp(cursor.getString(0));
                cur_card.setIsOwn(Integer.parseInt(cursor.getString(1)));
                cur_card.setName(cursor.getString(2));
                cur_card.setCompanyName(cursor.getString(3));
                cur_card.setWebsiteUrl(cursor.getString(4));
                cur_card.setPhone(cursor.getString(5));
                cur_card.setEmailAddress(cursor.getString(6));
                cur_card.setDirectPhone(cursor.getString(7));
                cur_card.setPhoto(cursor.getString(8));
                cur_card.setPost(cursor.getString(9));
                cur_card.setStreet(cursor.getString(10));
                cur_card.setCity(cursor.getString(11));
                cur_card.setState(cursor.getString(12));
                cur_card.setZipCode(cursor.getString(13));
                cur_card.setPhotocompanylogo(cursor.getString(14));
                cur_card.setCountryName(cursor.getString(15));


                cardList.add(cur_card);
            } while (cursor.moveToNext());

        }
        catch (Exception e) {
            // TODO: handle exception
            Log.d("MESSAGE", e.getMessage());
        }
        return cardList;
    }

    public int deleteCard(String timestamp)
    {
        SQLiteDatabase db = this.getReadableDatabase();
       /* String itemid = "Select item_id from data_field_values join data_items on data_field_values.item_id = data_items.item_id" +
                " where data_field_value.data_field_id = "'+()+"' and data_field_value.type_id = '"+()+"'"*/
      return db.delete(BIZCARD_TABLE_NAME, "timestamp = ?", new String[]{timestamp});

    }


    //Getting respective Id of the selected item
    public BusinessCard getSingleOwn() {
        SQLiteDatabase db = this.getReadableDatabase();
        BusinessCard cur_card = new BusinessCard();

        try{
            //String selected_id = "select * from "+BIZCARD_TABLE_NAME+" where timestamp = '"+timestamp+"'";
            String selected_id = "select * from "+BIZCARD_TABLE_NAME+" where isOwn = 1  order by timestamp DESC";
            Cursor cursor = db.rawQuery(selected_id, null);
            cursor.moveToFirst();

            cur_card.setTimestamp(cursor.getString(0));
            cur_card.setIsOwn(Integer.parseInt(cursor.getString(1)));
            cur_card.setName(cursor.getString(2));
            cur_card.setCompanyName(cursor.getString(3));
            cur_card.setWebsiteUrl(cursor.getString(4));
            cur_card.setPhone(cursor.getString(5));
            cur_card.setEmailAddress(cursor.getString(6));
            cur_card.setDirectPhone(cursor.getString(7));
            cur_card.setPhoto(cursor.getString(8));
            cur_card.setPost(cursor.getString(9));
            cur_card.setStreet(cursor.getString(10));
            cur_card.setCity(cursor.getString(11));
            cur_card.setState(cursor.getString(12));
            cur_card.setZipCode(cursor.getString(13));
            cur_card.setPhotocompanylogo(cursor.getString(14));
            cur_card.setCountryName(cursor.getString(15));



            cursor.close();
            db.close();

        }catch (Exception e)
        {

        }
        return cur_card;
    }


    public long insertCard(BusinessCard cur_card) {
        //Inserting values into data_field_value
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insert_data_field_value = new ContentValues();
        //insert_data_field_value.put("data_field_id", key);
       // insert_data_field_value.put("data_field_value", value);
       // insert_data_field_value.put("item_id", itemType);

        insert_data_field_value.put("timestamp",cur_card.getTimestamp());
        insert_data_field_value.put("name",cur_card.getName());
        insert_data_field_value.put("companyName",cur_card.getCompanyName());
        insert_data_field_value.put("phone",cur_card.getPhone());
        insert_data_field_value.put("directPhone",cur_card.getDirectPhone());
        insert_data_field_value.put("emailAddress",cur_card.getEmailAddress());
        insert_data_field_value.put("websiteUrl",cur_card.getWebsiteUrl());
        insert_data_field_value.put("photo",cur_card.getPhoto());
        insert_data_field_value.put("photocompanylogo",cur_card.getPhotocompanylogo());
        insert_data_field_value.put("post",cur_card.getPost());
        insert_data_field_value.put("city",cur_card.getCity());
        insert_data_field_value.put("street",cur_card.getStreet());
        insert_data_field_value.put("state",cur_card.getState());
        insert_data_field_value.put("zipCode",cur_card.getZipCode());
        insert_data_field_value.put("isOwn",cur_card.getIsOwn());
        insert_data_field_value.put("countryName",cur_card.getCountryName());


        try {
            return db.insert(BIZCARD_TABLE_NAME, null, insert_data_field_value);
        } finally {
            db.close();
        }

    }


	
}