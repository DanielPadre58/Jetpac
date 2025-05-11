package prof.jogos2D.tools;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/** Aplicação onde se podem visualizar as animações
 * 
 * @author F. Sérgio Barbosa
 */
@SuppressWarnings("serial")
public class AnimViewer extends JFrame {
	
	/** versão */
	private static final long serialVersionUID = 1L;
	
	/** manipulação das imagens */
	private BufferedImage imagem;
	
	/** Elementos gráficos */
	private JPanel painelImagem;
	private JRadioButton btSem, btSimples, btAnimada, btMulti;
	private JSpinner frames, anims, delay, animAtual;
	private ComponenteVisual visual;
	private static JFileChooser chooser; 

	
	/** Constroi a aplicação de visualização de animações
	 */
	public AnimViewer( ) {
		this( "." );
	}
	
	/** Constroi a aplicação de visualização de animações
	 * @param dirInicial diretório onde procurar as imagens
	 */
	public AnimViewer( String dirInicial) {
		super( "Visualizador de animações" );
		Objects.requireNonNull(dirInicial, "dirInical não pode ser null ");
		// se não for definido um dir inicial usa o corrente, senão usa o inicial
		chooser = new JFileChooser( dirInicial );
		setupAspeto( getContentPane() );
		pack();
		//setResizable( false );		
		setDefaultCloseOperation( JDialog.EXIT_ON_CLOSE );
		setLocationRelativeTo( null );
	}

	/** Define as opções possíveis de escolher nesta janela
	 * @param sem permite que não se escolha sem imagem
	 * @param simples permite uma imagem simples
	 * @param animada permite uma imagem animada
	 * @param multi permite uma imagem com m�ltiplas anima��es
	 */
	public void setOpcoes(boolean sem, boolean simples, boolean animada, boolean multi) {
		boolean temAtiva = false;
		btSem.setEnabled( sem );
		if( sem ) {
			temAtiva = true;
			btSem.doClick();
		}
		
		btSimples.setEnabled( simples );
		if( !temAtiva && simples) {
			temAtiva = true;
			btSimples.doClick();
		}
		
		btAnimada.setEnabled( animada );
		if( !temAtiva && animada) {
			temAtiva = true;
			btAnimada.doClick();
		}
		
		btMulti.setEnabled( multi );
		if( !temAtiva && multi) {
			temAtiva = true;
			btMulti.doClick();
		}
	}

	/** permite definir quantas animações tem de ter um dado componente visual
	 * @param n o número de animações exigidas
	 */
	public void setAnimacoes(int n ) {
		anims.getModel().setValue( n );
		anims.setEnabled( false );
	}

	/** m�todo que vai modificar a imagem a ser apresentada, porque 
	 * alguns dos seus parâmetros foram modificados
	 */
	private void modificarAnimacao() {		
		if( btSem.isSelected() && btSem.isEnabled() ) {
			visual = null;
		}
		else if( imagem == null ) {
			escolherFicheiro();
		}
		if( imagem == null ) 
			return;
		
		if( btSimples.isSelected() ){
			visual = new ComponenteSimples( imagem );
		}
		else if( btAnimada.isSelected() ){
			visual = new ComponenteAnimado( new Point(0,0), imagem, (Integer)frames.getValue(), (Integer)delay.getValue() );
		} 
		else if( btMulti.isSelected() ){
			int nAnims = (Integer)anims.getValue();
			ComponenteMultiAnimado v = new ComponenteMultiAnimado( new Point(0,0), imagem, nAnims, (Integer)frames.getValue(), (Integer)delay.getValue() );
			int anim = (Integer)animAtual.getValue();
			if( anim < nAnims )
				v.setAnim( anim );
			else
				v.setAnim( nAnims - 1 );
			visual = v;
		} 
	}

