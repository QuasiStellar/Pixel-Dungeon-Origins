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

import com.quasistellar.origins.Origins;
import com.quasistellar.origins.items.food.Blandfruit;
import com.quasistellar.origins.items.potions.Potion;
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
import com.quasistellar.origins.items.weapon.melee.MeleeWeapon;
import com.quasistellar.origins.items.weapon.melee.Pike;
import com.quasistellar.origins.items.weapon.melee.Shortsword;
import com.quasistellar.origins.items.weapon.melee.Stiletto;
import com.quasistellar.origins.items.weapon.melee.StuddedKnuckle;
import com.quasistellar.origins.items.weapon.missiles.darts.TippedDart;

import java.util.ArrayList;

public abstract class Scheme {
	
	public abstract boolean testIngredients(ArrayList<Item> ingredients);
	
	//not currently used
	public abstract int cost(ArrayList<Item> ingredients);
	
	public abstract ArrayList<Item> brew(ArrayList<Item> ingredients);
	
	public abstract ArrayList<Item> sampleOutput(ArrayList<Item> ingredients);
	
	//subclass for the common situation of a recipe with static inputs and outputs
	public static abstract class SimpleRecipe extends Scheme {
		
		//*** These elements must be filled in by subclasses
		protected Class<?extends Item>[] inputs;
		protected int[] inQuantity;
		
		protected int cost;
		
		protected Class<?extends Item> output1;
		protected Class<?extends Item> output2;
		protected Class<?extends Item> output3;
		protected int outQuantity;
		//***
		
		@Override
		public final boolean testIngredients(ArrayList<Item> ingredients) {
			boolean found;
			for(int i = 0; i < inputs.length; i++){
				found = false;
				for (Item ingredient : ingredients){
					if (ingredient.getClass() == inputs[i]
							&& ingredient.quantity() >= inQuantity[i]){
						found = true;
						break;
					}
				}
				if (!found){
					return false;
				}
			}
			return true;
		}
		
		public final int cost(ArrayList<Item> ingredients){
			return cost;
		}
		
		@Override
		public final ArrayList<Item> brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			for(int i = 0; i < inputs.length; i++){
				for (Item ingredient : ingredients){
					if (ingredient.getClass() == inputs[i]){
						ingredient.quantity( ingredient.quantity()-inQuantity[i]);
						break;
					}
				}
			}
			
			//sample output and real output are identical in this case.
			return sampleOutput(null);
		}
		
		//ingredients are ignored, as output doesn't vary
		public final ArrayList<Item> sampleOutput(ArrayList<Item> ingredients){
			try {
				Item result1 = output1.newInstance();
				Item result2 = output2.newInstance();
				Item result3 = output3.newInstance();
				ArrayList<Item> res = new ArrayList<>();
				res.add(result1);
				if (result2 != null) {
					res.add(result2);
				} else {
					res.add(null);
				}
				if (result3 != null) {
					res.add(result3);
				} else {
					res.add(null);
				}
				return res;
			} catch (Exception e) {
				Origins.reportException( e );
				return null;
			}
		}
	}
	
	
	//*******
	// Static members
	//*******
	
	private static Scheme[] oneIngredientRecipes = new Scheme[]{
		new Knuckleduster.Break(),
		new StuddedKnuckle.Break(),
		new IronGauntlet.Break(),
		new Stiletto.Break(),
		new Dirk.Break(),
		new Foil.Break(),
		new Shortsword.Break(),
		new Broadsword.Break(),
		new Claymore.Break(),
		new Hatchet.Break(),
		new BattleAxe.Break(),
		new Greataxe.Break(),
		new Mace.Break(),
		new Cudgel.Break(),
		new Flail.Break(),
		new Pike.Break(),
		new Glaive.Break(),
		new Halberd.Break()
	};
	
	private static Scheme[] twoIngredientRecipes = new Scheme[]{
//		new Knuckleduster.Unite(),
//		new StuddedKnuckle.Unite(),
//		new IronGauntlet.Unite(),
//		new Stiletto.Unite(),
//		new Dirk.Unite(),
//		new Foil.Unite(),
//		new Shortsword.Unite(),
//		new Broadsword.Unite(),
//		new Claymore.Unite(),
//		new Hatchet.Unite(),
//		new BattleAxe.Unite(),
//		new Greataxe.Unite(),
//		new Mace.Unite(),
//		new Cudgel.Unite(),
//		new Flail.Unite(),
//		new Pike.Unite(),
//		new Glaive.Unite(),
//		new Halberd.Unite(),
	};
	
	private static Scheme[] threeIngredientRecipes = new Scheme[]{
		new Knuckleduster.Assemble(),
		new StuddedKnuckle.Assemble(),
		new IronGauntlet.Assemble(),
		new Stiletto.Assemble(),
		new Dirk.Assemble(),
		new Foil.Assemble(),
		new Shortsword.Assemble(),
		new Broadsword.Assemble(),
		new Claymore.Assemble(),
		new Hatchet.Assemble(),
		new BattleAxe.Assemble(),
		new Greataxe.Assemble(),
		new Mace.Assemble(),
		new Cudgel.Assemble(),
		new Flail.Assemble(),
		new Pike.Assemble(),
		new Glaive.Assemble(),
		new Halberd.Assemble()
	};
	
	public static Scheme findRecipe(ArrayList<Item> ingredients){
		
		if (ingredients.size() == 1){
			for (Scheme recipe : oneIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 2){
			for (Scheme recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 3){
			for (Scheme recipe : threeIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
		}
		
		return null;
	}
	
}


