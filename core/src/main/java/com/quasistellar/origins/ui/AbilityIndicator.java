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
import com.quasistellar.origins.actors.buffs.Aura;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Disguise;
import com.quasistellar.origins.actors.buffs.Haste;
import com.quasistellar.origins.actors.buffs.Fatigue;
import com.quasistellar.origins.actors.buffs.Paralysis;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.CellSelector;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.sprites.CharSprite;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class AbilityIndicator extends Tag {

	private Image icon;
	private boolean on;

	public AbilityIndicator() {
		super(0xCDD5C0);

		setSize( 24, 24 );

		visible = false;
		on = false;

	}

	@Override
	protected void createChildren() {
		super.createChildren();
		switch (Dungeon.hero.heroClass) {
			case CHEVALIER:
				icon = Icons.get( Icons.C_ABILITY );
				break;
			case ENCHANTER:
				icon = Icons.get( Icons.E_ABILITY );
				break;
			case WANDERER:
				icon = Icons.get( Icons.W_ABILITY );
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
				if (on) {
					on = false;
					Buff.detach(Dungeon.hero, Disguise.class);
				} else {
					on = true;
					Buff.affect(Dungeon.hero, Disguise.class);
					Dungeon.hero.buff(Fatigue.class).reduceSpirit(3);
				}
				break;
			case ENCHANTER:
				if (on) {
					on = false;
					Dungeon.hero.sprite.remove(CharSprite.State.SHIELDED);
					Buff.detach(Dungeon.hero, Aura.class);
				} else {
					Dungeon.hero.sprite.add(CharSprite.State.SHIELDED);
					Buff.affect(Dungeon.hero, Aura.class);
					Dungeon.hero.buff(Fatigue.class).reduceSpirit(1);
					on = true;
				}
				break;
			case WANDERER:
				if (on) {
					on = false;
					Buff.detach(Dungeon.hero, Haste.class);
				} else {
					on = true;
					Buff.affect(Dungeon.hero, Haste.class);
					Dungeon.hero.buff(Fatigue.class).reduceSpirit(1);
				}
				break;
		}
	}

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
