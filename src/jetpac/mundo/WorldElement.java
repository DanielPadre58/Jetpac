package jetpac.mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Esta interface define um elemento que está no mundo.
 */
public interface WorldElement {

	/**
	 * define o mundo em que vai estar metido o elemento
	 * 
	 * @param w o mundo onde o elemento vai ficar
	 */
	public void setWorld(Mundo w);

	/**
	 * retorna o mundo onde está o elemento
	 * 
	 * @return o mundo onde está o elemento
	 */
	public Mundo getWorld();

	/**
	 * devolve a imagem do elemento
	 * 
	 * @return a imagem do elemento
	 */
	public ComponenteVisual getImage();

	/**
	 * define a imagem do elemento
	 * 
	 * @param img a imagem do elemento
	 */
	public void setImage(ComponenteVisual img);

	/**
	 * retorna a posição do elemento
	 * 
	 * @return a posição do elemento
	 */
	public Point getPosition();

	/**
	 * define a posição do elemento
	 * 
	 * @param pos a posição do elemento
	 */
	public void setPosition(Point pos);

	/**
	 * retorna o rectângulo envolvente do elemento. Útil para detetar as
	 * colisões.
	 * 
	 * @return o rectângulo envolvente do elemento
	 */
	public Rectangle getBounds();

	/**
	 * desloca o elemento.
	 * 
	 * @param dx deslocamento em x
	 * @param dy deslocamento em y
	 */
	public void move(int dx, int dy);

	/**
	 * desenha o elemento.
	 * 
	 * @param g onde desenhar
	 */
	public void draw(Graphics2D g);

	/**
	 * atualiza o elemento. Método chamado a cada ciclo de processamento.
	 */
	public void update();
}
