package me.antileaf.midori.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import me.antileaf.midori.patches.compatibility.HandCardSelectScreenPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OrderedSelectCardsInHandAction extends AbstractGameAction {
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;
    private String text;
    private boolean anyNumber;
    private boolean canPickZero;
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;

    public OrderedSelectCardsInHandAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this.tempHand = new ArrayList<>();
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.text = textForSelect;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.predicate = cardFilter;
        this.callback = callback;
        this.hand = AbstractDungeon.player.hand.group;
    }

    public OrderedSelectCardsInHandAction(int amount, String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, cardFilter, callback);
    }

    public OrderedSelectCardsInHandAction(int amount, String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, (c) -> {
            return true;
        }, callback);
    }

    public OrderedSelectCardsInHandAction(String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, cardFilter, callback);
    }

    public OrderedSelectCardsInHandAction(String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, (c) -> {
            return true;
        }, callback);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.callback == null) {
                this.isDone = true;
            } else {
                HandCardSelectScreenPatch.enable();

                this.hand.removeIf((c) -> {
                    return !this.predicate.test(c) && this.tempHand.add(c);
                });
                if (this.hand.isEmpty()) {
                    this.finish();
                } else if (this.hand.size() <= this.amount && !this.anyNumber && !this.canPickZero) {
                    ArrayList<AbstractCard> spoof = new ArrayList(this.hand);
                    this.hand.clear();
                    this.callback.accept(spoof);
                    this.hand.addAll(spoof);
                    this.finish();
                } else {
                    AbstractDungeon.handCardSelectScreen.open(this.text, this.amount, this.anyNumber, this.canPickZero);
                    this.tickDuration();
                }
            }
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            this.callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            this.hand.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.finish();
        } else {
            this.tickDuration();
        }
    }

    private void finish() {
        this.hand.addAll(this.tempHand);
//        AbstractDungeon.player.hand.refreshHandLayout();
        HandCardSelectScreenPatch.sortHand();
        HandCardSelectScreenPatch.disable();
        AbstractDungeon.player.hand.applyPowers();
        this.isDone = true;
    }
}
