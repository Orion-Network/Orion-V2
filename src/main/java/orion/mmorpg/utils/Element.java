package orion.mmorpg.utils;

public enum Element {
    PHYSIC,
    MAGIC,
    SOUND,
    WHITE,
    BLACK,
    FIRE,
    WATER,
    EARTH,
    ELECTRIC,
    ICE,
    VOID,
    ARCANE;

    public static String getElements() {
        StringBuilder elements = new StringBuilder("Elements : ");
        for (Element element : Element.values()) {
            elements.append(element);
        }
        return elements.toString();
    }
}
