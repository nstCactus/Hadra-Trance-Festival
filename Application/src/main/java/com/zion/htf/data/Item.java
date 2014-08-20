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

package com.zion.htf.data;

public class Item{
	public static final int TYPE_ITEM    = 0;
	public static final int TYPE_SECTION = 1;

	protected String name;
	protected int    type = TYPE_ITEM;

	public Item(String name){
		this.name = name;
		this.type = Item.TYPE_SECTION;
	}

	public Item(String name, int type){
		this.name = name;
		this.type = type;
	}

	public int getType(){
		return this.type;
	}

	public String toString(){
		return this.name;
	}
}
