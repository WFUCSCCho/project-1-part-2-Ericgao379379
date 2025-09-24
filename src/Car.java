//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: Car.java
//∗ @description: This program helps store car values
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
import java.util.Objects;

public class Car implements Comparable<Car> {
    public final String  name;
    public final Double  mpg;
    public final Integer cylinders;
    public final Double  displacement;
    public final Integer horsepower;     // null if "?"
    public final Integer weight;
    public final Double  acceleration;
    public final Integer modelYear;
    public final String  origin;

    public Car(String name, Double mpg, Integer cylinders, Double displacement,
                      Integer horsepower, Integer weight, Double acceleration,
                      Integer modelYear, String origin) {
        this.name = name; this.mpg = mpg; this.cylinders = cylinders;
        this.displacement = displacement; this.horsepower = horsepower;
        this.weight = weight; this.acceleration = acceleration;
        this.modelYear = modelYear; this.origin = origin;
    }

    /** Build from a CSV row with 9 columns. */
    public static Car fromCsvRow(String row) {
        String[] p = row.split(",", -1);
        if (p.length < 9) return null;
        try {
            String  name  = p[0].trim();
            Double  mpg   = parseD(p[1]);
            Integer cyl   = parseI(p[2]);
            Double  disp  = parseD(p[3]);
            Integer hp    = parseNullableI(p[4]);        // "?" allowed
            Integer wt    = parseI(p[5]);
            Double  acc   = parseD(p[6]);
            Integer year  = parseI(p[7]);
            String  orig  = p[8].trim();
            return new Car(name, mpg, cyl, disp, hp, wt, acc, year, orig);
        } catch (NumberFormatException e) { return null; }
    }

    private static Integer parseI(String s){ return java.lang.Integer.valueOf(s.trim()); }
    private static Double  parseD(String s){ return java.lang.Double.valueOf(s.trim()); }
    private static Integer parseNullableI(String s){
        s = s.trim(); if (s.isEmpty() || "?".equals(s)) return null; return java.lang.Integer.valueOf(s);
    }

    /** CSV string, preserving '?' for null horsepower and trimming .0 */
    @Override public String toString() {
        return String.join(",",
                name,
                fmt(mpg), String.valueOf(cylinders), fmt(displacement),
                (horsepower==null ? "?" : String.valueOf(horsepower)),
                String.valueOf(weight), fmt(acceleration),
                String.valueOf(modelYear), origin
        );
    }
    private static String fmt(Double d){
        if (d == null) return "";
        long asInt = Math.round(d);
        return (Math.abs(d - asInt) < 1e-9) ? java.lang.String.valueOf(asInt) : d.toString();
    }

    /** Total ordering using all fields; equals-consistent. */
    @Override public int compareTo(Car o) {
        int c;
        if ((c = name.compareToIgnoreCase(o.name)) != 0) return c;
        if ((c = cmp(modelYear, o.modelYear)) != 0) return c;
        if ((c = cmp(mpg, o.mpg)) != 0) return c;
        if ((c = cmp(cylinders, o.cylinders)) != 0) return c;
        if ((c = cmp(displacement, o.displacement)) != 0) return c;
        if ((c = cmp(horsepower, o.horsepower)) != 0) return c;   // nulls first
        if ((c = cmp(weight, o.weight)) != 0) return c;
        if ((c = cmp(acceleration, o.acceleration)) != 0) return c;
        return origin.compareToIgnoreCase(o.origin);
    }
    private static <E extends Comparable<E>> int cmp(E a, E b){
        if (a==null && b==null) return 0;
        if (a==null) return -1;
        if (b==null) return 1;
        return a.compareTo(b);
    }

    @Override public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Car other)) return false;
        return name.equalsIgnoreCase(other.name)
                && Objects.equals(mpg, other.mpg)
                && Objects.equals(cylinders, other.cylinders)
                && Objects.equals(displacement, other.displacement)
                && Objects.equals(horsepower, other.horsepower)
                && Objects.equals(weight, other.weight)
                && Objects.equals(acceleration, other.acceleration)
                && Objects.equals(modelYear, other.modelYear)
                && origin.equalsIgnoreCase(other.origin);
    }
    @Override public int hashCode(){
        return Objects.hash(name.toLowerCase(), mpg, cylinders, displacement,
                horsepower, weight, acceleration, modelYear, origin.toLowerCase());
    }
}
