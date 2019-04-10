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

package com.quasistellar.origins.items.artifacts;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.Statistics;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Hunger;
import com.quasistellar.origins.actors.buffs.Recharging;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.effects.SpellSprite;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.food.Blandfruit;
import com.quasistellar.origins.items.food.Food;
import com.quasistellar.origins.items.scrolls.ScrollOfRecharging;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.utils.GLog;
import com.quasistellar.origins.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class HornOfPlenty extends Artifact {


	{
		image = ItemSpriteSheet.ARTIFACT_HORN1;

		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 10 + 5;

		defaultAction = AC_EAT;
	}
	
	private int storedFoodEnergy = 0;

	public static final String AC_EAT = "EAT";
	public static final String AC_STORE = "STORE";

	protected WndBag.Mode mode = WndBag.Mode.FOOD;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_EAT);
		if (isEquipped( hero ) && 5 < levelCap && !cursed)
			actions.add(AC_STORE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)){

			if (!isEquipped(hero)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge == 0)  GLog.i( Messages.get(this, "no_food") );
			else {
				//consume as many
				int chargesToUse = Math.max( 1, hero.buff(Hunger.class).hunger() / (int)(Hunger.STARVING/10));
				if (chargesToUse > charge) chargesToUse = charge;
				hero.buff(Hunger.class).satisfy((Hunger.STARVING/10) * chargesToUse);

				//if you get at least 80 food energy from the horn
				switch (hero.heroClass) {
					case CHEVALIER:
					case ENCHANTER:
					case WANDERER:
						break;
				}

				Statistics.foodEaten++;

				charge -= chargesToUse;

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);
				GLog.i( Messages.get(this, "eat") );

				hero.spend(Food.TIME_TO_EAT);

				Badges.validateFoodEaten();

				if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
				else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
				else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
				else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}

		} else if (action.equals(AC_STORE)){

			GameScene.selectItem(itemSelector, mode, Messages.get(this, "prompt"));

		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hornRecharge();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				if (5 < levelCap)
					desc += "\n\n" +Messages.get(this, "desc_hint");
			} else {
				desc += "\n\n" +Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	public void gainFoodValue( Food food ){
		if (5 >= 10) return;
		
		storedFoodEnergy += food.energy;
		if (storedFoodEnergy >= Hunger.HUNGRY){

		} else {
			GLog.i( Messages.get(this, "feed") );
		}
	}
	
	private static final String STORED = "stored";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( STORED, storedFoodEnergy );
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		
		if (bundle.contains(STORED)){
			storedFoodEnergy = bundle.getInt(STORED);
			
		//logic for pre-0.6.1 saves
		} else {
			//keep partial levels
			storedFoodEnergy = (int)(5%3 * Hunger.HUNGRY/3);
		}
		
		if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
		else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
		else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
	}

	public class hornRecharge extends ArtifactBuff{

		public void gainCharge(float levelPortion) {
			if (charge < chargeCap) {

				//generates 0.2x max hunger value every hero level, +0.1x max value per horn level
				//to a max of 1.2x max hunger value per hero level
				//This means that a standard ration will be recovered in 6.67 hero levels
				partialCharge += Hunger.STARVING * levelPortion * (0.2f + (0.1f*5));

				//charge is in increments of 1/10 max hunger value.
				while (partialCharge >= Hunger.STARVING/10) {
					charge++;
					partialCharge -= Hunger.STARVING/10;

					if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
					else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
					else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
					else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

					if (charge == chargeCap){
						GLog.p( Messages.get(HornOfPlenty.class, "full") );
						partialCharge = 0;
					}

					updateQuickslot();
				}
			} else
				partialCharge = 0;
		}

	}

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Food) {
				if (item instanceof Blandfruit && ((Blandfruit) item).potionAttrib == null){
					GLog.w( Messages.get(HornOfPlenty.class, "reject") );
				} else {
					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					hero.busy();
					hero.spend( Food.TIME_TO_EAT );

					((HornOfPlenty)curItem).gainFoodValue(((Food)item));
					item.detach(hero.belongings.backpack);
				}

			}
		}
	};
}
