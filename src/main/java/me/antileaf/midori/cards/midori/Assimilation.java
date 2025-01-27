package me.antileaf.midori.cards.midori;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.common.OrderedSelectCardsInHandAction;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Assimilation extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Assimilation.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int BLOCK = 5;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Assimilation() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);

		this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsInHandAction(
				this.magicNumber,
				cardStrings.EXTENDED_DESCRIPTION[0],
				true,
				true,
				card -> true,
				(cards) -> {
					cards.forEach(c -> {
						c.upgrade();
						c.flash();
					});

					for (CardGroup group : new CardGroup[] {
							p.drawPile,
							p.hand,
							p.discardPile
					}) {
						group.group.forEach(c -> {
							if (!cards.contains(c) && c != this) {
								boolean same = HueManager.getHue(c) == HueManager.getHue(this);
								if (!same)
									for (Hue hue : Hue.values())
										if (HueManager.hasHue(c, hue) && HueManager.hasHue(this, hue)) {
											same = true;
											break;
										}

								if (same) {
									c.upgrade();
									if (AbstractDungeon.player.hand.contains(c))
										c.flash();
								}
							}
						});
					}
				}
		));

		this.addToBot(new GainBlockAction(p, p, this.block));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Assimilation();
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
