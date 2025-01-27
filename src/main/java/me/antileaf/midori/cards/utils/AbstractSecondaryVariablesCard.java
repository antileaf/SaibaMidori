package me.antileaf.midori.cards.utils;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractSecondaryVariablesCard extends CustomCard {
	public int secondaryMagicNumber = -1;
	public int baseSecondaryMagicNumber = -1;
	public boolean upgradedSecondaryMagicNumber = false;
	public boolean isSecondaryMagicNumberModified = false;

	public int secondaryDamage = -1;
	public int baseSecondaryDamage = -1;
	public int[] multiSecondaryDamage = null;
	public boolean isMultiSecondaryDamage = false;
	public boolean upgradedSecondaryDamage = false;
	public boolean isSecondaryDamageModified = false;

	public int secondaryBlock = -1;
	public int baseSecondaryBlock = -1;
	public boolean upgradedSecondaryBlock = false;
	public boolean isSecondaryBlockModified = false;

	public AbstractSecondaryVariablesCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			CardType type,
			CardColor color,
			CardRarity rarity,
			CardTarget target
	) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (this.secondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalIsMultiDamage = this.isMultiDamage;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			boolean originalDamageModified = this.isDamageModified;

			this.baseDamage = this.baseSecondaryDamage;
			this.isMultiDamage = this.isMultiSecondaryDamage;
			super.applyPowers();
			this.secondaryDamage = this.damage;
			this.multiSecondaryDamage = this.multiDamage;
			this.isSecondaryDamageModified = this.isDamageModified;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isMultiDamage = originalIsMultiDamage;
			this.multiDamage = originalMultiDamage;
			this.isDamageModified = originalDamageModified;
		}
	}

	@Override
	public void applyPowersToBlock() {
		super.applyPowersToBlock();

		if (this.baseSecondaryBlock != -1) {
			int originalBlock = this.block;
			int originalBaseBlock = this.baseBlock;
			boolean originalBlockModified = this.isBlockModified;

			this.baseBlock = this.baseSecondaryBlock;
			super.applyPowersToBlock();
			this.secondaryBlock = this.block;
			this.isSecondaryBlockModified = this.isBlockModified;

			this.block = originalBlock;
			this.baseBlock = originalBaseBlock;
			this.isBlockModified = originalBlockModified;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		if (this.baseSecondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalIsMultiDamage = this.isMultiDamage;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			boolean originalDamageModified = this.isDamageModified;

			this.baseDamage = this.baseSecondaryDamage;
			this.isMultiDamage = this.isMultiSecondaryDamage;
			super.calculateCardDamage(mo);
			this.secondaryDamage = this.damage;
			this.multiSecondaryDamage = this.multiDamage;
			this.isSecondaryDamageModified = this.isDamageModified;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isMultiDamage = originalIsMultiDamage;
			this.multiDamage = originalMultiDamage;
			this.isDamageModified = originalDamageModified;
		}
	}

	@Override
	public void resetAttributes() {
		super.resetAttributes();

		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.isSecondaryMagicNumberModified = false;

		this.secondaryDamage = this.baseSecondaryDamage;
		this.isSecondaryDamageModified = false;

		this.secondaryBlock = this.baseSecondaryBlock;
		this.isSecondaryBlockModified = false;
	}

	@Override
	public void displayUpgrades() {
		super.displayUpgrades();

		if (this.upgradedSecondaryMagicNumber) {
			this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
			this.isSecondaryMagicNumberModified = true;
		}

		if (this.upgradedSecondaryDamage) {
			this.secondaryDamage = this.baseSecondaryDamage;
			this.isSecondaryDamageModified = true;
		}

		if (this.upgradedSecondaryBlock) {
			this.secondaryBlock = this.baseSecondaryBlock;
			this.isSecondaryBlockModified = true;
		}
	}

	public void upgradeSecondaryMagicNumber(int amount) {
		this.baseSecondaryMagicNumber += amount;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.upgradedSecondaryMagicNumber = true;
	}

	public void upgradeSecondaryDamage(int amount) {
		this.baseSecondaryDamage += amount;
		this.secondaryDamage = this.baseSecondaryDamage;
		this.upgradedSecondaryDamage = true;
	}

	public void upgradeSecondaryBlock(int amount) {
		this.baseSecondaryBlock += amount;
		this.secondaryBlock = this.baseSecondaryBlock;
		this.upgradedSecondaryBlock = true;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractSecondaryVariablesCard card = (AbstractSecondaryVariablesCard) super.makeStatEquivalentCopy();

		card.secondaryMagicNumber = this.secondaryMagicNumber;
		card.baseSecondaryMagicNumber = this.baseSecondaryMagicNumber;

		card.secondaryDamage = this.secondaryDamage;
		card.baseSecondaryDamage = this.baseSecondaryDamage;

		card.secondaryBlock = this.secondaryBlock;
		card.baseSecondaryBlock = this.baseSecondaryBlock;

		return card;
	}
}
