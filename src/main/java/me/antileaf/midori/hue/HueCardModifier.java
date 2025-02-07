package me.antileaf.midori.hue;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.strings.CardModifierStrings;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class HueCardModifier extends AbstractCardModifier {
	private static final Logger logger = LogManager.getLogger(HueCardModifier.class.getName());

	private static final String SIMPLE_NAME = HueCardModifier.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardModifierStrings cardModifierStrings = CardModifierStrings.get(ID);

	public static HueCardModifier get(AbstractCard card) {
		ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
		if (!modifiers.isEmpty())
			return (HueCardModifier) modifiers.get(0);
		return null;
	}

	public final Hue hue;

	public HueCardModifier(Hue hue) {
		this.hue = hue;
	}

	@Override
	public List<TooltipInfo> additionalTooltips(AbstractCard card) {
		if (cardModifierStrings.EXTENDED_DESCRIPTION[this.hue.ordinal() * 2 + 1].isEmpty())
			return null;

		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(cardModifierStrings.EXTENDED_DESCRIPTION[this.hue.ordinal() * 2],
				cardModifierStrings.EXTENDED_DESCRIPTION[this.hue.ordinal() * 2 + 1]));
		return tips;
	}

	@Override
	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
		if (this.hue == Hue.MARIGOLD && action.exhaustCard) { // 黄色：防止被消耗一次
			action.exhaustCard = false;
			this.addToTop(new RemoveHueAction(card));
		}
	}

	@Override
	public void atEndOfTurn(AbstractCard card, CardGroup group) {
		if (group.type == CardGroup.CardGroupType.HAND && this.hue == Hue.VIOLET)
			card.retain = true; // 紫色：回合结束时保留
	}

	// The effect of Pomelo is hard-coded in HueManager.paint()

	@Override
	public AbstractCardModifier makeCopy() {
		return new HueCardModifier(hue);
	}

	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}

	// Rendered in HueRenderPatch
}
