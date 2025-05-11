package prof.jogos2D.map.hexagonal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Classe que representa um mapa hexagonal divido em células (tiles)
 * 
 * @author F. Sérgio Barbosa
 */
public class MapaHexagonal {
	// constante para calculos recorrentes
	private static final double SQRT3 = Math.sqrt( 3 );
	
	// variáveis do mapa
	private int comprimento, altura;    // comprimento e altura, em células, do mapa
	private Point topo;                 // coordenada do ecran onde se inicia o mapa    
	private int cellWidth, cellHeight;     			// comprimento e altura de cada célula
	private int halfCellWidth, halfCellHeight;
	private boolean cellTop;
	private MapTile []tiles;                  // as células do mapa

	// variáveis que indicam qual a informação que deve ser usada na construção do mapa 
	private boolean showGridInfo;
	private boolean showGridBorders;

	private Image fundo;                // guarda-se os terrenos e outras coisas nesta imagem, por causa da rapidez de desenho
	//private boolean fundoAlterado;      // indica se o fundo do mapa foi alterado, para se redesenhar este

	private Point topoDesenho;     // coordenada do topo do mapa
	private Point selected;        // coordenada da célula seleccionada
	
	private int vertexOffsets[][];
	private int distHoriz, distVert;
	
	private static final Point evenXNeighbours[] = { new Point(0,-1), new Point(1,-1), new Point(1,0), new Point(0,1), new Point(-1,0), new Point(-1,-1)};
	private static final Point oddXNeighbours[] = { new Point(0,-1), new Point(1,0), new Point(1,1), new Point(0,1), new Point(-1,1), new Point(-1,0) };
	
	// para criar um mapa é necessário dar as seguintes indicações:
	// comprimento e altura (em células), qual a coordenada no écran, o 
	// comprimento e a altura de cada grelha
	public MapaHexagonal( int width, int height, Point t, int cellWidth, boolean cellTop ){
		this.comprimento = width;
		this.altura = height;
		topo = t;
		topoDesenho = t;
		this.cellWidth = cellWidth;
		this.cellHeight = (int)(SQRT3 / 2 * cellWidth);
		halfCellWidth = cellWidth >> 1; // mesmo que compEdificio/2 mas mais rápido
		halfCellHeight  = cellHeight >> 1;
		this.cellTop = cellTop;
		
		// calcular os vértices das grelhas
		vertexOffsets = new int[6][2];
		for( int i = 0; i < 6; i++ ){
			vertexOffsets[i][0] = (int)(halfCellWidth * Math.cos( i*Math.PI/3 ));
			vertexOffsets[i][1] = (int)(halfCellWidth * Math.sin( i*Math.PI/3 ));
		}
		distHoriz = 3*cellWidth/4;
		distVert = (int)(Math.sqrt( 3 ) * halfCellWidth/2);
		
		showGridInfo = true;
		showGridBorders = true;

		// inicializar os vectores de fundos e ponteiros de unidades
		// são vectores lineares para evitar o uso de vectores bidimensionais
		// logo o tamanho é sempre o comprimento * altura
		int nCelulas = comprimento*altura;
		tiles = new MapTile[ nCelulas ];

		// imagem onde se vai construir o fundo do mapa, para rapidez de desenho    
		fundo = new BufferedImage( getPixelWidth(),
								   getPixelHeight(),
								   BufferedImage.TYPE_INT_RGB // imagem de 255 cores
								  );
		
		//fundoAlterado = true;   
	}
	
	
	/**
	 * converte a coordenada de ecran em coordenadas de mapa
	 * @param coordEcran cooredenada do ecran
	 * @return coordenada correspondente no mapa (mas atenção que a coordenada pode ser inválida)
	 */
	public Point convEcranMapa( Point coordEcran ){
		int x = coordEcran.x / distHoriz;
		int y = Math.floorDiv(coordEcran.y-(x&01)*halfCellHeight,cellHeight);
		
		int leftMidle = x*distHoriz+halfCellWidth/2;
		
		if( coordEcran.x < leftMidle ){
			int dx = coordEcran.x + halfCellWidth/2 - leftMidle;
			float dxn = 4*dx / (float)cellWidth;

			int centery = (y*cellHeight+halfCellHeight+(x&01)*halfCellHeight);
			int dy = coordEcran.y - centery;
			float dyn = Math.abs(dy) / (float)halfCellHeight;

			if( dyn > dxn ){
				if( dy < 0 && (x & 1) == 0)
					y--;
				if( dy > 0 && (x & 1) != 0)
					y++;
				
				x--;					
			}
		}
		return new Point(x, y);
	}
	
