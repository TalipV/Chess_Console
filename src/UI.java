import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.geometry.Point2D;

class UI {

	Game g1;
	Boolean whiteTurn = true;
	Boolean abort = false;
	Player p;
	int figMax, figMin;
	
	public static void main(String[] args) {	
		UI ui = new UI();
		try {
			ui.buildUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Spielverwaltung mit Benutzerinteraktionen
	 * @throws IOException durch Weitergabe der Exceptions durch die Figurenbildereinlesungen
	 */
	void buildUI() throws IOException{
		
		Scanner scan = new Scanner(System.in);
		System.out.println("The first players name(white) =");
		Player p1 = new Player(scan.next(), true);
		System.out.println("The second players name(black) =");
		Player p2 = new Player(scan.next(), false);
		String str;
		List<Point2D> liste = new ArrayList<Point2D>();
		
		g1 = new Game(p1, p2);

		do{
			
			if(whiteTurn){
				p = p1;
				figMax = 15;
				figMin = 0;
			}
			else{
				p = p2;
				figMax = 31;
				figMin = 16;
			}
			
			g1.consolePrint();
			
			do{
			System.out.println("It's your turn "+p.getName()+"!");
			System.out.println();
			System.out.println("Which figure should be moved?(A5, B2, A3, ...)");
			System.out.println("(x to abort)");
			str = scan.next().toUpperCase();
			
			if(str.equals("x")){
				System.out.println("The game was aborted!");
				abort = true;
				break;
			}

			if(!str.matches("^[A-H][1-8]$")){
				System.out.println("Invalid input!");
				str = "";
			}
			else{
				Boolean notEmpty = g1.brett.selectFigur(g1.brett.displayCoordToReal(str));
				
				//Wenn Figuren des falschen Teams ausgewaehlt wurden
				if((g1.brett.getSelFig() < figMin) || (g1.brett.getSelFig() > figMax)){
					str = "";
				}
				else{
					if(notEmpty){
						liste = g1.brett.filter(g1.brett.movePossibilities(), p.getIsWhite());
						if(liste.size() == 0){
							System.out.println("The figure which was chosen can't move!");
							str = "";
						}
					}
					else{
						str = "";
					}
				}
			}
			}while(str.length() != 2);
			
			
			
			if(abort){
				break;
			}
			
			do{
				int size = liste.size();
				for(int i = 0; i < size; i++){
					System.out.println((i+1)+"\t"+g1.brett.RealToDisplayCoord(liste.get(i)));
				}
				
				System.out.println();
				System.out.println("Choose a position by entering its index!");
				str = scan.next();
				
				int input = 0;
				
				try{
					input = Integer.parseInt(str);
				}
				catch(NumberFormatException e){
					str = "";
				}
				
				if(input > size || input < 1)
					str = "";
				
			}while(str == "");		
			
		g1.brett.move(liste.get(Integer.parseInt(str)-1));	
		
		if(g1.brett.SchachMatt(!whiteTurn)){
			g1.Win(whiteTurn);
			break;
		}
		else if(g1.brett.Patt(!whiteTurn)){
			g1.Draw(!whiteTurn);
			break;
		}     
		else if(g1.brett.Patt(whiteTurn)){
			g1.Draw(whiteTurn);
			break;
		}
		
		whiteTurn = !whiteTurn;
		
		}while(true);
		
		System.out.println("-----Bye----");
		scan.close();
	}

	}