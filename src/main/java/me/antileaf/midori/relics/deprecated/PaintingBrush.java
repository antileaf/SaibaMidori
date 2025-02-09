package me.antileaf.midori.relics.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

@Deprecated
@AutoAdd.Ignore
public class PaintingBrush extends CustomRelic {
	public static final String SIMPLE_NAME = PaintingBrush.class.getSimpleName();

	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final String IMG = MidoriHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = MidoriHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = MidoriHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	private boolean firstTurn = false;

	public PaintingBrush() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.STARTER,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
//		this.tips.add(new PowerTip(this.DESCRIPTIONS[2], this.DESCRIPTIONS[1]));
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	@Override
	public void atBattleStart() {
		this.firstTurn = true;
	}

	@Override
	public void atBattleStartPreDraw() {
		AbstractDungeon.player.gameHandSize -= 2;
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

		ArrayList<AbstractCard> cards = new ArrayList<>();
		for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
			if (HueManager.hasAllHues(c))
				continue;

			if (cards.stream().noneMatch(other -> HueManager.getHue(other) == HueManager.getHue(c)))
				cards.add(c);

			if (cards.size() == 3)
				break;
		}

		this.addToBot(new FilteredDrawCardAction(cards.size(),
				cards::contains, true, null));
	}

	@Override
	public void atTurnStartPostDraw() {
		if (this.firstTurn) {
			this.firstTurn = false;

			this.addToBot(new AnonymousAction(() -> {
				AbstractDungeon.player.gameHandSize += 2;
			}));
		}
	}

	@Override
	public void onVictory() {
		this.firstTurn = false;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new PaintingBrush();
	}
}
