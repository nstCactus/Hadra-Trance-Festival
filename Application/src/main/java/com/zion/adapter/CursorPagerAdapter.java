/*
 *
 *     Copyright 2013-2014 Yohann Bianchi
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *     or see <http://www.gnu.org/licenses/>.
 *
 */

package com.zion.adapter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CursorPagerAdapter<F extends Fragment> extends FragmentPagerAdapter{
    private final Class<F> fragmentClass;
    private final String[] projection;
    protected Cursor cursor;

    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass, String[] projection, Cursor cursor){
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.cursor = cursor;
    }

    @Override
    public F getItem(int position){
        if(null == this.cursor) // shouldn't happen
            return null;

        this.cursor.moveToPosition(position);
        F frag;
        try {
            frag = this.fragmentClass.newInstance();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        Bundle args = new Bundle();
        for(int i = 0; i < this.projection.length; ++i){
            args.putString(this.projection[i], this.cursor.getString(i));
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public int getCount(){
        if(null == this.cursor) return 0;
        else return this.cursor.getCount();
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there wasa not one.
     * If the given new Cursor is the same instance is the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor){
        Cursor oldCursor = this.cursor;
        if(oldCursor != newCursor){
            this.cursor = newCursor;
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param newCursor The new cursor to be used
     */
    public void changeCursor(Cursor newCursor){
        Cursor oldCursor = this.swapCursor(newCursor);
        if(null != oldCursor){
            oldCursor.close();
        }
    }


    public Cursor getCursor() {
        return this.cursor;
    }
}
