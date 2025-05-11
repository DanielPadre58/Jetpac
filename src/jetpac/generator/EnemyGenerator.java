package jetpac.generator;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.astro.Astronauta;
import jetpac.enemy.*;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;

/**
 * Esta classe é responsável por gerar os inimigos de cada nível.
 * 
 */
public class EnemyGenerator {

	private static final int creationCicle = 800; // o ciclo de criação é de 800 milisegundos

	private String type; // o tipo de inimigo a criar
	private int maxEnemys; // número máximo de inimigos que podem existir simultaneamente
	private int enemyVel; // a velocidade de cada inimigo
	private ComponenteMultiAnimado img; // a imagem que representa o inimigo
	private Mundo world; // o mundo ao qual serão adicionados os inimigos
	private long nextCreation; // indica quando se voltam a criar inimigos
	private int enemyScore; // a pontuação de cada inimigo

	/**
	 * Cria um gerador de inimigos
	 * 
	 * @param type       tipo dos inimigos a criar
	 * @param maxEnemys  número máximo de inimigos simultâneos
	 * @param enemyVel   velocidade dos inimigos
	 * @param enemyScore pontuação de cada inimigos
	 * @param img        imagem do inimigo
	 * @param world      mundo onde colocar os inimigos
	 */
	public EnemyGenerator(String type, int maxEnemys, int enemyVel, int enemyScore, ComponenteMultiAnimado img,
			Mundo world) {
		this.maxEnemys = maxEnemys;
		this.enemyVel = enemyVel;
		this.img = img;
		this.world = world;
		this.enemyScore = enemyScore;
		this.type = type;
		nextCreation = creationCicle + ReguladorVelocidade.tempoRelativo();
	}

	/**
	 * Método que trata da criação dos inimigos, se for altura de os criar
	 */
	public void update() {
		// verificar se é preciso criar inimigos
		if (world.getNumEnemies() >= maxEnemys)
			return;

		// verificar se já é tempo de os criar
		if (nextCreation <= ReguladorVelocidade.tempoRelativo()) {
			// criar sempre metade dos inimigos mais 1, para não criar todos de uma vez
			for (int i = 0; i <= (maxEnemys - world.getNumEnemies()) / 2; i++) {
				// escolher aleatoriamente a coordenada y onde vai aparecer o inimigo
				int y = ThreadLocalRandom.current().nextInt(world.getHeight() - 2 * img.getAltura());

				// escolher se aparece do lado esquerdo ou direito (1= direito, 0 = esquerdo)
				int r = ThreadLocalRandom.current().nextInt(2);
				int dir;
				Point pos;
				if (r == 0) {
					dir = Astronauta.RIGHT; // move-se para a direita
					pos = new Point(5 - img.getComprimento(), y); // aparece do lado esquerdo
				} else {
					dir = Astronauta.LEFT;
					pos = new Point(world.getWidth() - 5, y);
				}

				// verificar qual o tipo de inimigo a criar
				Inimigo e;
				if (type.equals("linear"))
					e = new Inimigo(Inimigo.LINEAR, pos, enemyVel, enemyScore, dir, img);
				else if (type.equals("ricochete"))
					e = new Inimigo(Inimigo.RICOCHETE, pos, enemyVel, enemyScore, dir, img);
				else if (type.equals("perseguidor"))
					e = new Inimigo(Inimigo.PERSEGUIDOR, pos, enemyVel, enemyScore, dir, img);
				else if (type.equals("saltador"))
					e = new Inimigo(Inimigo.SALTADOR, pos, enemyVel, enemyScore, dir, img);
				else // por defeito assume que é linear
					e = new Inimigo(Inimigo.LINEAR, pos, enemyVel, enemyScore, dir, img);
				// adicionar o inimigo ao mundo
				world.addEnemy(e);

				// reiniciar o contador de criação
				nextCreation = creationCicle + ReguladorVelocidade.tempoRelativo();
			}
		}
	}
}
