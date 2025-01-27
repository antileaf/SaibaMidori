package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import me.antileaf.midori.actions.common.AOEFatalAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class RisingWaves extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = RisingWaves.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int DAMAGE = 13;
	private static final int DAMAGE2 = 10;
	private static final int UPGRADE_PLUS_DMG2 = 6;

	public RisingWaves() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ALL_ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.secondaryDamage = this.baseSecondaryDamage = DAMAGE2;

		this.isMultiDamage = true;
		this.isMultiSecondaryDamage = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AOEFatalAction(p, this.multiDamage, this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.BLUNT_LIGHT,
				(fatal) -> {
					if (fatal.isEmpty())
						this.addToTop(new DamageAllEnemiesAction(p, this.multiSecondaryDamage,
								this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));

					this.addToTop(new WaitAction(0.1F));
				},
				true));
	}

	@Override
	public AbstractCard makeCopy() {
		return new RisingWaves();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeSecondaryDamage(UPGRADE_PLUS_DMG2);

			this.initializeDescription();
		}
	}
}
