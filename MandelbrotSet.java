import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;
import javafx.scene.input.MouseEvent;
import java.text.DecimalFormat;
/**
 * The Mandelbrot Set utilizes the complex plane, so our y-values will
 * represent imaginary numbers
 */

public class MandelbrotSet extends Application
{
   private double screenSize = 800;
   private int iterations = 100;
   private Group root = new Group();
   private Color[] colors = new Color[iterations];
   private boolean clicked = false;
   private double locX;
   private double locY;
   private double rX1, rX2, rY1, rY2;
   public void start(Stage primaryStage)
   {
      generateColors(iterations);
      /**
       * Of the form: small X, big X, big Y, small Y
       */
      mandelbrot(-2, 2, 2, -2); //Usually from -2, 2, 2, -2
      /**
       * FOR MATH CLUB:
       * Normal: mandelbrot(-2, 2, 2, -2);
       * Burning Ship 1st Zoom: mandelbrot(-1.792, -1.712, 0, -0.08);
       * D'Alessio Set 1st Zoom: mandelbrot(-.636, -.113, -.036, -.546);
       * D'Alessio Set 2nd Zoom: mandelbrot(-.56801, -.551666, -.183262, -.20175);
       * D'Alessio Set 3rd Zoom: mandelbrot(-.555649, -.55468964, -.19745154, -.19835154);
       * 
       * Weisman Set 1st Zoom: mandelbrot(-.37, -.175, 1.675, 1.48);
       * Weisman Set 2nd Zoom: mandelbrot(-.198875, -.19449, 1.5024, 1.498);
       */
      
      /** .296950524, .01946304, .296975088, .0194464
       * Favorite zooms: 
       * .296, .300, .02, .016
       * .2978, .2982, .0172, .0168
       * .29678, .01964, .297136, .01932
       * -.7885, -.7785, .153, .143
       * -.786, -.784, .14763, .14563
       * Smallest zoom: .29792969, .2979381, .0184188, .01841039
       */
      Scene scene = new Scene(root, screenSize, screenSize);
      
      primaryStage.setTitle("Mandelbrot Set");
      primaryStage.setScene(scene);
      primaryStage.show();
      
      scene.setOnMouseClicked(this::showPoint);
   }
   
   public void generateColors(int range)
   {
      int r = 255;
      int g = 0;
      int b = 0;
       for (int i = 0; i < range; i++)
        {
          if (i <= range/6)
          {
             g += 255/(range/6);
          }else if (i <= range/3)
          {
              r -= 255/(range/6);
          }else if (i <= range/2)
          {
              b += 255/(range/6);   
          }else if(i <= 2*range/3)
          {
             g -= 255/(range/6);   
          }else if (i <= 5*range/6)
          {
             r += 255/(range/6);   
          }else if (i < range)
          {
             b -= 255/(range/6);    
          }
          colors[i] = Color.rgb(r, g, b);  
        }
   }
   
   public void mandelbrot(double rangeX, double rangeX2, double rangeY, double rangeY2)
   {
      root.getChildren().clear();
      createSet(rangeX, rangeX2, rangeY, rangeY2);
      createAxis(rangeX, rangeX2, rangeY, rangeY2);
      rX1 = rangeX;
      rX2 = rangeX2;
      rY1 = rangeY;
      rY2 = rangeY2;
   }
   
   public void showPoint(MouseEvent event)
   {
       double xChange = Math.abs(rX2-rX1);
       double yChange = Math.abs(rY2-rY1);
          
       double x1 = rX1+xChange*(event.getX()/screenSize);
       double y1 = rY1-yChange*(event.getY()/screenSize);
       Text point = new Text(event.getX(), event.getY(), "("+x1 + ", " + y1 + ")");
       point.setFill(Color.WHITE);
       root.getChildren().add(point);
   }
   
   /**
    * public void zoom(MouseEvent event)
   {
      if (clicked)
      {
          double xChange = Math.abs(rX2-rX1);
          double yChange = Math.abs(rY2-rY1);
          double x1 = rX1+xChange*(locX/screenSize);
          double x2 = rX1+xChange*(event.getX()/screenSize);
          double y1 = rY1-yChange*(locY/screenSize);
          double y2 = rY1-yChange*(event.getY()/screenSize);
          mandelbrot(x1, x2, y1, y2);
          clicked = false;
      }else {
          locX = event.getX();
          locY = event.getY();
          clicked = true;
      }
   }
    */
   
