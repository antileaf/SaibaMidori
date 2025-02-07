package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class CrimsonImpact extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = CrimsonImpact.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int DAMAGE = 6;
	private static final int UPGRADE_PLUS_DMG = 3;

	public CrimsonImpact() {
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
	public void applyPowersToBlock() {
		this.magicNumber = this.baseMagicNumber = (int)AbstractDungeon.player.hand.group.stream().filter(c -> HueManager.hasHue(c, Hue.LAVA)).count();

		super.applyPowersToBlock();

		this.rawDescription = cardStrings.DESCRIPTION + " NL " + cardStrings.EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int count = (int)p.hand.group.stream().filter(c -> HueManager.hasHue(c, Hue.LAVA)).count();

		p.hand.group.stream()
				.filter(c -> HueManager.hasHue(c, Hue.LAVA))
				.forEach(c -> {
					addToBot(new RemoveHueAction(c));
					addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
							AbstractGameAction.AttackEffect.BLUNT_LIGHT));
				});
	}

	@Override
	public AbstractCard makeCopy() {
		return new CrimsonImpact();
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
