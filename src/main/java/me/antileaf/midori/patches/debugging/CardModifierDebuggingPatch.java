package me.antileaf.midori.patches.debugging;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import me.antileaf.midori.hue.HueCardModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

@SuppressWarnings("unused")
public class CardModifierDebuggingPatch {
//	private static final Logger logger = LogManager.getLogger(CardModifierDebuggingPatch.class.getName());
//
//	@SpirePatch(clz = CardGroup.class, method = "refreshHandLayout")
//	public static class RefreshHandLayoutPatch {
//		@SpirePostfixPatch
//		public static void Postfix(CardGroup _inst) {
//			logger.info("--- CardGroup.refreshHandLayout ---");
//
//			for (AbstractCard c : _inst.group)
//				logger.info("Card {} has modifiers: {}", c.name,
//						CardModifierManager.getModifiers(c, HueCardModifier.ID).stream()
//								.map(m -> m.getClass().getSimpleName())
//								.reduce((a, b) -> a + ", " + b));
//		}
//	}
}
