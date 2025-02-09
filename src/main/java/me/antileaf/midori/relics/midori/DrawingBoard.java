package me.antileaf.midori.relics.midori;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.common.LessDrawPower;
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
	public void onPlayerEndTurn() {
		if (!AbstractDungeon.player.hand.isEmpty() &&
				!AbstractDungeon.player.hasRelic(RunicPyramid.ID) &&
				!AbstractDungeon.player.hasPower(EquilibriumPower.POWER_ID)) {
			this.flash();
			this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

			if (!AbstractDungeon.player.hasPower(RetainCardPower.POWER_ID))
				this.addToBot(new SelectCardsInHandAction(2, this.DESCRIPTIONS[1],
						true, true,
						c -> !c.isEthereal,
						cards -> {
					if (cards.isEmpty())
						return;

					cards.forEach(c -> c.retain = true);
					this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
							new LessDrawPower(AbstractDungeon.player, cards.size())));
						}));
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new DrawingBoard();
	}
}
