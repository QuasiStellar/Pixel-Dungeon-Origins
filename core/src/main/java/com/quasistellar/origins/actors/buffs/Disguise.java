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

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Disguise extends Invisibility {

	{
		type = buffType.POSITIVE;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
	}
	
	@Override
	public boolean attachTo( Char target ) {
		return super.attachTo( target );
	}
	
	@Override
	public void detach() {
		super.detach();
	}
	
	@Override
	public boolean act() {
		spend( TICK );
		Dungeon.hero.buff(Fatigue.class).reduceSpirit(3);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.INVISIBLE;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.resetColor();
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
