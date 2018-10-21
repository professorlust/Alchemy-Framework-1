package fr.alchemy.editor.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.EditorComponent;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;

public class FXUtils {

    public static <T> TreeItem<T> findItemForValue(final TreeItem<T> root, final Object object) {

        if (object == null) {
            return null;
        } else if (Objects.equals(root.getValue(), object)) {
            return root;
        }

        final ObservableList<TreeItem<T>> children = root.getChildren();

        if (!children.isEmpty()) {
            for (final TreeItem<T> treeItem : children) {
                final TreeItem<T> result = findItemForValue(treeItem, object);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }
    
    public static <T> Stream<TreeItem<T>> allItems(TreeItem<T> root) {
    	List<TreeItem<T>> container = new ArrayList<>();
        collectAllItems(container, root);
        return container.stream();
    }

    public static <T> void collectAllItems(final List<TreeItem<T>> container, final TreeItem<T> root) {
        container.add(root);

        final ObservableList<TreeItem<T>> children = root.getChildren();

        for (final TreeItem<T> child : children) {
            collectAllItems(container, child);
        }
    }
    
    public static void fillComponents(Array<EditorComponent> container, Node node) {

        if (node instanceof EditorComponent) {
            container.add((EditorComponent) node);
        }

        if (node instanceof SplitPane) {
        	ObservableList<Node> items = ((SplitPane) node).getItems();
            items.forEach(child -> fillComponents(container, child));
        } else if (node instanceof TabPane) {
        	ObservableList<Tab> tabs = ((TabPane) node).getTabs();
            tabs.forEach(tab -> fillComponents(container, tab.getContent()));
        }
        
        if (!(node instanceof Parent)) {
            return;
        }

        ObservableList<Node> nodes = ((Parent) node).getChildrenUnmodifiable();
        nodes.forEach(child -> fillComponents(container, child));
    }
    
    public static <T extends Node> void fillComponents(Array<T> container, Node node, Class<T> type) {

    	if (type.isInstance(container)) {
    		container.add(type.cast(node));
    	}

    	if (!(node instanceof Parent)) {
    		return;
    	}

    	final ObservableList<Node> nodes = ((Parent) node).getChildrenUnmodifiable();
    	nodes.forEach(child -> fillComponents(container, child, type));
    }
}
