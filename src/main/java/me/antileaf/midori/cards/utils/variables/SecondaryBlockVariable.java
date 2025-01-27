package me.antileaf.midori.cards.utils.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.cards.utils.AbstractSecondaryVariablesCard;

public class SecondaryBlockVariable extends DynamicVariable {
	@Override
	public String key() {
		return "midoriB2";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).isSecondaryBlockModified;
	}

	@Override
	public int value(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).secondaryBlock :
				-1;
	}

	@Override
	public int baseValue(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).baseSecondaryBlock :
				-1;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).upgradedSecondaryBlock;
	}
}
