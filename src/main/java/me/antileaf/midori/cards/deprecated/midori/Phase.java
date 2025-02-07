package me.antileaf.midori.cards.deprecated.midori;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class Phase extends AbstractMidoriCard implements ConditionalExhaustCard {
	public static final String SIMPLE_NAME = Phase.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	private boolean shouldExhaustThisTime = false;

	public Phase() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);
	}

//	@Override
//	public List<TooltipInfo> getCustomTooltips() {
//		if (!this.upgraded)
//			return null;
//
//		ArrayList<TooltipInfo> tips = new ArrayList<>();
//		tips.add(new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0],
//				cardStrings.EXTENDED_DESCRIPTION[1]));
//		return tips;
//	}

	int getCount(boolean playing) {
		int count = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();

		if (!playing)
			count++;

		return count;
	}

	@Override
	public void triggerOnGlowCheck() {
		int count = this.getCount(false);
		if (count % 5 == 0)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else if (count % 5 % 2 == 1)
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = Color.RED.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int count = this.getCount(true);

		if (count % 5 == 0) {
			this.shouldExhaustThisTime = true;
			this.addToBot(new ChangeStanceAction(DivinityStance.STANCE_ID));
		}
		else if (count % 5 % 2 == 1)
			this.addToBot(new ChangeStanceAction(CalmStance.STANCE_ID));
		else
			this.addToBot(new ChangeStanceAction(WrathStance.STANCE_ID));
	}

	@Override
	public boolean shouldExhaust() {
		boolean ret = this.shouldExhaustThisTime;
		this.shouldExhaustThisTime = false;
		return ret;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Phase();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.fixedHue = Hue.VIOLET;
			if (HueManager.getHue(this) != Hue.VIOLET)
				HueManager.paintOutsideCombat(this, Hue.VIOLET);

			this.initializeDescription();
		}
	}
}
