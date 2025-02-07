package me.antileaf.midori.powers.unique;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.interfaces.OnLoseEnergyPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwistedKnowledgePower extends TwoAmountPower implements NonStackablePower {
	private static final Logger logger = LogManager.getLogger(TwistedKnowledgePower.class.getName());

	public static final String SIMPLE_NAME = TwistedKnowledgePower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public TwistedKnowledgePower(int amount, int amount2) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.amount2 = amount2;

		this.type = PowerType.BUFF;
		this.updateDescription();

		String img84 = MidoriHelper.getImgFilePath("powers", "default" + "84");
		String img32 = MidoriHelper.getImgFilePath("powers", "default" + "32");
		this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img84), 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img32), 0, 0, 32, 32);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount, this.amount2);
	}

	@Override
	public void onCardDraw(AbstractCard card) {
		if (!HueManager.hasHue(card, Hue.SKY))
			this.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
	}

	@Override
	public void onRemove() {
		this.addToTop(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount2)));
	}
}
