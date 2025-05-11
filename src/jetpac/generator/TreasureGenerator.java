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
public class TreasureGenerator {

	private int minTime; // tempo mínimo entre criação de tesouros
	private int range; // tempo durante o qual podem ser criados tesouros
	private TreasureInfo[] tInfo; // informação sobre os tesouros que podem ser criados
	private Mundo world; // mundo onde se adicionam os tesouros
	private long proxTreasureCreation; // temporizador de criação de tesouros
	private int maxTreasures;
	private int currentTreasures = 0;

	/**
	 * cria o gerador de tesouros creates the treasure generator
	 * 
	 * @param min   tempo mínimo entre criação de tesouros
	 * @param max   tempo máximo entre criação de tesouros
	 * @param tInfo informação sobre os tesouros a serem criados
	 * @param w     mundo ao qual os tesouros serão adicionados
	 */
	public TreasureGenerator(int maxTreasures, int min, int max, TreasureInfo[] tInfo, Mundo w) {
		world = w;
		this.tInfo = tInfo;
		minTime = min;
		range = max - min;
		proxTreasureCreation = nextCreationTime();
		this.maxTreasures = maxTreasures;
	}

	/**
	 * método que trata da criação dos tesouros
	 */
	public void update() {
		// se o mundo tem tesouro não se pode criar
		if (currentTreasures >= maxTreasures)
			return;

		// se o temporizador já 0 chegou a altura de criar
		if (proxTreasureCreation <= ReguladorVelocidade.tempoRelativo()) {
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
									.nextInt(world.getWidth() - 2 * tInfo[i].getImg().getComprimento());
					t = tInfo[i].createTresure(new Point(x, 0));

					break;
				}
			}
			// adicionar o tesouro ao mundo e reiniciar o temporizador
			currentTreasures++;
			world.addTesouro(t);
			proxTreasureCreation = nextCreationTime();
		}
	}

	/**
	 * estabelece o tempo de criação do próximo tesouro
	 * 
	 * @return o próximo tempo de criação
	 */
	private long nextCreationTime() {
		return ReguladorVelocidade.tempoRelativo() + minTime + ThreadLocalRandom.current().nextInt(range);
	}

	public void treasureRemoved() {
		currentTreasures--;
	}
}
