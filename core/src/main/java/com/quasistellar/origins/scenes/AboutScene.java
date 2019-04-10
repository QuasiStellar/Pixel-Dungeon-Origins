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

import android.content.Intent;
import android.net.Uri;

import com.quasistellar.origins.SPDSettings;
import com.quasistellar.origins.Origins;
import com.quasistellar.origins.effects.Flare;
import com.quasistellar.origins.ui.Archs;
import com.quasistellar.origins.ui.ExitButton;
import com.quasistellar.origins.ui.Icons;
import com.quasistellar.origins.ui.RenderedTextMultiline;
import com.quasistellar.origins.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;

public class AboutScene extends PixelScene {

	private static final String TTL_QSR  = "Pixel Dungeon Origins";

	private static final String TXT_QSR  =
			"Code & Graphics: QuasiStellar\n" +
			"Music: Dr.Blacker";

	private static final String LNK_QSR = "quasistellar.com";

	private static final String TTL_SHPX = "Shattered Pixel Dungeon";

	private static final String TXT_SHPX =
			"Code, & Graphics: Evan";

	private static final String TTL_WATA = "Pixel Dungeon";

	private static final String TXT_WATA =
			"Code & Graphics: Watabou\n" +
			"Music: Cube_Code";
	
	@Override
	public void create() {
		super.create();

		final float colWidth = Camera.main.width / (SPDSettings.landscape() ? 3 : 1);
		final float colTop = (Camera.main.height / 2) - (SPDSettings.landscape() ? 30 : 100);
		final float shpxOffset = SPDSettings.landscape() ? colWidth : 0;
		final float wataOffset = SPDSettings.landscape() ? colWidth * 2 : 0;

		Image qsr = Icons.ORIGINS.get();
		qsr.x = (colWidth - qsr.width()) / 2;
		qsr.y = colTop;
		align(qsr);
		add(qsr);

		new Flare( 7, 64 ).color( 0x4A4A4A, true ).show( qsr, 0 ).angularSpeed = +20;

		RenderedText qsrtitle = renderText( TTL_QSR, 8 );
		qsrtitle.hardlight( Window.TITLE_COLOR );
		add( qsrtitle );

		qsrtitle.x = (colWidth - qsrtitle.width()) / 2;
		qsrtitle.y = qsr.y + qsr.height + 5;
		align(qsrtitle);

		RenderedTextMultiline qsrtext = renderMultiline( TXT_QSR, 8 );
		qsrtext.maxWidth((int)Math.min(colWidth, 120));
		add( qsrtext );

		qsrtext.setPos((colWidth - qsrtext.width()) / 2, qsrtitle.y + qsrtitle.height() + 6);
		align(qsrtext);

		RenderedTextMultiline qsrlink = renderMultiline( LNK_QSR, 8 );
		qsrlink.maxWidth(qsrtext.maxWidth());
		qsrlink.hardlight( Window.TITLE_COLOR );
		add( qsrlink );

		qsrlink.setPos((colWidth - qsrlink.width()) / 2, qsrtext.bottom() + 6);
		align(qsrlink);

		TouchArea qsrhotArea = new TouchArea( qsrlink.left(), qsrlink.top(), qsrlink.width(), qsrlink.height() ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "https://quasistellar.neocities.org/origins.html" ) );
				Game.instance.startActivity( intent );
			}
		};
		add( qsrhotArea );

		Image shpx = Icons.SHPX.get();
		shpx.x = shpxOffset + (colWidth - shpx.width()) / 2;
		shpx.y = SPDSettings.landscape() ?
				colTop:
				qsrlink.top() + shpx.height + 20;
		align(shpx);
		add( shpx );

		new Flare( 7, 64 ).color( 0x225511, true ).show( shpx, 0 ).angularSpeed = +20;

		RenderedText shpxtitle = renderText( TTL_SHPX, 8 );
		shpxtitle.hardlight( Window.TITLE_COLOR );
		add( shpxtitle );

		shpxtitle.x = shpxOffset + (colWidth - shpxtitle.width()) / 2;
		shpxtitle.y = shpx.y + shpx.height + 5;
		align(shpxtitle);

		RenderedTextMultiline shpxtext = renderMultiline( TXT_SHPX, 8 );
		shpxtext.maxWidth((int)Math.min(colWidth, 120));
		add( shpxtext );

		shpxtext.setPos(shpxOffset + (colWidth - shpxtext.width()) / 2, shpxtitle.y + shpxtitle.height() + 6);
		align(shpxtext);

		Image wata = Icons.WATA.get();
		wata.x = wataOffset + (colWidth - wata.width()) / 2;
		wata.y = SPDSettings.landscape() ?
						colTop:
						shpxtext.top() + wata.height + 20;
		align(wata);
		add( wata );

		new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

		RenderedText wataTitle = renderText( TTL_WATA, 8 );
		wataTitle.hardlight(Window.TITLE_COLOR);
		add( wataTitle );

		wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
		wataTitle.y = wata.y + wata.height + 11;
		align(wataTitle);

		RenderedTextMultiline wataText = renderMultiline( TXT_WATA, 8 );
		wataText.maxWidth((int)Math.min(colWidth, 120));
		add( wataText );

		wataText.setPos(wataOffset + (colWidth - wataText.width()) / 2, wataTitle.y + wataTitle.height() + 6);
		align(wataText);

		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		Origins.switchNoFade(TitleScene.class);
	}
}
