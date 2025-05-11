package jetpac.drag;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jetpac.astro.Astronauta;
import jetpac.astro.Plataforma;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ReguladorVelocidade;
import prof.jogos2D.util.Vector2D;

/**
 * classe que prepresenta um tesouro que aparece no mundo. Os tesouros aumentam
 * a pontuação do jogador, mas, se não forem apanhados, desaparecem após algum
 * tempo
 *
 */
public class Tesouro extends DraggableElement{

	private long lifeTime; // tempo de vida
	private int score; // pontuação
	private long lifeLeft;

	/**
	 * Cria um tesouro
	 *
	 * @param p        posição onde vai aparecer
	 * @param lifeTime tempo de vida
	 * @param score    pontuação
	 * @param img      imagem
	 */
	public Tesouro(Point p, int lifeTime, int score, ComponenteVisual img) {
		this.imagem = img;
		imagem.setPosicao(p);
		this.lifeLeft = lifeTime;
		this.lifeTime = ReguladorVelocidade.tempoRelativo() + lifeTime;
		this.score = score;
	}

    @Override
	public boolean isActive() {
		return super.isActive() && lifeLeft > 0;
	}

    @Override
	public void update() {
		// ver se já passou o tempo de validade
		if (getEstado() != State.DRAGGED && getEstado() != State.DROPING) {
			if (lifeTime < ReguladorVelocidade.tempoRelativo()) {
				lifeLeft = 0;
				getWorld().getTreasureGenerator().treasureRemoved();
			}
		}

        super.update();
	}

    @Override
	public void setEstado(State estado) {
		super.setEstado(estado);
		if (estado == State.DRAGGED) {
			lifeLeft = lifeTime - ReguladorVelocidade.tempoRelativo();
		} else {
			lifeTime = lifeLeft + ReguladorVelocidade.tempoRelativo();
		}
	}

	/**
	 * devolve a pontuação do tesouro
	 *
	 * @return a pontuação do tesouro
	 */
	public int getScore() {
		return score;
	}

    @Override
	public void delivered() {
		super.delivered();
		lifeLeft = 0;
		getWorld().addCiclePoints(getScore());
		getWorld().getTreasureGenerator().treasureRemoved();
	}
}
