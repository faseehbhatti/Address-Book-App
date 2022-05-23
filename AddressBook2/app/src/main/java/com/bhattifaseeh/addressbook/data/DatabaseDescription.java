// DatabaseDescription.java
// Describes the table name and column names for this app's database,
// and other information required by the ContentProvider
package com.bhattifaseeh.addressbook.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
   // ContentProvider's name: typically the package name
   public static final String AUTHORITY =
      "com.bhattifaseeh.addressbook.data";

   // base URI used to interact with the ContentProvider
   private static final Uri BASE_CONTENT_URI =
      Uri.parse("content://" + AUTHORITY);

   // nested class defines contents of the contacts table
   public static final class Contact implements BaseColumns {
      public static final String TABLE_NAME = "contacts"; // table's name

      // Uri for the contacts table
      public static final Uri CONTENT_URI =
         BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

      // column names for contacts table's columns
      public static final String COLUMN_NAME = "name";
      public static final String COLUMN_PHONE = "phone";
      public static final String COLUMN_EMAIL = "email";
      public static final String COLUMN_STREET = "street";
      public static final String COLUMN_CITY = "city";
      public static final String COLUMN_STATE = "state";
      public static final String COLUMN_ZIP = "zip";

      // creates a Uri for a specific contact
      public static Uri buildContactUri(long id) {
         return ContentUris.withAppendedId(CONTENT_URI, id);
      }
   }
}
