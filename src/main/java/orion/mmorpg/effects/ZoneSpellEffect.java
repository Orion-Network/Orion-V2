package orion.mmorpg.effects;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ImageEffect;
import de.slikey.effectlib.util.BaseImageEffect;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;

import java.io.File;
import java.util.Objects;

public class ZoneSpellEffect extends ImageEffect {

    public ZoneSpellEffect(EffectManager effectManager) {
        super(effectManager);
        this.orient = false;
        this.angularVelocityX = 0;
        this.angularVelocityZ = 0;
        this.plane = BaseImageEffect.Plane.Y;
        this.rotation = new Vector(90, 0, 0);
    }

    public void load(BaseImage baseImage) {
        File f = Objects.requireNonNull(Mmorpg.INSTANCE.getDataFolder()
                .listFiles((dir, name) -> name.equalsIgnoreCase(baseImage.getImageName())))[0];
        this.loadFile(f);
    }

    public enum BaseImage {
        PENTAGRAM("pentagram.png"), ALCHEMY("img/alchemy.png"), ALCHEMY_1("img/alchemy_1.png"),
        ALCHEMY_2("img/alchemy_2.png"),;

        private final String imageName;

        BaseImage(String imageName) {
            this.imageName = imageName;
        }

        public String getImageName() {
            return imageName;
        }
    }

}
