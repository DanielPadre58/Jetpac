package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Esta classe representa um componente que possui v�rias anima��es
 * e permite que se escolha qual utilizar. � �til para situa��es em
 * que se tem v�rias direc��es por exemplo e se escolhe a anima��o
 * consoante a direc��o do boneco
 * @author F. S�rgio Barbosa
 *
 */
public class ComponenteMultiAnimado extends ComponenteSimples {

	private Image frames[][];
	private int   frame = 0;
	private int   nFrames = 0;
	private int   actualAnim = 0;
	private int   delay = 0;
	private int   actualDelay = 0;
	private int   nCiclos = 0;
	
	public ComponenteMultiAnimado() {
		setPosicao( new Point() );
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR );
		produzirFrames( 1, 1, img);
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posi��o no �cran
	 * @param fichImagem ficheiro onde est� a imagem do componente
	 * @param nAnims quantas anima��es o componente possui
	 * @param nFrames quantas frames tem cada anima��o
	 * @param delay factor que serve para controlar a anima��o (quanto maior menor a velocidade da anima��o)
	 * @throws IOException
	 */
	public ComponenteMultiAnimado(Point p, String fichImagem, int nAnims, int nFrames, int delay ) throws IOException {
		super( p, fichImagem );
		setPosicao( p );		
		BufferedImage img = ImageIO.read( new File( fichImagem ) );
		
		produzirFrames(nAnims, nFrames, img);
		this.delay = delay;
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posi��o no �cran
	 * @param img imagem da anima��o
	 * @param nAnims quantas anima��es o componente possui
	 * @param nFrames quantas frames tem cada anima��o
	 * @param delay factor que serve para controlar a anima��o (quanto maior menor a velocidade da anima��o)
	 */
	public ComponenteMultiAnimado(Point p, BufferedImage img, int nAnims, int nFrames, int delay ) {
		setPosicao( p );
		produzirFrames(nAnims, nFrames, img);
		this.delay = delay;
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posi��o no �cran
	 * @param img arrays de imagens a usar no componente pela ordem
	 * @param nAnims quantas anima��es o componente possui
	 * @param nFrames quantas frames tem cada anima��o
	 * @param delay factor que serve para controlar a anima��o (quanto maior menor a velocidade da anima��o)
	 */
	public ComponenteMultiAnimado(Point p, Image img[][], int delay ) {
		setPosicao( p );
		frames = img;
		//nAnims = img.length;
		nFrames = img[0].length;
		this.delay = delay;
		setFrameNum( 0 );
	}
	
	
	// vai produzir as frames apartir da imagem total
	private void produzirFrames(int nAnims, int nFrames, BufferedImage img) {
		this.nFrames = nFrames;
		frames = new Image[nAnims][ nFrames ];
		int comp = img.getWidth( ) / nFrames;
		int alt = img.getHeight() / nAnims;
		for( int a = 0; a  < nAnims; a++ ) {
			for( int i = 0; i < nFrames; i++ ){
				frames[ a ][ i ] = img.getSubimage(i*comp, a*alt, comp, alt);
			}
		}
		super.setSprite( frames[ actualAnim ][ frame ] );				
	}

	
	/**
	 * desenha este componente no ambiente gr�fico g
	 */
	public void desenhar(Graphics g) {
//		for( int k = 0; k < frames.length; k++)
//			for( int i = 0; i < frames[k].length; i++)
//				g.drawImage(frames[k][i], getPosicao().x+i*getComprimento(), getPosicao().y+k*getAltura(), null);
		
		super.desenhar(g);
		proximaFrame();
	}
	

	/**
	 * passa para a pr�xima frame
	 */
	public void proximaFrame() {
		if( estaPausa() )
			return;
		actualDelay++;
		if( actualDelay == delay ){
			actualDelay = 0;
			frame++;
			if( frame >= nFrames ) {
				frame = eCiclico()? 0: nFrames-1;
				nCiclos++;
			}
			super.setSprite( frames[ actualAnim ][ frame ] );
		}
	}

	/**
	 * come�a a anima��o numa dada frame
	 * @param f a frame onde deve come�ar a anima��o
	 */
	public void setFrameNum( int f ){
		frame = f;
		super.setSprite( frames[ actualAnim ][ frame ] );
		nCiclos = 0;
	}
	
	/**
	 * indica qual a frame que est� a ser desenhada
	 * @return a frame que est� a ser desenhada
	 */
	public int getFrameNum() {
		return frame;
	}
	
	/**
	 * Muda a anima��o para a. usar quando se pretende mudar de anima��o
	 * @param a a nova anima��o a utilizar
	 */
	public void setAnim( int a ){
		actualAnim = a;
		super.setSprite( frames[ actualAnim ][ frame ] );
		nCiclos = 0;
	}
	
	/**
	 * indica qual a anima��o que est� a ser reproduzida
	 * @return a anima��o que est� a ser reproduzida
	 */
	public int getAnim() {
		return actualAnim;
	}
	

	public int numCiclosFeitos() {
		return nCiclos;
	}
	
	public void reset() {
		nCiclos = 0;
		setFrameNum( 0 );
	}
	
	@Override
	public void inverter() {		
		Image framesInvertidas[] = new Image[ frames[actualAnim].length ];
		
		for( int i=0; i < frames[actualAnim].length; i++ )
			framesInvertidas[i] = frames[actualAnim][ frames.length - 1 - i ];
		
		frames[actualAnim] = framesInvertidas;
	}
	
	/** define o delay a aplicar � anima��o.
	 * Quanto maior o delay mais lenta a anima��o
	 * @param d o delay a aplicar.
	 */
	public void setDelay(int d) {
		delay = d;
	}
	
	/** indica quantas frames tem a anima��o deste componente
	 * @return o n�mero de frames da anima��o
	 */
	public int totalFrames() {		
		return nFrames;
	}
	
	public ComponenteMultiAnimado clone() {
		ComponenteMultiAnimado sp = new ComponenteMultiAnimado( getPosicao() != null? (Point)getPosicao().clone(): null, frames, delay );
		sp.setAngulo( getAngulo() );
		sp.setCiclico( eCiclico() );
		return sp;
	}


}
