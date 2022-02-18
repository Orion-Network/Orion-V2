package orion.mmorpg.utils;

/**
 * An enum to handle Elements used in spells
 */
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
