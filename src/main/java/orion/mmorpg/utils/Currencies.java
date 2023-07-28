package orion.mmorpg.utils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if(hasCurrency(name)) {
            return currencies.get(name).getAmount();
        } else {
            return 0;
        }
    }

    public Currency getCurrency(String name) {
        return currencies.get(name);
    }

   public void add(String name, long amount) {
        if(hasCurrency(name)) {
            currencies.get(name).add(amount);
        } else {
            currencies.put(name, new Currency(name, amount));
        }
   }

   public void add(Currency currency) {
         if(hasCurrency(currency.getName())) {
              currencies.get(currency.getName()).add(currency.getAmount());
         } else {
              currencies.put(currency.getName(), currency);
         }
   }

    public boolean remove(String name, int amount) {
        if(hasCurrency(name)) {
            if(currencies.get(name).getAmount() >= amount) {
                currencies.get(name).remove(amount);
                return true;
            }
        }
        return false;
    }

    public boolean hasEnough(String name, int amount) {
        if(hasCurrency(name)) {
            return currencies.get(name).getAmount() >= amount;
        }
        return false;
    }

   public boolean hasCurrency(String name) {
       return currencies.containsKey(name);
   }

   public List<String> asLore() {
       ArrayList<String> list = new ArrayList<String>();
       if(currencies.isEmpty()) {
           list.add("§c§lNo currencies");
           return list;
       }
         for(Currency currency : currencies.values()) {
              list.add(ChatColor.of(currency.color) + "" + currencies.get(currency) + " " + currency.name);
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
           this.name = name;
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
