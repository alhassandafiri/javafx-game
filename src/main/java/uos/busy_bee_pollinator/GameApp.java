package uos.busy_bee_pollinator;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.application.Platform;

public class GameApp extends Application {
	Pane root;
	Scene scene;
	Canvas canvas;
	GraphicsContext gc;
	ArrayList<GameObject> activeEntities = new ArrayList<GameObject>();
	BugFactory bugFactory;
	Bee playerBee;
	EnvironmentProps mainHive;
	Stage primaryStage;
	double fadeAlpha = 0.0;
	
	boolean seenIntro = false;
	boolean seenPollenFact = false;
	boolean seenSpiderFact = false;
	boolean seenMantisFact = false;
	boolean seenSunflowerFact = false;
	boolean seenLavenderFact = false;
	
	Image gardenMapImg;
	Image pollenImg;
	Image hiveImg;
	Image sunflowerImg;
	Image lavenderImg;
	Image fullHeartImg;
	Image halfHeartImg;
	
	double[][] spiderSpawns = {
	    {30.0, 30.0},   // Top Left Near Large Rock
	    {1025.0, 30.0},  // Top Right Near Small Rock
	    {500, 500}, // On Top of Spider Near Dirt Path
	    {30.0, 600}, // On dirt Path
	    {450, 700}, // Near Dirt Path 
	    {700.0, 800.0}, // Bottom Right Near Small Rock 
	};

	double[][] mantisSpawns = {
	    {600, 65.0},   // Centred with stream of water at top
	    {860, 520}, // Mid map near the pond
	    {400, 350}, // Near garden patches
	    {800, 280},  // Open grassy area in the bottom-right corner
	    {30.0, 400}, // Patrolling mid left side
	};
	
	double[][] sunflowerSpawns = {{800.0, 320.0}, {180.0, 210.0}, {40.0, 380.0}};
	double[][] lavenderSpawns = {{500.0, 570.0}, {800.0, 560.0}, {30.0, 100.0}};
	
