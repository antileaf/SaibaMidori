package me.antileaf.midori.actions.fatigue;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.cards.colorless.FatigueCard;
import me.antileaf.midori.fatigue.FatigueManager;

public class TriggerFatigueAction extends AbstractGameAction {
	public TriggerFatigueAction() {
		this.actionType = ActionType.DAMAGE;
	}

	@Override
	public void update() {
		if (!this.isDone) {
			FatigueManager.trigger();
			this.isDone = true;
		}
	}
}
