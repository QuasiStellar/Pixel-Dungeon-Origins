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

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Challenges;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.items.artifacts.Artifact;
import com.quasistellar.origins.items.artifacts.HornOfPlenty;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.ui.BuffIndicator;
import com.quasistellar.origins.utils.GLog;
import com.watabou.utils.Bundle;

public class Fatigue extends Buff implements Hero.Doom {

	private static final float STEP	= 10f;

	private float level;
	private float partialDamage;

	private float sum = 0f;

	private static final String LEVEL			= "level";
	private static final String PARTIALDAMAGE 	= "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( LEVEL, level );
		bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	@Override
	public boolean act() {

		if (target.isAlive()) {

			Hero hero = (Hero)target;

			boolean statusUpdated = false;

			if (isTired()) {

				partialDamage += STEP * target.HT / 1000f;

				if (partialDamage > 1) {
					target.damage((int) partialDamage, this);
					partialDamage -= (int) partialDamage;
				}

				if (level != 2) {
					GLog.n( Messages.get(this, "onfatigue") );
					statusUpdated = true;
					hero.interrupt();
				}

				level = 2;

			} else if (isRested()) {

				if (level != 0) {
					GLog.i( Messages.get(this, "onrested") );
					statusUpdated = true;
					hero.interrupt();
				}

				level = 0;

			} else {

				level = 1;

			}

			if (statusUpdated) {
				BuffIndicator.refreshHero();
			}
			
			spend( target.buff( Shadows.class ) == null ? STEP : STEP * 1.5f );

		} else {

			diactivate();

		}

		return true;
	}

	public void reduceSpirit( float energy ) {
		if (energy == Math.round(energy)) {
			if (Dungeon.hero.SP >= energy) {
				Dungeon.hero.SP -= energy;
			}
			else if (Dungeon.hero.SP < energy) {
				Dungeon.hero.damage( Math.round(energy - Dungeon.hero.SP), this );
				Dungeon.hero.SP = 0;
			}
			if (Dungeon.hero.SP < 0) {
				Dungeon.hero.SP = 0;
			} else if (Dungeon.hero.SP > Dungeon.hero.SM) {
				Dungeon.hero.SP = Dungeon.hero.SM;
			}

			BuffIndicator.refreshHero();
		}
		else {
			sum += energy;
			if (sum == Math.round(sum)) {
				reduceSpirit(sum);
				sum = 0;
				//im lazy, so 0.9 became 1.1 won't be finded
			}
		}
	}

	public boolean isTired() {
		return Dungeon.hero.SP == 0;
	}

	public boolean isRested() {
		return Dungeon.hero.SP >= Dungeon.hero.SM*0.9f;
	}

	@Override
	public int icon() {
		if (Dungeon.hero.SP >= 0.9f*Dungeon.hero.SM) {
			return BuffIndicator.RESTED;
		} else if (Dungeon.hero.SP == 0) {
			return BuffIndicator.FATIGUE;
		} else {
			return BuffIndicator.NONE;
		}
	}

	@Override
	public String toString() {
		if (Dungeon.hero.SP == 0) {
			return Messages.get(this, "fatigue");
		} else {
			return Messages.get(this, "rested");
		}
	}

	@Override
	public String desc() {
		String result;
		if (Dungeon.hero.SP == 0) {
			result = Messages.get(this, "desc_intro_fatigue");
		} else {
			result = Messages.get(this, "desc_intro_rested");
		}

		result += Messages.get(this, "desc");

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromFatigue();

		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
