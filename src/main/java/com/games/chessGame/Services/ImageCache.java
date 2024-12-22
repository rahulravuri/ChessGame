package com.games.chessGame.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import javafx.scene.image.Image;

@Component
public class ImageCache {
	private static final Map<String, Image> imageCache = new HashMap<>();
	 public static Image getImage(String imageName) {
	        if (!imageCache.containsKey(imageName)) {
	            Image image = new Image(ImageCache.class.getResourceAsStream("/images/" + imageName));
	            imageCache.put(imageName, image);
	        }
	        return imageCache.get(imageName);
	    }

}
