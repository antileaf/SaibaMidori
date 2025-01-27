package me.antileaf.midori.patches.hue;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import me.antileaf.midori.campfire.CampfireCreatingEffect;
import me.antileaf.midori.campfire.CreatingOption;
import me.antileaf.midori.cards.colorless.PaintChoiceCard;
import me.antileaf.midori.character.Midori;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CampfireCreatingPatch {
	private static final Logger logger = LogManager.getLogger(CampfireCreatingPatch.class.getName());

	@SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> forCreating = new SpireField<>(() -> false);
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "acquireCard",
			paramtypez = {AbstractCard.class})
	public static class AcquireCardPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardRewardScreen _inst, AbstractCard card) {
			CampfireCreatingEffect e = AbstractDungeon.effectList.stream()
					.filter(ef -> ef instanceof CampfireCreatingEffect)
					.map(ef -> (CampfireCreatingEffect) ef)
					.findFirst()
					.orElse(null);

			if (e != null) {
				if (card instanceof PaintChoiceCard)
					e.paint((PaintChoiceCard) card);
				else {
					logger.warn("CampfireCreatingEffect is active, but the card is not PaintChoiceCard: {}", card);
				}

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = CampfireUI.class, method = "initializeButtons", paramtypez = {})
	public static class AddButtonPatch {
		@SpireInsertPatch(rloc = 24)
		public static void Insert(CampfireUI _inst) {
			if (AbstractDungeon.player instanceof Midori) {
				ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(_inst,
						CampfireUI.class, "buttons");

				buttons.add(new CreatingOption());
			}
		}
	}

	@SpirePatch(clz = GridCardSelectScreen.class, method = "open",
			paramtypez = {CardGroup.class, int.class, String.class,
					boolean.class, boolean.class, boolean.class, boolean.class})
	public static class GridSelectScreenOpenPatch {
		@SpireInsertPatch(rloc = 15)
		public static void Insert(GridCardSelectScreen _inst, CardGroup group, int numCards,
								  String tipMsg, boolean forUpgrade, boolean forTransform,
								  boolean canCancel, boolean forPurge) {
			if (Fields.forCreating.get(_inst))
				AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
		}
	}

	@SpirePatch(clz = CancelButton.class, method = "update", paramtypez = {})
	public static class CancelButtonPatch {
		@SpireInsertPatch(rloc = 44)
		public static SpireReturn<Void> Insert(CancelButton _inst) {
			if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID &&
					Fields.forCreating.get(AbstractDungeon.gridSelectScreen)) {
				if (!AbstractDungeon.gridSelectScreen.confirmScreenUp) {
					AbstractDungeon.closeCurrentScreen();
					if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
						RestRoom r = (RestRoom) AbstractDungeon.getCurrRoom();
						r.campfireUI.reopen();

						AbstractDungeon.effectList.stream()
								.filter(e -> e instanceof CampfireCreatingEffect)
								.forEach(e -> ((CampfireCreatingEffect) e).close());
					}

					return SpireReturn.Return();
				}

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
