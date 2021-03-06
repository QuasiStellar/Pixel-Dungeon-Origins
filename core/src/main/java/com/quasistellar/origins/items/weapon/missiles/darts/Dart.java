/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.quasistellar.origins.items.weapon.missiles.darts;

import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.weapon.missiles.MissileWeapon;
import com.quasistellar.origins.sprites.ItemSpriteSheet;

public class Dart extends MissileWeapon {

	{
		image = ItemSpriteSheet.DART;

		bones = false; //Finding them in bones would be semi-frequent and disappointing.
	}

	@Override
	public int ave() {
		return 2;
	}
	
	@Override
	protected float durabilityPerUse() {
		return 0;
	}
	
	@Override
	public Item random() {
		super.random();
		quantity += 3;
		return this;
	}
	
	@Override
	public int price() {
		return 4 * quantity;
	}
}
