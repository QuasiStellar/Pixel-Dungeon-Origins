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

package com.quasistellar.origins.actors.buffs;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.actors.hero.HeroClass;
import com.quasistellar.origins.items.artifacts.ChaliceOfBlood;
import com.quasistellar.origins.ui.BuffIndicator;

public class Regeneration extends Buff {
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT && !((Hero)target).isStarving() && !((Hero)target).isTired()) {
				LockedFloor lock = target.buff(LockedFloor.class);
				if (target.HP > 0 && (lock == null || lock.regenOn())) {
					target.HP += 1;
					if ((target.HP == target.HT) || (target.SP == target.SM)) {
						((Hero) target).resting = false;
					}
				}
			}

			if (((Hero) target).heroClass == HeroClass.ENCHANTER && target.SP < target.SM) {
				target.SP +=1;
				BuffIndicator.refreshHero();
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			if (regenBuff != null)
				if (regenBuff.isCursed())
					spend( REGENERATION_DELAY * 1.5f );
				else if (((Hero)target).isRested())
					spend((REGENERATION_DELAY - 5 * 0.9f) * 0.5f);
				else
					spend(REGENERATION_DELAY - 5 * 0.9f);
			else
				spend( REGENERATION_DELAY );
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
}
