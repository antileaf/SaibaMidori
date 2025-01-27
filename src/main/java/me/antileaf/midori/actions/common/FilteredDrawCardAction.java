package me.antileaf.midori.actions.common;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.function.Predicate;

// Copied from Yuzu Mod. Thanks to 0v0!
// All copyrights reserved to 0v0.
public class FilteredDrawCardAction extends AbstractGameAction {

	private final Predicate<AbstractCard> filter;
	private boolean clearDrawHistory;
	private boolean shuffleCheck = false;
	private static final Logger logger = LogManager.getLogger(FilteredDrawCardAction.class.getName());
	public static ArrayList<AbstractCard> drawnCards = new ArrayList<>();
	private AbstractGameAction followUpAction = null;
	private AbstractPlayer p;
	private CardGroup filteredDrawPile, filteredDiscardPile;

	public FilteredDrawCardAction(int amount, Predicate<AbstractCard> filter, boolean clearDrawHistory, AbstractGameAction followUpAction) {
		this.amount = amount;
		this.filter = filter;
		this.actionType = ActionType.DRAW;
		this.p = AbstractDungeon.player;
		this.clearDrawHistory = clearDrawHistory;
		this.followUpAction = followUpAction;
		if (Settings.FAST_MODE) {
			this.duration = Settings.ACTION_DUR_XFAST;
		} else {
			this.duration = Settings.ACTION_DUR_FASTER;
		}
	}

	@Override
	public void update() {
		if (this.clearDrawHistory) {
			drawnCards.clear();
			this.clearDrawHistory = false;
		}
		if (this.filteredDrawPile == null) {
			filteredDrawPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			this.p.drawPile.group.stream().filter(this.filter).forEach(filteredDrawPile::addToTop);
		}
		if (this.filteredDiscardPile == null) {
			filteredDiscardPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			this.p.discardPile.group.stream().filter(this.filter).forEach(filteredDiscardPile::addToTop);
		}
		if (p.hasPower(NoDrawPower.POWER_ID)) {
			p.getPower(NoDrawPower.POWER_ID).flash();
			this.endActionWithFollowUp();
			return;
		}
		if (this.amount <= 0) {
			endActionWithFollowUp();
			return;
		}
		int deckSize = this.filteredDrawPile.size();
		int discardSize = this.filteredDiscardPile.size();
		if (deckSize + discardSize == 0) {
			endActionWithFollowUp();
			return;
		}

		if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
			this.p.createHandIsFullDialog();
			endActionWithFollowUp();
			return;
		}

		if (!this.shuffleCheck) {
			if (this.amount + this.p.hand.size() > BaseMod.MAX_HAND_SIZE) {
				this.amount = BaseMod.MAX_HAND_SIZE - this.p.hand.size();
				this.p.createHandIsFullDialog();
			}
			if (this.amount > deckSize) {
				int temp = this.amount - deckSize;
				addToTop(new FilteredDrawCardAction(temp, this.filter, false, this.followUpAction));
				addToTop(new EmptyDeckShuffleAction());
				if (deckSize != 0) {
					addToTop(new FilteredDrawCardAction(deckSize, this.filter, false, null));
				}
				this.amount = 0;
				this.isDone = true;
				return;
			}
			this.shuffleCheck = true;
		}
		this.duration -= Gdx.graphics.getDeltaTime();

		if (this.amount != 0 && this.duration < 0.0F) {
			if (Settings.FAST_MODE) {
				this.duration = Settings.ACTION_DUR_XFAST;
			} else {
				this.duration = Settings.ACTION_DUR_FASTER;
			}

			this.amount--;
			if (!this.filteredDrawPile.isEmpty()) {
				if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
					this.p.createHandIsFullDialog();
				} else {

					AbstractCard card = this.filteredDrawPile.getTopCard();

					drawnCards.add(card);
					card.current_x = CardGroup.DRAW_PILE_X;
					card.current_y = CardGroup.DRAW_PILE_Y;
					card.setAngle(0.0F, true);
					card.lighten(false);
					card.drawScale = 0.12F;
					card.targetDrawScale = 0.75F;
					CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
					this.p.hand.addToHand(card);
					this.p.drawPile.removeCard(card);
					this.filteredDrawPile.removeCard(card);
					card.triggerWhenDrawn();
					for (AbstractPower p : this.p.powers) {
						p.onCardDraw(card);
					}
					for (AbstractRelic r : this.p.relics) {
						r.onCardDraw(card);
					}
					this.p.onCardDrawOrDiscard();
					this.p.hand.refreshHandLayout();
				}
			}
			if (this.amount == 0) {
				endActionWithFollowUp();
			}
		}
	}

	private void endActionWithFollowUp() {
		this.isDone = true;
		if (this.followUpAction != null) {
			addToTop(this.followUpAction);
		}
	}
}