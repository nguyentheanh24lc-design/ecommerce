package model;

public interface Identifiable {
String getId();

default boolean hasId(String id) {

    if (id == null || id.trim().isEmpty()) {
        return false;
    }

    return getId().equalsIgnoreCase(id.trim());
}
}
