package prof.jogos2D.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Class utilitária que representa um vetor a duas dimensões
 * @author F. Sérgio Barbosa
 *
 */
public class Vector2D {

	public double x, y;  // valores de x e y
		
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D( Point2D.Double p1, Point2D.Double p2 ){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}
	
	public Vector2D( Point p1, Point p2 ){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}
	
	public void normalizar(){
		double comp = getComprimento();
		if( comp != 0){
			x /= comp;
			y /= comp;
		}
	}
	
	public double getComprimento(){
		return Math.hypot( x, y );
	}
	
	public double getComprimentoSqr(){
		return x*x + y*y;
	}
	
	/** devolve o ângulo que este vetor faz com a horizontal. O valor é entre 0 e 2*Math.PI
	 * @return  o ângulo que este vetor faz com a horizontal
	 */
	public double getAngulo(){
		double ang = Math.atan2(y, x);
		return ang < 0? 2*Math.PI + ang: ang;
	}
	
	public double produtoInterno( Vector2D v ){
		return x*v.x + y*v.y;
	}
	
	public String toString(){
		return "("+x + ", " + y + ")";
	}
	
	public Vector2D clone() {
		return new Vector2D( x, y );
	}

	public void soma(Vector2D v2) {
		x += v2.x;
		y += v2.y;
	}
	
	public Vector2D getSoma(Vector2D v2) {
		return new Vector2D( x + v2.x, y + v2.y );
	}

	public Vector2D getOrtogonalEsquerda() {
		return new Vector2D( -y, x );
	}

	public Vector2D getOrtogonalDireita() {
		return new Vector2D( y, -x );
	}

	/** aplica este vetor a um ponto, transformando-o noutro
	 * @param pt o ponto de origem do vetor
	 * @return o ponto após a aplicação do vetor
	 */
	public Double aplicaPonto(Double pt) {
		return new Point2D.Double( pt.x+x, pt.y+y);
	}
	
	/** altera um ponto aplicando-lhe um deslocamento dado pelo vetor
	 * @param p ponto a deslocar
	 */
	public void deslocaPonto( Point2D.Double p ) {
		p.x += x;
		p.y += y;
	}

	public void rodar(double ang) {
		double vx = x * Math.cos( ang ) - y * Math.sin( ang );
		double vy = x * Math.sin( ang ) + y * Math.cos( ang );
		x = vx;
		y = vy;
	}

	public void setAngulo(double ang) {
		double modulo = getComprimento();
		x = modulo * Math.cos( ang );
		y = modulo * Math.sin( ang );
	}

	/** multiplica o vetor por um escalar
	 * @param escalar valor a multiplicar pelo vetor 
	 */
	public void escalar(double escalar) {
		x *= escalar;
		y *= escalar;		
	}
}
