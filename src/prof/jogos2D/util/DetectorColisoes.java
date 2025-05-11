package prof.jogos2D.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Classe utilit�ria que faz v�rios testes para verificar colis�es e outras relacionadas com vetores
 * @author F. S�rgio Barbosa
  */
public class DetectorColisoes {
	
	public static boolean estaDireita( Point2D p, Point2D po, Vector2D v ) {
		double d = (p.getX()-po.getX())*v.y - (p.getY()-po.getY())*v.x;
		return d > 0;
	}
	
	public static boolean estaEsquerda( Point2D p, Point2D po, Vector2D v ) {
		double d = (p.getX()-po.getX())*v.y - (p.getY()-po.getY())*v.x;
		return d < 0;
	}
	
	/** Retorna o ponto de intersec��o entre duas linhas.
	 * Cada linha tem um ponto e um vetor (uniformizado).
	 * @param p1 ponto da linha 1
	 * @param v1 vetor da linha 1
	 * @param p2 ponto da linha 2
	 * @param v2 vetor da linha 2
	 * @return ponto de intersec��o
	 */
	public static Point2D.Double getIntersecao( Point2D p1, Vector2D v1, Point2D p2, Vector2D v2 ) {
		double div = v2.x*v1.y - v2.y * v1.x;
		if( div +0.000000001 > 0 && div +0.000000001 < 0)
			return null;
		
		double num = p2.getY()*v1.x + p1.getX()*v1.y - v1.x*p1.getY() - p2.getX()*v1.y;
		
		double fact = num / div;
		
		return new Point2D.Double( p2.getX() + fact*v2.x, p2.getY() + fact * v2.y );
	}
	
	/** Retorna o ponto de intersec��o entre duas linhas.
	 * Cada linha tem um ponto e um vetor (uniformizado).
	 * @param p1 ponto da linha 1
	 * @param v1 vetor da linha 1
	 * @param p2 ponto da linha 2
	 * @param v2 vetor da linha 2
	 * @return ponto de intersec��o
	 */
	public static Point2D.Double getIntersecao( Point2D.Double p1, Vector2D v1, Point2D.Double p2, Vector2D v2 ) {
		double div = v2.x*v1.y - v2.y * v1.x;
		if( div +0.000000001 > 0 && div -0.000000001 < 0)
			return null;
		
		double num = p2.y*v1.x + p1.x*v1.y - v1.x*p1.y - p2.x*v1.y;
		
		double fact = num / div;
		
		return new Point2D.Double( p2.x + fact*v2.x, p2.y + fact * v2.y );
	}
	 
	public static int getDivisaoAngulo( Point p1, Point p2, int nDivisoes ){	 
		double angulo = getAngulo( p1,p2 );
		return getDivisaoAngulo( angulo, nDivisoes ); 
	}
	
	/** divide um �ngulo em v�rias divis�es, �til para fazer rota��es
	 * parciais at� se atingir a rota��o total
	 * 
	 * @param angulo �ngulo a subdividir
	 * @param nDivisoes n� de divis�es em que subdividir o �ngulo
	 * @return a subdivis�o do �ngulo e, graus
	 */
	public static int getDivisaoAngulo( double angulo, int nDivisoes ){
		// converter para graus pois d� melhores resultados
		double anguloGraus = ((int)Math.toDegrees( angulo )) % 360;
		if( anguloGraus < 0 ){			
			anguloGraus = 360 + anguloGraus;
		}
				
		double divAng = 360.0 / nDivisoes;
		
		double gama = divAng / 2;
		
		anguloGraus = anguloGraus + gama;
		
		int div = (int)(anguloGraus) / (int)divAng;
		return div;
		/*
		anguloGraus = anguloGraus - gama;
		
		// descobrir qual das divis�es � a certa
		int div = 0;
		double angRef = 0;
		while( !estaPertoAngulo( angRef, anguloGraus, gama) ){
			div++;
			angRef += divAng;
			if( angRef >= 360 )
				angRef -= 360;
		}
		if( div >= nDivisoes ) div = 0;
		
		System.out.println( "   old div = " + div );
		return div;
		*/
				
	}
	
