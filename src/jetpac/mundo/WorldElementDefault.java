package jetpac.mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que define o comportamento normal de um elemento do mundo.
 */
public abstract class WorldElementDefault implements WorldElement {

	// a imagem do elemento do mundo
	private ComponenteVisual image;

	// o mundo em que o elemento se move
	private Mundo world;

	/**
	 * construtor do elemento, que define a imagem do elemento
	 * 
	 * @param image a imagem
	 */
	public WorldElementDefault(ComponenteVisual image) {
		this.image = image;
	}

	@Override
	public Rectangle getBounds() {
		return image.getBounds();
	}

	@Override
	public ComponenteVisual getImage() {
		return image;
	}

	@Override
	public Point getPosition() {
		return image.getPosicao();
	}

	@Override
	public Mundo getWorld() {
		return world;
	}

	@Override
	public void setImage(ComponenteVisual img) {
		this.image = img;
	}

	@Override
	public void setPosition(Point pos) {
		image.setPosicao(pos);
	}

	@Override
	public void setWorld(Mundo w) {
		world = w;
	}

	@Override
	public void draw(Graphics2D g) {
		image.desenhar(g);
	}

	@Override
	public void move(int dx, int dy) {
		image.getPosicao().translate(dx, dy);
	}
}
