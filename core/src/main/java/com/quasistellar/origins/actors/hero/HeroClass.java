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

package com.quasistellar.origins.actors.hero;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Challenges;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.items.Torch;
import com.quasistellar.origins.items.armor.ClothArmor;
import com.quasistellar.origins.items.artifacts.DriedRose;
import com.quasistellar.origins.items.bags.VelvetPouch;
import com.quasistellar.origins.items.food.Food;
import com.quasistellar.origins.items.potions.PotionOfFrost;
import com.quasistellar.origins.items.potions.PotionOfLiquidFlame;
import com.quasistellar.origins.items.potions.PotionOfMindVision;
import com.quasistellar.origins.items.scrolls.ScrollOfIdentify;
import com.quasistellar.origins.items.scrolls.ScrollOfMagicMapping;
import com.quasistellar.origins.items.scrolls.ScrollOfMirrorImage;
import com.quasistellar.origins.items.scrolls.ScrollOfRecharging;
import com.quasistellar.origins.items.scrolls.ScrollOfRemoveCurse;
import com.quasistellar.origins.items.stones.StoneOfIndestructibility;
import com.quasistellar.origins.items.weapon.melee.Claymore;
import com.quasistellar.origins.items.weapon.melee.Hatchet;
import com.quasistellar.origins.items.weapon.melee.Mace;
import com.quasistellar.origins.items.weapon.melee.Shortsword;
import com.quasistellar.origins.items.weapon.melee.Staff;
import com.quasistellar.origins.items.weapon.melee.Stiletto;
import com.quasistellar.origins.items.weapon.melee.Knuckleduster;
import com.quasistellar.origins.items.weapon.melee.Pike;
import com.quasistellar.origins.items.weapon.missiles.darts.Dart;
import com.quasistellar.origins.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroClass {

	CHEVALIER( "chevalier" ),
	ENCHANTER( "enchanter" ),
	WANDERER( "wanderer" ),
	WARRIOR( "warrior" ),
	MAGE( "mage" ),
	ROGUE( "rogue" ),
	HUNTRESS( "huntress" );

	private String title;

	HeroClass( String title ) {
		this.title = title;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case CHEVALIER:
				initChevalier( hero );
				break;

			case ENCHANTER:
				initEnchanter( hero );
				break;

			case WANDERER:
				initWanderer( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
			(hero.belongings.armor = new ClothArmor()).identify();

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			new Food().identify().quantity(10).collect();
		new VelvetPouch().collect();

		new Torch().identify().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case CHEVALIER:
				return Badges.Badge.MASTERY_CHEVALIER;
			case ENCHANTER:
				return Badges.Badge.MASTERY_ENCHANTER;
			case WANDERER:
				return Badges.Badge.MASTERY_WANDERER;
		}
		return null;
	}

	private static void initChevalier( Hero hero ) {
		(hero.belongings.weapon = new Stiletto()).identify();
		new PotionOfLiquidFlame().identify();
		new ScrollOfMagicMapping().identify();

		//TODO: delete test items
		new ScrollOfIdentify().identify().quantity(10).collect();
	}

	private static void initEnchanter( Hero hero ) {
		(hero.belongings.weapon = new Staff()).identify();
		new PotionOfFrost().identify();
		new ScrollOfRecharging().identify();
	}

	private static void initWanderer( Hero hero ) {
		(hero.belongings.weapon = new Knuckleduster()).identify();
		new PotionOfMindVision().identify();
		new ScrollOfMirrorImage().identify();

		Dart darts = new Dart();
		darts.quantity(10).collect();

		Dungeon.quickslot.setSlot(0, darts);
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public String spritesheet() {
		
		switch (this) {
		case CHEVALIER:
			return Assets.CHEVALIER;
			case ENCHANTER:
			return Assets.ENCHANTER;
			case WANDERER:
			return Assets.WANDERER;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
			case CHEVALIER:
			return new String[]{
					Messages.get(HeroClass.class, "chevalier_perk1"),
					Messages.get(HeroClass.class, "chevalier_perk2"),
					Messages.get(HeroClass.class, "chevalier_perk3"),
					Messages.get(HeroClass.class, "chevalier_perk4"),
					Messages.get(HeroClass.class, "chevalier_perk5"),
			};
			case ENCHANTER:
			return new String[]{
					Messages.get(HeroClass.class, "enchanter_perk1"),
					Messages.get(HeroClass.class, "enchanter_perk2"),
					Messages.get(HeroClass.class, "enchanter_perk3"),
					Messages.get(HeroClass.class, "enchanter_perk4"),
					Messages.get(HeroClass.class, "enchanter_perk5"),
			};
			case WANDERER:
			return new String[]{
					Messages.get(HeroClass.class, "wanderer_perk1"),
					Messages.get(HeroClass.class, "wanderer_perk2"),
					Messages.get(HeroClass.class, "wanderer_perk3"),
					Messages.get(HeroClass.class, "wanderer_perk4"),
					Messages.get(HeroClass.class, "wanderer_perk5"),
			};
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : CHEVALIER;
	}
}
