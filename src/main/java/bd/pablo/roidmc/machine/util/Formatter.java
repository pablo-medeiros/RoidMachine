package bd.pablo.roidmc.machine.util;

import com.roidmc.core.util.java.NumberFormatter;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Map;

public class Formatter {

    public static String fuel(long amount){
        if(amount>=1000){
            return String.format("%.1fL",((double)amount/1000));
        }
        return amount+"ML";
    }

    public static String weight(Map<Material,Long> materials){
        return weight(materials.entrySet());
    }
    public static String weight(Collection<Map.Entry<Material, Long>> materials){
        long total = 0;
        for(Map.Entry<Material, Long> entry : materials){
            int singleWeight = entry.getKey().isBlock() ? 20 : 7;
            total+= singleWeight*entry.getValue();
        }
        if(total>=1000){
            return String.format("%.1fKg",(double)total/1000);
        }
        return total+"G";
    }

    public static String price(double amount){
        return NumberFormatter.kmb(amount);
    }
}
