package prof.jogos2D.image;

import java.awt.Graphics;

public class ComponenteTemporario extends ComponenteDecorador {

	private int nCiclos, cicloIni;
	
	public ComponenteTemporario( ComponenteVisual ci, int nCiclos ) {
		super( ci );
		this.nCiclos = nCiclos;
		cicloIni = nCiclos;
	}

	@Override
	public void desenhar(Graphics g) {
		if( !estaPausa() )
			nCiclos--;
		if( nCiclos > 0 ){
			super.desenhar(g);
			//img.setPosicao( getPosicao() );
			//img.desenhar(g);
		}
	}
	
	@Override
	public int numCiclosFeitos() {
		return nCiclos <= 0? 1: 0;
	}	
	
	
	@Override
	public void reset() {
		super.reset();
		nCiclos = cicloIni;
	}
	
	public ComponenteTemporario clone(){
		return new ComponenteTemporario( getDecorado().clone(), nCiclos);
	}
}
