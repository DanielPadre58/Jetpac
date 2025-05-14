package jetpac.enemy;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.astro.Direcao;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;
import prof.jogos2D.util.Vector2D;
import jetpac.astro.Astronauta;
import jetpac.astro.Plataforma;
import jetpac.mundo.Mundo;

/**
 * classe que representa um inimigo
 */
public class Inimigo {

	// constantes para identificar as animações
	private static final int ANIM_LEFT = 0;
	private static final int ANIM_RIGHT = 1;
	private static final int ANIM_DIE = 2;

	private boolean dead = false; // está morto?
	private int velX, velY; // velocidade do inimigo em X e em Y
	private int score; // pontuação do inimigo

	private Mundo world;
	private ComponenteMultiAnimado imagem;

	// constantes para identificar o tipo de inimigo
	public static final int LINEAR = 0;
	public static final int RICOCHETE = 1;
	public static final int PERSEGUIDOR = 2;
	public static final int SALTADOR = 3;

	private int tipo;

	private long changeCycle; // se for um perseguidor isto indica quanto deve "atacar" o astronauta

	private int amplitude; // se for um saltador, afeta a altura do salto
	private float fase, mudaFase; // se for um saltador indica em que fase do salto está

	/**
	 * cria um inimigo
	 * 
	 * @param p     posição inicial
	 * @param vel   velocidade inicial
	 * @param score pontuação
	 * @param dir   direção en que está virado
	 * @param img   imagems
	 */
	public Inimigo(int tipo, Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
		this.tipo = tipo;

		imagem = (ComponenteMultiAnimado) img.clone();

		// ver qual animação a usar
		if (dir == Direcao.LEFT) {
			imagem.setAnim(ANIM_LEFT);
			velX = -vel;
		} else {
			imagem.setAnim(ANIM_RIGHT);
			velX = vel;
		}
		imagem.setPosicao(p);
		this.score = score;

		if (tipo == LINEAR || tipo == RICOCHETE) {
			// a velocidade em Y é aleatória
			velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
		} else if (tipo == PERSEGUIDOR) {
			// a velocidade em Y é aleatória
			velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
			changeCycle = proximaMudanca();
		} else if (tipo == SALTADOR) {
			velX = velX / 2;
			amplitude = ThreadLocalRandom.current().nextInt(6, 9);
			fase = ThreadLocalRandom.current().nextInt(180);
			mudaFase = ThreadLocalRandom.current().nextFloat(0.3f, 0.7f);
		}
	}

	/**
	 * atualiza o inimigo updates the enemy
	 */
	public void update() {
		// se está morto não faz nada
		if (dead)
			return;

		desloca();
		checkPlataformas();
		checkAstronauta();
	}

	public Mundo getWorld() {
		return world;
	}

	public void setWorld(Mundo world) {
		this.world = world;
	}

	public void draw(Graphics2D g) {
		getImagem().desenhar(g);
	}

	public ComponenteMultiAnimado getImagem() {
		return imagem;
	}

	// ver se bate no astronauta
	// check if it hits the astronaut
	protected void checkAstronauta() {
		Rectangle ra = getWorld().getAstronaut().getBounds();
		if (ra.intersects(getImagem().getBounds())) {
			die();
			getWorld().getAstronaut().die();
		}
	}

	protected void checkPlataformas() {
		// ver se bate nas plataformas
		// check if it hits a platform
		for (Plataforma f : getWorld().getPlatforms()) {
			Vector2D toque = f.hitV(getImagem().getBounds());
			if (toque.x == 0 && toque.y < 0)
				hitsPlatformTop(f);
			else if (toque.x == 0 && toque.y > 0)
				hitsPlatformBottom(f);
			if (toque.x < 0 && toque.y == 0)
				hitsPlatformLeft(f);
			if (toque.x > 0 && toque.y == 0)
				hitsPlatformRight(f);
		}
	}

	private void hitsPlatformTop(Plataforma f) {
		switch (tipo) {
			case LINEAR:
				die();
				break;
			case RICOCHETE:
			case PERSEGUIDOR:
				setVelY(-getVelY());
				break;
			case SALTADOR:
				fase = 125;
				move(0, -getVelY());
				break;
		}
	}