	/** desenha o mapa
	 * @param g ambiente gráfico onde desenhar o mapa
	 */
	public void desenhar( Graphics g ){
		//int idx=0;                                  // indice da célula
		//Point coordCell = new Point(0,0);

		// ver qual o Graphics associado ao bitmap
		Graphics2D gFundo = (Graphics2D)fundo.getGraphics( );
		gFundo.setBackground(Color.BLACK );
		gFundo.clearRect(0, 0, fundo.getWidth(null), fundo.getHeight(null) );
		
		// desenhar o mapa todo
		Point cellCenter = new Point( topo.x, topo.y );
		//int xleft = cellTop? topo.x+halfCellWidth: topo.x;
			
		// desenhar o background todo
		for( int y  = 0; y < altura; y++ ){
			cellCenter.x = topo.x+halfCellWidth;
			// percorrer todas as colunas
			for( int x = 0; x < comprimento; x++ ) {
				cellCenter.y = topo.y+distVert*2*y+(x&1)*distVert+(cellTop?distVert:0);
				
				int idx = verLinear( x, y );		
				if( tiles[ idx ] != null )
					tiles[idx].drawBackground(gFundo, cellCenter.x, cellCenter.y );

				if( selected != null && selected.x == x && selected.y == y ){
					int xs[] = new int[6];
					int ys[] = new int[6];
					gFundo.setColor( Color.cyan );
					for( int i=0; i < 6; i++){
						xs[i] = cellCenter.x + vertexOffsets[i][0];
						ys[i] = cellCenter.y + vertexOffsets[i][1];						
					}
					gFundo.fillPolygon(xs, ys, 6);
					gFundo.setColor( Color.WHITE );
				}
				if( showGridInfo ){
					String str = ""+x+","+y;
					gFundo.drawString(str, cellCenter.x, cellCenter.y );
					gFundo.drawLine( cellCenter.x-10, cellCenter.y, 
							cellCenter.x+10, cellCenter.y);
					gFundo.drawLine( cellCenter.x, cellCenter.y-10, 
							cellCenter.x, cellCenter.y+10);    
				}
				if( showGridBorders ){
					gFundo.setColor( Color.WHITE );
					for( int i = 0; i < 5; i++ )
						gFundo.drawLine( cellCenter.x + vertexOffsets[i][0], cellCenter.y + vertexOffsets[i][1],
								cellCenter.x + vertexOffsets[i+1][0], cellCenter.y + vertexOffsets[i+1][1] );
					gFundo.drawLine( cellCenter.x + vertexOffsets[5][0], cellCenter.y + vertexOffsets[5][1],
							cellCenter.x + vertexOffsets[0][0], cellCenter.y + vertexOffsets[0][1] );
				}
					
				cellCenter.x += distHoriz;
			}
		}
		
		
		// desenhar o foreground todo
		for( int y  = 0; y < altura; y++ ){
			cellCenter.x = topo.x+halfCellWidth;
			// percorrer todas as colunas
			for( int x = 0; x < comprimento; x++ ) {
				cellCenter.y = topo.y+distVert*2*y+(x&1)*distVert+(cellTop?distVert:0);
				
					int idx = verLinear( x, y );		
				if( tiles[ idx ] != null )
					tiles[idx].drawForeground(gFundo, cellCenter.x, cellCenter.y );
				cellCenter.x += distHoriz;
			}
		}

		// desenhar o bitmap de fundo
		g.drawImage( fundo, topoDesenho.x, topoDesenho.y, null );

		//fundoAlterado = false;    
	}

