package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {

    public static Koparka koparka = new Koparka();
    
    private static float xrot = 0.0f, yrot = 0.0f, zrot = 0.0f;
    static float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat�o otaczaj�ce
    static float diffuseLight[] = {0.7f, 0.7f, 0.7f, 100.0f};//?wiat�o rozproszone
    static float specular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //?wiat�o odbite
    static float lightPos[] = {0.0f, 150.0f, 150.0f, 10.0f};//pozycja ?wiat�a
    public static float ob = 0.0f;
    public static float ramie1 = 45.0f;
    public static float ramie2 = -45.0f;
    public static float ramie3 = -45.0f;
    public static boolean kopanie = false;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

        //Obs�uga klawiszy strza�ek
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    xrot -= 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    xrot += 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    yrot += 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    yrot -= 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    zrot += 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    zrot -= 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    koparka.setRamie1(5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    koparka.setRamie1(-5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    koparka.setRamie2(5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    koparka.setRamie2(-5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    koparka.setRamie3(5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    koparka.setRamie3(-5.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    ob += 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    ob -= 5.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    kopanie = !kopanie;
                }
                

                //swiatlo
                if (e.getKeyCode() == KeyEvent.VK_O) {
                    lightPos[3] += 10.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    lightPos[3] -= 10.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_U) {
                    lightPos[1] += 10.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_J) {
                    lightPos[1] -= 10.0f;
                }

                if (e.getKeyCode() == KeyEvent.VK_I) {
                    ambientLight[0] += 1.0f;
                    ambientLight[1] += 1.0f;
                    ambientLight[2] += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_K) {
                    ambientLight[0] -= 1.0f;
                    ambientLight[1] -= 1.0f;
                    ambientLight[2] -= 1.0f;
                }

                if (e.getKeyCode() == KeyEvent.VK_N) {
                    diffuseLight[0] += 1.0f;
                    diffuseLight[1] += 1.0f;
                    diffuseLight[2] += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    diffuseLight[0] -= 1.0f;
                    diffuseLight[1] -= 1.0f;
                    diffuseLight[2] -= 1.0f;
                }

            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        //warto?ci sk�adowe o?wietlenia i koordynaty ?r�d�a ?wiat�a
        //(czwarty parametr okre?la odleg�o?� ?r�d�a:
        //0.0f-niesko�czona; 1.0f-okre?lona przez pozosta�e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametr�w ?r�d�a ?wiat�a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //?wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja ?wiat�a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r�d�a ?wiat�a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor�w
        //kolory b�d� ustalane za pomoc� glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiekt�w
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(100.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni�cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok� osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok� osi Y
        gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f); //rotacja wok� osi Z

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //?wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja ?wiat�a

        //las(gl, 10, 10);
        koparka.Rysuj(gl);
        
//        //podstawa
//        gl.glColor3f(0.0f, 1.0f, 0.0f);
//        float x, y, z, kat;
//        gl.glBegin(GL.GL_TRIANGLE_FAN);
//        gl.glVertex3f(0.0f, -1.0f, 0.0f); //�rodek
//        for (kat = 0.0f; kat < (2.0f * Math.PI);
//                kat += (Math.PI / 32.0f)) {
//            x = 1.5f * (float) Math.sin(kat);
//            y = 0.0f * (float) Math.cos(kat) - 1.0f;
//            z = 1.5f * (float) Math.cos(kat);
//            gl.glVertex3f(x, y, z); //kolejne punkty
//        }
//        gl.glEnd();
//
//        //bok
//        gl.glColor3f(0.0f, 0.0f, 1.0f);
//        //float x, y, z, kat;
//        gl.glBegin(GL.GL_QUAD_STRIP);
//        gl.glVertex3f(0.0f, -1.0f, 0.0f);
//        gl.glVertex3f(0.0f, 1.0f, 0.0f);
//        for (kat = 0.0f; kat < (2.0f * Math.PI);
//                kat += (Math.PI / 32.0f)) {
//            x = 1.5f * (float) Math.sin(kat);
//            z = 1.5f * (float) Math.cos(kat);
//            gl.glVertex3f(x, -1.0f, z);
//            gl.glVertex3f(0.0f, 1.0f, 0.0f);
//        }
//        gl.glEnd();
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    void las(GL gl, int x, int y) {
        gl.glPushMatrix();
        for (int i = 0; i < x; i++) {
            gl.glPushMatrix();
            for (int j = 0; j < y; j++) {
                drzewo(gl);
                gl.glTranslatef(3.0f, 0.0f, 0.0f);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0.0f, 3.0f, 0.0f);
        }
        gl.glPopMatrix();
    }

    void drzewo(GL gl) {
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, 1.4f);
        gl.glScalef(1.4f, 1.4f, 1.2f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, 1.2f);
        gl.glScalef(1.2f, 1.2f, 1.0f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, 0.8f);
        gl.glColor3f(0.55f, 0.27f, 0.08f);
        gl.glScalef(0.8f, 0.8f, 1.5f);
        walec(gl);
        gl.glPopMatrix();
    }

    void walec(GL gl) {
//wywo�ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta�ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_QUAD_STRIP);
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), 0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
//wywo�ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), -2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    

}


