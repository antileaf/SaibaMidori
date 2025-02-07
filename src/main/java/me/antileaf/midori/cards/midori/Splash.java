package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.powers.unique.DripPower;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Splash extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Splash.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Splash() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.fixedHue = Hue.INK;
	}

	@Override
	public void applyPowersToBlock() {
		int min = AbstractDungeon.player.hand.group.stream()
				.filter(c -> c != this)
				.filter(c -> c.costForTurn >= 0)
				.mapToInt(c -> c.costForTurn)
				.min()
				.orElse(Integer.MAX_VALUE);

		int count = (int) AbstractDungeon.player.hand.group.stream()
				.filter(c -> c != this)
				.filter(c -> c.costForTurn == min)
				.count();

		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = count;
		this.rawDescription = cardStrings.DESCRIPTION + " " +
				cardStrings.EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	@Override
	public void moveToDiscardPile() {
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int min = p.hand.group.stream()
				.filter(c -> c != this)
				.filter(c -> c.costForTurn >= 0)
				.mapToInt(c -> c.costForTurn)
				.min()
				.orElse(Integer.MAX_VALUE);

		ArrayList<AbstractCard> discard = new ArrayList<>();

		int count = 0;
		for (AbstractCard c : p.hand.group)
			if (c != this && c.costForTurn == min) {
				discard.add(c);
				count++;
			}

		this.addToBot(new AnonymousAction(() -> {
			discard.forEach(c -> {
				p.hand.moveToDiscardPile(c);
				c.triggerOnManualDiscard();
				GameActionManager.incrementDiscard(false);
			});
		}));

		this.addToBot(new WaitAction(0.1F));

		for (int i = 0; i < count; i++)
			this.addToBot(new ApplyPowerAction(m, p,
					new PoisonPower(m, p, this.magicNumber)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Splash();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
