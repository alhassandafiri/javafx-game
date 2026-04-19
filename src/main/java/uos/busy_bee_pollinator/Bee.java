package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

public class Bee extends GameObject{
	
	public int pollenCount;
	public int health = 8;
	public long lastHitTime = 0;
	
	public Bee(GraphicsContext gc, double x, double y, Image sharedImage) {
		super(gc, x, y);
		this.img = sharedImage;
	}
	
	@Override
    public void update() {
        if (img != null) {
            gc.drawImage(img, x, y, 40, 40); 
        }
	}
	
	public void collectPollen() {
		pollenCount++;
		System.out.println("Got Pollen! Current count: " + pollenCount);
	}
	
	public void depositPollen() {
		if (pollenCount > 0) {
			System.out.println("Deposited " + pollenCount + " pollen into the hive!");
			this.pollenCount = 0;
		}
	}
	
	public void takeDamage(int damageAmount) {
		long currentTime = System.currentTimeMillis();
		
		if (currentTime - lastHitTime > 1000) {
			this.health -= damageAmount;
			this.lastHitTime = currentTime;
			
			System.out.println("Took damage.");
			
			if (this.health <= 0) {
				System.out.println("Game Over");
				this.health = 0;
			}
		}
	}
	
	public void moveUp() {
		this.y -= 7;
	}
	
	public void moveDown() {
		this.y += 7;
	}
	
	public void moveLeft() {
		this.x -= 7;
	}
	
	public void moveRight() {
		this.x += 7;
	}

}
