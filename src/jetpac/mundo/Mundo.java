package jetpac.mundo;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jetpac.astro.*;
import jetpac.drag.*;
import jetpac.enemy.*;
import jetpac.generator.*;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Esta classe é responsável por manter toda a informação acerca do mundo
 */
public class Mundo {

	private ComponenteVisual background; // imagem de fundo do nível

	private Astronauta astronauta; // o astronauta

	// Os vários elementos presentes no jogo
	private Spaceship ship;
	private ArrayList<Plataforma> platforms = new ArrayList<>();
	private ArrayList<Laser> lasers = new ArrayList<Laser>();
	private ArrayList<Inimigo> enemies = new ArrayList<Inimigo>();

	// TODO DONE usar apenas uma lista
	private ArrayList<DraggableElement> elementosPegaveis = new ArrayList<>();

	// os vários geradores de elementos
	private TreasureGenerator treasureGen;
	private FuelGenerator fuelGen;
	private EnemyGenerator enemyGen;

	// dimensões do mundo
	private int width;
	private int height;

	// mundo completo?
	private boolean completed;

	// pontuação em cada ciclo
	private int ciclePoints;

	// os estados possíveis para o mundo
	private static enum EstadoMundo {
		STARTING, PLAYING,
		ENDING, // acabar por morte
		COMPLETING // acabar por vitória
	}

	// estado atual
	private EstadoMundo state = EstadoMundo.STARTING;

	/**
	 * construtor do mundo
	 */
	public Mundo() {
		background = null;
	}

	/**
	 * construtor do mundo
	 * 
	 * @param img imagem de fundo do mundo, background image
	 */
	public Mundo(ComponenteVisual img) {
		background = img;
	}

	/**
	 * começar a jogar
	 */
	public void play() {
		state = EstadoMundo.STARTING;
		astronauta.reset();
	}

	/**
	 * começa a jogar o mundo
	 */
	private void start() {
		state = EstadoMundo.PLAYING;
	}

	/**
	 * vai desenhar todos os elementos do mundo
	 * 
	 * @param g onde vai desenhar
	 */
	public synchronized void draw(Graphics2D g) {
		if (background != null)
			background.desenhar(g);

		// se está a completar o astronauta não se desenha
		if (state != EstadoMundo.COMPLETING)
			astronauta.draw(g);

		for (Plataforma p : platforms)
			p.draw(g);

		for (Laser d : lasers)
			d.draw(g);

		for (Inimigo e : enemies)
			e.draw(g);

		// TODO DONE tentar usar apenas um for
		for (DraggableElement draggable : elementosPegaveis)
			draggable.draw(g);

		ship.draw(g);
	}

	/**
	 * Actualiza todos os elementos do mundo e remove os elementos que já não são
	 * necessários.
	 */
	public synchronized int update() {
		// reiniciar a pontuação do ciclo
		ciclePoints = 0;

		if (state == EstadoMundo.STARTING) {
			prepararCenario();
			return 0;
		}

		if (state == EstadoMundo.COMPLETING) {
			updateElementos();
			// se está a completar apenas faz subir a nave
			return subirNave();
		}

		if (state == EstadoMundo.PLAYING) {
			updateElementos();

			// se está a jogar precisa de usar os geradores
			treasureGen.update();
			fuelGen.update();
			enemyGen.update();

			astronauta.update();

			for (Laser d : lasers)
				d.update();
			return ciclePoints;
		}
		if (state == EstadoMundo.ENDING) {
			updateElementos();
			return 0;
		}
		return 0;
	}

	private void updateElementos() {
		ship.update();

		for (Plataforma f : platforms)
			f.update();

		for(Inimigo inimigo : enemies)
			inimigo.update();

		// TODO DONE tentar usar apenas um for
		for (DraggableElement draggable : elementosPegaveis)
			draggable.update();

		// retirar os disparos que já não estão ativos
		for (int i = lasers.size() - 1; i >= 0; i--) {
			if (lasers.get(i).isDead())
				lasers.remove(i);
		}

		// retirar os inimigos que já não estão ativos
		for (int i = enemies.size() - 1; i >= 0; i--) {
			if (enemies.get(i).isDead())
				enemies.remove(i);
		}

		// retirar os arrastáveis que já não estão ativos
		// TODO DONE tentar usar apenas um for
		for(DraggableElement draggable : elementosPegaveis)
			if(!draggable.isActive())
				elementosPegaveis.remove(draggable);
	}

	private void prepararCenario() {
		// não pode começar enquanto houverem coisas a cair
		boolean start = true;
		// TODO DONE tentar usar apenas um for
		for (DraggableElement draggable : elementosPegaveis)
			if (draggable.isFalling()) {
				start = false;
				break;
			}

		if (ship.isFalling())
			start = false;

		if (start)
			start();

		ship.update();
		// TODO DONE tentar usar apenas um for
		for (DraggableElement draggable : elementosPegaveis)
			draggable.update();
	}

	private int subirNave() {
		ship.move(0, -3);
		if (ship.getPosition().y <= 0) {
			completed = true;
			return -1;
		}
		return 0;
	}

	/**
	 * adicionar à pontuação
	 * 
	 * @param p pontos a adicionar
	 */
	public void addCiclePoints(int p) {
		ciclePoints += p;
	}

	/**
	 * retorna a imagem de fundo
	 * 
	 * @return a imagem de fundo
	 */
	public ComponenteVisual getBackground() {
		return background;
	}

