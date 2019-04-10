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

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Charm;
import com.quasistellar.origins.actors.buffs.Fatigue;
import com.quasistellar.origins.actors.buffs.Paralysis;
import com.quasistellar.origins.actors.buffs.Roots;
import com.quasistellar.origins.actors.buffs.Terror;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.actors.mobs.npcs.MirrorImage;
import com.quasistellar.origins.effects.CellEmitter;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.effects.particles.EarthParticle;
import com.quasistellar.origins.items.armor.WarriorArmor;
import com.quasistellar.origins.items.scrolls.ScrollOfTeleportation;
import com.quasistellar.origins.mechanics.Ballistica;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.CellSelector;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class SkillIndicator extends Tag {

	private Image icon;

	private static int LEAP_TIME	= 1;
	private static int SHOCK_TIME	= 1;

	public SkillIndicator() {
		super(0xCDD5C0);

		setSize( 24, 24 );

		visible = false;

	}

	@Override
	protected void createChildren() {
		super.createChildren();
		switch (Dungeon.hero.heroClass) {
			case CHEVALIER:
				icon = Icons.get( Icons.C_SKILL );
				break;
			case ENCHANTER:
				icon = Icons.get( Icons.E_SKILL );
				break;
			case WANDERER:
				icon = Icons.get( Icons.W_SKILL );
				break;
		}
		add( icon );
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
		switch (Dungeon.hero.heroClass) {
			case CHEVALIER:
				GameScene.selectCell( leaper );
				break;
			case ENCHANTER:
				GameScene.selectCell( braider );
				break;
			case WANDERER:
				GameScene.selectCell( illusion );
				break;
		}
	}

	protected static CellSelector.Listener leaper = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target != null && target != Dungeon.hero.pos) {

				OldBallistica route = new OldBallistica(Dungeon.hero.pos, target, OldBallistica.PROJECTILE);
				int cell = route.collisionPos;

				//can't occupy the same cell as another char, so move back one.
				if (Actor.findChar( cell ) != null && cell != Dungeon.hero.pos)
					cell = route.path.get(route.dist-1);

				Dungeon.hero.buff(Fatigue.class).reduceSpirit(route.dist*2);

				final int dest = cell;
				Dungeon.hero.busy();
				Dungeon.hero.sprite.jump(Dungeon.hero.pos, cell, new Callback() {
					@Override
					public void call() {
						Dungeon.hero.move(dest);
						Dungeon.level.press(dest, Dungeon.hero, true);
						Dungeon.observe();
						GameScene.updateFog();

						for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
							Char mob = Actor.findChar(Dungeon.hero.pos + PathFinder.NEIGHBOURS8[i]);
							if (mob != null && mob != Dungeon.hero) {
								Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
							}
						}

						Dungeon.hero.spendAndNext(LEAP_TIME);
					}
				});
			}
		}

		@Override
		public String prompt() {
			return Messages.get(this, "jump_desc");
		}
	};

	protected static CellSelector.Listener braider = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;
			final Char enemy = Actor.findChar( target );
			if (enemy == null
					|| !Dungeon.level.heroFOV[target]
					|| Dungeon.hero.isCharmedBy( enemy )){
				GLog.w( Messages.get(this, "bad_target") );
			} else {
				Buff.affect( enemy, Roots.class, 5 );
				CellEmitter.bottom( target ).start( EarthParticle.FACTORY, 0.05f, 8 );
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);
			}
		}

		@Override
		public String prompt() {
			return Messages.get(this, "jump_desc");
		}
	};

	protected static CellSelector.Listener illusion = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;
			if (!Dungeon.level.heroFOV[target]
					|| !Dungeon.level.passable[target]){
				GLog.w( Messages.get(this, "bad_target") );
			} else {
				MirrorImage illusion = new MirrorImage();
				illusion.duplicate( Dungeon.hero );
				GameScene.add( illusion );
				ScrollOfTeleportation.appear( illusion, target );
				Buff.affect( illusion, Terror.class, Terror.DURATION );
				Dungeon.hero.buff(Fatigue.class).reduceSpirit(5);

				for (Mob enemy : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (Dungeon.level.heroFOV[enemy.pos]) {
						if (enemy.state == enemy.HUNTING) {
							enemy.state = enemy.WANDERING;
							enemy.chooseEnemy();
						}
					}
				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(this, "jump_desc");
		}
	};

	@Override
	public void update() {
		if (!Dungeon.hero.isAlive())
			visible = false;
        else if (!visible){
            visible = true;
            flash();
        }
        super.update();
	}
}
