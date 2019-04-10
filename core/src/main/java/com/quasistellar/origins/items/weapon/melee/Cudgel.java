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
import com.quasistellar.origins.items.Dewdrop;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.Scheme;
import com.quasistellar.origins.items.trash.Blade;
import com.quasistellar.origins.items.trash.Glandule;
import com.quasistellar.origins.items.trash.Handle;
import com.quasistellar.origins.items.trash.Stick;
import com.quasistellar.origins.items.trash.Stone;
import com.quasistellar.origins.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Cudgel extends MeleeWeapon {

	{
		image = ItemSpriteSheet.CUDGEL;

		tier = 2;
		type = 5;
		ACC = 0.9f; //0.9x accuracy
	}

	@Override
	public int ave() {
		return  Math.round((6 +        //6 base, up from 4
				(Dungeon.hero.lvl*tier+1)/2)
				* (1 + 0.1f*level));
	}

	public static class Break extends Scheme {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 1) return false;
			return (ingredients.get(0) instanceof Cudgel);
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 80;
		}

		@Override
		public ArrayList<Item> brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Stone());
			res.add(new Stick());
			res.add(new Dewdrop());
			return res;
		}

		@Override
		public ArrayList<Item> sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Stone());
			res.add(new Stick());
			res.add(new Dewdrop());
			return res;
		}
	}

	public static class Assemble extends Scheme {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 3) {
				return false;
			}
			if ((ingredients.get(0) instanceof Mace) && (ingredients.get(1) instanceof Stick) && (ingredients.get(2) instanceof Stone)) return true;
			else if ((ingredients.get(0) instanceof Mace) && (ingredients.get(1) instanceof Stone) && (ingredients.get(2) instanceof Stick)) return true;
			else if ((ingredients.get(0) instanceof Stone) && (ingredients.get(1) instanceof Mace) && (ingredients.get(2) instanceof Stick)) return true;
			else if ((ingredients.get(0) instanceof Stone) && (ingredients.get(1) instanceof Stick) && (ingredients.get(2) instanceof Mace)) return true;
			else if ((ingredients.get(0) instanceof Stick) && (ingredients.get(1) instanceof Mace) && (ingredients.get(2) instanceof Stone)) return true;
			else if ((ingredients.get(0) instanceof Stick) && (ingredients.get(1) instanceof Stone) && (ingredients.get(2) instanceof Mace)) return true;
			else return false;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 200;
		}

		@Override
		public ArrayList<Item> brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Mace());
			res.add(new Dewdrop());
			res.add(new Dewdrop());
			return res;
		}

		@Override
		public ArrayList<Item> sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			ArrayList<Item> res = new ArrayList<>();
			res.add(new Mace());
			res.add(new Dewdrop());
			res.add(new Dewdrop());
			return res;
		}
	}
}