	private void hitsPlatformBottom(Plataforma f) {
		switch (tipo) {
			case LINEAR:
				die();
				break;
			case RICOCHETE:
			case PERSEGUIDOR:
				setVelY(-getVelY());
				break;
			case SALTADOR:
				fase = 225;
				move(0, -getVelY());
				break;
		}
	}

	private void hitsPlatformLeft(Plataforma f) {
		switch (tipo) {
			case LINEAR:
				die();
				break;
			case RICOCHETE:
			case PERSEGUIDOR:
				setVelX(-getVelX());
				break;
			case SALTADOR:
				setVelX(-getVelX());
				break;
		}
	}

	private void hitsPlatformRight(Plataforma f) {
		switch (tipo) {
			case LINEAR:
				die();
				break;
			case RICOCHETE:
			case PERSEGUIDOR:
				setVelX(-getVelX());
				break;
			case SALTADOR:
				setVelX(-getVelX());
				break;
		}
	}

	protected void desloca() {
		if (tipo == LINEAR || tipo == RICOCHETE)
			move(velX, velY);
		else if (tipo == PERSEGUIDOR) {
			if (ReguladorVelocidade.tempoRelativo() < changeCycle) {
				move(velX, velY);
				return;
			}

			// está na altura de mudar de direção
			Astronauta astro = getWorld().getAstronaut();
			if (getImagem().getPosicao().x < astro.getPosition().x)
				setVelX(Math.abs(getVelX()));
			else
				setVelX(-Math.abs(getVelX()));
			if (getImagem().getPosicao().y < astro.getPosition().y)
				setVelY(Math.abs(getVelX() / 2));
			else
				setVelY(-Math.abs(getVelX() / 2));
			changeCycle = proximaMudanca();
		} else if (tipo == SALTADOR) {
			fase += mudaFase;
			if (fase > 225)
				fase = 225;
			double velY = amplitude * Math.sin(Math.toRadians(-fase));
			setVelY((int) velY);
		}
	}

	/**
	 * indica se o inimigo está morto. Só está morto se a animação de morte já
	 * acabou.
	 * 
	 * @return true, se está morto
	 */
	public boolean isDead() {
		return dead && getImagem().numCiclosFeitos() > 1;
	}

	/**
	 * indica se o inimigo está a morrer
	 * 
	 * @return true se estiver a morrer
	 */
	public boolean isDying() {
		return dead;
	}

	/**
	 * desloca o inimigo
	 * 
	 * @param dx distância a mover em x
	 * @param dy distância a mover em y
	 */
	public void move(int dx, int dy) {
		ComponenteMultiAnimado img = getImagem();

		int y = img.getPosicao().y;

		// se chegar a uma das extremidades passa para a outra
		if (img.getPosicao().x < 0 && dx < 0)
			img.setPosicao(new Point(getWorld().getWidth(), y));
		else if (img.getPosicao().x > getWorld().getWidth() && dx > 0)
			img.setPosicao(new Point(-img.getComprimento(), y));
		getImagem().getPosicao().translate(dx, dy);
	}

	/**
	 * mata o inimigo
	 */
	public void die() {
		if (dead)
			return;
		dead = true;

		// passar para a animação de morte
		ComponenteMultiAnimado img = getImagem();
		img.setAnim(ANIM_DIE);
		img.setFrameNum(0);
		img.setCiclico(false);
	}

	/**
	 * retorna a pontuação do inimigo
	 * 
	 * @return a pontuação do inimigo
	 */
	public int getScore() {
		return score;
	}

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		// ver se mudou de direção
		if (velX > 0 && this.velX < 0)
			imagem.setAnim(ANIM_RIGHT);
		else if (velX < 0 && this.velX > 0)
			imagem.setAnim(ANIM_LEFT);
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	/**
	 * Quando é um perseguidor este método calcula quando deve fazer a próxima
	 * mudança de movimento
	 * 
	 * @return o tempo da próxima mudança de movimento
	 */
	private long proximaMudanca() {
		return ReguladorVelocidade.tempoRelativo() + 3000 + ThreadLocalRandom.current().nextInt(5000);
	}
}
