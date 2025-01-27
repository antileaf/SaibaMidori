//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.antileaf.midori.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class ExpertiseAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(ExpertiseAction.class.getName());

	private final int limit;
	private final AbstractCard excluded;
	private final int count;

	private ExpertiseAction(int limit, AbstractCard excluded, int count) {
		this.limit = limit;
		this.excluded = excluded;
		this.count = count;

		this.actionType = ActionType.DRAW;
	}

	public ExpertiseAction(int limit, AbstractCard excluded) {
		this(limit, excluded, 0);
	}
	
	public void update() {
		if (!this.isDone) {
			if (this.count > BaseMod.MAX_HAND_SIZE) {
				logger.info("count > MAX_HAND_SIZE. Check your code.");
				this.isDone = true;
				return;
			}

			if (AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
				AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
				this.isDone = true;
				return;
			}

			if (AbstractDungeon.player.drawPile.size() +
					AbstractDungeon.player.discardPile.size() == 0) {
				logger.info("No cards in draw or discard pile");

				this.isDone = true;
				return;
			}

			Set<Hue> set = new HashSet<>();
			for (AbstractCard card : AbstractDungeon.player.hand.group) {
				if (card != this.excluded) {
					for (Hue hue : Hue.values())
						if (HueManager.hasHue(card, hue))
							set.add(hue);
				}
			}

			int count = set.size();
			if (count >= this.limit) {
				this.isDone = true;
				return;
			}

			this.addToTop(new ExpertiseAction(this.limit, this.excluded,
					this.count + 1));
			this.addToTop(new DrawCardAction(1));

			this.isDone = true;
		}
	}
}
