package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.NoChoice;
import me.antileaf.midori.cards.colorless.YesChoice;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class VineWinding extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = VineWinding.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int DAMAGE = 9;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_DMG = 3;

	public VineWinding() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

		ArrayList<AbstractCard> choices = new ArrayList<>();
		choices.add(new YesChoice());
		choices.add(new NoChoice());

		if (!HueManager.hasAnyHue(this))
			this.addToBot(new ChooseOneCallbackAction(
					choices,
					chosen -> {
						if (chosen instanceof YesChoice)
							this.addToTop(new FilteredDrawCardAction(
									VineWinding.this.magicNumber,
									card -> !HueManager.hasAnyHue(card),
									true,
									null));
						else
							this.addToTop(new FilteredDrawCardAction(
									VineWinding.this.magicNumber,
									HueManager::hasAnyHue,
									true,
									null));
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					false
			));
		else {
			boolean[] has = new boolean[Hue.values().length];
			for (Hue hue : Hue.values())
				has[hue.ordinal()] = HueManager.hasHue(this, hue);

			this.addToBot(new ChooseOneCallbackAction(
					choices,
					chosen -> {
						if (chosen instanceof YesChoice)
							this.addToTop(new FilteredDrawCardAction(
									VineWinding.this.magicNumber,
									card -> {
										for (Hue hue : Hue.values())
											if (has[hue.ordinal()] && HueManager.hasHue(card, hue))
												return true;
										return false;
									},
									true,
									null));
						else
							this.addToTop(new FilteredDrawCardAction(
									VineWinding.this.magicNumber,
									card -> {
										for (Hue hue : Hue.values())
											if (has[hue.ordinal()] != HueManager.hasHue(card, hue))
												return true;
										return false;
									},
									true,
									null));
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					false
			));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new VineWinding();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.initializeDescription();
		}
	}
}
