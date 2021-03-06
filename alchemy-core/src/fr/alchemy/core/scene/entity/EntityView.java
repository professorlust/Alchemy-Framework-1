package fr.alchemy.core.scene.entity;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.BinaryWriter;
import fr.alchemy.core.asset.binary.Exportable;
import fr.alchemy.core.asset.cache.Asset;
import fr.alchemy.core.scene.component.VisualComponent;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;

/**
 * <code>EntityView</code> is the separated class to handle the view of an {@link Entity entity},
 * therefore you need to associate the view to the entity using a {@link VisualComponent}.
 * <p>
 * The <code>EntityView</code> should only contained graphic nodes as children.
 * 
 * @author Stickxy
 */
public final class EntityView extends Parent implements Exportable {
	
	/**
	 * Instantiates an empty <code>EntityView</code>.
	 */
	public EntityView() {}
	
	/**
	 * Instantiates an <code>EntityView</code> with the specified
	 * graphic node.
	 * 
	 * @param graphic The graphic node to add.
	 */
	public EntityView(final Node graphic) {
		addNode(graphic);
	}
	
	/**
	 * Adds the specified graphic node to the <code>EntityView</code>.
	 * 
	 * @param graphic The graphic node to add.
	 */
	public void addNode(final Node graphic) {
		if(graphic == null) {
			return;
		}
		
		if(graphic instanceof Circle) {
			final Circle circle = (Circle) graphic;
			circle.setCenterX(circle.getRadius());
			circle.setCenterY(circle.getRadius());
		}
		
		getChildren().add(graphic);
	}
	
	/**
	 * Adds the specified graphic nodes to the <code>EntityView</code>.
	 * 
	 * @param observableList The graphic nodes to add.
	 */
	public void addNodes(final ObservableList<Node> observableList) {
		for(Node graphic : observableList) {
			addNode(graphic);
		}
	}
	
	/**
	 * Adds the specified graphic nodes to the <code>EntityView</code>.
	 * 
	 * @param graphics The graphic nodes to add.
	 */
	public void addNodes(final Node...graphics) {
		for(Node graphic : graphics) {
			addNode(graphic);
		}
	}
	
	/**
	 * Removes the specified graphic node from the <code>EntityView</code>.
	 * 
	 * @param graphic The graphic node to remove.
	 */
	public void removeNode(final Node graphic) {
		getChildren().remove(graphic);
	}
	
	/**
	 * Removes the specified graphic node from the <code>EntityView</code>.
	 * 
	 * @param graphics The graphic nodes to remove.
	 */
	public void removeNodes(final Node...graphics) {
		getChildren().removeAll(graphics);
	}
	
	/**
	 * Clears the <code>EntityView</code> from all of its children.
	 * Therefore child should only be graphics node.
	 */
	public void clear() {
		getChildren().clear();
	}
	
	/**
	 * @return The graphical nodes composing the <code>EntityView</code>.
	 */
	public ObservableList<Node> getNodes() {
		return getChildren();
	}
	
	/**
	 * @return The list of {@link Texture textures} used as graphical nodes.
	 */
	public List<Texture> getTextures() {
		return getChildren().stream().filter(Texture.class::isInstance)
				.map(Texture.class::cast).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public void export(final BinaryWriter writer) throws IOException {
		final List<Texture> textures = getTextures();
		writer.write("views", textures);
	}

	@Override
	public void insert(final BinaryReader reader) throws IOException {
		final Asset[] assets = reader.readAssetArray("views", null);
		for(int i = 0; i < assets.length; i++) {
			if(assets[i] instanceof Texture) {
				addNodes((Texture) assets[i]);
			}
		}
	}
}
