package me.antileaf.midori.cards.utils.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.midori.cards.utils.AbstractSecondaryVariablesCard;

public class SecondaryDamageVariable extends DynamicVariable {
	@Override
	public String key() {
		return "midoriD2";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).isSecondaryDamageModified;
	}

	@Override
	public int value(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).secondaryDamage :
				-1;
	}

	@Override
	public int baseValue(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard ?
				((AbstractSecondaryVariablesCard) card).baseSecondaryDamage :
				-1;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		return card instanceof AbstractSecondaryVariablesCard &&
				((AbstractSecondaryVariablesCard) card).upgradedSecondaryDamage;
	}
}
