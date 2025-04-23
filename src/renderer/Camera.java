package renderer;

import primitives.*;

public class Camera implements Cloneable {
    // Camera properties
    private Point p0 = Point.ZERO; // Camera position
    private Vector vTo = new Vector(0,0,-1); // Camera direction
    private Vector vUp = new Vector(0,1,0); // Camera up vector
    private Vector vRight = new Vector(1,0,0); // Camera right vector
    private double width = 0.0; // Width of the view plane
    private double height = 0.0; // Height of the view plane
    private double distance = 0.0; // Distance from the camera to the view plane

    public Ray constructRay(int nX, int nY, int j, int i){
        //todo
        return null;
    }
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i){
        //todo
        return null;
    }
    public Point getP0() {
        return p0;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    private Camera(){
        // Private constructor to prevent instantiation
    }
    public Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        /// initialization of camera here; no need of constructor
        private final Camera camera = new Camera();


        public Builder setResolution(int nx,int ny){
            return this;
        }

        public Builder setP0(Point p0) {
            camera.p0 = p0;
            return this;
        }

        public Builder setVTo(Vector vTo) {
            camera.vTo = vTo;
            return this;
        }

        public Builder setVUp(Vector vUp) {
            camera.vUp = vUp;
            return this;
        }

        public Builder setVPSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVPDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        public Camera build()  {
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}

