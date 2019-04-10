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

package com.quasistellar.origins.items;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.items.armor.Armor;
import com.quasistellar.origins.items.armor.ClassArmor;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.sprites.HeroSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.utils.GLog;
import com.quasistellar.origins.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ArmorKit extends Item {

	private static final String TXT_UPGRADED		= "you applied the armor kit to upgrade your %s";
	
	private static final float TIME_TO_UPGRADE = 2;
	
	private static final String AC_APPLY = "APPLY";
	
	{
		image = ItemSpriteSheet.KIT;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action == AC_APPLY) {

			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt") );
			
		}
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void upgrade( Armor armor ) {

		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_EVOKE );
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
			}
		}
	};
}
