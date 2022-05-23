// DetailFragment.java
// Fragment subclass that displays one contact's details
package com.bhattifaseeh.addressbook;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bhattifaseeh.addressbook.data.DatabaseDescription.Contact;

public class DetailFragment extends Fragment
   implements LoaderManager.LoaderCallbacks<Cursor> {

   // callback methods implemented by MainActivity
   public interface DetailFragmentListener {
      void onContactDeleted(); // called when a contact is deleted

      // pass Uri of contact to edit to the DetailFragmentListener
      void onEditContact(Uri contactUri);
   }

   private static final int CONTACT_LOADER = 0; // identifies the Loader

   private DetailFragmentListener listener; // MainActivity
   private Uri contactUri; // Uri of selected contact

   private TextView nameTextView; // displays contact's name
   private TextView phoneTextView; // displays contact's phone
   private TextView emailTextView; // displays contact's email
   private TextView streetTextView; // displays contact's street
   private TextView cityTextView; // displays contact's city
   private TextView stateTextView; // displays contact's state
   private TextView zipTextView; // displays contact's zip

   // set DetailFragmentListener when fragment attached
   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      listener = (DetailFragmentListener) context;
   }

   // remove DetailFragmentListener when fragment detached
   @Override
   public void onDetach() {
      super.onDetach();
      listener = null;
   }

   // called when DetailFragmentListener's view needs to be created
   @Override
   public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      setHasOptionsMenu(true); // this fragment has menu items to display

      // get Bundle of arguments then extract the contact's Uri
      Bundle arguments = getArguments();

      if (arguments != null)
         contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);

      // inflate DetailFragment's layout
      View view =
         inflater.inflate(R.layout.fragment_detail, container, false);

      // get the EditTexts
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
      emailTextView = (TextView) view.findViewById(R.id.emailTextView);
      streetTextView = (TextView) view.findViewById(R.id.streetTextView);
      cityTextView = (TextView) view.findViewById(R.id.cityTextView);
      stateTextView = (TextView) view.findViewById(R.id.stateTextView);
      zipTextView = (TextView) view.findViewById(R.id.zipTextView);

      // load the contact
      getLoaderManager().initLoader(CONTACT_LOADER, null, this);
      return view;
   }

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_details_menu, menu);
   }

   // handle menu item selections
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_edit:
            listener.onEditContact(contactUri); // pass Uri to listener
            return true;
         case R.id.action_delete:
            deleteContact();
            return true;
      }

      return super.onOptionsItemSelected(item);
   }

   // delete a contact

   // DialogFragment to confirm deletion of contact
   private void deleteContact() {

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
              getActivity());
      // set title
      alertDialogBuilder.setTitle("Delete Contact?");
      alertDialogBuilder.setCancelable(true);
      // set dialog message
      alertDialogBuilder
              .setMessage("Delete Contact?")
              .setCancelable(true)
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    try {getActivity().getContentResolver().delete(
                            contactUri, null, null);
                       listener.onContactDeleted();
                       //so some work
                    } catch (Exception e) {
                       //Exception
                    }
                 }
              })
              .setNegativeButton("No", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    //do something if you need
                    dialog.cancel();
                 }
              });

      // create alert dialog
      AlertDialog alertDialog = alertDialogBuilder.create();

      // show it
      alertDialog.show();

   }

      // called by LoaderManager to create a Loader
   @Override
   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      // create an appropriate CursorLoader based on the id argument;
      // only one Loader in this fragment, so the switch is unnecessary
      CursorLoader cursorLoader;

      switch (id) {
         case CONTACT_LOADER:
            cursorLoader = new CursorLoader(getActivity(),
               contactUri, // Uri of contact to display
               null, // null projection returns all columns
               null, // null selection returns all rows
               null, // no selection arguments
               null); // sort order
            break;
         default:
            cursorLoader = null;
            break;
      }

      return cursorLoader;
   }

   // called by LoaderManager when loading completes
   @Override
   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
      // if the contact exists in the database, display its data
      if (data != null && data.moveToFirst()) {
         // get the column index for each data item
         int nameIndex = data.getColumnIndex(Contact.COLUMN_NAME);
         int phoneIndex = data.getColumnIndex(Contact.COLUMN_PHONE);
         int emailIndex = data.getColumnIndex(Contact.COLUMN_EMAIL);
         int streetIndex = data.getColumnIndex(Contact.COLUMN_STREET);
         int cityIndex = data.getColumnIndex(Contact.COLUMN_CITY);
         int stateIndex = data.getColumnIndex(Contact.COLUMN_STATE);
         int zipIndex = data.getColumnIndex(Contact.COLUMN_ZIP);

         // fill TextViews with the retrieved data
         nameTextView.setText(data.getString(nameIndex));
         phoneTextView.setText(data.getString(phoneIndex));
         emailTextView.setText(data.getString(emailIndex));
         streetTextView.setText(data.getString(streetIndex));
         cityTextView.setText(data.getString(cityIndex));
         stateTextView.setText(data.getString(stateIndex));
         zipTextView.setText(data.getString(zipIndex));
      }
   }

   // called by LoaderManager when the Loader is being reset
   @Override
   public void onLoaderReset(Loader<Cursor> loader) { }
}