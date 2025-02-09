package me.antileaf.midori.cards.midori;

import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Fragment_Attack;
import me.antileaf.midori.cards.colorless.Fragment_Defend;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueCardModifier;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Cutting extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Cutting.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int BLOCK = 12;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	private static final int DAMAGE = 12;
	private static final int UPGRADE_PLUS_DAMAGE = 3;

	public Cutting() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF_AND_ENEMY
		);

		this.block = this.baseBlock = BLOCK;
		this.damage = this.baseDamage = DAMAGE;

		MultiCardPreview.add(this, new Fragment_Defend(), new Fragment_Attack());
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));
		this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HEAVY));

		MultiCardPreview.multiCardPreview.get(this).forEach(c -> {
			AbstractCard copy = c.makeStatEquivalentCopy();
			HueManager.paintOutsideCombat(copy, HueManager.getHue(this));
			this.addToBot(new MakeTempCardInHandAction(copy));
		});

		this.purgeOnUse = true;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Cutting();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);

			MultiCardPreview.multiCardPreview.get(this).forEach(AbstractCard::upgrade);

			this.initializeDescription();
		}
	}
}
