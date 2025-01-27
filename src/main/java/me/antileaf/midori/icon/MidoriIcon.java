package me.antileaf.midori.icon;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import me.antileaf.midori.utils.MidoriHelper;

public class MidoriIcon extends AbstractCustomIcon {
	public MidoriIcon(String name, String imgName) {
		super("midori:" + name,
				new Texture(MidoriHelper.getImgFilePath("icons", imgName)));
	}

	@Override
	public float getRenderScale() {
		return 27.0F / (float) this.getImgSize();
	}
}
