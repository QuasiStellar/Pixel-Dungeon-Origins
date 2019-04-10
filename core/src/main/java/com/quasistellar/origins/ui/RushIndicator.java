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

package com.quasistellar.origins.ui;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Bleeding;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Combo;
import com.quasistellar.origins.actors.buffs.Cripple;
import com.quasistellar.origins.actors.buffs.EarthImbue;
import com.quasistellar.origins.actors.buffs.Fatigue;
import com.quasistellar.origins.actors.buffs.FireImbue;
import com.quasistellar.origins.actors.buffs.Paralysis;
import com.quasistellar.origins.actors.buffs.Vertigo;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.effects.Pushing;
import com.quasistellar.origins.effects.Wound;
import com.quasistellar.origins.effects.particles.ShadowParticle;
import com.quasistellar.origins.items.KindOfWeapon;
import com.quasistellar.origins.items.stones.StoneOfLoyalty;
import com.quasistellar.origins.items.stones.StoneOfRampage;
import com.quasistellar.origins.items.wands.WandOfBlastWave;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.weapon.melee.MeleeWeapon;
import com.quasistellar.origins.items.weapon.melee.Pike;
import com.quasistellar.origins.mechanics.Ballistica;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.CellSelector;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.sprites.MissileSprite;
import com.quasistellar.origins.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RushIndicator extends Tag {

	private Image icon;
	public KindOfWeapon wep = null;

	public RushIndicator() {
		super(0xFF4C4C);

		setSize( 24, 24 );

		visible = false;

	}

	@Override
	public void createChildren() {
		super.createChildren();
		if (Dungeon.hero.belongings.weapon != null) {
			Item item = Dungeon.hero.belongings.weapon;
			icon = new ItemSprite(item);
			add(icon);
		} else {
			icon = Icons.get(Icons.BACKPACK);
			add(icon);
		}
	}

	@Override
	protected void layout() {
		super.layout();

		icon.x = x+1 + (width - icon.width) / 2f;
		icon.y = y + (height - icon.height) / 2f;
		PixelScene.align(icon);
	}

	@Override
	protected void onClick() {
		if (Dungeon.hero.belongings.weapon != null) {
			switch (Dungeon.hero.belongings.weapon.type) {
				case 0:
					break;
				case 1:
					GameScene.selectCell( knuckle_rush );
					break;
				case 2:
					GameScene.selectCell( dagger_rush );
					break;
				case 3:
					GameScene.selectCell( sword_rush );
					break;
				case 4:
					GameScene.selectCell( axe_rush );
					break;
				case 5:
					GameScene.selectCell( mace_rush );
					break;
				case 6:
					GameScene.selectCell( polearm_rush );
					break;
				default:

			}
		}
	}

	protected static CellSelector.Listener knuckle_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !Dungeon.hero.canAttack(enemy)
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				Dungeon.hero.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			int dmg = Dungeon.hero.damageRoll();

			dmg -= enemy.drRoll();
			dmg = Dungeon.hero.attackProc(enemy, dmg);
			dmg = enemy.defenseProc(Dungeon.hero, dmg);
			enemy.damage( dmg, this );

			if (enemy.buff(FireImbue.class) != null)
				enemy.buff(FireImbue.class).proc(enemy);
			if (enemy.buff(EarthImbue.class) != null)
				enemy.buff(EarthImbue.class).proc(enemy);

			Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			enemy.sprite.bloodBurstA( enemy.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
			}

			if (Dungeon.hero.SP > 1 && enemy.isAlive()){
				Dungeon.hero.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});

				if (Dungeon.hero.belongings.weapon.hasStone(new StoneOfRampage())) {
					Dungeon.hero.buff(Fatigue.class).reduceSpirit(1);
				} else {
					Dungeon.hero.buff(Fatigue.class).reduceSpirit(2);
				}

			} else {

				Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());

			}

		}

		@Override
		public String prompt() {
			return Messages.get(this, "knuckle_rush_cell");
		}
	};

	protected static CellSelector.Listener dagger_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !Dungeon.hero.canAttack(enemy)
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				Dungeon.hero.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			int dmg;
			if (enemy.properties().contains(Char.Property.MINIBOSS) || enemy.properties().contains(Char.Property.BOSS)) {
				dmg = Dungeon.hero.damageRoll() * 3;
			} else {
				dmg = enemy.HP;
			}

			enemy.damage( dmg, this );

			Wound.hit( enemy );

			if (enemy.buff(FireImbue.class) != null)
				enemy.buff(FireImbue.class).proc(enemy);
			if (enemy.buff(EarthImbue.class) != null)
				enemy.buff(EarthImbue.class).proc(enemy);

			Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			enemy.sprite.bloodBurstA( enemy.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
			}

			if (Dungeon.hero.belongings.weapon.hasStone(new StoneOfRampage())) {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(4);
			} else {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
			}

			Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay() * 3);
		}

		@Override
		public String prompt() {
			return Messages.get(this, "dagger_rush_cell");
		}
	};

	protected static CellSelector.Listener sword_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !Dungeon.hero.canAttack(enemy)
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				Dungeon.hero.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			int dmg = Dungeon.hero.damageRoll();

			dmg -= enemy.drRoll();
			dmg = Dungeon.hero.attackProc(enemy, dmg);
			dmg = enemy.defenseProc(Dungeon.hero, dmg);
			enemy.damage( dmg, this );

			int oppositeDefender = enemy.pos + (enemy.pos - Dungeon.hero.pos);
			OldBallistica trajectory = new OldBallistica(enemy.pos, oppositeDefender, Ballistica.MAGIC_BOLT);
			WandOfBlastWave.throwChar(enemy, trajectory, 2);

			if (enemy.buff(FireImbue.class) != null)
				enemy.buff(FireImbue.class).proc(enemy);
			if (enemy.buff(EarthImbue.class) != null)
				enemy.buff(EarthImbue.class).proc(enemy);

			Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			enemy.sprite.bloodBurstA( enemy.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
			}

			if (Dungeon.hero.belongings.weapon.hasStone(new StoneOfRampage())) {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(4);
			} else {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
			}

			Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
		}

		@Override
		public String prompt() {
			return Messages.get(this, "sword_rush_cell");
		}
	};

	protected static CellSelector.Listener axe_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !Dungeon.hero.canAttack(enemy)
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				Dungeon.hero.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			int dmg = Dungeon.hero.damageRoll();

			dmg -= enemy.drRoll();
			dmg = Dungeon.hero.attackProc(enemy, dmg);
			dmg = enemy.defenseProc(Dungeon.hero, dmg);
			enemy.damage( dmg, this );

			Buff.affect( enemy, Bleeding.class ).set(dmg);

			if (enemy.buff(FireImbue.class) != null)
				enemy.buff(FireImbue.class).proc(enemy);
			if (enemy.buff(EarthImbue.class) != null)
				enemy.buff(EarthImbue.class).proc(enemy);

			Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			enemy.sprite.bloodBurstA( enemy.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
			}

			if (Dungeon.hero.belongings.weapon.hasStone(new StoneOfRampage())) {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(4);
			} else {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
			}

			Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
		}

		@Override
		public String prompt() {
			return Messages.get(this, "axe_rush_cell");
		}
	};

	protected static CellSelector.Listener mace_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !Dungeon.hero.canAttack(enemy)
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				Dungeon.hero.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			int dmg = Dungeon.hero.damageRoll();

			dmg -= enemy.drRoll();
			dmg = Dungeon.hero.attackProc(enemy, dmg);
			dmg = enemy.defenseProc(Dungeon.hero, dmg);
			enemy.damage( dmg, this );

			Buff.affect( enemy, Cripple.class, 5 );
			Buff.affect( enemy, Paralysis.class, 1 );

			if (enemy.buff(FireImbue.class) != null)
				enemy.buff(FireImbue.class).proc(enemy);
			if (enemy.buff(EarthImbue.class) != null)
				enemy.buff(EarthImbue.class).proc(enemy);

			Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			enemy.sprite.bloodBurstA( enemy.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
			}

			if (Dungeon.hero.belongings.weapon.hasStone(new StoneOfRampage())) {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(4);
			} else {
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
			}
			Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
		}

		@Override
		public String prompt() {
			return Messages.get(this, "mace_rush_cell");
		}
	};

	protected static CellSelector.Listener polearm_rush = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (!Dungeon.level.heroFOV[cell]){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
                Dungeon.hero.belongings.weapon.cast( Dungeon.hero, cell );
			}
		}

		@Override
		public String prompt() {
			return Messages.get(this, "polearm_rush_cell");
		}
	};

	@Override
	public void update() {
		if (!Dungeon.hero.isAlive())
			visible = false;
        else if (!visible && Dungeon.hero.belongings.weapon != null){
        	erase( icon );
			Item item = Dungeon.hero.belongings.weapon;
			icon = new ItemSprite(item);
			add( icon );
            visible = true;
            flash();
        }
        if (Dungeon.hero.belongings.weapon == null) {
        	visible = false;
		}
		if (visible) {
			if (Dungeon.hero.belongings.weapon != wep) {

				visible = false;

				wep = Dungeon.hero.belongings.weapon;
			}
		}
        super.update();
	}
}
