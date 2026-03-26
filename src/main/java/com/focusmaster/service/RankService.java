package com.focusmaster.service;

import com.focusmaster.enums.Rank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankService {

    private static final LinkedHashMap<Rank, Double> THRESHOLDS = new LinkedHashMap<>();
    static {
        THRESHOLDS.put(Rank.BRONZE,   0.0);
        THRESHOLDS.put(Rank.SILVER,   0.05);
        THRESHOLDS.put(Rank.GOLD,     0.15);
        THRESHOLDS.put(Rank.PLATINUM, 0.30);
        THRESHOLDS.put(Rank.DIAMOND,  0.50);
        THRESHOLDS.put(Rank.STARRY,   0.75);
        THRESHOLDS.put(Rank.KING,     1.0);
    }

    public Rank calculateRank(long accumulatedMinutes, long totalMinutes) {
        if (totalMinutes <= 0) return Rank.BRONZE;
        double percent = (double) accumulatedMinutes / totalMinutes;
        if (percent >= 1.0) return Rank.KING;
        if (percent >= 0.75) return Rank.STARRY;
        if (percent >= 0.50) return Rank.DIAMOND;
        if (percent >= 0.30) return Rank.PLATINUM;
        if (percent >= 0.15) return Rank.GOLD;
        if (percent >= 0.05) return Rank.SILVER;
        return Rank.BRONZE;
    }

    public List<Map<String, Object>> getRankThresholds(long totalMinutes) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<Rank, Double> entry : THRESHOLDS.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("rank", entry.getKey().name());
            item.put("rankName", entry.getKey().getName());
            item.put("color", entry.getKey().getColor());
            item.put("icon", entry.getKey().getIcon());
            item.put("description", entry.getKey().getDescription());
            item.put("percent", entry.getValue() * 100);
            item.put("minutesRequired", (long) Math.max(1, Math.ceil(totalMinutes * entry.getValue())));
            list.add(item);
        }
        return list;
    }

    public int getProgressPercent(long accumulatedMinutes, long totalMinutes) {
        if (totalMinutes <= 0) return 0;
        return (int) Math.min(100, (accumulatedMinutes * 100) / totalMinutes);
    }
}
