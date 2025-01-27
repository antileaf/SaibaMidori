package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class VolcanicStorm extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = VolcanicStorm.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 3;
	private static final int DAMAGE = 26;
	private static final int UPGRADE_PLUS_DMG = 6;

	public VolcanicStorm() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;

		this.isMultiDamage = true;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new VFXAction(p,
				new ScreenOnFireEffect(), 0.5F));

		this.addToBot(new DamageAllEnemiesAction(
				p,
				this.multiDamage,
				this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.FIRE
		));

		this.addToBot(new PaintAction(Hue.LAVA, p.drawPile.group.stream()
				.filter(c -> c instanceof AbstractMidoriCard)
				.toArray(AbstractCard[]::new)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new VolcanicStorm();
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
