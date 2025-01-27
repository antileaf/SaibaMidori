package me.antileaf.midori.powers.unique;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class FirePlumesHeartPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = FirePlumesHeartPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public FirePlumesHeartPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.type == AbstractCard.CardType.ATTACK) {
			this.flash();

			int damage = card.costForTurn;

			AbstractCard copy = card.makeStatEquivalentCopy();
			copy.damage = copy.baseDamage = damage;

			if (action.target != null) {
				copy.calculateCardDamage((AbstractMonster) action.target);

				for (int i = 0; i < this.amount; i++)
					this.addToBot(new DamageAction(action.target,
							new DamageInfo(this.owner, copy.damage, copy.damageTypeForTurn),
							AbstractGameAction.AttackEffect.FIRE));
			}
			else if (ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage")) {
				copy.calculateCardDamage(null);
				for (int i = 0; i < this.amount; i++)
					this.addToBot(new DamageAllEnemiesAction(this.owner, copy.multiDamage, copy.damageTypeForTurn,
							AbstractGameAction.AttackEffect.FIRE));
			}
			else {
				for (int i = 0; i < this.amount; i++)
					this.addToBot(new AttackDamageRandomEnemyAction(copy, AbstractGameAction.AttackEffect.FIRE));
			}
		}
	}
}
