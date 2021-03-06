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

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.items.BrokenSeal;
import com.quasistellar.origins.items.EquipableItem;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.armor.curses.AntiEntropy;
import com.quasistellar.origins.items.armor.curses.Corrosion;
import com.quasistellar.origins.items.armor.curses.Displacement;
import com.quasistellar.origins.items.armor.curses.Metabolism;
import com.quasistellar.origins.items.armor.curses.Multiplicity;
import com.quasistellar.origins.items.armor.curses.Stench;
import com.quasistellar.origins.items.armor.glyphs.Affection;
import com.quasistellar.origins.items.armor.glyphs.AntiMagic;
import com.quasistellar.origins.items.armor.glyphs.Brimstone;
import com.quasistellar.origins.items.armor.glyphs.Camouflage;
import com.quasistellar.origins.items.armor.glyphs.Entanglement;
import com.quasistellar.origins.items.armor.glyphs.Flow;
import com.quasistellar.origins.items.armor.glyphs.Obfuscation;
import com.quasistellar.origins.items.armor.glyphs.Potential;
import com.quasistellar.origins.items.armor.glyphs.Repulsion;
import com.quasistellar.origins.items.armor.glyphs.Stone;
import com.quasistellar.origins.items.armor.glyphs.Swiftness;
import com.quasistellar.origins.items.armor.glyphs.Thorns;
import com.quasistellar.origins.items.armor.glyphs.Viscosity;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.HeroSprite;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Armor extends EquipableItem {

	private static final int HITS_TO_KNOW    = 10;

	protected static final String AC_DETACH       = "DETACH";
	
	public int tier;
	
	private int hitsToKnow = HITS_TO_KNOW;
	
	public Glyph glyph;
	private BrokenSeal seal;
	
	public Armor( int tier ) {
		this.tier = tier;
	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";
	private static final String SEAL            = "seal";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( GLYPH, glyph );
		bundle.put( SEAL, seal);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		inscribe((Glyph) bundle.get(GLYPH));
		seal = (BrokenSeal)bundle.get(SEAL);
	}

	@Override
	public void reset() {
		super.reset();
		//armor can be kept in bones between runs, the seal cannot.
		seal = null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (seal != null) actions.add(AC_DETACH);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_DETACH) && seal != null){
			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			GLog.i( Messages.get(Armor.class, "detach_seal") );
			hero.sprite.operate(hero.pos);
			if (!seal.collect()){
				Dungeon.level.drop(seal, hero.pos);
			}
			seal = null;
		}
	}

	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);

		if (hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false )) {
			
			hero.belongings.armor = this;
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(Armor.class, "equip_cursed") );
			}
			
			((HeroSprite)hero.sprite).updateArmor();
			activate(hero);

			hero.spendAndNext( time2equip( hero ) );
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
			
		}
	}

	@Override
	public void activate(Char ch) {
		if (seal != null) Buff.affect(ch, BrokenSeal.WarriorShield.class).setArmor(this);
	}

	public void affixSeal(BrokenSeal seal){
		this.seal = seal;
		if (isEquipped(Dungeon.hero)){
			Buff.affect(Dungeon.hero, BrokenSeal.WarriorShield.class).setArmor(this);
		}
	}

	public BrokenSeal checkSeal(){
		return seal;
	}

	@Override
	protected float time2equip( Hero hero ) {
		return 2 / hero.speed();
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.belongings.armor = null;
			((HeroSprite)hero.sprite).updateArmor();

			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			return true;

		} else {

			return false;

		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.armor == this;
	}

	public int DRMax(){
		int effectiveTier = tier;
		if (glyph != null) effectiveTier += glyph.tierDRAdjust();
		effectiveTier = Math.max(0, effectiveTier);

		return Math.max(DRMin(), effectiveTier * (2 + Dungeon.hero.lvl));
	}

	public int DRMin(){
		if (glyph != null && glyph instanceof Stone)
			return 2*Dungeon.hero.lvl;
		else
			return Dungeon.hero.lvl;
	}

	public int proc( Char attacker, Char defender, int damage ) {
		
		if (glyph != null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}
		
		if (!isIdentified()) {
			if (--hitsToKnow <= 0) {
				identify();
				GLog.w( Messages.get(Armor.class, "identify") );
			}
		}
		
		return damage;
	}


	@Override
	public String name() {
		return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.name( super.name() ) : super.name();
	}
	
	@Override
	public String info() {
		String info = desc();
		
		if (isIdentified()) {
			info += "\n\n" + Messages.get(Armor.class, "curr_absorb", DRMin(), DRMax());
		} else {
			info += "\n\n" + Messages.get(Armor.class, "avg_absorb", DRMin(), DRMax());
		}
		
		if (glyph != null  && (cursedKnown || !glyph.curse())) {
			info += "\n\n" +  Messages.get(Armor.class, "inscribed", glyph.name());
			info += " " + glyph.desc();
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Armor.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Armor.class, "cursed");
		} else if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached");
		}
		
		return info;
	}

	@Override
	public Emitter emitter() {
		if (seal == null) return super.emitter();
		Emitter emitter = new Emitter();
		emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
		emitter.fillTarget = false;
		emitter.pour(Speck.factory( Speck.RED_LIGHT ), 0.6f);
		return emitter;
	}

	@Override
	public Item random() {

		//30% chance to be cursed
		//15% chance to be inscribed
		float effectRoll = Random.Float();
		if (effectRoll < 0.3f) {
			inscribe(Glyph.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.85f){
			inscribe();
		}

		return this;
	}
	
	@Override
	public int price() {
		if (seal != null) return 0;

		int price = 20 * tier;
		if (hasGoodGlyph()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseGlyph())) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public Armor inscribe( Glyph glyph ) {
		this.glyph = glyph;

		return this;
	}

	public Armor inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random();
		while (gl.getClass() == oldGlyphClass) {
			gl = Armor.Glyph.random();
		}

		return inscribe( gl );
	}

	public boolean hasGlyph(Class<?extends Glyph> type) {
		return glyph != null && glyph.getClass() == type;
	}

	public boolean hasGoodGlyph(){
		return glyph != null && !glyph.curse();
	}

	public boolean hasCurseGlyph(){
		return glyph != null && glyph.curse();
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{
				Obfuscation.class, Swiftness.class, Stone.class, Potential.class,
				Brimstone.class, Viscosity.class, Entanglement.class, Repulsion.class, Camouflage.class, Flow.class,
				Affection.class, AntiMagic.class, Thorns.class };
		private static final float[] chances= new float[]{
				10, 10, 10, 10,
				5, 5, 5, 5, 5, 5,
				2, 2, 2 };

		private static final Class<?>[] curses = new Class<?>[]{
				AntiEntropy.class, Corrosion.class, Displacement.class, Metabolism.class, Multiplicity.class, Stench.class
		};
			
		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );
		
		public String name() {
			if (!curse())
				return name( Messages.get(this, "glyph") );
			else
				return name( Messages.get(Item.class, "curse"));
		}
		
		public String name( String armorName ) {
			return Messages.get(this, "name", armorName);
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

		public int tierDRAdjust(){
			return 0;
		}

		public float tierSTRAdjust(){
			return 0;
		}

		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner instanceof Hero) {

				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "killed", name()) );

				Badges.validateDeathFromGlyph();
				return true;
				
			} else {
				return false;
			}
		}

		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				Origins.reportException(e);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public static Glyph randomCurse(){
			try {
				return ((Class<Glyph>)Random.oneOf(curses)).newInstance();
			} catch (Exception e) {
				Origins.reportException(e);
				return null;
			}
		}
		
	}
}
