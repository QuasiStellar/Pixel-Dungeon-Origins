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

package com.quasistellar.origins.items.weapon.melee;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.items.Dewdrop;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.Scheme;
import com.quasistellar.origins.items.trash.Blade;
import com.quasistellar.origins.items.trash.Glandule;
import com.quasistellar.origins.items.trash.Handle;
import com.quasistellar.origins.items.trash.Stone;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Foil extends MeleeWeapon {

	{
		image = ItemSpriteSheet.FOIL;

		tier = 3;
		type = 2;
	}

	@Override
	public int ave() {
		return  Math.round((1 +    //1 base, down from 4
				(Dungeon.hero.lvl*tier)/2)
				* (1 + 0.1f*level));   //scaling unchanged
	}

	public static class Break extends Scheme {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 1) return false;
			return (ingredients.get(0) instanceof Foil);
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 120;
		}

		@Override
		public ArrayList<Item> brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Blade());
			res.add(new Blade());
			res.add(new Handle());
			return res;
		}

		@Override
		public ArrayList<Item> sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Blade());
			res.add(new Blade());
			res.add(new Handle());
			return res;
		}
	}

	public static class Assemble extends Scheme {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 3) {
				return false;
			}
			if ((ingredients.get(0) instanceof Dirk) && (ingredients.get(1) instanceof Blade) && (ingredients.get(2) instanceof Glandule)) return true;
			else if ((ingredients.get(0) instanceof Dirk) && (ingredients.get(1) instanceof Glandule) && (ingredients.get(2) instanceof Blade)) return true;
			else if ((ingredients.get(0) instanceof Glandule) && (ingredients.get(1) instanceof Dirk) && (ingredients.get(2) instanceof Blade)) return true;
			else if ((ingredients.get(0) instanceof Glandule) && (ingredients.get(1) instanceof Blade) && (ingredients.get(2) instanceof Dirk)) return true;
			else if ((ingredients.get(0) instanceof Blade) && (ingredients.get(1) instanceof Dirk) && (ingredients.get(2) instanceof Glandule)) return true;
			else if ((ingredients.get(0) instanceof Blade) && (ingredients.get(1) instanceof Glandule) && (ingredients.get(2) instanceof Dirk)) return true;
			else return false;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 1000;
		}

		@Override
		public ArrayList<Item> brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Foil());
			res.add(new Dewdrop());
			res.add(new Dewdrop());
			return res;
		}

		@Override
		public ArrayList<Item> sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Foil());
			res.add(new Dewdrop());
			res.add(new Dewdrop());
			return res;
		}
	}
}
