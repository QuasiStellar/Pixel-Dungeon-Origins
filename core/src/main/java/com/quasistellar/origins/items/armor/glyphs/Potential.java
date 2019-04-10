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

package com.quasistellar.origins.items.armor.glyphs;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.effects.Lightning;
import com.quasistellar.origins.items.armor.Armor;
import com.quasistellar.origins.items.armor.Armor.Glyph;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Potential extends Glyph {
	
	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, 5 );

		if (Random.Int( level + 20 ) >= 18) {

			int shockDmg = Random.NormalIntRange( 2, 6 );

			defender.damage( shockDmg, this );
			
			checkOwner( defender );
			if (defender == Dungeon.hero) {
				Dungeon.hero.belongings.charge(1f + level/10f);
				Camera.main.shake( 2, 0.3f );
			}

			attacker.sprite.parent.add( new Lightning( attacker.pos, defender.pos, null ) );

		}
		
		return damage;
	}

	@Override
	public Glowing glowing() {
		return WHITE;
	}
}