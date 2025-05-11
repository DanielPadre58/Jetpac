package prof.jogos2D.movimento;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import prof.jogos2D.util.Vector2D;

/** Implementa um movimento em curva
 * 
 * @author F. Sérgio Barbosa
 */
public class Curva implements Movimento {

	// pontos iniciais, atuais e de destino
	private Point2D.Double ini, atual, dest; 
	private double raio;                // raio da curva
	private boolean estaFim = false;    // o movimento está no fim? 
	private double angulo, anguloIni;   // ângulos da curva, atual e inicial
	private double rotacao;             // sentido da rotação
	private Point2D.Double centro; 	    // posição do centro da curva

	/** Cria uma curva
	 * @param inicio ponto inical
	 * @param fim ponto final
	 * @param raio raio da curva
	 * @param sentidoRelog rotação no sentido dos ponteiros do relógio ou ao contrário?
	 */
	public Curva( Point2D.Double inicio, Point2D.Double fim, double raio, boolean sentidoRelog ) {
		this.dest = fim;
		atual = inicio;
		this.raio = raio;
		ini = (Point2D.Double)inicio.clone();
		rotacao = sentidoRelog? -1: 1;
		centro = calcularCentro(inicio, fim, raio);
		
		Vector2D dirIniOrto = (new Vector2D( centro, inicio ));		
		
		angulo = dirIniOrto.getAngulo();
		anguloIni = angulo;
	}
	
	/** Cria uma curva
	 * @param inicio	ponto inicial
	 * @param fim	ponto final
	 * @param centro	centro da curva
	 * @param sentidoRelog	rotação no sentido dos ponteiros do relógio ou ao contrário?
	 */
	public Curva( Point2D.Double inicio, Point2D.Double fim, Point2D.Double centro, boolean sentidoRelog ) {
		this.centro = centro;
		this.dest = fim;
		ini = (Point2D.Double)inicio.clone();
		rotacao = sentidoRelog? -1: 1;
		raio = centro.distance( ini );
		Vector2D dirIniOrto = (new Vector2D( centro, inicio ));		
		angulo = dirIniOrto.getAngulo();
		anguloIni = angulo;
	}

	// calcula o centro da curva
	private Point2D.Double calcularCentro(Point2D.Double inicio, Point2D.Double fim, double raio) {
		double raio2 = raio * raio;
		double dist = inicio.distance( fim );
		double delta = Math.sqrt(raio2 - ((dist / 2) * (dist / 2)));
		
	    double x3 = (inicio.x + fim.x) / 2;
        double centrox = x3 + rotacao*delta * ((inicio.y - fim.y) / dist);

        double y3 = (inicio.y + fim.y) / 2;
        double centroy = y3 - rotacao*delta * ((inicio.x - fim.x) / dist);
        
        return new Point2D.Double( centrox, centroy );
	}
	
	@Override
	public void startMovimento(Double p) {
		atual = p; 
		atual.x = ini.x;
		atual.y = ini.y;
		//direcaoAtual = direcao.clone();
		angulo = anguloIni;
		estaFim = false;
		
		System.out.println("curva com centro em " + centro);
	}

	@Override
	public void move( double vel ) {
		if( estaFim( ) )
			return;
		
		double angvel = vel / raio;
		
		double nextAng = angvel*rotacao + angulo;
		
		atual.x = centro.x + raio*Math.cos( nextAng );
		atual.y = centro.y + raio*Math.sin( nextAng );
		
		angulo = nextAng;
		
		if( estaFim( vel ) ) {
			estaFim = true;
			atual.x = dest.x;
			atual.y = dest.y;
		}
	}
	
	@Override
	public Double getNextPoint(double vel) {
		if( estaFim( ) )
			return (Double)atual.clone();
		
		double angvel = vel / raio;
		
		double nextAng = angvel*rotacao + angulo;
		
		Point2D.Double proximo = new Point2D.Double( centro.x + raio*Math.cos( nextAng ), centro.y + raio*Math.sin( nextAng ) );
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
		Vector2D dirAtualOrto = new Vector2D( centro, atual ); 
		return rotacao==1? dirAtualOrto.getOrtogonalEsquerda(): dirAtualOrto.getOrtogonalDireita();
	}
	
	@Override
	public double getAngulo() {
		return angulo + rotacao* Math.PI/2;
	}
	
	
	@Override
	public Double getPosicao() {
		return atual;
	}
	
	/** indica se a curva acaba quando se aplica a velociade vel
	 * @param vel velocidade do movimento 
	 * @return true se chegar ao fim
	 */
	private boolean estaFim( double vel) {
		return dest.distance( atual ) < vel*0.98;
	}
	
	@Override
	public Movimento clone() {
		return new Curva( (Point2D.Double)ini.clone(), (Point2D.Double)dest.clone(), (Point2D.Double)centro.clone(), rotacao == -1? true: false);
	}
}
