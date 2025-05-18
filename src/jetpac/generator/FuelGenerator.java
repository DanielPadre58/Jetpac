package jetpac.generator;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.mundo.WorldElement;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ReguladorVelocidade;
import jetpac.drag.Fuel;
import jetpac.mundo.Mundo;

/**
 * Classe responsável pela criação do combustível para a nave. Para criar um
 * fuel não pode haver fuels no mundo. O tempo entre um fuel ser colocado na
 * nave e a criação do próximo é aleatório, mas existe um tempo mínimo e um
 * tempo máximo para esse tempo.
 */
public class FuelGenerator extends GeneratorDefault {

    private ComponenteVisual img; // imagem de cada fuel
    private boolean foiEntregue = true;

    /**
     * Cria o gerador de fuel
     *
     * @param maxFuel máximo número de fuels a criar
     * @param min     tempo mínimo entre criação de fuels
     * @param max     tempo máximo entre criação de fuels
     * @param img     imagem de cada fuel
     * @param w       mundo onde os fuels irão ser colocados
     */
    public FuelGenerator(int maxFuel, int min, int max, ComponenteVisual img, Mundo w) {
        super(maxFuel, min, max - min, w);
        this.img = img;
    }

    /**
     * método qua cria os fuels quando for caso disso
     */
    @Override
    public void update() {
        // só pode criar se não houver fuel no mundo e se a nave estiver completa
        if (!podeCriarElemento() || !mundo.getMainSpaceship().isComplete() || !foiEntregue)
            return;

        // gerar aleatoriamente a coordenada x onde aparece o fuel
        int x = img.getComprimento()
                + ThreadLocalRandom.current().nextInt(mundo.getWidth() - 2 * img.getComprimento());

        // criar e adicionar o fuel ao mundo
        Fuel f = new Fuel(new Point(x, 0), img);
        mundo.adicionarElementoPegavel(f);
        foiEntregue = false;

        // incrementar o número de fuels já criados e reiniciar o temporizador
       super.update();
    }

    /**
     * retorna o número de fuels já criados
     *
     * @return o número de fuels já criados
     */
    @Override
    public int getNElementos() {
        return super.getNElementos() + (foiEntregue ? 0 : -1);
    }

    /**
     * indica ao gerador que o fuel foi entreguie na nave
     */
    public void fuelDelivered() {
        nextCreationTime = nextCreationTime();
        foiEntregue = true;
    }
}
