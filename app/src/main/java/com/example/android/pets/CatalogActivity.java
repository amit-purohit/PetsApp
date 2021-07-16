/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
//import android.support.v7.app.LoaderManager;
//import android.support.v7.content.CursorLoader;
//import android.support.v7.content.Loader;
//import android.support.v7.widget.SimpleCursorAdapter;


import android.provider.UserDictionary;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //private PetDbHelper mDbHelper;
    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView petListView = (ListView)findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
//        getLoaderManager().initLoader(PET_LOADER, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>)this);
        getSupportLoaderManager().initLoader(PET_LOADER,null,this);

        //mDbHelper = new PetDbHelper(this);
        //displayDatabaseInfo();
    }

//    private void displayDatabaseInfo(){
//
//
//        String projection[] = {
//                PetContract.PetEntry._ID,
//                PetContract.PetEntry.COLUMN_PET_NAME,
//                PetContract.PetEntry.COLUMN_PET_BREED,
//                PetContract.PetEntry.COLUMN_PET_GENDER,
//                PetContract.PetEntry.COLUMN_PET_WEIGHT
//
//        };
//
//        //Cursor cursor = db.query(PetContract.PetEntry.TABLE_NAME, project,null, null, null, null, null);
//        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, projection, null,null,null);
//
////        try{
////            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
////            displayView.setText("Number of rows in the pets table is "+cursor.getCount() );
////            displayView.append("\n" +PetContract.PetEntry._ID + " - " +
////                    PetContract.PetEntry.COLUMN_PET_NAME + " - " +
////                    PetContract.PetEntry.COLUMN_PET_BREED + " - " +
////                    PetContract.PetEntry.COLUMN_PET_GENDER + " - " +
////                    PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");
////
////            int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
////            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
////            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
////            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
////            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
////
////            while(cursor.moveToNext()){
////
////                int currentID = cursor.getInt(idColumnIndex);
////                String currentName = cursor.getString(nameColumnIndex);
////                String currentBreed = cursor.getString(breedColumnIndex);
////                int currentGender = cursor.getInt(genderColumnIndex);
////                int currentWeight = cursor.getInt(weightColumnIndex);
////
////                displayView.append(("\n" + currentID + " - " +
////                        currentName + " - " +
////                        currentBreed + " - " +
////                        currentGender + " - " +
////                        currentWeight));
////            }
////
////
////
////        }
////        finally {
////            cursor.close();
////        }
//        ListView petListView  = (ListView) findViewById(R.id.list);
//        PetCursorAdapter adpater = new PetCursorAdapter(this, cursor);
//        petListView.setAdapter(adpater);
//
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }


    private void insertPet(){
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,7);

        // this is inserting pet by directly connecting with database
//        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        Uri newUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);

    }

    private void deleteAllPets(){
        int rowsDeleted  = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED};

        return new CursorLoader(this,
                PetContract.PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull  Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull  Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
