package com.example.lesson62alertdialogitems;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import static android.content.DialogInterface.OnClickListener;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    final int DIALOG_ITEMS = 1;
    final int DIALOG_ADAPTER = 2;
    final int DIALOG_CURSOR = 3;
    int cnt = 0;
    DB db;
    Cursor cursor;

    String data[] = {"one", "two", "three", "four"};

    OnClickListener myClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // выводим в лог позицию нажатого элемента
            Log.d(LOG_TAG, "which = " + which);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // открываем подключение к БД
        db = new DB(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);
    }

    public void onclick(View v) {
        changeCount();
        switch (v.getId()) {
            case R.id.btnItems:
                showDialog(DIALOG_ITEMS);
                break;
            case R.id.btnAdapter:
                showDialog(DIALOG_ADAPTER);
                break;
            case R.id.btnCursor:
                showDialog(DIALOG_CURSOR);
                break;
            default:
                break;
        }
    }

    // меняем значение счетчика
    void changeCount() {
        cnt++;
        // обновляем массив
        data[3] = String.valueOf(cnt);
        // обновляем БД
        db.changeRec(4, String.valueOf(cnt));
        cursor.requery();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            // массив
            case DIALOG_ITEMS:
                adb.setTitle(R.string.items);
                adb.setItems(data, myClickListener);
                break;
            // адаптер
            case DIALOG_ADAPTER:
                adb.setTitle(R.string.adapter);
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(this, android.R.layout.select_dialog_item, data);
                adb.setAdapter(arrayAdapter, myClickListener);
                break;
            // курсор
            case DIALOG_CURSOR:
                adb.setTitle(R.string.cursor);
                adb.setCursor(cursor, myClickListener, DB.COLUMN_TXT);
                break;
        }
        return adb.create();
    }

//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        // получаем доступ к адаптеру списка диалога
//        AlertDialog alertDialog = (AlertDialog) dialog;
//        ListAdapter listAdapter = alertDialog.getListView().getAdapter();
//
//        switch (id){
//            case DIALOG_ITEMS:
//            case DIALOG_ADAPTER:
//                // проверка возможности преобразования
//                if(listAdapter instanceof BaseAdapter){
//                    // преобразование и вызов метода-уведомления о новых данных
//                    BaseAdapter baseAdapter = (BaseAdapter) listAdapter;
//                    baseAdapter.notifyDataSetChanged();
//                }
//                break;
//            case DIALOG_CURSOR:
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
