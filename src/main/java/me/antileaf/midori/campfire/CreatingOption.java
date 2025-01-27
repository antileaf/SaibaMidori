package me.antileaf.midori.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepScreenCoverEffect;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.utils.MidoriHelper;

public class CreatingOption extends AbstractCampfireOption {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			MidoriHelper.makeID(CreatingOption.class.getSimpleName()));

	public CreatingOption() {
		this.label = uiStrings.TEXT[0];
		this.description = uiStrings.TEXT[1];
		this.usable = AbstractDungeon.player.masterDeck.group.stream()
				.anyMatch(HueManager::canBePainted);
		this.img = ImageMaster.loadImage(MidoriHelper.getImgFilePath("ui/campfire", "creating"));
	}

	@Override
	public void useOption() {
		if (this.usable) {
			AbstractDungeon.effectsQueue.add(new CampfireCreatingEffect());
		}
	}
}
