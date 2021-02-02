package randomAccess;
import javax.swing.*;
import java.awt.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.net.URL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.EventHandler;
import java.util.*;
import javax.swing.Timer;
import java.io.IOException;

public class Clock extends JFrame{
	private static final  int sizeXFrame = 240;
	private static final  int sizeYFrame = 268;
	public void go(Clock e,final RysujKolo rk){
		e.setSize(new Dimension(sizeXFrame,sizeYFrame));
		e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e.setVisible(true);
		e.add(rk);
		
		Timer t = new Timer(1000,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				rk.show();			
			}
		});
		t.start();
	}
	public static void main(String []args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				Clock e = new Clock();
				RysujKolo rk = new RysujKolo();
				e.go(e,rk);
			}
		});
	}
}

class RysujKolo extends JComponent{
	private static final  int sizeXCircle = 220; //Rozmiar Cyferblatu
	private static final  int sizeYCircle = 220; //Rozmiar Cyferblatu
	private double sizeCyfra = 13;               //Odleg³oœæ cyfr na cyferblacie od œrodka Cyferblatu
	private static final int sizeXUndo = 110;    //Œrodek wskazówek dla X
	private static final int sizeYUndo = 112;    //Œrodek wskazówek dla Y
	
	private static final long serialVersionUID = 1L;
	private Point2D.Double srodek = new Point2D.Double(sizeXUndo,sizeYUndo);//225,225
	double x,y;
	private Point2D.Double wskGodz = new Point2D.Double(srodek.getX()*0.5,srodek.getY()*0.5);
	private Point2D.Double wskMin = new Point2D.Double(srodek.getX()*0.7,srodek.getY()*0.7);
	private Point2D.Double wskSekundnika = new Point2D.Double(srodek.getX()*0.9,srodek.getY()*0.9);
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		//Ustalenie Gruboœci Cyferblatu, i wskazówek dla: godziny,minuty,sekundy
		BasicStroke gruboscCyferblatu = new BasicStroke(2);
		BasicStroke gruboscGodzinnika = new BasicStroke(4);
		BasicStroke gruboscMinutnika = new BasicStroke(2);
		BasicStroke gruboscSekundnika = new BasicStroke(0);
		
		//Rysowanie Godzin na Cyferblacie
		g2.setStroke(gruboscCyferblatu);
		g2.setColor(Color.yellow);
		g2.fill(new Ellipse2D.Double(0,0,sizeXCircle,sizeYCircle));
		  
		for(int godz = 1; godz <= 12 ; godz++){
		  g2.setColor(Color.black);
		  g2.draw(new Ellipse2D.Double(0,0,sizeXCircle,sizeYCircle));
		  g2.setColor(Color.black);
		  g2.drawString(String.valueOf(godz),(int)(srodek.getX() + (sizeXUndo - sizeCyfra)/*225*/ * Math.cos(Math.PI*godz/6 - Math.PI/2)),(int)(srodek.getY() + (sizeYUndo - sizeCyfra)/*225*/ * Math.sin(Math.PI*godz/6 - Math.PI/2)));
		}
		//Ustalenie atualnego czasu
		Date t = this.getCzas();
		//Ustawienie Sekundnika
		g2.setStroke(gruboscSekundnika);
		Line2D.Double sekundnik = new Line2D.Double(srodek, wskSekundnika);
		sekundnik = setSekundnik(sekundnik,t.getSeconds());
		g2.setColor(Color.red);
		g2.draw(sekundnik);
		//Ustawienie minutnika
		g2.setStroke(gruboscMinutnika);
		Line2D.Double minutnik = new Line2D.Double(srodek, wskMin);
		minutnik = setMinutnik(minutnik,t.getMinutes());
		g2.setColor(Color.blue);
		g2.draw(minutnik);
		//Ustawienie godzinnika
		g2.setStroke(gruboscGodzinnika);
		Line2D.Double godzinnik = new Line2D.Double(srodek, wskGodz);
		godzinnik = setGodzinnik(godzinnik,t.getHours(),t.getMinutes());
		g2.draw(godzinnik);
	} 
	
	public void show(){ this.repaint(); }
	
	public Line2D.Double setSekundnik(Line2D.Double sekundnik, int sekunda){
		double x = srodek.getX() + wskSekundnika.getX() * Math.cos(Math.PI*sekunda/30 - Math.PI/2);
		double y = srodek.getY() + wskSekundnika.getY() * Math.sin(Math.PI*sekunda/30 - Math.PI/2);
		sekundnik = new Line2D.Double(srodek, new Point2D.Double(x, y));
		return sekundnik;
	}
	
	public Line2D.Double setMinutnik(Line2D.Double minutnik,int minuta){
		double x = srodek.getX() + wskMin.getX() * Math.cos(Math.PI * minuta/30 - Math.PI/2);
		double y = srodek.getY()+wskMin.getY() * Math.sin(Math.PI * minuta/30 - Math.PI/2);
		minutnik = new Line2D.Double(srodek, new Point2D.Double(x, y));
		return minutnik;
	}
	
	public Line2D.Double setGodzinnik(Line2D.Double godzinnik,int godzina,int minuta){
		godzina = godzina * 60 + minuta;
		double x = srodek.getX() + wskGodz.getX() * Math.cos(Math.PI * godzina/360 - Math.PI/2);
		double y = srodek.getY() + wskGodz.getY() * Math.sin(Math.PI * godzina/360 - Math.PI/2);
		godzinnik = new Line2D.Double(srodek, new Point2D.Double(x, y));
		return godzinnik;
	}
	
	public Date getCzas(){
		GregorianCalendar cal = new GregorianCalendar();
		return cal.getTime();
	}
}