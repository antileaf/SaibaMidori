package me.antileaf.midori.utils;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.steam.SteamSearch;
import me.antileaf.midori.MidoriCore;

import java.io.File;

public class WorkshopHelper {
	public static boolean isModInWorkshop(String id) {
		for (SteamSearch.WorkshopInfo info : Loader.getWorkshopInfos()) {
			File file = new File(info.getInstallPath());
			File[] files = file.listFiles((dir, name) -> name.endsWith(".jar"));
			if (files == null)
				continue;

			for (File f : files) {
				ModInfo modInfo = ModInfo.ReadModInfo(f);
				if (modInfo != null && modInfo.ID.equals(id))
					return true;
			}
		}

		return false;
	}

	@SpirePatches2({
			@SpirePatch2(clz = MidoriCore.class, method = "receiveEditCards"),
			@SpirePatch2(clz = MidoriCore.class, method = "receiveEditCharacters"),
			@SpirePatch2(clz = MidoriCore.class, method = "receiveEditKeywords"),
			@SpirePatch2(clz = MidoriCore.class, method = "receiveEditRelics"),
			@SpirePatch2(clz = MidoriCore.class, method = "receiveAddAudio"),
	})
	public static class WorkshopPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(MidoriCore __instance) {
			if (isModInWorkshop(MidoriCore.MOD_ID)) {
				System.out.println("Mod is in workshop, skipping");
				return SpireReturn.Return(null);
			} else {
				return SpireReturn.Continue();
			}
		}
	}
}
