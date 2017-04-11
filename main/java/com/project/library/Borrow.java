package com.project.library;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static com.project.library.SessionManager.KEY_NAME;

public class Borrow extends AppCompatActivity {

    private dbhelper db1;
    private SimpleCursorAdapter dataAdapter1;
    SessionManager session;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            db1  = new dbhelper(getApplicationContext());

            Cursor cursor1 = db1.getBookBorrowDetails(session.getUserDetails().get(KEY_NAME));
            displayListView1(cursor1);
        }else{

            alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("Login validation");

            alertDialog.setMessage("Login to view Borrow screen.");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);

                    startActivity(i);

                } });

        }


    }

    private void displayListView1(Cursor cursor) {


        // The desired columns to be bound
        String[] columns = new String[] {
                dbhelper.BOOK_ID,
                dbhelper.BOOK_NAME,
                dbhelper.BOOK_LANG,
                dbhelper.BOOK_AUTH
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.book_id,
                R.id.book_name,
                R.id.book_language,
                R.id.book_author,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter1 = new SimpleCursorAdapter(
                this, R.layout.activity_searchdetails,
                cursor,
                columns,
                to,
                0);


        ListView listView1 = (ListView) findViewById(R.id.listView2);
        // Assign adapter to ListView
        listView1.setAdapter(dataAdapter1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor1 = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String bookName =
                        cursor1.getString(cursor1.getColumnIndexOrThrow(dbhelper.BOOK_NAME));
                Toast.makeText(getApplicationContext(),
                        "Borrowed book : "+bookName, Toast.LENGTH_SHORT).show();

            }
        });



    }
}