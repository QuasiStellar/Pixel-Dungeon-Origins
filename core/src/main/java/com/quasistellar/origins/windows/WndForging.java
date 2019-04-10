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

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.actors.hero.Hero;
import com.quasistellar.origins.effects.Speck;
import com.quasistellar.origins.items.Dewdrop;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.Scheme;
import com.quasistellar.origins.items.weapon.missiles.darts.Dart;
import com.quasistellar.origins.levels.Terrain;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.scenes.GameScene;
import com.quasistellar.origins.scenes.PixelScene;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.tiles.DungeonTerrainTilemap;
import com.quasistellar.origins.ui.Icons;
import com.quasistellar.origins.ui.ItemSlot;
import com.quasistellar.origins.ui.RedButton;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.quasistellar.origins.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class WndForging extends Window {

	private static WndBlacksmith.ItemButton[] inputs = new WndBlacksmith.ItemButton[3];
	private ItemSlot output1;
	private ItemSlot output2;
	private ItemSlot output3;

	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;

	private RedButton btnCombine;

	private static final int WIDTH_P = 116;
	private static final int WIDTH_L = 160;

	private static final int BTN_SIZE	= 28;

	public WndForging(){
		
		int w = WIDTH_P;
		
		int h = 0;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon(DungeonTerrainTilemap.tile(0, Terrain.ANVIL));
		titlebar.label( Messages.get(this, "title") );
		titlebar.setRect( 0, 0, w, 0 );
		add( titlebar );
		
		h += titlebar.height() + 2;
		
		RenderedTextMultiline desc = PixelScene.renderMultiline(6);
		desc.text( Messages.get(this, "text") );
		desc.setPos(0, h);
		desc.maxWidth(w);
		add(desc);
		
		h += desc.height() + 6;

		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new WndBlacksmith.ItemButton() {
					@Override
					protected void onClick() {
						super.onClick();
						if (item != null) {
							if (!item.collect()) {
								Dungeon.level.drop(item, Dungeon.hero.pos);
							}
							item = null;
							slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
						}
						GameScene.selectItem(itemSelector, WndBag.Mode.FORGING, Messages.get(WndForging.class, "select"));
					}
				};
				inputs[i].setRect(10, h, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				h += BTN_SIZE + 2;
			}
		}
		
		btnCombine = new RedButton(""){
			Image arrow;
			
			@Override
			protected void createChildren() {
				super.createChildren();
				
				arrow = Icons.get(Icons.RESUME);
				add(arrow);
			}
			
			@Override
			protected void layout() {
				super.layout();
				arrow.x = x + (width - arrow.width)/2f;
				arrow.y = y + (height - arrow.height)/2f;
				PixelScene.align(arrow);
			}
			
			@Override
			public void enable(boolean value) {
				super.enable(value);
				if (value){
					arrow.tint(1, 1, 0, 1);
					arrow.alpha(1f);
					bg.alpha(1f);
				} else {
					arrow.color(0, 0, 0);
					arrow.alpha(0.6f);
					bg.alpha(0.6f);
				}
			}
			
			@Override
			protected void onClick() {
				super.onClick();
				combine();
			}
		};
		btnCombine.enable(false);
		btnCombine.setRect((w-30)/2f, inputs[1].top()+5, 30, inputs[1].height()-10);
		add(btnCombine);
		
		output1 = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null){
					GameScene.show(new WndInfoItem(item));
				}
			}
		};
		output2 = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null){
					GameScene.show(new WndInfoItem(item));
				}
			}
		};
		output3 = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null){
					GameScene.show(new WndInfoItem(item));
				}
			}
		};
		output1.setRect(w - BTN_SIZE - 10, inputs[1].top() - BTN_SIZE - 2, BTN_SIZE, BTN_SIZE);
		output2.setRect(w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
		output3.setRect(w - BTN_SIZE - 10, inputs[1].top() + BTN_SIZE + 2, BTN_SIZE, BTN_SIZE);

		ColorBlock outputBG1 = new ColorBlock(output1.width(), output1.height(), 0x9991938C);
		outputBG1.x = output1.left();
		outputBG1.y = output1.top();
		add(outputBG1);

		ColorBlock outputBG2 = new ColorBlock(output2.width(), output2.height(), 0x9991938C);
		outputBG2.x = output2.left();
		outputBG2.y = output2.top();
		add(outputBG2);

		ColorBlock outputBG3 = new ColorBlock(output3.width(), output3.height(), 0x9991938C);
		outputBG3.x = output3.left();
		outputBG3.y = output3.top();
		add(outputBG3);
		
		add(output1);
		output1.visible = false;

		add(output2);
		output1.visible = false;

		add(output3);
		output1.visible = false;
		
		bubbleEmitter = new Emitter();
		smokeEmitter = new Emitter();
		bubbleEmitter.pos(outputBG2.x - BTN_SIZE + (BTN_SIZE-16)/2f, outputBG2.y + (BTN_SIZE-16)/2f, 16, 16);
		smokeEmitter.pos(bubbleEmitter.x, bubbleEmitter.y, bubbleEmitter.width, bubbleEmitter.height);
		bubbleEmitter.autoKill = false;
		smokeEmitter.autoKill = false;
		add(bubbleEmitter);
		add(smokeEmitter);
		
		h += 4;
		
		float btnWidth = (w-14)/2f;
		
		RedButton btnRecipes = new RedButton(Messages.get(this, "recipes_title")){
			@Override
			protected void onClick() {
				super.onClick();
				Origins.scene().addToFront(new WndMessage(Messages.get(WndForging.class, "recipes_text")));
			}
		};
		btnRecipes.setRect(5, h, btnWidth, 18);
		PixelScene.align(btnRecipes);
		add(btnRecipes);
		
		RedButton btnClose = new RedButton(Messages.get(this, "close")){
			@Override
			protected void onClick() {
				super.onClick();
				onBackPressed();
			}
		};
		btnClose.setRect(w - 5 - btnWidth, h, btnWidth, 18);
		PixelScene.align(btnClose);
		add(btnClose);
		
		h += btnClose.height();
		
		resize(w, h);
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item == null) {
							if (item instanceof Dart) {
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
				}
				updateState();
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item;
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Scheme scheme = Scheme.findRecipe(ingredients);
		output1.visible = false;
		output2.visible = false;
		output3.visible = false;
		if (scheme != null){
			output1.item(scheme.sampleOutput(ingredients).get(0));
			output1.visible = true;
			if (!(scheme.sampleOutput(ingredients).get(1) instanceof Dewdrop)) {
				output2.item(scheme.sampleOutput(ingredients).get(1));
				output2.visible = true;
			}
			if (!(scheme.sampleOutput(ingredients).get(2) instanceof Dewdrop)) {
				output3.item(scheme.sampleOutput(ingredients).get(2));
				output3.visible = true;
			}
			btnCombine.enable(true);
			
		} else {
			btnCombine.enable(false);
			output1.visible = false;
			output2.visible = false;
			output3.visible = false;
		}
		
	}

	private void combine(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Scheme scheme = Scheme.findRecipe(ingredients);
		
		ArrayList<Item> result = null;
		
		if (scheme != null){
			result = scheme.brew(ingredients);
		}

        for (Item ingredient : ingredients){
            ingredient.quantity(ingredient.quantity() - 1);
        }
		
		if (result != null) {
			bubbleEmitter.start(Speck.factory(Speck.UP), 0.2f, 10);
			smokeEmitter.burst(Speck.factory(Speck.WOOL), 10);
            Sample.INSTANCE.play( Assets.SND_EVOKE );

			if (!(result.get(0) instanceof Dewdrop)) {
				output1.item(result.get(0));
				if (!result.get(0).collect()) {
					Dungeon.level.drop(result.get(0), Dungeon.hero.pos);
				}
			}
			if (!(result.get(1) instanceof Dewdrop)) {
				output2.item(result.get(1));
				if (!result.get(1).collect()) {
					Dungeon.level.drop(result.get(1), Dungeon.hero.pos);
				}
			}
			if (!(result.get(2) instanceof Dewdrop)) {
				output3.item(result.get(2));
				if (!result.get(2).collect()) {
					Dungeon.level.drop(result.get(2), Dungeon.hero.pos);
				}
			}



			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item != null) {
						if (inputs[i].item.quantity() <= 0) {
							inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
							inputs[i].item = null;
						} else {
							inputs[i].slot.item(inputs[i].item);
						}
					}
				}
			}
			
			btnCombine.enable(false);
		}
		
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i].item != null) {
					if (!inputs[i].item.collect()) {
						Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
					}
				}
				inputs[i] = null;
			}
		}
		super.destroy();
	}

	private static final String FORGING_INPUTS = "forging_inputs";

	public static void storeInBundle( Bundle b ){
		synchronized ( inputs ){
			ArrayList<Item> items = new ArrayList<>();
			for (WndBlacksmith.ItemButton i : inputs){
				if (i != null && i.item != null){
					items.add(i.item);
				}
			}
			if (!items.isEmpty()){
				b.put( FORGING_INPUTS, items );
			}
		}
	}

	public static void restoreFromBundle( Bundle b, Hero h ){

		if (b.contains(FORGING_INPUTS)){
			for (Bundlable item : b.getCollection(FORGING_INPUTS)){

				//try to add normally, force-add otherwise.
				if (!((Item)item).collect(h.belongings.backpack)){
					h.belongings.backpack.items.add((Item)item);
				}
			}
		}

	}
}
