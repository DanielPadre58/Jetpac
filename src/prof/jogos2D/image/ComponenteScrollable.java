package prof.jogos2D.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ComponenteScrollable extends ComponenteDecorador {

	//private Rectangle view;
	private Point visible;
	private BufferedImage imagem;
	
	public ComponenteScrollable(ComponenteVisual dec) {
		this(dec, new Rectangle( dec.getBounds() ), new Point() );
		imagem = new BufferedImage( dec.getComprimento(), dec.getAltura(), BufferedImage.TYPE_4BYTE_ABGR );
		Graphics2D ge = (Graphics2D )imagem.getGraphics();		
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	    ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);	
	}
	
	public ComponenteScrollable(ComponenteVisual dec, Rectangle view) {
		this(dec, view, new Point() );
	}
	
	public ComponenteScrollable(ComponenteVisual dec, Rectangle view, Point visible) {
		super(dec);
		//this.view = view;
		this.visible = visible;
	}
	
	@Override
	public void desenhar(Graphics g) {		
		super.desenhar( imagem.getGraphics() );
		g.drawImage( imagem, visible.x, -visible.y , null );
		
		if( visible.y + 700 > super.getAltura() )
			g.drawImage( imagem, visible.x, super.getAltura() - visible.y, null );
	}
	
	public void setVisible(Point visible) {
		this.visible = visible;
	}
	
	public Point getVisible() {
		return visible;
	}

	public void scrollVertical( int dy ){
		visible.y += dy;
		if( visible.y < 0  )
			visible.y = super.getAltura();
		if( visible.y > super.getAltura() )
			visible.y = 0;
	}
}
