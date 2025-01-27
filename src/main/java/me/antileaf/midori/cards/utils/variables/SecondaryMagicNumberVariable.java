package me.antileaf.midori.cards.utils.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.cards.utils.AbstractSecondaryVariablesCard;

public class SecondaryMagicNumberVariable extends DynamicVariable {
	@Override
	public String key() {
		return "midoriM2";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).isSecondaryMagicNumberModified;
	}

	@Override
	public int value(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).secondaryMagicNumber :
				-1;
	}

	@Override
	public int baseValue(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).baseSecondaryMagicNumber :
				-1;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).upgradedSecondaryMagicNumber;
	}
}
