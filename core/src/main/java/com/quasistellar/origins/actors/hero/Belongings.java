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

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.KindOfWeapon;
import com.quasistellar.origins.items.KindofMisc;
import com.quasistellar.origins.items.armor.Armor;
import com.quasistellar.origins.items.bags.Bag;
import com.quasistellar.origins.items.keys.GoldenKey;
import com.quasistellar.origins.items.keys.IronKey;
import com.quasistellar.origins.items.keys.Key;
import com.quasistellar.origins.items.keys.SkeletonKey;
import com.quasistellar.origins.items.scrolls.ScrollOfRemoveCurse;
import com.quasistellar.origins.items.wands.Wand;
import com.quasistellar.origins.journal.Notes;
import com.quasistellar.origins.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 30;
	
	private Hero owner;
	
	public Bag backpack;

	public KindOfWeapon weapon = null;
	public Armor armor = null;
	public KindofMisc misc1 = null;
	public KindofMisc misc2 = null;
    public KindofMisc misc3 = null;
	
	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = Messages.get(Bag.class, "name");
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}
	
	private static final String WEAPON		= "weapon";
	private static final String ARMOR		= "armor";
	private static final String MISC1       = "misc1";
	private static final String MISC2       = "misc2";
    private static final String MISC3       = "misc3";

	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		
		bundle.put( WEAPON, weapon );
		bundle.put( ARMOR, armor );
		bundle.put( MISC1, misc1);
		bundle.put( MISC2, misc2);
        bundle.put( MISC3, misc3);
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		
		weapon = (KindOfWeapon) bundle.get(WEAPON);
		if (weapon != null) {
			weapon.activate(owner);
		}
		
		armor = (Armor)bundle.get( ARMOR );
		if (armor != null){
			armor.activate( owner );
		}
		
		misc1 = (KindofMisc)bundle.get(MISC1);
		if (misc1 != null) {
			misc1.activate( owner );
		}
		
		misc2 = (KindofMisc)bundle.get(MISC2);
		if (misc2 != null) {
			misc2.activate( owner );
		}

        misc3 = (KindofMisc)bundle.get(MISC3);
        if (misc3 != null) {
            misc3.activate( owner );
        }
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public Item getSimilar( Item similar ){
		
		for (Item item : this) {
			if (item.isSimilar(similar)) {
				return item;
			}
		}
		
		return null;
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {
		if (weapon != null) {
			weapon.identify();
		}
		if (armor != null) {
			armor.identify();
		}
		if (misc1 != null) {
			misc1.identify();
		}
		if (misc2 != null) {
			misc2.identify();
		}
        if (misc3 != null) {
            misc3.identify();
        }
		for (Item item : backpack) {
			item.cursedKnown = true;
		}
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, armor, weapon, misc1, misc2, misc3);
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect( int depth ) {

		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof Key) {
				if (((Key)item).depth == depth) {
					item.detachAll( backpack );
				}
			} else if (item.unique) {
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if (item instanceof Bag){
					((Bag)item).clear();
				}
				item.collect();
			} else if (!item.isEquipped( owner )) {
				item.detachAll( backpack );
			}
		}
		
		if (weapon != null) {
			weapon.cursed = false;
			weapon.activate( owner );
		}
		
		if (armor != null) {
			armor.cursed = false;
			armor.activate( owner );
		}
		
		if (misc1 != null) {
			misc1.cursed = false;
			misc1.activate( owner );
		}
		if (misc2 != null) {
			misc2.cursed = false;
			misc2.activate( owner );
		}
        if (misc3 != null) {
            misc3.cursed = false;
            misc3.activate( owner );
        }
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weapon, armor, misc1, misc2, misc3};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
			case 0:
				equipped[0] = weapon = null;
				break;
			case 1:
				equipped[1] = armor = null;
				break;
			case 2:
				equipped[2] = misc1 = null;
				break;
			case 3:
				equipped[3] = misc2 = null;
				break;
            case 4:
                equipped[4] = misc3 = null;
                break;
			default:
				backpackIterator.remove();
			}
		}
	}
}
