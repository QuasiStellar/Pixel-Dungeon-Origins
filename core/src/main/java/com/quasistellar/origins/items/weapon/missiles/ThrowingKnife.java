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

package com.quasistellar.origins.items.weapon.missiles;

import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.actors.hero.HeroClass;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.items.rings.RingOfSharpshooting;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ThrowingKnife extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.THROWING_KNIFE;
		
		bones = false;
		
	}

	@Override
	public int ave() {
		return 5;
	}

	private Char enemy;
	
	@Override
	public void onThrow(int cell) {
		enemy = Actor.findChar(cell);
		super.onThrow(cell);
	}
	
	@Override
	protected float durabilityPerUse() {
		return super.durabilityPerUse()*2f;
	}
	
	@Override
	public int price() {
		return 6 * quantity;
	}
}
