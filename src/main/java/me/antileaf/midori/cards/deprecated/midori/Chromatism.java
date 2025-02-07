package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class Chromatism extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Chromatism.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 9;
	private static final int UPGRADE_PLUS_BLOCK = 3;

	public Chromatism() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.SELF
		);

		this.block = this.baseBlock = BLOCK;
	}

	private int countHuesInHand() {
		Set<Hue> hues = new HashSet<>();

		for (AbstractCard c : AbstractDungeon.player.hand.group)
			for (Hue hue : Hue.values())
				if (HueManager.hasHue(c, hue))
					hues.add(hue);

		return hues.size();
	}

	@Override
	public void triggerOnGlowCheck() {
		if (this.countHuesInHand() >= 3)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));

		if (this.countHuesInHand() >= 3)
			this.addToBot(new IncreaseMaxOrbAction(1));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Chromatism();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeBlock(UPGRADE_PLUS_BLOCK);

			this.initializeDescription();
		}
	}
}
