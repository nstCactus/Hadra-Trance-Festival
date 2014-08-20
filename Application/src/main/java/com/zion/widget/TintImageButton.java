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

package com.zion.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.zion.htf.R;

/**
 * A TintImageButton is a plain ImageButton that automatically has a tint upon selection.
 * The tint color has to be defined and accessible through <code>R.color.tint</code>
 */
public class TintImageButton extends ImageButton{
	public final static String TAG = "TintImageButton";

	public TintImageButton(Context context){
		super(context);
	}

	public TintImageButton(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public TintImageButton(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed){
		super.setPressed(pressed);
		if(pressed) this.setColorFilter(R.color.tint);
		else this.clearColorFilter();
	}
}
