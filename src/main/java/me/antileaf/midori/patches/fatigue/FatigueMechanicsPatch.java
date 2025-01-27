package me.antileaf.midori.patches.fatigue;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.actions.fatigue.TriggerFatigueAction;
import me.antileaf.midori.character.Midori;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class FatigueMechanicsPatch {
	private static final Logger logger = LogManager.getLogger(FatigueMechanicsPatch.class.getName());

	@SpirePatch(clz = DrawCardAction.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> ignore = new SpireField<>(() -> false);
		public static SpireField<Integer> lazyFatigue = new SpireField<>(() -> 0);
	}

	public static void ignore(DrawCardAction action) {
		Fields.ignore.set(action, true);
	}

	public static boolean isIgnored(DrawCardAction action) {
		return Fields.ignore.get(action);
	}

	public static void trigger(int times) {
		for (int i = 0; i < times; i++)
			MidoriHelper.addToTop(new TriggerFatigueAction());

		logger.info("Triggered fatigue {} time(s).", times);
	}

	@SpirePatch(clz = AbstractRoom.class, method = "update", paramtypez = {})
	public static class IgnoreStartOfBattleDrawPatch {
		// 你无敌了矢野，谁无敌的过你啊
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("addToBottom"))
						m.replace("{ if ($1 instanceof " + DrawCardAction.class.getName() +
								") { " + FatigueMechanicsPatch.class.getName() + ".ignore((" +
								DrawCardAction.class.getName() + ") $1); } $_ = $proceed($$); }");
				}
			};
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction", paramtypez = {})
	public static class IgnoreStartOfTurnDrawPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() { // 和上面是一样的
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("addToBottom"))
						m.replace("{ if ($1 instanceof " + DrawCardAction.class.getName() +
								") { " + FatigueMechanicsPatch.class.getName() + ".ignore((" +
								DrawCardAction.class.getName() + ") $1); } $_ = $proceed($$); }");
				}
			};
		}
	}

	@SpirePatch(clz = DrawCardAction.class, method = "update", paramtypez = {})
	public static class DrawCardActionPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(DrawCardAction.class, "followUpAction"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"tmp", "deckSize"})
		public static SpireReturn<Void> Insert(DrawCardAction _inst, int tmp, int deckSize) {
			if (AbstractDungeon.player instanceof Midori) {
				if (!Fields.ignore.get(_inst)) {
					MidoriHelper.addToTop(new EmptyDeckShuffleAction());

					trigger(tmp);

					if (deckSize != 0) {
						AbstractGameAction followUpAction = ReflectionHacks.getPrivate(
								_inst, DrawCardAction.class, "followUpAction");
						MidoriHelper.addToTop(new DrawCardAction(deckSize, followUpAction, false));
					}

					_inst.amount = 0;
					_inst.isDone = true;

					return SpireReturn.Return();
				}
				else
					logger.info("Ignored draw action.");
			}

			return SpireReturn.Continue();
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("addToTop"))
						m.replace("{ if ($1 instanceof " + DrawCardAction.class.getName() +
								" && " + FatigueMechanicsPatch.class.getName() + ".isIgnored($0)) { " +
								FatigueMechanicsPatch.class.getName() + ".ignore((" +
								DrawCardAction.class.getName() + ") $1); } $_ = $proceed($$); }");
					}
			};
		}

		private static class Locator2 extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(SoulGroup.class, "isActive"));
			}
		}

		@SpireInsertPatch(locator = Locator2.class, localvars = {"deckSize", "discardSize"})
		public static void Insert2(DrawCardAction _inst, int deckSize, int discardSize) {
			if (AbstractDungeon.player instanceof Midori && !Fields.ignore.get(_inst) &&
					!SoulGroup.isActive() && deckSize + discardSize == 0)
				Fields.lazyFatigue.set(_inst, _inst.amount);
		}

		private static class Locator3 extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractPlayer.class, "createHandIsFullDialog"));
			}
		}

		@SpireInsertPatch(locator = Locator3.class, localvars = {"deckSize", "discardSize"})
		public static void Insert3(DrawCardAction _inst, int deckSize, int discardSize) {
			if (AbstractDungeon.player instanceof Midori && !Fields.ignore.get(_inst) &&
					AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE)
				Fields.lazyFatigue.set(_inst, _inst.amount);
		}
	}

	@SpirePatch(clz = DrawCardAction.class, method = "endActionWithFollowUp", paramtypez = {})
	public static class EndActionWithFollowUpPatch {
		@SpirePostfixPatch
		public static void Postfix(DrawCardAction _inst) {
			if (Fields.lazyFatigue.get(_inst) > 0) {
				trigger(Fields.lazyFatigue.get(_inst));
				Fields.lazyFatigue.set(_inst, 0);
			}
		}
	}
}
