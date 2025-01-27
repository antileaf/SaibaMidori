package me.antileaf.midori.patches.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class CardColorEnum {
	@SpireEnum
	public static AbstractCard.CardColor MIDORI_COLOR;

	public static class LibraryEnum {
		@SpireEnum
		public static CardLibrary.LibraryType MIDORI_COLOR;
	}
}