	/**
	 * Coloca uma célula no mapa
	 * @param c coordenada onde colocar a célula
	 * @param t célula a colocar
	 */
	public void addTile( Point c, MapTile t ){
		if( !eCoordValida( c ) )
			return;                       // se a coordenada não for válida sai

		int idx = verLinear( c );
		tiles[ idx ] = t;
		t.setPosition( c );
		t.setMap( this );
	}

	/**
	 * indica que oa célula na posição p deve aparecer como seleccionada
	 * @param p posição a seleccionar
	 */
	public void setSelected( Point p ){
		selected = p;
		//fundoAlterado = true;
	}
	
	/** retorna as bordas da célula
	 * @param cell posição da célula
	 * @return um polígono com os pontos das bordas da célula
	 */
	public Polygon getCellBounds( Point cell ){
		Polygon border = new Polygon();
		Point cellCenter = getCoordCelulaMeio( cell );
		for( int i=0; i < 6; i++ ){
			border.addPoint(cellCenter.x+vertexOffsets[i][0], cellCenter.y+vertexOffsets[i][1] );
		}
		return border;
	}

	/**
	 * retorna qual a célula seleccionada
	 */
	public Point getSelected( ) {
		return selected;
	}

	/**
	 * retorna a célula associado a uma posição
	 * @param p coordenada da célula
	 * @return a célula na posição p
	 */
	public MapTile getTile( Point p ){
		// se a coordenada não for válida sai
		if( !eCoordValida( p ) )
			return null;                       

		int idx = verLinear(p);
		return tiles[ idx ];
	}
	
	/** retorna as posições vizinhas de uma célula, 
	 * que estejam à distãncia de um dado raio
	 * 
	 * @param center posição da célula central
	 * @param raio raio dentro do qual se pretendem saber os vizinhos
	 * @return um array com todas as posições vizinhas
	 */
	public Point[] getNeighboursPositions( Point center, int raio ){
		if( raio < 1 )
			return null;
		
		Vector<Point> neig = new Vector<Point>();
		Point ps[] = getNeighboursPositions( center );
		for( Point p : ps ){
			if( p!= null )
				neig.add( p );
		}
		Vector<Point> novos = new Vector<Point>();
		for( int r = 2; r <= raio; r++ ){
			for( int i=0; i < neig.size(); i++ ){
				Point p = neig.get(i);
				Point ps2[] = getNeighboursPositions( p );
				for( Point p2 : ps2 ){
					if( p2!= null && !p2.equals(center) && !neig.contains( p2 ) && !novos.contains( p2 ) )
						novos.add( p2 );
				}
			}
			neig.addAll( novos );
			novos.clear();
		}
		return neig.toArray( new Point[neig.size()] );
	}

	/**
	 * retira do mapa a célula na coordenada p 
	 * @param p coordenada de onde retirar a célula
	 */
	public void retirarTile( Point p ){
		if( !eCoordValida( p ) )
			return;

		int idx = verLinear( p );
		tiles[idx].setMap( null );
		tiles[ idx ] = null;     // coloca o ponteiro a zero (sem célula)
	}

	/**
	 * retorna as dimensões (em pixeis) do mapa 
	 * @return as dimensões (em pixeis) do mapa
	 */
	public Dimension getPixelDimension(){
		return new Dimension( getPixelWidth( ), getPixelHeight( ));
	}

	/**
	 * retorna o comprimento (em pixeis) do mapa 
	 * @return o comprimento (em pixeis) do mapa
	 */
	public int getPixelWidth( ){
		return cellWidth + (comprimento-1) * 3*cellWidth/4;		
	}

	/**
	 * retorna a altura (em pixeis) do mapa 
	 * @return a altura (em pixeis) do mapa
	 */
	public int getPixelHeight( ){
		return cellHeight*altura + halfCellHeight;		
	}

	// funções de coordenadas
	/**
	 * indica se uma dada coordenada é valida neste mapa
	 * @param p coordenada a verificar
	 * @return se a coordenada é válida ou não
	 */
	public  boolean eCoordValida( Point p ){
		if( p.x < 0 || p.x >= comprimento ) return false;
		if( p.y < 0 || p.y >= altura ) return false;
		return true;    	
	}



