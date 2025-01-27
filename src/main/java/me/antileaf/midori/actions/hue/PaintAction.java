package me.antileaf.midori.actions.hue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;

import java.util.ArrayList;
import java.util.List;

public class PaintAction extends AbstractGameAction {
	private Hue hue;
	private AbstractCard[] cards;

	public PaintAction(Hue hue, AbstractCard... cards) {
		this.hue = hue;
		this.cards = cards;

		this.actionType = ActionType.SPECIAL;
		this.duration = 0.1F;
	}

	public PaintAction(Hue hue, List<AbstractCard> cards) {
		this(hue, cards.toArray(new AbstractCard[0]));
	}

	@Override
	public void update() {
		this.tickDuration();

		if (this.isDone) {
			boolean shouldRefresh = false;

			for (AbstractCard card : this.cards) {
				HueManager.paint(card, this.hue);

				if (AbstractDungeon.player.hand.contains(card)) {
					card.superFlash();
					shouldRefresh = true;
				}
			}

			if (shouldRefresh)
				AbstractDungeon.player.hand.refreshHandLayout();
		}
	}
}
