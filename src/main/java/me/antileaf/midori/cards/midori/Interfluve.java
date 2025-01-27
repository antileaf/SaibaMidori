package me.antileaf.midori.cards.midori;

import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.MaxHandSizePatch;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.common.OrderedSelectCardsInHandAction;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Interfluve extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Interfluve.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int DAMAGE = 6;

	public Interfluve() {
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
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_VERTICAL));

		this.addToBot(new AnonymousAction(() -> AbstractDungeon.player.hand.refreshHandLayout()));

		this.addToBot(new OrderedSelectCardsInHandAction(
				1,
				cardStrings.EXTENDED_DESCRIPTION[0],
				false,
				false,
				card -> true,
				(cards) -> {
					if (cards.isEmpty())
						return;

					AbstractCard pivot = cards.get(0);

					this.addToTop(new AnonymousAction(() -> {
						int index = p.hand.group.indexOf(pivot);
						ArrayList<AbstractCard> lava = new ArrayList<>();
						ArrayList<AbstractCard> sky = new ArrayList<>();

						for (int i = 0; i < index; i++)
							lava.add(p.hand.group.get(i));

						for (int i = index + 1; i < p.hand.group.size(); i++)
							sky.add(p.hand.group.get(i));

						this.addToTop(new PaintAction(Hue.SKY, sky));
						this.addToTop(new PaintAction(Hue.LAVA, lava));
					}));
				}
		));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Interfluve();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
