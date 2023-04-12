package my.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Pokemon {
    private int id;
    private String name;
    private String category;
    private double weight;
    private String ablility;
    private String types;
    private String description;
    private Stats stats;

    public Pokemon(int id, String name, String category, double weight, String ablility, String types, String description, Stats stats) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.ablility = ablility;
        this.types = types;
        this.description = description;
        this.stats = stats;
    }

    public Pokemon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAblility() {
        return ablility;
    }

    public void setAblility(String ablility) {
        this.ablility = ablility;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

}