   public void createAxis(double rangeX, double rangeX2, double rangeY, double rangeY2)
   {
       DecimalFormat dc = new DecimalFormat("#.#####");
       Line yAxis = new Line(screenSize/2, 0, screenSize/2, screenSize);
       Line xAxis = new Line(0, screenSize/2, screenSize, screenSize/2);
       
       root.getChildren().add(yAxis);
       root.getChildren().add(xAxis);
       
       double difference = Math.abs(rangeX2 - rangeX);
       double difference2 = Math.abs(rangeY2 - rangeY);
       String change1;
       String change2;
       for (int i = 0; i<screenSize; i++)
       {
          if (i%(screenSize/10) == 0)
          {
           change1 = dc.format(rangeX+i*(difference/screenSize));
           change2 = dc.format(rangeY-i*(difference2/screenSize));
           Text marker = new Text(i, (screenSize/2)-10, change1);
           Text marker2 = new Text((screenSize/2)+10, i, change2+"i");
           root.getChildren().addAll(marker, marker2);
          }
       }
   }
   
   public void createSet(double rangeX, double rangeX2, double rangeY, double rangeY2)
   {
       double difference = Math.abs(rangeX2 - rangeX);
       double difference2 = Math.abs(rangeY2 - rangeY);
       
       for (int x = 0; x<=screenSize; x++)
       {
           for (int y = 0; y<=screenSize; y++)
           {
               ComplexNumber p = new ComplexNumber((x*(difference/screenSize))+rangeX, rangeY-(y*(difference2/screenSize)));
               int result = checkStable(p);
               if (result == 0)
               {
                  Rectangle c = new Rectangle(x, y, 1, 1);
                  c.setFill(Color.BLACK);
                  root.getChildren().add(c);
               }else {
                  Rectangle c = new Rectangle(x, y, 1, 1);
                  c.setFill(colors[result]);
                  root.getChildren().add(c);
               }
           }
       }
   }
   
   //Note that for the Mandelbrot set, our starting point is the origin,
   //And p represents our constant
   //**USE THIS METHOD TO CHANGE THE DYNAMICAL SYSTEM!!
   public int checkStable(ComplexNumber p)
   {
       ComplexNumber origin = new ComplexNumber(0, 0);
       for (int i = 1; i<iterations; i++)
       {
           origin = equation(origin, p);
           if(!origin.isStable())
           {
               return i;
           }
       }
       return 0;
   }
   
   /**Regular Mandelbrot:
    * zn1.addThis(zn.pow(2));
      zn1.addThis(constant);
    * 
    * Burning Ship: 
    * zn1.addThis(zn.weirdAbs().pow(2));
       zn1.addThis(constant);
       
       Other submissions!:
       Noah Levy: Trigify()
       
       Ale D'alessio: zn1.addThis(zn.swap().conjugate().sin());
       zn1.addThis(constant);
       
       Long Set: zn1.addThis(zn.sin().cos().sin());
       zn1.addThis(constant);
       
       Yang Set: zn1.addThis(zn.pow(3).swap().sin());
       zn1.addThis(constant);
       
       Glick Set: zn1.addThis(zn.pow((int) constant.getReal()));
       zn1.addThis(constant);
       
       Kennedy Set: zn1.addThis(zn.trigify().swap().pow(2));
       zn1.addThis(constant);
       
       Ripke-Yang Set: 
        zn1.addThis(zn.pow(3).swap().sin().pow(2));
       zn1.addThis(constant);
       
       Ripke-Levy Set:
       zn1.addThis(zn.divide(zn.trigify()).pow(2));
       zn1.addThis(constant);
       
       M-Weisman Set:
       zn1.addThis(zn.cos().sin().conjugate().pow(2));
       zn1.addThis(constant);
       
       C-Weisman Set:
       zn1.addThis(zn.sin().add(zn).trigify().swap().pow(2));
       zn1.addThis(constant);
       
       Ripke Set:
       zn1.addThis(zn.divide(zn.sin()).trigify().pow(2));
       zn1.addThis(constant.multiply(zn));
    */
   public ComplexNumber equation(ComplexNumber zn, ComplexNumber constant)
   {
       ComplexNumber zn1 = new ComplexNumber(0, 0);
       ComplexNumber w = new ComplexNumber(0, 0);
       
       zn1.addThis(zn.sin().add(zn).trigify().swap().pow(2));
       zn1.addThis(constant);
       return zn1;
   }
   
