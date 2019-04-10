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

package com.quasistellar.origins.items.armor;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.buffs.Invisibility;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.items.BrokenSeal;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_SPECIAL = "SPECIAL";
	
	{
		cursedKnown = true;
		defaultAction = AC_SPECIAL;

		bones = false;
	}

	private int armorTier;
	
	public ClassArmor() {
		super( 6 );
	}
	
	public static ClassArmor upgrade ( Hero owner, Armor armor ) {
		
		ClassArmor classArmor = null;
		
		switch (owner.heroClass) {
		case CHEVALIER:
			classArmor = new RogueArmor();
			break;
		case ENCHANTER:
			classArmor = new MageArmor();
			break;
		case WANDERER:
			classArmor = new HuntressArmor();
			break;
		}

		classArmor.armorTier = armor.tier;
		classArmor.inscribe( armor.glyph );
		classArmor.identify();
		
		return classArmor;
	}

	private static final String ARMOR_TIER	= "armortier";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, armorTier );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		armorTier = bundle.getInt( ARMOR_TIER );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.HP >= 3 && isEquipped( hero )) {
			actions.add( AC_SPECIAL );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_SPECIAL)) {
			
			if (hero.HP < 3) {
				GLog.w( Messages.get(this, "low_hp") );
			} else if (!isEquipped( hero )) {
				GLog.w( Messages.get(this, "not_equipped") );
			} else {
				curUser = hero;
				Invisibility.dispel();
				doSpecial();
			}
			
		}
	}

	abstract public void doSpecial();

	@Override
	public int DRMax(){
		int effectiveTier = armorTier;
		if (glyph != null) effectiveTier += glyph.tierDRAdjust();
		effectiveTier = Math.max(0, effectiveTier);
		
		return Math.max(DRMin(), effectiveTier * (2 + Dungeon.hero.lvl));
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 0;
	}

}
