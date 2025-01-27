package me.antileaf.midori.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.hue.Hue;

public interface OnCardPaintedPower {
	void onCardPainted(AbstractCard card, Hue hue);
}
