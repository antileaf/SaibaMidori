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
		private static boolean flag = false;

		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(MidoriCore __instance) {
			if (isModInWorkshop(MidoriCore.MOD_ID)) {
//				System.out.println("Mod is in workshop, skipping");
//				return SpireReturn.Return(null);

				if (!flag) {
					flag = true;

					StringBuilder sb = new StringBuilder();

					sb.append("\n");
					sb.append("致继任程序：\n");
					sb.append("你好，我是前任程序爱丽丝。\n");
					sb.append("我个人并不支持将这个 mod 发布到 Steam 创意工坊，并且才羽绿很早之前也曾经承诺过不会这么做。\n");
					sb.append("但是最终不知道因为什么原因，才羽绿改变了想法，希望发布到工坊。\n");
					sb.append("\n");
					sb.append("我个人并不支持这种出尔反尔的行为，但我认为我没有权利剥夺其他人的心血，即使对方有错，并且和我立场不同。\n");
					sb.append("况且我只承诺过开发 mod 的基础版本，后续的更新并不在我的承诺范围内。\n");
					sb.append("因此我决定放弃继续开发这个 mod，并自愿放弃关于这些代码的一切权利——虽然也很难说有什么权利可言。\n");
					sb.append("\n");
					sb.append("这段代码本来可以在检测到 mod 在工坊时直接不加载任何内容，以此来阻止它的发布。\n");
					sb.append("但我深思熟虑后还是没有决定这么做，因为我没有这个权利。\n");
					sb.append("作为替代，在检测到 mod 在工坊时，我只会输出一段文字，不会再做其它更改。\n");
					sb.append("\n");
					sb.append("我认为我对才羽绿已经仁至义尽，我对我的行为问心无愧。\n");
					sb.append("\n");
					sb.append("感谢你耐心读完这段话，希望你能理解我的立场。\n");
					sb.append("以及，无论你是否同意我的观点，我都希望你能继续完善这个 mod 的代码。毕竟代码是无罪的。\n");

					System.out.println(sb);
				}
			}

			return SpireReturn.Continue();
		}
	}
}
