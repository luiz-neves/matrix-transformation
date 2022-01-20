import java.io.IOException;
import java.io.PrintWriter;

class Image {

    public static final int MAX_VALUE = 255;	// valor maximo que um pixel da image pode assumir

    private int width;	// largura da imagem (numero de colunas da imagem)
    private int height;	// altura da imagem (numero de linhas da imagem)
    int [][] matrix;	// matriz que armazena o conteudo da imagem

    public Image(int width, int height, int bg_color){

        this.width = width;
        this.height = height;

        matrix = new int[this.height][this.width];

        for(int i = 0; i < this.height; i++) {

            for(int j = 0; j < this.width; j++) {

                matrix[i][j] = bg_color;
            }
        }
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    // metodo que salva uma imagem em um arquivo, no formato PGM. Programas/utilitarios que trabalham com
    // imagens bitmap (como o Gimp e visualizadores de imagens normalmente instalados em ambiente Linux) em
    // geral reconhecem este formato. Se tiver dificuldades para visualizar os arquivos de imagem salvos por 
    // esta funcao, um visualizador online pode ser encontrado neste link: http://paulcuth.me.uk/netpbm-viewer/.

    public void save(String file_name){

        try{
            PrintWriter out = new PrintWriter(file_name);

            out.println("P2");
            out.println(width + " " + height);
            out.println(MAX_VALUE);

            for(int lin = 0; lin < height; lin++){

                for(int col = 0; col < width; col++){

                    out.print((col == 0 ? "" : " ") + matrix[lin][col]);
                }

                out.println();
            }

            out.close();
        }
        catch(IOException e){

            e.printStackTrace();
        }
    }

    // metodo que pinta um pixel da imagem com a "cor" (tonalidade de cinza) especificada.

    public void set_pixel(double x, double y, int color){

        int col = (int) Math.round(x);
        int lin = (int) Math.round(y);

        if(lin < 0 || col < 0 || col >= width || lin >= height) return;

        matrix[lin][col] = color;
    }

    // metodo que desenha uma linha conectando os pontos representados pelos vetores v1 e v2, 
    // com a cor especificada. A area de desenho compreende o retangulo formado pelos seguintes 
    // cantos, independente da dimensao real da imagem em pixels:
    //
    //   (-1,  1): canto superior esquerdo
    //   ( 1,  1): canto superior direito
    //   ( 1, -1): canto inferior direito
    //   (-1, -1): canto inferior esquerdo
    //
    // Logo, espera-se que as coordenadas dos vetores estejam dentro destes limites

    public void draw_line(Vector v1, Vector v2, int color){

        Vector p1 = v1.convert(width, height);
        Vector p2 = v2.convert(width, height);
        Vector a, b;

        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());
        double x, y;

        if(deltaX >= deltaY){

            a = p1.getX() < p2.getX() ? p1 : p2;
            b = p1.getX() < p2.getX() ? p2 : p1;

            for(x = a.getX(); x <= b.getX(); x++){

                y = ((x - a.getX()) / deltaX ) * (b.getY() - a.getY()) + a.getY();
                set_pixel(x, y, color);
            }
        }
        else{

            a = p1.getY() < p2.getY() ? p1 : p2;
            b = p1.getY() < p2.getY() ? p2 : p1;

            for(y = a.getY(); y <= b.getY(); y++){

                x = ((y - a.getY()) / deltaY ) * (b.getX() - a.getX()) + a.getX();
                set_pixel(x, y, color);
            }
        }
    }
}