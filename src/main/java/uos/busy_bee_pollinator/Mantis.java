package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Mantis extends GameObject {

	double dy = 1.7;
	double startY;
	
	public Mantis(GraphicsContext gc, double x, double y, Image sharedImage) {
		super(gc, x, y);
		
		this.img = sharedImage;
		
		this.startY = y;
	}
	
	@Override
    public void update() {
        if (img != null) {
            gc.drawImage(img, x, y, 50, 50); 
        }
        
        this.y += dy;
        
        if (this.y > startY + 40 || this.y < startY - 40) {
            this.dy = -this.dy; 
        }
    }

}
