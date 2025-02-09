package me.antileaf.midori.character;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import me.antileaf.midori.MidoriCore;
import me.antileaf.midori.cards.midori.Defend_Midori;
import me.antileaf.midori.cards.midori.Lassitude;
import me.antileaf.midori.cards.midori.Strike_Midori;
import me.antileaf.midori.cards.midori.Touch;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.PlayerEnum;
import me.antileaf.midori.relics.midori.DrawingBoard;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Midori extends CustomPlayer {
	private static final Logger logger = LogManager.getLogger(Midori.class.getName());

	public static final String SIMPLE_NAME = Midori.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);

	private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);

	private static final String SHOULDER_2 = "SaibaMidori/img/char/Midori/shoulder.png"; // shoulder2 / shoulder_1
	private static final String SHOULDER_1 = "SaibaMidori/img/char/Midori/shoulder.png"; // shoulder1 / shoulder_2
	private static final String CORPSE = "SaibaMidori/img/char/Midori/Midori.png"; // dead corpse
	private static final String[] ORB_TEXTURES = {
			"SaibaMidori/img/ui/EPanel/layer5.png",
			"SaibaMidori/img/ui/EPanel/layer4.png",
			"SaibaMidori/img/ui/EPanel/layer3.png",
			"SaibaMidori/img/ui/EPanel/layer2.png",
			"SaibaMidori/img/ui/EPanel/layer1.png",
			"SaibaMidori/img/ui/EPanel/layer0.png",
			"SaibaMidori/img/ui/EPanel/layer5d.png",
			"SaibaMidori/img/ui/EPanel/layer4d.png",
			"SaibaMidori/img/ui/EPanel/layer3d.png",
			"SaibaMidori/img/ui/EPanel/layer2d.png",
			"SaibaMidori/img/ui/EPanel/layer1d.png"
	};
	private static final String ORB_VFX = "SaibaMidori/img/ui/energyVFX.png";
	private static final float[] LAYER_SPEED =
			{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};

	public Midori(String name) {
		super(name, PlayerEnum.MIDORI_PLAYER_CLASS, ORB_TEXTURES, ORB_VFX, LAYER_SPEED, null, null);

		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

		this.initializeClass(
				"SaibaMidori/img/char/Midori/Midori.png",
				SHOULDER_2, // required call to load textures and setup energy/loadout
				SHOULDER_1,
				CORPSE,
				this.getLoadout(),
				20.0F, -10.0F, 220.0F, 290.0F,
				new EnergyManager(3)
		);
	}

	@Override
	public ArrayList<String> getStartingDeck() { // 初始卡组
		ArrayList<String> ret = new ArrayList<>();

		for (int i = 0; i < 4; i++)
			ret.add(Strike_Midori.ID);

		for (int i = 0 ; i < 4; i++)
			ret.add(Defend_Midori.ID);

		ret.add(Touch.ID);
		ret.add(Lassitude.ID);

		return ret;
	}

	@Override
	public void initializeStarterDeck() {
		super.initializeStarterDeck();

		int strike_cnt = 0, defend_cnt = 0;
		for (AbstractCard c : this.masterDeck.group) {
			if (c instanceof Strike_Midori) {
				if (strike_cnt < 3) {
					HueManager.paintOutsideCombat(c, Hue.values()[strike_cnt]);
					logger.info("Painting strike {} with {}", c.cardID, Hue.values()[strike_cnt]);
					strike_cnt++;
				}
			}
			else if (c instanceof Defend_Midori) {
				if (defend_cnt < 3) {
					HueManager.paintOutsideCombat(c, Hue.values()[defend_cnt]);
					logger.info("Painting defend {} with {}", c.cardID, Hue.values()[defend_cnt]);
					defend_cnt++;
				}
			}
		}
	}

	@Override
	public ArrayList<String> getStartingRelics() { // 初始遗物
		ArrayList<String> ret = new ArrayList<>();

		ret.add(DrawingBoard.ID);

		return ret;
	}

	private static final int STARTING_HP = 70;
	private static final int MAX_HP = 70;
	private static final int STARTING_GOLD = 99;
	private static final int HAND_SIZE = 5;
	private static final int ASCENSION_MAX_HP_LOSS = 6;

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				characterStrings.NAMES[0],
				characterStrings.TEXT[0],
				STARTING_HP,
				MAX_HP,
				2,
				STARTING_GOLD,
				HAND_SIZE,
				this,
				this.getStartingRelics(),
				this.getStartingDeck(),
				false
		);
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return CardColorEnum.MIDORI_COLOR;
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return new Touch();
	}

	@Override
	public String getTitle(PlayerClass playerClass) { // 称号
		return characterStrings.NAMES[1];
	}

	@Override
	public Color getCardTrailColor() {
		return MidoriCore.LIGHT_GREEN.cpy();
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return ASCENSION_MAX_HP_LOSS;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontBlue;
	}

	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.play("SaibaMidori:CHAR_SELECT" + MathUtils.random(1, 2));
		CardCrawlGame.screenShake.shake(
				ScreenShake.ShakeIntensity.LOW,
				ScreenShake.ShakeDur.SHORT,
				false
		);
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "SaibaMidori:CHAR_SELECT" + MathUtils.random(1, 2);
	}

	@Override
	public String getLocalizedCharacterName() {
		return characterStrings.NAMES[0];
	}

	@Override
	public AbstractPlayer newInstance() {
		return new Midori(this.name);
	}

	@Override
	public String getVampireText() {
		return Vampires.DESCRIPTIONS[1]; // 加入我们，姐妹
	}

	@Override
	public Color getCardRenderColor() {
		return MidoriCore.LIGHT_GREEN.cpy();
	}

//	@Override
//	public void updateOrb(int orbCount) {
//		this.energyOrb.updateOrb(orbCount);
//	}

//	@Override
//	public TextureAtlas.AtlasRegion getOrb() {
//		return new TextureAtlas.AtlasRegion(ImageMaster.loadImage(
//				MidoriHelper.getImgFilePath("ui", "energyOrb")),
//				0, 0, 24, 24);
//	}

	@Override
	public Color getSlashAttackColor() {
		return MidoriCore.LIGHT_GREEN.cpy();
	}

	@Override
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[]{
				AttackEffect.SLASH_HEAVY,
				AttackEffect.BLUNT_LIGHT,
				AttackEffect.SLASH_DIAGONAL,
				AttackEffect.SLASH_HEAVY,
				AttackEffect.BLUNT_LIGHT,
				AttackEffect.SLASH_DIAGONAL
		};
	}

	@Override
	public String getSpireHeartText() {
		return characterStrings.TEXT[1];
	}

	@Override
	public void damage(DamageInfo info) {
//		if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS)
//				&& (info.output - this.currentBlock > 0)) {
//			AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
//			this.state.addAnimation(0, "Idle", true, 0.0F);
//			e.setTimeScale(1.0F);
//		}
		super.damage(info);
	}

//	@Override
//	public void applyPreCombatLogic() {
//		super.applyPreCombatLogic();
//	}

//	public void render(SpriteBatch sb) {
//		super.render(sb);
//	}
}
