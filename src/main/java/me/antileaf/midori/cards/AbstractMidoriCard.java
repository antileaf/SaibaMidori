package me.antileaf.midori.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import me.antileaf.midori.MidoriCore;
import me.antileaf.midori.cards.utils.AbstractSecondaryVariablesCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.utils.MidoriHelper;

public abstract class AbstractMidoriCard extends AbstractSecondaryVariablesCard {
	public Hue fixedHue = null;
	public boolean hasAllHues = false;

	public AbstractMidoriCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			CardType type,
			CardColor color,
			CardRarity rarity,
			CardTarget target
	) {
		super(id, name,
				img != null ? img :
				(type == CardType.ATTACK ? MidoriHelper.getCardImgFilePath("Attack") :
						type == CardType.SKILL ? MidoriHelper.getCardImgFilePath("Skill") :
								MidoriHelper.getCardImgFilePath("Power")),
				cost, rawDescription, type, color, rarity, target);

		FlavorText.AbstractCardFlavorFields.boxColor.set(this, MidoriCore.LIGHT_GREEN);
	}
}
