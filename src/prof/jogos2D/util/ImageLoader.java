package prof.jogos2D.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Esta classe existe para poupar memória no carregamento de imagens.
 * Como nos jogos muitas imagens vão ser as mesmas, os vários elementos podem partilhar essas imagens: por 
 * exemplo se existirem 10 elementos do mesmo tipo, seriam necessárias 10 imagens. Com esta classe todas esses
 * elementos partilham a mesma imagem poupando-se assim muita memória.
 * O que a classe faz é carregar uma imagem na primeira vez que esta for requisitada e armazena-a. Quando se requisitar
 * a mesma imagem, esta já não é criada mas usada a já carregada.
 * 
 * @author F. Sérgio Barbosa
 */
public class ImageLoader {
	
	private static ImageLoader iLoader = null;
	
	// guarda as imagens já armazenadas com referência ao nome
	private HashMap<String,Image> asImagens = new HashMap<String,Image>();
		
	
	private ImageLoader(){
	}
	
	/** devolve a instância única do ImageLoader
	 * @return  a instância única do ImageLoader
	 */
	public static ImageLoader getLoader(){
		if( iLoader == null )
			iLoader = new ImageLoader();
		return iLoader;
	}
	
	/**
	 * Devolve a imagem pretendida no ficheiro. Se ela ainda n�o tiver sido carregada, l�-a e carrega-a em mem�ria.
	 * Se j� tiver sido carregada, devolve a imagem previamente carregada.
	 * @param file nome do ficheiro com a imagem a carregar.
	 * @return a imagem pretendida.
	 */
	public Image getImage( String file ){
		// ver se a imagem j� est� carregada, se estiver devolve-a
		if( asImagens.containsKey( file ) )
			return asImagens.get( file );

		// carregar a imagem, armazen�-la no hasmap e devolv�-la		
		Image img = null;
		try {
			img = ImageIO.read( new File( file ) );
			asImagens.put( file, img );
			return img;			
		} catch (IOException e) {
			System.err.println("N�o li a imagem " + file + " porque " ) ;
			e.printStackTrace( System.err );
			return null;
		}		
	}
	
	/**
	 * Devolve um array de imagems pegando na imagem pretendida no ficheiro, 
	 * partindo-a em subimagens de acordo com o n�mero de imagens na horizontal e na vertical
	 * Se j� tiver sido carregada devolve as imagens previamente carregadas
	 * @param file nome do ficheiro com as imagens a carregar
	 * @param nHoriz n�mero de imagens na horizontal
	 * @param nVert n�mero de imagens na vertical
	 * @return um array de nHoriz*nVert imagens
	 */
	public Image[][] getImages( String file, int nHoriz, int nVert ){
		// para verificar se a imagem j� foi partida o nome do ficheiro � alterado
		// para nome*nHorxz*nVert
		// como nos v�rios sistemas operativos o * n�o � caracter v�lido
		// no nome dos ficheiros, nunca ir� aparecer um ficheiro com este nome
		String novoNome = file+"*"+nHoriz+"*"+nVert;
		
		// determinar o n�mero de imagens que vamos ter e criar um array para aas armazenar
		int nImagens = nHoriz*nVert;
		Image imagens[][] = new Image[nVert][nHoriz];
			
		// ver se a imagem j� est� carregada, se estiver devolve as v�rias imagens j� criadas
		if( asImagens.containsKey( novoNome ) ){
			for( int i = 0; i < nImagens; i++ ){
				String nome = novoNome+"*"+i;
				imagens[i/nHoriz][i%nHoriz] = asImagens.get( nome );
			}
					
			return imagens;				
		}

		// carregar a imagem, dividi-la em v�rias, armazen�-las no hasmap e devolv�-las		
		BufferedImage img = null;		
		try {
			img = ImageIO.read( new File( file ) );			 
			asImagens.put( novoNome, null ); // marcar que esta imagem j� foi carregada com estas divis�es
			int comp = img.getWidth() / nHoriz;			
			int alt = img.getHeight() / nVert;
			// fazer as divis�es
			int imgIdx = 0;
			int y = 0;
			for( int i = 0; i < nVert; i++ ){
				int x = 0;
				for( int k = 0; k < nHoriz; k++ ){					
					Image subimg = img.getSubimage(x, y, comp, alt);
					x += comp;
					// colocar no hashmap e devolver
					asImagens.put( novoNome+"*"+imgIdx, subimg);
					imagens[i][k] = subimg;
					imgIdx++;					
				}
				y += alt;
			}
			return imagens;
			
		} catch (IOException e) {
			System.err.println("n�o li a imagem " + file );
			return null;
		}		
	}
	
}
