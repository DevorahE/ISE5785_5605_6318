package primitives;

public class Vector extends Point{

    public Vector(double x, double y, double z){
        super(x,y,z);
        if(this.equals(ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    public Vector(Double3 xyz)
    {
        super(xyz);
        if(this.equals(ZERO))
            throw new IllegalArgumentException("The vector is ZERO!");
    }

    public Vector add(Vector vector)
    {
        //TODO
        return new Vector(0,0,0);
    }

    public Vector scale(double d)
    {
        //TODO
        return new Vector(0,0,0);
    }

    public Vector dotProduct(Vector vector)
    {
        //TODO
        return new Vector(0,0,0);
    }

    public Vector crossProduct(Vector vector)
    {
        //TODO
        return new Vector(0,0,0);
    }

    public double lengthSquared()
    {
        //TODO
        return 0;
    }

    public double length()
    {
        //TODO
        return 0;
    }

    public Vector normalize()
    {
        //TODO
        return new Vector(0,0,0);
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
