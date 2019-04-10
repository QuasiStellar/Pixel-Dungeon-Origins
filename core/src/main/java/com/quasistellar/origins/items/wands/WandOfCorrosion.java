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
import com.quasistellar.origins.actors.blobs.Blob;
import com.quasistellar.origins.actors.blobs.CorrosiveGas;
import com.quasistellar.origins.actors.buffs.Buff;
import com.quasistellar.origins.actors.buffs.Ooze;
import com.quasistellar.origins.effects.CellEmitter;
import com.quasistellar.origins.effects.MagicMissile;
import com.quasistellar.origins.effects.particles.CorrosionParticle;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfCorrosion extends Wand {

	{
		image = ItemSpriteSheet.WAND_CORROSION;

		collisionProperties = OldBallistica.STOP_TARGET | OldBallistica.STOP_TERRAIN;
	}

	@Override
	protected void onZap(OldBallistica bolt) {
		Blob corrosiveGas = Blob.seed(bolt.collisionPos, 50 + 10 * 5, CorrosiveGas.class);
		CellEmitter.center(bolt.collisionPos).burst( CorrosionParticle.SPLASH, 10 );
		((CorrosiveGas)corrosiveGas).setStrength(5+1);
		GameScene.add(corrosiveGas);

		for (int i : PathFinder.NEIGHBOURS9) {
			Char ch = Actor.findChar(bolt.collisionPos + i);
			if (ch != null) {
				processSoulMark(ch, chargesPerCast());
			}
		}
		
		if (Actor.findChar(bolt.collisionPos) == null){
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}

	@Override
	protected void fx(OldBallistica bolt, Callback callback) {
		MagicMissile.boltFromChar(
				curUser.sprite.parent,
				MagicMissile.CORROSION,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

}
