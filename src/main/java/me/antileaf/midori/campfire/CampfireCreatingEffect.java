package me.antileaf.midori.campfire;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepScreenCoverEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.PaintChoiceCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.hue.CampfireCreatingPatch;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class CampfireCreatingEffect extends AbstractGameEffect {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			MidoriHelper.makeID(CampfireCreatingEffect.class.getSimpleName()));

	private boolean openedScreen = false;
	private boolean hasChosenColor = false;
	private boolean hasPainted = false;
	private Color screenColor;

	public AbstractCard card;

	public CampfireCreatingEffect() {
		this.screenColor = AbstractDungeon.fadeColor.cpy();
		this.duration = 1.5F;
		this.screenColor.a = 0.0F;
		AbstractDungeon.overlayMenu.proceedButton.hide();
	}

	// Called in patch
	public void paint(PaintChoiceCard chosen) {
		HueManager.paintOutsideCombat(this.card, chosen.hue);

//		for(int i = 0; i < 30; ++i)
//			AbstractDungeon.topLevelEffects.add(new CampfireSleepScreenCoverEffect());

		AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.card.makeStatEquivalentCopy()));

		this.hasPainted = true;

		RestRoom r = (RestRoom)AbstractDungeon.getCurrRoom();
		ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(r.campfireUI,
				CampfireUI.class, "buttons");

		buttons.stream()
				.filter(b -> b instanceof CreatingOption)
				.forEach(b -> b.usable = false);
	}

	public void close() {
		this.isDone = true;
		CampfireCreatingPatch.Fields.forCreating.set(AbstractDungeon.gridSelectScreen, false);
	}

	@Override
	public void update() {
		if (!AbstractDungeon.isScreenUp) {
			this.duration -= Gdx.graphics.getDeltaTime();
			this.updateBlackScreenColor();
		}

		if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forUpgrade) {
			for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
				AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
			}

			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
		}

		if (this.duration < 1.0F && !this.openedScreen) {
			this.openedScreen = true;
			CampfireCreatingPatch.Fields.forCreating.set(AbstractDungeon.gridSelectScreen, true);
			AbstractDungeon.gridSelectScreen.open(
					new CardGroup(CardGroup.CardGroupType.UNSPECIFIED) {{
						AbstractDungeon.player.masterDeck.group.stream()
								.filter(HueManager::canBePainted)
								.forEach(this::addToTop);
					}},
					1,
					uiStrings.TEXT[0], false, false, true, false);
		}

		if (this.openedScreen && !this.hasChosenColor && !AbstractDungeon.isScreenUp &&
				!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			this.hasChosenColor = true;
			this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(new PaintChoiceCard(Hue.LAVA));
			choices.add(new PaintChoiceCard(Hue.MINT));
			choices.add(new PaintChoiceCard(Hue.SKY));
			choices.add(new PaintChoiceCard(Hue.INK));

			AbstractDungeon.cardRewardScreen.open(choices, null,
					String.format(uiStrings.TEXT[1], this.card.name));
		}

		if (this.duration < 0.0F && this.hasPainted) {
			this.close();
			if (CampfireUI.hidden) {
				AbstractRoom.waitTimer = 0.0F;
				AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
				((RestRoom)AbstractDungeon.getCurrRoom()).campfireUI.reopen();
			}
		}

	}

	private void updateBlackScreenColor() {
		if (this.duration > 1.0F) {
			this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
		} else {
			this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
		}

	}

	public void render(SpriteBatch sb) {
		sb.setColor(this.screenColor);
		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
		if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
			AbstractDungeon.gridSelectScreen.render(sb);
		}

	}

	public void dispose() {
	}
}
