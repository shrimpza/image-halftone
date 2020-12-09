package net.shrimpworks.halftone;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HalftoneTest {

	private Path tmpDir;
	private Path tmpFile;

	@BeforeAll
	public void setup() throws IOException {
		this.tmpDir = Files.createTempDirectory("ht-test");
		this.tmpFile = Files.createFile(tmpDir.resolve("test.png"));
		try (InputStream is = getClass().getResourceAsStream("test.png")) {
			Files.copy(is, tmpFile, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@AfterAll
	public void teardown() throws IOException {
		Files.deleteIfExists(tmpFile);
		Files.deleteIfExists(tmpDir);
	}

	@Test
	public void testHalftone() throws IOException {
		Halftone halftone = new Halftone(tmpFile);

		BufferedImage outImage = halftone.halftone(3, 2, 1, Halftone.DotShape.DOT, Color.BLUE, Color.CYAN);

		assertNotNull(outImage);
		assertEquals(Color.BLUE.getRGB(), outImage.getRGB(0, 0));

		outImage = halftone.halftone(3, 2, 1, Halftone.DotShape.DOT, null, Color.BLUE);

		assertNotNull(outImage);
		assertNotEquals(Color.BLUE.getRGB(), outImage.getRGB(0, 0));
	}
}
