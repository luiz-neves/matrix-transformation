////////////////////////////////////////////////////////////////
//                                                            //
// 10687684 - ISABELLE NEVES PORTO                            //
// 10856758 - JOÃO VÍCTOR FERREIRA ARAUJO                     //
//                                                            //
////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//                                                                        //
// Classe principal. Le um arquivo de entrada com a seguinte estrutura:   //
//                                                                        //
//	IMAGE_WIDTH IMAGE_HEIGHT BG_COLOR                                     //
//	OBSERVER_X, OBSERVER_Y, DIRECTION_X, DIRECTION_Y                      //
//	N_SHAPES                                                              //
//	N_VERTICES_SHAPE0 X_0 Y_0 X_1 Y_1 ... X_(N-1) Y_(N-1)                 //
//	N_VERTICES_SHAPE1 X_0 Y_0 X_1 Y_1 ... X_(N-1) Y_(N-1)                 //
//	N_VERTICES_SHAPE2 X_0 Y_0 X_1 Y_1 ... X_(N-1) Y_(N-1)                 //
//	...                                                                   //
//	<DRAW_COMMAND_0>                                                      //
//	<DRAW_COMMAND_1>                                                      //
//	<DRAW_COMMAND_2>                                                      //
//	...                                                                   //
//	END                                                                   //
//                                                                        //
// Sendo que cada linha referente a um comando de desenho pode ser:       //
//                                                                        //
//	DRAW_SHAPE SHAPE_ID COLOR ROTATION SCALE T_X T_Y                      //
//                                                                        //
// OU                                                                     //
//                                                                        //
//	DRAW_SHAPE_BASE SHAPE_ID COLOR E1_X E1_Y E2_X E2_Y T_X T_Y            //
//                                                                        //
// E gera uma imagem a partir das configurações e comandos especificados. //
//                                                                        //
////////////////////////////////////////////////////////////////////////////

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class EP2_esqueleto {

    public static String DRAW_SHAPE = "DRAW_SHAPE";			// constante para o comando DRAW_SHAPE usada no arquivo de entrada
    public static String DRAW_SHAPE_BASE = "DRAW_SHAPE_BASE";	// constante para o comando DRAW_SHAPE_BASE usada no arquivo de entrada
    public static String END = "END";				// indica fim da listagem de comandos no arquivo de entrada

    public static void main(String [] args) throws IOException {

        ////////////////
        //            //
        // Variaveis: //
        //            //
        ////////////////

        String command;
        String input_file_name;		// nome do arquivo de entrada (com as definicoes do cena/desenho)
        String output_file_name;	// nome do arquivo de saida (imagem gerada a partir das definicoes do arquivo de entrada)

        int width;			// largura da imagem a ser gerada
        int height;			// altura da imagem a ser gerada
        int background_color;		// cor de fundo da imagem a ser gerada

        int n_shapes;			// quantidade de shapes definidos no arquivo de entrada

        Vector observer;		// vetor que representa a posicao do observador na cena a ser desenhada
        Vector direction;		// vetor que indica a direcao para a qual o observador olha na cena
        Vector v;			// variavel do tipo vetor de uso geral

        Shape [] shapes;			// vetor de shapes. Usado para armazenar os shapes definidos no arquivo de entrada

        Scanner in;
        //FILE * in;			// FILE handler para o arquivo de entrada
        Image img;			// imagem na qual as operacoes de desenho serao realizada

        ///////////////////////////////////////////
        //                                       //
        // Programa principal propriamente dito: //
        //                                       //
        ///////////////////////////////////////////

        // verificacao dos parametros da linha de comando:

        if(args.length != 2){

            System.out.println("Usage: " + EP2_esqueleto.class.getName() + " <input_file_name> <output_file_name>");
            return;
        }

        input_file_name = args[0];
        output_file_name = args[1];

        // abertura do arquivo de entrada, e leitura dos parametros fixos (parametros da imagem e do observador, quantidade de shapes):

        in = new Scanner(new FileInputStream(input_file_name));
        in.useLocale(Locale.ROOT);

        width = in.nextInt();
        height = in.nextInt();
        background_color = in.nextInt();

        observer = new Vector(in.nextDouble(), in.nextDouble());
        direction = new Vector(in.nextDouble(), in.nextDouble());

        n_shapes = in.nextInt();

        img = new Image(width, height, background_color);	// criacao da imagem
        shapes = new Shape[n_shapes];				// alocacao do vetor de shapes com o tamanho adequado

        // leitura dos shapes definidos no arquivo de entrada:

        for(int i = 0; i < n_shapes; i++){

            shapes[i] = new Shape(in.nextInt());

            for(int j = 0; j < shapes[i].nVertices(); j++) {

                shapes[i].add(new Vector(in.nextDouble(), in.nextDouble()));
            }
        }

        // leitura dos comandos de desenho, até que uma linha com o comando "END" seja encontrada:

        while( !(command = in.next()).equals(END) ) {

            // a cada iteracao um shape sera desenhado atraves do comando DRAW_SHAPE ou DRAW_SHAPE_BASE.
            //
            // Para primeiro comando (DRAW_SHAPE), especifica-se o id do shape a ser desenhdo, sua cor (tonalidade
            // de cinza na realidade), assim como rotacao, fator de escala e vetor de translacao que devem ser
            // aplicados ao shape antes de desenha-lo.
            //
            // Já para o segundo caso (DRAW_SHAPE_BASE), ao inves de especificar os fatores de rotacao e escala,
            // especifica-se os vetores da base que define a transformacao matricial a ser aplicada no shape, além
            // de também se espeficiar o vetor de translacao que tambem deve ser aplicado. Apesar de ser menos intuitivo
            // este segundo comando permite especificar transformacoes com maior flexibilidade (escalas nao uniformes, e
            // cizalhamentos, por exemplo).

            if(command.equals(DRAW_SHAPE) || command.equals(DRAW_SHAPE_BASE)){

                int shape_id = in.nextInt();
                Shape shape = shapes[shape_id];
                Vector[] finalImage = new Vector[shape.nVertices()];

                int color = in.nextInt();

                if(command.equals(DRAW_SHAPE)){

                    double rotation = in.nextDouble();
                    double scale = in.nextDouble();
                    Vector t = new Vector(in.nextDouble(), in.nextDouble());

                    for (int i = 0; i < finalImage.length; i++) {
                        Vector p = shape.get(i);
                        finalImage[i] = rotation(p, rotation);
                        finalImage[i] = scale(finalImage[i], scale);
                        finalImage[i] = translate(finalImage[i], t);
                    }
                }

                if(command.equals(DRAW_SHAPE_BASE)){

                    Vector e1 = new Vector(in.nextDouble(), in.nextDouble());
                    Vector e2 = new Vector(in.nextDouble(), in.nextDouble());
                    Vector t = new Vector(in.nextDouble(), in.nextDouble());

                    for (int i = 0; i < finalImage.length; i++) {
                        Vector p = shape.get(i);
                        finalImage[i] = transform(p, e1, e2, t);
                    }
                }

                for(int i = 0; i < finalImage.length - 1; i++){
                    Vector v1 = observer(finalImage[i], observer, direction);
                    Vector v2 = observer(finalImage[i + 1], observer, direction);
                    img.draw_line(v1, v2, color);
                }
            }
            else {
                System.out.println("Unknown command: " + command);
            }
        }

        img.save(output_file_name);	// salva imagem no arquivo de saida
    }

    private static Vector rotation(Vector v, double theta) {
        Matrix rotationMatrix = Matrix.get_rotation_matrix(theta);
        return rotationMatrix.transform(v);
    }

    private static Vector scale(Vector v, double scale) {
        Matrix scaleMatrix = Matrix.get_scale_matrix(scale);
        return scaleMatrix.transform(v);
    }

    private static Vector translate(Vector v, Vector translateVector) {
        Matrix p = new Matrix(4, 1, new double[] {v.getX(), v.getY(), 1, 1});
        Matrix translationMatrix = Matrix.get_translation_matrix(translateVector);
        Matrix result = translationMatrix.multiply(p);
        return new Vector(result.get(0, 0), result.get(1, 0));
    }

    private static Vector transform(Vector v, Vector e1, Vector e2, Vector t) {
        Matrix transformationMatrix = Matrix.get_transformation_matrix(e1, e2, t);
        return transformationMatrix.transform(v);
    }

    private static Vector observer(Vector v, Vector observer, Vector direction) {
        Matrix observerMatrix = Matrix.get_observer_matrix(observer, direction);
        return observerMatrix.transform(v);
    }
}