	/**
	 * retorna a coordenada do écran do meio da célula coordCell
	 * @param coordCell a coordenda do mapa
	 * @return a coordenada do écran correspondente ao meio da célula 
	 */
	public Point verCoordEcranMeio( Point coordCell ){
		int y = (int)(coordCell.y);
		int xEcran = ((y & 0x01) == 0) ?    // é linha par?
				(coordCell.x * cellWidth) + cellWidth / 2 :
					(coordCell.x * cellWidth) + cellWidth ;

		int yEcran = (coordCell.y * cellHeight/2) + cellHeight /2 ;

		return new Point( topoDesenho.x + xEcran, topoDesenho.y + yEcran );		
	}

		
	/**
	 * activa/desactiva as grelhas no mapa
	 * @param on estado pretendido das grelhas
	 */
	public void setShowGrelhas( boolean on ) {
		showGridBorders = on ;
		//fundoAlterado = true;
	}

	/**
	 * devolve o estado da informação das grelhas
	 */
	public boolean getShowGrelhas( ) {
		return showGridBorders;
	}

	/**
	 * activa/desactiva a informação das células no mapa
	 * @param on estado pretendido das células
	 */    
	public void setInfoCelula( boolean on ) {
		showGridInfo = on ;
		//fundoAlterado = true;
	}

	/**
	 * devolve o estado da informação das células
	 */
	public boolean getInfoCelula( ) {
		return showGridInfo;
	}    

	/** retorna o topo da célual em pixeis
	 * @param coordCell celula em questão
	 * @return a coordenada, em pixeis, do topo da célula
	 */
	protected Point verCoordCelulaTopo( Point coordCell ){
		int y = (int)(coordCell.y);
		int xEcran = ((y & 0x01) == 0) ?             // é linha par?
				(coordCell.x * cellWidth) :
					(coordCell.x * cellWidth) + (cellWidth / 2);

				int yEcran = (coordCell.y * cellHeight/2) ;

				return new Point( xEcran, yEcran );    	
	}

	/** retorna o funco da célual em pixeis
	 * @param coordCell celula em questão
	 * @return a coordenada, em pixeis, do fundo da célula
	 */
	public Point getCoordCelulaFundo( Point coordCell ){
		return (coordCell.y & 0x01) == 0? // linha par´
				  new Point( coordCell.x * cellWidth+ halfCellWidth, (coordCell.y+1)* halfCellHeight + halfCellHeight ) :
			      new Point( (coordCell.x +1)* cellWidth, coordCell.y* halfCellHeight + cellHeight );
		
	}

	/** retorna o meio da célual em pixeis
	 * @param coordCell celula em questão
	 * @return a coordenada, em pixeis, do meio da célula
	 */
	public Point getCoordCelulaMeio( Point coordCell ){
		Point cellCenter = new Point();
		cellCenter.x = topo.x+halfCellWidth + distHoriz * coordCell.x;
		cellCenter.y = topo.y+distVert*2*coordCell.y+(coordCell.x&1)*distVert+(cellTop?distVert:0);
		return cellCenter;
	}

	private int verLinear( Point coord ){
		return verLinear( coord.x, coord.y);
	}
	
	private int verLinear( int x, int y ){
		return x + y * comprimento;
	}

	/** retorna os pontos vizinhos de uma célula
	 * @param position célula a ver dos vizinhos
	 * @return um array com os vizinhos da célula central
	 */
	public Point[] getNeighboursPositions(Point position) {
		Point neig[] = new Point[ 6 ];
		// se y for par
		if( (position.x & 0x01) == 0){
			for( int i=0; i < 6; i++ ){
				neig[i] = new Point( position.x+evenXNeighbours[i].x, position.y+evenXNeighbours[i].y);
				if( !eCoordValida(neig[i]) )
					neig[i] = null;
			}
		}
		else {
			for( int i=0; i < 6; i++ ){
				neig[i] = new Point( position.x+oddXNeighbours[i].x, position.y+oddXNeighbours[i].y);
				if( !eCoordValida(neig[i]) )
					neig[i] = null;
			}
		}
		return neig;
	}
}
