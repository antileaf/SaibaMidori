package me.antileaf.midori.patches.hue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.HueRenderHelper;

@SuppressWarnings("unused")
public class HueRenderPatch {
	@SpirePatch(clz = AbstractCard.class, method = "renderInLibrary", paramtypez = {SpriteBatch.class})
	public static class RenderInLibraryPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(Settings.class, "lineBreakViaCharacter"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractCard _inst, SpriteBatch sb) {
			if (_inst instanceof AbstractMidoriCard)
				HueRenderHelper.renderHueInLibrary((AbstractMidoriCard)_inst, sb);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderCard",
			paramtypez = {SpriteBatch.class, boolean.class, boolean.class})
	public static class RenderCardPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(Settings.class, "lineBreakViaCharacter"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
			HueRenderHelper.renderHueOnCard(_inst, sb);
		}
	}
}
