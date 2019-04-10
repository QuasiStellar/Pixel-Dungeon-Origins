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

import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.SPDSettings;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.stones.Runestone;
import com.quasistellar.origins.items.weapon.melee.MeleeWeapon;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.ui.RedButton;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.quasistellar.origins.ui.Window;
import com.watabou.noosa.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class WndMeleeWeapon extends Window {

	private static final float BUTTON_HEIGHT	= 16;

	private static final float GAP	= 2;

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	private WndBlacksmith.ItemButton btnStone1;
	private WndBlacksmith.ItemButton btnStone2;
	private WndBlacksmith.ItemButton btnStone3;

	public WndMeleeWeapon(final WndBag owner, final Item item ){
		this( owner, item, owner != null );
	}

	public WndMeleeWeapon(final WndBag owner, final Item item , final boolean options ) {
		
		super();

		int width = SPDSettings.landscape() ? WIDTH_L : WIDTH_P;
		final float BTN_GAP	= 12;
		final int BTN_SIZE	= 32;
		
		IconTitle titlebar = new IconTitle( item );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		RenderedTextMultiline info = PixelScene.renderMultiline( item.info(), 6 );
		info.maxWidth(width);
		info.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add( info );

		final MeleeWeapon weapon = (MeleeWeapon)item;

		if (Dungeon.hero.isAlive()) {
			btnStone1 = new WndBlacksmith.ItemButton() {
				@Override
				protected void onClick() {
					if (weapon.stone1 != null) {
						GameScene.show(new WndOptions(Messages.titleCase(weapon.name()), Messages.get(this, "warning", weapon.stone1.name()),
								Messages.get(this, "yes"), Messages.get(this, "no")) {
							@Override
							protected void onSelect(int index) {
								switch (index) {
									case 0:
										item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
										weapon.stone1 = null;
										break;
									case 1:
										break;
								}
							}

							public void onBackPressed() {
							}

						});
					} else {
						GameScene.selectItem(new WndBag.Listener() {
							@Override
							public void onSelect(Item item) {
								if (!(item instanceof Runestone)) {
									//do nothing, should only happen when window is cancelled
								} else {
									item.detach(Dungeon.hero.belongings.backpack);
									weapon.stone1 = (Runestone) item;
									item(weapon.stone1);
								}

							}
						}, WndBag.Mode.STONE, Messages.get(MeleeWeapon.class, "stone_prompt"));
					}
				}
			};

			btnStone2 = new WndBlacksmith.ItemButton() {
				@Override
				protected void onClick() {
					if (weapon.stone2 != null) {
						GameScene.show(new WndOptions(Messages.titleCase(weapon.name()), Messages.get(this, "warning"),
								Messages.get(this, "yes"), Messages.get(this, "no")) {
							@Override
							protected void onSelect(int index) {
								switch (index) {
									case 0:
										item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
										weapon.stone2 = null;
										break;
									case 1:
										break;
								}
							}

							public void onBackPressed() {
							}

						});
					} else {
						GameScene.selectItem(new WndBag.Listener() {
							@Override
							public void onSelect(Item item) {
								if (!(item instanceof Runestone)) {
									//do nothing, should only happen when window is cancelled
								} else {
									item.detach(Dungeon.hero.belongings.backpack);
									weapon.stone2 = (Runestone) item;
									item(weapon.stone2);
								}

							}
						}, WndBag.Mode.STONE, Messages.get(MeleeWeapon.class, "stone_prompt"));
					}
				}
			};

			btnStone3 = new WndBlacksmith.ItemButton() {
				@Override
				protected void onClick() {
					if (weapon.stone3 != null) {
						Game.scene().add( new WndItem( null, item ) );
					} else {
						GameScene.selectItem(new WndBag.Listener() {
							@Override
							public void onSelect(Item item) {
								if (!(item instanceof Runestone)) {
									//do nothing, should only happen when window is cancelled
								} else {
									item.detach(Dungeon.hero.belongings.backpack);
									weapon.stone3 = (Runestone) item;
									item(weapon.stone3);
								}

							}
						}, WndBag.Mode.STONE, Messages.get(MeleeWeapon.class, "stone_prompt"));
					}
				}
			};

			if (weapon.tier == 1) {
				btnStone1.setRect((width - BTN_SIZE) / 2, info.top() + info.height() + GAP, BTN_SIZE, BTN_SIZE);
				if (weapon.stone1 != null) {
					btnStone1.item(weapon.stone1);
				} else {
					btnStone1.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone1);
			} else if (weapon.tier == 2) {
				btnStone1.setRect((width - BTN_GAP) / 2 - BTN_SIZE, info.top() + info.height() + GAP, BTN_SIZE, BTN_SIZE);
				if (weapon.stone1 != null) {
					btnStone1.item(weapon.stone1);
				} else {
					btnStone1.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone1);
				btnStone2.setRect(btnStone1.right() + BTN_GAP, btnStone1.top(), BTN_SIZE, BTN_SIZE);
				if (weapon.stone1 != null) {
					btnStone2.item(weapon.stone2);
				} else {
					btnStone2.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone2);
			} else {
				btnStone1.setRect((width - 2 * (GAP * 2) - 3 * BTN_SIZE) / 2, info.top() + info.height() + GAP, BTN_SIZE, BTN_SIZE);
				if (weapon.stone1 != null) {
					btnStone1.item(weapon.stone1);
				} else {
					btnStone1.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone1);
				btnStone2.setRect(btnStone1.right() + GAP * 2, btnStone1.top(), BTN_SIZE, BTN_SIZE);
				if (weapon.stone1 != null) {
					btnStone2.item(weapon.stone2);
				} else {
					btnStone2.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone2);
				btnStone3.setRect(btnStone2.right() + GAP * 2, btnStone1.top(), BTN_SIZE, BTN_SIZE);
				if (weapon.stone3 != null) {
					btnStone3.item(weapon.stone3);
				} else {
					btnStone3.item(new WndBag.Placeholder(ItemSpriteSheet.STONE_HOLDER));
				}
				add(btnStone3);
			}
		}

		float y = btnStone1.top() + BTN_SIZE + GAP;
		float x = 0;

		if (Dungeon.hero.isAlive() && options) {
			ArrayList<RedButton> line = new ArrayList<>();
			for (final String action:item.actions( Dungeon.hero )) {
				
				RedButton btn = new RedButton( Messages.get(item, "ac_" + action), 8 ) {
					@Override
					protected void onClick() {
						hide();
						if (owner != null && owner.parent != null) owner.hide();
						item.execute( Dungeon.hero, action );
					};
				};
				btn.setSize( btn.reqWidth(), BUTTON_HEIGHT );
				if (x + btn.width() > width || line.size() == 3) {
					layoutButtons(line, width - x, y);
					x = 0;
					y += BUTTON_HEIGHT + 1;
					line = new ArrayList<>();
				}
				x++;
				add( btn );
				line.add( btn );

				if (action.equals(item.defaultAction)) {
					btn.textColor( TITLE_COLOR );
				}

				x += btn.width();
			}
			layoutButtons(line, width - x, y);
		}
		
		resize( width, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}

	//this method assumes a max of 3 buttons per line
	//FIXME: this is really messy for just trying to make buttons fill the window. Gotta be a cleaner way.
	private static void layoutButtons(ArrayList<RedButton> line, float extraWidth, float y){
		if (line == null || line.size() == 0 || extraWidth == 0) return;
		if (line.size() == 1){
			line.get(0).setSize(line.get(0).width()+extraWidth, BUTTON_HEIGHT);
			line.get(0).setPos( 0 , y );
			return;
		}
		ArrayList<RedButton> lineByWidths = new ArrayList<>(line);
		Collections.sort(lineByWidths, widthComparator);
		RedButton smallest, middle, largest;
		smallest = lineByWidths.get(0);
		middle = lineByWidths.get(1);
		largest = null;
		if (lineByWidths.size() == 3) {
			largest = lineByWidths.get(2);
		}

		float btnDiff = middle.width() - smallest.width();
		smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth), BUTTON_HEIGHT);
		extraWidth -= btnDiff;
		if (extraWidth > 0) {
			if (largest == null) {
				smallest.setSize(smallest.width() + extraWidth / 2, BUTTON_HEIGHT);
				middle.setSize(middle.width() + extraWidth / 2, BUTTON_HEIGHT);
			} else {
				btnDiff = largest.width() - smallest.width();
				smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth/2), BUTTON_HEIGHT);
				middle.setSize(middle.width() + Math.min(btnDiff, extraWidth/2), BUTTON_HEIGHT);
				extraWidth -= btnDiff*2;
				if (extraWidth > 0){
					smallest.setSize(smallest.width() + extraWidth / 3, BUTTON_HEIGHT);
					middle.setSize(middle.width() + extraWidth / 3, BUTTON_HEIGHT);
					largest.setSize(largest.width() + extraWidth / 3, BUTTON_HEIGHT);
				}
			}
		}

		float x = 0;
		for (RedButton btn : line){
			btn.setPos( x , y );
			x += btn.width()+1;
		}
	}

	private static Comparator<RedButton> widthComparator = new Comparator<RedButton>() {
		@Override
		public int compare(RedButton lhs, RedButton rhs) {
			if (lhs.width() < rhs.width()){
				return -1;
			} else if (lhs.width() == rhs.width()){
				return 0;
			} else {
				return 1;
			}
		}
	};
}
