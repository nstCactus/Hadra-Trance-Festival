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

package com.zion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.zion.htf.R;

public class CheckableImageButton extends ImageButton implements View.OnClickListener{
	private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
	protected boolean checked = false;
	private CheckableImageButton.OnCheckedChangeListener onCheckedChangeListener;

	public CheckableImageButton(Context context){
		super(context);
	}
	public CheckableImageButton(Context context, AttributeSet attrs){
		super(context, attrs);
		this.loadAttributes(context, attrs);
	}
	public CheckableImageButton(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.loadAttributes(context, attrs);
	}

	private void loadAttributes(Context context, AttributeSet attributeSet) {
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CheckableImageButtonStates, 0, 0);
		this.checked = typedArray.getBoolean(R.styleable.CheckableImageButtonStates_checked, false);
	}

	@Override
	public void onAttachedToWindow(){
		super.onAttachedToWindow();
		this.setOnClickListener(this);
	}

	@Override
	public void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		this.setOnClickListener(null);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if(this.checked){
			View.mergeDrawableStates(drawableState, CheckableImageButton.CHECKED_STATE_SET);
		}
		return drawableState;
	}

	public boolean isChecked(){
		return this.checked;
	}

	public void setChecked(boolean checked){
		if(checked != this.checked){
			this.checked = checked;
			this.refreshDrawableState();
			this.onCheckedChangeListener.onCheckedChanged(this, this.checked);
		}
	}

	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes.
	 *
	 * @param listener the callback to call on checked state change
	 */
	public void setOnCheckedChangeListener(CheckableImageButton.OnCheckedChangeListener listener) {
		this.onCheckedChangeListener = listener;
	}

	@Override
	public void onClick(View v){
		this.setChecked(!this.checked);
	}

	/**
	 * Interface definition for a callback to be invoked when the checked state
	 * of a checkable image button changed.
	 */
	public interface OnCheckedChangeListener{
		/**
		 * Called when the checked state of a checkable image button has changed.
		 *
		 * @param buttonView The checkable image button view whose state has changed.
		 * @param isChecked  The new checked state of buttonView.
		 */
		void onCheckedChanged(CheckableImageButton buttonView, boolean isChecked);
	}

}
