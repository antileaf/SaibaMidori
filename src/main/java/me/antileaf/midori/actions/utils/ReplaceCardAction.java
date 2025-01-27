package me.antileaf.midori.actions.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.cards.colorless.Ignited;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ReplaceCardAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(ReplaceCardAction.class.getName());

	private final AbstractCard card;
	private final Supplier<AbstractCard> replacement;

	public ReplaceCardAction(AbstractCard card, Supplier<AbstractCard> replacement) {
		this.card = card;
		this.replacement = replacement;

		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public ReplaceCardAction(AbstractCard card, AbstractCard replacement) {
		this(card, () -> replacement);
	}

	@Override
	public void update() {
		if (!this.isDone) {

			CardGroup group = null;

			if (AbstractDungeon.player.hand.contains(this.card))
				group = AbstractDungeon.player.hand;
			else if (AbstractDungeon.player.drawPile.contains(this.card))
				group = AbstractDungeon.player.drawPile;
			else if (AbstractDungeon.player.discardPile.contains(this.card))
				group = AbstractDungeon.player.discardPile;
			else if (AbstractDungeon.player.exhaustPile.contains(this.card))
				group = AbstractDungeon.player.exhaustPile;

			if (group == null) {
				logger.error("ReplaceCardAction: Card not found in any group.");
				this.isDone = true;
				return;
			}

			AbstractCard rep = this.replacement.get();

			rep.current_x = this.card.current_x;
			rep.current_y = this.card.current_y;
			rep.target_x = this.card.target_x;
			rep.target_y = this.card.target_y;
			rep.drawScale = this.card.drawScale;
			rep.targetDrawScale = this.card.targetDrawScale;
			rep.angle = this.card.angle;

			int index = group.group.indexOf(this.card);
			group.group.set(index, rep);

			if (group.type == CardGroup.CardGroupType.HAND)
				rep.flash();

			this.isDone = true;
		}
	}
}
