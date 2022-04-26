package orion.mmorpg.items.gui;

import orion.mmorpg.items.sample.BasicGuiItem;

public class SpecialPlaceholder {
    public static class FirstSpecialPlaceholder extends BasicGuiItem {
        public FirstSpecialPlaceholder() {
            super("First Special", "FIRST_SPECIAL_PLACEHOLDER", 1);
        }
    }

    public static class SecondSpecialPlaceholder extends BasicGuiItem {
        public SecondSpecialPlaceholder() {
            super("Second Special", "SECOND_SPECIAL_PLACEHOLDER", 1);
        }
    }

    public static class ThirdSpecialPlaceholder extends BasicGuiItem {
        public ThirdSpecialPlaceholder() {
            super("Third Special", "THIRD_SPECIAL_PLACEHOLDER", 1);
        }
    }
}
