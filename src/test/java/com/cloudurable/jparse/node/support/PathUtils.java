package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.node.BooleanNode;
import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.NumberNode;
import com.cloudurable.jparse.node.RootNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PathUtils {

    public static Object walkFull(Object object) {
        AtomicInteger i = new AtomicInteger();
        walkFull(object, i);
        return i;
    }


    private static Object walkFull(Object object, AtomicInteger i) {
        if (object instanceof Map) {
            Map map = (Map) object;
            ((Map<?, ?>) object).keySet().forEach(key -> {
                walkFull(map.get(key), i);
                i.incrementAndGet();
            });
        } else if (object instanceof List) {
            List list = (List) object;
            list.forEach(o -> {
                walkFull(o, i);
                i.incrementAndGet();
            });
        } else {
            return i.incrementAndGet();
        }
        return i;
    }

    public static Object getLastObject(String path, Object root) {
        if (root instanceof RootNode) {
            return getLastJsonElement(path, (RootNode) root);
        }
        List<String> pathList = Arrays.asList(path.split("(/|\\.|[|])"));
        String lastElementName = last(pathList);

        Object object = getObjectAtPath(pathList, root);

        if (object instanceof Map) {
            return ((Map) object).get(lastElementName);
        } else if (object instanceof List) {
            return ((List) object).get(Integer.parseInt(lastElementName));
        } else {
            throw new IllegalStateException();
        }
    }

    public static Node getLastJsonElement(String path, Node theNode) {
        final List<String> pathList = Arrays.asList(path.split("(/|\\.|[|])"));
        final String lastElementName = last(pathList);
        final Node node = getNodeAtPath(pathList, theNode);
        if (node.isCollection()) {
            return node.asCollection().getNode(lastElementName);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public static String getString(String path, RootNode root) {
        return getLastJsonElement(path, root).toString();
    }

    public static String getString(String path, Object root) {
        return getLastObject(path, root).toString();
    }

    public static Object getObject(String path, Object root) {
        return getLastObject(path, root);
    }

    public static int getInt(String path, RootNode root) {
        return ((NumberNode) getLastJsonElement(path, root)).intValue();
    }

    public static boolean getBoolean(String path, RootNode root) {
        return ((BooleanNode) getLastJsonElement(path, root)).booleanValue();
    }

    private static Node getNodeAtPath(List<String> pathList, Node node) {
        final List<String> objectPath = pathList.subList(0, pathList.size() - 1);
        for (int i = 0; i < objectPath.size(); i++) {
            final String elementPath = objectPath.get(i);
            node = node.asCollection().getNode(elementPath);
        }
        return node;
    }

    private static Object getObjectAtPath(List<String> pathList, Object root) {
        List<String> objectPath = pathList.subList(0, pathList.size() - 1);
        Object object = root;
        for (int i = 0; i < objectPath.size(); i++) {
            String elementPath = objectPath.get(i);
            if (object instanceof Map) {
                object = ((Map) object).get(elementPath);
            } else if (object instanceof List) {
                object = ((List) object).get(Integer.parseInt(elementPath));
            } else {
                throw new IllegalStateException();
            }
        }
        return object;
    }

    private static String last(List<String> pathList) {
        return pathList.get(pathList.size() - 1);
    }
}