	AnimationTimer timer = new AnimationTimer() {
		public void handle(long arg0) {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			
			if (!seenIntro) {
			    seenIntro = true;
			    showFact("Welcome to Busy Bee Pollinator!", 
			             "Use the arrow keys to fly! Collect pollen and return it to the hive.\n\nWatch out for predators, and explore the flowers to learn real-life ecosystem facts!");
			}
			
			gc.drawImage(gardenMapImg, 0, 0, canvas.getWidth(), canvas.getHeight());
			
            // 1. Create a safe list for things we want to delete this frame
            ArrayList<GameObject> itemsToRemove = new ArrayList<>();

			for (GameObject entity : activeEntities) {
				entity.update();

				if (entity instanceof Pollen) {
				    if (checkCollision(playerBee, entity, 35.0)) {
				        if (!seenPollenFact) {
				            seenPollenFact = true;
				            showFact("Pollen Power!", "Bees collect pollen in special 'pollen baskets' on their hind legs. They use it as a high-protein food source to feed baby bees in the hive!");
				        }
				        playerBee.collectPollen();
				        itemsToRemove.add(entity);
				    }
				}

                if (entity == mainHive) {
                	if (playerBee.pollenCount > 0) {
                        if (checkCollision(playerBee, entity, 25.0)) {
                            playerBee.depositPollen();
                        }
                	}
                }
                
                if (entity instanceof Spider) {
                    if (checkCollision(playerBee, entity, 30.0)) {
                        if (!seenSpiderFact) {
                            seenSpiderFact = true;
                            showFact("Predator Alert: Crab Spiders!", "Crab Spiders don't spin webs! They camouflage themselves on flowers and ambush bees when they land to drink nectar.");
                        }
                        playerBee.takeDamage(1);
                    }
                }

                if (entity instanceof Mantis) { 
                    if (checkCollision(playerBee, entity, 35.0)) {
                        if (!seenMantisFact) {
                            seenMantisFact = true;
                            showFact("Predator Alert: Praying Mantis!", "Praying Mantises are apex insect predators. They use their lightning-fast, spiked front legs to catch flying insects right out of the air!");
                        }
                        playerBee.takeDamage(2);
                    }
                }
			}
			
			if (playerBee.health <= 0) {
				if (fadeAlpha < 0.85) {
					fadeAlpha += 0.015;
				}
				
				gc.setFill(Color.rgb(0, 0, 0, fadeAlpha)); 
			    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
			    
			    gc.setTextAlign(TextAlignment.CENTER);
			   
			    gc.setFill(Color.RED);
			    gc.setFont(Font.font("Courier New", 60));
			    gc.fillText("GAME OVER", canvas.getWidth() / 2, canvas.getHeight() / 2 - 20);
			    
			    gc.setFill(Color.WHITE);
			    gc.setFont(Font.font("Courier New", 24));
			    gc.fillText("Final Pollen Collected: " + playerBee.pollenCount, canvas.getWidth() / 2, canvas.getHeight() / 2 + 30);
			    
			    gc.setFill(Color.YELLOW);
			    gc.setFont(Font.font("Courier New", 20));
			    gc.fillText("> Press ENTER to Try Again <", canvas.getWidth() / 2, canvas.getHeight() / 2 + 80);
			    
			    gc.setTextAlign(TextAlignment.LEFT);
			    
			    gc.setTextAlign(TextAlignment.LEFT);
			    
			}
            activeEntities.removeAll(itemsToRemove);
            
            if (!seenSunflowerFact) {
                for (double[] coords : sunflowerSpawns) {
                    double dist = Math.hypot((playerBee.x + 15) - (coords[0] + 25), (playerBee.y + 15) - (coords[1] + 25));
                    if (dist < 60.0) { 
                        seenSunflowerFact = true;
                        showFact("Sunflowers & Bees", "A single sunflower is actually made of thousands of tiny flowers packed together! Bees love them because they provide massive amounts of both nectar and pollen.");
                        break;
                    }
                }
            }

            if (!seenLavenderFact) {
                for (double[] coords : lavenderSpawns) {
                    double dist = Math.hypot((playerBee.x + 15) - (coords[0] + 25), (playerBee.y + 15) - (coords[1] + 25));
                    if (dist < 60.0) {
                        seenLavenderFact = true;
                        showFact("Lavender & Bees", "Lavender produces nectar that is highly concentrated with sugar. The shape of the flower is perfectly designed for a bee's tongue to reach inside!");
                        break;
                    }
                }
            }
            
            gc.setFill(Color.WHITE);
    		gc.setFont(Font.font("Courier New", 24));
    		gc.fillText("Pollen: " + playerBee.pollenCount, 20, 40);
    		
    		int maxHearts = 4;
    		
    		for (int i = 0; i < maxHearts; i++) {
    		    
    		    // Calculate the X position so they line up in a row
    		    double heartX = 250 + (i * 40); 
    		    
    		    // Each heart slot represents 2 health points (e.g., slot 0 is points 1 and 2)
    		    int pointsNeededForFull = (i * 2) + 2;
    		    int pointsNeededForHalf = (i * 2) + 1;

    		    if (playerBee.health >= pointsNeededForFull) {
    		        // The bee has enough health for a full heart here
    		        gc.drawImage(fullHeartImg, heartX, 15, 30, 30); 
    		        
    		    } else if (playerBee.health == pointsNeededForHalf) {
    		        // The bee only has enough health for a half heart here!
    		        gc.drawImage(halfHeartImg, heartX, 15, 30, 30); 
    		    }
    		    // (If the health is lower than both, it draws nothing, leaving an empty space)
    		}
		}
    };
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		root = new Pane();
		
		int tileSize = 30;
		int columns = 16;
		int rows = 16;
		int screenWidth = columns * tileSize;
		int screenHeight = rows * tileSize;
		
		scene = new Scene(root, screenWidth, screenHeight);
		canvas = new Canvas(screenWidth, screenHeight);
		canvas.setFocusTraversable(true);
		gc = canvas.getGraphicsContext2D();
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		root.getChildren().add(canvas);
		canvas.widthProperty().bind(root.widthProperty());
	    canvas.heightProperty().bind(root.heightProperty());
	    primaryStage.setResizable(false);
	    
	    try {
	        String musicFile = getClass().getResource("retro.wav").toExternalForm();
	        Media sound = new Media(musicFile);
	        MediaPlayer mediaPlayer = new MediaPlayer(sound);
	        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	        mediaPlayer.setVolume(0.005);
	        mediaPlayer.play();
	        
	    } catch (Exception e) {
	    	
	        System.out.println("Warning: Could not load background music.");
	        e.printStackTrace();
	    }
		
