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

package com.quasistellar.origins.journal;

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.armor.ClothArmor;
import com.quasistellar.origins.items.armor.HuntressArmor;
import com.quasistellar.origins.items.armor.LeatherArmor;
import com.quasistellar.origins.items.armor.MageArmor;
import com.quasistellar.origins.items.armor.MailArmor;
import com.quasistellar.origins.items.armor.PlateArmor;
import com.quasistellar.origins.items.armor.RogueArmor;
import com.quasistellar.origins.items.armor.ScaleArmor;
import com.quasistellar.origins.items.armor.WarriorArmor;
import com.quasistellar.origins.items.artifacts.CapeOfThorns;
import com.quasistellar.origins.items.artifacts.ChaliceOfBlood;
import com.quasistellar.origins.items.artifacts.CloakOfShadows;
import com.quasistellar.origins.items.artifacts.DriedRose;
import com.quasistellar.origins.items.artifacts.EtherealChains;
import com.quasistellar.origins.items.artifacts.HornOfPlenty;
import com.quasistellar.origins.items.artifacts.LloydsBeacon;
import com.quasistellar.origins.items.artifacts.MasterThievesArmband;
import com.quasistellar.origins.items.artifacts.SandalsOfNature;
import com.quasistellar.origins.items.artifacts.TalismanOfForesight;
import com.quasistellar.origins.items.artifacts.TimekeepersHourglass;
import com.quasistellar.origins.items.artifacts.UnstableSpellbook;
import com.quasistellar.origins.items.potions.PotionOfExperience;
import com.quasistellar.origins.items.potions.PotionOfFrost;
import com.quasistellar.origins.items.potions.PotionOfHealing;
import com.quasistellar.origins.items.potions.PotionOfInvisibility;
import com.quasistellar.origins.items.potions.PotionOfLevitation;
import com.quasistellar.origins.items.potions.PotionOfLiquidFlame;
import com.quasistellar.origins.items.potions.PotionOfMight;
import com.quasistellar.origins.items.potions.PotionOfMindVision;
import com.quasistellar.origins.items.potions.PotionOfParalyticGas;
import com.quasistellar.origins.items.potions.PotionOfPurity;
import com.quasistellar.origins.items.potions.PotionOfStrength;
import com.quasistellar.origins.items.potions.PotionOfToxicGas;
import com.quasistellar.origins.items.rings.RingOfAccuracy;
import com.quasistellar.origins.items.rings.RingOfElements;
import com.quasistellar.origins.items.rings.RingOfEnergy;
import com.quasistellar.origins.items.rings.RingOfEvasion;
import com.quasistellar.origins.items.rings.RingOfForce;
import com.quasistellar.origins.items.rings.RingOfFuror;
import com.quasistellar.origins.items.rings.RingOfHaste;
import com.quasistellar.origins.items.rings.RingOfMight;
import com.quasistellar.origins.items.rings.RingOfSharpshooting;
import com.quasistellar.origins.items.rings.RingOfTenacity;
import com.quasistellar.origins.items.rings.RingOfWealth;
import com.quasistellar.origins.items.scrolls.ScrollOfIdentify;
import com.quasistellar.origins.items.scrolls.ScrollOfLullaby;
import com.quasistellar.origins.items.scrolls.ScrollOfMagicMapping;
import com.quasistellar.origins.items.scrolls.ScrollOfMagicalInfusion;
import com.quasistellar.origins.items.scrolls.ScrollOfMirrorImage;
import com.quasistellar.origins.items.scrolls.ScrollOfPsionicBlast;
import com.quasistellar.origins.items.scrolls.ScrollOfRage;
import com.quasistellar.origins.items.scrolls.ScrollOfRecharging;
import com.quasistellar.origins.items.scrolls.ScrollOfRemoveCurse;
import com.quasistellar.origins.items.scrolls.ScrollOfTeleportation;
import com.quasistellar.origins.items.scrolls.ScrollOfTerror;
import com.quasistellar.origins.items.scrolls.ScrollOfUpgrade;
import com.quasistellar.origins.items.wands.WandOfBlastWave;
import com.quasistellar.origins.items.wands.WandOfCorruption;
import com.quasistellar.origins.items.wands.WandOfDisintegration;
import com.quasistellar.origins.items.wands.WandOfFireblast;
import com.quasistellar.origins.items.wands.WandOfFrost;
import com.quasistellar.origins.items.wands.WandOfLightning;
import com.quasistellar.origins.items.wands.WandOfMagicMissile;
import com.quasistellar.origins.items.wands.WandOfPrismaticLight;
import com.quasistellar.origins.items.wands.WandOfRegrowth;
import com.quasistellar.origins.items.wands.WandOfTransfusion;
import com.quasistellar.origins.items.wands.WandOfCorrosion;
import com.quasistellar.origins.items.weapon.melee.BattleAxe;
import com.quasistellar.origins.items.weapon.melee.Broadsword;
import com.quasistellar.origins.items.weapon.melee.Claymore;
import com.quasistellar.origins.items.weapon.melee.Cudgel;
import com.quasistellar.origins.items.weapon.melee.Dirk;
import com.quasistellar.origins.items.weapon.melee.Flail;
import com.quasistellar.origins.items.weapon.melee.Foil;
import com.quasistellar.origins.items.weapon.melee.Glaive;
import com.quasistellar.origins.items.weapon.melee.Greataxe;
import com.quasistellar.origins.items.weapon.melee.Halberd;
import com.quasistellar.origins.items.weapon.melee.Hatchet;
import com.quasistellar.origins.items.weapon.melee.IronGauntlet;
import com.quasistellar.origins.items.weapon.melee.Knuckleduster;
import com.quasistellar.origins.items.weapon.melee.Mace;
import com.quasistellar.origins.items.weapon.melee.Pike;
import com.quasistellar.origins.items.weapon.melee.Shortsword;
import com.quasistellar.origins.items.weapon.melee.Staff;
import com.quasistellar.origins.items.weapon.melee.Stiletto;
import com.quasistellar.origins.items.weapon.melee.StuddedKnuckle;
import com.quasistellar.origins.items.weapon.missiles.Boomerang;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog {
	
	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS;
	
	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();
	
	public Collection<Class<? extends Item>> items(){
		return seen.keySet();
	}
	
	public boolean allSeen(){
		for (Class<?extends Item> item : items()){
			if (!seen.get(item)){
				return false;
			}
		}
		return true;
	}
	
	static {
		WEAPONS.seen.put( Staff.class,                      false);
		WEAPONS.seen.put( Knuckleduster.class,              false);
		WEAPONS.seen.put( StuddedKnuckle.class,             false);
		WEAPONS.seen.put( IronGauntlet.class,               false);
		WEAPONS.seen.put( Stiletto.class,                   false);
		WEAPONS.seen.put( Dirk.class,                       false);
		WEAPONS.seen.put( Foil.class,                       false);
		WEAPONS.seen.put( Shortsword.class,                 false);
		WEAPONS.seen.put( Broadsword.class,                 false);
		WEAPONS.seen.put( Claymore.class,                   false);
		WEAPONS.seen.put( Hatchet.class,                    false);
		WEAPONS.seen.put( BattleAxe.class,                  false);
		WEAPONS.seen.put( Greataxe.class,                   false);
		WEAPONS.seen.put( Mace.class,                       false);
		WEAPONS.seen.put( Cudgel.class,                     false);
		WEAPONS.seen.put( Flail.class,                      false);
		WEAPONS.seen.put( Pike.class,                       false);
		WEAPONS.seen.put( Glaive.class,                     false);
		WEAPONS.seen.put( Halberd.class,                    false);
	
		ARMOR.seen.put( ClothArmor.class,                   false);
		ARMOR.seen.put( LeatherArmor.class,                 false);
		ARMOR.seen.put( MailArmor.class,                    false);
		ARMOR.seen.put( ScaleArmor.class,                   false);
		ARMOR.seen.put( PlateArmor.class,                   false);
		ARMOR.seen.put( WarriorArmor.class,                 false);
		ARMOR.seen.put( MageArmor.class,                    false);
		ARMOR.seen.put( RogueArmor.class,                   false);
		ARMOR.seen.put( HuntressArmor.class,                false);
	
		WANDS.seen.put( WandOfMagicMissile.class,           false);
		WANDS.seen.put( WandOfLightning.class,              false);
		WANDS.seen.put( WandOfDisintegration.class,         false);
		WANDS.seen.put( WandOfFireblast.class,              false);
		WANDS.seen.put( WandOfCorrosion.class,              false);
		WANDS.seen.put( WandOfBlastWave.class,              false);
		//WANDS.seen.put( WandOfLivingEarth.class,          false);
		WANDS.seen.put( WandOfFrost.class,                  false);
		WANDS.seen.put( WandOfPrismaticLight.class,         false);
		//WANDS.seen.put( WandOfWarding.class,              false);
		WANDS.seen.put( WandOfTransfusion.class,            false);
		WANDS.seen.put( WandOfCorruption.class,             false);
		WANDS.seen.put( WandOfRegrowth.class,               false);
	
		RINGS.seen.put( RingOfAccuracy.class,               false);
		RINGS.seen.put( RingOfEnergy.class,                 false);
		RINGS.seen.put( RingOfElements.class,               false);
		RINGS.seen.put( RingOfEvasion.class,                false);
		RINGS.seen.put( RingOfForce.class,                  false);
		RINGS.seen.put( RingOfFuror.class,                  false);
		RINGS.seen.put( RingOfHaste.class,                  false);
		RINGS.seen.put( RingOfMight.class,                  false);
		RINGS.seen.put( RingOfSharpshooting.class,          false);
		RINGS.seen.put( RingOfTenacity.class,               false);
		RINGS.seen.put( RingOfWealth.class,                 false);
	
		//ARTIFACTS.seen.put( AlchemistsToolkit.class,      false);
		ARTIFACTS.seen.put( CapeOfThorns.class,             false);
		ARTIFACTS.seen.put( ChaliceOfBlood.class,           false);
		ARTIFACTS.seen.put( CloakOfShadows.class,           false);
		ARTIFACTS.seen.put( DriedRose.class,                false);
		ARTIFACTS.seen.put( EtherealChains.class,           false);
		ARTIFACTS.seen.put( HornOfPlenty.class,             false);
		ARTIFACTS.seen.put( LloydsBeacon.class,             false);
		ARTIFACTS.seen.put( MasterThievesArmband.class,     false);
		ARTIFACTS.seen.put( SandalsOfNature.class,          false);
		ARTIFACTS.seen.put( TalismanOfForesight.class,      false);
		ARTIFACTS.seen.put( TimekeepersHourglass.class,     false);
		ARTIFACTS.seen.put( UnstableSpellbook.class,        false);
	
		POTIONS.seen.put( PotionOfHealing.class,            false);
		POTIONS.seen.put( PotionOfStrength.class,           false);
		POTIONS.seen.put( PotionOfLiquidFlame.class,        false);
		POTIONS.seen.put( PotionOfFrost.class,              false);
		POTIONS.seen.put( PotionOfToxicGas.class,           false);
		POTIONS.seen.put( PotionOfParalyticGas.class,       false);
		POTIONS.seen.put( PotionOfPurity.class,             false);
		POTIONS.seen.put( PotionOfLevitation.class,         false);
		POTIONS.seen.put( PotionOfMindVision.class,         false);
		POTIONS.seen.put( PotionOfInvisibility.class,       false);
		POTIONS.seen.put( PotionOfExperience.class,         false);
		POTIONS.seen.put( PotionOfMight.class,              false);
	
		SCROLLS.seen.put( ScrollOfIdentify.class,           false);
		SCROLLS.seen.put( ScrollOfUpgrade.class,            false);
		SCROLLS.seen.put( ScrollOfRemoveCurse.class,        false);
		SCROLLS.seen.put( ScrollOfMagicMapping.class,       false);
		SCROLLS.seen.put( ScrollOfTeleportation.class,      false);
		SCROLLS.seen.put( ScrollOfRecharging.class,         false);
		SCROLLS.seen.put( ScrollOfMirrorImage.class,        false);
		SCROLLS.seen.put( ScrollOfTerror.class,             false);
		SCROLLS.seen.put( ScrollOfLullaby.class,            false);
		SCROLLS.seen.put( ScrollOfRage.class,               false);
		SCROLLS.seen.put( ScrollOfPsionicBlast.class,       false);
		SCROLLS.seen.put( ScrollOfMagicalInfusion.class,    false);
	}
	
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass)) {
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}
	
	private static final String CATALOGS = "catalogs";
	
	public static void store( Bundle bundle ){
		
		Badges.loadGlobal();
		
		ArrayList<String> seen = new ArrayList<>();
		
		//if we have identified all items of a set, we use the badge to keep track instead.
		if (!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)) {
			for (Catalog cat : values()) {
				if (!Badges.isUnlocked(catalogBadges.get(cat))) {
					for (Class<? extends Item> item : cat.items()) {
						if (cat.seen.get(item)) seen.add(item.getSimpleName());
					}
				}
			}
		}
		
		bundle.put( CATALOGS, seen.toArray(new String[0]) );
		
	}
	
	public static void restore( Bundle bundle ){
		
		Badges.loadGlobal();
		
		//logic for if we have all badges
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for ( Catalog cat : values()){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
			return;
		}
		
		//catalog-specific badge logic
		for (Catalog cat : values()){
			if (Badges.isUnlocked(catalogBadges.get(cat))){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		}
		
		//general save/load
		if (bundle.contains(CATALOGS)) {
			List<String> seen = Arrays.asList(bundle.getStringArray(CATALOGS));
			
			//pre-0.6.3 saves
			//TODO should adjust this to tie into the bundling system's class array
			if (seen.contains("WandOfVenom")){
				WANDS.seen.put(WandOfCorrosion.class, true);
			}
			
			for (Catalog cat : values()) {
				for (Class<? extends Item> item : cat.items()) {
					if (seen.contains(item.getSimpleName())) {
						cat.seen.put(item, true);
					}
				}
			}
		}
	}
	
}
