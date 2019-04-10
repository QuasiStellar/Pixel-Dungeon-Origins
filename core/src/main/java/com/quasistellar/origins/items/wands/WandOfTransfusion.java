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

package com.quasistellar.origins.items.wands;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Charm;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.effects.Beam;
import com.quasistellar.origins.effects.CellEmitter;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.effects.particles.BloodParticle;
import com.quasistellar.origins.effects.particles.LeafParticle;
import com.quasistellar.origins.effects.particles.ShadowParticle;
import com.quasistellar.origins.items.Generator;
import com.quasistellar.origins.items.Heap;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.rings.Ring;
import com.quasistellar.origins.levels.Terrain;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.plants.Plant;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.sprites.CharSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.tiles.DungeonTilemap;
import com.quasistellar.origins.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfTransfusion extends Wand {

	{
		image = ItemSpriteSheet.WAND_TRANSFUSION;

		collisionProperties = OldBallistica.PROJECTILE;
	}

	private boolean freeCharge = false;

	@Override
	protected void onZap(OldBallistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);
		Heap heap = Dungeon.level.heaps.get(cell);

		//this wand does a bunch of different things depending on what it targets.

		//if we find a character..
		if (ch != null && ch instanceof Mob){

			processSoulMark(ch, chargesPerCast());

			//heals an ally, or a charmed enemy
			if (ch.alignment == Char.Alignment.ALLY || ch.buff(Charm.class) != null){

				int missingHP = ch.HT - ch.HP;
				//heals 30%+3%*lvl missing HP.
				int healing = (int)Math.ceil((missingHP * (0.30f+(0.03f*5))));
				ch.HP += healing;
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + 5 / 2);
				ch.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing);

			//harms the undead
			} else if (ch.properties().contains(Char.Property.UNDEAD)){

				//deals 30%+5%*lvl total HP.
				int damage = (int) Math.ceil(ch.HT*(0.3f+(0.05f*5)));
				ch.damage(damage, this);
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + 5);
				Sample.INSTANCE.play(Assets.SND_BURNING);

			//charms an enemy
			} else {

				float duration = 5+5;
				Buff.affect(ch, Charm.class, duration).object = curUser.id();

				duration *= Random.Float(0.75f, 1f);
				Buff.affect(curUser, Charm.class, duration).object = ch.id();

				ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
				curUser.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

			}


		//if we find an item...
		} else if (heap != null && heap.type == Heap.Type.HEAP){
			Item item = heap.peek();

			//30% + 10%*lvl chance to uncurse the item and reset it to base level if degraded.
			if (item != null && Random.Float() <= 0.3f+5*0.1f){
				if (item.cursed){
					item.cursed = false;
					CellEmitter.get(cell).start( ShadowParticle.UP, 0.05f, 10 );
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}

				int lvldiffFromBase = 5 - (item instanceof Ring ? 1 : 0);
				if (lvldiffFromBase < 0){
					CellEmitter.get(cell).start(Speck.factory(Speck.UP), 0.2f, 3);
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}
			}

		//if we find some trampled grass...
		} else if (Dungeon.level.map[cell] == Terrain.GRASS) {

			//regrow one grass tile, suuuuuper useful...
			Dungeon.level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
			CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);

		//If we find embers...
		} else if (Dungeon.level.map[cell] == Terrain.EMBERS) {

			//30% + 3%*lvl chance to grow a random plant, or just regrow grass.
			if (Random.Float() <= 0.3f+5*0.03f) {
				Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 8);
				GameScene.updateMap(cell);
			} else{
				Dungeon.level.set(cell, Terrain.HIGH_GRASS);
				GameScene.updateMap(cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}

		} else
			return; //don't damage the hero if we can't find a target;

		if (!freeCharge) {
			damageHero();
		} else {
			freeCharge = false;
		}
	}

	//this wand costs health too
	private void damageHero(){
		// 10% of max hp
		int damage = (int)Math.ceil(curUser.HT*0.10f);
		curUser.damage(damage, this);

		if (!curUser.isAlive()){
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "ondeath") );
		}
	}

	@Override
	protected int initialCharges() {
		return 1;
	}

	@Override
	protected void fx(OldBallistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

}
