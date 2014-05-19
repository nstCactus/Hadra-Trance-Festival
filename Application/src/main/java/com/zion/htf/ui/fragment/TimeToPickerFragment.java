/*
    Copyright 2014 Yohann Bianchi

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
package com.zion.htf.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.zion.htf.R;

public class TimeToPickerFragment extends DialogFragment{
    Spinner unitSpinner;
    NumberPicker timePicker;

    public TimeToPickerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getDialog().setTitle(R.string.title_new_alarm_dialog);
        View view = inflater.inflate(R.layout.fragment_time_to_picker,  container, false);
        this.unitSpinner = (Spinner)view.findViewById(R.id.unitSpinner);
        this.timePicker = (NumberPicker)view.findViewById(R.id.time_picker);

        if(null == this.unitSpinner) throw new NullPointerException("Couldn't find unitSpinner");
        if(null == this.timePicker) throw new NullPointerException("Couldn't find timePicker");

        this.timePicker.setMinValue(0);
        this.timePicker.setMaxValue(500);
        this.timePicker.setValue(60);

        this.unitSpinner.setSelection(1);

        return view;
    }
}
