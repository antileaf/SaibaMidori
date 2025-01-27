package me.antileaf.midori.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface BetterAtDamageFinalGivePower {
	// 不会再调用原版的 atDamageFinalGive
	float betterAtDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target);
}
