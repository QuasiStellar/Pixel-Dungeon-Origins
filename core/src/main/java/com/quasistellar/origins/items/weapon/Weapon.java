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

package com.quasistellar.origins.items.weapon;

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.items.Generator;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.KindOfWeapon;
import com.quasistellar.origins.items.rings.RingOfFuror;
import com.quasistellar.origins.items.stones.Runestone;
import com.quasistellar.origins.items.stones.StoneOfIndestructibility;
import com.quasistellar.origins.items.stones.StoneOfSwiftness;
import com.quasistellar.origins.items.stones.StoneOfWeighting;
import com.quasistellar.origins.items.weapon.curses.Annoying;
import com.quasistellar.origins.items.weapon.curses.Displacing;
import com.quasistellar.origins.items.weapon.curses.Exhausting;
import com.quasistellar.origins.items.weapon.curses.Fragile;
import com.quasistellar.origins.items.weapon.curses.Sacrificial;
import com.quasistellar.origins.items.weapon.curses.Wayward;
import com.quasistellar.origins.items.weapon.enchantments.Blazing;
import com.quasistellar.origins.items.weapon.enchantments.Chilling;
import com.quasistellar.origins.items.weapon.enchantments.Dazzling;
import com.quasistellar.origins.items.weapon.enchantments.Eldritch;
import com.quasistellar.origins.items.weapon.enchantments.Grim;
import com.quasistellar.origins.items.weapon.enchantments.Lucky;
import com.quasistellar.origins.items.weapon.enchantments.Projecting;
import com.quasistellar.origins.items.weapon.enchantments.Shocking;
import com.quasistellar.origins.items.weapon.enchantments.Stunning;
import com.quasistellar.origins.items.weapon.enchantments.Unstable;
import com.quasistellar.origins.items.weapon.enchantments.Vampiric;
import com.quasistellar.origins.items.weapon.enchantments.Venomous;
import com.quasistellar.origins.items.weapon.enchantments.Vorpal;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

abstract public class Weapon extends KindOfWeapon {

	private static final int HITS_TO_KNOW    = 20;

	public int durability = 100;
	public int tier;

	public Runestone stone1 = null;
	public Runestone stone2 = null;
	public Runestone stone3 = null;

	private static final String TXT_TO_STRING		= "%s :%d";

	public float    ACC = 1f;	// Accuracy modifier
	public float	DLY	= 1f;	// Speed modifier
	public int      RCH = 1;    // Reach modifier (only applies to melee hits)

	private int hitsToKnow = HITS_TO_KNOW;
	
	public Enchantment enchantment;
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		
		if (enchantment != null) {
			damage = enchantment.proc( this, attacker, defender, damage );
		}
		
		if (!isIdentified()) {
			if (--hitsToKnow <= 0) {
				identify();
				GLog.i( Messages.get(Weapon.class, "identify") );
			}
		}

