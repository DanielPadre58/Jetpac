package jetpac.generator;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ReguladorVelocidade;
import jetpac.drag.Fuel;
import jetpac.mundo.Mundo;

/**
 * Classe responsável pela criação do combustível para a nave. Para criar um
 * fuel não pode haver fuels no mundo. O tempo entre um fuel ser colocado na
 * nave e a criação do próximo é aleatório, mas existe um tempo mínimo e um
 * tempo máximo para esse tempo.
 * 
 */
public class FuelGenerator {

	private int nFuels; // número de fuels já criados
	private int maxFuel; // máximo de fuels a criar
	private ComponenteVisual img; // imagem de cada fuel
	private Mundo world; // mundo ao qual se adiciona fuel
	private int minTime; // tempo mínimo entre criação de fuels
	private int range; // tempo durante o qual podem ser criados fuels
	private long proxFuel; // temporizador de criação do próximo fuel
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
		this.nFuels = 0;
		this.maxFuel = maxFuel;
		this.img = img;
		world = w;
		minTime = min;
		range = max - min;
		proxFuel = nextFuelTime();
	}

	/**
	 * método qua cria os fuels quando for caso disso
	 */
	public void update() {
		// só pode criar se não houver fuel no mundo e se a nave estiver completa
		if (nFuels >= maxFuel || !world.getMainSpaceship().isComplete() || !foiEntregue)
			return;

		// quando o temporizador chega a zero é altura de criar fuel
		if (proxFuel <= ReguladorVelocidade.tempoRelativo()) {
			// gerar aleatoriamente a coordenada x onde aparece o fuel
			int x = img.getComprimento()
					+ ThreadLocalRandom.current().nextInt(world.getWidth() - 2 * img.getComprimento());

			// criar e adicionar o fuel ao mundo
			Fuel f = new Fuel(new Point(x, 0), img);
			world.addFuel(f);
			foiEntregue = false;

			// incrementar o número de fuels já criados e reiniciar o temporizador
			nFuels++;
			proxFuel = nextFuelTime();
		}
	}

	/**
	 * indica se ainda tem mais fuel para criar
	 * 
	 * @return true se ainda vai criar mais fuel
	 */
	public boolean hasMoreFuel() {
		return nFuels < maxFuel;
	}

	/**
	 * estabelece o tempo de criação do próximo fuel
	 * 
	 * @return o número de ciclos até crir o próximo
	 */
	private long nextFuelTime() {
		return minTime + ReguladorVelocidade.tempoRelativo() + ThreadLocalRandom.current().nextInt(range);
	}

	/**
	 * retorna o número máximo de fuels a criar
	 * 
	 * @return retorna o número máximo de fuels a criar
	 */
	public int getMaxFuel() {
		return maxFuel;
	}

	/**
	 * retorna o número de fuels já criados
	 * 
	 * @return o número de fuels já criados
	 */
	public int getNumFuels() {
		return nFuels + (foiEntregue ? 0 : -1);
	}

	/**
	 * indica ao gerador que o fuel foi entreguie na nave
	 */
	public void fuelDelivered() {
		proxFuel = nextFuelTime();
		foiEntregue = true;
	}
}
