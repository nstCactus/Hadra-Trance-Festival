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

package com.zion.htf.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zion.htf.ui.fragment.ArtistDetailsFragment;
import com.zion.htf.ui.fragment.ArtistSoundcloudFragment;

public class ArtistDetailsPagerAdapter extends FragmentPagerAdapter{
    private static final int PAGE_COUNT = 2;
    private final Bundle args;

    public ArtistDetailsPagerAdapter(FragmentManager fm, Bundle args){
        super(fm);

        this.args = args;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                Fragment artistDetailsFragment = new ArtistDetailsFragment();
                artistDetailsFragment.setArguments(this.args);
                return artistDetailsFragment;
            case 1:
                Fragment artistSoundcloudFragment = new ArtistSoundcloudFragment();
                artistSoundcloudFragment.setArguments(this.args);
                return artistSoundcloudFragment;
        }
        return null;
    }

    @Override
    public int getCount(){
        return ArtistDetailsPagerAdapter.PAGE_COUNT;
    }
}
