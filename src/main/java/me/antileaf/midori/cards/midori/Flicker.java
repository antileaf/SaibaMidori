package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Flicker extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Flicker.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	public Flicker() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);

		this.returnToHand = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int index = p.hand.group.indexOf(this); // 如果不在手牌里也会是 -1

		ArrayList<AbstractCard> cards = p.hand.group.stream()
				.filter(c -> p.hand.group.indexOf(c) > index)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		this.addToBot(new PaintAction(Hue.MINT, cards));

		this.addToBot(new AnonymousAction(() -> cards.forEach(AbstractCard::upgrade)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Flicker();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.initializeDescription();
		}
	}
}
