package me.antileaf.midori;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.DynamicVariable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import me.antileaf.midori.character.Midori;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.hue.HueRenderHelper;
import me.antileaf.midori.icon.MidoriIcon;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.PlayerEnum;
import me.antileaf.midori.save.SaveData;
import me.antileaf.midori.strings.CardModifierStrings;
import me.antileaf.midori.utils.MidoriAudioMaster;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpireInitializer
public class MidoriCore implements
		EditCardsSubscriber,
		EditCharactersSubscriber,
		EditRelicsSubscriber,
		EditStringsSubscriber,
		EditKeywordsSubscriber,
		AddAudioSubscriber,
		PostInitializeSubscriber,
		PostDungeonInitializeSubscriber,
		OnStartBattleSubscriber,
		PostBattleSubscriber,
		CustomSavable<SaveData> {
	public static final String MOD_ID = "SaibaMidori";

	private static final Logger logger = LogManager.getLogger(MidoriCore.class.getName());

	public static final Color LIGHT_GREEN = new Color(0.565F, 0.933F, 0.565F, 1.0F);

	private static String get512Path(String s) {
		return MidoriHelper.getImgFilePath("512", s);
	}

	private static String get1024Path(String s) {
		return MidoriHelper.getImgFilePath("1024", s);
	}

	public MidoriCore() {
		BaseMod.subscribe(this);
		BaseMod.addSaveField(MOD_ID + ":SaveData", this);

		BaseMod.addColor(
				CardColorEnum.MIDORI_COLOR,
				LIGHT_GREEN,
				LIGHT_GREEN,
				LIGHT_GREEN,
				LIGHT_GREEN,
				LIGHT_GREEN,
				LIGHT_GREEN,
				LIGHT_GREEN,
				get512Path("attack"),
				get512Path("skill"),
				get512Path("power"),
				get512Path("orb"),
				get1024Path("attack"),
				get1024Path("skill"),
				get1024Path("power"),
				get1024Path("orb"),
				MidoriHelper.getImgFilePath("ui", "energyOrb")
		);
	}

	@SuppressWarnings("unused")
	public static void initialize() {
		new MidoriCore();
		// TODO: load config
	}

	private void loadIcons() {
		for (String name : new String[] {"lava", "mint", "sky", "ink"})
			CustomIconHelper.addCustomIcon(new MidoriIcon(name, name + "_icon"));
	}

	@Override
	public void receiveEditCards() {
		loadIcons();

		// 注册次要 damage, block, magicNumber
		// 虽然只有三个变量，但是 AutoAdd 很酷！
		new AutoAdd(MidoriCore.MOD_ID)
				.packageFilter("me.antileaf.midori.cards.utils.variables")
				.any(DynamicVariable.class, (info, variable) -> {
					logger.info("Adding dynamic variable: {}", variable.getClass().getSimpleName());
					BaseMod.addDynamicVariable(variable);
				});

		new AutoAdd(MidoriCore.MOD_ID)
				.packageFilter("me.antileaf.midori.cards.midori")
				.setDefaultSeen(true)
				.cards();

		new AutoAdd(MidoriCore.MOD_ID)
				.packageFilter("me.antileaf.midori.cards.colorless")
				.setDefaultSeen(true)
				.cards();
	}

	@Override
	public void receiveEditCharacters() {
		BaseMod.addCharacter(
				new Midori("Midori"),
				MidoriHelper.getImgFilePath("char/Midori", "button"),
				MidoriHelper.getImgFilePath("char/Midori", "select"),
				PlayerEnum.MIDORI_PLAYER_CLASS
		);
	}

	@Override
	public void receiveEditKeywords() {
		String lang = MidoriHelper.getLangShort();

		Keyword[] keywords = (new Gson()).fromJson(
				Gdx.files.internal("SaibaMidori/localization/" + lang + "/keywords.json")
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				Keyword[].class);

		for (Keyword keyword : keywords)
			BaseMod.addKeyword("midori", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
	}

	@Override
	public void receiveEditRelics() {
		new AutoAdd(MidoriCore.MOD_ID)
				.packageFilter("me.antileaf.midori.relics.midori")
				.any(CustomRelic.class, (info, relic) -> {
					logger.info("Adding relic: {}", relic.getClass().getSimpleName());
					BaseMod.addRelicToCustomPool(relic, CardColorEnum.MIDORI_COLOR);
					UnlockTracker.markRelicAsSeen(relic.relicId);
				});
	}

	@Override
	public void receiveEditStrings() {
		String lang = MidoriHelper.getLangShort();

		BaseMod.loadCustomStringsFile(CharacterStrings.class,
				"SaibaMidori/localization/" + lang + "/character.json");

		BaseMod.loadCustomStringsFile(CardStrings.class,
				"SaibaMidori/localization/" + lang + "/cards.json");

		BaseMod.loadCustomStringsFile(PowerStrings.class,
				"SaibaMidori/localization/" + lang + "/powers.json");

		BaseMod.loadCustomStringsFile(RelicStrings.class,
				"SaibaMidori/localization/" + lang + "/relics.json");

		BaseMod.loadCustomStringsFile(UIStrings.class,
				"SaibaMidori/localization/" + lang + "/ui.json");

		CardModifierStrings.init((new Gson()).fromJson(
				Gdx.files.internal("SaibaMidori/localization/" + lang + "/card_modifiers.json")
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, CardModifierStrings>>() {}).getType()));

		Hue.initialize((new Gson()).fromJson(
				Gdx.files.internal("SaibaMidori/localization/" + lang + "/hue.json")
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType()));
	}

	@Override
	public void receiveAddAudio() {
		MidoriAudioMaster.init();
	}

	@Override
	public void receivePostInitialize() {
		HueRenderHelper.initialize();
	}

	@Override
	public void receivePostDungeonInitialize() {
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom abstractRoom) {
		FatigueManager.initPreBattle();
	}

	@Override
	public void receivePostBattle(AbstractRoom abstractRoom) {
		FatigueManager.clearPostBattle();
	}

	@Override
	public SaveData onSave() {
		return SaveData.save();
	}

	@Override
	public void onLoad(SaveData saveData) {
		SaveData.load(saveData);

		if (SaveData.saveData != null) {
//			HueManager.load(SaveData.saveData.masterHues);
//			SaveData.saveData.masterHues = null;
		}
		else
			logger.error("SaveData is null!");
	}
}
