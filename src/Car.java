//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: Car.java
//∗ @description: This program helps store car values
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
public class Car implements Comparable<Car> {
    public final String name;
    public final String origin;
    public final double mpg;
    public final int cylinders;
    public final double displacement;
    public final int horsepower;
    public final int weight;
    public final double acceleration;
    public final int modelYear;

    // default constructor
    public Car() {
        name = null;
        origin = null;
        mpg = 0;
        cylinders = 0;
        displacement = 0;
        horsepower = 0;
        weight = 0;
        acceleration = 0;
        modelYear = 0;
    }

    // parameterized constructor
    public Car(String name, double mpg, int cylinders, double displacement,
               int horsepower, int weight, double acceleration,
               int modelYear, String origin) {
        this.name = name;
        this.mpg = mpg;
        this.cylinders = cylinders;
        this.displacement = displacement;
        this.horsepower = horsepower;
        this.weight = weight;
        this.acceleration = acceleration;
        this.modelYear = modelYear;
        this.origin = origin;
    }

    // copy constructor
    public Car(Car other) {
        this(other.name, other.mpg, other.cylinders, other.displacement,
                other.horsepower, other.weight, other.acceleration,
                other.modelYear, other.origin);
    }

    @Override
    public String toString() {
        String output = "";
        output += "Name: " + name + "\n";
        output += "Origin: " + origin + "\n";
        output += "Mpg: " + mpg + "\n";
        output += "Cylinders: " + cylinders + "\n";
        output += "Displacement: " + displacement + "\n";
        output += "Horsepower: " + horsepower + "\n";
        output += "Weight: " + weight + "\n";
        output += "Acceleration: " + acceleration + "\n";
        output += "ModelYear: " + modelYear + "\n";
        return output;
    }

    public boolean equals(Car o) {
        if (o == null) return false;
        return name.equalsIgnoreCase(o.name)
                && origin.equalsIgnoreCase(o.origin)
                && Double.compare(mpg, o.mpg) == 0
                && Double.compare(displacement, o.displacement) == 0
                && Double.compare(acceleration, o.acceleration) == 0
                && cylinders == o.cylinders
                && horsepower == o.horsepower
                && weight == o.weight
                && modelYear == o.modelYear;
    }

    // Natural order for the BST:
    // By name (case-insensitive)
    @Override
    public int compareTo(Car obj) {
        if (obj == null) return -1;
        int c = this.name.compareToIgnoreCase(obj.name);
        return c;
    }
}
