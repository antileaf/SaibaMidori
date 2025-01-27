package me.antileaf.midori.cards.midori;

import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;
import java.util.List;

public class FullBloom extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = FullBloom.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int DAMAGE = 11;

	public FullBloom() {
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
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (HueManager.getHue(this) == Hue.POMELO)
			return null;

		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0],
				cardStrings.EXTENDED_DESCRIPTION[1]));
		return tips;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		ArrayList<AbstractCard> cards = new ArrayList<>();
		p.hand.group.stream()
				.filter(c -> c != this)
				.findFirst()
				.ifPresent(cards::add);

		if (this.upgraded) {
			for (int i = p.hand.size() - 1; i >= 0; i--) {
				AbstractCard c = p.hand.group.get(i);
				if (c != this && !cards.contains(c)) {
					cards.add(c);
					break;
				}
			}
		}

		if (!cards.isEmpty())
			this.addToBot(new PaintAction(Hue.POMELO, cards));
	}

	@Override
	public AbstractCard makeCopy() {
		return new FullBloom();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
