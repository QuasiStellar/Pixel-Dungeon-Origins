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

package com.quasistellar.origins.levels.rooms.special;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.blobs.Alchemy;
import com.quasistellar.origins.items.Generator;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.keys.IronKey;
import com.quasistellar.origins.items.potions.Potion;
import com.quasistellar.origins.items.weapon.Weapon;
import com.quasistellar.origins.levels.Level;
import com.quasistellar.origins.levels.Terrain;
import com.quasistellar.origins.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SmithyRoom extends SpecialRoom {

	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		
		Door entrance = entrance();
		
		Point pot = null;
		if (entrance.x == left) {
			pot = new Point( right-1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
		} else if (entrance.x == right) {
			pot = new Point( left+1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
		} else if (entrance.y == top) {
			pot = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, bottom-1 );
		} else if (entrance.y == bottom) {
			pot = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, top+1 );
		}
		Painter.set( level, pot, Terrain.ANVIL );

		int n = Random.IntRange( 1, 2 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (
				level.map[pos] != Terrain.EMPTY_SP ||
				level.heaps.get( pos ) != null);
			level.drop( prize( level ), pos );
		}
		
		entrance.set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );
	}
	
	private static Item prize( Level level ) {

		Item prize = level.findPrizeItem( Weapon.class );
		if (prize == null)
			prize = Generator.random( Generator.Category.WEAPON );

		return prize;
	}
}
