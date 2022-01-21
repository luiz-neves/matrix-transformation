class Shape {

    public static final int MAX_VERTICES = 100;	// numero maximo de vertices que um shape pode possuir

    private int free;
    private Vector [] vertices;

    public Shape(int n){

        free = 0;
        vertices = new Vector[n];
    }

    public void add(Vector v){

        if(free < vertices.length){

            vertices[free++] = v;
        }
    }

    public int nVertices(){

        return vertices.length
                ;
    }

    public Vector get(int i){

        return i < free ? vertices[i] : null;

    }

}