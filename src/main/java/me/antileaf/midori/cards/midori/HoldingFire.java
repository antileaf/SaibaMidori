package me.antileaf.midori.cards.midori;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
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
import java.util.Arrays;
import java.util.List;

public class HoldingFire extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = HoldingFire.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	public HoldingFire() {
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
		ArrayList<AbstractCard> lava = p.hand.group.stream()
				.filter(c -> c != this)
				.filter(c -> HueManager.hasHue(c, Hue.LAVA))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		this.addToBot(new PaintAction(Hue.POMELO, lava));

		if (!this.upgraded)
			this.addToBot(new AnonymousAction(() -> {
				AbstractCard[] discard = p.hand.group.stream()
						.filter(c -> !lava.contains(c))
						.filter(c -> c != this)
						.toArray(AbstractCard[]::new);

				Arrays.stream(discard)
						.forEach(c -> {
							p.hand.moveToDiscardPile(c);
							c.triggerOnManualDiscard();
							GameActionManager.incrementDiscard(false);
						});
			}));
		else
			this.addToBot(new SelectCardsInHandAction(BaseMod.MAX_HAND_SIZE,
					cardStrings.EXTENDED_DESCRIPTION[2],
					true,
					true,
					c -> true,
					(cards) -> {
				List<AbstractCard> selected = new ArrayList<>(cards);

				this.addToTop(new AnonymousAction(() -> {
					System.out.println("Selected cards: " + selected);
					selected.forEach(c -> {
						p.hand.moveToDiscardPile(c);
						c.triggerOnManualDiscard();
						GameActionManager.incrementDiscard(false);
					});
				}));
					}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new HoldingFire();
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
