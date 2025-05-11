package jetpac.astro;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jetpac.drag.SpaceshipPart;
import jetpac.mundo.WorldElementDefault;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa a nave espacial.
 */
public class Spaceship extends WorldElementDefault {

	private int nParts; // número de partes da nave
	private int nextPartDue; // indica qual a próxima parte a ser precisa
	private Rectangle dropArea; // zona de descarga da nave
	private boolean falling = true; // está a cair?
	private ComponenteVisual partes[];

	/**
	 * Cria uma nave
	 * 
	 * @param nParts quantas partes constituem a nave
	 * @param img    a imagem da nave
	 */
	public Spaceship(int nParts, ComponenteVisual img) {
		super(img);
		this.nParts = nParts;
		nextPartDue = nParts > 0 ? 1 : 0;
		partes = new ComponenteVisual[nParts];
		partes[0] = img;
	}

	/**
	 * Indica se a nave está completa
	 * 
	 * @return true, se está completa
	 */
	public boolean isComplete() {
		return nParts == nextPartDue;
	}

	/**
	 * indica qual a próxima parte a ser precisa para a nave
	 * 
	 * @return o índice da próxima parte
	 */
	public int getNextPartDue() {
		return nextPartDue;
	}

	@Override
	public void draw(Graphics2D g) {
		// super.draw(g);
		for (int i = 0; i < nextPartDue; i++)
			partes[i].desenhar(g);
	}

	@Override
	public void move(int dx, int dy) {
		for (int i = 0; i < nextPartDue; i++)
			partes[i].getPosicao().translate(dx, dy);
	}

	/**
	 * devolve a zona de descarga da nave
	 * 
	 * @return a zona de descarga da nave
	 */
	public Rectangle getDropArea() {
		if (dropArea == null) {
			Rectangle r = getBounds();

			// a zona de descarga tem metade da largura da nave
			int width = r.width / 2;
			dropArea = new Rectangle(r.x + width / 2, 0, width, r.y + r.height);

			// ver se a drop area está coberta por alguma plataforma
			for (Plataforma p : getWorld().getPlatforms()) {
				Rectangle rp = p.getBounds();
				if (p.getBounds().intersects(dropArea))
					dropArea.y = rp.y + rp.height;
			}
		}
		return dropArea;
	}

	public void setFalling(boolean f) {
		falling = f;
	}

	/**
	 * indica se está a cair
	 * 
	 * @return true se está a cair
	 */
	public boolean isFalling() {
		return falling;
	}

	@Override
	public void update() {
		if (!isFalling())
			return;

		// cai e deteta se bateu nas plataformas
		move(0, 2);
		for (Plataforma p : getWorld().getPlatforms()) {
			Rectangle inter = p.getBounds().intersection(getBounds());
			if (!inter.isEmpty()) {
				move(0, -inter.height);
				falling = false;
			}
		}
	}

	/**
	 * adiciona uma parte à nave.
	 * 
	 * @param p a parte a adicionar
	 */
	public void addParte(SpaceshipPart p) {
		partes[nextPartDue] = p.getImagem();
		int y = partes[nextPartDue - 1].getPosicao().y - p.getImagem().getAltura();
		Point novaPos = new Point(p.getImagem().getPosicao().x, y);
		p.getImagem().setPosicao(novaPos);
		nextPartDue++;
	}
}