	/**
	 * define a imagem de fundo
	 * 
	 * @param fundo a nova imagem
	 */
	public void setBackground(ComponenteVisual fundo) {
		this.background = fundo;
	}

	/**
	 * retorna o astronauta
	 * 
	 * @return o astronauta
	 */
	public Astronauta getAstronaut() {
		return astronauta;
	}

	/**
	 * define o astronauta
	 * 
	 * @param astronauta o astronauta do jogo
	 */
	public void setAstronauta(Astronauta astronauta) {
		this.astronauta = astronauta;
		astronauta.setWorld(this);
	}

	public void adicionarElementoPegavel(DraggableElement d) {
		elementosPegaveis.add(d);
		d.setWorld(this);
	}


	public List<Fuel> getFuel() {
		List<Fuel> fuels = new ArrayList<>();
		for (DraggableElement d : elementosPegaveis)
			if (d instanceof Fuel)
				fuels.add((Fuel) d);

		return Collections.unmodifiableList(fuels);
	}

	public List<Tesouro> getTesouros() {
		List<Tesouro> tesouros = new ArrayList<>();
		for (DraggableElement d : elementosPegaveis)
			if (d instanceof Tesouro)
				tesouros.add((Tesouro) d);

		return Collections.unmodifiableList(tesouros);
	}

	public List<SpaceshipPart> getSpaceshipParts() {
		List<SpaceshipPart> partes = new ArrayList<>();
		for (DraggableElement d : elementosPegaveis)
			if (d instanceof SpaceshipPart)
				partes.add((SpaceshipPart) d);

		return Collections.unmodifiableList(partes);
	}

	public void addSpaceship(Spaceship s) {
		ship = s;
		s.setWorld(this);
	}

	/**
	 * retorna a nave principal
	 * 
	 * @return a nave principal
	 */
	public Spaceship getMainSpaceship() {
		return ship;
	}

	/**
	 * adiciona uma plataforma
	 * 
	 * @param p a plataforma a adicionar
	 */
	public void addPlatform(Plataforma p) {
		platforms.add(p);
		p.setWorld(this);
	}

	/**
	 * retorna as plataformas
	 * 
	 * @return as plataformas
	 */
	public List<Plataforma> getPlatforms() {
		return Collections.unmodifiableList(platforms);
	}

	/**
	 * adiciona um laser
	 * 
	 * @param l o laser a adicionar
	 */
	public void addLaser(Laser l) {
		lasers.add(l);
		l.setWorld(this);
	}

	/**
	 * Define o gerador de tesouros
	 * 
	 * @param tg o gerador de tesouros
	 */
	public void setTreasureGenerator(TreasureGenerator tg) {
		treasureGen = tg;
	}

	/**
	 * retorna o gerador de tesouros
	 * 
	 * @return o gerador de tesouros
	 */
	public TreasureGenerator getTreasureGenerator() {
		return treasureGen;
	}

	/**
	 * define o gerador de fuel
	 * 
	 * @param fg gerador de fuel
	 */
	public void setFuelGen(FuelGenerator fg) {
		fuelGen = fg;
	}

	/**
	 * Retorna o gerador de fuel
	 * 
	 * @return o gerador de fuel
	 */
	public FuelGenerator getFuelGen() {
		return fuelGen;
	}

	/**
	 * retorna o gerador de inimigos
	 * 
	 * @return o gerador de inimigos
	 */
	public EnemyGenerator getEnemyGen() {
		return enemyGen;
	}

	/**
	 * retorna a percentagem de fuel já entregue (0 a 100)
	 * 
	 * @return a percentagem de fuel
	 */
	public int getFuelPercentage() {
		return fuelGen.getNElementos() * 100 / fuelGen.getMaxElementos();
	}

	/**
	 * o mundo está completo
	 */
	public void completed() {
		state = EstadoMundo.COMPLETING;
		lasers.clear();
	}

	/**
	 * indica se já está completo
	 * 
	 * @return true, se já está completo
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * indica se já perdeu
	 * 
	 * @return se já perdeu
	 */
	public boolean isOver() {
		return astronauta.isDead();
	}

	/**
	 * o mundo está a acabar por morte
	 */
	public void dying() {
		state = EstadoMundo.ENDING;
		lasers.clear();
		enemies.clear();
	}

	/**
	 * define o gerador de inimigos
	 * 
	 * @param eg o novo gerador de inimigos
	 */
	public void setEnemyGen(EnemyGenerator eg) {
		enemyGen = eg;
	}

	/**
	 * adiciona um inimigo ao mundo
	 * 
	 * @param e o inimigo a adicionar
	 */
	public void addEnemy(Inimigo e) {
		enemies.add(e);
		e.setWorld(this);
	}

	/**
	 * retorna o número de inimigos
	 * 
	 * @return o número de inimigos
	 */
	public int getNumEnemies() {
		return enemies.size();
	}

	/**
	 * retorna os inimigos returns the enemies
	 * 
	 * @return os inimigos
	 */
	public List<Inimigo> getEnemies() {
		return Collections.unmodifiableList(enemies);
	}

	/**
	 * define as dimensões do mundo
	 * 
	 * @param width  comprimento do mundo
	 * @param height altura do mundo
	 */
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * devolve o comprimento
	 * 
	 * @return the world width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * devolve a altura
	 * 
	 * @return the world height
	 */
	public int getHeight() {
		return height;
	}
}
