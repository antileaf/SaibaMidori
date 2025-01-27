package me.antileaf.midori.utils;

import basemod.BaseMod;

public abstract class MidoriAudioMaster {
	public static String[] CHAR_SELECT = new String[] {"SaibaMidori:CHAR_SELECT1", "SaibaMidori:CHAR_SELECT2"};

	public static void init() {
		BaseMod.addAudio(CHAR_SELECT[0], "SaibaMidori/audio/char_select1.ogg");
		BaseMod.addAudio(CHAR_SELECT[1], "SaibaMidori/audio/char_select2.ogg");
	}
}
