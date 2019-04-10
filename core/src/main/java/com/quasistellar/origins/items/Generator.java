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

package com.quasistellar.origins.items;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.items.armor.Armor;
import com.quasistellar.origins.items.armor.ClothArmor;
import com.quasistellar.origins.items.armor.LeatherArmor;
import com.quasistellar.origins.items.armor.MailArmor;
import com.quasistellar.origins.items.armor.PlateArmor;
import com.quasistellar.origins.items.armor.ScaleArmor;
import com.quasistellar.origins.items.armor.glyphs.Stone;
import com.quasistellar.origins.items.artifacts.AlchemistsToolkit;
import com.quasistellar.origins.items.artifacts.Artifact;
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
import com.quasistellar.origins.items.bags.Bag;
import com.quasistellar.origins.items.food.Food;
import com.quasistellar.origins.items.food.MysteryMeat;
import com.quasistellar.origins.items.food.Pasty;
import com.quasistellar.origins.items.potions.Potion;
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
import com.quasistellar.origins.items.rings.Ring;
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
import com.quasistellar.origins.items.scrolls.Scroll;
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
import com.quasistellar.origins.items.stones.Runestone;
import com.quasistellar.origins.items.stones.StoneOfAcidity;
import com.quasistellar.origins.items.stones.StoneOfBanishment;
import com.quasistellar.origins.items.stones.StoneOfConquest;
import com.quasistellar.origins.items.stones.StoneOfCruelty;
import com.quasistellar.origins.items.stones.StoneOfDestruction;
import com.quasistellar.origins.items.stones.StoneOfDullness;
import com.quasistellar.origins.items.stones.StoneOfElasticity;
import com.quasistellar.origins.items.stones.StoneOfElectricity;
import com.quasistellar.origins.items.stones.StoneOfFortune;
import com.quasistellar.origins.items.stones.StoneOfGrace;
import com.quasistellar.origins.items.stones.StoneOfGreed;
import com.quasistellar.origins.items.stones.StoneOfHolocaust;
import com.quasistellar.origins.items.stones.StoneOfIndestructibility;
import com.quasistellar.origins.items.stones.StoneOfLoyalty;
import com.quasistellar.origins.items.stones.StoneOfParry;
import com.quasistellar.origins.items.stones.StoneOfPenetration;
import com.quasistellar.origins.items.stones.StoneOfProtection;
import com.quasistellar.origins.items.stones.StoneOfRampage;
import com.quasistellar.origins.items.stones.StoneOfSacrifice;
import com.quasistellar.origins.items.stones.StoneOfSharpness;
import com.quasistellar.origins.items.stones.StoneOfSwiftness;
import com.quasistellar.origins.items.stones.StoneOfTransfusion;
import com.quasistellar.origins.items.stones.StoneOfVengeance;
import com.quasistellar.origins.items.stones.StoneOfWeighting;
import com.quasistellar.origins.items.wands.Wand;
import com.quasistellar.origins.items.wands.WandOfBlastWave;
import com.quasistellar.origins.items.wands.WandOfCorrosion;
import com.quasistellar.origins.items.wands.WandOfCorruption;
import com.quasistellar.origins.items.wands.WandOfDisintegration;
import com.quasistellar.origins.items.wands.WandOfFireblast;
import com.quasistellar.origins.items.wands.WandOfFrost;
import com.quasistellar.origins.items.wands.WandOfLightning;
import com.quasistellar.origins.items.wands.WandOfMagicMissile;
import com.quasistellar.origins.items.wands.WandOfPrismaticLight;
import com.quasistellar.origins.items.wands.WandOfRegrowth;
import com.quasistellar.origins.items.wands.WandOfTransfusion;
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
import com.quasistellar.origins.items.weapon.melee.Pike;
import com.quasistellar.origins.items.weapon.melee.Mace;
import com.quasistellar.origins.items.weapon.melee.Staff;
import com.quasistellar.origins.items.weapon.melee.MeleeWeapon;
import com.quasistellar.origins.items.weapon.melee.Shortsword;
import com.quasistellar.origins.items.weapon.melee.Stiletto;
import com.quasistellar.origins.items.weapon.melee.StuddedKnuckle;
import com.quasistellar.origins.items.weapon.missiles.Bolas;
import com.quasistellar.origins.items.weapon.missiles.FishingSpear;
import com.quasistellar.origins.items.weapon.missiles.Javelin;
import com.quasistellar.origins.items.weapon.missiles.MissileWeapon;
import com.quasistellar.origins.items.weapon.missiles.Shuriken;
import com.quasistellar.origins.items.weapon.missiles.ThrowingHammer;
import com.quasistellar.origins.items.weapon.missiles.ThrowingKnife;
import com.quasistellar.origins.items.weapon.missiles.Tomahawk;
import com.quasistellar.origins.items.weapon.missiles.Trident;
import com.quasistellar.origins.items.weapon.missiles.darts.Dart;
import com.quasistellar.origins.items.weapon.missiles.darts.IncendiaryDart;
import com.quasistellar.origins.items.weapon.missiles.darts.ParalyticDart;
import com.quasistellar.origins.plants.BlandfruitBush;
import com.quasistellar.origins.plants.Blindweed;
import com.quasistellar.origins.plants.Dreamfoil;
import com.quasistellar.origins.plants.Earthroot;
import com.quasistellar.origins.plants.Fadeleaf;
import com.quasistellar.origins.plants.Firebloom;
import com.quasistellar.origins.plants.Icecap;
import com.quasistellar.origins.plants.Plant;
import com.quasistellar.origins.plants.Rotberry;
import com.quasistellar.origins.plants.Sorrowmoss;
import com.quasistellar.origins.plants.Starflower;
import com.quasistellar.origins.plants.Stormvine;
import com.quasistellar.origins.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 0,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		POTION	( 20,   Potion.class ),
		SCROLL	( 20,   Scroll.class ),

		STONE   ( 20,   Runestone.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		SEED	( 0,    Plant.Seed.class ),
		
		FOOD	( 0,    Food.class ),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfIdentify.class,
					ScrollOfTeleportation.class,
					ScrollOfRemoveCurse.class,
					ScrollOfUpgrade.class,
					ScrollOfRecharging.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfTerror.class,
					ScrollOfLullaby.class,
					ScrollOfMagicalInfusion.class,
					ScrollOfPsionicBlast.class,
					ScrollOfMirrorImage.class };
			SCROLL.probs = new float[]{ 30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10 };
			
			POTION.classes = new Class<?>[]{
					PotionOfHealing.class,
					PotionOfExperience.class,
					PotionOfToxicGas.class,
					PotionOfParalyticGas.class,
					PotionOfLiquidFlame.class,
					PotionOfLevitation.class,
					PotionOfStrength.class,
					PotionOfMindVision.class,
					PotionOfPurity.class,
					PotionOfInvisibility.class,
					PotionOfMight.class,
					PotionOfFrost.class };
			POTION.probs = new float[]{ 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10 };

			STONE.classes = new Class<?>[]{
					StoneOfAcidity.class,
					StoneOfBanishment.class,
					StoneOfConquest.class,
					StoneOfCruelty.class,
					StoneOfDestruction.class,
					StoneOfDullness.class,
					StoneOfElasticity.class,
					StoneOfElectricity.class,
					StoneOfFortune.class,
					StoneOfGrace.class,
					StoneOfGreed.class,
					StoneOfHolocaust.class,
					StoneOfIndestructibility.class,
					StoneOfLoyalty.class,
					StoneOfParry.class,
					StoneOfPenetration.class,
					StoneOfProtection.class,
					StoneOfRampage.class,
					StoneOfSacrifice.class,
					StoneOfSharpness.class,
					StoneOfSwiftness.class,
					StoneOfTransfusion.class,
					StoneOfVengeance.class,
					StoneOfWeighting.class };
			STONE.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

			//TODO: add last ones when implemented
			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					//WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					//WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					Knuckleduster.class,
					Stiletto.class,
					Shortsword.class,
					Hatchet.class,
					Mace.class,
					Pike.class,

					Staff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					StuddedKnuckle.class,
					Dirk.class,
					Broadsword.class,
					BattleAxe.class,
					Cudgel.class,
					Glaive.class
			};
			WEP_T2.probs = new float[]{ 1, 1, 1, 1, 1, 1 };
			
			WEP_T3.classes = new Class<?>[]{
					IronGauntlet.class,
					Foil.class,
					Claymore.class,
					Greataxe.class,
					Flail.class,
					Halberd.class
			};
			WEP_T3.probs = new float[]{ 1, 1, 1, 1, 1, 1 };

			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					Dart.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 1, 1 };
			
			MIS_T2.classes = new Class<?>[]{
					Shuriken.class,
					IncendiaryDart.class,
					ParalyticDart.class,
			};
			MIS_T2.probs = new float[]{ 8, 3, 3 };
			
			MIS_T3.classes = new Class<?>[]{
					FishingSpear.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 4, 3 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class
			};
			MIS_T4.probs = new float[]{ 4, 3 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class
			};
			MIS_T5.probs = new float[]{ 4, 3 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
			
			SEED.classes = new Class<?>[]{
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Blindweed.Seed.class,
					Sungrass.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Rotberry.Seed.class,
					BlandfruitBush.Seed.class,
					Dreamfoil.Seed.class,
					Stormvine.Seed.class,
					Starflower.Seed.class};
			SEED.probs = new float[]{ 10, 10, 10, 10, 10, 10, 10, 0, 2, 10, 10, 1 };
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};

	private static final float[][] weaponFloorSetTierProbs = new float[][] {
			{100, 0, 0},
			{90, 10, 0},
			{80, 20, 0},
			{60, 30, 10},
			{30, 50, 20}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		try {
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			Origins.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			Origins.reportException(e);
			return null;
			
		}
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			Origins.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth);
	}
	
	public static MeleeWeapon randomWeapon(int floor) {

		int floorSet = (int)GameMath.gate(0, floor / 5, weaponFloorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(weaponFloorSetTierProbs[floorSet])];
			MeleeWeapon w = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			boolean up = true;
			int lvl = 0;
			while (up && lvl <= (floorSet+1)) {
				if (Random.Float() < (3f*floor+(5f/(floorSet+1f)))/100f) {
					w.upgrade();
					lvl += 1;
				} else up = false;
			}
			return w;
		} catch (Exception e) {
			Origins.reportException(e);
			return null;
		}
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		try {
			Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
			MissileWeapon w = (MissileWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			Origins.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}
			
			Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

			if (removeArtifact(art)) {
				Artifact artifact = art.newInstance();
				
				artifact.random();
				
				return artifact;
			} else {
				return null;
			}

		} catch (Exception e) {
			Origins.reportException(e);
			return null;
		}
	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		if (bundle.contains(SPAWNED_ARTIFACTS)){
			for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
				removeArtifact(artifact);
			}
		//pre-0.6.1 saves
		} else if (bundle.contains("artifacts")) {
			String[] names = bundle.getStringArray("artifacts");
			Category cat = Category.ARTIFACT;

			for (String artifact : names)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
