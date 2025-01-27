package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class DeepInfection extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = DeepInfection.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int MAGIC = 8;
	private static final int MAGIC2 = 1;
	private static final int UPGRADE_PLUS_MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;

	public DeepInfection() {
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
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;

		this.tags.add(CardTagEnum.MIDORI_INFECTION);
	}

	private int countLavaOrInk() {
		return (int) AbstractDungeon.player.hand.group.stream()
				.filter(c -> HueManager.hasHue(c, Hue.LAVA) || HueManager.hasHue(c, Hue.INK))
				.count();
	}

	@Override
	public void applyPowers() {
		int realBaseMagicNumber = this.baseMagicNumber;
		this.baseMagicNumber += this.countLavaOrInk() * this.secondaryMagicNumber;
		this.magicNumber = this.baseMagicNumber;

		super.applyPowers();

		this.baseMagicNumber = realBaseMagicNumber;
		this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(m, p,
				new PoisonPower(m, p, this.magicNumber)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new DeepInfection();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
