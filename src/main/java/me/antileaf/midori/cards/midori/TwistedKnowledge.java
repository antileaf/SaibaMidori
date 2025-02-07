package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.hue.HueThisCombatPatch;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Drill extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Drill.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int DAMAGE = 14;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_DMG = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Drill() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.BASIC,
				CardTarget.ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void triggerOnGlowCheck() {
		ArrayList<Hue> hues = HueThisCombatPatch.get();
		if (!hues.isEmpty() && HueManager.isHue(hues.get(hues.size() - 1), Hue.LAVA))
			this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		ArrayList<Hue> hues = HueThisCombatPatch.get();
		if (hues.size() > 1 && HueManager.isHue(hues.get(hues.size() - 2), Hue.LAVA))
			this.addToBot(new ApplyPowerAction(m, p,
					new VulnerablePower(m, this.magicNumber, false)));

		if (HueManager.isHue(HueManager.getHue(this), Hue.LAVA))
			this.addToBot(new ApplyPowerAction(m, p,
					new VulnerablePower(m, this.magicNumber, false)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Drill();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
