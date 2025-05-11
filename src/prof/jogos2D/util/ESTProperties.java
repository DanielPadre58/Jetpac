package prof.jogos2D.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/** Classe que acrescenta métodos auxiliares para a leitura de propriedades
 * @author F. Sergio Barbosa
 */
public class ESTProperties {
	
	private Properties props = null;
	
	public ESTProperties( Reader in ) throws IOException {
		props = new Properties();		
		props.load( in );
	}
	
	/** Devolve a propriedade, em String, associada ao texto indicado 
	 * 
	 * @param cfg a configuração a retornar
	 * @return a String associada à configuração, ou null se não existir
	 */
	public String getConfig( String cfg ){
		return props.getProperty(cfg); 
	}
	
	/** Devolve a configuração, convertida em inteiro, associada ao texto indicado 
	 * 
	 * @param cfg a configuração a retornar
	 * @return o valor inteiro associado à configuração
	 */
	public int getConfigAsInt( String cfg ){
		return Integer.parseInt( props.getProperty(cfg) ); 
	}
	
	/** Devolve a configuração, convertida em long, associada ao texto indicado 
	 * 
	 * @param cfg a configuração a retornar
	 * @return o valor long associado à configuração
	 */
	public long getConfigAsLong( String cfg ){
		return Long.parseLong( props.getProperty(cfg) ); 
	}
	
	/** Devolve a configuração, convertida em float, associada ao texto indicado 
	 * 
	 * @param cfg a configuração a retornar
	 * @return o valor float associado à configuração
	 */
	public float getConfigAsFloat( String cfg ){
		return Float.parseFloat( props.getProperty(cfg) ); 
	}

	/** Devolve a configuração, convertida em double, associada ao texto indicado 
	 * 
	 * @param cfg a configuração a retornar
	 * @return o valor double associado à configuração
	 */
	public double getConfigAsDouble( String cfg ){
		return Double.parseDouble( props.getProperty(cfg) ); 
	}
	
}
