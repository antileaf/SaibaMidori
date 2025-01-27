package me.antileaf.midori.cards.midori;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class DesperateFighting extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = DesperateFighting.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int DAMAGE = 22;
	private static final int UPGRADE_PLUS_DMG = 6;

	public DesperateFighting() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ALL_ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.isMultiDamage = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);

		for (int i = 0; i < this.multiDamage.length; i++) {
			AbstractMonster mo = AbstractDungeon.getMonsters().monsters.get(i);
			if (!mo.isDeadOrEscaped() && mo.currentHealth < mo.maxHealth)
				this.addToBot(new DamageAction(mo, new DamageInfo(p, this.multiDamage[i], this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new DesperateFighting();
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
