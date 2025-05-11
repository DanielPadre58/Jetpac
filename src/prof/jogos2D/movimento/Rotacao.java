package prof.jogos2D.movimento;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import prof.jogos2D.util.Vector2D;

/** Movimento de rotação em torno de um ponto 
 * @author F. Sérgio Barbosa
 */
public class Rotacao implements Movimento {

	private Point2D.Double atual;     // ponto atual do movimento
	private boolean estaFim = false;  // está no fim?
	// ângulos do movimento: atual, inicial, final,  
	private double angulo, angIni, angFim;
	private double rotacao;	// tipo de rotação (ponteiros do relógio ou contrário)
	private double raio;    // raio da rotação
	
	/** Cria uma rotação
	 * 
	 * @param angInicial	ângulop inicial
	 * @param angFinal	ângulo final
	 * @param raio	raio de rotação
	 * @param sentidoRelog	rotação no sentido dos ponteiros do relógio ou contrário
	 */
	public Rotacao( double angInicial, double angFinal, double raio, boolean sentidoRelog ) {
		angulo = angInicial;
		angIni = angInicial;
		angFim = angFinal;
		rotacao = sentidoRelog? -1: 1;
		this.raio = raio;
	}

	@Override
	public void startMovimento(Double p) {
		atual = p;
		angulo = angIni;
		estaFim = false;
	}
	
	@Override
	public void move( double vel ) {
		if( estaFim( ) )
			return;
		
		double angvel = vel / raio;
		double nextAng = angvel*rotacao + angulo;

		
		if( nextAng > angFim && angulo < angFim ||
			nextAng < angFim && angulo > angFim ) {
			angulo = angFim;
			estaFim = true;
		}
		else {
			angulo = nextAng;
		}
	}
	
	@Override
	public Double getNextPoint(double vel) {
		return (Double)atual.clone();
	}
	
	@Override
	public boolean estaFim() {
		return estaFim;
	}
	
	@Override
	public Vector2D getDirecao() {
		return new Vector2D( Math.cos(angulo), Math.sin(angulo) );
	}
	
	@Override
	public double getAngulo() {
		return angulo;
	}
	
	@Override
	public Double getPosicao() {
		return atual;
	}
	
	@Override
	public Movimento clone() {
		return new Rotacao( angIni, angFim, raio, rotacao==-1);
	}
}
