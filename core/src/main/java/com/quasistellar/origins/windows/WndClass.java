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

package com.quasistellar.origins.windows;

import com.quasistellar.origins.Badges;
import com.quasistellar.origins.actors.hero.HeroClass;
import com.quasistellar.origins.actors.hero.HeroSubClass;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;

public class WndClass extends WndTabbed {

	private static final int WIDTH			= 110;

	private static final int TAB_WIDTH	= 50;

	private HeroClass cl;

	private PerksTab tabPerks;
	private MasteryTab tabMastery;

	public WndClass( HeroClass cl ) {

		super();

		this.cl = cl;

		tabPerks = new PerksTab();
		add( tabPerks );

		Tab tab = new RankingTab( cl.title().toUpperCase(), tabPerks );
		tab.setSize( TAB_WIDTH, tabHeight() );
		add( tab );

		if (Badges.isUnlocked( cl.masteryBadge() )) {
			tabMastery = new MasteryTab();
			add( tabMastery );

			tab = new RankingTab( Messages.get(this, "mastery"), tabMastery );
			add( tab );

			resize(
					(int)Math.max( tabPerks.width, tabMastery.width ),
					(int)Math.max( tabPerks.height, tabMastery.height ) );
		} else {
			resize( (int)tabPerks.width, (int)tabPerks.height );
		}

		layoutTabs();

		select( 0 );
	}

	private class RankingTab extends LabeledTab {

		private Group page;

		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}

		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}

	private class PerksTab extends Group {

		private static final int MARGIN	= 4;
		private static final int GAP	= 4;

		public float height;
		public float width;

		public PerksTab() {
			super();

			float dotWidth = 0;

			String[] items = cl.perks();
			float pos = MARGIN;

			for (int i=0; i < items.length; i++) {

				if (i > 0) {
					pos += GAP;
				}

				BitmapText dot = PixelScene.createText( "-", 6 );
				dot.x = MARGIN;
				dot.y = pos;
				if (dotWidth == 0) {
					dot.measure();
					dotWidth = dot.width();
				}
				add( dot );

				RenderedTextMultiline item = PixelScene.renderMultiline( items[i], 6 );
				item.maxWidth((int)(WIDTH - MARGIN * 2 - dotWidth));
				item.setPos(dot.x + dotWidth, pos);
				add( item );

				pos += item.height();
				float w = item.width();
				if (w > width) {
					width = w;
				}
			}

			width += MARGIN + dotWidth;
			height = pos + MARGIN;
		}
	}

	private class MasteryTab extends Group {

		private static final int MARGIN	= 4;

		public float height;
		public float width;

		public MasteryTab() {
			super();

			String message = null;
			switch (cl) {
				case ENCHANTER:
					message = HeroSubClass.SORCERER.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
					break;
				case CHEVALIER:
					message = HeroSubClass.KNIGHT.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
					break;
				case WANDERER:
					message = HeroSubClass.RANGER.desc() + "\n\n" + HeroSubClass.EXPLORER.desc();
					break;
			}

			RenderedTextMultiline text = PixelScene.renderMultiline( 6 );
			text.text( message, WIDTH - MARGIN * 2 );
			text.setPos( MARGIN, MARGIN );
			add( text );

			height = text.bottom() + MARGIN;
			width = text.right() + MARGIN;
		}
	}
}