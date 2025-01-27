package me.antileaf.midori.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MoveToHandAction extends AbstractGameAction {
	private CardGroup source;
	private final Predicate<AbstractCard> filter;
	private final Consumer<ArrayList<AbstractCard>> callback;

	public MoveToHandAction(CardGroup source, int amount, Predicate<AbstractCard> filter, Consumer<ArrayList<AbstractCard>> callback) {
		this.source = source;
		this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
		this.filter = filter;
		this.callback = callback;

		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = this.startDuration = Settings.ACTION_DUR_MED;
	}

	public MoveToHandAction(CardGroup source, int amount, Predicate<AbstractCard> filter) {
		this(source, amount, filter, null);
	}

	public MoveToHandAction(CardGroup source, int amount) {
		this(source, amount, c -> true, null);
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (source.isEmpty()) {
				this.isDone = true;
				return;
			}
			
			if (source == AbstractDungeon.player.hand) {
				this.isDone = true;
				return;
			}
			
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			for (AbstractCard c : this.source.group)
				if (this.filter.test(c))
					tmp.addToTop(c);
			
			if (tmp.isEmpty()) {
				this.isDone = true;
				return;
			}
			
			tmp.shuffle();
			ArrayList<AbstractCard> result = new ArrayList<>();
			
			for (int i = 0; i < this.amount; i++) {
				if (tmp.isEmpty())
					break;
				
				AbstractCard card = tmp.getTopCard();
				tmp.removeCard(card);
				result.add(card);
				
				if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
					if (this.source != AbstractDungeon.player.discardPile)
						AbstractDungeon.player.discardPile.moveToDiscardPile(card);
					
					AbstractDungeon.player.createHandIsFullDialog();
				}
				else {
					card.unhover();
					card.lighten(true);
					card.setAngle(0.0F);
					card.drawScale = 0.12F;
					card.targetDrawScale = 0.75F;
					
					if (this.source == AbstractDungeon.player.drawPile) {
						card.current_x = CardGroup.DRAW_PILE_X;
						card.current_y = CardGroup.DRAW_PILE_Y;
					}
					else if (this.source == AbstractDungeon.player.discardPile ||
							this.source == AbstractDungeon.player.exhaustPile) {
						card.current_x = CardGroup.DISCARD_PILE_X;
						card.current_y = CardGroup.DISCARD_PILE_Y;
					}
					
					this.source.removeCard(card);
					AbstractDungeon.player.hand.addToTop(card);
					AbstractDungeon.player.hand.refreshHandLayout();
					AbstractDungeon.player.hand.applyPowers();
				}
			}
			
			if (this.callback != null)
				this.callback.accept(result);
			
			this.isDone = true;
		}
		
		this.tickDuration();
	}
}
