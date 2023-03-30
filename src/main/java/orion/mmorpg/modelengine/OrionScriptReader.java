package orion.mmorpg.modelengine;

import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.animation.script.ScriptReader;
import com.ticxo.modelengine.api.entity.BaseEntity;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Skill;

import java.util.Arrays;
import java.util.HashMap;

public class OrionScriptReader implements ScriptReader {
    HashMap<String, Skill> scriptMap = new HashMap<>();

    public OrionScriptReader() {
        scriptMap.put("test", new Skill() {
            @Override
            public void accept(IAnimationProperty property, HashMap<String, String> args) {
                double damage = Double.parseDouble(args.get("damage"));
                if(property.getModel().getBone(args.get("bone")) == null) {
                    Bukkit.getLogger().warning("Bone " + args.get("bone") + " does not exist!");
                    return;
                }

                Vector hitboxPosition = property.getModel().getBone(args.get("bone")).getPosition();
                Location location = new Location(property.getModel().getModeledEntity().getBase().getWorld(), hitboxPosition.getX(), hitboxPosition.getY(), hitboxPosition.getZ());

                Bukkit.getScheduler().runTask(Mmorpg.INSTANCE, () -> {
                    location.getWorld().getNearbyEntities(location, 2, 2, 2).forEach(entity -> {
                        if(entity instanceof Player) {
                            ((Player) entity).damage(damage, Bukkit.getEntity(property.getModel().getModeledEntity().getBase().getUniqueId()));
                        }
                    });
                });
            }
        });
    }
    @Override
    public void read(IAnimationProperty property, String script) {
        //String is composed of identifier{args:value,...}
        String identifier = script.substring(0, script.indexOf("{"));
        HashMap<String, String> args = new HashMap<>();
        //Convert to map from json
        String[] argsArray = script.substring(script.indexOf("{") + 1, script.indexOf("}")).split(",");
        for(String arg : argsArray) {
            String[] argArray = arg.split(":");
            args.put(argArray[0], argArray[1]);
        }

        if(scriptMap.containsKey(identifier)) {
            scriptMap.get(identifier).accept(property, args);
        }
    }
}
