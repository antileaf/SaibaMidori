package me.antileaf.midori.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChooseOneCallbackAction extends AbstractGameAction {
	private static final float DURATION = Settings.ACTION_DUR_FAST;

	private final Supplier<ArrayList<AbstractCard>> supplier;
	private final Consumer<AbstractCard> consumer;
	private final String text;
	private final boolean masterReality;
	private final boolean skippable;
	private boolean selected = false;

	public ChooseOneCallbackAction(Supplier<ArrayList<AbstractCard>> supplier, Consumer<AbstractCard> consumer,
								   String text, boolean masterReality, boolean skippable) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = DURATION;

		this.supplier = supplier;
		this.consumer = consumer;
		this.text = text;
		this.masterReality = masterReality;
		this.skippable = skippable;
	}

	public ChooseOneCallbackAction(ArrayList<AbstractCard> cards, Consumer<AbstractCard> consumer,
								   String text, boolean masterReality, boolean skippable) {
		this(() -> cards, consumer, text, masterReality, skippable);
	}

	public ChooseOneCallbackAction(Supplier<ArrayList<AbstractCard>> supplier, Consumer<AbstractCard> consumer,
								   String text, boolean masterReality) {
		this(supplier, consumer, text, masterReality, false);
	}

	public ChooseOneCallbackAction(ArrayList<AbstractCard> cards, Consumer<AbstractCard> consumer,
								   String text, boolean masterReality) {
		this(cards, consumer, text, masterReality, false);
	}
	
	public void update() {
		if (this.duration == DURATION) {
			ArrayList<AbstractCard> cards = new ArrayList<>(supplier.get());
			
			if (this.masterReality && AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID))
				for (AbstractCard c : cards)
					c.upgrade();
			
			if (cards.isEmpty()) {
				this.isDone = true;
				return;
			}
			
			if (cards.size() == 1) {
				consumer.accept(cards.get(0));
				this.isDone = true;
				return;
			}
			
			AbstractDungeon.cardRewardScreen.customCombatOpen(cards, text, this.skippable);
			this.tickDuration();
		}
		else {
			if (!this.selected) {
				if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
					consumer.accept(AbstractDungeon.cardRewardScreen.discoveryCard);
					AbstractDungeon.cardRewardScreen.discoveryCard = null;
				}
				
				this.selected = true;
			}
			
			this.tickDuration();
		}
	}
}
