package me.antileaf.midori.patches.fatigue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.character.Midori;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.utils.MidoriHelper;

@SuppressWarnings("unused")
public class FatigueRenderPatch {
	@SpirePatch(clz = DrawPilePanel.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class DrawPilePanelRenderPatch {
		private static Texture CIRCLE = null;

		private static final float COUNT_OFFSET_X = 54.0F * Settings.scale;
		private static final float COUNT_OFFSET_Y = 42.0F * Settings.scale;
		private static final float COUNT_CIRCLE_W = 128.0F * Settings.scale;

		private static class CircleLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(ImageMaster.class, "DECK_COUNT_CIRCLE"));
			}
		}

		@SpireInsertPatch(locator = CircleLocator.class)
		public static void CircleInsert(DrawPilePanel _inst, SpriteBatch sb) {
			if (AbstractDungeon.player instanceof Midori) {
				if (CIRCLE == null)
					CIRCLE = ImageMaster.loadImage(
						MidoriHelper.getImgFilePath("ui", "countCircle"));

				sb.draw(
						CIRCLE,
						_inst.current_x + COUNT_OFFSET_X,
						_inst.current_y + COUNT_OFFSET_Y,
						COUNT_CIRCLE_W,
						COUNT_CIRCLE_W
				);
			}
		}

		private static final float COUNT_X = 118.0F * Settings.scale;
		private static final float COUNT_Y = 108.0F * Settings.scale;

		private static class NumLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered"));
			}
		}

		@SpireInsertPatch(locator = NumLocator.class)
		public static void NumInsert(DrawPilePanel _inst, SpriteBatch sb) {
			if (AbstractDungeon.player instanceof Midori) {
				int fatigue = FatigueManager.getFatigue();

				FontHelper.renderFontCentered(
						sb,
						FontHelper.turnNumFont,
						String.valueOf(fatigue),
						_inst.current_x + COUNT_X,
						_inst.current_y + COUNT_Y
				);
			}
		}

		public static String getTip(String body) {
			return body + " NL NL " + FatigueManager.getTip();
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("renderGenericTip"))
						m.replace("{ if ( " + AbstractDungeon.class.getName() +
								" .player instanceof " + Midori.class.getName() + ") { $4 = " +
								DrawPilePanelRenderPatch.class.getName() +
								" .getTip($4); } $_ = $proceed($$); }");
				}
			};
		}
	}
}
