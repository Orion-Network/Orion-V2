package orion.mmorpg.utils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO change so there is no multiple call to toLowerCase
public class Currencies {
    @Getter
    public HashMap<String, Currency> currencies;
    private static final Utils.NumberFormat DEFAULT_FORMAT = Utils.NumberFormat.READABLE;

    public Currencies() {
        currencies = new HashMap<>();
    }

    public String getFormatted(String name, Utils.NumberFormat format) {
        if(format == null) {
            format = DEFAULT_FORMAT;
        }
        return format.format(get(name)) + " " + name;
    }

    public long get(String name) {
        name = name.toLowerCase();
        if(hasCurrency(name)) {
            return currencies.get(name).getAmount();
        } else {
            return 0;
        }
    }

    public Currency getCurrency(String name) {
        return currencies.get(name.toLowerCase());
    }

   public void add(String name, long amount) {
        name = name.toLowerCase();
        if(hasCurrency(name)) {
            currencies.get(name).add(amount);
        } else {
            currencies.put(name, new Currency(name, amount));
        }
   }

   public void add(Currency currency) {
        String name = currency.getName().toLowerCase();
         if(hasCurrency(name)) {
              currencies.get(name).add(currency.getAmount());
         } else {
              currencies.put(name, currency);
         }
   }

    public boolean remove(String name, int amount) {
        name = name.toLowerCase();
        if(hasCurrency(name)) {
            if(currencies.get(name).getAmount() >= amount) {
                currencies.get(name).remove(amount);
                return true;
            }
        }
        return false;
    }

    public boolean hasEnough(String name, int amount) {
        name = name.toLowerCase();
        if(hasCurrency(name)) {
            return currencies.get(name).getAmount() >= amount;
        }
        return false;
    }

   public boolean hasCurrency(String name) {
        name = name.toLowerCase();
       return currencies.containsKey(name);
   }

   public List<String> asLore() {
       ArrayList<String> list = new ArrayList<String>();
       if(currencies.isEmpty()) {
           list.add("§c§lNo currencies");
           return list;
       }
         for(Currency currency : currencies.values()) {
              list.add(ChatColor.of(currency.color) + "" + currency.getAmount()+ " " + currency.name);
         }
            return list;
   }

   public static class Currency {
        @Getter
       public String name;
       @Getter@Setter
       public String symbol = "\uD83D\uDCB0", color = "#FFFFFF";
       @Getter@Setter
       long amount;

       public Currency(String name, long amount) {
           this.name = StringUtils.capitalize(name);
           this.amount = amount;
       }

       public void add(long amount) {
           this.amount += amount;
       }

       public void remove(long amount) {
           this.amount -= amount;
       }
    }
}
