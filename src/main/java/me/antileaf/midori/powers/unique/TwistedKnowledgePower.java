package me.antileaf.midori.powers.unique;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.OnCardRemovedHuePower;
import me.antileaf.midori.powers.interfaces.OnLoseEnergyPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SilentDisguisePower extends TwoAmountPower implements OnLoseEnergyPower {
	private static final Logger logger = LogManager.getLogger(SilentDisguisePower.class.getName());

	public static final String SIMPLE_NAME = SilentDisguisePower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public static final int THRESHOLD = 3;

	private boolean upgraded;

	public SilentDisguisePower(int amount, boolean upgraded) {
		this.upgraded = upgraded;

		this.name = powerStrings.NAME + (this.upgraded ? "+" : "");
		this.ID = POWER_ID + (this.upgraded ? "_Upgraded" : "");
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.amount2 = 0;

		this.type = PowerType.BUFF;
		this.updateDescription();

		String img84 = MidoriHelper.getImgFilePath("powers", "default" + "84");
		String img32 = MidoriHelper.getImgFilePath("powers", "default" + "32");
		this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img84), 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img32), 0, 0, 32, 32);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[this.upgraded ? 1 : 0], this.amount);
	}

//	private AbstractCard getRandomSilentCard() {
//		ArrayList<AbstractCard> cards = new ArrayList<>();
//
//		for (AbstractCard card : CardLibrary.getAllCards()) {
//			if (card.color == AbstractCard.CardColor.GREEN)
//				cards.add(card);
//		}
//
//		return cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1)).makeCopy();
//	}

	private void trigger() {
		this.addToBot(new ApplyPowerAction(this.owner, this.owner,
				!this.upgraded ?
						new PlatedArmorPower(this.owner, this.amount) :
						new MetallicizePower(this.owner, this.amount)));
	}

	@Override
	public void onLoseEnergy(int amount, boolean startOfTurn) {
		this.amount2 += amount;

		while (this.amount2 >= THRESHOLD) {
			this.flash();
			this.trigger();
			this.amount2 -= THRESHOLD;
		}
	}
}