	public static boolean estaPertoAngulo( double ref, double angulo, double erro ){
		// o zero � um caso especial
		if( ref == 0 )
			return angulo < erro || angulo > 360-erro;
		return ref - erro < angulo && ref + erro > angulo;			
	}
	
	
	/**
	 * devolve o �ngulo do vector formado por dois pontos. 
	 * @param p1 1� ponto
	 * @param p2 2� ponto
	 * @return �ngulo de ambate
	 */
	public static double getAngulo( Point p1, Point p2 ){
		double distancia = p1.distance( p2 );
		double dx = p1.x - p2.x;
		double angulo = Math.acos( dx / distancia );
		// se estiver no quadrante de baixo � preciso actualizar o angulo
		if( p1.y < p2.y )
			angulo = 2*Math.PI - angulo;						
		return angulo;
	}
	
	/**
	 * indica se 2 circunfer�ncias se intersectam
	 * @param p1 centro da 1� circunfer�ncia
	 * @param raio1 raio da 1� circunfer�ncia
	 * @param p2 centro da 2� circunfer�ncia
	 * @param raio2 raio da 2� circunfer�ncia
	 * @return se bateram
	 */
	public static boolean intersectam( Point2D p1, double raio1, Point2D p2, double raio2 ){
		// para terem batido a dist�ncia entre as circunfer�ncias tem de ser
		// menor que a soma dos seus raios
		double somaRaios = raio1 + raio2;
		double distancia = p1.distance( p2 );
		return distancia < somaRaios;		
	}
	
	/** indica se uma linha e uma circunfer�ncia se intersetam
	 * @param p1 origem da linha
	 * @param p2 fim da linha
	 * @param c centro da circunfer�ncia
	 * @param raio raio da circunfer�ncia
	 * @return true, se se intersetarem
	 */
	public static boolean intersetam( Point2D p1, Point2D p2, Point2D c, double raio ) {
		double dist = distanceToSegment( p1, p2, c );
		return dist <= raio+0.00000001;
	}
	
    /**
     * Returns the distance of p3 to the segment defined by p1,p2;
     * 
     * @param p1
     *                First point of the segment
     * @param p2
     *                Second point of the segment
     * @param p3
     *                Point to which we want to know the distance of the segment
     *                defined by p1,p2
     * @return The distance of p3 to the segment defined by p1,p2
     */
    public static double distanceToSegment(Point2D p1, Point2D p2, Point2D p3) {
		final Point2D closestPoint = pontoMaisProximo(p1, p2, p3);
		return closestPoint.distance(p3);
    }

    /** Para o segmento de reta, P1-P2, o m�todo retorna o ponto do segmento que esteja mais pr�ximo do ponto P3.
     * 
     * @param p1 inicio do segmento de reta
     * @param p2 fim do segmento de reta
     * @param p3 ponto fora da reta
     * @return o ponto do segmento que esteja mais pr�ximo de p3
     */
	public static Point2D pontoMaisProximo(Point2D p1, Point2D p2, Point2D p3) {
		final double xDelta = p2.getX() - p1.getX();
		final double yDelta = p2.getY() - p1.getY();
		
		if ((xDelta == 0) && (yDelta == 0)) {
		    throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}
		
		final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
		
		final Point2D closestPoint;
		if (u < 0) {
		    closestPoint = p1;
		} else if (u > 1) {
		    closestPoint = p2;
		} else {
		    closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
		}
		return closestPoint;
	}
	
