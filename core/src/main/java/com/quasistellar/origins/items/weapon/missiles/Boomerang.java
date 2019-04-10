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

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.weapon.Weapon;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.sprites.MissileSprite;

import java.util.ArrayList;

public class Boomerang extends MissileWeapon {

	{
		image = ItemSpriteSheet.BOOMERANG;

		stackable = false;

		unique = true;
		bones = false;
		
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (!isEquipped(hero)) actions.add(AC_EQUIP);
		return actions;
	}

	@Override
	public int ave() {
		return  4 +     //half the base damage of a tier-1 weapon
				2 * Dungeon.hero.lvl;//scales the same as a tier 1 weapon
	}

	@Override
	public boolean isIdentified() {
		return cursedKnown;
	}

	@Override
	protected float durabilityPerUse() {
		return 0;
	}
	
	@Override
	public void rangedHit( Char enemy, int cell ) {
		circleBack(cell, curUser);
	}

	@Override
	protected void rangedMiss( int cell ) {
		circleBack( cell, curUser );
	}

	private void circleBack( int from, Hero owner ) {

		((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
				reset( from, owner.sprite, curItem, null );

		if (throwEquiped) {
			owner.belongings.weapon = this;
			owner.spend( -TIME_TO_EQUIP );
			Dungeon.quickslot.replacePlaceholder(this);
			updateQuickslot();
		} else
		if (!collect( curUser.belongings.backpack )) {
			Dungeon.level.drop( this, owner.pos ).sprite.drop();
		}
	}

	private boolean throwEquiped;

	@Override
	public void cast( Hero user, int dst ) {
		throwEquiped = isEquipped( user ) && !cursed;
		if (throwEquiped) Dungeon.quickslot.convertToPlaceholder(this);
		super.cast( user, dst );
	}
	
	@Override
	public String desc() {
		String info = super.desc();

		return info;
	}
}