		return damage;
	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String STONE1			= "stone1";
	private static final String STONE2			= "stone2";
	private static final String STONE3			= "stone3";
	private static final String DURABILITY		= "durability";
	private static final String LEVEL			= "level";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( STONE1, stone1 );
		bundle.put( STONE2, stone2 );
		bundle.put( STONE3, stone3 );
		bundle.put( DURABILITY, durability );
		bundle.put( LEVEL, level );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		stone1 = (Runestone)bundle.get( STONE1 );
		stone2 = (Runestone)bundle.get( STONE2 );
		stone3 = (Runestone)bundle.get( STONE3 );
		durability = bundle.getInt( DURABILITY );
		level = bundle.getInt( LEVEL );
	}
	
	@Override
	public float accuracyFactor( Char owner ) {
		
		int encumbrance = 0;

		if (hasEnchant(Wayward.class))
			encumbrance = Math.max(3, encumbrance+3);

		float ACC = this.ACC;

		return encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
	}
	
	@Override
	public float speedFactor( Char owner ) {

		float DLY = this.DLY;

		DLY = RingOfFuror.modifyAttackDelay(DLY, owner);

		if (this.hasStone(new StoneOfWeighting())) {
			DLY *= 1.5f;
		}
		if (this.hasStone(new StoneOfSwiftness())) {
			DLY *= 0.7f;
		}

		return DLY;
	}

	@Override
	public int reachFactor(Char owner) {
		return hasEnchant(Projecting.class) ? RCH+1 : RCH;
	}
	
	@Override
	public String name() {
		return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( super.name() ) : super.name();
	}
	
	@Override
	public Item random() {
		//30% chance to be cursed
		//10% chance to be enchanted
		float effectRoll = Random.Float();
		if (effectRoll < 0.3f) {
			enchant(Enchantment.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.9f){
			enchant();
		}
		durability = Random.Int(100)+1;
		float stone1Roll = Random.Float();
		if (stone1Roll >= 0.8) {
			Runestone st = (Runestone) Generator.random( Generator.Category.STONE );
			this.stone1 = st;
		}
		if (this.tier > 1) {
			float stone2Roll = Random.Float();
			if (stone2Roll >= 0.8) {
				do {
					Runestone st = (Runestone) Generator.random( Generator.Category.STONE );
					this.stone2 = st;
				} while (this.stone2 == this.stone1);
			}
			if (this.tier > 2) {
				float stone3Roll = Random.Float();
				if (stone3Roll >= 0.8) {
					do {
						Runestone st = (Runestone) Generator.random( Generator.Category.STONE );
						this.stone3 = st;
					} while (this.stone3 == this.stone1 || this.stone3 == this.stone2);
				}
			}
		}

		return this;
	}

	@Override
	public boolean hasStone( Runestone stone ) {
		if (this.stone1 != null && this.stone1.getClass() == stone.getClass() ) return true;
		if (this.stone2 != null && this.stone2.getClass() == stone.getClass() ) return true;
		if (this.stone3 != null && this.stone3.getClass() == stone.getClass() ) return true;
		return false;
	}
	
	public Weapon enchant( Enchantment ench ) {
		enchantment = ench;
		return this;
	}

	public Weapon enchant() {

		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while (ench.getClass() == oldEnchantment) {
			ench = Enchantment.random();
		}

		return enchant( ench );
	}

	public boolean hasEnchant(Class<?extends Enchantment> type) {
		return enchantment != null && enchantment.getClass() == type;
	}

	public boolean hasGoodEnchant(){
		return enchantment != null && !enchantment.curse();
	}

	public boolean hasCurseEnchant(){		return enchantment != null && enchantment.curse();
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.glowing() : null;
	}

	public static abstract class Enchantment implements Bundlable {

		private static final Class<?>[] enchants = new Class<?>[]{
			Blazing.class, Venomous.class, Vorpal.class, Shocking.class,
			Chilling.class, Eldritch.class, Lucky.class, Projecting.class, Unstable.class, Dazzling.class,
			Grim.class, Stunning.class, Vampiric.class,};
		private static final float[] chances= new float[]{
			10, 10, 10, 10,
			5, 5, 5, 5, 5, 5,
			2, 2, 2 };

		private static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Exhausting.class, Fragile.class, Sacrificial.class, Wayward.class
		};
			
		public abstract int proc( Weapon weapon, Char attacker, Char defender, int damage );

		public String name() {
			if (!curse())
				return name( Messages.get(this, "enchant"));
			else
				return name( Messages.get(Item.class, "curse"));
		}

		public String name( String weaponName ) {
			return Messages.get(this, "name", weaponName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();
		
		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				Origins.reportException(e);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse(){
			try {
				return ((Class<Enchantment>)Random.oneOf(curses)).newInstance();
			} catch (Exception e) {
				Origins.reportException(e);
				return null;
			}
		}
		
	}
}
