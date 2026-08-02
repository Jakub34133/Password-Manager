package pl.kalisz.uk.prup.passwordmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.kalisz.uk.prup.passwordmanager.AccountModel;
import pl.kalisz.uk.prup.passwordmanager.security.MySecurityManager;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "passwordmanager.db";
    public static final int DB_VERSION = 1;
    public static final String ACCOUNTS_TABLE = "accounts";
    public static final String ACCOUNTS_ID = "id";
    public static final String ACCOUNTS_PLATFORM = "platform";
    public static final String ACCOUNTS_LOGIN = "login";
    public static final String ACCOUNTS_PASSWORD = "password";
    Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAccountsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete older tables
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE);
        onCreate(db);
    }

    public void createAccountsTable(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ACCOUNTS_TABLE + " " +
                "(" +
                ACCOUNTS_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                ACCOUNTS_PLATFORM + " text," +
                ACCOUNTS_LOGIN + " text," +
                ACCOUNTS_PASSWORD + " text" +
                ");";
        db.execSQL(createTableStatement);
    }

    public boolean addAccount(AccountModel accountModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNTS_PLATFORM, accountModel.getPlatform());
        cv.put(ACCOUNTS_LOGIN, accountModel.getLogin());
        String encryptedPass = MySecurityManager.encryptPass(accountModel.getPassword());
        cv.put(ACCOUNTS_PASSWORD, encryptedPass);

        long insert = db.insert(ACCOUNTS_TABLE, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }

    public boolean updateAccount(AccountModel accountModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNTS_PLATFORM, accountModel.getPlatform());
        cv.put(ACCOUNTS_LOGIN, accountModel.getLogin());
        String encryptedPass = MySecurityManager.encryptPass(accountModel.getPassword());
        cv.put(ACCOUNTS_PASSWORD, encryptedPass);

        String selection = ACCOUNTS_ID + " = ?";
        String[] selectionArgs = { String.valueOf(accountModel.getId()) };

        int result = db.update(ACCOUNTS_TABLE, cv, selection, selectionArgs);
        if(result <= 0)
            return false;
        else
            return true;
    }

    public boolean deleteAccount(AccountModel accountModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = ACCOUNTS_ID + " = ?";
        String[] selectionArgs = { String.valueOf(accountModel.getId()) };

        int result = db.delete(ACCOUNTS_TABLE, selection, selectionArgs);
        if(result <= 0)
            return false;
        else
            return true;
    }

    public AccountModel getAccountById(int account_id) {
        AccountModel accountModel = null;
        String queryString = "SELECT * FROM " + ACCOUNTS_TABLE + " WHERE " + ACCOUNTS_ID + "=" + account_id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            String platform = cursor.getString(1);
            String login = cursor.getString(2);
            String password = cursor.getString(3);
            password = MySecurityManager.decryptPass(password);

            accountModel = new AccountModel(account_id, platform, login, password);
        }

        cursor.close();
        db.close();

        return accountModel;
    }

    public List<AccountModel> getAccounts() {
        List<AccountModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACCOUNTS_TABLE + " ORDER BY " + ACCOUNTS_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String platform = cursor.getString(1);
                String login = cursor.getString(2);
                String password = cursor.getString(3);
                password = MySecurityManager.decryptPass(password);

                AccountModel accountModel = new AccountModel(id, platform, login, password);
                returnList.add(accountModel);

            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }
}
