package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Spider extends GameObject {

	double dx = 2;
	double startX;
	
	public Spider(GraphicsContext gc, double x, double y, Image sharedImage) {
		super(gc, x, y);
		
		this.img = sharedImage;
		this.startX = x;
	}
	
	@Override
    public void update() {
        if (img != null) {
            gc.drawImage(img, x, y, 40, 40); 
        }
        
        this.x += dx;
        if (this.x > startX + 65 || this.x < startX - 65) {
            this.dx = -this.dx; 
        }
    }
}
