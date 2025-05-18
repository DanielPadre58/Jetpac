package jetpac.generator;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.astro.Direcao;
import jetpac.enemy.*;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteMultiAnimado;

/**
 * Esta classe é responsável por gerar os inimigos de cada nível.
 */
public class EnemyGenerator extends GeneratorDefault {

    private static final int creationCicle = 800; // o ciclo de criação é de 800 milisegundos

    private String type; // o tipo de inimigo a criar
    private int enemyVel; // a velocidade de cada inimigo
    private ComponenteMultiAnimado img; // a imagem que representa o inimigo
    private int enemyScore; // a pontuação de cada inimigo

    /**
     * Cria um gerador de inimigos
     *
     * @param type       tipo dos inimigos a criar
     * @param maxEnemies número máximo de inimigos simultâneos
     * @param enemyVel   velocidade dos inimigos
     * @param enemyScore pontuação de cada inimigos
     * @param img        imagem do inimigo
     * @param world      mundo onde colocar os inimigos
     */
    public EnemyGenerator(String type, int maxEnemies, int enemyVel, int enemyScore, ComponenteMultiAnimado img, Mundo world) {
        super(maxEnemies, creationCicle, 0, world);
        this.enemyVel = enemyVel;
        this.img = img;
        this.enemyScore = enemyScore;
        this.type = type;
    }

    /**
     * Método que trata da criação dos inimigos, se for altura de os criar
     */
    @Override
    public void update() {
        nElementos = mundo.getNumEnemies();
        if (!podeCriarElemento()) return;

        // criar sempre metade dos inimigos mais 1, para não criar todos de uma vez
        for (int i = 0; i <= (maxElementos - nElementos) / 2; i++) {
            // escolher aleatoriamente a coordenada y onde vai aparecer o inimigo
            int y = ThreadLocalRandom.current().nextInt(mundo.getHeight() - 2 * img.getAltura());

            // escolher se aparece do lado esquerdo ou direito (1= direito, 0 = esquerdo)
            int r = ThreadLocalRandom.current().nextInt(2);
            Direcao dir;
            Point pos;
            if (r == 0) {
                dir = Direcao.RIGHT; // move-se para a direita
                pos = new Point(5 - img.getComprimento(), y); // aparece do lado esquerdo
            } else {
                dir = Direcao.LEFT;
                pos = new Point(mundo.getWidth() - 5, y);
            }

            // verificar qual o tipo de inimigo a criar
            InimigoDefault e = switch (type) {
                case "linear" -> new Linear(pos, enemyVel, enemyScore, dir, img);
                case "ricochete" -> new Ricochete(pos, enemyVel, enemyScore, dir, img);
                case "flutuante" -> new Flutuante(pos, enemyVel, enemyScore, dir, img);
                case "perseguidor" -> new Perseguidor(pos, enemyVel, enemyScore, dir, img);
                case "sinusoidal" -> new Sinusoidal(pos, enemyVel, enemyScore, dir, img);
                case "saltador" -> new Saltador(pos, enemyVel, enemyScore, dir, img);
                case "ziguezague" -> new Ziguezague(pos, enemyVel, enemyScore, dir, img);
                default -> new Linear(pos, enemyVel, enemyScore, dir, img); // por defeito assume que é linear
            };
            // adicionar o inimigo ao mundo
            mundo.addEnemy(e);
            super.update();
        }
    }
}
