package com.project.businesscardexchange;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pcore.P2PManager;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.project.businesscardexchange.adapters.SongAdapter;
import com.project.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.ui.WifiFilehelper;
import com.project.businesscardexchange.ui.transfer.FileSelectActivity;
import com.project.businesscardexchange.ui.transfer.RadarScanActivity;
import com.project.businesscardexchange.ui.transfer.ReceiveActivity;
import com.project.businesscardexchange.ui.transfer.fragment.CardFragment;
import com.project.businesscardexchange.ui.uientity.CardInfo;
import com.project.businesscardexchange.utils.DBHelper;
import com.project.businesscardexchange.utils.ToastUtils;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public static Context mContext;
    List<BusinessCard> bizCardLists;
    //Realm myRealm;
    DBHelper myDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        mContext = getApplicationContext();

      //  myRealm = MyApplication.getRealmInstance(getApplicationContext());
        myDbHelper = DBHelper.getInstance(getApplicationContext());
        Cache.selectedList.clear();
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTitle("COLLECT AND SHARE BUSSINESS CARDS")
                .setTarget(toolbar)
                .setDescription("See list business cards.")
                .setDelay(1000)
                .displayOneTime(3)
                .setBackgroundColor(R.color.colorAccent)
                .setBackgroundAlpha(95)
                .setCloseButtonColor(R.color.colorAccent)
                .setButtonText("GOT IT?")
                .build();
        showtips.setDisplayOneTimeID(3);
        showtips.show(this);



        FloatingActionButton fab_receive = (FloatingActionButton) findViewById(R.id.fab);
        ShowTipsView showtips3 = new ShowTipsBuilder(this)
                .setTitle("RECEIVE CARDS AND PICTURES VIA WIFI")
                .setTarget(fab_receive)
                .setDescription("Receive business cards or pictures")
                .setDelay(1000)
                .displayOneTime(5)
                .setBackgroundColor(R.color.colorAccent)
                .setBackgroundAlpha(95)
                .setCloseButtonColor(R.color.colorAccent)
                .setButtonText("GOT IT?")
                .build();
        showtips3.setDisplayOneTimeID(5);
        showtips3.show(this);
        FloatingActionButton fab_send = (FloatingActionButton) findViewById(R.id.fab1);
        ShowTipsView showtips2 = new ShowTipsBuilder(this)
                .setTitle("SEND CARDS AND PICTURES VIA WIFI")
                .setTarget(fab_send)
                .setDescription("Select business cards or pictures you want to share and send")
                .setDelay(1000)
                .displayOneTime(6)
                .setBackgroundColor(R.color.colorAccent)
                .setBackgroundAlpha(95)
                .setCloseButtonColor(R.color.colorAccent)
                .setButtonText("GOT IT?")
                .build();
        showtips2.setDisplayOneTimeID(6);
        showtips2.show(this);

        fab_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
                /*Intent gotoWifi = new Intent(CardsActivity.this, com.project.businesscardexchange.ui.main.MainActivity.class);
                gotoWifi.putExtra("type","receive");
                startActivity(gotoWifi);*/
                Cache.selectedList.clear();
                startActivity(new Intent(CardsActivity.this, ReceiveActivity.class)
                        .putExtra("name", Build.DEVICE));//nameEdit.getText().toString()));
            }
        });
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoWifi = new Intent(CardsActivity.this, FileSelectActivity.class);
                Cache.selectedList.clear();
                gotoWifi.putExtra("name",Build.DEVICE);
                gotoWifi.putExtra("type","shareCard");
                //gotoWifi.putExtra("card",b);
                startActivity(gotoWifi);
            }
        });

      //  Select select = new Select();

        // Call select.all() to select all rows from our table which is
        // represented by Person.class and execute the query.

        // It returns an ArrayList of our Person objects where each object
        // contains data corresponding to a row of our database.



        mRecyclerView = (RecyclerView) findViewById(R.id.cards_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        bizCardLists = new ArrayList<>();
        // specify an adapter (see also next example)
      //  registerForContextMenu(mRecyclerView);


        ItemClickSupport.addTo(mRecyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(final RecyclerView recyclerView, final int position, final View v) {

                /*new MaterialDialog.Builder(CardsActivity.this)
                        .positiveText("Delete")
                        .positiveColor(Color.parseColor("#F44336"))
                        .negativeText("Share")
                        .negativeColor(Color.parseColor("#00BCD4"))
                        .backgroundColor(Color.parseColor("#40303F9F"))
                        .theme(Theme.DARK)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                try {
                                    recyclerView.removeView(v);

                                    new SongAdapter(bizCardLists, CardsActivity.this).removeItem(position);

                                    //  List<BCard> bCardList = new Select().all().from(BCard.class).execute();
                                    //new Delete().from(BCard.class).where("Id = ?",id).execute();
                                    RealmResults<BusinessCardRealm> results = myRealm.where(BusinessCardRealm.class).findAll();
                                    BusinessCardRealm b = results.get(position);
                                    String title = b.getName();
                                    // All changes to data must happen in a transaction
                                    myRealm.beginTransaction();
                                    // remove single match
                                    results.remove(position);
                                    myRealm.commitTransaction();
                                    if (results.size() == 0) {
                                      //  Prefs.with(context).setPreLoad(false);
                                    }
                                   // notifyDataSetChanged();
                                    Toast.makeText(CardsActivity.this, "Deleted " + title, Toast.LENGTH_SHORT).show();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                Toast.makeText(CardsActivity.this, "share", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("tel:"));
//                                startActivity(intent);
                                RealmResults<BusinessCardRealm> results = myRealm.where(BusinessCardRealm.class).findAll();
                               final BusinessCardRealm bCard = results.get(position);

                                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(CardsActivity.this);
                                assert nfcAdapter != null;
                                nfcAdapter.setNdefPushMessageCallback(
                                        new NfcAdapter.CreateNdefMessageCallback() {
                                            public NdefMessage createNdefMessage(NfcEvent event) {
                                               return new NFCService(getApplicationContext(),position,bCard).createMessage();
                                            }
                                        }, CardsActivity.this);



                            }
                        }).show();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(CardsActivity.this);
                builder.setTitle("Choose Action:");
                builder.setItems(new CharSequence[]
                                {"Share", "Send to NFC", "Send to wifi", "Delete"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // The 'which' argument contains the index position
                                // of the selected item
                             //   RealmResults<BusinessCardRealm> results = myRealm.where(BusinessCardRealm.class).findAll();
                             //   results.sort("name");
                                final BusinessCard b = bizCardLists.get(position);

                                switch (which) {
                                    case 0:
                                        String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+File.separator+MyApplication.ZIP_DIRECTORY_NAME;
                                       // Log.d(tag,"inputPath:"+inputPath);
                                        File file = new File(inputPath+b.getTimestamp()+".zip");
                                      //  Log.d(tag,"filePath:"+file.getAbsolutePath());
                                        if (file.exists())
                                        {
                                            String zipName = b.getTimestamp();
                                            Intent shareIntent = new Intent();
                                            shareIntent.setAction(Intent.ACTION_SEND);
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(zipName)));
                                            shareIntent.setType("*/*");
                                            startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.send_to)));
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Can not share 1:"+b.getName(), Toast.LENGTH_SHORT).show();
                                        }

                                        break;
                                    case 1:
                                       // Toast.makeText(getApplicationContext(), "clicked 2:"+b.getName(), Toast.LENGTH_SHORT).show();
                                      //  Toast.makeText(CardsActivity.this, "share", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("tel:"));
