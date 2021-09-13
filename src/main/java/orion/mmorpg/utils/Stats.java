package orion.mmorpg.utils;

import org.bukkit.ChatColor;

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

    public List<String> statsAsLore() {
        List<String> lore = new LinkedList<>();
        if(strength != 0)
            lore.add(ChatColor.GRAY + "Strength: " + ChatColor.RED + strength);
        if(critical_damage != 0)
            lore.add(ChatColor.GRAY + "Critical Damage: " + ChatColor.RED + critical_damage);
        if(critical_chance != 0)
            lore.add(ChatColor.GRAY + "Critical Chance: " + ChatColor.RED + critical_chance + "%");
        if(attack_speed != 0)
            lore.add(ChatColor.GRAY + "Attack Speed: " + ChatColor.YELLOW + attack_speed);
        if(vitality != 0)
            lore.add(ChatColor.GRAY + "Vitality: " + ChatColor.YELLOW + vitality);
        if(intel != 0)
            lore.add(ChatColor.GRAY + "Intelligence: " + ChatColor.YELLOW + intel);
        if(defence != 0)
            lore.add(ChatColor.GRAY + "Defence: " + ChatColor.YELLOW + defence);
        if(speed != 0)
            lore.add(ChatColor.GRAY + "Speed: " + ChatColor.WHITE + speed);
        if(luck != 0)
            lore.add(ChatColor.GRAY + "Luck: " + ChatColor.WHITE + luck);
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
}
