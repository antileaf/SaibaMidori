package me.antileaf.midori.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class RightChoice extends AbstractMidoriCard {
	private static final Logger logger = LogManager.getLogger(RightChoice.class.getName());

	public static final String SIMPLE_NAME = RightChoice.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;

	public RightChoice() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
	}

	public RightChoice(ArrayList<AbstractCard> cards) {
		this();

		logger.info("cards = {}", cards);

		if (!cards.isEmpty()) {
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] +
					cards.stream()
							.map(c -> c.name)
							.map(c -> Arrays.stream(c.split(" "))
									.map(s -> "*" + s)
									.reduce((a, b) -> a + " " + b)
									.orElse(""))
							.reduce((a, b) -> a + cardStrings.EXTENDED_DESCRIPTION[1] + b)
							.orElse("") +
					cardStrings.EXTENDED_DESCRIPTION[2];
		}
		else
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[3];

		this.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new RightChoice();
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public void upgrade() {}
}
