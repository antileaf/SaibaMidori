package me.antileaf.midori.hue;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.OnOtherCardRemovedHueCard;
import me.antileaf.midori.cards.interfaces.OnPaintedCard;
import me.antileaf.midori.powers.interfaces.OnCardPaintedPower;
import me.antileaf.midori.powers.interfaces.OnCardRemovedHuePower;
import me.antileaf.midori.powers.unique.RainbowPower;
import me.antileaf.midori.save.SaveData;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HueManager {
	private static final Logger logger = LogManager.getLogger(HueManager.class.getName());

	public static Random rng = new Random();

	public static void generateSeeds() {
		rng = new Random(Settings.seed);
	}

	public static void loadSeeds() {
		rng = new Random(Settings.seed, SaveData.saveData.hueRngCounter);
	}

	private static Hue getRandomHue() {
		Hue ret = Hue.values()[rng.random(2)];
		SaveData.saveData.hueRngCounter = rng.counter;
		return ret;
	}

	private static Hue getRandomHueInCombat() {
		return Hue.values()[AbstractDungeon.cardRandomRng.random(2)];
	}

	public static Hue getHue(AbstractCard card) {
//		return HueMechanicsPatch.Fields.hue.get(card);
		HueCardModifier modifier = HueCardModifier.get(card);
		return modifier != null ? modifier.hue : null;
	}

	public static boolean hasAllHues(AbstractCard card) {
		return card instanceof AbstractMidoriCard && ((AbstractMidoriCard) card).hasAllHues;
	}

	private static Hue setHue(AbstractCard card, Hue hue) { // Returns the original hue
		Hue original = getHue(card);
//		HueMechanicsPatch.Fields.hue.set(card, hue);

		CardModifierManager.removeModifiersById(card, HueCardModifier.ID, false);
		if (hue != null)
			CardModifierManager.addModifier(card, new HueCardModifier(hue));

		return original;
	}

	public static boolean hasHue(AbstractCard card, Hue target) {
		if (card instanceof AbstractMidoriCard && ((AbstractMidoriCard)card).hasAllHues)
			return true;

		return isHue(getHue(card), target);

//		if (!MidoriHelper.isInBattle())
//			return getHue(card) == target;
//
//		Hue hue = getHue(card);
//		if (hue == target)
//			return true;
//
//		if (AbstractDungeon.player.hasPower(RainbowPower.POWER_ID) && hue == Hue.SKY) {
//			if (target == Hue.LAVA || target == Hue.MINT)
//				return true;
//
//			return target == Hue.INK && AbstractDungeon.player.getPower(RainbowPower.POWER_ID).amount > 0;
//		}
//
//		return false;
	}

	public static boolean isHue(Hue hue, Hue target) {
		if (!MidoriHelper.isInBattle())
			return hue == target;

		if (hue == target)
			return true;

		if (AbstractDungeon.player.hasPower(RainbowPower.POWER_ID) && hue == Hue.SKY) {
			if (target == Hue.LAVA || target == Hue.MINT)
				return true;

			return target == Hue.INK && AbstractDungeon.player.getPower(RainbowPower.POWER_ID).amount > 0;
		}

		return false;
	}

	public static boolean hasAnyHue(AbstractCard card) {
		return getHue(card) != null || hasAllHues(card);
	}

	public static Hue getFixedHue(AbstractCard card) {
		if (card instanceof AbstractMidoriCard)
			return ((AbstractMidoriCard) card).fixedHue;
		else if (card.type == AbstractCard.CardType.CURSE)
			return Hue.INK;

		return null;
	}

	public static boolean canBePainted(AbstractCard card) {
		return getFixedHue(card) == null && !hasAllHues(card) &&
				card.type != AbstractCard.CardType.STATUS;
	}

	public static void configureOnSpawn(AbstractCard card) { // used in patch
		if (hasAllHues(card))
			return;

		Hue hue = getFixedHue(card);

		if (hue == null) {
			if (card instanceof AbstractMidoriCard)
				hue = getRandomHue();
			else if (card.color == AbstractCard.CardColor.RED)
				hue = Hue.LAVA;
			else if (card.color == AbstractCard.CardColor.GREEN)
				hue = Hue.MINT;
			else if (card.color == AbstractCard.CardColor.BLUE)
				hue = Hue.SKY;
		}

//		HueMechanicsPatch.Fields.hue.set(card, hue);
		if (hue != null)
			CardModifierManager.addModifier(card, new HueCardModifier(hue));
	}

	public static void configureWithCheck(AbstractCard card) {
		if (getHue(card) == null && !hasAllHues(card))
			configureOnSpawn(card);
	}

	public static void configureOnSpawnInCombat(AbstractCard card) {
		if (getHue(card) != null || hasAllHues(card))
			return;

		Hue hue = getFixedHue(card);

		if (hue == null) {
			if (card instanceof AbstractMidoriCard && card.color != AbstractCard.CardColor.COLORLESS)
				hue = getRandomHueInCombat();
			else if (card.color == AbstractCard.CardColor.RED)
				hue = Hue.LAVA;
			else if (card.color == AbstractCard.CardColor.GREEN)
				hue = Hue.MINT;
			else if (card.color == AbstractCard.CardColor.BLUE)
				hue = Hue.SKY;
		}

//		HueMechanicsPatch.Fields.hue.set(card, hue);
		if (hue != null)
			CardModifierManager.addModifier(card, new HueCardModifier(hue));
	}

	public static void configureOnCopy(AbstractCard copy, AbstractCard original) {
		Hue hue = getHue(original);

		// No need to copy. CardModifierManager will handle it.
//		HueMechanicsPatch.Fields.hue.set(copy, hue);
	}

	public static void remove(AbstractCard card) {
		if (getFixedHue(card) != null || hasAllHues(card))
			return;

		Hue original = setHue(card, null);

		for (CardGroup group : new CardGroup[] {
				AbstractDungeon.player.hand,
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.discardPile
		})
			group.group.forEach(c -> {
				if (c != card && c instanceof OnOtherCardRemovedHueCard)
					((OnOtherCardRemovedHueCard) c).onOtherCardRemovedHue(card, original);
			});

		for (AbstractPower power : AbstractDungeon.player.powers)
			if (power instanceof OnCardRemovedHuePower)
				((OnCardRemovedHuePower) power).onCardRemovedHue(card, original);
	}

	public static void paint(AbstractCard card, Hue hue) {
		if (getFixedHue(card) != null)
			logger.info("Card {} has fixed hue {}", card.name, getFixedHue(card));
		else if (hasAllHues(card))
			logger.info("Card {} has all hues", card.name);
		else if (card.type == AbstractCard.CardType.STATUS)
			logger.info("Card {} is a status card", card.name);
		else {
			Hue original = getHue(card);
			if (original != null && original != hue) {
				remove(card);

				if (hue == Hue.POMELO) // 橙色：覆盖其它颜色时获得 3 点活力
					MidoriHelper.addToBot(new ApplyPowerAction(AbstractDungeon.player,
							AbstractDungeon.player, new VigorPower(AbstractDungeon.player, 3)));
			}

			setHue(card, hue);

			if (card instanceof OnPaintedCard)
				((OnPaintedCard) card).onPainted(hue);

			for (AbstractPower p : AbstractDungeon.player.powers)
				if (p instanceof OnCardPaintedPower)
					((OnCardPaintedPower) p).onCardPainted(card, hue);
		}
	}

	public static void paintOutsideCombat(AbstractCard card, Hue hue) {
		setHue(card, hue);
	}

	// No need to save hues. Card modifiers will be saved automatically.

//	public static Hue[] save() {
//		if (AbstractDungeon.player == null)
//			return null;
//
//		Hue[] ret = new Hue[AbstractDungeon.player.masterDeck.group.size()];
//		for (int i = 0; i < ret.length; i++)
//			ret[i] = getHue(AbstractDungeon.player.masterDeck.group.get(i));
//
//		logger.info("Saved {} hues: {}", ret.length, ret);
//
//		return ret;
//	}
//
//	public static void load(Hue[] hues) {
//		if (AbstractDungeon.player == null || hues == null)
//			return;
//
//		for (int i = 0; i < hues.length; i++)
//			setHue(AbstractDungeon.player.masterDeck.group.get(i), hues[i]);
//
//		logger.info("Loaded {} hues: {}", hues.length, hues);
//	}
}
