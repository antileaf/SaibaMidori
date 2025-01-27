package me.antileaf.midori.actions.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AOEFatalAction extends AbstractGameAction {
	public int[] damage;
	Consumer<ArrayList<AbstractMonster>> callback;
	boolean ignoreMinion;

	public AOEFatalAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type,
						  AttackEffect effect, Consumer<ArrayList<AbstractMonster>> callback, boolean ignoreMinion) {
		this.source = source;
		this.damage = amount;
		this.actionType = ActionType.DAMAGE;
		this.damageType = type;
		this.attackEffect = effect;
		this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
		this.callback = callback;
		this.ignoreMinion = ignoreMinion;
	}

	public AOEFatalAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type,
						  AttackEffect effect, Consumer<ArrayList<AbstractMonster>> callback) {
		this(source, amount, type, effect, callback, false);
	}
	
	public void update() {
		if (this.duration == this.startDuration) {
			boolean playedSound = false;

			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped()) {
					AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY,
							this.attackEffect, playedSound));

					playedSound = true;
				}
		}

		this.tickDuration();
		if (this.isDone) {
			for (AbstractPower p : AbstractDungeon.player.powers)
				p.onDamageAllEnemies(this.damage);

			ArrayList<AbstractMonster> fatal = new ArrayList<>();

			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped()) {
					if (this.attackEffect == AttackEffect.POISON) {
						m.tint.color.set(Color.CHARTREUSE);
						m.tint.changeColor(Color.WHITE.cpy());
					} else if (this.attackEffect == AttackEffect.FIRE) {
						m.tint.color.set(Color.RED);
						m.tint.changeColor(Color.WHITE.cpy());
					}

					int i = AbstractDungeon.getMonsters().monsters.indexOf(m);
					m.damage(new DamageInfo(this.source, this.damage[i], this.damageType));

					if ((m.isDying || m.currentHealth <= 0) && !m.halfDead &&
							(this.ignoreMinion || !m.hasPower("Minion")))
						fatal.add(m);
				}

			this.callback.accept(fatal);

			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
				AbstractDungeon.actionManager.clearPostCombatActions();

			if (!Settings.FAST_MODE) {
				this.addToTop(new WaitAction(0.1F));
			}
		}
	}
}
