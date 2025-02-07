package me.antileaf.midori.relics.midori;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class DrawingBoard extends CustomRelic {
	public static final String SIMPLE_NAME = DrawingBoard.class.getSimpleName();

	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final String IMG = MidoriHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = MidoriHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = MidoriHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	public DrawingBoard() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.STARTER,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	@Override
	public void atBattleStart() {
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

		this.addToBot(new MakeTempCardInHandAction(new Paint()));
	}

	@Override
	public AbstractRelic makeCopy() {
		return new DrawingBoard();
	}
}