	/** apresenta uma janela onde se pode escolher o ficheiro com a imagem pretendida
	 */
	protected void escolherFicheiro( ) {
		//JFileChooser chooser = new JFileChooser("./imagens");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle( getTitle() );
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheiros de imagem", new String[] {"jpg", "jpeg", "png", "gif", "tif", "tiff"});
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {		 
		  File selFich = chooser.getSelectedFile();
		  try {
			imagem = ImageIO.read( selFich );	
			modificarAnimacao();
		  } catch (IOException e) {
			JOptionPane.showMessageDialog(null, selFich + " n�o tem uma imagem v�lida", "Erro", JOptionPane.ERROR_MESSAGE);
		  }
		}
	}

	
	/** método que criar os botões e toda a interface da janela
	 * @param janela onde colocar os componentes gráficos
	 */
	private void setupAspeto( Container janela ) {		
		//janela.setLayout( new FlowLayout(FlowLayout.LEFT, 5, 0) );
		janela.setPreferredSize( new Dimension(500,520) );
		
		JPanel comandos = new JPanel();
		// painel com as opções possiveis
		JPanel opcoesPanel = new JPanel( new GridLayout(0, 1) );
		ButtonGroup opcoes = new ButtonGroup();

		ActionListener modificarImagem = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modificarAnimacao();
			}
		};
		
		btSem = criarRadioButton( "Sem imagem", opcoes, opcoesPanel, modificarImagem );
		btSem.setSelected( true );
		btSimples = criarRadioButton( "Simples", opcoes, opcoesPanel, modificarImagem );
		btAnimada = criarRadioButton( "Animada", opcoes, opcoesPanel, modificarImagem );
		btMulti = criarRadioButton( "N animações", opcoes, opcoesPanel, modificarImagem );		
		JButton btImg = new JButton("Escolher imagem");
		btImg.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				escolherFicheiro();
			}
		});
		opcoesPanel.add( btImg );
		comandos.add( opcoesPanel );

		JPanel framesPanel = new JPanel( new GridLayout(0, 1) );
		
		ChangeListener mudancas = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				modificarAnimacao();
			}
		};
		JPanel quantidade = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		quantidade.add( new JLabel("Frames: ") );
		quantidade.add( frames = new JSpinner( new SpinnerNumberModel(1, 1, 100, 1)));
		frames.addChangeListener(mudancas);
		framesPanel.add( quantidade );
		
		quantidade = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		quantidade.add( new JLabel("Animações: ") );
		quantidade.add( anims = new JSpinner( new SpinnerNumberModel(1, 1, 100, 1)));
		anims.addChangeListener(mudancas);
		framesPanel.add( quantidade );

		JPanel controlo = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		controlo.add( new JLabel("Delay: ") );
		controlo.add( delay = new JSpinner( new SpinnerNumberModel(5, 0, 100, 1)));
		delay.addChangeListener(mudancas);
		framesPanel.add( controlo );
		
		controlo = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		controlo.add( new JLabel("Animação: ") );
		controlo.add( animAtual = new JSpinner( new SpinnerNumberModel(0, 0, 100, 1)));
		animAtual.addChangeListener(mudancas);
		framesPanel.add( controlo );
		
		controlo = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		controlo.add( new JLabel("Cor fundo: ") );
		Color cores[] = { Color.black, Color.BLUE, Color.RED, Color.GREEN,
				          Color.DARK_GRAY, Color.GRAY, Color.CYAN, Color.MAGENTA,
				          Color.ORANGE, Color.PINK, Color.YELLOW, Color.WHITE };
		JComboBox<Color> corCb = new JComboBox<Color>( cores );
		corCb.setRenderer( new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel c = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				c.setText("           ");
				c.setBackground( (Color)value );
				return c;
			}
		});
		corCb.addItemListener( e -> painelImagem.setBackground( (Color)corCb.getSelectedItem() ) );
		controlo.add( corCb );
		framesPanel.add( controlo );
		

		painelImagem = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if( visual != null )
					visual.desenhar( g );
			}
		};
		//painelImagem.setPreferredSize( new Dimension( 290,300 ) );
		painelImagem.setBackground(Color.BLACK );
		
		JPanel topoPanel = new JPanel( new GridLayout(1,0) );
		topoPanel.add( comandos );
		topoPanel.add( framesPanel );
		
		janela.add( topoPanel, BorderLayout.NORTH );
		janela.add( painelImagem, BorderLayout.CENTER );
		
		/*
		JPanel okpanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		okpanel.setPreferredSize( new Dimension( 290,40 ) );
		btOk = new JButton("Ok");
		okpanel.add( btOk );
		janela.add( okpanel );
		btOk.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if( !btSem.isSelected() && imagem == null )
					JOptionPane.showMessageDialog(null, "Imagem n�o selecionada", "Erro de configura��o", JOptionPane.ERROR_MESSAGE );
				else {
					setVisible( false );
				}
			}
		});
		*/
		
		Timer anim = new Timer(33, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painelImagem.repaint();
			}
		});
		anim.start();
	}

	/** m�todo auxiliar para criar um radio button
	 * @param texto texto a colcoar no bot�o
	 * @param opcoes grupo a que o bot�o pertence
	 * @param opcoesPanel painel onde vai ser colocado o bot�o
	 * @param modificarImagem quem chamar quando o bot�o for selecionado
	 * @return
	 */
	private JRadioButton criarRadioButton(String texto, ButtonGroup opcoes, JPanel opcoesPanel, ActionListener modificarImagem) {
		JRadioButton bt = new JRadioButton( texto );
		opcoes.add( bt );
		opcoesPanel.add( bt );
		bt.addActionListener(  modificarImagem );
		return bt;
	}
	
	public static void main(String[] args) {
		AnimViewer animViewer = new AnimViewer( );
		animViewer.setVisible( true );
	}
}