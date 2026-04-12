package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bee extends GameObject{

	public Bee(GraphicsContext gc, double x, double y, Image sharedImage) {
		super(gc, x, y);
		
		this.img = sharedImage;
	}
	
	@Override
    public void update() {
        if (img != null) {
            gc.drawImage(img, x, y, 50, 50); 
        }
    }

}
