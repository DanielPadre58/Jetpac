package jetpac.generator;

import jetpac.drag.Tesouro;
import jetpac.mundo.Mundo;
import prof.jogos2D.util.ReguladorVelocidade;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe responsável pela criação de tesouros. Cada tesouro só pode ser criado
 * se não houver mais tesouros no mundo. O tempo entre um tesouro ser colocado
 * na nave e a criação do próximo é aleatório, mas existe um tempo mínimo e um
 * tempo máximo para esse tempo.
 */
public class TreasureGenerator extends GeneratorDefault {

    private TreasureInfo[] tInfo; // informação sobre os tesouros que podem ser criados

    /**
     * cria o gerador de tesouros creates the treasure generator
     *
     * @param min   tempo mínimo entre criação de tesouros
     * @param max   tempo máximo entre criação de tesouros
     * @param tInfo informação sobre os tesouros a serem criados
     * @param w     mundo ao qual os tesouros serão adicionados
     */
    public TreasureGenerator(int maxTreasures, int min, int max, TreasureInfo[] tInfo, Mundo w) {
        super(maxTreasures, min, max - min, w);
        this.tInfo = tInfo;
    }

    /**
     * método que trata da criação dos tesouros
     */
    @Override
    public void update() {
        super.update();

        // escolher aleatoriamente qual o tesouro a criar
        int prob = ThreadLocalRandom.current().nextInt(100);

        // ver qual dos tesouros tem a probabilidade escolhida
        int total = 0;
        Tesouro t = null;
        for (int i = 0; i < tInfo.length; i++) {
            total += tInfo[i].getProbability();
            if (prob < total) {
                // escolher a coordenada x e criar o tesouro
                int x = tInfo[i].getImg().getComprimento()
                        + ThreadLocalRandom.current()
                        .nextInt(mundo.getWidth() - 2 * tInfo[i].getImg().getComprimento());
                t = tInfo[i].createTresure(new Point(x, 0));

                break;
            }
        }

        mundo.adicionarElementoPegavel(t);
    }

    public void treasureRemoved() {
        nElementos--;
    }
}
