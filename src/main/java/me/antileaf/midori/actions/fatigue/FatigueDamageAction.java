package me.antileaf.midori.actions.fatigue;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.cards.colorless.FatigueCard;
import me.antileaf.midori.powers.unique.CrescendoPower;

public class FatigueDamageAction extends AbstractGameAction {
	public int damage;

	public FatigueDamageAction(int damage) {
		this.source = AbstractDungeon.player;
		this.damage = damage;

		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DAMAGE;
	}

	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			FatigueCard card = new FatigueCard(this.damage,
					AbstractDungeon.player.hasPower(CrescendoPower.POWER_ID));
			card.current_y = -200.0F * Settings.scale;
			AbstractDungeon.player.limbo.group.add(card);

			card.target_x = Settings.WIDTH / 2.0F + MathUtils.random(-200.0F, 200.0F) * Settings.scale;
			card.target_y = Settings.HEIGHT / 2.0F + MathUtils.random(-100.0F, 100.0F) * Settings.scale;
			card.targetAngle = 0.0F;
			card.lighten(false);
			card.drawScale = 0.12F;
			card.targetDrawScale = 0.9F;

			this.addToTop(new UnlimboAction(card, true));
		}

		this.tickDuration();

		if (this.isDone) {
			if (AbstractDungeon.player.hasPower(CrescendoPower.POWER_ID))
				this.addToTop(new DamageAllEnemiesAction(
						AbstractDungeon.player, DamageInfo.createDamageMatrix(this.damage, true),
						DamageInfo.DamageType.THORNS, AttackEffect.POISON));
			else
				this.addToTop(new DamageRandomEnemyAction(new DamageInfo(
						this.source, this.damage, DamageInfo.DamageType.THORNS),
						AttackEffect.POISON));
		}
	}
}
