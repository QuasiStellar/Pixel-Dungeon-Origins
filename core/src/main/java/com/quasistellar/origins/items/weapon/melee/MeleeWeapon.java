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
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Disguise;
import com.quasistellar.origins.actors.buffs.Fatigue;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.items.Heap;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.armor.Armor;
import com.quasistellar.origins.items.artifacts.DriedRose;
import com.quasistellar.origins.items.stones.Runestone;
import com.quasistellar.origins.items.stones.StoneOfAcidity;
import com.quasistellar.origins.items.stones.StoneOfBanishment;
import com.quasistellar.origins.items.stones.StoneOfConquest;
import com.quasistellar.origins.items.stones.StoneOfElasticity;
import com.quasistellar.origins.items.stones.StoneOfElectricity;
import com.quasistellar.origins.items.stones.StoneOfFortune;
import com.quasistellar.origins.items.stones.StoneOfIndestructibility;
import com.quasistellar.origins.items.stones.StoneOfProtection;
import com.quasistellar.origins.items.stones.StoneOfSwiftness;
import com.quasistellar.origins.items.stones.StoneOfVengeance;
import com.quasistellar.origins.items.stones.StoneOfWeighting;
import com.quasistellar.origins.items.wands.WandOfBlastWave;
import com.quasistellar.origins.items.weapon.Weapon;
import com.quasistellar.origins.items.weapon.missiles.Boomerang;
import com.quasistellar.origins.mechanics.Ballistica;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.quasistellar.origins.ui.Window;
import com.quasistellar.origins.utils.GLog;
import com.quasistellar.origins.windows.IconTitle;
import com.quasistellar.origins.windows.WndBag;
import com.quasistellar.origins.windows.WndBlacksmith;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {

	@Override
	public int ave() {
		return  Math.round((4 +    //base
				(Dungeon.hero.lvl*tier)/2)
				* (1 + 0.1f*level));   //level scaling
	}

	@Override
	public void onThrow( int cell ) {
		if (this.type == 6 && Dungeon.hero.belongings.weapon == null) {
			Char enemy = Actor.findChar(cell);
			if (enemy == null || enemy == curUser) {
				super.onThrow(cell);
			} else {

				if (!curUser.shootPolearm(enemy, this)) {
					super.onThrow(cell);
				} else {

					Dungeon.level.drop(this, enemy.pos).sprite.drop();

				}
			}
			Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
		} else {
            Heap heap = Dungeon.level.drop( this, cell );
            if (!heap.isEmpty()) {
                heap.sprite.drop( cell );
            }
        }
	}
	
	@Override
	public int damageRoll(Char owner) {
		Char enemy = Dungeon.hero.enemy();
		int dmg = super.damageRoll(owner);
		if (this.type == 2 && enemy instanceof Mob && ((Mob) enemy).surprisedBy(Dungeon.hero)) {
			if (dmg < ave()) dmg = 2*ave() - dmg;
		}
		int damage = Math.min(dmg, Math.round(dmg * (durability + 50) / 100f));
		if (this.hasStone(new StoneOfBanishment()) && enemy.properties().contains(Char.Property.UNDEAD)) {
			damage = Math.round(damage * 1.3f);
		}
		if (this.hasStone(new StoneOfElectricity()) && enemy.properties().contains(Char.Property.INORGANIC)) {
			damage = Math.round(damage * 1.3f);
		}
		if (this.hasStone(new StoneOfConquest()) && (enemy.properties().contains(Char.Property.BOSS) || enemy.properties().contains(Char.Property.MINIBOSS))) {
			damage = Math.round(damage * 1.15f);
		}
		if (this.hasStone(new StoneOfWeighting())) {
			damage = Math.round(damage * 1.6667f);
		}
		if (this.hasStone(new StoneOfFortune()) && Random.Float()>0.9) {
			damage = Math.round(damage * 2);
		}
		if (this.hasStone(new StoneOfSwiftness())) {
			damage = Math.round(damage * 0.6667f);
		}
		if (this.hasStone(new StoneOfVengeance())) {
			damage = Math.round(damage * (1 + ((Dungeon.hero.HP - Dungeon.hero.HT) ^ 2) / (2 * Dungeon.hero.HT ^ 2)));
		}
		if (this.hasStone(new StoneOfElasticity()) && Random.Float()>0.9) {
			int oppositeDefender = enemy.pos + (enemy.pos - Dungeon.hero.pos);
			OldBallistica trajectory = new OldBallistica(enemy.pos, oppositeDefender, Ballistica.MAGIC_BOLT);
			WandOfBlastWave.throwChar(enemy, trajectory, 1);
		}
		if (!this.hasStone(new StoneOfIndestructibility()) || this instanceof Staff) {
			durability--;
			if (durability == 49) {
				GLog.w( Messages.get(this, "breaking", this.name()) );
			}
		}
		if (durability <= 0){
			Dungeon.hero.belongings.weapon = null;
			GLog.w( Messages.get(this, "broken", this.name()) );
		}
		return damage;
	}

	@Override
	public int defenseFactor( Char owner ) {
		if (this.hasStone(new StoneOfProtection())) {
			return tier;
		}
		return 0;
	}
	
	@Override
	public String info() {

		String info = desc();

		if (isIdentified()) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, ave());

		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, ave());
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
}
