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

package com.quasistellar.origins.scenes;

import com.quasistellar.origins.Assets;
import com.quasistellar.origins.Badges;
import com.quasistellar.origins.Dungeon;
import com.quasistellar.origins.SPDSettings;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.Statistics;
import com.quasistellar.origins.actors.Actor;
import com.quasistellar.origins.actors.blobs.Blob;
import com.quasistellar.origins.actors.mobs.Mob;
import com.quasistellar.origins.effects.BannerSprites;
import com.quasistellar.origins.effects.BlobEmitter;
import com.quasistellar.origins.effects.EmoIcon;
import com.quasistellar.origins.effects.Flare;
import com.quasistellar.origins.effects.FloatingText;
import com.quasistellar.origins.effects.Ripple;
import com.quasistellar.origins.effects.SpellSprite;
import com.quasistellar.origins.items.Heap;
import com.quasistellar.origins.items.Honeypot;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.bags.PotionBandolier;
import com.quasistellar.origins.items.bags.ScrollHolder;
import com.quasistellar.origins.items.bags.VelvetPouch;
import com.quasistellar.origins.items.bags.WandHolster;
import com.quasistellar.origins.items.potions.Potion;
import com.quasistellar.origins.items.scrolls.ScrollOfTeleportation;
import com.quasistellar.origins.journal.Journal;
import com.quasistellar.origins.levels.RegularLevel;
import com.quasistellar.origins.levels.features.Chasm;
import com.quasistellar.origins.levels.traps.Trap;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.plants.Plant;
import com.quasistellar.origins.sprites.CharSprite;
import com.quasistellar.origins.sprites.DiscardedItemSprite;
import com.quasistellar.origins.sprites.HeroSprite;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.tiles.CustomTiledVisual;
import com.quasistellar.origins.tiles.DungeonTerrainTilemap;
import com.quasistellar.origins.tiles.DungeonTileSheet;
import com.quasistellar.origins.tiles.DungeonTilemap;
import com.quasistellar.origins.tiles.DungeonWallsTilemap;
import com.quasistellar.origins.tiles.FogOfWar;
import com.quasistellar.origins.tiles.GridTileMap;
import com.quasistellar.origins.tiles.TerrainFeaturesTilemap;
import com.quasistellar.origins.tiles.WallBlockingTilemap;
import com.quasistellar.origins.ui.ActionIndicator;
import com.quasistellar.origins.ui.AttackIndicator;
import com.quasistellar.origins.ui.Banner;
import com.quasistellar.origins.ui.BusyIndicator;
import com.quasistellar.origins.ui.CharHealthIndicator;
import com.quasistellar.origins.ui.GameLog;
import com.quasistellar.origins.ui.LootIndicator;
import com.quasistellar.origins.ui.QuickSlotButton;
import com.quasistellar.origins.ui.ResumeIndicator;
import com.quasistellar.origins.ui.RushIndicator;
import com.quasistellar.origins.ui.SkillIndicator;
import com.quasistellar.origins.ui.AbilityIndicator;
import com.quasistellar.origins.ui.StatusPane;
import com.quasistellar.origins.ui.TargetHealthIndicator;
import com.quasistellar.origins.ui.Toast;
import com.quasistellar.origins.ui.Toolbar;
import com.quasistellar.origins.ui.Window;
import com.quasistellar.origins.utils.GLog;
import com.quasistellar.origins.windows.WndBag;
import com.quasistellar.origins.windows.WndBag.Mode;
import com.quasistellar.origins.windows.WndGame;
import com.quasistellar.origins.windows.WndHero;
import com.quasistellar.origins.windows.WndInfoCell;
import com.quasistellar.origins.windows.WndInfoItem;
import com.quasistellar.origins.windows.WndInfoMob;
import com.quasistellar.origins.windows.WndInfoPlant;
import com.quasistellar.origins.windows.WndInfoTrap;
import com.quasistellar.origins.windows.WndMessage;
import com.quasistellar.origins.windows.WndOptions;
import com.quasistellar.origins.windows.WndStory;
import com.quasistellar.origins.windows.WndTradeItem;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class GameScene extends PixelScene {

	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTerrainTilemap tiles;
	private GridTileMap visualGrid;
	private TerrainFeaturesTilemap terrainFeatures;
	private DungeonWallsTilemap walls;
	private WallBlockingTilemap wallBlocking;
	private FogOfWar fog;
	private HeroSprite hero;

	private StatusPane pane;
	
	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group customTiles;
	private Group levelVisuals;
	private Group customWalls;
	private Group ripples;
	private Group plants;
	private Group traps;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	private Group healthIndicators;
	
	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ActionIndicator action;
	private ResumeIndicator resume;
	private SkillIndicator skill;
	private AbilityIndicator ability;
	private RushIndicator rush;
	
	@Override
	public void create() {
		
		Music.INSTANCE.play( Assets.TUNE, true );

		SPDSettings.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + SPDSettings.zoom(), maxZoom));

		scene = this;

		terrain = new Group();
		add( terrain );

		water = new SkinnedBlock(
			Dungeon.level.width() * DungeonTilemap.SIZE,
			Dungeon.level.height() * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() ){

			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		terrain.add( water );

		ripples = new Group();
		terrain.add( ripples );

		DungeonTileSheet.setupVariance(Dungeon.level.map.length, Dungeon.seedCurDepth());
		
		tiles = new DungeonTerrainTilemap();
		terrain.add( tiles );

		customTiles = new Group();
		terrain.add(customTiles);

		for( CustomTiledVisual visual : Dungeon.level.customTiles){
			addCustomTile(visual);
		}

		visualGrid = new GridTileMap();
		terrain.add( visualGrid );

		terrainFeatures = new TerrainFeaturesTilemap(Dungeon.level.plants, Dungeon.level.traps);
		terrain.add(terrainFeatures);
		
		levelVisuals = Dungeon.level.addVisuals();
		add(levelVisuals);
		
		heaps = new Group();
		add( heaps );
		
		int size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite( Dungeon.level.heaps.valueAt( i ) );
		}
		
		emitters = new Group();
		effects = new Group();
		healthIndicators = new Group();
		emoicons = new Group();
		
		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}

		walls = new DungeonWallsTilemap();
		add(walls);

		customWalls = new Group();
		add(customWalls);

		for( CustomTiledVisual visual : Dungeon.level.customWalls){
			addCustomWall(visual);
		}

		wallBlocking = new WallBlockingTilemap();
		add (wallBlocking);

		add( emitters );
		add( effects );

		gases = new Group();
		add( gases );

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}


		fog = new FogOfWar( Dungeon.level.width(), Dungeon.level.height() );
		add( fog );

		spells = new Group();
		add( spells );
		
		statuses = new Group();
		add( statuses );
		
		add( healthIndicators );
		//always appears ontop of other health indicators
		add( new TargetHealthIndicator() );
		
		add( emoicons );
		
		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );
		
		add( cellSelector = new CellSelector( tiles ) );

		pane = new StatusPane();
		pane.camera = uiCamera;
		pane.setSize( uiCamera.width, 0 );
		add( pane );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		action = new ActionIndicator();
		action.camera = uiCamera;
		add( action );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		skill = new SkillIndicator();
		skill.camera = uiCamera;
		add( skill );

		ability = new AbilityIndicator();
		ability.camera = uiCamera;
		add( ability );

		rush = new RushIndicator();
		rush.camera = uiCamera;
		add( rush );

		log = new GameLog();
		log.camera = uiCamera;
		log.newLine();
		add( log );

		layoutTags();

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = pane.bottom() +1;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			ScrollOfTeleportation.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			ScrollOfTeleportation.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				break;
			case 6:
				WndStory.showChapter( WndStory.ID_PRISON );
				break;
			case 11:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 16:
				WndStory.showChapter( WndStory.ID_CITY );
				break;
			case 22:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
			if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
				Badges.validateNoKilling();
			}
			break;
		default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

		Dungeon.hero.next();

		Camera.main.target = hero;

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
			if (Dungeon.depth == Statistics.deepestFloor
					&& (InterlevelScene.mode == InterlevelScene.Mode.DESCEND || InterlevelScene.mode == InterlevelScene.Mode.FALL)) {
				GLog.h(Messages.get(this, "descend"), Dungeon.depth);
				Sample.INSTANCE.play(Assets.SND_DESCEND);
			} else if (InterlevelScene.mode == InterlevelScene.Mode.RESET) {
				GLog.h(Messages.get(this, "warp"));
			} else {
				GLog.h(Messages.get(this, "return"), Dungeon.depth);
			}

			switch (Dungeon.level.feeling) {
				case CHASM:
					GLog.w(Messages.get(this, "chasm"));
					break;
				case WATER:
					GLog.w(Messages.get(this, "water"));
					break;
				case GRASS:
					GLog.w(Messages.get(this, "grass"));
					break;
				case DARK:
					GLog.w(Messages.get(this, "dark"));
					break;
				default:
			}
			if (Dungeon.level instanceof RegularLevel &&
					((RegularLevel) Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
				GLog.w(Messages.get(this, "secrets"));
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			fadeIn();
		}

	}
	
	public void destroy() {
		
		//tell the actor thread to finish, then wait for it to complete any actions it may be doing.
		if (actorThread.isAlive()){
			synchronized (GameScene.class){
				synchronized (actorThread) {
					actorThread.interrupt();
				}
				try {
					GameScene.class.wait(5000);
				} catch (InterruptedException e) {
					Origins.reportException(e);
				}
				synchronized (actorThread) {
					if (Actor.processing()) {
						Throwable t = new Throwable();
						t.setStackTrace(actorThread.getStackTrace());
						throw new RuntimeException("timeout waiting for actor thread! ", t);
					}
				}
			}
		}
		
		freezeEmitters = false;
		
		scene = null;
		Badges.saveGlobal();
		Journal.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void onPause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			Origins.reportException(e);
		}
	}

	private static final Thread actorThread = new Thread() {
		@Override
		public void run() {
			Actor.process();
		}
	};

	@Override
	public synchronized void update() {
		if (Dungeon.hero == null || scene == null) {
			return;
		}

		super.update();
		
		if (!freezeEmitters) water.offset( 0, -5 * Game.elapsed );

		if (!Actor.processing() && Dungeon.hero.isAlive()) {
			if (!actorThread.isAlive()) {
				//if cpu cores are limited, game should prefer drawing the current frame
				if (Runtime.getRuntime().availableProcessors() == 1) {
					actorThread.setPriority(Thread.NORM_PRIORITY - 1);
				}
				actorThread.start();
			} else {
				synchronized (actorThread) {
					actorThread.notify();
				}
			}
		}
		
		if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
			log.newLine();
		}

		if (tagAttack != attack.active ||
				tagLoot != loot.visible ||
				tagAction != action.visible ||
				tagResume != resume.visible ||
				tagSkill != skill.visible ||
				tagAbility != ability.visible ||
				tagRush != rush.visible) {

			//we only want to change the layout when new tags pop in, not when existing ones leave.
			boolean tagAppearing = (attack.active && !tagAttack) ||
									(loot.visible && !tagLoot) ||
									(action.visible && !tagAction) ||
									(resume.visible && !tagResume) ||
									(skill.visible && !tagSkill) ||
									(ability.visible && !tagAbility) ||
									(rush.visible && !tagRush);

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagAction = action.visible;
			tagResume = resume.visible;
			tagSkill = skill.visible;
			tagAbility = ability.visible;
			tagRush = rush.visible;

			if (tagAppearing) layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);
	}

	private boolean tagAttack    = false;
	private boolean tagLoot      = false;
	private boolean tagAction    = false;
	private boolean tagResume    = false;
	private boolean tagSkill     = false;
	private boolean tagAbility     = false;
	private boolean tagRush      = false;

	public static void layoutTags() {

		if (scene == null) return;

		float tagLeft = SPDSettings.flipTags() ? 0 : uiCamera.width - scene.attack.width();
		float tagRight = SPDSettings.flipTags() ? uiCamera.width - scene.attack.width() : 0;

		if (SPDSettings.flipTags()) {
			scene.log.setRect(scene.attack.width(), scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
		} else {
			scene.log.setRect(0, scene.toolbar.top(), uiCamera.width - scene.attack.width(),  0 );
		}

		float rightPos = scene.toolbar.top();
        float leftPos = scene.pane.bottom();

		if (scene.tagAttack){
			scene.attack.setPos( tagLeft, rightPos - scene.attack.height());
			scene.attack.flip(tagLeft == 0);
            rightPos = scene.attack.top();
		}

		if (scene.tagLoot) {
			scene.loot.setPos( tagLeft, rightPos - scene.loot.height() );
			scene.loot.flip(tagLeft == 0);
            rightPos = scene.loot.top();
		}

		if (scene.tagAction) {
			scene.action.setPos( tagLeft, rightPos - scene.action.height() );
			scene.action.flip(tagLeft == 0);
            rightPos = scene.action.top();
		}

		if (scene.tagResume) {
			scene.resume.setPos( tagLeft, rightPos - scene.resume.height() );
			scene.resume.flip(tagLeft == 0);
		}

		if (scene.tagSkill) {
			scene.skill.setPos( tagRight, leftPos + 10 );
			scene.skill.flip(tagLeft != 0);
		}

		if (scene.tagAbility) {
			scene.ability.setPos( tagRight, leftPos + scene.skill.height() + 12 );
			scene.ability.flip(tagLeft != 0);
		}

		if (scene.tagRush) {
			scene.rush.setPos( tagRight, leftPos + scene.skill.height() + scene.ability.height() + 14 );
			scene.rush.flip(tagLeft != 0);
		}
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}

	public void addCustomTile( CustomTiledVisual visual){
		customTiles.add( visual.create() );
	}

	public void addCustomWall( CustomTiledVisual visual){
		customWalls.add( visual.create() );
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {

	}

	private void addTrapSprite( Trap trap ) {

	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.level.heroFOV[mob.pos];
		mobs.add( sprite );
		sprite.link( mob );
	}
	
	private synchronized void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt.destroy();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		addToFront( banner );
	}
	
	// -------------------------------------------------------

	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}

	public static void add( Trap trap ) {
		if (scene != null) {
			scene.addTrapSprite( trap );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void add( CharHealthIndicator indicator ){
		if (scene != null) scene.healthIndicators.add(indicator);
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		if (scene != null) {
			Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
			ripple.reset(pos);
			return ripple;
		} else {
			return null;
		}
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item, int pos ) {
		if (scene != null) scene.toolbar.pickup( item, pos );
	}

	public static void pickUpJournal( Item item, int pos ) {
		if (scene != null) scene.pane.pickup( item, pos );
	}
	
	public static void flashJournal(){
		if (scene != null) scene.pane.flash();
	}
	
	public static void updateKeyDisplay(){
		if (scene != null) scene.pane.updateKeys();
	}

	public static void resetMap() {
		if (scene != null) {
			scene.tiles.map(Dungeon.level.map, Dungeon.level.width() );
			scene.visualGrid.map(Dungeon.level.map, Dungeon.level.width() );
			scene.terrainFeatures.map(Dungeon.level.map, Dungeon.level.width() );
			scene.walls.map(Dungeon.level.map, Dungeon.level.width() );
		}
		updateFog();
	}

	//updates the whole map
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updateMap();
			scene.visualGrid.updateMap();
			scene.terrainFeatures.updateMap();
			scene.walls.updateMap();
			updateFog();
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updateMapCell( cell );
			scene.visualGrid.updateMapCell( cell );
			scene.terrainFeatures.updateMapCell( cell );
			scene.walls.updateMapCell( cell );
			//update adjacent cells too
			updateFog( cell, 1 );
		}
	}

	public static void plantSeed( int cell ) {
		if (scene != null) {
			scene.terrainFeatures.growPlant( cell );
		}
	}
	
	//todo this doesn't account for walls right now
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		if (scene != null) {
			cancelCellSelector();
			scene.addToFront(wnd);
		}
	}

	public static void updateFog(){
		if (scene != null) {
			scene.fog.updateFog();
			scene.wallBlocking.updateMap();
		}
	}

	public static void updateFog(int x, int y, int w, int h){
		if (scene != null) {
			scene.fog.updateFogArea(x, y, w, h);
			scene.wallBlocking.updateArea(x, y, w, h);
		}
	}
	
	public static void updateFog( int cell, int radius ){
		if (scene != null) {
			scene.fog.updateFog( cell, radius );
			scene.wallBlocking.updateArea( cell, radius );
		}
	}
	
	public static void afterObserve() {
		if (scene != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null)
					mob.sprite.visible = Dungeon.level.heroFOV[mob.pos];
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		if (scene != null)
			scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd =
				mode == Mode.SEED ?
					WndBag.getBag( VelvetPouch.class, listener, mode, title ) :
				mode == Mode.SCROLL ?
					WndBag.getBag( ScrollHolder.class, listener, mode, title ) :
				mode == Mode.POTION ?
					WndBag.getBag( PotionBandolier.class, listener, mode, title ) :
				mode == Mode.WAND ?
					WndBag.getBag( WandHolster.class, listener, mode, title ) :
				WndBag.lastBag( listener, mode, title );
		
		if (scene != null) scene.addToFront( wnd );
		
		return wnd;
	}
	
	static boolean cancel() {
		if (Dungeon.hero != null && (Dungeon.hero.curAction != null || Dungeon.hero.resting)) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
		if (scene != null && scene.toolbar != null) scene.toolbar.examining = false;
	}

	public static void examineCell( Integer cell ) {
		if (cell == null
				|| cell < 0
				|| cell > Dungeon.level.length()
				|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
			return;
		}

		ArrayList<String> names = new ArrayList<>();
		final ArrayList<Object> objects = new ArrayList<>();

		if (cell == Dungeon.hero.pos) {
			objects.add(Dungeon.hero);
			names.add(Dungeon.hero.className().toUpperCase(Locale.ENGLISH));
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Mob mob = (Mob) Actor.findChar(cell);
				if (mob != null) {
					objects.add(mob);
					names.add(Messages.titleCase( mob.name ));
				}
			}
		}

		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null && heap.seen) {
			objects.add(heap);
			names.add(Messages.titleCase( heap.toString() ));
		}

		Plant plant = Dungeon.level.plants.get( cell );
		if (plant != null) {
			objects.add(plant);
			names.add(Messages.titleCase( plant.plantName ));
		}

		Trap trap = Dungeon.level.traps.get( cell );
		if (trap != null && trap.visible) {
			objects.add(trap);
			names.add(Messages.titleCase( trap.name ));
		}

		if (objects.isEmpty()) {
			GameScene.show(new WndInfoCell(cell));
		} else if (objects.size() == 1){
			examineObject(objects.get(0));
		} else {
			GameScene.show(new WndOptions(Messages.get(GameScene.class, "choose_examine"),
					Messages.get(GameScene.class, "multiple_examine"), names.toArray(new String[names.size()])){
				@Override
				protected void onSelect(int index) {
					examineObject(objects.get(index));
				}
			});

		}
	}

	public static void examineObject(Object o){
		if (o == Dungeon.hero){
			GameScene.show( new WndHero() );
		} else if ( o instanceof Mob ){
			GameScene.show(new WndInfoMob((Mob) o));
		} else if ( o instanceof Heap ){
			Heap heap = (Heap)o;
			if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
				GameScene.show(new WndTradeItem(heap, false));
			} else {
				GameScene.show(new WndInfoItem(heap));
			}
		} else if ( o instanceof Plant ){
			GameScene.show( new WndInfoPlant((Plant) o) );
		} else if ( o instanceof Trap ){
			GameScene.show( new WndInfoTrap((Trap) o));
		} else {
			GameScene.show( new WndMessage( Messages.get(GameScene.class, "dont_know") ) ) ;
		}
	}

	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
