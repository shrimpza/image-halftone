package net.shrimpworks.halftone;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * See {@link Main#help()} for startup arguments.
 */
public final class Main {

	private static final Properties DEFAULTS;

	/**
	 * Format for CLI option parsing.
	 * <p>
	 * eg: --option-name=value
	 */
	private static final String OPTION_PATTERN = "--([a-zA-Z0-9-_]+)=(.+)?";

	static {
		/**
		 * Set default options (overridden by command line options)
		 */
		DEFAULTS = new Properties();
		DEFAULTS.put("dot-size", "5");
		DEFAULTS.put("dot-space", "2");
		DEFAULTS.put("render-scale", "6");
		DEFAULTS.put("bg-color", "0,0,0");
		DEFAULTS.put("fg-color", "255,255,255");
	}

	private Main() {
	}

	/**
	 * See {@link Main#help()} for startup arguments.
	 *
	 * @param args application arguments
	 * @throws IOException failed to read or write image files
	 */
	public static void main(String... args) throws IOException {
		if (args.length < 2) {
			help();
			System.exit(1);
		}

		Path inPath = Paths.get(args[args.length - 2]).toAbsolutePath();
		Path outPath = Paths.get(args[args.length - 1]).toAbsolutePath();

		if (!Files.exists(inPath)) {
			System.out.printf("File does not exist: %s%n", inPath);
			System.exit(2);
		}

		if (!Files.isDirectory(outPath.toAbsolutePath().getParent())) {
			System.out.printf("Output path does not exist: %s%n", outPath);
			System.exit(2);
		}

		Properties options = parseOptions(DEFAULTS, args);

		Halftone halftone = new Halftone(
				Integer.parseInt(options.getProperty("dot-size")),
				Integer.parseInt(options.getProperty("dot-space")),
				Integer.parseInt(options.getProperty("render-scale")),
				stringRgbToColor(options.getProperty("bg-color")),
				stringRgbToColor(options.getProperty("fg-color")));

		BufferedImage out = halftone.halftone(ImageIO.read(inPath.toFile()));
		ImageIO.write(out, outPath.toString().substring(outPath.toString().lastIndexOf(".") + 1), outPath.toFile());
	}

	private static Color stringRgbToColor(String s) {
		if (s == null || s.isEmpty()) return null;

		String[] c = s.split(",");
		return new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
	}

	private static Properties parseOptions(Properties properties, String... args) {
		Properties result = new Properties(properties);

		Pattern pattern = Pattern.compile(OPTION_PATTERN);

		for (String arg : args) {
			Matcher matcher = pattern.matcher(arg);
			if (matcher.matches()) {
				result.setProperty(matcher.group(1), matcher.group(2) == null ? "" : matcher.group(2));
			}
		}

		return result;
	}

	private static void help() {
		System.out.println("Usage: image-halftone.jar [options] <input-file> <output-file>\n"
						   + "Options and defaults:\n"
						   + " --dot-size=" + DEFAULTS.get("dot-size")
						   + "\n      - size of dots in pixels\n"
						   + " --dot-space=" + DEFAULTS.get("dot-space")
						   + "\n      - space between dots in pixels\n"
						   + " --render-scale=" + DEFAULTS.get("render-scale")
						   + "\n      - internally scales image up by this factor to ensure smooth dot rendering\n"
						   + " --bg-color=" + DEFAULTS.get("bg-color")
						   + "\n      - background color in format R,G,B, or empty to use the source image as a background\n"
						   + " --fg-color=" + DEFAULTS.get("fg-color")
						   + "\n      - dot color in format R,G,B, or blank to use colour of pixel from source image\n"
		);
	}
}
