package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GameObject {

	protected Image img;
	protected GraphicsContext gc;
	protected double x, y;
	
	public GameObject(GraphicsContext gc, double x, double y) {
		super();
		this.gc = gc;
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		if (img != null) {
			gc.drawImage(img, x, y, 30, 30);
		} 
	}
}
