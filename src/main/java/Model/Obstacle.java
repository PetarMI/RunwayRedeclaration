package Model;


public class Obstacle {

    private String name, description;
    private int height, width, length;


    /**
    * Default constructor for an obstacle
     */
    public Obstacle(String name, int width, int height, int length, String description) {
        this.name = (name == null? "Temporary Obstacle" : name);
        this.width = width;
        this.height = height;
        this.length = length;
        this.description = (description == null? "Temporary Description" : description);
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Obstacle obstacle = (Obstacle) o;

        if (height != obstacle.height) return false;
        if (length != obstacle.length) return false;
        if (width != obstacle.width) return false;
        if (description != null ? !description.equals(obstacle.description) : obstacle.description != null)
            return false;
        if (name != null ? !name.equals(obstacle.name) : obstacle.name != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return name;

    }
}
