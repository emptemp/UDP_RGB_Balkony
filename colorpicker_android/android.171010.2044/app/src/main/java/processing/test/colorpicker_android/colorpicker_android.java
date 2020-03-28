package processing.test.colorpicker_android;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import hypermedia.net.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class colorpicker_android extends PApplet {




int port = 4210;
String ip ="192.168.0.25";
UDP udpTX;

byte[] payload = new byte[720];

ColorPicker cp;
ControlP5 cp5;

int clr;

public void setup() 
{
  udpTX=new UDP(this);
  udpTX.log(true);
  //size( 720, 720 );
    
  background( 80 );
  fill(0);
  rect(0, width+150, width-1,100);
  
  //size( displayWidth, displayHeight );

  frameRate( 25 );  
  cp = new ColorPicker( 10, 10, width-25, width-200, 255 );
  //cp = new ColorPicker( 10, 10, 400, 400, 255 );
   
  cp5 = new ControlP5(this);
  //text("bsp: RGB: 00FF00 (without #) // stroke between 10 - 200 // opacity < 100ish // ", 70, height-120);
  //cp5.addTextfield("HEX_COLOR").setPosition(70, height-180).setSize(150, 35).setAutoClear(false);
  //cp5.addTextfield("STROKE").setPosition(230, height-180).setSize(70, 35).setAutoClear(false);
  //cp5.addTextfield("OPACITY").setPosition(310, height-180).setSize(70, 35).setAutoClear(false);
  //cp5.addBang("OK").setPosition(390, height-180).setSize(35, 35); 
  cp5.addBang("RESET").setPosition(width-200, height-200).setSize(150, 150); 
}

public void draw ()
{

  cp.render();
  cp.drender();
}

public void RESET() {
  background( 80 );
  fill(0);
  rect(0, width+150, width-1,100);
  cp.sw = 50;
  cp.opa = 100;
}

public class ColorPicker 
{
  int x, y, w, h, c, opa = 50, sw = 40;
  PImage cpImage;
  
  public ColorPicker ( int x, int y, int w, int h, int c )
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.c = c;
    
    cpImage = new PImage( w, h );
    
    init();
  }
  
  private void init ()
  {
    // draw color.
    int cw = w - 40;
    for( int i=0; i<cw; i++ ) 
    {
      float nColorPercent = i / (float)cw;
      float rad = (-360 * nColorPercent) * (PI / 180);
      int nR = (int)(cos(rad) * 127 + 128) << 16;
      int nG = (int)(cos(rad + 2 * PI / 3) * 127 + 128) << 8;
      int nB = (int)(Math.cos(rad + 4 * PI / 3) * 127 + 128);
      int nColor = nR | nG | nB;
      
      setGradient( i, 0, 1, h/2, 0xFFFFFF, nColor );
      setGradient( i, (h/2), 1, h/2, nColor, 0x000000 );
    }
    
    // draw black/white.
    drawRect( cw, 0,   30, h/2, 0xFFFFFF );
    drawRect( cw, h/2, 30, h/2, 0 );

    
    // draw grey scale.
    for( int j=0; j<h; j++ )
    {
      int g = 255 - (int)(j/(float)(h-1) * 255 );
      drawRect( w-30, j, 30, 1, color( g, g, g ) );
    }
  }

  private void setGradient(int x, int y, float w, float h, int c1, int c2 )
  {
    float deltaR = red(c2) - red(c1);
    float deltaG = green(c2) - green(c1);
    float deltaB = blue(c2) - blue(c1);

    for (int j = y; j<(y+h); j++)
    {
      int c = color( red(c1)+(j-y)*(deltaR/h), green(c1)+(j-y)*(deltaG/h), blue(c1)+(j-y)*(deltaB/h) );
      cpImage.set( x, j, c );
    }
  }
  
  public void drawRect( int rx, int ry, int rw, int rh, int rc )
  {
    for(int i=rx; i<rx+rw; i++) 
    {
      for(int j=ry; j<ry+rh; j++) 
      {
        cpImage.set( i, j, rc );
      }
    }
  }
  
  public void render ()
  {
    image( cpImage, x, y );
    if( mousePressed &&
  mouseX >= x && 
  mouseX < x + w &&
  mouseY >= y &&
  mouseY < y + h )
    {
      c = get( mouseX, mouseY );
    }
    /*
    if( mousePressed )
    {
     stroke(c);
     strokeWeight(20);
     line(pmouseX, pmouseY, mouseX, mouseY);
    }*/
    fill( c );
    rect( x, y+h+10, 150, 150 );
  }  
  public void drender ()
  {
    if( mousePressed &&
  mouseY >= width )
    {
     pushStyle();
     stroke(c,opa);
     strokeWeight(sw);
     //fill( c );
     //point(mouseX, mouseY);
     line(pmouseX, pmouseY, mouseX, mouseY);
     popStyle();
    }
    loadPixels();
    int r = 0, g = 0, b = 0;
    for (int i=0; i<240; i++) {
      int cl = pixels[(i*(displayWidth/240))+(displayWidth*(displayWidth+200))+1];
      //color cl = pixels[(i*(720/240))+(720*661)+1];
      
      r = cl>>16&0xFF;
      g = cl>>8&0xFF;
      b = cl&0xFF;
      //print("i " + i + "r " + r + " g " + g + " b " + b + "\n");
      payload[i*3] = PApplet.parseByte(r);
      payload[i*3+1] = PApplet.parseByte(g);
      payload[i*3+2] = PApplet.parseByte(b);    
    }
    udpTX.send(payload,ip,port);
    
    //delay(100);
  }
}
  public void settings() {  size(displayWidth, displayHeight); }
}