   public class ComplexNumber
{
    double real;
    double img;
    
    public ComplexNumber(double r, double i)
    {
        real = r;
        img = i;
    }
    
    public void addThis(ComplexNumber z)
    {
        real += z.getReal();
        img += z.getImg();
    }
    
    public ComplexNumber add(ComplexNumber z)
    {
        return new ComplexNumber(real + z.getReal(), img + z.getImg());
    }
    
    public ComplexNumber subtract(ComplexNumber z)
    {
        return new ComplexNumber(real - z.getReal(), img - z.getImg());
    }
    
    public void subtractThis(ComplexNumber z)
    {
        real -= z.getReal();
        img -= z.getImg();
    }
    
    public boolean isStable()
    {
        return (real > -2) && (real < 2) && (img > -2) && (img < 2);
    }
    
    public void multiplyThis(ComplexNumber z)
    {
        //a+bi * c+di = ac+adi+bci-bd
        double realN = real*z.getReal() - img*z.getImg();
        double imgN = real*z.getImg() + img*z.getReal();
        real = realN;
        img = imgN;
    }
    
    public ComplexNumber multiply(ComplexNumber z)
    {
        ComplexNumber w = new ComplexNumber(real, img);
        w.multiplyThis(z);
        return w;
    }
    
    public void divideThis(ComplexNumber z)
    {
        ComplexNumber w = this.multiply(z.conjugate());
        w.divideConstThis(z.getReal()*z.getReal()+ z.getImg()*z.getImg());
        real = w.getReal();
        img = w.getImg();
    }
    
    public ComplexNumber divide(ComplexNumber z)
    {
        ComplexNumber w = new ComplexNumber(real, img);
        w.multiplyThis(z);
        return w;
    }
    
    public ComplexNumber multiplyConst(double k)
    {
        return new ComplexNumber(real*k, img*k);
    }
    
    public void multiplyConstThis(double k)
    {
        real *= k;
        img *= k;
    }
    
    public ComplexNumber divideConst(double k)
    {
        return new ComplexNumber(real/k, img/k);
    }
    
    public void divideConstThis(double k)
    {
        real /= k;
        img /= k;
    }
    
    public void powThis(int p)
    {
        ComplexNumber z = new ComplexNumber(real, img);
        for (int i = 1; i<p; i++)
        {
            multiplyThis(z);
        }
    }
    
    public ComplexNumber pow(int p)
    {
        ComplexNumber z = new ComplexNumber(real, img);
        z.powThis(p);
        return z;
    }
    
    public double abs()
    {
        return Math.sqrt(real*real + img*img);
    }
    
    public ComplexNumber weirdAbs()
    {
        return new ComplexNumber(Math.abs(real), Math.abs(img));
    }
    
    public ComplexNumber conjugate()
    {
        return new ComplexNumber(real, -img);
    }
    
    public ComplexNumber realConjugate()
    {
        return new ComplexNumber(-real, img);
    }
    
    public ComplexNumber swap()
    {
        return new ComplexNumber(img, real);
    }
    
    public ComplexNumber trigify()
    {
        //Note java.cos/sin use radians
        return new ComplexNumber(Math.cos(real), Math.sin(img));
    }
    
    //Taylor series expansion
    public ComplexNumber sin()
    {
        ComplexNumber z = new ComplexNumber(0, 0);
        z.addThis(this);
        z.subtractThis(this.pow(3).divideConst(6));
        z.addThis(this.pow(5).divideConst(120));
        
        return z;
    }
    
    //Taylor series expansion
    public ComplexNumber cos()
    {
        ComplexNumber z = new ComplexNumber(0, 0);
        z.addThis(new ComplexNumber(1, 0));
        z.subtractThis(this.pow(2).divideConst(2));
        z.addThis(this.pow(4).divideConst(24));
        
        return z;
    }
    
    public double getReal()
    {
        return real;
    }
    
    public double getImg()
    {
        return img;
    }
}
}
