package me.antileaf.midori.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.hue.Hue;

public interface OnCardRemovedHuePower {
	void onCardRemovedHue(AbstractCard card, Hue hue);
}
