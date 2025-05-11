package prof.jogos2D.image;

import java.awt.*;
import java.awt.font.*;
import java.text.*;

public class ComponenteMensagem extends ComponenteDecorador {

	private AttributedString msg;
	
	public ComponenteMensagem( String msg, ComponenteVisual  fundo ) {
		super( fundo );
		this.msg = new AttributedString( msg );

	}
	
	@Override
	public void desenhar(Graphics g) {
		super.desenhar( g );
		//BufferedImage i = (BufferedImage)getSprite(); 
		desenhaStringFormatada( (Graphics2D)g /*i.createGraphics()*/, getComprimento()-20 );
	}

	private void desenhaStringFormatada( Graphics2D g, float width ){
		g.setColor( Color.BLACK );		
		AttributedCharacterIterator paragraph = msg.getIterator();
		int paragraphStart = paragraph.getBeginIndex();
		int paragraphEnd = paragraph.getEndIndex();
		FontRenderContext frc = g.getFontRenderContext();
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

		Point topo = getPosicao();
		
		float drawPosY = 10 + topo.y;
		// definir a posição para o 1º caracter do 1º parágrafo
		lineMeasurer.setPosition(paragraphStart);

		// enquanto não se chegar ao fim do parágrafo
		while (lineMeasurer.getPosition() < paragraphEnd) {

			TextLayout layout = lineMeasurer.nextLayout(width);

			// calcular onde se deve desenhar a seguir
			float drawPosX = 10 + topo.x;

			// mover a coordenada em y de acordo com o tamanho da letra
			drawPosY += layout.getAscent();

			// desenhar a string na posição calculada
			layout.draw(g, drawPosX, drawPosY);

			// preparar para a próxima linha
			drawPosY += layout.getDescent() + layout.getLeading();
		}

	}


	public ComponenteMensagem clone() {
		return new ComponenteMensagem( msg.toString(), getDecorado() );
	}

	public void setMensagem(String mensagem) {
		msg = new AttributedString( mensagem );
		reset();
	}
}
