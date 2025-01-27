package me.antileaf.midori.hue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.unique.RainbowPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class HueRenderHelper {
	private static final Logger logger = LogManager.getLogger(HueRenderHelper.class.getName());

	private static Map<Hue, Texture> textures;
	private static Texture rainbow, rainbowBlack;

	public static final float WIDTH = 60.0F, HEIGHT = WIDTH / 102.0F * 144.0F;

	public static void initialize() {
		textures = new HashMap<>();

		textures.put(Hue.LAVA, new Texture("SaibaMidori/img/hue/lava.png"));
		textures.put(Hue.MINT, new Texture("SaibaMidori/img/hue/mint.png"));
		textures.put(Hue.SKY, new Texture("SaibaMidori/img/hue/sky.png"));
		textures.put(Hue.INK, new Texture("SaibaMidori/img/hue/ink.png"));

		textures.put(Hue.POMELO, new Texture("SaibaMidori/img/hue/pomelo.png"));
		textures.put(Hue.VIOLET, new Texture("SaibaMidori/img/hue/violet.png"));
		textures.put(Hue.MARIGOLD, new Texture("SaibaMidori/img/hue/marigold.png"));

		rainbow = new Texture("SaibaMidori/img/hue/rainbow.png");
		rainbowBlack = new Texture("SaibaMidori/img/hue/rainbow_black.png");
	}

	private static Texture getTexture(AbstractCard card) {
		if (HueManager.hasAllHues(card))
			return rainbowBlack;

		Hue hue = HueManager.getHue(card);

		if (MidoriHelper.isInBattle() && hue == Hue.SKY &&
				AbstractDungeon.player.hasPower(RainbowPower.POWER_ID)) {
			if (AbstractDungeon.player.getPower(RainbowPower.POWER_ID).amount == 0)
				return rainbow;
			else
				return rainbowBlack;
		}

		if (hue == null)
			hue = HueManager.getFixedHue(card);

		return textures.get(hue);
	}

	public static void drawOnCard(AbstractCard card, SpriteBatch sb, Texture img, float xPos, float yPos,
						   Vector2 offset, float width, float height, float alpha,
						   float drawScale, float scaleModifier) {
		if (card.angle != 0.0F)
			offset.rotate(card.angle);

		offset.scl(Settings.scale * drawScale);

		float drawX = xPos + offset.x, drawY = yPos + offset.y;
		float scale = drawScale * Settings.scale * scaleModifier;

		Color backup = sb.getColor();
		sb.setColor(1.0F, 1.0F, 1.0F, alpha);
		sb.draw(
				img,
				drawX - width / 2.0F,
				drawY - height / 2.0F,
				width / 2.0F,
				height / 2.0F,
				width,
				height,
				scale,
				scale,
				card.angle,
				0,
				0,
				img.getWidth(),
				img.getHeight(),
				false,
				false
		);
		sb.setColor(backup);
	}

	private static final float X_OFFSET = -134.0F, Y_OFFSET = 158.0F;

	public static void renderHueOnCard(AbstractCard card, SpriteBatch sb) {
		if (card.isFlipped || card.isLocked || card.transparency <= 0.0F)
			return;

		if (HueManager.getHue(card) == null && !HueManager.hasAllHues(card))
			return;

//		logger.info("Rendering hue on card {}, hue = {}", card.name, HueManager.getHue(card));

		drawOnCard(
				card,
				sb,
				getTexture(card),
				card.current_x,
				card.current_y,
				new Vector2(X_OFFSET, Y_OFFSET),
				WIDTH,
				HEIGHT,
				card.transparency,
				card.drawScale,
				1.0F
		);
	}

	public static void renderHueInLibrary(AbstractMidoriCard card, SpriteBatch sb) {
		if (card.isFlipped || card.isLocked || card.transparency <= 0.0F)
			return;

		if (card.fixedHue == null && !card.hasAllHues)
			return;

		drawOnCard(
				card,
				sb,
				getTexture(card),
				card.current_x,
				card.current_y,
				new Vector2(X_OFFSET, Y_OFFSET),
				WIDTH,
				HEIGHT,
				card.transparency,
				card.drawScale,
				1.0F
		);
	}
}
