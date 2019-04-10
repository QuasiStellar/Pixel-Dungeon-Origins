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

package com.quasistellar.origins.effects;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.utils.PointF;
import com.quasistellar.origins.sprites.CharSprite;


public class Shield extends Halo {

	private float phase;

	private CharSprite target;

	public Shield( CharSprite sprite ) {

		super( 12, 0xBBAACC, 1f );

		am = -1;
		aa = +1;

		target = sprite;

//		phase = 1;
		phase = 0;
	}

	@Override
	public void update() {
		super.update();

//		if (phase < 1) {
//			if ((phase -= Game.elapsed) <= 0) {
//				killAndErase();
//			} else {
//				scale.set( (2 - phase) * radius / RADIUS );
//				am = phase * (-1);
//				aa = phase * (+1);
//			}
//		}

		if (phase < 0) {
			if ((phase += Game.elapsed) >= 0) {
				killAndErase();
			} else {
				scale.set( (2 + phase) * radius / RADIUS );
				am = -phase * (-1);
				aa = -phase * (+1);
			}
		} else if (phase < 1) {
			if ((phase += Game.elapsed) >= 1) {
				phase = 1;
			}
			scale.set( (2 - phase) * radius / RADIUS );
			am = phase * (-1);
			aa = phase * (+1);
		}

		point( target.x + target.width / 2, target.y + target.height / 2 );
	}

	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}

	public void putOut() {
//		phase = 0.999f;
		phase = -1;
	}
}
