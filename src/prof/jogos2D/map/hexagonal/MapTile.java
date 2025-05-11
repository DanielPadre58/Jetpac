package prof.jogos2D.map.hexagonal;

import java.awt.Graphics2D;
import java.awt.Point;


/** Representa uma célula, tile, de um mapa hexagonal
 * 
 * @author F. Sérgio Barbosa
 *
 */
public interface MapTile {

	/** Desenha plano de fundo da célula 
	 * @param g ambiente gráfico onde desenhar
	 * @param x coordenada x onde desenhar
	 * @param y coordenada y onde desenhar
	 */
	void drawBackground(Graphics2D g, int x, int y);
	
	/** Desenha o plano da frente da célula 
	 * @param g ambiente gráfico onde desenhar
	 * @param x coordenada x onde desenhar
	 * @param y coordenada y onde desenhar
	 */
	void drawForeground(Graphics2D g, int x, int y);
	
	/** define a posição da célula
	 * @param c a posição da célula, em coordenadas de mapa
	 */
	void setPosition(Point c);
	
	/** retorna a posição da célula
	 * @return a posição da célula, em coordenadas de mapa
	 */
	Point getPosition();
	
	/** associada esta célula a um dado mapa
	 * @param map mapa ao qual a célula ficará associada
	 */
	void setMap( MapaHexagonal map );
	
	/** retorna o mapa ao qual a célula pertence
	 * @return o mapa ao qual a célula pertence
	 */
	MapaHexagonal getMap();
}
