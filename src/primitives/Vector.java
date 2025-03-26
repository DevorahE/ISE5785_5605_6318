package primitives;
/**
 * Class Vector is the basic class representing a vector of geometry
 * 3-Dimensional coordinate system.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Vector extends Point{

    /**
     * Constructor to initialize Vector based on 3 number values
     *
     * @param x - number value for x coordinate
     * @param y - number value for y coordinate
     * @param z - number value for z coordinate
     */
    public Vector(double x, double y, double z) throws IllegalArgumentException{
        super(x,y,z);

        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    /**
     * Constructor to initialize Vector based on 3 double numbers (Double3) value
     *
     * @param xyz - number value for all 3 numbers
     */
    public Vector(Double3 xyz) throws IllegalArgumentException
    {
        super(xyz);

        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    /**
     * Sums two vectors into a new vector where each coordinate is summarized
     *
     * @param vector - right handle side operand for addition
     * @return vector result of addition
     */
    public Vector add(Vector vector)
    {
        return new Vector(this.xyz.add(vector.xyz));
    }

    /**
     * Cales a vector into a new vector where each coordinate is multiplied by the scale factor
     *
     * @param scalar
     * @return vector
     */
    public Vector scale(double scalar)
    {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Calculates the dot product of two vectors
     *
     * @param vector - right handle side operand for dot product calculation
     * @return result of dot product
     */
    public double dotProduct(Vector vector)
    {
        return this.xyz.d1() * vector.xyz.d1() + this.xyz.d2() * vector.xyz.d2() + this.xyz.d3() * vector.xyz.d3();
    }

    /**
     * Calculates the cross product of two vectors
     *
     * @param vector - vector right handle side operand for cross product calculation
     * @return result of cross product
     */
    public Vector crossProduct(Vector vector)
    {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Calculates length of the vector squared
     *
     * @return length of vector squared
     */
    public double lengthSquared()
    {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3()* xyz.d3();
    }

    /**
     * Length of the vector
     *
     * @return length of the vector
     */
    public double length()
    {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector
     *
     * @return a new normalized vector
     */
    public Vector normalize()
    {
        if(Util.isZero(length()))
            throw new ArithmeticException("Can't normalize vector ZERO");

        return new Vector(xyz.reduce(this.length()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Vector other)
                && super.equals(other);
    }

    @Override
    public String toString() {
        return  "->" + super.toString();
    }
}
