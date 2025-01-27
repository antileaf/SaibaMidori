package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;
import me.antileaf.midori.cards.interfaces.OnPaintedCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class ToxinInfection extends AbstractMidoriCard implements OnPaintedCard {
	public static final String SIMPLE_NAME = ToxinInfection.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int MAGIC = 3;
	private static final int MAGIC2 = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public ToxinInfection() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;

		this.tags.add(CardTagEnum.MIDORI_INFECTION);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(m, p,
				new PoisonPower(m, p, this.magicNumber)));
	}

	@Override
	public void onPainted(Hue hue) {
		if (!this.upgraded)
			this.addToTop(new AnonymousAction(() -> {
				AbstractMonster m = AbstractDungeon.getRandomMonster();
				this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player,
						new PoisonPower(m, AbstractDungeon.player,
								this.secondaryMagicNumber), this.secondaryMagicNumber, true));
			}));
		else {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				MidoriHelper.addActionToBuffer(new ApplyPowerAction(m, AbstractDungeon.player,
						new PoisonPower(m, AbstractDungeon.player,
								this.secondaryMagicNumber), this.secondaryMagicNumber, true));
			}

			MidoriHelper.commitBuffer();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ToxinInfection();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.initializeDescription();
		}
	}
}
