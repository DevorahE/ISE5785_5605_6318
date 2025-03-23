package primitives;

public class Vector extends Point{

    public Vector(double x, double y, double z) throws IllegalArgumentException{
        super(x,y,z);
        if(this.equals(ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    public Vector(Double3 xyz) throws IllegalArgumentException
    {
        super(xyz);
        if(this.equals(ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    public Vector add(Vector vector)
    {
        return new Vector(this.xyz.add(vector.xyz));
    }

    public Vector scale(double d)
    {
        return new Vector(this.xyz.scale(d));
    }

    public double dotProduct(Vector vector)
    {
        return this.xyz.d1() * vector.xyz.d1() + this.xyz.d2() * vector.xyz.d2() + this.xyz.d3() * vector.xyz.d3();
    }

    public Vector crossProduct(Vector vector)
    {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    public double lengthSquared()
    {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3()* xyz.d3();
    }

    public double length()
    {
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize()
    {
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
        return super.toString();
    }
}
