package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnvironmentProps extends GameObject {
	
	private double width;
	private double height;

	public EnvironmentProps(GraphicsContext gc, double x, double y, double width, double height, Image sharedImage) {
		super(gc, x, y);
		this.width = width;
		this.height = height;
		this.img = sharedImage;
	}
	
	@Override
    public void update() {
        if (img != null) {
            gc.drawImage(img, x, y, width, height); 
        }
    }
}
