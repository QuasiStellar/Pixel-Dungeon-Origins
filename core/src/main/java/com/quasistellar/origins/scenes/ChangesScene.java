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
import com.quasistellar.origins.Chrome;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.effects.BadgeBanner;
import com.quasistellar.origins.items.Ankh;
import com.quasistellar.origins.items.DewVial;
import com.quasistellar.origins.items.Honeypot;
import com.quasistellar.origins.items.Item;
import com.quasistellar.origins.items.Stylus;
import com.quasistellar.origins.items.Torch;
import com.quasistellar.origins.items.armor.PlateArmor;
import com.quasistellar.origins.items.artifacts.CloakOfShadows;
import com.quasistellar.origins.items.artifacts.DriedRose;
import com.quasistellar.origins.items.artifacts.EtherealChains;
import com.quasistellar.origins.items.artifacts.HornOfPlenty;
import com.quasistellar.origins.items.artifacts.TimekeepersHourglass;
import com.quasistellar.origins.items.artifacts.UnstableSpellbook;
import com.quasistellar.origins.items.food.Blandfruit;
import com.quasistellar.origins.items.food.Food;
import com.quasistellar.origins.items.potions.PotionOfHealing;
import com.quasistellar.origins.items.rings.RingOfElements;
import com.quasistellar.origins.items.rings.RingOfEnergy;
import com.quasistellar.origins.items.rings.RingOfEvasion;
import com.quasistellar.origins.items.rings.RingOfMight;
import com.quasistellar.origins.items.rings.RingOfSharpshooting;
import com.quasistellar.origins.items.rings.RingOfWealth;
import com.quasistellar.origins.items.stones.StoneOfAcidity;
import com.quasistellar.origins.items.wands.WandOfCorrosion;
import com.quasistellar.origins.items.wands.WandOfCorruption;
import com.quasistellar.origins.items.weapon.melee.Broadsword;
import com.quasistellar.origins.items.weapon.melee.Claymore;
import com.quasistellar.origins.items.weapon.melee.Dirk;
import com.quasistellar.origins.items.weapon.melee.Flail;
import com.quasistellar.origins.items.weapon.melee.Glaive;
import com.quasistellar.origins.items.weapon.melee.Greataxe;
import com.quasistellar.origins.items.weapon.melee.Staff;
import com.quasistellar.origins.messages.Messages;
import com.quasistellar.origins.sprites.CharSprite;
import com.quasistellar.origins.sprites.ItemSprite;
import com.quasistellar.origins.sprites.ItemSpriteSheet;
import com.quasistellar.origins.ui.Archs;
import com.quasistellar.origins.ui.ExitButton;
import com.quasistellar.origins.ui.Icons;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.quasistellar.origins.ui.ScrollPane;
import com.quasistellar.origins.ui.Window;
import com.quasistellar.origins.windows.WndTitledMessage;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

	private final ArrayList<ChangeInfo> infos = new ArrayList<>();

	@Override
	public void create() {
		super.create();

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedText title = PixelScene.renderText( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.x = (w - title.width()) / 2f ;
		title.y = (16 - title.baseLine()) / 2f;
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 10;
		int ph = h - 16;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.y + title.height();
		align( panel );
		add( panel );

		ScrollPane list = new ScrollPane( new Component() ){

			@Override
			public void onClick(float x, float y) {
				for (ChangeInfo info : infos){
					if (info.onClick( x, y )){
						return;
					}
				}
			}

		};
		add( list );

		//**********************
		//     v0.1-indev
		//**********************

		ChangeInfo changes = new ChangeInfo("v0.1-indev", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.ORIGINS), "Developer Commentary",
				"- Вышла 3 апреля 2019\n" +
						"- Через 442 дней после начала разработки"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE, null), "Strength",
						"Сила убрана из игры. Зелья силы больше не встречаются в подземельях, и зелье могущества недоступно. " +
						"Характеристики снаряжения теперь зависят от уровня героя, а не от его силы."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_TURQUOISE, null), "Spirit",
				"В игру введена шкала духа!\n\n" +
						"_-_ Дух тратится на использование навыков, умений, и натиска с помощью оружия\n" +
						"_-_ Максимальное значение духа зависит от уровня героя\n" +
						"_-_ Когда дух полностью исчерпан, герой получает эффект усталости, наносящий урон со временем\n" +
						"_-_ Дух можно восстановить, поспав - он восполняется в два раза быстрее здоровья\n" +
						"_-_ 3 единицы духа тратятся на поиск\n" +
						"_-_ При практически заполненной шкале духа, герой получает эффект бодрости, ускоряющий регенерацию и поиск ловушек\n" +
						"_-_ Добавлен значок за смерть от переутомления"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DART, null), "Ballistica",
				"Теперь герой и монстры могут заглядывать и стрелять из за угла!"));

		changes.addButton( new ChangeButton(Icons.get(Icons.BACKPACK), "Backpack",
				"_-_ Инвентарь героя расширен до 30 мест! Размер его интерфейса подстраивается под количество предметов в нём\n\n" +
						"_-_ Количество слотов для колец и артефактов увеличено до трёх\n" +
						"_-_ Герой начинает игру с мешочком для семян, который теперь хранит рунные камни"));

		changes.addButton( new ChangeButton(new Image(Assets.ENCHANTER, 0, 15, 12, 15), "New Classes!",
				"Кавалер\n" +
						"_-_ Невидим в высокой траве\n" +
						"_-_ Внезапные атаки наносят удвоенный урон\n" +
						"_-_ Навык: Прыжок\n" +
						"_-_ Умение: Обман\n\n" +
						"Чародей\n" +
						"_-_ Восстанавливает дух со временем\n" +
						"_-_ Начинает игру с уникальным посохом\n" +
						"_-_ Навык: Оплетение\n" +
						"_-_ Умение: Аура\n\n" +
						"Странник\n" +
						"_-_ Обладает увеличенной дальностью обзора и радиусом поиска\n" +
						"_-_ Слышит врагов за стенами\n" +
						"_-_ Навык: Иллюзия\n" +
						"_-_ Умение: Бег"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FLAIL, null), "Rush",
				"Оружие ближнего боя переработано и разделено на шесть категорий:\n" +
						"_-_ Кастеты\n" +
						"_-_ Кинжалы\n" +
						"_-_ Мечи\n" +
						"_-_ Топоры\n" +
						"_-_ Булавы\n" +
						"_-_ Копья\n\n" +
						"Теперь оружие позволяет нанести мощный удар - натиск - зависящий от категории:\n" +
						"_-_ Град\n" +
						"_-_ Убийство\n" +
						"_-_ Выпад\n" +
						"_-_ Рассечение\n" +
						"_-_ Сотрясение\n" +
						"_-_ Бросок"));

		changes.addButton( new ChangeButton(new Image(Assets.TILES_SEWERS, 208, 0, 16, 16), "Grass & Dew",
				"_-_ Временно убрано выпадение семен и росы из высокой травы\n" +
						"_-_ Из генерации убран флакон для росы"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_DAGAZ, null), "Runestones",
				"В игру добавлены рунные камни, которые можно прикреплять к оружию ближнего боя. " +
						"Количество доступных слотов определяется порядком оружия. Оружие на полу подземелья может содержать камни. " +
						"Открепить камни нельзя, можно лишь разбить, освободив место для новых."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STILETTO, null), "Durability",
				"Введена прочность для оружия ближнего боя. Полной прочности хватает на 100 ударов, причём наносимый урон " +
						"начинает уменьшаться лишь при прочности менее 50, тогда же изменяется цвет индикатора."));

		changes.addButton( new ChangeButton(new Image(Assets.TILES_SEWERS, 240, 0, 16, 16), "Forge",
				"На чётных этажах подземелья появилась кузница с наковальней. " +
						"С помощью кузницы можно разбирать оружие ближнего боя на составные части и повышать порядок оружия, соединяя его с запчастями."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ, null), "Upgrades",
				"Из игры убраны свитки улучшения. Теперь улучшенное оружие генерируется чаще, причём чем глубже в подземелье, " +
						"тем больше вероятность найти улучшенный предмет, а также его потенциальный уровень улучшения. Уровень оружия, " +
						"как и уровень героя, влияет на урон оружия."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Miscellaneous Changes",
				"_-_ Магазины теперь встечаются чаще\n" +
						"_-_ Анх временно убран из игры\n" +
						"_-_ Надевание и снятие предметов теперь длится полхода\n" +
						"_-_ Кровотечение больше не останавливается само, осторожнее с прыжками в пропасть\n" +
						"_-_ Максимальное здоровье и дух увеличиваются на 2 при повышении уровня"));

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "Translations",
				"Переводы временно недоступны."));

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second =false;
		for (ChangeInfo info : infos){
			if (info.major) {
				posY = nextPosY;
				second = false;
				info.setRect(0, posY, panel.innerWidth(), 0);
				content.add(info);
				posY = nextPosY = info.bottom();
			} else {
				if (!second){
					second = true;
					info.setRect(0, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = info.bottom();
				} else {
					second = false;
					info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = Math.max(info.bottom(), nextPosY);
					posY = nextPosY;
				}
			}
		}


		content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		Origins.switchNoFade(TitleScene.class);
	}

	private static class ChangeInfo extends Component {

		protected ColorBlock line;

		private RenderedText title;
		private boolean major;

		private RenderedTextMultiline text;

		private ArrayList<ChangeButton> buttons = new ArrayList<>();

		public ChangeInfo( String title, boolean majorTitle, String text){
			super();
			
			if (majorTitle){
				this.title = PixelScene.renderText( title, 9 );
				line = new ColorBlock( 1, 1, 0xFF222222);
				add(line);
			} else {
				this.title = PixelScene.renderText( title, 6 );
				line = new ColorBlock( 1, 1, 0xFF333333);
				add(line);
			}
			major = majorTitle;

			add(this.title);

			if (text != null && !text.equals("")){
				this.text = PixelScene.renderMultiline(text, 6);
				add(this.text);
			}

		}

		public void hardlight( int color ){
			title.hardlight( color );
		}

		public void addButton( ChangeButton button ){
			buttons.add(button);
			add(button);

			button.setSize(16, 16);
			layout();
		}

		public boolean onClick( float x, float y ){
			for( ChangeButton button : buttons){
				if (button.inside(x, y)){
					button.onClick();
					return true;
				}
			}
			return false;
		}

		@Override
		protected void layout() {
			float posY = this.y + 2;
			if (major) posY += 2;

			title.x = x + (width - title.width()) / 2f;
			title.y = posY;
			PixelScene.align( title );
			posY += title.baseLine() + 2;

			if (text != null) {
				text.maxWidth((int) width());
				text.setPos(x, posY);
				posY += text.height();
			}

			float posX = x;
			float tallest = 0;
			for (ChangeButton change : buttons){

				if (posX + change.width() >= right()){
					posX = x;
					posY += tallest;
					tallest = 0;
				}

				//centers
				if (posX == x){
					float offset = width;
					for (ChangeButton b : buttons){
						offset -= b.width();
						if (offset <= 0){
							offset += b.width();
							break;
						}
					}
					posX += offset / 2f;
				}

				change.setPos(posX, posY);
				posX += change.width();
				if (tallest < change.height()){
					tallest = change.height();
				}
			}
			posY += tallest + 2;

			height = posY - this.y;
			
			if (major) {
				line.size(width(), 1);
				line.x = x;
				line.y = y+2;
			} else if (x == 0){
				line.size(1, height());
				line.x = width;
				line.y = y;
			} else {
				line.size(1, height());
				line.x = x;
				line.y = y;
			}
		}
	}

	//not actually a button, but functions as one.
	private static class ChangeButton extends Component {

		protected Image icon;
		protected String title;
		protected String message;

		public ChangeButton( Image icon, String title, String message){
			super();
			
			this.icon = icon;
			add(this.icon);

			this.title = Messages.titleCase(title);
			this.message = message;

			layout();
		}

		public ChangeButton( Item item, String message ){
			this( new ItemSprite(item), item.name(), message);
		}

		protected void onClick() {
			Origins.scene().add(new ChangesWindow(new Image(icon), title, message));
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + (width - icon.width) / 2f;
			icon.y = y + (height - icon.height) / 2f;
			PixelScene.align(icon);
		}
	}
	
	private static class ChangesWindow extends WndTitledMessage {
	
		public ChangesWindow( Image icon, String title, String message ) {
			super( icon, title, message);
			
			add( new TouchArea( chrome ) {
				@Override
				protected void onClick( Touchscreen.Touch touch ) {
					hide();
				}
			} );
			
		}
		
	}
}
