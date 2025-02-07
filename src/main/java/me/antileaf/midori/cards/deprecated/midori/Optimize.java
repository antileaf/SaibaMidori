package me.antileaf.midori.cards.deprecated;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

@Deprecated
public class Optimize extends AbstractMidoriCard implements OnObtainCard {
	public static final String SIMPLE_NAME = Optimize.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int BLOCK = 16;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	public Optimize() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);

		this.block = this.baseBlock = BLOCK;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));
	}

	@Override
	public void onObtainCard() {
		ArrayList<AbstractCard> others = AbstractDungeon.player.masterDeck.group.stream()
				.filter(c -> c != this && c instanceof Optimize)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		int count = 0;
		for (AbstractCard c : others) {
			c.upgrade();

			if (count++ < 20) {
				float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
				float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;

				AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
				AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));
			}
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Optimize();
	}

	@Override
	public boolean canUpgrade() {
		return true;
	}

	@Override
	protected void upgradeName() {
		this.timesUpgraded++;
		this.upgraded = true;
		this.name = cardStrings.NAME + "+" + this.timesUpgraded;
		this.initializeTitle();
	}

	@Override
	public void upgrade() {
		this.upgradeName();

		this.upgradeBlock(UPGRADE_PLUS_BLOCK);

		this.initializeDescription();
	}
}
