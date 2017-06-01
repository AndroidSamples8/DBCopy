package com.msr.dbcopy.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.msr.dbcopy.DatabaseContants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database {
    private Context context;
    private SQLiteDatabase db;

    public Database(Context context) {
        this.context = context;
        openDatabase("contacts.db"); //Hardcode this for now since we only have one DB
    }

    /**
     * Checks if the database exists at the application's data directory
     *
     * @param dbName Name of the database to check the existence of
     * @return True if the database exists, false if not
     */
    private boolean checkDatabase(String dbName) {
        File dbFile = new File(this.context.getApplicationInfo().dataDir + "/" + dbName);
        return dbFile.exists();
    }

    /**
     * Copies the database from assets to the application's data directory
     *
     * @param dbName Name of the database to be copied
     */
    private void copyDatabase(String dbName) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = this.context.getAssets().open(dbName);
            output = new FileOutputStream(this.context.getApplicationInfo().dataDir + "/" + dbName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

        } catch (IOException ignored) {
        } finally {
            try {
                if (output != null && input != null) {
                    output.close();
                    input.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Opens a connection to a SQLite database
     *
     * @param dbName The database to open a connection to
     */
    private void openDatabase(String dbName) {
        if (!this.checkDatabase(dbName)) {
            this.copyDatabase(dbName);
        }
        try {
            this.db = SQLiteDatabase.openDatabase(this.context.getApplicationInfo().dataDir + "/" + dbName, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            this.db = null;
        }
    }

    /**
     * Performs a query against the database
     *
     * @param query The query itself
     * @param args  Arguments for any bound parameters
     * @return Cursor for iterating over results
     */
    public Cursor queryDatabase(String query, String[] args) {
        if (this.db != null && this.db.isOpen()) {
            return db.rawQuery(query, args);
        } else {
            return null;
        }
    }

    public int getRecords() {
        Cursor cursor = null;
        int count = 0;
        if (this.db != null && this.db.isOpen()) {
            try {
                cursor = db.rawQuery("SELECT * from " + DatabaseContants.TABLE_EMPLOYEE, null);
                count = cursor.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } else {
            return 0;
        }
        return count;
    }

    /**
     * Closes the database handle
     */
    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }

}
