/*
    Copyright 2013 Yohann Bianchi

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
    or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 2;
    private Context context;
    private final String TAG = "DatabaseOpenHelper";

    public DatabaseOpenHelper(Context context){
        super(context, "database.sqlite", null, DB_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            this.applyScript(sqLiteDatabase, "database.sql");
        }
        catch(IOException e){
            Toast.makeText(this.context, R.string.error_database_creation, Toast.LENGTH_SHORT).show();
            Log.e(this.TAG, "Impossible de lire le script de création de la base", e);
        }
        catch(SQLException e){
            Toast.makeText(this.context, R.string.error_database_creation, Toast.LENGTH_SHORT).show();
            Log.e(this.TAG, "Une requête du script de création de la base a échoué", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        try{
            if(from < 2){
                this.applyScript(sqLiteDatabase, "database_upgrade_v2.sql");
            }
        }
        catch(IOException e){
            Toast.makeText(this.context, R.string.error_database_creation, Toast.LENGTH_SHORT).show();
            Log.e(this.TAG, "Impossible de lire le script de mise à jour de la base", e);
        }

        catch(SQLException e){
            Toast.makeText(this.context, R.string.error_database_creation, Toast.LENGTH_SHORT).show();
            Log.e(this.TAG, "Une requête du script a échoué", e);
        }
    }

    /**
     * Parses a file containing sql statements into a String array that contains
     * only the sql statements. Comments and white spaces in the file are not
     * parsed into the String array. Note the file must not contained malformed
     * comments and all sql statements must end with a semi-colon ";" in order
     * for the file to be parsed correctly. The sql statements in the String
     * array will not end with a semi-colon ";".
     * @param sqlStream InputStream that contains sql statements.
     * @return String array containing the sql statements.
     */
    private static String[] parseSqlFile(InputStream sqlStream) throws IOException {
        String line;
        StringBuilder sql = new StringBuilder();
        String multiLineComment = null;
        BufferedReader sqlBuffer = new BufferedReader(new InputStreamReader(sqlStream));

        while ((line = sqlBuffer.readLine()) != null) {
            line = line.trim();

            // Check for start of multi-line comment
            if (multiLineComment == null) {
                // Check for first multi-line comment type
                if (line.startsWith("/*")) {
                    if (!line.endsWith("*/")) {
                        multiLineComment = "/*";
                    }
                    // Check for second multi-line comment type
                } else if (line.startsWith("{")) {
                    if (!line.endsWith("}")) {
                        multiLineComment = "{";
                    }
                    // Append line if line is not empty or a single line comment
                } else if (!line.startsWith("--") && !line.equals("")) {
                    sql.append(line);
                } // Check for matching end comment
            } else if (multiLineComment.equals("/*")) {
                if (line.endsWith("*/")) {
                    multiLineComment = null;
                }
                // Check for matching end comment
            } else if (multiLineComment.equals("{")) {
                if (line.endsWith("}")) {
                    multiLineComment = null;
                }
            }

        }

        sqlBuffer.close();

        return sql.toString().split(";");
    }

    private void applyScript(SQLiteDatabase db, String name) throws IOException, SQLException{
        try{
            String[] sqlStatements = parseSqlFile(context.getResources().getAssets().open(name));

            db.beginTransaction();
            for(String statement : sqlStatements){
                db.execSQL(statement);
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }
}
