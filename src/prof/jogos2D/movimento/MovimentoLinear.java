package prof.jogos2D.movimento;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import prof.jogos2D.util.Vector2D;

/** Representa um movimento linear.
 * 
 * @author F. Sérgio Barbosa
 *
 */
public class MovimentoLinear implements Movimento {

	// pontos de destino, atual e inicial
	private Point2D.Double dest, atual, ini;
	private Vector2D direcao;         // direção do movimento
	private boolean estaFim = false;  // movimento acabou?
	
	/** Cria um  movimento linear
	 * @param p ponto de ínicio
	 * @param d ponto de destino
	 */
	public MovimentoLinear( Point2D.Double p, Point2D.Double d ) {
		atual = p;
		dest = d;
		direcao = new Vector2D( p, d );
		direcao.normalizar();
		ini = (Point2D.Double)p.clone();
	}

	@Override
	public void startMovimento(Double p) {
		atual = p;
		atual.x = ini.x;
		atual.y = ini.y;
		estaFim = false;
	}
	
	@Override
	public void move( double vel ) {
		if( estaFim( vel ) )
			return;
		atual.x += vel * direcao.x;
		atual.y += vel * direcao.y;
		if( estaFim( vel ) ) {
			atual.x = dest.x;
			atual.y = dest.y;
			estaFim = true;
		}
	}
	
	@Override
	public Double getNextPoint(double vel) {
		Point2D.Double proximo = (Double)atual.clone(); 
		if( estaFim( ) )
			return proximo;
		proximo.x += vel * direcao.x;
		proximo.y += vel * direcao.y;
		if( estaFim( vel ) ) {
			proximo.x = dest.x;
			proximo.y = dest.y;
		}
		
		return proximo;
	}
	

	
	@Override
	public boolean estaFim() {
		return estaFim;
	}
	
	@Override
	public Vector2D getDirecao() {
		return direcao;
	}
	
	@Override
	public double getAngulo() {
		return direcao.getAngulo();
	}
	
	@Override
	public Double getPosicao() {
		return atual;
	}

	/** indica se o movimento acaba quando se aplica a velociade vel
	 * @param vel velocidade do movimento 
	 * @return true se chegar ao fim
	 */
	private boolean estaFim( double vel ) {
		return dest.distance( atual ) < vel*0.98;
	}
	
	@Override
	public Movimento clone()  {
		return new MovimentoLinear( (Point2D.Double)ini.clone(), (Point2D.Double)dest.clone() );
	}
}
