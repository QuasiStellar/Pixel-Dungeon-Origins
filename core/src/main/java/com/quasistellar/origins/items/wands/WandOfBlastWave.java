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
import com.quasistellar.origins.actors.buffs.Paralysis;
import com.quasistellar.origins.effects.Effects;
import com.quasistellar.origins.effects.MagicMissile;
import com.quasistellar.origins.effects.Pushing;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.tiles.DungeonTilemap;
import com.quasistellar.origins.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfBlastWave extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_BLAST_WAVE;

		collisionProperties = OldBallistica.PROJECTILE;
	}

	public int min(){
		return 1+5;
	}

	public int max(){
		return 5+3*5;
	}

	@Override
	protected void onZap(OldBallistica bolt) {
		Sample.INSTANCE.play( Assets.SND_BLAST );
		BlastWave.blast(bolt.collisionPos);

		int damage = damageRoll();

		//presses all tiles in the AOE first
		for (int i : PathFinder.NEIGHBOURS9){
			Dungeon.level.press(bolt.collisionPos+i, Actor.findChar(bolt.collisionPos+i), true);
		}

		//throws other chars around the center.
		for (int i  : PathFinder.NEIGHBOURS8){
			Char ch = Actor.findChar(bolt.collisionPos + i);

			if (ch != null){
				processSoulMark(ch, chargesPerCast());
				ch.damage(Math.round(damage * 0.667f), this);

				if (ch.isAlive()) {
					OldBallistica trajectory = new OldBallistica(ch.pos, ch.pos + i, OldBallistica.MAGIC_BOLT);
					int strength = 1 + Math.round(5 / 2f);
					throwChar(ch, trajectory, strength);
				}
			}
		}

		//throws the char at the center of the blast
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){
			processSoulMark(ch, chargesPerCast());
			ch.damage(damage, this);

			if (ch.isAlive() && bolt.path.size() > bolt.dist+1) {
				OldBallistica trajectory = new OldBallistica(ch.pos, bolt.path.get(bolt.dist + 1), OldBallistica.MAGIC_BOLT);
				int strength = 5 + 3;
				throwChar(ch, trajectory, strength);
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n( Messages.get( this, "ondeath") );
		}
	}

	public static void throwChar(final Char ch, final OldBallistica trajectory, int power){
		int dist = Math.min(trajectory.dist, power);

		if (ch.properties().contains(Char.Property.BOSS))
			dist /= 2;

		if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

		if (Actor.findChar(trajectory.path.get(dist)) != null){
			dist--;
		}

		final int newPos = trajectory.path.get(dist);

		if (newPos == ch.pos) return;

		final int finalDist = dist;
		final int initialpos = ch.pos;

		Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
			public void call() {
				if (initialpos != ch.pos) {
					//something cased movement before pushing resolved, cancel to be safe.
					ch.sprite.place(ch.pos);
					return;
				}
				ch.pos = newPos;
				if (ch.pos == trajectory.collisionPos) {
					ch.damage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), this);
					Paralysis.prolong(ch, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
				}
				Dungeon.level.press(ch.pos, ch, true);
				if (ch == Dungeon.hero){
					Dungeon.observe();
				}
			}
		}), -1);
	}

	@Override
	protected void fx(OldBallistica bolt, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.FORCE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	public static class BlastWave extends Image {

		private static final float TIME_TO_FADE = 0.2f;

		private float time;

		public BlastWave(){
			super(Effects.get(Effects.Type.RIPPLE));
			origin.set(width / 2, height / 2);
		}

		public void reset(int pos) {
			revive();

			x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
			y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

			time = TIME_TO_FADE;
		}

		@Override
		public void update() {
			super.update();

			if ((time -= Game.elapsed) <= 0) {
				kill();
			} else {
				float p = time / TIME_TO_FADE;
				alpha(p);
				scale.y = scale.x = (1-p)*3;
			}
		}

		public static void blast(int pos) {
			Group parent = Dungeon.hero.sprite.parent;
			BlastWave b = (BlastWave) parent.recycle(BlastWave.class);
			parent.bringToFront(b);
			b.reset(pos);
		}

	}
}
