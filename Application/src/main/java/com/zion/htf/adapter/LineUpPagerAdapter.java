/*
 *
 *     Copyright 2013-2015 Yohann Bianchi
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

package com.zion.htf.adapter;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.zion.adapter.CursorPagerAdapter;
import com.zion.htf.ui.fragment.LineUpListFragment;

public class LineUpPagerAdapter extends CursorPagerAdapter {
	private static final String TAG = "LineUpPagerAdapter";
	private LineUpListFragment currentFragment;

	public LineUpPagerAdapter(FragmentManager fm, Class fragmentClass, String[] projection, Cursor cursor){
        super(fm, fragmentClass, projection, cursor);
    }

	@Override
	public CharSequence getPageTitle(int position){
        super.cursor.moveToPosition(position);
        return super.cursor.getString(0);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object){
		super.setPrimaryItem(container, position, object);

		if(this.currentFragment != object) this.currentFragment = (LineUpListFragment)object;
	}

	public LineUpListFragment getCurrentFragment(){
		return this.currentFragment;
	}
}
