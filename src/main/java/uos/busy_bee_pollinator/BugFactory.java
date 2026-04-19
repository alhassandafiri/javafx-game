package uos.busy_bee_pollinator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BugFactory implements BugFactoryIF {

	GraphicsContext gc;
	
	Image beeImg;
	Image spiderImg;
	Image mantisImg;
	
	public BugFactory(GraphicsContext gc) {
		super();
		this.gc = gc;
		this.beeImg = new Image(getClass().getResource("Bee.png").toExternalForm());
		this.spiderImg = new Image(getClass().getResource("Spider.png").toExternalForm());
		this.mantisImg = new Image(getClass().getResource("Praying Mantis.png").toExternalForm());
	}
	
	@Override
	public GameObject createBug(String bugType, double x, double y) {
		
		if (bugType.equals("Bee"))
			return new Bee(gc, x, y, beeImg);
		else if (bugType.equals("Spider"))
			return new Spider(gc, x, y, spiderImg);
		else if (bugType.equals("Praying Mantis"))
			return new Mantis(gc, x, y, mantisImg);
		return null;
	}
}
