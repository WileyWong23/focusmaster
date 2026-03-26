package com.focusmaster.enums;

public enum Rank {
    BRONZE("青铜", "#665500", "stars", "初心萌动 · 专注之旅从这里开始"),
    SILVER("白银", "#6d758c", "verified", "初露锋芒 · 坚持的力量正在显现"),
    GOLD("黄金", "#ffd709", "star", "锐意进取 · 你已是专注的践行者"),
    PLATINUM("白金", "#81ecff", "verified", "超越自我 · 纯粹的意志铸就辉煌"),
    DIAMOND("钻石", "#00d4ec", "diamond", "坚不可摧 · 璀璨如你的毅力"),
    STARRY("星耀", "#a68cff", "auto_awesome", "星辰大海 · 你的专注闪耀天际"),
    KING("王者", "#ffd709", "workspace_premium", "至高荣耀 · 登峰造极的专注大师");

    private final String name;
    private final String color;
    private final String icon;
    private final String description;

    Rank(String name, String color, String icon, String description) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.description = description;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
}