//                                startActivity(intent);
                                    try {

                                        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(CardsActivity.this);
                                        assert nfcAdapter != null;
                                        nfcAdapter.setNdefPushMessageCallback(
                                                new NfcAdapter.CreateNdefMessageCallback() {
                                                    public NdefMessage createNdefMessage(NfcEvent event) {
                                                        return new NFCService(getApplicationContext(), position, b).createMessage();
                                                    }
                                                }, CardsActivity.this);
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(getApplicationContext(),"NFC ERROR ",Toast.LENGTH_LONG).show();
                                    }

                                        break;
                                    case 2:
                                        //Toast.makeText(getApplicationContext(), "clicked 3:"+b.getName(), Toast.LENGTH_SHORT).show();
                                       /* Intent gotoWifi = new Intent(CardsActivity.this, com.project.businesscardexchange.ui.main.MainActivity.class);
                                        gotoWifi.putExtra("type","shareCard");
                                        //gotoWifi.putExtra("card",b);
                                        startActivity(gotoWifi);*/
                                        CardInfo info = CardFragment.getCardInfo(b);
                                        try {
                                            P2PFileInfo fileInfo = new P2PFileInfo();
                                            fileInfo.name = info.getFileName();
                                            fileInfo.type = P2PConstant.TYPE.APP;
                                            fileInfo.size = new File(info.getFilePath()).length();
                                            fileInfo.path = info.getFilePath();
                                            Cache.selectedList.add(fileInfo);
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("ErrorWifi","Do cache:"+e.getLocalizedMessage());
                                        }
                                        if (Cache.selectedList.size() > 0)
                                            startActivity(new Intent(CardsActivity.this,
                                                    RadarScanActivity.class).putExtra("name", Build.DEVICE));
                                        else
                                            ToastUtils.showTextToast(getApplicationContext(),
                                                    getString(R.string.file_not_found));
                                        break;
                                    case 3:
                                        try {
                                            //bizCardLists.remove(position);
                                            recyclerView.removeView(v);
                                            new SongAdapter(bizCardLists, CardsActivity.this).removeItem(position);
                                            //  List<BCard> bCardList = new Select().all().from(BCard.class).execute();
                                            //new Delete().from(BCard.class).where("Id = ?",id).execute();
                                            String title = b.getName();
                                            String deleteName=b.getTimestamp();
                                            myDbHelper.deleteCard(deleteName);
                                            // All changes to data must happen in a transaction
                                         //   myRealm.beginTransaction();
                                            // remove single match
                                            //results.remove(position);
                                          //  myRealm.commitTransaction();
                                           // b.removeFromRealm();
                                            if (bizCardLists.size() == 0) {
                                                //  Prefs.with(context).setPreLoad(false);
                                            }

                                            File fileDelete = new File(P2PManager.SAVE_DIR + File.separator +"Zip"+File.separator+deleteName+".zip");
                                          //  Log.e("deleteTest",""+fileDelete.getAbsolutePath());
                                            Log.e("deleteTest",""+fileDelete.getPath());
                                            fileDelete.delete();
                                            if(fileDelete.exists())
                                            {

                                                fileDelete.getCanonicalFile().delete();
                                                if(fileDelete.exists()){
                                                    getApplicationContext().deleteFile(fileDelete.getName());
                                                }
                                            }
                                            File folderDelete = new File(P2PManager.SAVE_DIR+File.separator+deleteName);
                                            Log.e("deleteTest","folderDelete:"+folderDelete.getPath());

                                            if (folderDelete.isDirectory())
                                            {
                                                String[] children = folderDelete.list();
                                                for (int i = 0; i < children.length; i++)
                                                {
                                                    Log.e("deleteTest","folderDelete:"+folderDelete.getPath()+File.separator+children[i]);

                                                    new File(folderDelete, children[i]).delete();
                                                }
                                                folderDelete.delete();
                                                if(folderDelete.exists())
                                                {

                                                    folderDelete.getCanonicalFile().delete();
                                                    if(folderDelete.exists()){
                                                        getApplicationContext().deleteFile(folderDelete.getName());
                                                    }
                                                }
                                            }


                                            // notifyDataSetChanged();
                                            Toast.makeText(CardsActivity.this, "Deleted " + title, Toast.LENGTH_SHORT).show();


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                       // Toast.makeText(getApplicationContext(), "clicked 4", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                builder.create().show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
           // RealmResults<BusinessCardRealm> results1 =   myRealm.where(BusinessCardRealm.class).findAll();
          //   results1.sort("name");
            bizCardLists = myDbHelper.getAllCards();
            Log.e("bizCard","size:"+bizCardLists.size());
            String[] myNameList = new String[bizCardLists.size()];
            int index=0;
            for(BusinessCard c:bizCardLists)
            {
                Log.e("bizCard","name:"+c.getName());
                myNameList[index] = c.getTimestamp();//+"_other";
                index++;
            }
            File f = new File(P2PManager.SAVE_DIR+File.separator);
            File[] files = f.listFiles();
            if(files.length != bizCardLists.size() && files.length-1 != bizCardLists.size() && files.length-2 != bizCardLists.size())
            {
                //Show progress dialog
            }
            Log.e("read_insert","filesCount:"+files.length);
            Log.e("read_insert","realmCount:"+bizCardLists.size());
            for (File inFile : files)
            {

                // is directory
                if (inFile.isDirectory() && !inFile.getName().equals("Zip") && !inFile.getName().equals("Image"))
                {
                    // if this already in database?
                    if(!Arrays.asList(myNameList).contains(inFile.getName())) {
                        // if this not already in database: insert to database
                        try {
                            Log.e("read_insert", "readStart");
                            Log.e("read_insert", "readPath:" + P2PManager.SAVE_DIR + File.separator + inFile.getName() + File.separator + "DATA.txt");
                            String myFile = WifiFilehelper.readFromFile(P2PManager.SAVE_DIR + File.separator + inFile.getName() + File.separator + "DATA.txt", getApplicationContext());
                            //  myFile = myFile.substring(myFile.lastIndexOf("=")+1,myFile.length());
                            JSONObject data = new JSONObject(myFile);
                            Log.e("read_insert", "data:"+data.toString());
                            BusinessCard curRealmObject = new BusinessCard();
                            // for (int i=0;i<data.length();i++)
                            //  {
                            // JSONObject cur_name= data.getJSONObject(i);
                            // Log.e("read_insert", "current:"+cur_name.toString());
                            int is_own = data.getInt("isOwn");
                            if(is_own ==0) {
                                curRealmObject.setIsOwn(0);
                                curRealmObject.setName(data.getString("name"));
                                curRealmObject.setCompanyName(data.getString("companyName"));
                                curRealmObject.setWebsiteUrl(data.getString("websiteUrl"));
                                curRealmObject.setPhone(data.getString("phone"));
                                curRealmObject.setEmailAddress(data.getString("emailAddress"));
                                curRealmObject.setDirectPhone(data.getString("directPhone"));
                                curRealmObject.setPhoto(data.getString("photo"));
                                curRealmObject.setPost(data.getString("post"));
                                curRealmObject.setStreet(data.getString("street"));
                                curRealmObject.setCity(data.getString("city"));
                                curRealmObject.setState(data.getString("state"));
                                curRealmObject.setZipCode(data.getString("zipCode"));
                                curRealmObject.setPhotocompanylogo(data.getString("photocompanylogo"));
                                curRealmObject.setTimestamp(data.getString("timestamp"));
                                // }
                                //   BusinessCardRealm myCurrentObject = (BusinessCardRealm)myFile;
                                Log.e("read_insert", "read:New:" + curRealmObject.toString());
                              //  myRealm.beginTransaction();
                               // BusinessCardRealm copyOfBCard = myRealm.copyToRealm(curRealmObject);
                               // myRealm.commitTransaction();
                               // bizCardLists.add(copyOfBCard);
                                myDbHelper.insertCard(curRealmObject);
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("read_insert", "Error"+e.getLocalizedMessage());
                            e.fillInStackTrace();
                        }
                    }
                }
            }


            //lists = select.all().from(BCard.class).orderBy("name ASC").execute();
        } catch (Exception e)
        {
            Log.e("Error","No any record");
            e.fillInStackTrace();
        }


        //dummy list*
//        BCard bCard3 = new BCard("rajan","infosys","789432165","sdfsd21@gmail.com","www.google.com","1234545","shantinager","df","MD","ktm","bagmati","1234");
//        BCard bCard = new BCard("name","companyName","789432165","emailAddress","www.google.com","1234545","street","photo","post","city","state","1234");
//        BCard bCard2 = new BCard("sajan","infosys","789432165","sdfsd21@gmail.com","www.google.com","1234545","shantinager","df","MD","ktm","bagmati","1234");
//        BCard bCard1 = new BCard("aasdf","adsf","789432165","sdfsd21@","www.google.com","1234545","ged","df","sT","df","sdfdtate","1234");
//        BCard bCard4 = new BCard("aarjan","infosys","789432165","sdfsd21@gmail.com","www.google.com","1234545","shantinager","df","MD","ktm","bagmati","1234");
//
//
//        bCard.save();
//        bCard1.save();
//        bCard2.save();
//        bCard3.save();
//        bCard4.save();
//        businessCardList.add(bCard);
//        businessCardList.add(bCard1);
//        businessCardList.add(bCard2);
//        businessCardList.add(bCard3);
//        businessCardList.add(bCard4);

        mAdapter = new SongAdapter(bizCardLists,this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CardsActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    String[] menuList = {"Share","Send to NFC","Send to Wifi","Delete"};
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.cards_recycle) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Choose Action");//Countries[info.position]);
            String[] menuItems = menuList;//getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = menuList;//getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        BusinessCardRealm selectedItem = bizCardLists.get(info.position);

       // TextView text = (TextView)findViewById(R.id.footer);
        Toast.makeText(getApplicationContext(),""+String.format("Selected %s for item %s", menuItemName, selectedItem.getName()),Toast.LENGTH_SHORT).show();
        return true;
    }*/


}
