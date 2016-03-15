package net.shrimpworks.halftone;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Halftone {

	private final int size;
	private final int spacing;
	private final int scale;
	private final Color bg;
	private final Color fg;

	public Halftone(int size, int spacing, int scale, Color bg, Color fg) {
		this.size = size;
		this.spacing = spacing;
		this.scale = scale;
		this.bg = bg;
		this.fg = fg;
	}

	public BufferedImage halftone(BufferedImage image) {
		BufferedImage bigImage = new BufferedImage(image.getWidth() * scale, image.getHeight() * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bigImage.createGraphics();
		graphics.setColor(bg);
		graphics.fillRect(0, 0, bigImage.getWidth(), bigImage.getHeight());

		if (bg == null) {
			graphics.drawImage(image.getScaledInstance(bigImage.getWidth(), bigImage.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
		}

		if (fg != null) graphics.setColor(fg);

		for (int x = 0; x < image.getWidth(); x += size + spacing) {
			for (int y = 0; y < image.getHeight(); y += size + spacing) {
				int rgb = image.getRGB(x, y);
				float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF, null);

				if (fg == null) graphics.setColor(new Color(rgb));

				float dotSize = (float)(size * scale) * (hsb[2]);

				graphics.fillOval((x - ((size - spacing) / 2)) * scale,
								  (y - ((size - spacing) / 2)) * scale,
								  Math.round(dotSize), Math.round(dotSize));
			}
		}

		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		result.createGraphics().drawImage(bigImage.getScaledInstance(result.getWidth(), result.getHeight(), Image.SCALE_SMOOTH), 0, 0,
										  null);

		return result;
	}

}
