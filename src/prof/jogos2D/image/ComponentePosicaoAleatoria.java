package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Point;

public class ComponentePosicaoAleatoria extends ComponenteDecorador {
	
	private Point[] posicoes;
	private int lastCiclo = 0;
	
	public ComponentePosicaoAleatoria( ComponenteVisual cv, Point[] pts ) {
		super( cv );
		posicoes = pts;
	}

	@Override
	public void desenhar(Graphics g) {
		super.desenhar(g);
		if( !estaPausa() ) {
			if( numCiclosFeitos() > lastCiclo ){
				setPosicao(	posicoes[ (int)(Math.random()*posicoes.length) ] ); 
				lastCiclo = numCiclosFeitos();
			}
		}
	}
	
	public ComponentePosicaoAleatoria clone(){
		return new ComponentePosicaoAleatoria( getDecorado(), posicoes.clone());
	}
}
