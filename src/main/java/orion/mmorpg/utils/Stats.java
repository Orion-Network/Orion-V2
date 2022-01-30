package orion.mmorpg.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.LinkedList;
import java.util.List;

public class Stats {
    float strength, critical_chance, critical_damage, attack_speed, vitality, intel, defence, speed, luck;

    public Stats(float strength, float critical_chance, float critical_damage, float attack_speed, float vitality, float intel, float defence, float speed, float luck) {
        this.strength = strength;
        this.critical_chance = critical_chance;
        this.critical_damage = critical_damage;
        this.attack_speed = attack_speed;
        this.vitality = vitality;
        this.intel = intel;
        this.defence = defence;
        this.speed = speed;
        this.luck = luck;
    }

    private Stats(Builder builder) {
        setStrength(builder.strength);
        setCritical_chance(builder.critical_chance);
        setCritical_damage(builder.critical_damage);
        setAttack_speed(builder.attack_speed);
        setVitality(builder.vitality);
        setIntel(builder.intel);
        setDefence(builder.defence);
        setSpeed(builder.speed);
        setLuck(builder.luck);
    }

    public List<String> statsAsLore() {
        List<String> lore = new LinkedList<>();
        if(strength != 0)
            lore.add(StatsInfo.STRENGTH.getColor() + StatsInfo.STRENGTH.character + " Strength: " + ChatColor.RED + strength);
        if(critical_damage != 0)
            lore.add(StatsInfo.CRITICAL_DAMAGE.getColor() + StatsInfo.CRITICAL_DAMAGE.character + "Critical Damage: " + ChatColor.RED + critical_damage);
        if(critical_chance != 0)
            lore.add(StatsInfo.CRITICAL_CHANCE.getColor() + StatsInfo.CRITICAL_CHANCE.character + "Critical Chance: " + ChatColor.RED + critical_chance + "%");
        if(attack_speed != 0)
            lore.add(StatsInfo.ATTACK_SPEED.getColor() + StatsInfo.ATTACK_SPEED.character + " Attack Speed: " + ChatColor.YELLOW + attack_speed);
        if(vitality != 0)
            lore.add(StatsInfo.VITALITY.getColor() + StatsInfo.VITALITY.character + " Vitality: " + ChatColor.YELLOW + vitality);
        if(intel != 0)
            lore.add(StatsInfo.INTELLIGENCE.getColor() + StatsInfo.INTELLIGENCE.character + " Intelligence: " + ChatColor.YELLOW + intel);
        if(defence != 0)
            lore.add(StatsInfo.DEFENCE.getColor() + StatsInfo.DEFENCE.character + " Defence: " + ChatColor.YELLOW + defence);
        if(speed != 0)
            lore.add(StatsInfo.SPEED.getColor() + StatsInfo.SPEED.character + " Speed: " + ChatColor.WHITE + speed);
        if(luck != 0)
            lore.add(StatsInfo.LUCK.getColor() + StatsInfo.LUCK.character + " Luck: " + ChatColor.WHITE + luck);
        return lore;
    }

    public Stats combineOrSeparateStats(Stats stats, boolean combine) {
        setStrength((combine ? stats.getStrength() : stats.getStrength() * -1)+getStrength());
        setCritical_chance((combine ? stats.getCritical_chance() : stats.getCritical_chance() * -1)+getCritical_chance());
        setCritical_damage((combine ? stats.getCritical_damage() : stats.getCritical_damage() * -1)+getCritical_damage());
        setAttack_speed((combine ? stats.getAttack_speed() : stats.getAttack_speed() * -1)+getAttack_speed());
        setVitality((combine ? stats.getVitality() : stats.getVitality() * -1)+getVitality());
        setIntel((combine ? stats.getIntel() : stats.getIntel() * -1)+getIntel());
        setDefence((combine ? stats.getDefence() : stats.getDefence() * -1)+getDefence());
        setSpeed((combine ? stats.getSpeed() : stats.getSpeed() * -1)+getSpeed());
        setLuck((combine ? stats.getLuck() : stats.getLuck() * -1)+getLuck());
        return this;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public float getCritical_chance() {
        return critical_chance;
    }

    public void setCritical_chance(float critical_chance) {
        this.critical_chance = critical_chance;
    }

    public float getCritical_damage() {
        return critical_damage;
    }

    public void setCritical_damage(float critical_damage) {
        this.critical_damage = critical_damage;
    }

    public float getAttack_speed() {
        return attack_speed;
    }

    public void setAttack_speed(float attack_speed) {
        this.attack_speed = attack_speed;
    }

    public float getVitality() {
        return vitality;
    }

    public void setVitality(float vitality) {
        this.vitality = vitality;
    }

    public float getIntel() {
        return intel;
    }

    public void setIntel(float intel) {
        this.intel = intel;
    }

    public float getDefence() {
        return defence;
    }

    public void setDefence(float defence) {
        this.defence = defence;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getLuck() {
        return luck;
    }

    public void setLuck(float luck) {
        this.luck = luck;
    }

    public enum StatsInfo {
        STRENGTH("🗡", ChatColor.of("#F00020")),
        CRITICAL_DAMAGE("☠ ", ChatColor.of("#FF6600")),
        CRITICAL_CHANCE("☣ ", ChatColor.of("#FFD500")),
        ATTACK_SPEED("⚔", ChatColor.of("#3A9D23")),
        VITALITY("❤", ChatColor.of("#F9429E")),
        INTELLIGENCE("࿇", ChatColor.of("#00ACEE")),
        DEFENCE("🛡", ChatColor.of("#884DA7")),
        SPEED("࿓", ChatColor.of("#201725")),
        LUCK("☘", ChatColor.of("#FFA500"))
        ;

        String character;
        ChatColor color;
        StatsInfo(String character, ChatColor color) {
            this.character = character;
            this.color = color;
        }

        public String getCharacter() {
            return character;
        }

        public ChatColor getColor() {
            return color;
        }
    }

    public static final class Builder {
        private float strength = 0;
        private float critical_chance = 0;
        private float critical_damage = 0;
        private float attack_speed = 0;
        private float vitality = 0;
        private float intel = 0;
        private float defence = 0;
        private float speed = 0;
        private float luck = 0;

        public Builder withStrength(float strength) {
            this.strength = strength;
            return this;
        }

        public Builder withCritical_chance(float critical_chance) {
            this.critical_chance = critical_chance;
            return this;
        }

        public Builder withCritical_damage(float critical_damage) {
            this.critical_damage = critical_damage;
            return this;
        }

        public Builder withAttack_speed(float attack_speed) {
            this.attack_speed = attack_speed;
            return this;
        }

        public Builder withVitality(float vitality) {
            this.vitality = vitality;
            return this;
        }

        public Builder withIntel(float intel) {
            this.intel = intel;
            return this;
        }

        public Builder withDefence(float defence) {
            this.defence = defence;
            return this;
        }

        public Builder withSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder withLuck(float luck) {
            this.luck = luck;
            return this;
        }

        public Stats build() {
            return new Stats(this);
        }

        public float getStrength() {
            return strength;
        }

        public float getCritical_chance() {
            return critical_chance;
        }

        public float getCritical_damage() {
            return critical_damage;
        }

        public float getAttack_speed() {
            return attack_speed;
        }

        public float getVitality() {
            return vitality;
        }

        public float getIntel() {
            return intel;
        }

        public float getDefence() {
            return defence;
        }

        public float getSpeed() {
            return speed;
        }

        public float getLuck() {
            return luck;
        }
    }
}