		gardenMapImg = new Image(getClass().getResource("Garden Map.png").toExternalForm());
		pollenImg = new Image(getClass().getResource("Pollen.png").toExternalForm());
		hiveImg = new Image(getClass().getResource("Bee Hive.png").toExternalForm());
		sunflowerImg = new Image(getClass().getResource("Sunflower.png").toExternalForm());
		lavenderImg = new Image(getClass().getResource("Lavender.png").toExternalForm());
		fullHeartImg = new Image(getClass().getResource("Full Heart.png").toExternalForm());
		halfHeartImg = new Image(getClass().getResource("Half Heart.png").toExternalForm());
		
		bugFactory = new BugFactory(gc);
		
		resetGame();
		
		scene.setOnKeyPressed(event -> {
			
			if (playerBee.health <= 0) {
				
				if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
			        resetGame();
			    }
				
				return;
			}
			
			switch (event.getCode()) {
				case UP: playerBee.moveUp(); break;
				case DOWN: playerBee.moveDown(); break;
				case LEFT: playerBee.moveLeft(); break;
				case RIGHT: playerBee.moveRight(); break;
			}
		});
		
		timer.start();
				
	}
	
	private void showFact(String title, String fact) {
	    timer.stop();

	    Platform.runLater(() -> {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.initOwner(primaryStage);
	        alert.initModality(javafx.stage.Modality.WINDOW_MODAL);
	        alert.setTitle("Bee Fact!");
	        alert.setHeaderText(title);
	        alert.setContentText(fact);
	        alert.showAndWait();

	        playerBee.lastHitTime = System.currentTimeMillis();
	        timer.start();
	    });
	}
	
	private boolean checkCollision(GameObject obj1, GameObject obj2, double hitRadius) {
		double centerX1 = obj1.x + 15;
		double centerY1 = obj1.y + 15;
		double centerX2 = obj2.x + 15;
		double centerY2 = obj2.y + 15;
		
		double distance = Math.hypot(centerX1 - centerX2, centerY1 - centerY2);
		return distance < hitRadius;
	}
	
	private void spawnPollenNear(double centerX, double centerY, double radius, int amountOfPollen) {
	    
	    // Divide a full circle (2 * PI radians) by the amount of pollen
	    double angleStep = (Math.PI * 2) / amountOfPollen; 
	    
	    // Pick a random starting rotation so the ring looks different every time
	    double randomRotation = Math.random() * (Math.PI * 2);

	    for (int i = 0; i < amountOfPollen; i++) {
	        // Calculate the exact angle for this specific piece of pollen
	        double currentAngle = (i * angleStep) + randomRotation;
	        
	        // Use Sin and Cos to find the exact X and Y on the edge of the circle
	        double spawnX = centerX + (Math.cos(currentAngle) * radius);
	        double spawnY = centerY + (Math.sin(currentAngle) * radius);
	        
	        Pollen p = new Pollen(gc, spawnX, spawnY, pollenImg);
	        activeEntities.add(p);
	    }
	}
	
	private void resetGame() {
	    activeEntities.clear();
	    fadeAlpha = 0.0;
	    
	    mainHive = new EnvironmentProps(gc, 30.0, 800.0, 100.0, 100.0, hiveImg);
	    activeEntities.add(mainHive);
	    
	    for (double[] coords : sunflowerSpawns) {
	        EnvironmentProps sunflower = new EnvironmentProps(gc, coords[0], coords[1], 50.0, 50.0, sunflowerImg);
	        activeEntities.add(sunflower);
	    }
	    
	    for (double[] coords : lavenderSpawns) {
	        EnvironmentProps lavender = new EnvironmentProps(gc, coords[0], coords[1], 50.0, 50.0, lavenderImg);
	        activeEntities.add(lavender);
	    }
	    
	    playerBee = (Bee) bugFactory.createBug("Bee", 100, 750);
	    playerBee.health = 8;
	    playerBee.pollenCount = 0;
	    activeEntities.add(playerBee);
	    
	    for (double[] coords : spiderSpawns) {
	        GameObject enemySpider = bugFactory.createBug("Spider", coords[0], coords[1]);
	        activeEntities.add(enemySpider);
	    }
	    
	    for (double[] coords : mantisSpawns) {
	        GameObject enemyMantis = bugFactory.createBug("Praying Mantis", coords[0], coords[1]);
	        activeEntities.add(enemyMantis);
	    }

	    double dangerRadius = 80.0;
	    for (double[] coords : spiderSpawns) {
	        spawnPollenNear(coords[0], coords[1], dangerRadius, 2);
	    }
	    for (double[] coords : mantisSpawns) {
	        spawnPollenNear(coords[0], coords[1], dangerRadius, 3);
	    }
	}

}