	/** Retorna o ponto de intersec��o entre duas linhas.
	 * Cada linha tem um ponto e um vetor (uniformizado).
	 * Assume que as linhas se interseptam
	 * 
	 * @param p1 ponto da linha 1
	 * @param v1 vetor da linha 1
	 * @param p2 ponto da linha 2
	 * @param v2 vetor da linha 2
	 * @return ponto de intersec��o
	 */
	public static Point2D.Double intersecao( Point2D p1, Vector2D v1, Point2D p2, Vector2D v2 ) {
		// caso especial das linhas verticais
		double v1x = v1.x == 0? 0.000000001: v1.x;
		double v2x = v2.x == 0? 0.000000001: v2.x;
		
		// por as linhas nas f�rmulas lineares
		// linha1 = a.x + c, linha 2 = b.x + d
		double a = v1.y / v1x;
		double c = -a * p1.getX() + p1.getY();
		
		double b = v2.y / v2x;
		double d = -b * p2.getX() + p2.getY();

		return interseccao(a, c, b, d);
	}

	/** retorna o ponto de interseccao entre duas linhas.
	 * Cada linha � representada pela sua equa��o linear. Linha 1 = a*x + c, Linha 2 = b*x + d
	 * @param a declive da linha 1
	 * @param c origem da linha 1
	 * @param b declive da linha 2
	 * @param d origem da linha 2
	 * @return ponto de intersep��o
	 */
	public static Point2D.Double interseccao(double a, double c, double b, double d) {
		double x = (d - c) / (a - b);
		double y = (a*d - b * c) / ( a - b);
		return new Point2D.Double( x, y );
	}
	
	/** Retorna o ponto de intersec��o entre duas linhas.
	 * Cada linah � representada por dois pontos
	 * 
	 * @param p1i ponto inical da linha 1
	 * @param p1f ponto final da linha 1
	 * @param p2i ponto inical da linha 2
	 * @param p2f ponto final da linha 2
	 * @return ponto de intersep��o
	 */
	public static Point2D interseccao(Point2D p1i, Point2D p1f, Point2D p2i, Point2D p2f) {
		double a = (p1f.getX() - p1i.getX()) / (p1f.getY() - p1i.getY() );
		double c = 1; // TODOS calcular este coeficiente
		
		double b = (p1f.getX() - p1i.getX()) / (p1f.getY() - p1i.getY() );
		double d = 1;  // TODOS calcular este coeficiente
		
		return interseccao( a, c, b, d);
	}
	
	
	/**
	 * indica se um rect�ngulo e uma circunfer�ncia se intersectam
	 * @param r
	 * @param c
	 * @param raio
	 * @return
	 */
	public static boolean intersectam( Rectangle r, Point c, int raio  ){
		// ver se o ponto esquerdo est� para l� do circulo
		int dx1 = r.x - c.x;
		if( dx1 > raio )
			return false;
		
		// ver se o ponto direito est� para c� do circulo
		int dx2 = r.x+r.width - c.x;
		if( dx2 < -raio)
			return false;
		
		// ver se o ponto inferior est� para cima do circulo		
		int dy1 = r.y - c.y;
		if( dy1 > raio )
			return false;
		
		// ver se o ponto superior est� para baixo do circulo		
		int dy2 = r.y +r.height - c.y;
		if( dy2 < -raio )
			return false;
		
		// ok, sabemos que o rectangulo r est� dentro do rectangulo que delimita o circulo
		// se ambos os pontos estiverem em quadrantes diferentes � se intersectam
		if( dx1 < 0 && dx2 > 0 )
			return true;
		if( dy1 < 0 && dy2 > 0 )
			return true;
		
		// ok, no m�ximo est� um ponto dentro do c�rculo (qual?)
		Point teste;
		// ver se � um ponto dolado esquerdo
		if( dx1 > 0 ){ 
			if( dy1 > 0 )
				teste = r.getLocation();
			else
				teste = new Point( r.x, r.y+r.height);
		}
		else {
			if( dy1 > 0 )
				teste = new Point( r.x+r.width, r.y);
			else
				teste = new Point( r.x+r.width, r.y+r.height);			
		}
		
		// est� dentro se a dist�ncia entre esse ponto e o centro for menor que o raio
		return c.distanceSq( teste ) < raio*raio;
	}
	
}
