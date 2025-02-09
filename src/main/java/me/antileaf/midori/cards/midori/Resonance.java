package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Resonance extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Resonance.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 9;
	private static final int UPGRADE_PLUS_BLOCK = 3;

	public Resonance() {
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

		this.block = this.baseBlock = BLOCK;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));

		this.addToBot(new AnonymousAction(() -> {
			p.hand.group.stream()
					.filter(c -> HueManager.hasHue(c, Hue.SKY))
					.forEach(c -> {
						if (c.costForTurn != 1 || c.cost != 1) {
							c.costForTurn = c.cost = 1;
							c.isCostModified = true;
						}

						c.freeToPlayOnce = false;
					});
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Resonance();
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
