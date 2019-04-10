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

package com.quasistellar.origins.levels.rooms.special;

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.hero.Belongings;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.actors.mobs.npcs.Shopkeeper;
import com.quasistellar.origins.items.Bomb;
import com.quasistellar.origins.items.Generator;
import com.quasistellar.origins.items.Heap;
import com.quasistellar.origins.items.Honeypot;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.MerchantsBeacon;
import com.quasistellar.origins.items.Stylus;
import com.quasistellar.origins.items.Torch;
import com.quasistellar.origins.items.armor.LeatherArmor;
import com.quasistellar.origins.items.armor.MailArmor;
import com.quasistellar.origins.items.armor.PlateArmor;
import com.quasistellar.origins.items.armor.ScaleArmor;
import com.quasistellar.origins.items.artifacts.TimekeepersHourglass;
import com.quasistellar.origins.items.bags.Bag;
import com.quasistellar.origins.items.bags.PotionBandolier;
import com.quasistellar.origins.items.bags.ScrollHolder;
import com.quasistellar.origins.items.bags.VelvetPouch;
import com.quasistellar.origins.items.bags.WandHolster;
import com.quasistellar.origins.items.food.SmallRation;
import com.quasistellar.origins.items.potions.Potion;
import com.quasistellar.origins.items.potions.PotionOfHealing;
import com.quasistellar.origins.items.scrolls.Scroll;
import com.quasistellar.origins.items.scrolls.ScrollOfIdentify;
import com.quasistellar.origins.items.scrolls.ScrollOfMagicMapping;
import com.quasistellar.origins.items.scrolls.ScrollOfRemoveCurse;
import com.quasistellar.origins.items.wands.Wand;
import com.quasistellar.origins.items.weapon.missiles.Bolas;
import com.quasistellar.origins.items.weapon.missiles.FishingSpear;
import com.quasistellar.origins.items.weapon.missiles.Javelin;
import com.quasistellar.origins.items.weapon.missiles.Shuriken;
import com.quasistellar.origins.items.weapon.missiles.ThrowingHammer;
import com.quasistellar.origins.items.weapon.missiles.Tomahawk;
import com.quasistellar.origins.items.weapon.missiles.Trident;
import com.quasistellar.origins.items.weapon.missiles.darts.IncendiaryDart;
import com.quasistellar.origins.items.weapon.missiles.darts.TippedDart;
import com.quasistellar.origins.levels.Level;
import com.quasistellar.origins.levels.Terrain;
import com.quasistellar.origins.levels.painters.Painter;
import com.quasistellar.origins.plants.Plant;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShopRoom extends SpecialRoom {

	private ArrayList<Item> itemsToSpawn;
	
	@Override
	public int minWidth() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}
	
	@Override
	public int minHeight() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );

		placeShopkeeper( level );

		placeItems( level );
		
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

	}

	protected void placeShopkeeper( Level level ) {

		int pos = level.pointToCell(center());

		Mob shopkeeper = new Shopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );

	}

	protected void placeItems( Level level ){

		if (itemsToSpawn == null)
			itemsToSpawn = generateItems();

		Point itemPlacement = new Point(entrance());
		if (itemPlacement.y == top){
			itemPlacement.y++;
		} else if (itemPlacement.y == bottom) {
			itemPlacement.y--;
		} else if (itemPlacement.x == left){
			itemPlacement.x++;
		} else {
			itemPlacement.x--;
		}

		for (Item item : itemsToSpawn) {

			if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
				itemPlacement.y--;
			} else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
				itemPlacement.x++;
			} else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
				itemPlacement.y++;
			} else {
				itemPlacement.x--;
			}

			int cell = level.pointToCell(itemPlacement);

			if (level.heaps.get( cell ) != null) {
				do {
					cell = level.pointToCell(random());
				} while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
			}

			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
		}

	}
	
	protected static ArrayList<Item> generateItems() {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		
		switch (Dungeon.depth) {
		case 6:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Shuriken().quantity(2) :
					new IncendiaryDart().quantity(5));
			itemsToSpawn.add( new LeatherArmor().identify() );
			break;
			
		case 11:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new FishingSpear().quantity(2) :
					new Bolas().quantity(2));
			itemsToSpawn.add( new MailArmor().identify() );
			break;
			
		case 16:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Javelin().quantity(2) :
					new Tomahawk().quantity(2));
			itemsToSpawn.add( new ScaleArmor().identify() );
			break;
			
		case 21:
			itemsToSpawn.add( Random.Int(2) == 0 ?
					new Trident().quantity(2) :
					new ThrowingHammer().quantity(2));
			itemsToSpawn.add( new PlateArmor().identify() );
			itemsToSpawn.add( new Torch() );
			itemsToSpawn.add( new Torch() );
			itemsToSpawn.add( new Torch() );
			break;
		}
		
		itemsToSpawn.add( TippedDart.randomTipped() );

		itemsToSpawn.add( new MerchantsBeacon() );


		itemsToSpawn.add(ChooseBag(Dungeon.hero.belongings));


		itemsToSpawn.add( new PotionOfHealing() );
		for (int i=0; i < 3; i++)
			itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++)
			itemsToSpawn.add( Random.Int(2) == 0 ?
					Generator.random( Generator.Category.POTION ) :
					Generator.random( Generator.Category.SCROLL ) );


		itemsToSpawn.add( new SmallRation() );
		itemsToSpawn.add( new SmallRation() );

		itemsToSpawn.add( new Bomb().random() );
		switch (Random.Int(5)){
			case 1:
				itemsToSpawn.add( new Bomb() );
				break;
			case 2:
				itemsToSpawn.add( new Bomb().random() );
				break;
			case 3:
			case 4:
				itemsToSpawn.add( new Honeypot() );
				break;
		}

		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			switch (Dungeon.depth) {
				case 6:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
				case 11:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
				case 16:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
				case 21:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Item rare;
		switch (Random.Int(10)){
			case 0:
				rare = Generator.random( Generator.Category.WAND );
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				break;
			case 2:
				rare = Generator.random( Generator.Category.ARTIFACT );
				break;
			default:
				rare = new Stylus();
		}
		rare.cursed = rare.cursedKnown = false;
		itemsToSpawn.add( rare );

		//hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
		if (itemsToSpawn.size() > 63)
			throw new RuntimeException("Shop attempted to carry more than 63 items!");

		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	protected static Bag ChooseBag(Belongings pack){

		int seeds = 0, scrolls = 0, potions = 0, wands = 0;

		//count up items in the main bag, for bags which haven't yet been dropped.
		for (Item item : pack.backpack.items) {
			if (!Dungeon.LimitedDrops.SEED_POUCH.dropped() && item instanceof Plant.Seed)
				seeds++;
			else if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped() && item instanceof Scroll)
				scrolls++;
			else if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped() && item instanceof Potion)
				potions++;
			else if (!Dungeon.LimitedDrops.WAND_HOLSTER.dropped() && item instanceof Wand)
				wands++;
		}

		//then pick whichever valid bag has the most items available to put into it.
		//note that the order here gives a perference if counts are otherwise equal
		if (seeds >= scrolls && seeds >= potions && seeds >= wands && !Dungeon.LimitedDrops.SEED_POUCH.dropped()) {
			Dungeon.LimitedDrops.SEED_POUCH.drop();
			return new VelvetPouch();

		} else if (scrolls >= potions && scrolls >= wands && !Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) {
			Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
			return new ScrollHolder();

		} else if (potions >= wands && !Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) {
			Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
			return new PotionBandolier();

		} else if (!Dungeon.LimitedDrops.WAND_HOLSTER.dropped()) {
			Dungeon.LimitedDrops.WAND_HOLSTER.drop();
			return new WandHolster();
		}

		return null;
	}

}
