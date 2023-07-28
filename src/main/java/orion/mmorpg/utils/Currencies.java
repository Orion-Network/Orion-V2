package orion.mmorpg.utils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Currencies {
    @Getter
    public HashMap<Currency, Integer> currencies;
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

    public int get(String name) {
        if(hasCurrency(name)) {
            return currencies.get(getCurrency(name));
        } else {
            return 0;
        }
    }

    public Currency getCurrency(String name) {
        for(Currency currency : currencies.keySet()) {
            if(currency.name.equalsIgnoreCase(name)) {
                return currency;
            }
        }
        return null;
    }

   public void add(String name, int amount) {
        if(hasCurrency(name)) {
            Currency currency = getCurrency(name);
            currencies.put(currency, currencies.get(currency) + amount);
        } else {
            currencies.put(new Currency(name), amount);
        }
   }

   public void add(Currency currency, int amount) {
        if(hasCurrency(currency.name)) {
            currencies.put(currency, currencies.get(currency) + amount);
        } else {
            currencies.put(currency, amount);
        }
   }

    public boolean remove(String name, int amount) {
          if(hasCurrency(name)) {
                currencies.put(getCurrency(name), Math.max(0, currencies.get(name) - amount));
                return true;
          } else {
              return false;
          }
    }

   public boolean hasCurrency(String name) {
       return currencies.containsKey(getCurrency(name));
   }

   public List<String> asLore() {
       ArrayList<String> list = new ArrayList<String>();
       if(currencies.isEmpty()) {
           list.add("§c§lNo currencies");
           return list;
       }
         for(Currency currency : currencies.keySet()) {
              list.add(ChatColor.of(currency.color) + "" + currencies.get(currency) + " " + currency.name);
         }
            return list;
   }

   public static class Currency {
        @Getter
       public String name;
       @Getter@Setter
       public String symbol = "\uD83D\uDCB0", color = "#FFFFFF";

         public Currency(String name) {
              this.name = name;
         }
   }
}
