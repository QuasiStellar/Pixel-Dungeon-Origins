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

package com.quasistellar.origins.items.wands;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Recharging;
import com.quasistellar.origins.effects.SpellSprite;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.sprites.ItemSpriteSheet;

public class WandOfMagicMissile extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
	}

	public int min(){
		return 2+5;
	}

	public int max(){
		return 8+2*5;
	}
	
	@Override
	protected void onZap( OldBallistica bolt ) {
				
		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null) {

			processSoulMark(ch, chargesPerCast());
			ch.damage(damageRoll(), this);

			ch.sprite.burst(0xFFFFFFFF, 5 / 2 + 2);

		} else {
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}
	
	protected int initialCharges() {
		return 3;
	}

}
