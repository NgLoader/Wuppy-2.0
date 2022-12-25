package de.ngloader.wuppy.gui.panorama;

public class PanoramaGlobal {
	
	static PanoramaRenderer panoramaRenderer;
	static int ticks = 0;

	public static PanoramaRenderer getPanoramaRenderer(int width, int height) {
		if(panoramaRenderer == null) {
        	panoramaRenderer = new PanoramaRenderer(width, height);
            panoramaRenderer.init();
		} else
			panoramaRenderer.updateSize(width, height);
		return panoramaRenderer;
	}
}