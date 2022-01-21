class Vector {

    private double x;	// coordenada horizontal
    private double y;	// coordenada vertical

    public Vector(double x, double y){

        this.x = x;
        this.y = y;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    // metodo que converte as coordenadas de um vetor do espaco referente a area de desenho
    // (veja detalhamento nos comentarios da funcao draw_line) para o espaco referente a imagem
    // propriamente dita.

    Vector convert(double width, double height){

        return new Vector((this.x + 1) * width / 2.0, (1 - this.y) * height / 2.0);
    }
}