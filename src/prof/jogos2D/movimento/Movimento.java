package prof.jogos2D.movimento;

import java.awt.geom.Point2D;

import prof.jogos2D.util.Vector2D;

/** Interface que representa um movimento. Um movimento vai
 * deslocar um ponto ao longo do seu trajeto
 * 
 * @author F. Sérgio BArbosa
 *
 */
public interface Movimento extends Cloneable {

	/** começar o movimento, deslocando o ponto p
	 * @param p ponto qeu vai sendo alterado pelo movimento
	 */
	void startMovimento( Point2D.Double p );
	
	/** move o ponto de acordo com a velocidade indicada
	 * @param vel velocidade a aplicar ao movimento
	 */
	void move( double vel );
	
	/** Indica qual o ponto onde o movimento irá estar ao aplicar-se a 
	 * velocidade indicada. Não move o ponto.
	 * @param vel velocidade a aplicar ao movimento
	 * @return o ponto onde iria ficar após o movimento, se este fosse aplicado
	 */
	Point2D.Double getNextPoint( double vel );
	
	/** Retorna a posição atual do movimento
	 * @return a posição atual do movimento
	 */
	Point2D.Double getPosicao();
	
	/** Indica a direção atual do movimento
	 * @return a direção atual do movimento
	 */
	Vector2D getDirecao();
	
	/** Indica se o movimento chegou ao fim
	 * @return true, se o movimento chegou ao fim
	 */
	boolean estaFim( );
	
	/** Indica qual o ângulo associado ao movimento 
	 * @return qual o ângulo associado ao movimento
	 */
	double getAngulo();
	
	/** retorna um clone deste movimento
	 * @return um clone deste movimento
	 */
	Movimento clone();
}