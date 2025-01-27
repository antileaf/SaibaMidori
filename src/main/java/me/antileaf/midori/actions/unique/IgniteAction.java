//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.antileaf.midori.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import me.antileaf.midori.actions.utils.ReplaceCardAction;
import me.antileaf.midori.cards.colorless.Ignited;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class IgniteAction extends ReplaceCardAction {
	private static final Logger logger = LogManager.getLogger(IgniteAction.class.getName());

	public IgniteAction(AbstractCard card, boolean upgraded) {
		super(card, () -> {
			Ignited ignited = new Ignited();
			if (upgraded)
				ignited.upgrade();
			HueManager.paintOutsideCombat(ignited, Hue.LAVA);

			return ignited;
		});
	}
}
