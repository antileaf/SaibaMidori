package me.antileaf.midori.actions.hue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;

import java.util.ArrayList;

public class RemoveHueAction extends AbstractGameAction {
	private AbstractCard[] cards;

	public RemoveHueAction(AbstractCard... cards) {
		this.cards = cards;

		this.actionType = ActionType.SPECIAL;
		this.duration = 0.1F;
	}

	public RemoveHueAction(ArrayList<AbstractCard> cards) {
		this(cards.toArray(new AbstractCard[0]));
	}

	@Override
	public void update() {
		this.tickDuration();

		if (this.isDone) {
			boolean shouldRefresh = false;

			for (AbstractCard card : this.cards) {
				HueManager.remove(card);

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
