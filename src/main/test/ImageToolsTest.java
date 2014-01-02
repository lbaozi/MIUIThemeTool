package main.test;

import static org.junit.Assert.*;

import main.ImageTools;

import org.junit.Before;
import org.junit.Test;

public class ImageToolsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChangeFileAtt() {
		ImageTools image = new ImageTools();
		image.changeFileAtt("Resources/wallPaper.jpg");
		//fail("Not yet implemented");
	}

}
