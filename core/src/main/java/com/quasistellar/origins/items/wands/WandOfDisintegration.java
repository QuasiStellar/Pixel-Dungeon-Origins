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

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.Char;
import com.quasistellar.origins.effects.Beam;
import com.quasistellar.origins.effects.CellEmitter;
import com.quasistellar.origins.effects.particles.PurpleParticle;
import com.quasistellar.origins.mechanics.OldBallistica;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.tiles.DungeonTilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfDisintegration extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_DISINTEGRATION;

		collisionProperties = OldBallistica.WONT_STOP;
	}


	public int min(){
		return 2+5;
	}

	public int max(){
		return 8+4*5;
	}
	
	@Override
	protected void onZap( OldBallistica beam ) {
		
		boolean terrainAffected = false;
		
		int maxDistance = Math.min(distance(), beam.dist);
		
		ArrayList<Char> chars = new ArrayList<>();

		int terrainPassed = 2, terrainBonus = 0;
		for (int c : beam.subPath(1, maxDistance)) {
			
			Char ch;
			if ((ch = Actor.findChar( c )) != null) {

				//we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
				//terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
				terrainBonus += terrainPassed/3;
				terrainPassed = terrainPassed%3;

				chars.add( ch );
			}

			if (Dungeon.level.flamable[c]) {

				Dungeon.level.destroy( c );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			}

			if (Dungeon.level.solid[c])
				terrainPassed++;
			
			CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}
		
		int lvl = 5 + (chars.size()-1) + terrainBonus;
		for (Char ch : chars) {
			processSoulMark(ch, chargesPerCast());
			ch.damage( damageRoll(lvl), this );
			ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
			ch.sprite.flash();
		}
	}


	private int distance() {
		return 5*2 + 4;
	}
	
	@Override
	protected void fx( OldBallistica beam, Callback callback ) {
		
		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
		callback.call();
	}

}
