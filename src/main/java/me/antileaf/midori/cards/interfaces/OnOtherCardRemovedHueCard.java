package me.antileaf.midori.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.hue.Hue;

public interface OnOtherCardRemovedHueCard {
	void onOtherCardRemovedHue(AbstractCard other, Hue removed);
}
